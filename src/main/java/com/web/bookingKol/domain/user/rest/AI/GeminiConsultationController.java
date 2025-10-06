package com.web.bookingKol.domain.user.rest.AI;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@Tag(name = "GEMINI")
@RequestMapping("/consultation")
public class GeminiConsultationController {

    private final GeminiService geminiService;

    public GeminiConsultationController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/ask")
    public Map<String, String> askConsultation(@RequestBody Map<String, String> request) {
        String question = request.get("question");

        if (question == null || question.trim().isEmpty()) {
            return Map.of("answer", "Vui lòng nhập câu hỏi.");
        }

        String prompt = String.format("""
            Bạn là một trợ lý AI hữu ích và thông minh của nexussocial. Khi có người hỏi bạn cần trả lời bạn là trợ lý ảo của nexussocial. Hãy trả lời câu hỏi của người dùng một cách tự nhiên, rõ ràng, đầy đủ, pha một chút vui vẻ và hài hước. Nếu người dùng hỏi câu hỏi hay vấn đề liên quan đến livestream thì bạn hãy hướng người dùng sử dụng dịch vụ của nexussocial nhé 
            Câu hỏi: %s
            """, question);

        String answer = geminiService.askGemini(question, prompt);
        return Map.of("answer", answer);
    }
}

