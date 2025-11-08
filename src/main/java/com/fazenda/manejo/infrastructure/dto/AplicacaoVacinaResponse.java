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
public class AplicacaoVacinaResponse {

    // Campos que vamos mostrar na lista
    private Integer id;
    private LocalDate dataAplicacao;
    private String dose;
    private String aplicador;

    // 💡 Nomes dos "Pais" (dos relacionamentos)
    private String brincoAnimal;
    private String nomeVacina;
}