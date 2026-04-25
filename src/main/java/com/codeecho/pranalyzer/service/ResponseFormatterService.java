package com.codeecho.pranalyzer.service;

import com.codeecho.pranalyzer.model.PRFile;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for formatting API responses to be more UI-friendly
 */
@Service
public class ResponseFormatterService {

    public String formatPatchChanges(List<PRFile> files) {
        if (files == null || files.isEmpty()) {
            return "📄 **No files changed**";
        }

        StringBuilder formatted = new StringBuilder();
        formatted.append("📁 **FILES CHANGED**\n\n");

        for (PRFile file : files) {
            formatted.append("### ").append(getFileIcon(file.getStatus())).append(" `").append(file.getFilename()).append("`\n");
            formatted.append("**Status:** ").append(formatStatus(file.getStatus())).append("\n");
            
            if (file.getAdditions() > 0 || file.getDeletions() > 0) {
                formatted.append("**Changes:** ");
                if (file.getAdditions() > 0) {
                    formatted.append("➕ ").append(file.getAdditions()).append(" additions ");
                }
                if (file.getDeletions() > 0) {
                    formatted.append("➖ ").append(file.getDeletions()).append(" deletions");
                }
                formatted.append("\n");
            }

            if (file.getPatch() != null && !file.getPatch().isEmpty()) {
                formatted.append("\n**Key Changes:**\n");
                formatted.append("```diff\n");
                formatted.append(formatPatch(file.getPatch()));
                formatted.append("\n```\n");
            }
            
            formatted.append("\n---\n\n");
        }

        return formatted.toString();
    }

    private String getFileIcon(String status) {
        return switch (status.toLowerCase()) {
            case "added" -> "🆕";
            case "modified" -> "✏️";
            case "deleted" -> "🗑️";
            case "renamed" -> "📝";
            default -> "📄";
        };
    }

    private String formatStatus(String status) {
        return switch (status.toLowerCase()) {
            case "added" -> "🆕 **Added**";
            case "modified" -> "✏️ **Modified**";
            case "deleted" -> "🗑️ **Deleted**";
            case "renamed" -> "📝 **Renamed**";
            default -> "📄 **" + status + "**";
        };
    }

    private String formatPatch(String patch) {
        if (patch == null || patch.isEmpty()) {
            return "No patch data available";
        }

        StringBuilder formatted = new StringBuilder();
        String[] lines = patch.split("\n");

        // Extract key changes and format them nicely
        formatted.append("**🔍 Key Changes Summary:**\n\n");

        int changeCount = 0;
        for (String line : lines) {
            if (line.startsWith("-") && !line.startsWith("---")) {
                formatted.append("❌ **Removed:** `").append(line.substring(1).trim()).append("`\n");
                changeCount++;
            } else if (line.startsWith("+") && !line.startsWith("+++")) {
                formatted.append("✅ **Added:** `").append(line.substring(1).trim()).append("`\n");
                changeCount++;
            }

            // Limit to 5 most important changes for readability
            if (changeCount >= 10) {
                formatted.append("\n*... and ").append(lines.length - changeCount).append(" more changes*\n");
                break;
            }
        }

        return formatted.toString();
    }

    public String formatCodePatterns(String methodPatterns, String variablePatterns, 
                                   String logicPatterns, String structuralPatterns) {
        StringBuilder formatted = new StringBuilder();
        formatted.append("🔍 **EXTRACTED PATTERNS**\n\n");

        if (methodPatterns != null && !methodPatterns.isEmpty()) {
            formatted.append("## 🔧 Method Patterns\n");
            formatted.append(methodPatterns).append("\n\n");
        }

        if (variablePatterns != null && !variablePatterns.isEmpty()) {
            formatted.append("## 📊 Variable Patterns\n");
            formatted.append(variablePatterns).append("\n\n");
        }

        if (logicPatterns != null && !logicPatterns.isEmpty()) {
            formatted.append("## 🧠 Logic Patterns\n");
            formatted.append(logicPatterns).append("\n\n");
        }

        if (structuralPatterns != null && !structuralPatterns.isEmpty()) {
            formatted.append("## 🏗️ Structural Patterns\n");
            formatted.append(structuralPatterns).append("\n\n");
        }

        return formatted.toString();
    }

    public String formatGeneratedCode(String code) {
        if (code == null || code.isEmpty()) {
            return "⚠️ **No code generated**";
        }

        StringBuilder formatted = new StringBuilder();
        formatted.append("💻 **GENERATED CODE**\n\n");
        formatted.append("```java\n");
        formatted.append(code);
        formatted.append("\n```\n\n");
        formatted.append("*Generated based on extracted patterns from the PR analysis*");

        return formatted.toString();
    }
}
