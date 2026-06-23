package com.taxhelper.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private EditText urlInput;
    private Button saveButton;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        urlInput = findViewById(R.id.urlInput);
        saveButton = findViewById(R.id.saveButton);

        prefs = getSharedPreferences("taxhelper", MODE_PRIVATE);

        // Load current URL
        String currentUrl = prefs.getString("server_url", "");
        urlInput.setText(currentUrl);

        saveButton.setOnClickListener(v -> {
            String url = urlInput.getText().toString().trim();
            if (!url.isEmpty()) {
                // Ensure scheme
                if (!url.startsWith("http")) {
                    url = "http://" + url;
                }
                // Remove trailing slash
                if (url.endsWith("/")) {
                    url = url.substring(0, url.length() - 1);
                }
                prefs.edit().putString("server_url", url).apply();
                Toast.makeText(this, "✅ Saved! Go back to connect.", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Please enter a URL", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
