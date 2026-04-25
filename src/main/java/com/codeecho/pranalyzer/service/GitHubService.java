package com.codeecho.pranalyzer.service;

import com.codeecho.pranalyzer.model.PRData;
import com.codeecho.pranalyzer.model.PRResponse;
import com.codeecho.pranalyzer.model.PRFile;
import com.codeecho.pranalyzer.util.PRUrlParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * Service for interacting with GitHub API
 */
@Service
public class GitHubService {
    
    @Value("${github.token}")
    private String githubToken;
    
    private final RestTemplate restTemplate;
    
    public GitHubService() {
        this.restTemplate = new RestTemplate();
    }
    
    public PRData analyzePR(String prUrl) {
        System.out.println("🔍 GitHub Service: Parsing URL: " + prUrl);
        PRUrlParser.ParsedUrl parsed = PRUrlParser.parse(prUrl);

        System.out.println("🔍 GitHub Service: Parsed - Owner: " + parsed.getOwner() +
                          ", Repo: " + parsed.getRepo() + ", PR: " + parsed.getPrNumber());

        // Get PR details
        String prApiUrl = String.format("https://api.github.com/repos/%s/%s/pulls/%s",
            parsed.getOwner(), parsed.getRepo(), parsed.getPrNumber());

        System.out.println("🔍 GitHub Service: API URL: " + prApiUrl);
        System.out.println("🔍 GitHub Service: Using token: " +
                          (githubToken != null ? githubToken.substring(0, 10) + "..." : "NULL"));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + githubToken);
        headers.set("Accept", "application/vnd.github.v3+json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            System.out.println("🔍 GitHub Service: Calling GitHub API for PR details...");
            ResponseEntity<PRResponse> prResponse = restTemplate.exchange(
                prApiUrl, HttpMethod.GET, entity, PRResponse.class);
            System.out.println("✅ GitHub Service: Got PR response: " + prResponse.getStatusCode());

            // Get file changes
            String filesUrl = prApiUrl + "/files";
            System.out.println("🔍 GitHub Service: Calling GitHub API for files...");
            ResponseEntity<PRFile[]> filesResponse = restTemplate.exchange(
                filesUrl, HttpMethod.GET, entity, PRFile[].class);
            System.out.println("✅ GitHub Service: Got files response: " + filesResponse.getStatusCode());

            return PRData.builder()
                .title(prResponse.getBody().getTitle())
                .description(prResponse.getBody().getBody())
                .files(Arrays.asList(filesResponse.getBody()))
                .build();
        } catch (Exception e) {
            System.err.println("❌ GitHub Service Error: " + e.getMessage());
            throw new RuntimeException("GitHub API call failed: " + e.getMessage(), e);
        }
    }
}
