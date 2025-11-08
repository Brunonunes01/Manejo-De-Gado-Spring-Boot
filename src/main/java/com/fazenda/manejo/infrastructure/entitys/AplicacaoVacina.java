package com.fazenda.manejo.infrastructure.entitys;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "aplicacao_vacina") // Nome da tabela
@Entity
public class AplicacaoVacina {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "data_aplicacao", nullable = false)
    private LocalDate dataAplicacao;

    @Column(name = "dose")
    private String dose; // Ex: "5ml", "1 dose"

    @Column(name = "aplicador")
    private String aplicador; // Nome do funcionário

    @Column(name = "observacao", length = 500)
    private String observacao; // Ex: "Dose de reforço"

    // -----------------------------------------------------------------
    // RELACIONAMENTO COM ANIMAL (Muitas-para-Um)
    // -----------------------------------------------------------------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false) // 'nullable = false' -> Obrigatório
    private Animal animal;

    // -----------------------------------------------------------------
    // RELACIONAMENTO COM VACINA (Muitas-para-Um)
    // -----------------------------------------------------------------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacina_id", nullable = false) // 'nullable = false' -> Obrigatório
    private Vacina vacina;
}