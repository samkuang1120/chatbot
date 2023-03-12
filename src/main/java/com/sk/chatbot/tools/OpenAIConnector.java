package com.sk.chatbot.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenAIConnector {
    private final String API_KEY;
    private final String API_URL;

    public OpenAIConnector(String apiKey, String apiUrl) {
        this.API_KEY = apiKey;
        this.API_URL = apiUrl;
    }

    public String request(String model, String prompt) throws IOException {
        URL url = new URL(API_URL + "/completions");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + API_KEY);

        String input = "{\"model\":\"" + model + "\",\"prompt\":\"" + prompt + "\",\"temperature\":0.5,\"max_tokens\":100}";

        connection.setDoOutput(true);
        connection.getOutputStream().write(input.getBytes("UTF-8"));

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }
}
