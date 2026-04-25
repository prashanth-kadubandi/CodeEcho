package com.codeecho.pranalyzer.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Model for OpenAI API request
 */
@Data
@Builder
public class OpenAIRequest {
    
    private String model;
    private List<OpenAIMessage> messages;
    private double temperature;
    private int max_tokens;
}
