package com.shinhan.friends_stock_admin.domain.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "game")
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private Long gameId;

    @Column(name = "game_type")
    @NotNull
    private String gameType;

    @Column(name = "character_name")
    @NotNull
    private String characterName;

    @Column(name = "character_desc", columnDefinition = "MEDIUMTEXT")
    private String characterDesc;

    @Column(name = "description", columnDefinition = "MEDIUMTEXT")
    private String description;

    @Column(name = "reward", columnDefinition = "TINYTEXT")
    private String reward;

    @Column(name = "created_at")
    @NotNull
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @NotNull
    @LastModifiedDate
    private LocalDateTime updatedAt;

}
