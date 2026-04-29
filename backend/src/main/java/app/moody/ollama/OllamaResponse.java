package app.moody.ollama;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OllamaResponse {
    private String model;
    private String response;
    private boolean done;
}
