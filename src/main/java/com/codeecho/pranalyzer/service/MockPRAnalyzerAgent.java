package com.codeecho.pranalyzer.service;

import com.codeecho.pranalyzer.model.AgentResponse;
import com.codeecho.pranalyzer.model.PRData;
import com.codeecho.pranalyzer.model.PRFile;
import com.codeecho.pranalyzer.model.CodePatterns;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Mock implementation of PR Analyzer Agent for testing without external API calls
 */
@Service
@Profile("mock")
public class MockPRAnalyzerAgent extends PRAnalyzerAgent {

    public AgentResponse analyzePRAndGenerate(String prUrl, String requirements) {
        // Create mock PR data
        PRFile mockFile = new PRFile();
        mockFile.setFilename("src/main/java/com/example/UserController.java");
        mockFile.setStatus("added");
        mockFile.setAdditions(45);
        mockFile.setDeletions(0);
        mockFile.setChanges(45);
        mockFile.setPatch("""
            +@RestController
            +@RequestMapping("/api/users")
            +public class UserController {
            +    
            +    @Autowired
            +    private UserService userService;
            +    
            +    @GetMapping("/{id}")
            +    public ResponseEntity<User> getUser(@PathVariable Long id) {
            +        User user = userService.findById(id);
            +        return ResponseEntity.ok(user);
            +    }
            +    
            +    @PostMapping
            +    public ResponseEntity<User> createUser(@RequestBody User user) {
            +        User savedUser = userService.save(user);
            +        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
            +    }
            +}
            """);

        PRData mockPRData = PRData.builder()
            .title("Add User Management Controller")
            .description("This PR adds a new REST controller for user management operations")
            .files(Arrays.asList(mockFile))
            .build();

        // Create mock extracted patterns
        CodePatterns mockPatterns = new CodePatterns();
        mockPatterns.addMethodPattern("@GetMapping(\"/{id}\")");
        mockPatterns.addMethodPattern("public ResponseEntity<User> getUser(@PathVariable Long id)");
        mockPatterns.addMethodPattern("@PostMapping");
        mockPatterns.addMethodPattern("public ResponseEntity<User> createUser(@RequestBody User user)");
        
        mockPatterns.addVariablePattern("@Autowired private UserService userService");
        
        mockPatterns.addLogicPattern("if (user != null)");
        
        mockPatterns.addStructuralPattern("@RestController");
        mockPatterns.addStructuralPattern("@RequestMapping(\"/api/users\")");

        // Create mock generated code
        String mockGeneratedCode = String.format("""
            💻 **GENERATED CODE**

            ```java
            // Generated code based on PR patterns for: %s

            @RestController
            @RequestMapping("/api/assets")
            public class AssetController {

                @Autowired
                private AssetService assetService;

                @GetMapping("/{glKey}")
                public ResponseEntity<Asset> getAssetByGLKey(@PathVariable String glKey) {
                    Asset asset = assetService.findByGLKey(glKey);
                    return ResponseEntity.ok(asset);
                }

                @PostMapping
                public ResponseEntity<Asset> createAsset(@RequestBody Asset asset) {
                    // Validate GLKey format
                    if (!isValidGLKey(asset.getGlKey())) {
                        throw new ValidationException("Invalid GLKey format");
                    }

                    Asset savedAsset = assetService.save(asset);
                    return ResponseEntity.status(HttpStatus.CREATED).body(savedAsset);
                }

                @PutMapping("/{glKey}")
                public ResponseEntity<Asset> updateAsset(@PathVariable String glKey,
                                                       @RequestBody Asset asset) {
                    asset.setGlKey(glKey);
                    Asset updatedAsset = assetService.update(asset);
                    return ResponseEntity.ok(updatedAsset);
                }

                private boolean isValidGLKey(String glKey) {
                    return glKey != null && glKey.matches("^[A-Z0-9]{6}$");
                }
            }
            ```

            *Generated based on extracted patterns from the PR analysis*
            """, requirements);

        String mockSuggestions = String.format("""
            🎯 **IMPLEMENTATION ROADMAP**

            ## 🎪 **DEMO IMPACT**
            *Transform GLKey mappings from nested objects to direct key references - reducing complexity and improving performance*

            ## 📋 **CHANGE SUMMARY**
            | Aspect | Details |
            |--------|---------|
            | 🎯 **Objective** | Simplify GLKey mappings in schema |
            | 📁 **Files Affected** | 1 primary file + 2 supporting files |
            | ⏱️ **Effort** | 15 minutes (Quick Win!) |
            | 🔥 **Priority** | HIGH - Immediate impact |

            ## 🛠️ **IMPLEMENTATION PLAN**

            ### 📁 **Primary File Changes**
            #### `fixed-assets.asset.s1.schema.yaml`
            - **🎯 Purpose:** Standardize GLKey mapping pattern across all account types
            - **📍 Location:** Lines 800, 828, 856, 889, 954 (5 locations)
            - **🔧 Action:** Replace `.key` object references with direct key mappings

            ### 💡 **Key Transformations**
            ```yaml
            # BEFORE (Complex nested structure)
            x-mappedTo: assetAcct.key
            x-mappedTo: deprAcct.key
            x-mappedTo: deprExpAcct.key

            # AFTER (Clean direct mapping)
            x-mappedTo: assetAcctKey
            x-mappedTo: deprAcctKey
            x-mappedTo: deprExpAcctKey
            ```

            ## 🔗 **RIPPLE EFFECTS**
            - **Dependencies:** Backend API mapping logic needs alignment
            - **Testing:** Validate all 5 GLKey types work correctly
            - **Documentation:** Update API docs with new mapping structure

            **Target Requirement:** %s
            """, requirements);

        String mockImplementationSteps = """
            🚀 **EXECUTION PLAYBOOK**

            ## 🎬 **DEMO SCRIPT**
            *"Watch how CodeEcho identifies the pattern and applies it to create similar GLKey mappings..."*

            ## ⚡ **QUICK WINS** (5-10 minutes)
            ### 🎯 **Step 1: Identify Target GLKey**
            - **What:** Find another GLKey that needs the same mapping pattern
            - **Where:** `fixed-assets.asset.s1.schema.yaml` lines 950-960
            - **Why:** Consistency across all GL account mappings improves maintainability

            ### 🔧 **Step 2: Apply the Pattern**
            - **Action:** Replace nested object reference with direct key mapping
            - **Code:**
            ```yaml
            # Find this pattern:
            gainLossGLAccount:
              properties:
                key:
                  x-mappedTo: gainLossAcct.key  # ← CHANGE THIS

            # Replace with:
            gainLossGLAccount:
              properties:
                key:
                  x-mappedTo: gainLossAcctKey   # ← TO THIS
            ```

            ### ✅ **Step 3: Validate Impact**
            - **Test:** Check schema validation passes
            - **Expected:** All GLKey mappings now follow consistent pattern

            ## 🎪 **PRESENTATION POINTS**
            - **💡 Innovation:** "AI detected the pattern and suggested 3 more locations to apply it"
            - **⚡ Speed:** "What took 30 minutes of manual review now takes 2 minutes"
            - **🎯 Accuracy:** "Zero risk of missing similar patterns across large codebases"

            ## 🏆 **SUCCESS METRICS**
            - **Before:** 5 different GLKey mapping patterns (inconsistent)
            - **After:** 1 standardized pattern across all GLKeys
            - **Impact:** 40% reduction in mapping complexity, easier maintenance
            """;

        return AgentResponse.builder()
            .prAnalysis(mockPRData)
            .extractedPatterns(mockPatterns)
            .generatedCode(mockGeneratedCode)
            .suggestions(mockSuggestions)
            .implementationSteps(mockImplementationSteps)
            .success(true)
            .mode("MOCK")
            .build();
    }
}
