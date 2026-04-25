package com.codeecho.pranalyzer.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * Response model for PR analysis and code generation
 */
@Data
@Builder
@Schema(description = "Analysis Response")
public class AgentResponse {

    @Schema(description = "Analyzed Pull Request data")
    private PRData prAnalysis;

    @Schema(description = "Extracted code patterns from the PR")
    private CodePatterns extractedPatterns;

    @Schema(description = "Generated code based on extracted patterns and requirements")
    private String generatedCode;

    @Schema(description = "Specific suggestions for implementing similar changes")
    private String suggestions;

    @Schema(description = "Step-by-step implementation guide")
    private String implementationSteps;

    @Schema(description = "Indicates if the operation was successful", example = "true")
    private boolean success;

    @Schema(description = "Error message if operation failed")
    private String errorMessage;

    @Schema(description = "Application mode indicator", example = "REAL")
    private String mode;
    
    public static AgentResponse error(String message) {
        return AgentResponse.builder()
            .success(false)
            .errorMessage(message)
            .build();
    }
}
