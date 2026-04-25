# Test Different AI Providers
Write-Host "Testing AI Providers for CodeEcho" -ForegroundColor Green
Write-Host "=============================================" -ForegroundColor Green

# Function to update .env file
function Update-EnvProvider {
    param($provider)
    
    $envContent = Get-Content ".env" -Raw
    $envContent = $envContent -replace "AI_PROVIDER=.*", "AI_PROVIDER=$provider"
    Set-Content ".env" -Value $envContent
    
    Write-Host "✅ Updated .env to use: $provider" -ForegroundColor Green
}

# Function to test API
function Test-API {
    param($providerName)
    
    Write-Host "`n[TESTING] $providerName Provider" -ForegroundColor Yellow
    Write-Host "================================" -ForegroundColor Yellow
    
    $baseUrl = "http://localhost:8080"
    $headers = @{ "Content-Type" = "application/json" }
    
    $testBody = @{
        prUrl = "https://github.com/microsoft/vscode/pull/266299"
        requirements = "Generate a similar REST controller for user management"
    } | ConvertTo-Json
    
    try {
        Write-Host "Making API call..." -ForegroundColor Cyan
        $response = Invoke-RestMethod -Uri "$baseUrl/api/pr-analyzer/analyze-and-generate" -Method POST -Body $testBody -Headers $headers -TimeoutSec 60
        
        if ($response.success) {
            Write-Host "🎉 SUCCESS with $providerName!" -ForegroundColor Green
            Write-Host "Generated Code Length: $($response.generatedCode.Length) characters" -ForegroundColor White
            
            # Show a preview
            if ($response.generatedCode -and $response.generatedCode.Length -gt 0) {
                Write-Host "`nGenerated Code Preview:" -ForegroundColor Cyan
                $preview = $response.generatedCode.Substring(0, [Math]::Min(200, $response.generatedCode.Length))
                Write-Host $preview -ForegroundColor White
                Write-Host "..." -ForegroundColor Gray
            }
            
            return $true
        } else {
            Write-Host "❌ Failed: $($response.errorMessage)" -ForegroundColor Red
            return $false
        }
        
    } catch {
        Write-Host "❌ Error: $($_.Exception.Message)" -ForegroundColor Red
        
        if ($_.Exception.Message -like "*quota*" -or $_.Exception.Message -like "*billing*") {
            Write-Host "   → Billing/quota issue" -ForegroundColor Yellow
        } elseif ($_.Exception.Message -like "*401*" -or $_.Exception.Message -like "*403*") {
            Write-Host "   → Authentication issue" -ForegroundColor Yellow
        } else {
            Write-Host "   → Other error - check logs" -ForegroundColor Yellow
        }
        
        return $false
    }
}

Write-Host "🔧 Testing AI Providers..." -ForegroundColor Cyan
Write-Host "Make sure your application is running on port 8080" -ForegroundColor White
Write-Host ""

# Test GitHub Copilot
Update-EnvProvider "github"
Write-Host "Restart your application with: mvn spring-boot:run" -ForegroundColor Yellow
Write-Host "Then press Enter to test GitHub Copilot..." -ForegroundColor White
Read-Host

$githubSuccess = Test-API "GitHub Copilot"

# Test OpenAI (if user wants to)
Write-Host "`nDo you want to test OpenAI? (y/n): " -ForegroundColor Yellow -NoNewline
$testOpenAI = Read-Host

if ($testOpenAI -eq "y" -or $testOpenAI -eq "Y") {
    Update-EnvProvider "openai"
    Write-Host "Restart your application with: mvn spring-boot:run" -ForegroundColor Yellow
    Write-Host "Then press Enter to test OpenAI..." -ForegroundColor White
    Read-Host
    
    $openaiSuccess = Test-API "OpenAI"
}

# Summary
Write-Host "`n📋 SUMMARY:" -ForegroundColor Green
Write-Host "==========" -ForegroundColor Green

if ($githubSuccess) {
    Write-Host "✅ GitHub Copilot: WORKING" -ForegroundColor Green
    Write-Host "   → Best for proprietary code analysis" -ForegroundColor White
    Write-Host "   → Uses your existing GitHub token" -ForegroundColor White
} else {
    Write-Host "❌ GitHub Copilot: FAILED" -ForegroundColor Red
    Write-Host "   → May need Copilot Business/Enterprise" -ForegroundColor Yellow
    Write-Host "   → Check if API access is enabled" -ForegroundColor Yellow
}

if ($testOpenAI -eq "y" -or $testOpenAI -eq "Y") {
    if ($openaiSuccess) {
        Write-Host "✅ OpenAI: WORKING" -ForegroundColor Green
    } else {
        Write-Host "❌ OpenAI: FAILED (likely billing/quota)" -ForegroundColor Red
    }
}

Write-Host "`n🎯 RECOMMENDATIONS:" -ForegroundColor Yellow
Write-Host "==================" -ForegroundColor Yellow

if ($githubSuccess) {
    Write-Host "✅ Use GitHub Copilot for your demo!" -ForegroundColor Green
    Write-Host "   → Set AI_PROVIDER=github in .env" -ForegroundColor White
    Write-Host "   → Perfect for proprietary code analysis" -ForegroundColor White
} else {
    Write-Host "🎭 Use Mock Mode for reliable demo:" -ForegroundColor Cyan
    Write-Host "   → cmd /c start-mock.bat" -ForegroundColor White
    Write-Host "   → Always works, no API dependencies" -ForegroundColor White
}

Write-Host "`n🚀 Your application now supports:" -ForegroundColor Green
Write-Host "- GitHub Copilot (best for code)" -ForegroundColor White
Write-Host "- OpenAI ChatGPT (general purpose)" -ForegroundColor White
Write-Host "- Azure OpenAI (enterprise)" -ForegroundColor White
Write-Host "- Mock Mode (demo-safe)" -ForegroundColor White
