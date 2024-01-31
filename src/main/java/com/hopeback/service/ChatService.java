package com.hopeback.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hopeback.dto.chat.ChatMsgDto;
import com.hopeback.dto.chat.ChatRoomReqDto;
import com.hopeback.dto.chat.ChatRoomResDto;
import com.hopeback.entity.chat.*;
import com.hopeback.repository.ChatRepository;
import com.hopeback.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final ObjectMapper objectMapper;
    private Map<String, ChatRoomResDto> chatRooms;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;

    @PostConstruct // 의존성 주입 이후 초기화 수행하는 메소드
    private void init() { chatRooms = new LinkedHashMap<>();}
    public List<ChatRoomResDto> findAllRoom() { return new ArrayList<>(chatRooms.values());}


    //채팅내역전체조회
    public List<ChatMsgDto> findAllChat() {
        List<Chat> chat = chatRepository.findAll();
        List<ChatMsgDto> chatMsgDtos = new ArrayList<>();
        for(Chat chat1 : chat) {
            chatMsgDtos.add(convertEntityToChatDto(chat1));
        }
        return chatMsgDtos;
    }

    //채팅방전체조회
    public List<ChatRoomResDto> findAllChatRoom() {
        List<ChatRoom> chatRoom = chatRoomRepository.findAllByOrderByCreatedAtDesc();
        List<ChatRoomResDto> chatRoomResDtos = new ArrayList<>();
        for(ChatRoom chatRoom1 : chatRoom) {
            chatRoomResDtos.add(convertEntityToRoomDto(chatRoom1));
        }
        return chatRoomResDtos;
    }

    public ChatRoomResDto findRoomById(String roomId) { return chatRooms.get(roomId);}

    //이전 채팅 가져오기
    public List<Chat> getRecentMsg(String roomId) { return chatRepository.findRecentMsg(roomId);}

    //방 개설하기
    public ChatRoomResDto createRoom(ChatRoomReqDto chatRoomDto) {
        String randomId = UUID.randomUUID().toString();
        log.info("UUID : " + randomId);
        ChatRoom chatRoomEntity = new ChatRoom(); //ChatRoom엔티티 객체 생성(채팅방 정보db저장 하려고)
        ChatRoomResDto chatRoom = ChatRoomResDto.builder()
                .roomId(randomId)
                .name(chatRoomDto.getName())
                .regDate(LocalDateTime.now())
                .build();
        chatRoomEntity.setRoomId(randomId);
        chatRoomEntity.setRoomName(chatRoomDto.getName());
        chatRoomEntity.setCreatedAt(LocalDateTime.now());
        chatRoomRepository.save(chatRoomEntity);
        chatRooms.put(randomId, chatRoom);
        return chatRoom;
    }

    public <T> void sendMsg(WebSocketSession session, T msg) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(msg)));
        }catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    //채팅 메세지 데이터베이스 저장하기
    public void saveMsg(String roomId, String sender, String msg) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("해당 채팅방이 존재하지 않습니다."));
        Chat chatMsg = new Chat();
        chatMsg.setChatRoom(chatRoom);
        chatMsg.setSender(sender);
        chatMsg.setMsg(msg);
        chatMsg.setSentAt(LocalDateTime.now());
        chatMsg.setActive("active");
        chatRepository.save(chatMsg);
    }

    //CatRoom 엔티티를 dto로 변환
    private ChatRoomResDto convertEntityToRoomDto(ChatRoom chatRoom) {
        ChatRoomResDto chatRoomResDto = new ChatRoomResDto();
        chatRoomResDto.setRoomId(chatRoom.getRoomId());
        chatRoomResDto.setName(chatRoom.getRoomName());
        return chatRoomResDto;
    }

    //Chat 엔티티 dto로 변환
    private ChatMsgDto convertEntityToChatDto(Chat chat) {
        ChatMsgDto chatMsgDto = new ChatMsgDto();
        chatMsgDto.setId(chat.getId());
        chatMsgDto.setRoomId(chat.getChatRoom().getRoomId());
        chatMsgDto.setMsg(chat.getMsg());
        chatMsgDto.setActive(chat.getActive());
        chatMsgDto.setSender(chat.getSender());
        return chatMsgDto;
    }
}
