# Find a working public PR for demo
Write-Host "Finding Working Public PRs for Demo..." -ForegroundColor Green
Write-Host "====================================" -ForegroundColor Green

$baseUrl = "http://localhost:8080"
$headers = @{ "Content-Type" = "application/json" }

# Test different repositories with low PR numbers that are likely to exist
$testPRs = @(
    @{ repo = "octocat/Hello-World"; pr = 1; desc = "The original GitHub test repo" },
    @{ repo = "microsoft/vscode"; pr = 100; desc = "VS Code - very active" },
    @{ repo = "microsoft/vscode"; pr = 500; desc = "VS Code - medium PR" },
    @{ repo = "facebook/react"; pr = 100; desc = "React - popular framework" },
    @{ repo = "facebook/react"; pr = 500; desc = "React - medium PR" },
    @{ repo = "nodejs/node"; pr = 100; desc = "Node.js - core runtime" },
    @{ repo = "torvalds/linux"; pr = 1; desc = "Linux kernel (if it has PRs)" },
    @{ repo = "microsoft/TypeScript"; pr = 100; desc = "TypeScript compiler" },
    @{ repo = "angular/angular"; pr = 100; desc = "Angular framework" },
    @{ repo = "vuejs/vue"; pr = 100; desc = "Vue.js framework" }
)

foreach ($test in $testPRs) {
    $prUrl = "https://github.com/$($test.repo)/pull/$($test.pr)"
    Write-Host "`n[TESTING] $($test.desc)" -ForegroundColor Yellow
    Write-Host "URL: $prUrl" -ForegroundColor Cyan
    
    $body = @{
        prUrl = $prUrl
        requirements = "Generate a similar REST controller for user management"
    } | ConvertTo-Json
    
    try {
        $response = Invoke-RestMethod -Uri "$baseUrl/api/pr-analyzer/analyze-and-generate" -Method POST -Body $body -Headers $headers -TimeoutSec 30
        
        if ($response.success) {
            Write-Host "🎉 SUCCESS! Found working PR!" -ForegroundColor Green
            Write-Host "PR Title: $($response.prAnalysis.title)" -ForegroundColor White
            Write-Host "Files Changed: $($response.prAnalysis.files.Count)" -ForegroundColor White
            Write-Host "Generated Code Length: $($response.generatedCode.Length) characters" -ForegroundColor White
            
            Write-Host "`n✅ USE THIS FOR YOUR DEMO:" -ForegroundColor Green
            Write-Host $body -ForegroundColor White
            
            # Show a preview of the generated code
            if ($response.generatedCode -and $response.generatedCode.Length -gt 0) {
                Write-Host "`nGenerated Code Preview:" -ForegroundColor Cyan
                $preview = $response.generatedCode.Substring(0, [Math]::Min(200, $response.generatedCode.Length))
                Write-Host $preview -ForegroundColor White
                Write-Host "..." -ForegroundColor Gray
            }
            
            break  # Found a working one, stop testing
        } else {
            Write-Host "❌ Failed: $($response.errorMessage)" -ForegroundColor Red
        }
        
    } catch {
        Write-Host "❌ Error: $($_.Exception.Message)" -ForegroundColor Red
        
        # Check if it's a 404 (PR doesn't exist) vs other errors
        if ($_.Exception.Message -like "*404*") {
            Write-Host "   → PR #$($test.pr) doesn't exist in this repo" -ForegroundColor Gray
        } elseif ($_.Exception.Message -like "*403*") {
            Write-Host "   → Access denied (private repo or SAML)" -ForegroundColor Gray
        } else {
            Write-Host "   → Other error: check logs" -ForegroundColor Gray
        }
    }
    
    Start-Sleep -Seconds 1  # Be nice to the APIs
}

Write-Host "`n📋 If no PRs worked:" -ForegroundColor Yellow
Write-Host "1. Use mock mode: cmd /c start-mock.bat" -ForegroundColor White
Write-Host "2. Check if your GitHub token is valid" -ForegroundColor White
Write-Host "3. Try manually browsing GitHub to find an existing PR" -ForegroundColor White
