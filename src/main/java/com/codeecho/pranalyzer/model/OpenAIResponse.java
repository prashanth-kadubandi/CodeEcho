package com.codeecho.pranalyzer.model;

import lombok.Data;

import java.util.List;

/**
 * Model for OpenAI API response
 */
@Data
public class OpenAIResponse {
    
    private List<Choice> choices;
    
    @Data
    public static class Choice {
        private OpenAIMessage message;
        private String finishReason;
    }
}
