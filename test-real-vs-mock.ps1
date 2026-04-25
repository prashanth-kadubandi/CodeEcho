# Test Real vs Mock Mode
Write-Host "Testing Real vs Mock Mode Differences" -ForegroundColor Green
Write-Host "=====================================" -ForegroundColor Green

$baseUrl = "http://localhost:8080"
$headers = @{ "Content-Type" = "application/json" }

# Test with your private repo
$privateRepoTest = @{
    prUrl = "https://github.com/intacct/ia-ds-fa/pull/2360"
    requirements = "Generate a similar API for gainLossAcct in fixed-assets.asset.s1.schema.yaml"
} | ConvertTo-Json

Write-Host "`n[TEST 1] Private Repository (Real Mode)" -ForegroundColor Yellow
Write-Host "URL: https://github.com/intacct/ia-ds-fa/pull/2360" -ForegroundColor Cyan

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/pr-analyzer/analyze-and-generate" -Method POST -Body $privateRepoTest -Headers $headers -TimeoutSec 30
    
    Write-Host "✅ SUCCESS! Private repo accessible!" -ForegroundColor Green
    Write-Host "PR Title: $($response.prAnalysis.title)" -ForegroundColor White
    Write-Host "Files Changed: $($response.prAnalysis.files.Count)" -ForegroundColor White
    
    # Check if this looks like real data
    if ($response.prAnalysis.title -like "*User Management*" -or $response.prAnalysis.title -like "*Add User*") {
        Write-Host "⚠️  WARNING: This looks like mock data!" -ForegroundColor Yellow
    } else {
        Write-Host "✅ This appears to be real PR data!" -ForegroundColor Green
    }
    
} catch {
    Write-Host "❌ FAILED (Expected for private repo): $($_.Exception.Message)" -ForegroundColor Red
    
    if ($_.Exception.Response) {
        try {
            $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
            $responseBody = $reader.ReadToEnd()
            Write-Host "Error details: $responseBody" -ForegroundColor Yellow
            
            if ($responseBody -like "*401*" -or $responseBody -like "*403*" -or $responseBody -like "*Not Found*") {
                Write-Host "💡 This confirms real mode - GitHub API returned authentication/access error" -ForegroundColor Cyan
            }
        } catch {
            Write-Host "Could not read error details" -ForegroundColor Gray
        }
    }
}

# Test with public repo
$publicRepoTest = @{
    prUrl = "https://github.com/octocat/Hello-World/pull/1"
    requirements = "Generate a similar API for gainLossAcct"
} | ConvertTo-Json

Write-Host "`n[TEST 2] Public Repository (Real Mode)" -ForegroundColor Yellow
Write-Host "URL: https://github.com/octocat/Hello-World/pull/1" -ForegroundColor Cyan

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/pr-analyzer/analyze-and-generate" -Method POST -Body $publicRepoTest -Headers $headers -TimeoutSec 30
    
    Write-Host "✅ SUCCESS! Public repo works!" -ForegroundColor Green
    Write-Host "PR Title: $($response.prAnalysis.title)" -ForegroundColor White
    Write-Host "Files Changed: $($response.prAnalysis.files.Count)" -ForegroundColor White
    
    # Check if this looks like real data
    if ($response.prAnalysis.title -like "*User Management*" -or $response.prAnalysis.title -like "*Add User*") {
        Write-Host "⚠️  WARNING: This looks like mock data!" -ForegroundColor Yellow
    } else {
        Write-Host "✅ This appears to be real PR data!" -ForegroundColor Green
    }
    
} catch {
    Write-Host "❌ FAILED: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "This suggests an issue with GitHub API access or token" -ForegroundColor Yellow
}

Write-Host "`n📋 Summary:" -ForegroundColor Green
Write-Host "- If private repo fails but public works = Real mode working correctly" -ForegroundColor White
Write-Host "- If both return same 'User Management' data = Still in mock mode" -ForegroundColor White
Write-Host "- If both fail = GitHub token or API issue" -ForegroundColor White

Write-Host "`n🎭 For Demo:" -ForegroundColor Yellow
Write-Host "- Mock mode: Reliable, always works, shows concept" -ForegroundColor White
Write-Host "- Real mode: Impressive, shows actual GitHub integration" -ForegroundColor White
Write-Host "- Best: Start with mock, then switch to real for wow factor!" -ForegroundColor White
