package com.fazenda.manejo.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AplicacaoVacinaRequest {

    private Integer id; // Para o Update

    // Campos do formulário
    private LocalDate dataAplicacao;
    private String dose;
    private String aplicador;
    private String observacao;

    // 💡 Chaves dos 2 relacionamentos (dos dropdowns)
    private Integer animalId;
    private Integer vacinaId;
}