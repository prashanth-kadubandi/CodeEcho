package com.codeecho.pranalyzer.model;

import lombok.Data;

/**
 * Model representing GitHub PR API response
 */
@Data
public class PRResponse {
    
    private String title;
    private String body;
    private String state;
    private int number;
}
