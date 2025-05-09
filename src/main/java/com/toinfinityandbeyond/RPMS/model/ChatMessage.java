package com.toinfinityandbeyond.RPMS.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    public enum MessageType {
        CHAT, JOIN, LEAVE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "sender_name")
    private String senderName;

    @Column(name = "recipient_id")
    private Long recipientId;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    // This will help group messages by conversation
    @Column(name = "conversation_id")
    private String conversationId;

    @Column(name = "is_read")
    private Boolean isRead;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
        if (isRead == null) {
            isRead = false;
        }
    }

    public static String generateChatRoomId(Long userId1, Long userId2) {
        // Sort IDs to ensure consistency
        if (userId1 < userId2) {
            return "chat_" + userId1 + "_" + userId2;
        } else {
            return "chat_" + userId2 + "_" + userId1;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean read) {
        isRead = read;
    }
}
