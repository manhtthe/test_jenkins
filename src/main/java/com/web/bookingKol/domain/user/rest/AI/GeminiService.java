package com.web.bookingKol.domain.user.rest.AI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.bookingKol.domain.user.models.AiConsultationLog;
import com.web.bookingKol.domain.user.repositories.AiConsultationLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent";

    private final RestTemplate restTemplate = new RestTemplate();
    private final AiConsultationLogRepository logRepository;

    public String askGemini(String userQuestion, String prompt) {
        Map<String, Object> message = Map.of(
                "parts", List.of(Map.of("text", prompt)),
                "role", "user"
        );
        Map<String, Object> body = Map.of("contents", List.of(message));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        String answer;

        try {
            String url = GEMINI_API_URL + "?key=" + apiKey;
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            JsonNode textNode = root.path("candidates")
                    .path(0)
                    .path("content")
                    .path("parts")
                    .path(0)
                    .path("text");

            if (textNode.isMissingNode()) {
                answer = "Không có phản hồi từ chatbot.";
            } else {
                answer = textNode.asText();
            }


        } catch (Exception e) {
            answer = "Chatbot API lỗi: " + e.getMessage();
        }
        AiConsultationLog log = AiConsultationLog.builder()
                .question(userQuestion)
                .answer(answer)
                .createdAt(Instant.now())
                .build();
        logRepository.save(log);

        return answer;
    }


}
