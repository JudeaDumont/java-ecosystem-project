package com.webapi.integrationtests.tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.webapi.integrationtests.IntegrationTestHttpClient;
import com.webapi.model.candidate.Candidate;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IntegrationTestCandidateControllerTest {

    @LocalServerPort
    private int port;

    //todo: check to see if there is an easy way to run tests in parallel
    // foreshadowing: how many connections can postgresql handle?

    private List<Candidate> getCandidatesByName(String uuid) throws IOException, InterruptedException {
        Gson gson = new Gson();
        HttpResponse<String> getNameHttpResponse = IntegrationTestHttpClient.
                get("http://localhost:" + port + "/api/v1/candidate/getByName/" + uuid);

        return gson.fromJson(
                getNameHttpResponse.body(),
                new TypeToken<List<Candidate>>() {
                }.getType());
    }

    private Candidate getCandidateWithUUIDName() {
        String uuid = UUID.randomUUID().toString();
        return new Candidate(uuid);
    }

    private <T> String getJson(Class<T> classOfObject, T object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    private String postCandidate(Candidate newCandidate1) throws IOException, InterruptedException {
        return IntegrationTestHttpClient.
                post("http://localhost:" + port + "/api/v1/candidate", getJson(Candidate.class, newCandidate1)).body();
    }

    private String deleteCandidate(Candidate candidate) throws IOException, InterruptedException {
        return IntegrationTestHttpClient.
                delete("http://localhost:" + port + "/api/v1/candidate/" + candidate.getId().toString()).body();
    }

    @Test
    @Order(1)
    void test_Save_GetByName_Delete() throws IOException, InterruptedException {

        Candidate newCandidate1 = getCandidateWithUUIDName();

        String postResult = postCandidate(newCandidate1);

        assert (Objects.equals(postResult, "1"));

        List<Candidate> candidatesMatchingName = getCandidatesByName(newCandidate1.getName());

        assert (candidatesMatchingName.size() == 1);

        assert (Objects.equals(candidatesMatchingName.get(0).getName(), newCandidate1.getName()));

        String deleteResponse = deleteCandidate(candidatesMatchingName.get(0));

        assert (Objects.equals(deleteResponse, "1"));

        List<Candidate> deleteCheck = getCandidatesByName(newCandidate1.getName());

        assert (deleteCheck.size() == 0);
    }

    //todo: give a good explanation of each test, and why this one in particular seems so elaborate
    @Test
    @Order(2)
    void test_Save_Save_GetAll_GetByName_GetByName_GetAll_Delete_Delete_GetAll() throws IOException, InterruptedException {

        Gson gson = new Gson();
        String uuid1 = UUID.randomUUID().toString();
        String uuid2 = UUID.randomUUID().toString();

        HttpResponse<String> allCandidates = IntegrationTestHttpClient.
                get("http://localhost:" + port + "/api/v1/candidate");

        assert (allCandidates.statusCode() == 200);
        List<Candidate> candidates = gson.fromJson(
                allCandidates.body(),
                new TypeToken<List<Candidate>>() {
                }.getType());

        int candidatesSize = candidates.size();

        HttpResponse<String> saveReturnIDResponse = IntegrationTestHttpClient.
                post("http://localhost:" + port + "/api/v1/candidate",
                        gson.toJson(new Candidate(uuid1)));

        HttpResponse<String> save2 = IntegrationTestHttpClient.
                post("http://localhost:" + port + "/api/v1/candidate",
                        gson.toJson(new Candidate(uuid2)));

        assert (Objects.equals(saveReturnIDResponse.body(), "1"));

        assert (Objects.equals(save2.body(), "1"));

        HttpResponse<String> allCandidates2 = IntegrationTestHttpClient.
                get("http://localhost:" + port + "/api/v1/candidate");

        List<Candidate> candidatesAfterInserts = gson.fromJson(
                allCandidates2.body(),
                new TypeToken<List<Candidate>>() {
                }.getType());

        assert (candidatesAfterInserts.size() == candidatesSize + 2);

        HttpResponse<String> getNameHttpResponse = IntegrationTestHttpClient.
                get("http://localhost:" + port + "/api/v1/candidate/getByName/" + uuid1);

        List<Candidate> candidatesMatchingName = gson.fromJson(
                getNameHttpResponse.body(),
                new TypeToken<List<Candidate>>() {
                }.getType());

        assert (candidatesMatchingName.size() == 1);
        assert (Objects.equals(candidatesMatchingName.get(0).getName(), uuid1));

        HttpResponse<String> getNameHttpResponse2 = IntegrationTestHttpClient.
                get("http://localhost:" + port + "/api/v1/candidate/getByName/" + uuid2);

        List<Candidate> candidatesMatchingName2 = gson.fromJson(
                getNameHttpResponse2.body(),
                new TypeToken<List<Candidate>>() {
                }.getType());

        assert (candidatesMatchingName2.size() == 1);
        assert (Objects.equals(candidatesMatchingName2.get(0).getName(), uuid2));

        HttpResponse<String> deleteHttpResponse = IntegrationTestHttpClient.
                delete("http://localhost:" + port + "/api/v1/candidate/" + candidatesMatchingName.get(0).getId().toString());
        assert (Objects.equals(deleteHttpResponse.body(), "1"));

        HttpResponse<String> deleteHttpResponse2 = IntegrationTestHttpClient.
                delete("http://localhost:" + port + "/api/v1/candidate/" + candidatesMatchingName2.get(0).getId().toString());
        assert (Objects.equals(deleteHttpResponse2.body(), "1"));

        HttpResponse<String> candidatesAfterDeletes = IntegrationTestHttpClient.
                get("http://localhost:" + port + "/api/v1/candidate");

        assert (allCandidates.statusCode() == 200);
        List<Candidate> candidates3 = gson.fromJson(
                candidatesAfterDeletes.body(),
                new TypeToken<List<Candidate>>() {
                }.getType());

        assert (candidates3.size() == candidatesSize);
    }

    //todo: need a once over on all of the names, ensure they match the result they are actually getting
    // start a naming convention standard here in this project

    @Test
    @Order(3)
    void test_SaveID_Get_Del() throws IOException, InterruptedException {
        String uuid1 = UUID.randomUUID().toString();

        Gson gson = new Gson();

        HttpResponse<String> saveReturnIDResponse = IntegrationTestHttpClient.
                post("http://localhost:" + port + "/api/v1/candidate",
                        gson.toJson(new Candidate(uuid1)));

        assert (!Objects.equals(saveReturnIDResponse.body(), "0"));

        HttpResponse<String> getByNameHttpResponse2 = IntegrationTestHttpClient.
                get("http://localhost:" + port + "/api/v1/candidate/getByName/" + uuid1);

        List<Candidate> candidatesFromGetByName = gson.fromJson(
                getByNameHttpResponse2.body(),
                new TypeToken<List<Candidate>>() {
                }.getType());

        Candidate candidateFromApp = candidatesFromGetByName.get(0);

        HttpResponse<String> deleteHttpResponse2 = IntegrationTestHttpClient.
                delete("http://localhost:" + port + "/api/v1/candidate/" + candidateFromApp.getId().toString());
        assert (Objects.equals(deleteHttpResponse2.body(), "1"));
    }

    @Test
    @Order(4)
    void test_SaveID_Get_Update_GetByName_GetByName_Delete() throws IOException, InterruptedException {
        String uuid1 = UUID.randomUUID().toString();
        String changeUuid1 = UUID.randomUUID().toString();

        Gson gson = new Gson();

        HttpResponse<String> saveReturnIDResponse = IntegrationTestHttpClient.
                post("http://localhost:" + port + "/api/v1/candidate/saveReturnID",
                        gson.toJson(new Candidate(uuid1)));

        assert (!Objects.equals(saveReturnIDResponse.body(), ""));

        HttpResponse<String> getByIdHttpResponse2 = IntegrationTestHttpClient.
                get("http://localhost:" + port + "/api/v1/candidate/" + saveReturnIDResponse.body());

        Candidate candidateFromApp = gson.fromJson(
                getByIdHttpResponse2.body(), Candidate.class);

        assert (candidateFromApp != null);
        assert (candidateFromApp.getId().toString().equals(saveReturnIDResponse.body()));
        assert (Objects.equals(candidateFromApp.getName(), uuid1));

        candidateFromApp.setName(changeUuid1);

        HttpResponse<String> updateResponse = IntegrationTestHttpClient.
                put("http://localhost:" + port + "/api/v1/candidate",
                        gson.toJson(candidateFromApp));

        assert (Objects.equals(updateResponse.body(), "1"));

        HttpResponse<String> getByNameHttpResponse2 = IntegrationTestHttpClient.
                get("http://localhost:" + port + "/api/v1/candidate/getByName/" + changeUuid1);

        List<Candidate> candidatesFromGetByName2 = gson.fromJson(
                getByNameHttpResponse2.body(),
                new TypeToken<List<Candidate>>() {
                }.getType());

        assert (candidatesFromGetByName2.size() == 1);

        List<Candidate> candidatesFromGetByName3 = gson.fromJson(
                getByNameHttpResponse2.body(),
                new TypeToken<List<Candidate>>() {
                }.getType());

        assert (candidatesFromGetByName3.get(0).getId().toString().equals(saveReturnIDResponse.body()));

        HttpResponse<String> deleteHttpResponse2 = IntegrationTestHttpClient.
                delete("http://localhost:" + port + "/api/v1/candidate/" + saveReturnIDResponse.body());
        assert (Objects.equals(deleteHttpResponse2.body(), "1"));

        //todo: add comments for each on of these sequences of http requests, and make each comment match the name of the test
        // name should be short and descriptive, test 1 thing, and it should use domain specific language
    }

    @Test
    @Order(5)
    void test_SaveID_Get_Del_Del() throws IOException, InterruptedException {
        String uuid1 = UUID.randomUUID().toString();

        Gson gson = new Gson();

        HttpResponse<String> saveReturnIDResponse = IntegrationTestHttpClient.
                post("http://localhost:" + port + "/api/v1/candidate",
                        gson.toJson(new Candidate(uuid1)));

        assert (!Objects.equals(saveReturnIDResponse.body(), "0"));

        HttpResponse<String> getByNameHttpResponse2 = IntegrationTestHttpClient.
                get("http://localhost:" + port + "/api/v1/candidate/getByName/" + uuid1);

        List<Candidate> candidatesFromGetByName = gson.fromJson(
                getByNameHttpResponse2.body(),
                new TypeToken<List<Candidate>>() {
                }.getType());

        Candidate candidateFromApp = candidatesFromGetByName.get(0);

        HttpResponse<String> deleteHttpResponse2 = IntegrationTestHttpClient.
                delete("http://localhost:" + port + "/api/v1/candidate/" + candidateFromApp.getId().toString());
        assert (Objects.equals(deleteHttpResponse2.body(), "1"));

        HttpResponse<String> deleteHttpResponse3 = IntegrationTestHttpClient.
                delete("http://localhost:" + port + "/api/v1/candidate/" + candidateFromApp.getId().toString());
        assert (Objects.equals(deleteHttpResponse3.body(), "0"));
    }

    @Test
    @Order(6)
    void test_SaveReturnID() throws IOException, InterruptedException {
        Gson gson = new Gson();
        String uuid1 = UUID.randomUUID().toString();

        HttpResponse<String> saveReturnIDResponse = IntegrationTestHttpClient.
                post("http://localhost:" + port + "/api/v1/candidate/saveReturnID",
                        gson.toJson(new Candidate(uuid1)));

        HttpResponse<String> deleteHttpResponse2 = IntegrationTestHttpClient.
                delete("http://localhost:" + port + "/api/v1/candidate/" + saveReturnIDResponse.body());
        assert (Objects.equals(deleteHttpResponse2.body(), "1"));
    }

    @Test
    @Order(7)
    void test_BadUpdate() throws IOException, InterruptedException {
        String uuid1 = UUID.randomUUID().toString();
        Gson gson = new Gson();
        HttpResponse<String> updateResponse = IntegrationTestHttpClient.
                put("http://localhost:" + port + "/api/v1/candidate",
                        gson.toJson(new Candidate(0L, uuid1)));
        assert (Objects.equals(updateResponse.body(), "0"));
    }
}
