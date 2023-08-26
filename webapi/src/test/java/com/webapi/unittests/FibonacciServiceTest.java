package com.webapi.unittests;

import com.webapi.services.FibonacciService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.webapi.unittests.FibonacciServiceTestHelper.checkFibonacci;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FibonacciServiceTest {

    static private final FibonacciService fibonacciService =
            new FibonacciService();

    @Test
    @Order(1)
    public void testFibonacci3() {
        List<Long> longs = fibonacciService.get(1L, 2L, 3L);
        assert(longs.get(0) == 3);
        assert(longs.get(1) == 5);
        assert(longs.get(2) == 8);
    }

    @Test
    @Order(2)
    public void checkFibonacci20() {
        List<Long> longs = fibonacciService.get(1L, 2L, 20L);
        assert(checkFibonacci(longs));
    }

    @Test
    @Order(3)
    public void checkFibonacci50() {
        List<Long> longs = fibonacciService.get(1L, 2L, 50L);
        assert(checkFibonacci(longs));
    }
}
