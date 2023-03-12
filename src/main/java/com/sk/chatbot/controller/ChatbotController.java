package com.sk.chatbot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sk.chatbot.data.ChatbotResponse;
import com.sk.chatbot.data.OpenAIResponse;
import com.sk.chatbot.repository.ChatMessageRepository;
import com.sk.chatbot.repository.entity.ChatMessage;
import com.sk.chatbot.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
@RestController
@RequestMapping("api")
public class ChatbotController {

    @Value("${openai.api.secret-key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String openaiUrl;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ChatbotService chatbotService;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @PostMapping(value = "/chat", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> generateText(@RequestBody Map<String, String> request) {
        try {
            String prompt = request.get("message");

            System.out.println("prompt: " + prompt);

            ObjectMapper mapper = new ObjectMapper();

// create JSON object
            ObjectNode requestBody = mapper.createObjectNode();
            requestBody.put("prompt", prompt);
            requestBody.put("temperature", 0.5); // specify other parameters as needed
            requestBody.put("max_tokens", 3200);

// convert JSON object to string
            String requestBodyString = mapper.writeValueAsString(requestBody);

            // create HTTP headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            // create HTTP entity with request body and headers
            HttpEntity<String> entity = new HttpEntity<>(requestBodyString, headers);

            // send API request and get response
            String url = openaiUrl;
            OpenAIResponse response = null;
            Map<String, Object> result = new HashMap<>();
            try{
                response = restTemplate.postForObject(url, entity, OpenAIResponse.class);
                if(response != null) {
                    result.put("message", response.getChoices().get(0).getText());
                } else {
                    throw new Exception("connection to openai error!");
                }
            } catch (Exception e) {
                String exception = e.getMessage();
                result.put("message", "System issue: " + exception);
            }

            System.out.println("response message = " + response);

            save(prompt, response);
            return ResponseEntity.ok().body(result);

        } catch (Exception e) {
            // handle exception
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void save(String prompt, OpenAIResponse response) {
        LocalDateTime timestamp = LocalDateTime.now();
        ChatMessage chatMessage = new ChatMessage(prompt, response.getChoices().get(0).getText(), timestamp);
        chatMessageRepository.save(chatMessage);
    }

    @GetMapping("/")
    public String redirectToNewPath() {
        return "redirect:/api/chatmessages";
    }

    @GetMapping("/chatmessages")
    public ModelAndView getChatMessages() {
        List<ChatMessage> chatMessages = chatMessageRepository.findAllByOrderByTimestampDesc();
        ModelAndView modelAndView = new ModelAndView("chatmessages");
        List<ChatbotResponse> responses = chatMessages.stream().map(m -> new ChatbotResponse(m.getMessage(), m.getResponse(), m.getTimestamp())).collect(Collectors.toList());
        modelAndView.addObject("chatMessages", responses);
        return modelAndView;
    }

    @GetMapping("/chatmessage/{id}")
    public ModelAndView getChatMessageById(@PathVariable("id") long id) {
        ChatMessage chatMessage = chatMessageRepository.findById(id);
        ModelAndView modelAndView = new ModelAndView("chatmessage");
        ChatbotResponse response = new ChatbotResponse(chatMessage.getMessage(), chatMessage.getResponse(), chatMessage.getTimestamp());
        modelAndView.addObject("chatMessage", response);
        return modelAndView;
    }
}
