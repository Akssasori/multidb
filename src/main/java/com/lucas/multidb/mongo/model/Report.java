package com.lucas.multidb.mongo.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {
    private String status;
    private LocalDateTime createAt;
    private Integer qtd;
}
