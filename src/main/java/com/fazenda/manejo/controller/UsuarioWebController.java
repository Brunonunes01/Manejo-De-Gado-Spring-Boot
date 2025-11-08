package com.fazenda.manejo.controller;

import com.fazenda.manejo.business.BusinessException; // 1. IMPORT (para o alerta de erro)
import com.fazenda.manejo.business.UsuarioService;
import com.fazenda.manejo.infrastructure.dto.UsuarioRequest;
import com.fazenda.manejo.infrastructure.dto.UsuarioResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // 2. IMPORT (para os alertas)

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/usuarios") // 3. 💡 MUDANÇA PRINCIPAL: Adiciona o prefixo
public class UsuarioWebController {

    private final UsuarioService usuarioService;

    // 4. 💡 MUDANÇA: Era @GetMapping("/") agora é só @GetMapping
    @GetMapping
    public String listarUsuarios(Model model) {
        List<UsuarioResponse> usuarios = usuarioService.listarTodos();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("titulo", "Lista de Usuários Cadastrados");
        return "lista-usuarios";
    }

    // 5. 💡 MUDANÇA: A URL agora será /usuarios/novo
    @GetMapping("/novo")
    public String mostrarFormularioDeCadastro(Model model) {
        model.addAttribute("usuario", new UsuarioRequest());
        model.addAttribute("titulo", "Cadastrar Novo Usuário");
        return "form-usuario";
    }

    // 6. 💡 MUDANÇA: A URL agora será /usuarios/editar/{id}
    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEdicao(@PathVariable Integer id, Model model) {
        UsuarioRequest usuarioDto = usuarioService.buscarUsuarioPorId(id);
        model.addAttribute("usuario", usuarioDto);
        model.addAttribute("titulo", "Editar Usuário (ID: " + id + ")");
        return "form-usuario";
    }

    // 7. 💡 MUDANÇA: A URL agora será /usuarios/excluir/{id}
    // (Também adicionei o tratamento de erro que fizemos no Lote)
    @GetMapping("/excluir/{id}")
    public String excluirUsuario(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.deletarUsuarioPorId(id);
            redirectAttributes.addFlashAttribute("successMessage", "Usuário excluído com sucesso!");
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro inesperado ao excluir usuário.");
        }
        return "redirect:/usuarios"; // 8. 💡 MUDANÇA: Redireciona para /usuarios
    }

    // 9. 💡 MUDANÇA: A URL agora será /usuarios/salvar
    @PostMapping("/salvar")
    public String salvarUsuario(@ModelAttribute("usuario") UsuarioRequest request, RedirectAttributes redirectAttributes) {

        String operacao = (request.getId() == null) ? "criado" : "atualizado";

        if (request.getId() == null) {
            usuarioService.salvarUsuario(request);
        } else {
            usuarioService.atualizarUsuarioPorId(request.getId(), request);
        }

        redirectAttributes.addFlashAttribute("successMessage", "Usuário " + operacao + " com sucesso!");
        return "redirect:/usuarios"; // 10. 💡 MUDANÇA: Redireciona para /usuarios
    }
}