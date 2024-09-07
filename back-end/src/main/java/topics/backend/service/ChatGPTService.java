package topics.backend.service;

import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class ChatGPTService {
  private final String apiKey = System.getenv("OPENAI_API_KEY");
  private final String model = "gpt-4o-mini";

  private static final String URL = "https://api.openai.com/v1/chat/completions";
  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final HttpClient httpClient = HttpClient.newBuilder()
          .version(HttpClient.Version.HTTP_2)
          .connectTimeout(Duration.ofSeconds(10))
          .build();

  public String getResponse(String prompt) {
    try {
      String body = objectMapper.writeValueAsString(Map.of(
              "model", model,
              "messages", List.of(
                      Map.of("role", "system", "content", "You are an assistant that responds in markdown format. Provide concise summaries and clear questions in markdown."),
                      Map.of("role", "user", "content", prompt)
              )
      ));

      HttpRequest request = HttpRequest.newBuilder()
              .uri(URI.create(URL))
              .header("Content-Type", "application/json")
              .header("Authorization", "Bearer " + apiKey)
              .POST(HttpRequest.BodyPublishers.ofString(body))
              .build();

      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == 200) {
        return extractMessageFromJSONResponse(response.body());
      } else {
        throw new RuntimeException("Error en la respuesta de la API: " + response.statusCode() + " " + response.body());
      }
    } catch (Exception e) {
      throw new RuntimeException("Error al interactuar con ChatGPT", e);
    }
  }

  private String extractMessageFromJSONResponse(String response) throws Exception {
    JsonNode jsonNode = objectMapper.readTree(response);
    return jsonNode.path("choices").get(0).path("message").path("content").asText();
  }
}