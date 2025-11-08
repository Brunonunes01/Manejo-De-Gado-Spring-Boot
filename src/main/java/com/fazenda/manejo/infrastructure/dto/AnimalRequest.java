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
public class AnimalRequest {

    // Campo para o 'UPDATE'
    private Integer id;

    // Campos do formulário
    private String brinco;
    private LocalDate dataNascimento;
    private LocalDate dataEntrada;
    private String sexo;
    private String raca;
    private String status;
    private String brincoMae;
    private String observacao;

    // -----------------------------------------------------------------
    // 💡 AQUI ESTÁ A CHAVE DO RELACIONAMENTO (INPUT)
    // -----------------------------------------------------------------
    /**
     * Quando o usuário salvar o formulário, o HTML vai enviar
     * o 'id' do Lote que foi selecionado no <select> (dropdown).
     * O Service vai usar esse ID para buscar a Entidade Lote.
     */
    private Integer loteId;

}