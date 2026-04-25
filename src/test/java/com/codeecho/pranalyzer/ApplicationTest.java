package com.codeecho.pranalyzer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * Basic application context test
 */
@SpringBootTest
@TestPropertySource(properties = {
    "github.token=test-token",
    "openai.api.key=test-key"
})
class ApplicationTest {

    @Test
    void contextLoads() {
        // This test will pass if the application context loads successfully
    }
}
