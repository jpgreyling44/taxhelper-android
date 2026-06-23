@echo off
REM Gradle wrapper for Windows
cd /d "%~dp0"
if exist "%JAVA_HOME%\bin\java.exe" (
    set JAVA_EXE="%JAVA_HOME%\bin\java.exe"
) else (
    set JAVA_EXE=java
)

%JAVA_EXE% -classpath "%~dp0gradle\wrapper\gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain %*
