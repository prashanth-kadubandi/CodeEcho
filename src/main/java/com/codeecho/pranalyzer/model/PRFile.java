package com.codeecho.pranalyzer.model;

import lombok.Data;

/**
 * Model representing a file in a Pull Request
 */
@Data
public class PRFile {
    
    private String filename;
    private String status;
    private int additions;
    private int deletions;
    private int changes;
    private String patch;
}
