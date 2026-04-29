package app.moody.service;

import app.moody.ollama.OllamaRequest;
import app.moody.ollama.OllamaResponse;
import app.moody.dto.StatisticDTO;
import app.moody.entity.Mood;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Slf4j
@Service
public class AIAdviceService {
    @Value("${spring.ai.ollama.chat.model}")
    private String DEFAULT_MODEL;
    private final RestClient restClient;

    public AIAdviceService(RestClient ollamaRestClient) {
        this.restClient = ollamaRestClient;
    }

    public Optional<String> requestAdvice(Mood mood, StatisticDTO statisticDTO) {
        OllamaRequest request = new OllamaRequest(DEFAULT_MODEL, createPrompt(mood, statisticDTO), false);

        try {
            OllamaResponse response = restClient.post()
                    .uri("/api/generate")
                    .body(request)
                    .retrieve()
                    .body(OllamaResponse.class);

            if(response == null) {
                log.warn("Ollama returned a null response");
                return Optional.empty();
            } else {
                return Optional.of(response.getResponse());
            }
        } catch(Exception ex) {
            log.error("Error: {}", ex.getMessage());
            return Optional.empty();
        }
    }

    private String createPrompt(Mood mood, StatisticDTO statisticDTO) {
        return """
            You are a wellness expert who is asked to give a short and concise advice about health.
            You must provide a 1-2 sentence (KEEP IT SHORT) ritual, advice, activity, etc. that could improve a person's mood.
            You must consider given parameters concerning the mood and given statistics from past days.
            \n
            Parameter info:
                - value is a measure of person's mood ranging 1-10 (1 being absolute worst, 10 being the best mood)
                - note is an additional feedback
            \n
            Be smart with the mood value: if a person put a low value they really need help, if they put
            for example 9 or 10, they probably don't need much help. Adjust you response by taking mood value into account.
            Look into statistics - check trends (if it is up or down), check average mood from past days. It should
            guide you to giving the most suitable response. Remember, the provided stats are not updated with the newest
            mood submission you are receiving.
            \n
            IMPORTANT: Provide only the advice, no extra text.
            \n
            Here is the current mood:
            %s
            \n
            Here are recent statistics:
            %s
            """.formatted(mood.toString(), statisticDTO.toString());
    }
}
