package com.codeecho.pranalyzer.service;

import com.codeecho.pranalyzer.model.AgentResponse;
import com.codeecho.pranalyzer.model.PRData;
import com.codeecho.pranalyzer.model.CodePatterns;
import com.codeecho.pranalyzer.util.PatternExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Main service for analyzing PRs and generating similar code
 */
@Service
@Profile("!mock")
public class PRAnalyzerAgent {

    @Autowired
    private GitHubService gitHubService;

    @Autowired
    private AICodeGenerationService aiCodeGenerationService;

    @Autowired
    private PatternExtractor patternExtractor;

    @Autowired
    private SuggestionService suggestionService;

    @Autowired
    private ResponseFormatterService responseFormatterService;

    public AgentResponse analyzePRAndGenerate(String prUrl, String requirements) {
        try {
            // Step 1: Analyze PR
            PRData prData = gitHubService.analyzePR(prUrl);

            // Step 2: Extract patterns
            CodePatterns patterns = patternExtractor.extractPatterns(prData);

            // Step 3: Generate similar code
            String generatedCode = aiCodeGenerationService.generateSimilarCode(patterns, requirements);

            // Step 4: Generate specific suggestions
            String suggestions = suggestionService.generateSuggestions(prData, patterns, requirements);

            // Step 5: Generate implementation steps
            String implementationSteps = suggestionService.generateImplementationSteps(prData, patterns, requirements);

            // Step 6: Format the generated code for better UI display
            String formattedCode = responseFormatterService.formatGeneratedCode(generatedCode);

            return AgentResponse.builder()
                .prAnalysis(prData)
                .extractedPatterns(patterns)
                .generatedCode(formattedCode)
                .suggestions(suggestions)
                .implementationSteps(implementationSteps)
                .success(true)
                .mode("REAL")
                .build();

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid PR URL format. Expected: https://github.com/owner/repo/pull/NUMBER", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to analyze PR and generate code: " + e.getMessage(), e);
        }
    }
}
