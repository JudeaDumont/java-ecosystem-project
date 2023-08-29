package com.webapi.integrationtests.tests.Candidate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.webapi.integrationtests.IntegrationTestHttpClient;
import com.webapi.model.candidate.Candidate;
import testutil.AppYamlManager;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CandidateTestHelperFunctions {
    private final String candidateAPIPath;

    public CandidateTestHelperFunctions(int port) {
        this.candidateAPIPath = AppYamlManager.getTestingUrl() + port + "/api/v1/candidate/";
    }

    //todo: notice that this code suffers from wetness as well
    // for example, expecting 1 or 0 can call another method and pass the number it expects in as a parameter
    // instead of methods repeating each other greatly


    //todo: pull this from the application.yaml

    private List<Candidate> getCandidatesByName(String candidateName) throws IOException, InterruptedException {
        Gson gson = new Gson();
        HttpResponse<String> getNameHttpResponse = IntegrationTestHttpClient.
                get(candidateAPIPath + "getByName/" + candidateName);

        return gson.fromJson(
                getNameHttpResponse.body(),
                new TypeToken<List<Candidate>>() {
                }.getType());
    }

    private Candidate getCandidateWithUUIDName() {
        String uuid = UUID.randomUUID().toString();
        return new Candidate(uuid);
    }

    //this one will be private when moved
    private <T> String getJson(Class<T> classOfObject, T object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    private String postCandidate(Candidate newCandidate1) throws IOException, InterruptedException {
        return IntegrationTestHttpClient.
                post(candidateAPIPath, getJson(Candidate.class, newCandidate1)).body();
    }

    private String deleteCandidate(Candidate candidate) throws IOException, InterruptedException {
        return IntegrationTestHttpClient.
                delete(candidateAPIPath + candidate.getId().toString()).body();
    }

    private List<Candidate> getAllCandidates() throws IOException, InterruptedException {
        Gson gson = new Gson();
        HttpResponse<String> allCandidates = IntegrationTestHttpClient.
                get(candidateAPIPath);

        assert (allCandidates.statusCode() == 200);
        //if you try and fromJson the body into a Response object it throws up because timestamp type visibility,
        // therefor we have to unpack the object via strings as follows

        String body = allCandidates.body();
        JsonObject response = gson.fromJson(body, JsonObject.class);
        JsonObject data = gson.fromJson(response.get("data").toString(), JsonObject.class);
        List<Candidate> candidates = gson.fromJson(data.get("candidates").toString(), new TypeToken<List<Candidate>>() {
        }.getType());
        return candidates;
    }

    private String postCandidateReturnID(Candidate newCandidate1) throws IOException, InterruptedException {
        return IntegrationTestHttpClient.
                post(candidateAPIPath + "saveReturnID",
                        getJson(Candidate.class, newCandidate1)).body();
    }

    public String updateCandidateReturn1(Candidate candidateFromID) throws IOException, InterruptedException {
        Gson gson = new Gson();
        String putResponse = IntegrationTestHttpClient.
                put(candidateAPIPath,
                        gson.toJson(candidateFromID)).body();
        assert (Objects.equals(putResponse, "1"));
        return putResponse;
    }

    public String updateCandidateReturn0(Candidate candidateFromID) throws IOException, InterruptedException {
        Gson gson = new Gson();
        String putResponse = IntegrationTestHttpClient.
                put(candidateAPIPath,
                        gson.toJson(candidateFromID)).body();
        assert (Objects.equals(putResponse, "0"));
        return putResponse;
    }

    public List<Candidate> getCandidatesByNameReturn0(String candidateName) throws IOException, InterruptedException {
        List<Candidate> candidatesMatchingName = getCandidatesByName(candidateName);
        assert (candidatesMatchingName.isEmpty());
        return candidatesMatchingName;
    }

    private String updateCandidate(Candidate candidateFromID) throws IOException, InterruptedException {
        Gson gson = new Gson();
        return IntegrationTestHttpClient.
                put(candidateAPIPath,
                        gson.toJson(candidateFromID)).body();
    }

    public Candidate getCandidateByIDReturnCandidate(Long candidateID) throws IOException, InterruptedException {
        Gson gson = new Gson();
        HttpResponse<String> getByIdHttpResponse = IntegrationTestHttpClient.
                get(candidateAPIPath + candidateID);
        assert (getByIdHttpResponse.body() != null);
        return gson.fromJson(
                getByIdHttpResponse.body(), Candidate.class);
    }

    public Candidate saveReturnCandidate() throws IOException, InterruptedException {
        Candidate candidate = getCandidateWithUUIDName();
        String postResult = postCandidateReturnID(candidate);
        assert (!Objects.equals(postResult, null));
        assert (!Objects.equals(postResult, ""));
        assert (!Objects.equals(postResult, "0"));
        candidate.setId(Long.parseLong(postResult));
        return candidate;
    }

    public List<Candidate> getCandidatesByNameReturn1(String candidateName) throws IOException, InterruptedException {
        List<Candidate> candidatesMatchingName = getCandidatesByName(candidateName);
        assert (candidatesMatchingName.size() == 1);
        return candidatesMatchingName;
    }

    public void getCandidatesReturn0(Candidate candidate) throws IOException, InterruptedException {
        List<Candidate> deleteCheck = getCandidatesByName(candidate.getName());
        assert (deleteCheck.isEmpty());
    }

    public String deleteCandidateReturn1(Candidate candidate) throws IOException, InterruptedException {
        String deleteResponse = deleteCandidate(candidate);
        assert (Objects.equals(deleteResponse, "1"));
        return deleteResponse;
    }

    public String deleteCandidateReturn0(Candidate candidate) throws IOException, InterruptedException {
        String deleteResponse = deleteCandidate(candidate);
        assert (Objects.equals(deleteResponse, "0"));
        return deleteResponse;
    }

    public int getAllCandidatesReturnSize() throws IOException, InterruptedException {
        List<Candidate> allCandidates = getAllCandidates();
        return allCandidates.size();
    }
}
