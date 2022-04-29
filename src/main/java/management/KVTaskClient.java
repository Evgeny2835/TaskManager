package management;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final URI url;
    private final HttpClient httpClient;
    private final String apiKey;

    public KVTaskClient(URI url) throws IOException, InterruptedException {
        httpClient = HttpClient.newHttpClient();
        this.url = url;
        URI urlPath = URI.create(url + "/register");
        HttpRequest request = HttpRequest.newBuilder().uri(urlPath).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            this.apiKey = response.body();
        } else {
            throw new IOException("Зарегистрироваться не удалось, код ответа сервера " + response.statusCode());
        }
    }

    public void put(String key, String json) {
        URI requestURI = URI.create(url + "/save/" + key + "?API_KEY=" + apiKey);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(requestURI)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println("Saved successfully");
            } else {
                throw new ManagerSaveException("Not saved response.statusCode=" + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    public String load(String key) {
        String json = "";
        URI requestURI = URI.create(url + "/load/" + key + "?API_KEY=" + apiKey);
        HttpRequest request = HttpRequest.newBuilder().uri(requestURI).GET().build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println("Loaded successfully");
                json = response.body();
            } else {
                throw new ManagerSaveException("Not loaded, response.statusCode=" + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException(e.getMessage());
        }
        return json;
    }
}