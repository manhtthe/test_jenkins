//package com.web.bookingKol.domain.user.rest.AI;
//
//import io.swagger.v3.oas.annotations.tags.Tag;
//import org.springframework.web.bind.annotation.*;
//import java.util.*;
//
//@RestController
//@Tag(name = "GPT")
//@RequestMapping("/consultationgpt")
//public class GptConsultationController {
//
//    private final ChatGPTService chatGPTService;
//
//    public GptConsultationController(ChatGPTService chatGPTService) {
//        this.chatGPTService = chatGPTService;
//    }
//
//    @PostMapping("/ask")
//    public Map<String, String> askConsultation(@RequestBody Map<String, String> request) {
//        String question = request.get("question");
//
//        if (question == null || question.trim().isEmpty()) {
//            return Map.of("answer", "Vui lòng nhập câu hỏi.");
//        }
//
//        String prompt = String.format("""
//            Bạn là một trợ lý AI hữu ích và thông minh.
//            Hãy trả lời câu hỏi của người dùng một cách tự nhiên, rõ ràng, đầy đủ.
//
//            Câu hỏi: %s
//            """, question);
//
//        String answer = chatGPTService.askChatGPT(prompt);
//        return Map.of("answer", answer);
//    }
//}
