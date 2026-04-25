package com.codeecho.pranalyzer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model for OpenAI message
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenAIMessage {
    
    private String role;
    private String content;
}
