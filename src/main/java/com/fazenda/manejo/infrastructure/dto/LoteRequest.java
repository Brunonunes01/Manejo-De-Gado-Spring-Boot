package com.fazenda.manejo.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoteRequest {

    // Usado para saber se é um 'UPDATE'
    private Integer id;

    // Campos que virão do formulário
    private String nome;
    private String descricao;
    private String localizacao;
    private String status;

    // 💡 NOTA:
    // Não colocamos 'List<Animal>' aqui.
    // O cadastro de um Lote não deve envolver animais.
    // Os animais são associados ao lote em *outro* CRUD (o de Animal).
}