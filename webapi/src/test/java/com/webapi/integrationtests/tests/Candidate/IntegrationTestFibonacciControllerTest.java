package com.webapi.integrationtests.tests.Candidate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.webapi.integrationtests.IntegrationTestHttpClient;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import testutil.AppYamlManager;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IntegrationTestFibonacciControllerTest {

    @Autowired
    private ServerProperties serverProperties;
    private String fibonacciAPIPath;

    public IntegrationTestFibonacciControllerTest() {
    }

    @BeforeEach
    void setUp() {
        this.fibonacciAPIPath = AppYamlManager.getTestingUrl() + serverProperties.getPort() + "/api/v1/fibonacci/";
    }

    @Test
    @Order(1)
    void test_fib() throws IOException, InterruptedException {
        Gson gson = new Gson();
        HttpResponse<String> getFibHttpResponse = IntegrationTestHttpClient.
                get(fibonacciAPIPath + "1/1/4/");

        List<Long> longs = gson.fromJson(
                getFibHttpResponse.body(),
                new TypeToken<List<Long>>() {
                }.getType());
        assert (longs.get(0) == 2);
        assert (longs.get(1) == 3);
        assert (longs.get(2) == 5);
        assert (longs.get(3) == 8);
    }
}

//todo: create a front end entity that uses the fibonacci endpoint,
//todo: create a selenium test that tests the front ed entity that uses the fibonacci endpoint