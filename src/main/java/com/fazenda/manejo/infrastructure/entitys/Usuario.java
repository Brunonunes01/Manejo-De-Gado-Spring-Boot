package com.fazenda.manejo.infrastructure.entitys;

import jakarta.persistence.*;
import lombok.*;
import lombok.Builder;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "usuario")
@Entity

public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "email", unique = true)//unique o email é unico nao pode ter dois usuarios com o mesmo
    private String email;

    @Column(name = "nome")
    private String nome;
}
