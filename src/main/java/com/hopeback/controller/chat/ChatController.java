package com.hopeback.controller.chat;

import com.hopeback.dto.chat.ChatMsgDto;
import com.hopeback.dto.chat.ChatRoomReqDto;
import com.hopeback.dto.chat.ChatRoomResDto;
import com.hopeback.entity.chat.Chat;
import com.hopeback.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    //채팅방 생성
    @PostMapping("/new")
    public ResponseEntity<String> createRoom(@RequestBody ChatRoomReqDto chatRoomReqDto) {
        ChatRoomResDto room = chatService.createRoom(chatRoomReqDto);
        return ResponseEntity.ok(room.getRoomId());
    }

    // 모든 채팅방 리스트(비활성화 된 채팅방까지 _ 어드민 활용 가능)
    @GetMapping("/roomList")
    public ResponseEntity<List<ChatRoomResDto>> findAllRoom() {
        List<ChatRoomResDto> chatRooms = chatService.findAllChatRoom();
        log.info("채팅방 정보 불러오기: {}", chatRooms);
        return ResponseEntity.ok(chatRooms);
    }
    //채팅방 리스트
    @GetMapping("/freeList")
    public ResponseEntity<List<ChatRoomResDto>> findByFreeRoom() {
        return ResponseEntity.ok(chatService.findFreeRoom());
    }

    // 채팅방 참여자 목록 가져오기
    @GetMapping("/members/{roomId}")
    public ResponseEntity<List<String>> getChatMembers(@PathVariable String roomId) {
        List<String> chatMembers = chatService.getChatMembers(roomId);
        log.info("챗 컨트롤러 채팅방 참여자 어떻게 가져오나 : {}", chatService.getChatMembers(roomId));
        return ResponseEntity.ok(chatMembers);
    }

    // 전체 채팅 내역 리스트
    @GetMapping("/chatList")
    public ResponseEntity<List<ChatMsgDto>> findAll() {return ResponseEntity.ok(chatService.findAllChat()); }

    // 방 정보 가져오기
    @GetMapping("/room/{roomId}")
    public ResponseEntity<ChatRoomResDto> findRoomById(@PathVariable String roomId) {
        log.info("채팅방 정보 가져가기 : {}", chatService.findRoomById(roomId));
        return ResponseEntity.ok(chatService.findRoomById(roomId));
    }

    // 메세지 저장하기
    @PostMapping("/message")
    public ResponseEntity<ChatMsgDto> saveMessage(@RequestBody ChatMsgDto chatMsgDto) {
        chatService.saveMsg(chatMsgDto.getRoomId(), chatMsgDto.getSender(), chatMsgDto.getMsg());
        return ResponseEntity.ok(chatMsgDto);
    }

    // 해당 방의 최근 메세지 불러오기
    @GetMapping("/message/{roomId}")
    public List<Chat> getRecentMsg(@PathVariable String roomId) {
        return chatService.getRecentMsg(roomId);
    }

    // 채팅 내역 삭제
    @DeleteMapping("/delChat/{id}")
    public ResponseEntity<Boolean> deleteChat(@PathVariable Long id) {
        boolean isTrue = chatService.deleteChat(id);
        return ResponseEntity.ok(isTrue);
    }
    // 채팅방 삭제
    @DeleteMapping("/delRoom/{roomId}")
    public ResponseEntity<Boolean> deleteRoom(@PathVariable String roomId) {
        boolean isTrue = chatService.deleteRoom(roomId);
        return ResponseEntity.ok(isTrue);
    }
}
