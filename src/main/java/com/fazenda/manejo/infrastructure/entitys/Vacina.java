package com.fazenda.manejo.infrastructure.entitys;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "vacina") // Nome da tabela
@Entity
public class Vacina {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "nome", nullable = false)
    private String nome; // Ex: "Aftosa"

    @Column(name = "fabricante")
    private String fabricante; // Ex: "Zoetis"

    @Column(name = "lote_fabricacao")
    private String loteFabricacao; // Lote do frasco

    @Column(name = "data_validade")
    private LocalDate dataValidade; // Validade do frasco

    @Column(name = "dias_carencia_abate")
    private Integer diasCarenciaAbate; // Em dias

    @Column(name = "tipo")
    private String tipo; // Ex: "Viral", "Obrigatória"

    // 💡 NOTA:
    // O @OneToMany com 'AplicacaoVacina' será adicionado
    // DEPOIS, quando criarmos o CRUD de Aplicação.
    // Por enquanto, a Vacina é só um catálogo.
    // ... (depois do campo 'tipo')

    // -----------------------------------------------------------------
    // 💡 ADICIONAR ISSO AO VACINA.JAVA
    // -----------------------------------------------------------------
    /**
     * Relacionamento "Um-para-Muitos" com AplicacaoVacina.
     * UMA Vacina pode estar em MUITAS aplicações.
     */
    @OneToMany(mappedBy = "vacina", fetch = FetchType.LAZY)
    private java.util.List<AplicacaoVacina> aplicacoes;
}