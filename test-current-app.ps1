# Test the currently running application
Write-Host "Testing Current Application..." -ForegroundColor Green
Write-Host "==============================" -ForegroundColor Green

$baseUrl = "http://localhost:8080"
$headers = @{ "Content-Type" = "application/json" }

# Test 1: Health Check
Write-Host "`n[TEST 1] Health Check..." -ForegroundColor Yellow
try {
    $health = Invoke-RestMethod -Uri "$baseUrl/api/pr-analyzer/health" -Method GET
    Write-Host "✅ Health: $health" -ForegroundColor Green
} catch {
    Write-Host "❌ Health check failed. Make sure app is running on port 8080" -ForegroundColor Red
    exit 1
}

# Test 2: Try with a simple public repository
Write-Host "`n[TEST 2] Testing with public repository..." -ForegroundColor Yellow

$testBody = @{
    prUrl = "https://github.com/octocat/Hello-World/pull/1"
    requirements = "Generate a similar API for gainLossAcct in fixed-assets.asset.s1.schema.yaml"
} | ConvertTo-Json

Write-Host "Request body:" -ForegroundColor Cyan
Write-Host $testBody -ForegroundColor White

try {
    Write-Host "`nCalling API..." -ForegroundColor Gray
    $response = Invoke-RestMethod -Uri "$baseUrl/api/pr-analyzer/analyze-and-generate" -Method POST -Body $testBody -Headers $headers -TimeoutSec 60
    
    Write-Host "✅ SUCCESS! API is working!" -ForegroundColor Green
    Write-Host "Success: $($response.success)" -ForegroundColor Cyan
    
    if ($response.prAnalysis) {
        Write-Host "PR Title: $($response.prAnalysis.title)" -ForegroundColor White
        Write-Host "Files Changed: $($response.prAnalysis.files.Count)" -ForegroundColor White
    }
    
    if ($response.extractedPatterns) {
        Write-Host "Method Patterns Found: $($response.extractedPatterns.methodPatterns.Count)" -ForegroundColor White
        Write-Host "Variable Patterns Found: $($response.extractedPatterns.variablePatterns.Count)" -ForegroundColor White
    }
    
    if ($response.generatedCode) {
        Write-Host "`nGenerated Code Preview:" -ForegroundColor Cyan
        $preview = $response.generatedCode.Substring(0, [Math]::Min(300, $response.generatedCode.Length))
        Write-Host $preview -ForegroundColor White
        Write-Host "..." -ForegroundColor Gray
    }
    
    Write-Host "`n🎉 PERFECT! Your application is working!" -ForegroundColor Green
    Write-Host "Use this exact request for your demo!" -ForegroundColor Yellow
    
} catch {
    Write-Host "❌ API call failed: $($_.Exception.Message)" -ForegroundColor Red
    
    if ($_.Exception.Response) {
        try {
            $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
            $responseBody = $reader.ReadToEnd()
            Write-Host "Error Response: $responseBody" -ForegroundColor Yellow
            
            # Check if it's a GitHub API issue
            if ($responseBody -like "*github*" -or $responseBody -like "*401*" -or $responseBody -like "*403*") {
                Write-Host "`n💡 This looks like a GitHub API access issue." -ForegroundColor Cyan
                Write-Host "Try these solutions:" -ForegroundColor Yellow
                Write-Host "1. Use mock mode: start-mock.bat" -ForegroundColor White
                Write-Host "2. Check your GitHub token in .env file" -ForegroundColor White
                Write-Host "3. Try a different public repository" -ForegroundColor White
            }
        } catch {
            Write-Host "Could not read error details" -ForegroundColor Gray
        }
    }
}

Write-Host "`n📋 Summary:" -ForegroundColor Green
Write-Host "- App is running on: http://localhost:8080" -ForegroundColor White
Write-Host "- Swagger UI: http://localhost:8080/swagger-ui.html" -ForegroundColor White
Write-Host "- For guaranteed demo: use start-mock.bat" -ForegroundColor White
