package com.fazenda.manejo.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoteResponse {

    // Campos que vamos mostrar na tela
    private Integer id;
    private String nome;
    private String descricao;
    private String localizacao;
    private String status;

    // 💡 CAMPO EXTRA (MUITO ÚTIL):
    // Vamos adicionar um campo para mostrar na lista
    // quantos animais estão neste lote.
    private Integer quantidadeAnimais;

    // 💡 NOTA:
    // Também não colocamos 'List<Animal>' aqui.
    // Enviar a lista inteira seria pesado e desnecessário
    // para a tela de "lista de lotes". O número já ajuda.
}