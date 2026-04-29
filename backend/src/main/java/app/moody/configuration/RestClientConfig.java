package app.moody.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Value("${spring.ai.ollama.base-url}")
    private String OLLAMA_URL;

    @Bean
    public RestClient ollamaRestClient() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(60_000);
        factory.setReadTimeout(60_000);

        return RestClient.builder()
                .requestFactory(factory)
                .baseUrl(OLLAMA_URL)
                .build();
    }
}
