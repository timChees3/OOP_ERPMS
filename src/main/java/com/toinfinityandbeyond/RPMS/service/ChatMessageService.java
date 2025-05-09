package com.toinfinityandbeyond.RPMS.service;

import com.toinfinityandbeyond.RPMS.exception.ResourceNotFoundException;
import com.toinfinityandbeyond.RPMS.model.ChatMessage;
import com.toinfinityandbeyond.RPMS.model.ChatMessage.MessageType;
import com.toinfinityandbeyond.RPMS.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public List<ChatMessage> getAllMessages()
    {
        return chatMessageRepository.findAll();
    }

    public ChatMessage getMessageById(Long id)
    {
        return chatMessageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ChatMessage not found with id: " + id));
    }

    public List<ChatMessage> getMessagesByConversation(String conversationId)
    {
        return chatMessageRepository.findByConversationIdOrderByTimestampAsc(conversationId);
    }

    public List<ChatMessage> getUnreadMessages(Long recipientId) {
        return chatMessageRepository.findByRecipientIdAndIsReadFalse(recipientId);
    }

    public ChatMessage createMessage(ChatMessage message) {
        if (message.getConversationId() == null || message.getConversationId().isBlank()) {
            String conv = ChatMessage.generateChatRoomId(
                    message.getSenderId(), message.getRecipientId());
            message.setConversationId(conv);
        }
        if (message.getType() == null) {
            message.setType(MessageType.CHAT);
        }
        return chatMessageRepository.save(message);
    }

    @Transactional
    public void markAsRead(Long messageId) {
        ChatMessage msg = getMessageById(messageId);
        if (!msg.getIsRead()) {
            msg.setIsRead(true);
            chatMessageRepository.save(msg);
        }
    }

    @Transactional
    public void markConversationAsRead(String conversationId, Long recipientId) {
        List<ChatMessage> unread = chatMessageRepository
                .findByConversationIdAndRecipientIdAndIsReadFalse(conversationId, recipientId);
        List<ChatMessage> updated = unread.stream()
                .peek(m -> m.setIsRead(true))
                .collect(Collectors.toList());
        chatMessageRepository.saveAll(updated);
    }
}
