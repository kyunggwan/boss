package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    @Test 
    void appCanBeInstantiated() {
        App app = new App();
        assertNotNull(app, "App should not be null");
    }
    
    @Test
    void appHasStatusEndpoint() {
        App app = new App();
        String result = app.status();
        assertNotNull(result, "status() should return a non-null string");
        assertTrue(result.contains("running"), "status() should contain 'running'");
    }
}
