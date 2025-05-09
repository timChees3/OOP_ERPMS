package com.toinfinityandbeyond.RPMS.controller;

import com.toinfinityandbeyond.RPMS.model.ChatMessage;
import com.toinfinityandbeyond.RPMS.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat/messages")
public class ChatMessageController {

    @Autowired
    private ChatMessageService chatMessageService;

    /**
     * GET /api/chat/messages
     * Retrieve all chat messages
     */
    @GetMapping
    public ResponseEntity<List<ChatMessage>> getAllMessages() {
        List<ChatMessage> messages = chatMessageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    /**
     * GET /api/chat/messages/{id}
     * Retrieve a single message by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ChatMessage> getMessageById(@PathVariable Long id) {
        ChatMessage message = chatMessageService.getMessageById(id);
        return ResponseEntity.ok(message);
    }

    /**
     * GET /api/chat/messages/conversation/{conversationId}
     * Retrieve all messages in a conversation
     */
    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<ChatMessage>> getMessagesByConversation(
            @PathVariable String conversationId
    ) {
        List<ChatMessage> messages = chatMessageService.getMessagesByConversation(conversationId);
        return ResponseEntity.ok(messages);
    }

    /**
     * GET /api/chat/messages/unread/{recipientId}
     * Retrieve all unread messages for a recipient
     */
    @GetMapping("/unread/{recipientId}")
    public ResponseEntity<List<ChatMessage>> getUnreadMessages(
            @PathVariable Long recipientId
    ) {
        List<ChatMessage> messages = chatMessageService.getUnreadMessages(recipientId);
        return ResponseEntity.ok(messages);
    }

    /**
     * POST /api/chat/messages
     * Create a new chat message
     */
    @PostMapping
    public ResponseEntity<ChatMessage> createMessage(@RequestBody ChatMessage message) {
        ChatMessage created = chatMessageService.createMessage(message);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * PUT /api/chat/messages/{id}/read
     * Mark a single message as read
     */
    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markMessageAsRead(@PathVariable Long id) {
        chatMessageService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * PUT /api/chat/messages/conversation/{conversationId}/read/{recipientId}
     * Mark all messages in a conversation as read for a recipient
     */
    @PutMapping("/conversation/{conversationId}/read/{recipientId}")
    public ResponseEntity<Void> markConversationAsRead(
            @PathVariable String conversationId,
            @PathVariable Long recipientId
    ) {
        chatMessageService.markConversationAsRead(conversationId, recipientId);
        return ResponseEntity.noContent().build();
    }
}