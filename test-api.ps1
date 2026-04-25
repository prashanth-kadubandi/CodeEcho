# Test script for ReflexAI PR Analyzer
Write-Host "Testing ReflexAI PR Analyzer API..." -ForegroundColor Green
Write-Host "====================================" -ForegroundColor Green

$baseUrl = "http://localhost:8080"

# Test 1: Health Check
Write-Host "`n[TEST 1] Health Check..." -ForegroundColor Yellow
try {
    $health = Invoke-RestMethod -Uri "$baseUrl/api/pr-analyzer/health" -Method GET
    Write-Host "✅ Health: $health" -ForegroundColor Green
} catch {
    Write-Host "❌ Health check failed: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "Make sure the application is running on port 8080" -ForegroundColor Yellow
    exit 1
}

# Test 2: URL Parsing Test
Write-Host "`n[TEST 2] Testing PR URL parsing..." -ForegroundColor Yellow
$testUrl = "https://github.com/intacct/ia-ds-fa/pull/2360/files"
try {
    $urlTest = Invoke-RestMethod -Uri "$baseUrl/api/pr-analyzer/test-url?prUrl=$([System.Web.HttpUtility]::UrlEncode($testUrl))" -Method GET
    Write-Host "✅ URL Test Result:" -ForegroundColor Green
    Write-Host $urlTest -ForegroundColor White
} catch {
    Write-Host "❌ URL test failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 3: Mock Mode Test
Write-Host "`n[TEST 3] Testing with mock data..." -ForegroundColor Yellow
$mockBody = @{
    prUrl = "https://github.com/example/repo/pull/123"
    requirements = "Generate a similar API for gainLossAcct"
} | ConvertTo-Json

$headers = @{ "Content-Type" = "application/json" }

try {
    Write-Host "Calling main API endpoint..." -ForegroundColor Cyan
    $response = Invoke-RestMethod -Uri "$baseUrl/api/pr-analyzer/analyze-and-generate" -Method POST -Body $mockBody -Headers $headers
    
    Write-Host "✅ API Response received!" -ForegroundColor Green
    Write-Host "Success: $($response.success)" -ForegroundColor Cyan
    Write-Host "PR Title: $($response.prAnalysis.title)" -ForegroundColor Cyan
    Write-Host "Patterns Found: $($response.extractedPatterns.methodPatterns.Count) method patterns" -ForegroundColor Cyan
    Write-Host "Generated Code Preview:" -ForegroundColor Cyan
    Write-Host $response.generatedCode.Substring(0, [Math]::Min(200, $response.generatedCode.Length)) -ForegroundColor White
    Write-Host "..." -ForegroundColor White
    
} catch {
    Write-Host "❌ API call failed: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "Response: $responseBody" -ForegroundColor Yellow
    }
}

Write-Host "`n🎯 Next Steps:" -ForegroundColor Green
Write-Host "1. If tests pass, try your real PR URL: https://github.com/intacct/ia-ds-fa/pull/2360" -ForegroundColor White
Write-Host "2. Make sure your GitHub token has access to the intacct/ia-ds-fa repository" -ForegroundColor White
Write-Host "3. Check the Swagger UI at: http://localhost:8080/swagger-ui.html" -ForegroundColor White
