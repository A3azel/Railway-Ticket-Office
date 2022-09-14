package com.example.springbootpetproject.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCommentsDTO {
    private Long id;
    private String userComments;
    private LocalDateTime publicationTime;
    private String username;
    private String trainNumber;
}
