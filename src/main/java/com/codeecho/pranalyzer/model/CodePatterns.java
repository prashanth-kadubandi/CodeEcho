package com.codeecho.pranalyzer.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Model representing extracted code patterns
 */
@Data
@Schema(description = "Code patterns extracted from Pull Request changes")
public class CodePatterns {

    @Schema(description = "Method signature patterns found in the PR")
    private List<String> methodPatterns = new ArrayList<>();

    @Schema(description = "Variable declaration patterns found in the PR")
    private List<String> variablePatterns = new ArrayList<>();

    @Schema(description = "Logic/control flow patterns found in the PR")
    private List<String> logicPatterns = new ArrayList<>();

    @Schema(description = "Structural patterns (annotations, class declarations) found in the PR")
    private List<String> structuralPatterns = new ArrayList<>();
    
    public void addMethodPattern(String pattern) {
        methodPatterns.add(pattern);
    }
    
    public void addVariablePattern(String pattern) {
        variablePatterns.add(pattern);
    }
    
    public void addLogicPattern(String pattern) {
        logicPatterns.add(pattern);
    }
    
    public void addStructuralPattern(String pattern) {
        structuralPatterns.add(pattern);
    }
}
