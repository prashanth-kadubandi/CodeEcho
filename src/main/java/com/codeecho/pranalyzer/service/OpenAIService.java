package com.codeecho.pranalyzer.service;

import com.codeecho.pranalyzer.model.CodePatterns;
import com.codeecho.pranalyzer.model.OpenAIRequest;
import com.codeecho.pranalyzer.model.OpenAIResponse;
import com.codeecho.pranalyzer.model.OpenAIMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * Service for interacting with OpenAI API
 */
@Service
public class OpenAIService {
    
    @Value("${openai.api.key}")
    private String apiKey;
    
    private final RestTemplate restTemplate;
    
    public OpenAIService() {
        this.restTemplate = new RestTemplate();
    }
    
    public String generateSimilarCode(CodePatterns patterns, String requirements) {
        String prompt = buildPrompt(patterns, requirements);
        
        OpenAIRequest request = OpenAIRequest.builder()
            .model("gpt-3.5-turbo")
            .messages(Arrays.asList(
                new OpenAIMessage("system", "You are a code generation assistant that follows established patterns."),
                new OpenAIMessage("user", prompt)
            ))
            .temperature(0.1)
            .max_tokens(2000)
            .build();
            
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<OpenAIRequest> entity = new HttpEntity<>(request, headers);
        
        ResponseEntity<OpenAIResponse> response = restTemplate.exchange(
            "https://api.openai.com/v1/chat/completions", 
            HttpMethod.POST, entity, OpenAIResponse.class);
            
        return response.getBody().getChoices().get(0).getMessage().getContent();
    }
    
    private String buildPrompt(CodePatterns patterns, String requirements) {
        return String.format("""
            Based on these patterns from a previous PR:
            
            Method Patterns:
            %s
            
            Variable Patterns:
            %s
            
            Logic Patterns:
            %s
            
            Structural Patterns:
            %s
            
            Generate similar code for: %s
            
            Follow the same naming conventions, code structure, and business logic patterns.
            """, 
            String.join("\n", patterns.getMethodPatterns()),
            String.join("\n", patterns.getVariablePatterns()),
            String.join("\n", patterns.getLogicPatterns()),
            String.join("\n", patterns.getStructuralPatterns()),
            requirements);
    }
}
