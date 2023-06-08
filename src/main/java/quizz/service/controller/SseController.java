package quizz.service.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import quizz.service.models.increment;
import quizz.service.models.question;
import quizz.service.models.quiz;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/sse")
public class SseController {
    private List<SseEmitter> emitters = new java.util.ArrayList<>();
    private long lastId = 0;

    @GetMapping("/subscribe")
    public SseEmitter subscribe() throws IOException {
        SseEmitter sseEmitter = new SseEmitter(600000L);
        sseEmitter.send(SseEmitter.event()
                    .name("message")
                    .id("" + lastId++)
                    .data("connexion"));
        sseEmitter.onCompletion(() -> this.emitters.remove(sseEmitter));
        this.emitters.add(sseEmitter);
        return sseEmitter;
    }

    @PostMapping("/message")
    private void sendMessage(@RequestBody Object data) throws IOException {
        for(SseEmitter emitter : this.emitters) {
            emitter.send(SseEmitter.event()
                    .name("message")
                    .id("" + ++lastId)
                    .data(data));
        }
    }

    @PostMapping("/quiz")
    private void sendQuiz() throws IOException {
        question question = new question("what does SSE stends for ?", new String[]{"Send-server enabling", "server-send Event", "Server-send enabler"}, "server-send Event");
        quiz quiz = new quiz("quizz", question);
        for (SseEmitter emitter : this.emitters) {
            emitter.send(SseEmitter.event()
                    .name("message")
                    .id("" + ++lastId)
                    .data(quiz));
        }
    }

    @PostMapping("/response")
    private void sendReponse() throws IOException {
        increment increment = new increment("increment", 1);
        for (SseEmitter emitter : this.emitters) {
            emitter.send(SseEmitter.event()
                    .name("message")
                    .id("" + ++lastId)
                    .data(increment));
        }
    }

}