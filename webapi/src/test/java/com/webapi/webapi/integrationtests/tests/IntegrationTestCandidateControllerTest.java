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
import java.util.Objects;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IntegrationTestCandidateControllerTest {

    @LocalServerPort
    private int port;

    @Test
    @Order(1)
    void test_Save_GetByName_Delete() throws IOException, InterruptedException {
        String uuid = UUID.randomUUID().toString();

        Gson gson = new Gson();
        String json = gson.toJson(new Candidate(uuid));

        HttpResponse<String> voidHttpResponse = IntegrationTestHttpClient.
                post("http://localhost:" + port + "/api/v1/candidate", json);

        assert (Objects.equals(voidHttpResponse.body(), "1"));

        HttpResponse<String> getNameHttpResponse = IntegrationTestHttpClient.
                get("http://localhost:" + port + "/api/v1/candidate/getByName/" + uuid);

        List<Candidate> candidatesMatchingName = gson.fromJson(
                getNameHttpResponse.body(),
                new TypeToken<List<Candidate>>() {
                }.getType());

        assert (candidatesMatchingName.size() == 1);

        assert (Objects.equals(candidatesMatchingName.get(0).getName(), uuid));

        HttpResponse<String> voidDeleteHttpResponse = IntegrationTestHttpClient.
                delete("http://localhost:" + port + "/api/v1/candidate/" + candidatesMatchingName.get(0).getId().toString());

        assert (Objects.equals(voidDeleteHttpResponse.body(), "1"));

        HttpResponse<String> getNameHttpResponse2 = IntegrationTestHttpClient.
                get("http://localhost:" + port + "/api/v1/candidate/getByName/" + uuid);

        List<Candidate> candidatesMatchingName2 = gson.fromJson(
                getNameHttpResponse2.body(),
                new TypeToken<List<Candidate>>() {
                }.getType());

        assert (candidatesMatchingName2.size() == 0);
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

        HttpResponse<String> save1 = IntegrationTestHttpClient.
                post("http://localhost:" + port + "/api/v1/candidate",
                        gson.toJson(new Candidate(uuid1)));

        HttpResponse<String> save2 = IntegrationTestHttpClient.
                post("http://localhost:" + port + "/api/v1/candidate",
                        gson.toJson(new Candidate(uuid2)));

        assert (Objects.equals(save1.body(), "1"));

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
//
//    @Test
//    @Order(3)
//    void test_SaveID_Get_Del() throws NonExistentCandidateException {
//        String uuid1 = UUID.randomUUID().toString();
//
//        Long id = candidateService.saveReturnID(new Candidate(uuid1));
//        assert (id != null);
//        assert (id != 0);
//
//        Candidate candidate = candidateService.get(id);
//        assert (candidate != null);
//
//        assert (candidateService.delete(candidate) == 1);
//    }
//
//    @Test
//    @Order(4)
//    void test_SaveID_Get_Update_GetByName_GetByName_Delete() throws NonExistentCandidateException {
//        String uuid1 = UUID.randomUUID().toString();
//        String changeUuid1 = UUID.randomUUID().toString();
//
//        Long id = candidateService.saveReturnID(new Candidate(uuid1));
//        assert (id != null);
//        assert (id != 0);
//
//        Candidate candidate = candidateService.get(id);
//        assert (candidate != null);
//
//        candidate.setName(changeUuid1);
//
//        int update = candidateService.update(candidate);
//
//        List<Candidate> candidatesMatchingName = candidateService.getByName(uuid1);
//        assert (candidatesMatchingName.size() == 0);
//
//        List<Candidate> candidatesMatchingChangeName = candidateService.getByName(changeUuid1);
//        assert (Objects.equals(candidatesMatchingChangeName.get(0).getName(), changeUuid1));
//
//        assert (update == 1);
//        assert (candidateService.delete(candidate) == 1);
//    }
//
//    @Test
//    @Order(5)
//    void test_SaveID_Get_Del_Del() throws NonExistentCandidateException {
//        String uuid1 = UUID.randomUUID().toString();
//
//        Long id = candidateService.saveReturnID(new Candidate(uuid1));
//        assert (id != null && id != 0);
//
//        Candidate candidate = candidateService.get(id);
//        assert (candidate != null);
//
//        assert (candidateService.delete(candidate) == 1);
//        assert (candidateService.delete(candidate) == 0);
//    }
//
//    @Test
//    @Order(6)
//    void test_SaveReturnID() throws NonExistentCandidateException {
//        Candidate kraken = new Candidate("kraken");
//        Long id = candidateService.saveReturnID(kraken);
//        assert (id != null && id != 0);
//        assert (candidateService.delete(kraken) == 1);
//    }
//
//    @Test
//    @Order(7)
//    void test_BadUpdate() throws NonExistentCandidateException {
//        String uuid1 = UUID.randomUUID().toString();
//        assert (candidateService.update(new Candidate(0L, uuid1)) == 0);
//    }
//
//    @Test
//    @Order(2)
//    void testSave() throws IOException, InterruptedException {
//        Candidate candidate = new Candidate("Greg");
//        Gson gson = new Gson();
//        String json = gson.toJson(candidate);
//
//        HttpResponse<String> voidHttpResponse = IntegrationTestHttpClient.
//                post("http://localhost:" + port + "/api/v1/candidate", json);
//
//        assert (voidHttpResponse.statusCode() == 200);
//    }
//
//    @Test
//    @Order(3)
//    void testGetAll() throws IOException, InterruptedException {
//        HttpResponse<String> voidHttpResponse = IntegrationTestHttpClient.
//                get("http://localhost:" + port + "/api/v1/candidate");
//
//        assert (voidHttpResponse.statusCode() == 200);
//
//        Gson gson = new Gson();
//        List<Candidate> candidates = gson.fromJson(
//                voidHttpResponse.body(),
//                new TypeToken<List<Candidate>>() {
//                }.getType());
//
//        assert (candidates.size() != 0);
//        idOfSaved = candidates.stream().findFirst().get().getId();
//        assert (idOfSaved != null);
//    }
//
//    @Test
//    @Order(4)
//    void testGet() throws IOException, InterruptedException {
//        HttpResponse<String> voidHttpResponse = IntegrationTestHttpClient.
//                get("http://localhost:" + port + "/api/v1/candidate/" + idOfSaved.toString());
//
//        assert (voidHttpResponse.statusCode() == 200);
//    }
//
//    @Test
//    @Order(5)
//    void testUpdate() throws IOException, InterruptedException {
//
//        //Get that same candidate (need generated ID)
//        HttpResponse<String> voidHttpGetResponse = IntegrationTestHttpClient.
//                get("http://localhost:" + port + "/api/v1/candidate/" +
//                        idOfSaved.toString());
//
//        assert (voidHttpGetResponse.statusCode() == 200);
//
//        Gson gson = new Gson();
//        //Update
//        Candidate candidateFromApp = gson.fromJson(
//                voidHttpGetResponse.body(),
//                Candidate.class);
//        candidateFromApp.setName("craig");
//        String candidateFromAppJson = gson.toJson(candidateFromApp);
//
//        HttpResponse<String> voidHttpResponse = IntegrationTestHttpClient.
//                put("http://localhost:" + port + "/api/v1/candidate",
//                        candidateFromAppJson);
//
//        assert (voidHttpResponse.statusCode() == 200);
//    }
//
//    @Test
//    @Order(6)
//    void testDelete() throws IOException, InterruptedException {
//
//        HttpResponse<String> voidDeleteHttpResponse = IntegrationTestHttpClient.
//                delete("http://localhost:" + port + "/api/v1/candidate/" + idOfSaved.toString());
//
//        assert (voidDeleteHttpResponse.statusCode() == 200);
//    }
}
