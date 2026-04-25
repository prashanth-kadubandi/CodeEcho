# ReflexAI PR Analyzer - API Keys Setup Script
Write-Host "ReflexAI PR Analyzer - API Keys Setup" -ForegroundColor Green
Write-Host "======================================" -ForegroundColor Green
Write-Host ""

# Check if .env file exists
if (Test-Path ".env") {
    Write-Host "[OK] .env file found!" -ForegroundColor Green

    # Read current .env content
    $envContent = Get-Content ".env" -Raw

    if ($envContent -match "your-github-personal-access-token-here") {
        Write-Host "[WARN] GitHub token not set yet" -ForegroundColor Yellow
        Write-Host "   Please edit .env file and replace the placeholder with your actual GitHub PAT" -ForegroundColor White
        Write-Host "   Get it from: https://github.com/settings/tokens" -ForegroundColor Cyan
    } else {
        Write-Host "[OK] GitHub token appears to be set" -ForegroundColor Green
    }

    if ($envContent -match "your-openai-api-key-here") {
        Write-Host "[WARN] OpenAI API key not set yet" -ForegroundColor Yellow
        Write-Host "   Please edit .env file and replace the placeholder with your actual OpenAI key" -ForegroundColor White
        Write-Host "   Get it from: https://platform.openai.com/api-keys" -ForegroundColor Cyan
    } else {
        Write-Host "[OK] OpenAI API key appears to be set" -ForegroundColor Green
    }

} else {
    Write-Host "[ERROR] .env file not found!" -ForegroundColor Red
    Write-Host "   Run this script from the project root directory" -ForegroundColor White
}

Write-Host ""
Write-Host "Next Steps:" -ForegroundColor Yellow
Write-Host "1. Edit the .env file with your actual API keys" -ForegroundColor White
Write-Host "2. Run: mvn clean compile" -ForegroundColor White
Write-Host "3. Run: mvn spring-boot:run" -ForegroundColor White
Write-Host "4. Visit: http://localhost:8080/swagger-ui.html" -ForegroundColor White
Write-Host ""
Write-Host "For demo mode (no keys needed): mvn spring-boot:run -Dspring.profiles.active=mock" -ForegroundColor Cyan
