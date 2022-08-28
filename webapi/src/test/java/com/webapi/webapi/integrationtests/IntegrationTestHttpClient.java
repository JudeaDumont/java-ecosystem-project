package com.webapi.webapi.integrationtests;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class IntegrationTestHttpClient {

    private static HttpClient client = null;

    private IntegrationTestHttpClient() {
    }

    private static HttpClient getHttpClient() {
        if (client == null) {
            client = HttpClient.newHttpClient();
        }
        return client;
    }

    public static HttpResponse<Void> get(String url) throws IOException, InterruptedException {


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<Void> response = getHttpClient().send(request,
                HttpResponse.BodyHandlers.discarding());

        return response;
    }
}
