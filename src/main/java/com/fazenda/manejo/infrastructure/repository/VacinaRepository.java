package com.fazenda.manejo.infrastructure.repository;

import com.fazenda.manejo.infrastructure.entitys.Vacina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// Gerencia a entidade 'Vacina', cujo ID é 'Integer'
public interface VacinaRepository extends JpaRepository<Vacina, Integer> {

    // Método de busca customizado (pode ser útil no futuro)
    Optional<Vacina> findByNomeAndLoteFabricacao(String nome, String loteFabricacao);
}