package com.lucas.multidb.oracle.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Table(name = "users")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLegacy {

    @Id
    private Long id;
    private String nome;
    private String cpf;
    private String email;
}
