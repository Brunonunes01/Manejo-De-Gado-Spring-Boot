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
public class AnimalResponse {

    // Campos que vamos mostrar na lista
    private Integer id;
    private String brinco;
    private String sexo;
    private String raca;
    private LocalDate dataNascimento;
    private String status;

    // -----------------------------------------------------------------
    // 💡 AQUI ESTÁ A CHAVE DO RELACIONAMENTO (OUTPUT)
    // -----------------------------------------------------------------
    /**
     * Na nossa lista de animais, queremos mostrar o NOME do lote,
     * e não o ID dele. O Service vai preencher este campo.
     */
    private String nomeLote;

    // (Poderíamos adicionar 'ultimoPeso' aqui no futuro,
    // quando o CRUD de Pesagem estiver pronto)
}