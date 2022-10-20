package com.example.springbootpetproject.dto;

import lombok.*;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCommentsDTO {
    private Long id;
    private String created;
    private String updated;
    private String createdBy;
    private String lastModifiedBy;
    @Size(max = 5000, message = "Comment cannot be longer than 5000 characters")
    private String userComments;
    private String username;
    private String trainNumber;
}
