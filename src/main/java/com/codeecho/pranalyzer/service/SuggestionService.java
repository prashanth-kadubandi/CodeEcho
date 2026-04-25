package com.codeecho.pranalyzer.service;

import com.codeecho.pranalyzer.model.CodePatterns;
import com.codeecho.pranalyzer.model.PRData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for generating specific implementation suggestions
 */
@Service
public class SuggestionService {

    @Autowired
    private AICodeGenerationService aiCodeGenerationService;

    public String generateSuggestions(PRData prData, CodePatterns patterns, String requirements) {
        String prompt = buildSuggestionsPrompt(prData, patterns, requirements);
        return aiCodeGenerationService.generateSimilarCode(patterns, prompt);
    }

    public String generateImplementationSteps(PRData prData, CodePatterns patterns, String requirements) {
        String prompt = buildImplementationPrompt(prData, patterns, requirements);
        return aiCodeGenerationService.generateSimilarCode(patterns, prompt);
    }

    private String buildSuggestionsPrompt(PRData prData, CodePatterns patterns, String requirements) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Based on the following PR analysis, provide SPECIFIC SUGGESTIONS for implementing similar changes.\n\n");

        prompt.append("ORIGINAL PR CHANGES:\n");
        prompt.append("Title: ").append(prData.getTitle()).append("\n");
        prompt.append("Description: ").append(prData.getDescription()).append("\n\n");

        prompt.append("FILES CHANGED:\n");
        if (prData.getFiles() != null) {
            prData.getFiles().forEach(file -> {
                prompt.append("- ").append(file.getFilename()).append(" (").append(file.getStatus()).append(")\n");
                if (file.getPatch() != null && !file.getPatch().isEmpty()) {
                    prompt.append("  Changes: ").append(file.getPatch().substring(0, Math.min(200, file.getPatch().length()))).append("...\n");
                }
            });
        }

        prompt.append("\nEXTRACTED PATTERNS:\n");
        prompt.append("- Method Patterns: ").append(patterns.getMethodPatterns()).append("\n");
        prompt.append("- Variable Patterns: ").append(patterns.getVariablePatterns()).append("\n");
        prompt.append("- Logic Patterns: ").append(patterns.getLogicPatterns()).append("\n");
        prompt.append("- Structural Patterns: ").append(patterns.getStructuralPatterns()).append("\n\n");

        prompt.append("TARGET REQUIREMENT: ").append(requirements).append("\n\n");

        prompt.append("PROVIDE SPECIFIC SUGGESTIONS in this EXACT FORMAT:\n\n");
        prompt.append("🎯 **IMPLEMENTATION ROADMAP**\n\n");
        prompt.append("## 🎪 **DEMO IMPACT**\n");
        prompt.append("*What this change accomplishes and why it matters*\n\n");
        prompt.append("## 📋 **CHANGE SUMMARY**\n");
        prompt.append("| Aspect | Details |\n");
        prompt.append("|--------|--------|\n");
        prompt.append("| 🎯 **Objective** | [What you're trying to achieve] |\n");
        prompt.append("| 📁 **Files Affected** | [Number] files need modification |\n");
        prompt.append("| ⏱️ **Effort** | [Estimated time/complexity] |\n");
        prompt.append("| 🔥 **Priority** | [High/Medium/Low] |\n\n");
        prompt.append("## 🛠️ **IMPLEMENTATION PLAN**\n\n");
        prompt.append("### 📁 **Primary File Changes**\n");
        prompt.append("#### `[filename]`\n");
        prompt.append("- **🎯 Purpose:** [Why this file needs changes]\n");
        prompt.append("- **📍 Location:** [Specific section/lines]\n");
        prompt.append("- **🔧 Action:** [What to modify]\n\n");
        prompt.append("### 💡 **Key Transformations**\n");
        prompt.append("```yaml\n");
        prompt.append("# BEFORE\n[original code]\n\n");
        prompt.append("# AFTER  \n[modified code]\n");
        prompt.append("```\n\n");
        prompt.append("## 🔗 **RIPPLE EFFECTS**\n");
        prompt.append("- **Dependencies:** [What else might be affected]\n");
        prompt.append("- **Testing:** [What needs to be validated]\n");
        prompt.append("- **Documentation:** [What docs need updates]\n\n");
        prompt.append("Make it presentation-ready with clear visual hierarchy and impact statements.");

        return prompt.toString();
    }

    private String buildImplementationPrompt(PRData prData, CodePatterns patterns, String requirements) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Create a STEP-BY-STEP IMPLEMENTATION GUIDE for: ").append(requirements).append("\n\n");

        prompt.append("Based on the patterns from this PR:\n");
        prompt.append("Title: ").append(prData.getTitle()).append("\n\n");

        prompt.append("PROVIDE IMPLEMENTATION STEPS in this EXACT FORMAT:\n\n");
        prompt.append("🚀 **EXECUTION PLAYBOOK**\n\n");
        prompt.append("## 🎬 **DEMO SCRIPT**\n");
        prompt.append("*\"Here's how we implement similar changes in another GLKey...\"*\n\n");
        prompt.append("## ⚡ **QUICK WINS** (5-10 minutes)\n");
        prompt.append("### 🎯 **Step 1: Identify Target**\n");
        prompt.append("- **What:** [Specific target to modify]\n");
        prompt.append("- **Where:** `[exact file location]`\n");
        prompt.append("- **Why:** [Business impact]\n\n");
        prompt.append("### 🔧 **Step 2: Apply Pattern**\n");
        prompt.append("- **Action:** [Specific change to make]\n");
        prompt.append("- **Code:**\n");
        prompt.append("```yaml\n[exact code to add/modify]\n```\n\n");
        prompt.append("### ✅ **Step 3: Validate**\n");
        prompt.append("- **Test:** [How to verify it works]\n");
        prompt.append("- **Expected:** [What should happen]\n\n");
        prompt.append("## 🎪 **PRESENTATION POINTS**\n");
        prompt.append("- **💡 Innovation:** [What's clever about this approach]\n");
        prompt.append("- **⚡ Speed:** [How this saves time]\n");
        prompt.append("- **🎯 Accuracy:** [How this reduces errors]\n\n");
        prompt.append("## 🏆 **SUCCESS METRICS**\n");
        prompt.append("- **Before:** [Current state]\n");
        prompt.append("- **After:** [Improved state]\n");
        prompt.append("- **Impact:** [Measurable benefit]\n\n");
        prompt.append("Make it demo-ready with clear talking points and visual impact.");

        return prompt.toString();
    }
}
