package com.webapi.webapi.integrationtests.tests;

import com.google.gson.Gson;
import com.webapi.webapi.integrationtests.IntegrationTestHttpClient;
import com.webapi.webapi.model.candidate.Candidate;
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
    void testSave() throws IOException, InterruptedException {
        Candidate candidate = new Candidate("Greg");
        Gson gson = new Gson();
        String json = gson.toJson(candidate);

        HttpResponse<Void> voidHttpResponse = IntegrationTestHttpClient.
                post("http://localhost:8080/api/v1/candidate", json);
    }

    @Test
    @Order(2)
    void testGet() throws IOException, InterruptedException {
        HttpResponse<Void> voidHttpResponse = IntegrationTestHttpClient.get("http://localhost:8080/api/v1/candidate/1");
    }
}
