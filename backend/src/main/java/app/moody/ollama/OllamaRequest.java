package app.moody.ollama;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OllamaRequest {
    private String model;
    private String prompt;
    private boolean stream;
}
