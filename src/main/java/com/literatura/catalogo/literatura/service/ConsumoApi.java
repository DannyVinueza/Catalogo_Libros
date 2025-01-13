package com.literatura.catalogo.literatura.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoApi {
    private static final String URLBASE = "https://gutendex.com/books?search=";
    private final ObjectMapper objectMapper = new ObjectMapper();
    public String obtenerDatos(String url){
        try {
            HttpClient client = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.ALWAYS)
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URLBASE + url))
                    .build();
            HttpResponse<String> response;
            response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode root = objectMapper.readTree(response.body());
            JsonNode results = root.get("results");
            if (results.size() > 0) {
                JsonNode book = results.get(0);
                return book.toPrettyString();
            } return null;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
