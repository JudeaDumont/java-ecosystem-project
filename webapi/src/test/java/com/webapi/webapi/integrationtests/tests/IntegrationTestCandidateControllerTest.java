package com.webapi.webapi.integrationtests.tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.webapi.webapi.integrationtests.IntegrationTestHttpClient;
import com.webapi.webapi.model.candidate.Candidate;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IntegrationTestCandidateControllerTest {

    @LocalServerPort
    private int port;

    private static Long idOfSaved = null;

    @Test
    @Order(2)
    void testSave() throws IOException, InterruptedException {
        Candidate candidate = new Candidate("Greg");
        Gson gson = new Gson();
        String json = gson.toJson(candidate);

        HttpResponse<String> voidHttpResponse = IntegrationTestHttpClient.
                post("http://localhost:" + port + "/api/v1/candidate", json);

        assert (voidHttpResponse.statusCode() == 200);
    }

    @Test
    @Order(3)
    void testGetAll() throws IOException, InterruptedException {
        HttpResponse<String> voidHttpResponse = IntegrationTestHttpClient.
                get("http://localhost:" + port + "/api/v1/candidate");

        assert (voidHttpResponse.statusCode() == 200);

        Gson gson = new Gson();
        List<Candidate> candidates = gson.fromJson(
                voidHttpResponse.body(),
                new TypeToken<List<Candidate>>() {
                }.getType());

        assert (candidates.size() != 0);
        idOfSaved = candidates.stream().findFirst().get().getId();
        assert (idOfSaved != null);
    }

    @Test
    @Order(4)
    void testGet() throws IOException, InterruptedException {
        HttpResponse<String> voidHttpResponse = IntegrationTestHttpClient.
                get("http://localhost:" + port + "/api/v1/candidate/" + idOfSaved.toString());

        assert (voidHttpResponse.statusCode() == 200);
    }

    @Test
    @Order(5)
    void testUpdate() throws IOException, InterruptedException {

        //Get that same candidate (need generated ID)
        HttpResponse<String> voidHttpGetResponse = IntegrationTestHttpClient.
                get("http://localhost:" + port + "/api/v1/candidate/" +
                        idOfSaved.toString());

        assert (voidHttpGetResponse.statusCode() == 200);

        Gson gson = new Gson();
        //Update
        Candidate candidateFromApp = gson.fromJson(
                voidHttpGetResponse.body(),
                Candidate.class);
        candidateFromApp.setName("craig");
        String candidateFromAppJson = gson.toJson(candidateFromApp);

        HttpResponse<String> voidHttpResponse = IntegrationTestHttpClient.
                put("http://localhost:" + port + "/api/v1/candidate",
                        candidateFromAppJson);

        assert (voidHttpResponse.statusCode() == 200);
    }

    @Test
    @Order(6)
    void testDelete() throws IOException, InterruptedException {

        HttpResponse<String> voidDeleteHttpResponse = IntegrationTestHttpClient.
                delete("http://localhost:" + port + "/api/v1/candidate/" + idOfSaved.toString());

        assert (voidDeleteHttpResponse.statusCode() == 200);
    }
}
