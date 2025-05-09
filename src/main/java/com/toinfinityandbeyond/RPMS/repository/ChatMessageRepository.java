package com.toinfinityandbeyond.RPMS.repository;

import com.toinfinityandbeyond.RPMS.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByConversationId(String conversationId);

    List<ChatMessage> findBySenderIdAndRecipientId(Long senderId, Long recipientId);

    List<ChatMessage> findByRecipientIdAndIsReadFalse(Long recipientId);

    List<ChatMessage> findByConversationIdOrderByTimestampAsc(String conversationId);

    List<ChatMessage> findByConversationIdAndRecipientIdAndIsReadFalse(String conversationId, Long recipientId);
}