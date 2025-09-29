package org.example.administrator;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Spring Boot test class for Administrator Application
 */
@SpringBootTest
@ActiveProfiles("test")
@Slf4j
class AdministratorApplicationTests {

    @Test
    void contextLoads() {
        log.info(" Spring Boot application context loads successfully");
    }

    @Test
    void applicationStarts() {
        log.info(" Spring Boot application starts successfully");
    }
}
