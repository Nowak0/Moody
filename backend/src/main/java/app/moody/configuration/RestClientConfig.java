package app.moody.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Value("${spring.ai.ollama.base-url}")
    private String OLLAMA_URL;

    @Bean
    public RestClient ollamaRestClient() {
        return RestClient.builder()
                .baseUrl(OLLAMA_URL)
                .build();
    }
}
