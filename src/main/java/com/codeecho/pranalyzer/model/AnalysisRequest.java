package com.codeecho.pranalyzer.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request model for PR analysis
 */
@Data
@Schema(description = "Analysis Request")
public class AnalysisRequest {

    @NotBlank(message = "PR URL is required")
    @Schema(description = "GitHub PR URL")
    private String prUrl;

    @NotBlank(message = "Requirements are required")
    @Schema(description = "Requirements")
    private String requirements;
}
