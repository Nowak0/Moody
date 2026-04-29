package app.moody.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
