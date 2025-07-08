package com.ameen.chat_ai.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "chat")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_message")
    private String userMessage;

    @Column(name = "reply_ai_message")
    private String replyAiMessage;

    @Column(name = "user_audio_message")
    private String userAudioMessage;

    @Column(name = "ai_model")
    private String aiModel;  // replyAudio in Ai

    @Enumerated(EnumType.STRING)
    @Column(name = "upload_type")
    private UploadType uploadType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ChatStatus status;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "deleted_flag")
    private Boolean deletedFlag;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "modified_at")
    private Timestamp modifiedAt;

    @Column(name = "modified_by")
    private Long modifiedBy;

    public enum ChatStatus {
        SENT, DELIVERED, READ, FAILED, PENDING
    }

    public enum UploadType {
        IMAGE, AUDIO, DOCUMENT, LOCATION, CONTACT, GALLERY
    }

}
