package com.fazenda.manejo.infrastructure.entitys;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "pesagem") // Nome da tabela
@Entity
public class Pesagem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "data_pesagem", nullable = false)
    private LocalDate dataPesagem;

    @Column(name = "peso", nullable = false)
    private Double peso; // Em Kg (ex: 60.5)

    @Column(name = "observacao", length = 500)
    private String observacao; // Ex: "Pesagem de entrada"

    // -----------------------------------------------------------------
    // 💡 AQUI ESTÁ A "DONA" DO RELACIONAMENTO
    // -----------------------------------------------------------------
    /**
     * Define um relacionamento de "Muitos-para-Um" com a entidade Animal.
     * MUITAS Pesagens podem pertencer a UM Animal.
     *
     * - 'nullable = false': Garante que nenhuma pesagem
     * seja salva sem estar associada a um animal.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;
}