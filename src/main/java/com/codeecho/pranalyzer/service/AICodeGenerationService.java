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
 * AI Code Generation Service supporting multiple providers:
 * - OpenAI (ChatGPT)
 * - GitHub Copilot
 * - Azure OpenAI
 */
@Service
public class AICodeGenerationService {
    
    @Value("${ai.provider:openai}")
    private String aiProvider;
    
    @Value("${openai.api.key:}")
    private String openaiApiKey;
    
    @Value("${github.token:}")
    private String githubToken;
    
    @Value("${azure.openai.key:}")
    private String azureOpenaiKey;
    
    @Value("${azure.openai.endpoint:}")
    private String azureOpenaiEndpoint;
    
    private final RestTemplate restTemplate;
    
    public AICodeGenerationService() {
        this.restTemplate = new RestTemplate();
    }
    
    public String generateSimilarCode(CodePatterns patterns, String requirements) {
        String prompt = buildPrompt(patterns, requirements);
        
        switch (aiProvider.toLowerCase()) {
            case "github":
            case "copilot":
                return generateWithGitHubCopilot(prompt);
            case "azure":
                return generateWithAzureOpenAI(prompt);
            case "openai":
            default:
                return generateWithOpenAI(prompt);
        }
    }
    
    private String generateWithGitHubCopilot(String prompt) {
        System.out.println("🤖 GitHub Copilot API not publicly available - falling back to OpenAI...");

        // GitHub Copilot doesn't have a public API yet
        // Fall back to OpenAI with GitHub-style prompting
        return generateWithOpenAI(prompt);
    }
    
    private String generateWithAzureOpenAI(String prompt) {
        System.out.println("🤖 Using Azure OpenAI for code generation...");
        
        String apiUrl = azureOpenaiEndpoint + "/openai/deployments/gpt-4o/chat/completions?api-version=2024-02-15-preview";

        OpenAIRequest request = OpenAIRequest.builder()
            .messages(Arrays.asList(
                new OpenAIMessage("system", "You are an AI coding assistant specialized in enterprise code generation."),
                new OpenAIMessage("user", prompt)
            ))
            .temperature(0.1)
            .max_tokens(4000)
            .build();
            
        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", azureOpenaiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        try {
            HttpEntity<OpenAIRequest> entity = new HttpEntity<>(request, headers);
            ResponseEntity<OpenAIResponse> response = restTemplate.exchange(
                apiUrl, HttpMethod.POST, entity, OpenAIResponse.class);
                
            return response.getBody().getChoices().get(0).getMessage().getContent();
        } catch (Exception e) {
            System.err.println("❌ Azure OpenAI API failed: " + e.getMessage());
            // Fallback to OpenAI
            return generateWithOpenAI(prompt);
        }
    }
    
    private String generateWithOpenAI(String prompt) {
        System.out.println("🤖 Using OpenAI for code generation...");
        
        String apiUrl = "https://api.openai.com/v1/chat/completions";
        
        OpenAIRequest request = OpenAIRequest.builder()
            .model("gpt-4o")
            .messages(Arrays.asList(
                new OpenAIMessage("system", "You are a code generation assistant that follows established patterns."),
                new OpenAIMessage("user", prompt)
            ))
            .temperature(0.1)
            .max_tokens(4000)
            .build();
            
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openaiApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<OpenAIRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<OpenAIResponse> response = restTemplate.exchange(
            apiUrl, HttpMethod.POST, entity, OpenAIResponse.class);
            
        return response.getBody().getChoices().get(0).getMessage().getContent();
    }
    
    private String buildPrompt(CodePatterns patterns, String requirements) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Based on the following code patterns extracted from a GitHub Pull Request, ");
        prompt.append("generate similar code that meets the specified requirements.\n\n");
        
        prompt.append("EXTRACTED PATTERNS:\n");
        prompt.append("Method Patterns: ").append(patterns.getMethodPatterns()).append("\n");
        prompt.append("Variable Patterns: ").append(patterns.getVariablePatterns()).append("\n");
        prompt.append("Logic Patterns: ").append(patterns.getLogicPatterns()).append("\n");
        prompt.append("Structural Patterns: ").append(patterns.getStructuralPatterns()).append("\n\n");
        
        prompt.append("REQUIREMENTS: ").append(requirements).append("\n\n");
        
        prompt.append("Please generate clean, production-ready code that follows the same patterns ");
        prompt.append("and architectural style. Include proper annotations, error handling, and documentation.");
        
        return prompt.toString();
    }
}
