package com.hopeback.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hopeback.dto.chat.ChatMsgDto;
import com.hopeback.dto.chat.ChatRoomResDto;
import com.hopeback.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Slf4j
@Component
//WebSocketHandler를 상속받아 WebSocketHandkler를 구현
public class WebSocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper; //JSON 문자열로 변환하기 위한 객체
    private final ChatService chatService; // 채팅방 관련 비즈니스 로직을 처리할 서비스
    private final Map<WebSocketSession, String> sessionRoomIdMap = new ConcurrentHashMap<>();
    @Override
    //클라이언트가 서버로 연결을 시도할 때 호출
    protected void handleTextMessage(WebSocketSession session, TextMessage msg) throws Exception {
        String payload = msg.getPayload(); // 클라이언트가 전송한 메세지
        log.warn("{}",payload);
        //JSON 문자열을 ChatMsgDto 객체로 변환
        ChatMsgDto chatMsg = objectMapper.readValue(payload, ChatMsgDto.class);
        log.info("클라이언트로 부터 채팅방 아이디 받아오는지 확인 : {}", chatMsg.getRoomId());
        ChatRoomResDto chatRoom = chatService.findRoomById(chatMsg.getRoomId());
        log.info("채팅룸 아이디 ? : {}", chatMsg.getRoomId());
        if(chatRoom != null) { log.info("채팅룸의 getRegDate() : {}", chatRoom.getRegDate()); }
        else {log.warn("채팅방 존재하지 않음 : {}", chatMsg.getRoomId());}

        log.info("채팅룸의 getRegDate() : {}",  chatRoom.getRegDate());
        sessionRoomIdMap.put(session, chatMsg.getRoomId()); // 세션과 채팅방 ID 매핑
        log.info("채팅룸 세션 확인해야함!!!!@2222 : {}", sessionRoomIdMap);
        chatRoom.handlerActions(session, chatMsg, chatService);
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        //세션과 매핑된 채팅방 ID 가져오기
        try {
            log.warn("채팅방 종료 : {}", session);
            String roomId = sessionRoomIdMap.remove(session);
            ChatRoomResDto chatRoom = chatService.findRoomById(roomId);
            if (chatRoom != null) {
                chatRoom.handleSessionClosed(session, chatService);
            } else {
                log.warn("!!!!채팅방 찾기 아이디 확인해야하는데 어디있어!!!!! : {}", roomId);
            }
        }catch (Exception e) {
            log.error("채팅방 종료 중 에러", e);
        }
    }
}
