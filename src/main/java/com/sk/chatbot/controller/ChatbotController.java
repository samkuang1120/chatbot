package com.sk.chatbot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sk.chatbot.data.ChatbotResponse;
import com.sk.chatbot.data.Message;
import com.sk.chatbot.data.OpenAIReqMessages;
import com.sk.chatbot.data.OpenAIResponse;
import com.sk.chatbot.repository.ChatMessageRepository;
import com.sk.chatbot.repository.entity.ChatMessage;
import com.sk.chatbot.service.ChatbotService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@RestController
@RequestMapping("api")
public class ChatbotController {

    @Value("${openai.api.secret-key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String openaiUrl;

    @Value("${openai.api.model}")
    private String model;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ChatbotService chatbotService;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @PostMapping(value = "/chat", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> generateText(@RequestBody Map<String, String> request, HttpServletRequest httpReq) {
        try {
            String prompt = request.get("message");

            System.out.println("prompt: " + prompt);

            ObjectMapper mapper = new ObjectMapper();

//// create JSON object
//            ObjectNode requestBody = mapper.createObjectNode();
//            requestBody.put("prompt", prompt);
////            requestBody.put("temperature", 0.5); // specify other parameters as needed
////            requestBody.put("max_tokens", 3200);
//            requestBody.put("model", model);
            OpenAIReqMessages reqBody = new OpenAIReqMessages(model);

            List<Message> histMessages = new ArrayList<Message>();
            Message mesg = new Message("user", prompt);
            histMessages.add(mesg);

            reqBody.setMessages(histMessages);

            /*HttpSession session = httpReq.getSession();
            List<OpenAIReqMessages.Message> histMessages = null;

            if(session.getAttribute("context") != null) {
                histMessages = (List<OpenAIReqMessages.Message>)session.getAttribute("context");
                if(histMessages.size() > 5) {
                    histMessages.re
                }*/
//            }



// convert JSON object to string

            String requestBodyString = mapper.writeValueAsString(reqBody);

            // create HTTP headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            // create HTTP entity with request body and headers
            HttpEntity<String> entity = new HttpEntity<>(requestBodyString, headers);

            System.out.println("apiKey = " + apiKey);
            System.out.println("openaiUrl = " + openaiUrl);
            System.out.println("model = " + model);
            System.out.println("requestBodyString = " + requestBodyString);

            // send API request and get response
            String url = openaiUrl;
            OpenAIResponse response = null;
            Map<String, Object> result = new HashMap<>();
            try{
                response = restTemplate.postForObject(url, entity, OpenAIResponse.class);
                if(response != null) {
                    result.put("message", response.getChoices().get(0).getMessage().getContent());

                    System.out.println("response message = " + response);

                    save(prompt, response);

                } else {
                    throw new Exception("connection to openai error!");
                }
            } catch (Exception e) {
                String exception = e.getMessage();
                result.put("message", "System issue: " + exception);
            }

            return ResponseEntity.ok().body(result);

        } catch (Exception e) {
            // handle exception
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void save(String prompt, OpenAIResponse response) {
        LocalDateTime timestamp = LocalDateTime.now();
        ChatMessage chatMessage = new ChatMessage(prompt, response.getChoices().get(0).getMessage().getContent(), timestamp);
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
