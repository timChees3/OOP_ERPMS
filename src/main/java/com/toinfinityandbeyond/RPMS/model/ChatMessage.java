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
}
