package com.taxhelper.app;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;
    private TextView statusText;
    private SharedPreferences prefs;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        statusText = findViewById(R.id.statusText);

        prefs = getSharedPreferences("taxhelper", MODE_PRIVATE);

        // WebView setup
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setAllowFileAccess(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        // Pull-to-refresh
        swipeRefresh.setOnRefreshListener(() -> webView.reload());
        swipeRefresh.setColorSchemeColors(
            getColor(android.R.color.holo_blue_bright),
            getColor(android.R.color.holo_green_light),
            getColor(android.R.color.holo_orange_light)
        );

        // WebView client
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                statusText.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // Allow self-signed certs for local dev
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                statusText.setVisibility(View.VISIBLE);
                statusText.setText("⚠️ Connection error\nCheck your server is running");
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
            }
        });

        // Load the saved URL or show settings
        loadServerUrl();
    }

    private void loadServerUrl() {
        String url = prefs.getString("server_url", "");
        if (url.isEmpty()) {
            // First run — prompt for URL
            promptForUrl();
        } else {
            webView.loadUrl(url);
        }
    }

    private void promptForUrl() {
        String savedUrl = prefs.getString("server_url", "");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("🔗 Connect to TaxHelper");
        builder.setMessage("Enter your TaxHelper server URL:\n\n" +
            "☁️ Cloud: https://your-app.onrender.com\n" +
            "🏠 Local: http://192.168.1.42:8010");

        final View input = getLayoutInflater().inflate(R.layout.dialog_url, null);
        TextView urlInput = input.findViewById(R.id.urlInput);
        urlInput.setText(savedUrl.isEmpty() ? "http://" : savedUrl);
        builder.setView(input);

        builder.setPositiveButton("Connect", (dialog, which) -> {
            String enteredUrl = urlInput.getText().toString().trim();
            if (!enteredUrl.isEmpty()) {
                // Ensure URL has scheme
                if (!enteredUrl.startsWith("http")) {
                    enteredUrl = "http://" + enteredUrl;
                }
                // Remove trailing slash
                if (enteredUrl.endsWith("/")) {
                    enteredUrl = enteredUrl.substring(0, enteredUrl.length() - 1);
                }
                prefs.edit().putString("server_url", enteredUrl).apply();
                webView.loadUrl(enteredUrl);
            } else {
                Toast.makeText(this, "Please enter a URL", Toast.LENGTH_SHORT).show();
                promptForUrl();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            Toast.makeText(this, "Set URL in Settings ⚙️", Toast.LENGTH_LONG).show();
        });

        builder.setNeutralButton("⚙️ Settings", (dialog, which) -> {
            openSettings();
        });

        builder.show();
    }

    private void openSettings() {
        startActivity(new android.content.Intent(this, SettingsActivity.class));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload if URL might have changed
        String currentUrl = webView.getUrl();
        String savedUrl = prefs.getString("server_url", "");
        if (currentUrl == null || (savedUrl.isEmpty() && !currentUrl.equals(savedUrl))) {
            loadServerUrl();
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
