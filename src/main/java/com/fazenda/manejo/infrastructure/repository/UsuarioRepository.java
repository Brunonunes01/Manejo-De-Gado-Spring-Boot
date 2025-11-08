package com.fazenda.manejo.infrastructure.repository;

import com.fazenda.manejo.infrastructure.entitys.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//nome da tabela e o tipo do id
public interface UsuarioRepository extends JpaRepository <Usuario, Integer>{
    Optional<Usuario> findByEmail (String email);

    @Transactional
        //caso der algum erro ele noa pode deletar
    void deleteByEmail(String email);
}
