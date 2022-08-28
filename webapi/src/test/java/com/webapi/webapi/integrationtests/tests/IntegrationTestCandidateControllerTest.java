package com.webapi.webapi.integrationtests.tests;

import com.webapi.webapi.integrationtests.IntegrationTestHttpClient;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.http.HttpResponse;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IntegrationTestCandidateControllerTest {

    @Test
    @Order(1)
    void testGet() throws IOException, InterruptedException {
        HttpResponse<Void> voidHttpResponse = IntegrationTestHttpClient.get("http://localhost:8080/api/v1/candidate/1");
    }
}
