package com.sparta.showmethecode.controller;

import com.sparta.showmethecode.config.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Slf4j
@RestController
public class SseController {

    public static Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
    private final JwtUtils jwtUtils;

    @CrossOrigin
    @GetMapping(value = "/sub", consumes = MediaType.ALL_VALUE)
    public SseEmitter subscribe(@RequestParam String token) {

        Long userId = jwtUtils.getUserIdFromToken(token);

        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        try {
            sseEmitter.send(SseEmitter.event().name("connect"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        sseEmitters.put(userId, sseEmitter);

        sseEmitter.onCompletion(() -> sseEmitters.remove(userId));
        sseEmitter.onTimeout(() -> sseEmitters.remove(userId));
        sseEmitter.onError((e) -> sseEmitters.remove(userId));

        return sseEmitter;
    }
}


