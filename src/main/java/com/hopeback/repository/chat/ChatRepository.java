package com.hopeback.repository.chat;

import com.hopeback.entity.chat.Chat;
import com.hopeback.entity.chat.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository <Chat, Long> {
    @Query(value = "SELECT * FROM chat WHERE room_id = ?1 ORDER BY sent_at DESC LIMIT 50", nativeQuery = true)
    List<Chat> findRecentMsg(String roomId);
    Page<Chat> findAllByOrderByIdDesc(Pageable pageable);

    Optional<List<Chat>> findByChatRoom(ChatRoom chatRoom);

    Optional<Chat> findById(String chatId);
}
