package com.webapi.unittests;

import com.webapi.services.FibonacciService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FibonacciTest {

    static private final FibonacciService fibonacciService =
            new FibonacciService();

    @Test
    @Order(1)
    public void testFibonacci() {
        List<Long> longs = fibonacciService.get(1L, 2L, 3L);
        assert(longs.get(0) == 3);
        assert(longs.get(1) == 5);
        assert(longs.get(2) == 8);
    }
}
