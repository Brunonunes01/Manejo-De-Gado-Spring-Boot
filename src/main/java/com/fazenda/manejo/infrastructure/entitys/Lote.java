package com.fazenda.manejo.infrastructure.entitys;

import jakarta.persistence.*;
import lombok.*;

import java.util.List; // 1. Importar a List

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "lote") // Define o nome da tabela no banco
@Entity
public class Lote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "nome", nullable = false) // 'nullable = false' torna o campo obrigatório
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "localizacao")
    private String localizacao;

    @Column(name = "status")
    private String status; // Ex: "ATIVO", "QUARENTENA"

    // -----------------------------------------------------------------
    // 💡 AQUI ESTÁ O RELACIONAMENTO (A MÁGICA ACONTECE AQUI)
    // -----------------------------------------------------------------
    /**
     * Define um relacionamento de "Um-para-Muitos" com a entidade Animal.
     * UM Lote pode ter MUITOS Animais.
     *
     * - 'mappedBy = "lote"': Diz ao Hibernate: "A entidade 'Animal' é a dona
     * deste relacionamento. Procure nela um campo chamado 'lote' para
     * fazer a ligação."
     *
     * - 'cascade = CascadeType.ALL': (OPCIONAL - CUIDADO)
     * Se você deletar um Lote, todos os Animais nele são deletados.
     * ***NÃO VAMOS USAR ISSO AGORA***, pois queremos o oposto
     * (impedir a exclusão).
     *
     * - 'fetch = FetchType.LAZY': (BOA PRÁTICA)
     * Só carregue a lista de animais do banco de dados quando
     * alguém explicitamente chamar o método 'lote.getAnimais()'.
     * Isso evita carregar dados desnecessários.
     */
    @OneToMany(mappedBy = "lote", fetch = FetchType.LAZY)
    private List<Animal> animais; // O Lote agora "sabe" quais animais ele possui
}