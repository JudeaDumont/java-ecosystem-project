package com.webapi.integrationtests;

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

    public static HttpResponse<String> get(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return getHttpClient().send(request,
                HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> delete(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .build();

        return getHttpClient().send(request,
                HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> post(String url, String contentBody) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(contentBody))
                .setHeader("content-type", "application/json")
                .build();
        return getHttpClient().send(request,
                HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> put(String url, String contentBody) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .PUT(HttpRequest.BodyPublishers.ofString(contentBody))
                .setHeader("content-type", "application/json")
                .build();
        return getHttpClient().send(request,
                HttpResponse.BodyHandlers.ofString());
    }
}
