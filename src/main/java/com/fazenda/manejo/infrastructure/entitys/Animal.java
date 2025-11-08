package com.fazenda.manejo.infrastructure.entitys;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
// (Faltarão outras Listas aqui, como Pesagem e AplicacaoVacina,
// mas vamos adicionar depois para manter o foco)

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "animal")
@Entity
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "brinco", unique = true, nullable = false) // Brinco é único e obrigatório
    private String brinco;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "data_entrada")
    private LocalDate dataEntrada;

    @Column(name = "sexo")
    private String sexo; // "MACHO" ou "FEMEA"

    @Column(name = "raca")
    private String raca; // "Nelore", "Angus"

    @Column(name = "status")
    private String status; // "ATIVO", "VENDIDO"

    @Column(name = "brinco_mae")
    private String brincoMae;

    @Column(name = "observacao", length = 1000) // 'length' aumenta o tamanho do campo
    private String observacao;

    // -----------------------------------------------------------------
    // 💡 AQUI ESTÁ A "DONA" DO RELACIONAMENTO
    // -----------------------------------------------------------------
    /**
     * Define um relacionamento de "Muitos-para-Um" com a entidade Lote.
     * MUITOS Animais podem pertencer a UM Lote.
     *
     * - 'fetch = FetchType.LAZY': (BOA PRÁTICA)
     * Só carregue o objeto 'Lote' quando alguém chamar 'animal.getLote()'.
     *
     * - '@JoinColumn(name = "lote_id")': (IMPORTANTE)
     * Isso diz ao Hibernate para criar uma coluna na tabela 'animal'
     * chamada 'lote_id'. Esta coluna será a Chave Estrangeira (Foreign Key)
     * que aponta para o 'id' da tabela 'lote'.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id")
    private Lote lote; // O Animal agora "sabe" a qual lote ele pertence
    @OneToMany(mappedBy = "animal", fetch = FetchType.LAZY)
    private java.util.List<Pesagem> pesagens;
    @OneToMany(mappedBy = "animal", fetch = FetchType.LAZY)
    private java.util.List<AplicacaoVacina> aplicacoes;
}