package com.codeecho.pranalyzer.util;

import com.codeecho.pranalyzer.model.CodePatterns;
import com.codeecho.pranalyzer.model.PRData;
import com.codeecho.pranalyzer.model.PRFile;
import org.springframework.stereotype.Component;

/**
 * Utility class for extracting code patterns from PR data
 */
@Component
public class PatternExtractor {

    public CodePatterns extractPatterns(PRData prData) {
        CodePatterns patterns = new CodePatterns();

        for (PRFile file : prData.getFiles()) {
            if (file.getPatch() != null) {
                extractFromPatch(file.getPatch(), patterns);
            }
        }

        return patterns;
    }

    private void extractFromPatch(String patch, CodePatterns patterns) {
        String[] lines = patch.split("\n");

        for (String line : lines) {
            if (line.startsWith("+") && !line.startsWith("+++")) {
                String addedLine = line.substring(1).trim();

                // Extract method patterns
                if (addedLine.matches(".*\\b(public|private|protected)\\s+.*\\s+\\w+\\s*\\(.*")) {
                    patterns.addMethodPattern(addedLine);
                }

                // Extract variable patterns (Java style)
                if (addedLine.matches(".*\\b(int|String|boolean|double|float|long|char|byte)\\s+\\w+.*")) {
                    patterns.addVariablePattern(addedLine);
                }

                // Extract conditional logic
                if (addedLine.matches(".*(if\\s*\\(|switch\\s*\\().*")) {
                    patterns.addLogicPattern(addedLine);
                }

                // Extract class/interface usage
                if (addedLine.matches(".*\\bnew\\s+\\w+.*|.*\\bextends\\s+\\w+.*|.*\\bimplements\\s+\\w+.*")) {
                    patterns.addStructuralPattern(addedLine);
                }
            }
        }
    }
}
