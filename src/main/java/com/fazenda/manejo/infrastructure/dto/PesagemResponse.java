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
public class PesagemResponse {

    // Campos que vamos mostrar na lista
    private Integer id;
    private LocalDate dataPesagem;
    private Double peso;
    private String observacao;

    // 💡 Identificador do "Pai":
    // Vamos mostrar o brinco do animal
    // para saber de quem é essa pesagem.
    private String brincoAnimal;
}