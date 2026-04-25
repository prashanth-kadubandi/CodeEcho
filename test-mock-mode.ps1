# Test script for mock mode
Write-Host "Testing Mock Mode..." -ForegroundColor Green

# Test the main endpoint with mock data
$body = @{
    prUrl = "https://github.com/example/repo/pull/123"
    requirements = "Generate a product management controller similar to the user controller"
} | ConvertTo-Json

$headers = @{
    "Content-Type" = "application/json"
}

try {
    Write-Host "Calling analyze-and-generate endpoint..." -ForegroundColor Yellow
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/pr-analyzer/analyze-and-generate" -Method POST -Body $body -Headers $headers
    
    Write-Host "Response received successfully!" -ForegroundColor Green
    Write-Host "PR Title: $($response.prAnalysis.title)" -ForegroundColor Cyan
    Write-Host "Generated Code Preview:" -ForegroundColor Cyan
    Write-Host $response.generatedCode.Substring(0, [Math]::Min(500, $response.generatedCode.Length)) -ForegroundColor White
    Write-Host "..." -ForegroundColor White
    
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "This is expected if not running in mock mode or if API keys are not set" -ForegroundColor Yellow
}
