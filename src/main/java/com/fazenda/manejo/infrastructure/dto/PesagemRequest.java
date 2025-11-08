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
public class PesagemRequest {

    private Integer id; // Para o Update

    // Campos do formulário
    private LocalDate dataPesagem;
    private Double peso;
    private String observacao;

    // 💡 Chave do relacionamento:
    // O formulário vai nos enviar o ID do Animal
    // que foi selecionado no dropdown.
    private Integer animalId;
}