package com.fazenda.manejo.infrastructure.repository;

import com.fazenda.manejo.infrastructure.entitys.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// 1. Dizemos que este é um Repositório (opcional, mas boa prática)
@Repository
// 2. Trocamos "Usuario" por "Lote". O ID do Lote também é Integer.
public interface LoteRepository extends JpaRepository<Lote, Integer> {

    // Podemos adicionar buscas customizadas aqui no futuro, como:
    // Optional<Lote> findByNome(String nome);
}