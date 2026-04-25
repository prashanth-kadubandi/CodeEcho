# Test with public repositories
Write-Host "Testing with Public GitHub Repositories..." -ForegroundColor Green
Write-Host "==========================================" -ForegroundColor Green

$baseUrl = "http://localhost:8080"
$headers = @{ "Content-Type" = "application/json" }

# Test with different public repositories
$testCases = @(
    @{
        name = "Spring Boot (Java)"
        prUrl = "https://github.com/spring-projects/spring-boot/pull/38000"
        requirements = "Generate a similar REST controller for user management"
    },
    @{
        name = "VS Code (TypeScript)"
        prUrl = "https://github.com/microsoft/vscode/pull/200000"
        requirements = "Generate a similar feature implementation"
    },
    @{
        name = "React (JavaScript)"
        prUrl = "https://github.com/facebook/react/pull/25000"
        requirements = "Generate a similar React component"
    }
)

foreach ($test in $testCases) {
    Write-Host "`n[TEST] $($test.name)" -ForegroundColor Yellow
    Write-Host "PR URL: $($test.prUrl)" -ForegroundColor Cyan
    
    $body = @{
        prUrl = $test.prUrl
        requirements = $test.requirements
    } | ConvertTo-Json
    
    try {
        Write-Host "Calling API..." -ForegroundColor Gray
        $response = Invoke-RestMethod -Uri "$baseUrl/api/pr-analyzer/analyze-and-generate" -Method POST -Body $body -Headers $headers -TimeoutSec 30
        
        Write-Host "✅ SUCCESS!" -ForegroundColor Green
        Write-Host "PR Title: $($response.prAnalysis.title)" -ForegroundColor White
        Write-Host "Files Changed: $($response.prAnalysis.files.Count)" -ForegroundColor White
        Write-Host "Method Patterns: $($response.extractedPatterns.methodPatterns.Count)" -ForegroundColor White
        Write-Host "Generated Code Length: $($response.generatedCode.Length) characters" -ForegroundColor White
        
        # Show a preview of generated code
        if ($response.generatedCode.Length -gt 0) {
            Write-Host "`nGenerated Code Preview:" -ForegroundColor Cyan
            $preview = $response.generatedCode.Substring(0, [Math]::Min(300, $response.generatedCode.Length))
            Write-Host $preview -ForegroundColor White
            Write-Host "..." -ForegroundColor Gray
        }
        
        # Success - we found a working example!
        Write-Host "`n🎉 WORKING EXAMPLE FOUND!" -ForegroundColor Green
        Write-Host "Use this for your demo:" -ForegroundColor Yellow
        Write-Host $body -ForegroundColor White
        break
        
    } catch {
        Write-Host "❌ Failed: $($_.Exception.Message)" -ForegroundColor Red
        if ($_.Exception.Response) {
            try {
                $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
                $responseBody = $reader.ReadToEnd()
                Write-Host "Error details: $responseBody" -ForegroundColor Yellow
            } catch {
                Write-Host "Could not read error details" -ForegroundColor Gray
            }
        }
        Write-Host "Trying next repository..." -ForegroundColor Gray
    }
    
    Start-Sleep -Seconds 2
}

Write-Host "`n📝 Tips for Your Demo:" -ForegroundColor Green
Write-Host "1. Use the working example above" -ForegroundColor White
Write-Host "2. Try mock mode if GitHub API has issues: mvn spring-boot:run -Dspring.profiles.active=mock" -ForegroundColor White
Write-Host "3. Check Swagger UI: http://localhost:8080/swagger-ui.html" -ForegroundColor White
