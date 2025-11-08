// Em: src/main/java/com/faculdade/crud/infrastructure/dto/UsuarioRequest.java
package com.fazenda.manejo.infrastructure.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequest {
    private Integer id; // <-- ADICIONE ESTA LINHA
    private String email;
    private String nome;
}