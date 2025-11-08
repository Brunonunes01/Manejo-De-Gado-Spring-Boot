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
public class VacinaRequest {

    private Integer id; // Para o Update

    // Campos do formulário
    private String nome;
    private String fabricante;
    private String loteFabricacao;
    private LocalDate dataValidade; // O formulário usará <input type="date">
    private Integer diasCarenciaAbate;
    private String tipo;
}