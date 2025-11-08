package com.fazenda.manejo.infrastructure.repository;

import com.fazenda.manejo.infrastructure.entitys.Pesagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PesagemRepository extends JpaRepository<Pesagem, Integer> {

    // 💡 MÉTODO NOVO (MUITO IMPORTANTE):
    // Busca todas as pesagens de um animal específico,
    // ordenadas pela data da mais recente para a mais antiga.
    // Isso será usado no "Histórico de Pesagens".
    List<Pesagem> findByAnimalIdOrderByDataPesagemDesc(Integer animalId);

    // (O Spring Data JPA entende esse nome de método e cria o SQL)
}