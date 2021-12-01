package com.sparta.showmethecode.service;

import com.sparta.showmethecode.domain.ReviewRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static com.sparta.showmethecode.controller.SseController.sseEmitters;

@RequiredArgsConstructor
@Service
public class NotificationService {

    public void notifyAddCommentEvent(ReviewRequest reviewRequest) {

        Long userId = reviewRequest.getRequestUser().getId();

        if (sseEmitters.containsKey(userId)) {
            SseEmitter sseEmitter = sseEmitters.get(userId);
            try {
                sseEmitter.send(SseEmitter.event().name("addComment").data("댓글이 달렸습니다!"));
            } catch (Exception e) {
                sseEmitters.remove(userId);
            }
        }
    }
}



