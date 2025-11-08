package com.fazenda.manejo.business;

import com.fazenda.manejo.infrastructure.dto.UsuarioRequest;
import com.fazenda.manejo.infrastructure.dto.UsuarioResponse;
import com.fazenda.manejo.infrastructure.entitys.Usuario;
import com.fazenda.manejo.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor; // 1. NOVO IMPORT
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor // 2. ADICIONE ESTA ANOTAÇÃO
public class UsuarioService {

    private final UsuarioRepository repository; // 3. APENAS DECLARE O REPOSITORY

    // 4. REMOVA O CONSTRUTOR MANUAL (public UsuarioService(...) { ... })

    // 1. Método Salvar: (Permanece igual)
    public void salvarUsuario (UsuarioRequest request){
        Usuario novoUsuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .build();
        repository.saveAndFlush(novoUsuario);
    }

    // 2. Método Buscar por Email: (Permanece igual)
    public UsuarioResponse buscarUsuarioPorEmail(String email){
        Usuario usuarioEntity = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email não Encontrado"));

        return UsuarioResponse.builder()
                .id(usuarioEntity.getId())
                .nome(usuarioEntity.getNome())
                .email(usuarioEntity.getEmail())
                .build();
    }

    // 3. Método Listar Todos: (Permanece igual)
    public List<UsuarioResponse> listarTodos() {
        return repository.findAll().stream()
                .map(usuarioEntity -> UsuarioResponse.builder()
                        .id(usuarioEntity.getId())
                        .nome(usuarioEntity.getNome())
                        .email(usuarioEntity.getEmail())
                        .build())
                .toList();
    }

    // 4. Método Deletar por Email: (Permanece igual)
    public void deletarUsuarioPorEmail(String email){
        repository.deleteByEmail(email);
    }

    // 5. Método Atualizar: (Permanece igual)
    public void atualizarUsuarioPorId(Integer id, UsuarioRequest request){
        Usuario usuarioEntity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario Não Encontrado"));

        Usuario usuarioAtualizado = Usuario.builder()
                .email(request.getEmail() != null ? request.getEmail() : usuarioEntity.getEmail())
                .nome(request.getNome() != null ? request.getNome() : usuarioEntity.getNome())
                .id(usuarioEntity.getId()) // Importante manter o ID na atualização
                .build();

        repository.saveAndFlush(usuarioAtualizado);
    }

    // -----------------------------------------------------------------
    // 💡 6. NOVO: Método para Excluir (para a página Web)
    // -----------------------------------------------------------------
    public void deletarUsuarioPorId(Integer id) {
        // O findById verifica se o usuário existe antes de deletar
        Usuario usuarioEntity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario Não Encontrado para deletar"));

        repository.delete(usuarioEntity);
        // ou pode usar repository.deleteById(id); que é mais direto
    }

    // -----------------------------------------------------------------
    // 💡 7. NOVO: Método para Buscar (para preencher o form de edição)
    // -----------------------------------------------------------------
    public UsuarioRequest buscarUsuarioPorId(Integer id) {
        Usuario usuarioEntity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario Não Encontrado para editar"));

        // Converte a Entidade para o DTO de *Requisição* (o mesmo usado pelo form)
        return UsuarioRequest.builder()
                .id(usuarioEntity.getId())
                .nome(usuarioEntity.getNome())
                .email(usuarioEntity.getEmail())
                .build();
    }
}