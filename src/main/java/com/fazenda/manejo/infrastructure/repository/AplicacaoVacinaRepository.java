package com.fazenda.manejo.infrastructure.repository;

import com.fazenda.manejo.infrastructure.entitys.AplicacaoVacina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AplicacaoVacinaRepository extends JpaRepository<AplicacaoVacina, Integer> {

    // 💡 Método para o "Histórico de Vacinas de um Animal"
    List<AplicacaoVacina> findByAnimalIdOrderByDataAplicacaoDesc(Integer animalId);

    // 💡 Método para verificação (evitar excluir vacina usada)
    List<AplicacaoVacina> findByVacinaId(Integer vacinaId);
}