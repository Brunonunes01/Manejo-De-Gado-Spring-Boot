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
public class VacinaResponse {

    // Campos que vamos mostrar na lista
    private Integer id;
    private String nome;
    private String fabricante;
    private String loteFabricacao;
    private LocalDate dataValidade; // Vamos mostrar a validade na lista
    private Integer diasCarenciaAbate;

    // (Não precisamos do 'tipo' na lista, para simplificar)
}
