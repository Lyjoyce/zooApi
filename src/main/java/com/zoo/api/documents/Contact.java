package com.zoo.api.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "contacts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contact {

    @Id
    private String id;

    private String name;
    private String email;
    private String subject;
    private String message;

    @Builder.Default
    private LocalDateTime dateSent = LocalDateTime.now();
}
