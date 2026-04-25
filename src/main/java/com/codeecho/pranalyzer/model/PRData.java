package com.codeecho.pranalyzer.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Model representing Pull Request data
 */
@Data
@Builder
@Schema(description = "Pull Request data extracted from GitHub")
public class PRData {

    @Schema(description = "Pull Request title", example = "Add user authentication feature")
    private String title;

    @Schema(description = "Pull Request description/body")
    private String description;

    @Schema(description = "List of files changed in the Pull Request")
    private List<PRFile> files;
}
