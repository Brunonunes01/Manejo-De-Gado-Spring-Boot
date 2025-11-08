package com.fazenda.manejo.infrastructure.repository;

import com.fazenda.manejo.infrastructure.entitys.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Integer> {

    // 💡 BOA PRÁTICA:
    // O 'brinco' é o identificador único na fazenda.
    // Vamos criar um método de busca para ele,
    // pois vamos usá-lo muito.
    Optional<Animal> findByBrinco(String brinco);

}