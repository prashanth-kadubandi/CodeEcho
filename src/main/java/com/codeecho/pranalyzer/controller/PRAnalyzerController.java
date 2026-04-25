package com.codeecho.pranalyzer.controller;

import com.codeecho.pranalyzer.model.AnalysisRequest;
import com.codeecho.pranalyzer.model.AgentResponse;
import com.codeecho.pranalyzer.service.PRAnalyzerAgent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * REST Controller for PR Analysis and Code Generation
 */
@RestController
@RequestMapping("/api/pr-analyzer")
@Tag(name = "CodeEcho", description = "AI-Powered PR Pattern Analyzer & Code Generator - Revolutionizing development workflows through intelligent pattern recognition")
public class PRAnalyzerController {
    
    @Autowired
    private PRAnalyzerAgent agent;

    @Autowired
    private Environment environment;

    @Value("${ai.provider:openai}")
    private String aiProvider;
    
    @Operation(
        summary = "Analyze PR and Generate Similar Code",
        description = "Analyzes a GitHub Pull Request to extract code patterns and generates similar code based on requirements"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "500", description = "Server Error")
    })
    @PostMapping("/analyze-and-generate")
    public ResponseEntity<AgentResponse> analyzeAndGenerate(
        @Parameter(description = "Analysis request containing PR URL and requirements", required = true)
        @Valid @RequestBody AnalysisRequest request) {
        try {
            System.out.println("🔍 DEBUG: Received request for PR: " + request.getPrUrl());
            System.out.println("🔍 DEBUG: Requirements: " + request.getRequirements());

            AgentResponse response = agent.analyzePRAndGenerate(
                request.getPrUrl(),
                request.getRequirements()
            );

            System.out.println("✅ DEBUG: Successfully generated response");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("❌ ERROR: Failed to analyze PR: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(AgentResponse.error("Failed to analyze PR: " + e.getMessage()));
        }
    }
    
    @Operation(
        summary = "Health Check",
        description = "Simple health check endpoint to verify the service is running"
    )
    @ApiResponse(responseCode = "200", description = "Service is healthy")
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("CodeEcho is running");
    }

    @Operation(
        summary = "Application Status",
        description = "Shows current application mode (mock/real) and AI provider configuration"
    )
    @ApiResponse(responseCode = "200", description = "Application status information")
    @GetMapping("/status")
    public ResponseEntity<String> status() {
        String[] activeProfiles = environment.getActiveProfiles();
        boolean isMockMode = activeProfiles.length > 0 &&
                           java.util.Arrays.asList(activeProfiles).contains("mock");

        String mode = isMockMode ? "MOCK" : "REAL";
        String agentType = isMockMode ? "MockPRAnalyzerAgent" : "PRAnalyzerAgent";

        String status = String.format(
            "🤖 CodeEcho Status:\n" +
            "Mode: %s\n" +
            "Agent: %s\n" +
            "AI Provider: %s\n" +
            "Active Profiles: %s\n" +
            "\n" +
            "%s",
            mode,
            agentType,
            isMockMode ? "Mock (no external APIs)" : aiProvider,
            activeProfiles.length > 0 ? String.join(", ", activeProfiles) : "default",
            isMockMode ?
                "✅ Mock mode - reliable for demos, returns realistic sample data" :
                "🔗 Real mode - connects to GitHub API and " + aiProvider + " for live analysis"
        );

        return ResponseEntity.ok(status);
    }

    @Operation(
        summary = "Test PR URL Parsing",
        description = "Test endpoint to validate PR URL format and parsing"
    )
    @GetMapping("/test-url")
    public ResponseEntity<String> testUrl(@Parameter(description = "GitHub PR URL to test")
                                         @RequestParam String prUrl) {
        try {
            com.codeecho.pranalyzer.util.PRUrlParser.ParsedUrl parsed =
                com.codeecho.pranalyzer.util.PRUrlParser.parse(prUrl);
            return ResponseEntity.ok(String.format(
                "✅ URL parsed successfully!\nOwner: %s\nRepo: %s\nPR Number: %s",
                parsed.getOwner(), parsed.getRepo(), parsed.getPrNumber()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("❌ Error: " + e.getMessage());
        }
    }
}
