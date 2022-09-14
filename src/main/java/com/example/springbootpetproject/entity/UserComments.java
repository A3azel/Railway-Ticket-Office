package com.example.springbootpetproject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_comments")
@Data
@NoArgsConstructor
public class UserComments implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_comment")
    private String userComments;

    @Column(name = "publication_time")
    private LocalDateTime publicationTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @OneToOne
    @JoinColumn(name = "train_id")
    private Train train;

    public UserComments(String userComments, LocalDateTime publicationTime, User user, Train train) {
        this.userComments = userComments;
        this.publicationTime = publicationTime;
        this.user = user;
        this.train = train;
    }
}
