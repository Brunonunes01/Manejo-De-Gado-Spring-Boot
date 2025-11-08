package com.fazenda.manejo.controller;

import com.fazenda.manejo.business.UsuarioService;
import com.fazenda.manejo.infrastructure.dto.UsuarioRequest; // Novo import para DTO de entrada
import com.fazenda.manejo.infrastructure.dto.UsuarioResponse; // Novo import para DTO de saída
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // CREATE (POST) - Recebe o DTO de Requisição
    @PostMapping
    public ResponseEntity<Void> salvarUsuario(@RequestBody UsuarioRequest request){
        usuarioService.salvarUsuario(request); // Service recebe o DTO
        return ResponseEntity.ok().build();
    }

    // READ (GET) - Retorna o DTO de Resposta
    @GetMapping
    public ResponseEntity<UsuarioResponse> buscarUsuarioPorEmail(@RequestParam String email){
        // Service retorna o DTO de Resposta
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorEmail(email));
    }

    // DELETE (DELETE) - Permanece inalterado, pois só usa parâmetro simples (String)
    @DeleteMapping
    public ResponseEntity<Void> deletarUsuarioPorEmail(@RequestParam String email){
        usuarioService.deletarUsuarioPorEmail(email);
        return ResponseEntity.ok().build();
    }

    // UPDATE (PUT) - Recebe o DTO de Requisição
    @PutMapping
    public ResponseEntity<Void> atualizarUsuarioPorId(@RequestParam Integer id,
                                                      @RequestBody UsuarioRequest request){
        usuarioService.atualizarUsuarioPorId(id, request); // Service recebe o DTO
        return ResponseEntity.ok().build();
    }
}