package com.audiobea.crm.app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AudioBeaCrmApplicationTests {

    private static final Integer EXPECTED_VALUE = 1;

    @Test
    void contextLoads() {
        assertEquals(EXPECTED_VALUE, 1);
    }

}
