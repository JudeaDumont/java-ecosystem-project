package com.webapi.integrationtests.tests.Candidate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.webapi.integrationtests.IntegrationTestHttpClient;
import com.webapi.model.candidate.Candidate;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CandidateTestHelperFunctions {

    private final int port;

    public CandidateTestHelperFunctions(int port) {
        this.port = port;
    }

    //todo: notice that this code suffers from wetness as well
    // for example, expecting 1 or 0 can call another method and pass the number it expects in as a parameter
    // instead of methods repeating each other greatly


    //todo: pull this from the application.yaml
    private final String springHost = "http://postgres:";

    private List<Candidate> getCandidatesByName(String candidateName) throws IOException, InterruptedException {
        Gson gson = new Gson();
        HttpResponse<String> getNameHttpResponse = IntegrationTestHttpClient.
                get(springHost + port + "/api/v1/candidate/getByName/" + candidateName);

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
                post(springHost + port + "/api/v1/candidate", getJson(Candidate.class, newCandidate1)).body();
    }

    private String deleteCandidate(Candidate candidate) throws IOException, InterruptedException {
        return IntegrationTestHttpClient.
                delete(springHost + port + "/api/v1/candidate/" + candidate.getId().toString()).body();
    }

    private List<Candidate> getAllCandidates() throws IOException, InterruptedException {
        Gson gson = new Gson();
        HttpResponse<String> allCandidates = IntegrationTestHttpClient.
                get(springHost + port + "/api/v1/candidate");

        assert (allCandidates.statusCode() == 200);
        return gson.fromJson(
                allCandidates.body(),
                new TypeToken<List<Candidate>>() {
                }.getType());
    }

    private String postCandidateReturnID(Candidate newCandidate1) throws IOException, InterruptedException {
        return IntegrationTestHttpClient.
                post(springHost + port + "/api/v1/candidate/saveReturnID",
                        getJson(Candidate.class, newCandidate1)).body();
    }

    public String updateCandidateReturn1(Candidate candidateFromID) throws IOException, InterruptedException {
        Gson gson = new Gson();
        String putResponse = IntegrationTestHttpClient.
                put(springHost + port + "/api/v1/candidate",
                        gson.toJson(candidateFromID)).body();
        assert (Objects.equals(putResponse, "1"));
        return putResponse;
    }

    public String updateCandidateReturn0(Candidate candidateFromID) throws IOException, InterruptedException {
        Gson gson = new Gson();
        String putResponse = IntegrationTestHttpClient.
                put(springHost + port + "/api/v1/candidate",
                        gson.toJson(candidateFromID)).body();
        assert (Objects.equals(putResponse, "0"));
        return putResponse;
    }

    public List<Candidate> getCandidatesByNameReturn0(String candidateName) throws IOException, InterruptedException {
        List<Candidate> candidatesMatchingName = getCandidatesByName(candidateName);
        assert (candidatesMatchingName.size() == 0);
        return candidatesMatchingName;
    }

    private String updateCandidate(Candidate candidateFromID) throws IOException, InterruptedException {
        Gson gson = new Gson();
        return IntegrationTestHttpClient.
                put(springHost + port + "/api/v1/candidate",
                        gson.toJson(candidateFromID)).body();
    }

    public Candidate getCandidateByIDReturnCandidate(Long candidateID) throws IOException, InterruptedException {
        Gson gson = new Gson();
        HttpResponse<String> getByIdHttpResponse = IntegrationTestHttpClient.
                get(springHost + port + "/api/v1/candidate/" + candidateID);
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
        assert (deleteCheck.size() == 0);
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
