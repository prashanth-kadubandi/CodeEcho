package com.codeecho.pranalyzer.util;

import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for parsing GitHub PR URLs
 */
public class PRUrlParser {
    
    private static final Pattern PR_URL_PATTERN =
        Pattern.compile("https://github\\.com/([^/]+)/([^/]+)/pull/(\\d+)(?:/.*)?");

    public static ParsedUrl parse(String prUrl) {
        // Clean the URL by removing any trailing paths like /files, /commits, etc.
        String cleanUrl = prUrl.replaceAll("(https://github\\.com/[^/]+/[^/]+/pull/\\d+).*", "$1");

        Matcher matcher = PR_URL_PATTERN.matcher(cleanUrl);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid GitHub PR URL: " + prUrl +
                ". Expected format: https://github.com/owner/repo/pull/NUMBER");
        }

        return new ParsedUrl(
            matcher.group(1), // owner
            matcher.group(2), // repo
            matcher.group(3)  // prNumber
        );
    }
    
    @Data
    public static class ParsedUrl {
        private final String owner;
        private final String repo;
        private final String prNumber;
    }
}
