@echo off
echo ============================================
echo CodeEcho Security Check for Hackathon Upload
echo ============================================
echo.

echo 1. Checking for API keys in source code...
echo.

echo Searching for OpenAI keys (sk-):
findstr /s /i "sk-" src\* 2>nul
if %errorlevel% equ 0 (
    echo ❌ FOUND OpenAI keys in source code!
) else (
    echo ✅ No OpenAI keys found in source code
)
echo.

echo Searching for GitHub tokens:
findstr /s /i "ghp_" src\* 2>nul
findstr /s /i "gho_" src\* 2>nul
findstr /s /i "github_pat_" src\* 2>nul
if %errorlevel% equ 0 (
    echo ❌ FOUND GitHub tokens in source code!
) else (
    echo ✅ No GitHub tokens found in source code
)
echo.

echo 2. Checking git status...
echo.
git status --porcelain | findstr ".env"
if %errorlevel% equ 0 (
    echo ❌ WARNING: .env file is tracked by git!
) else (
    echo ✅ .env file is properly ignored
)
echo.

echo 3. Verifying required files exist...
echo.
if exist .env.example (
    echo ✅ .env.example template exists
) else (
    echo ❌ Missing .env.example template
)

if exist HACKATHON_SETUP.md (
    echo ✅ HACKATHON_SETUP.md guide exists
) else (
    echo ❌ Missing HACKATHON_SETUP.md guide
)

if exist start-mock.bat (
    echo ✅ start-mock.bat demo script exists
) else (
    echo ❌ Missing start-mock.bat demo script
)
echo.

echo ============================================
echo Security check complete!
echo ============================================
pause
