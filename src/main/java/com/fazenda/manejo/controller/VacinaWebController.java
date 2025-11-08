package com.fazenda.manejo.controller;

import com.fazenda.manejo.business.VacinaService; // 1. Importa o Service de Vacina
import com.fazenda.manejo.infrastructure.dto.VacinaRequest; // 2. Importa os DTOs de Vacina
import com.fazenda.manejo.infrastructure.dto.VacinaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/vacinas") // 3. 💡 IMPORTANTE: Prefixo de URL para este CRUD
public class VacinaWebController {

    private final VacinaService vacinaService; // 4. Injeta o VacinaService

    /**
     * LISTAR (READ)
     * URL: GET /vacinas
     */
    @GetMapping
    public String listarVacinas(Model model) {
        List<VacinaResponse> vacinas = vacinaService.listarTodos();

        model.addAttribute("vacinas", vacinas);
        model.addAttribute("titulo", "Gerenciamento de Vacinas");

        return "lista-vacinas"; // (Vamos criar este HTML)
    }

    /**
     * MOSTRAR FORMULÁRIO DE CADASTRO (CREATE)
     * URL: GET /vacinas/novo
     */
    @GetMapping("/novo")
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("vacina", new VacinaRequest());
        model.addAttribute("titulo", "Cadastrar Nova Vacina");

        return "form-vacina"; // (Vamos criar este HTML)
    }

    /**
     * MOSTRAR FORMULÁRIO DE EDIÇÃO (UPDATE P-1)
     * URL: GET /vacinas/editar/{id}
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable Integer id, Model model) {
        VacinaRequest vacinaDto = vacinaService.buscarPorIdParaEdicao(id);

        model.addAttribute("vacina", vacinaDto);
        model.addAttribute("titulo", "Editar Vacina (ID: " + id + ")");

        return "form-vacina"; // Reutiliza o mesmo formulário
    }

    /**
     * SALVAR (CREATE P-2 ou UPDATE P-2)
     * URL: POST /vacinas/salvar
     */
    @PostMapping("/salvar")
    public String salvarVacina(@ModelAttribute("vacina") VacinaRequest request) {

        if (request.getId() == null) {
            vacinaService.salvarVacina(request);
        } else {
            vacinaService.atualizarVacina(request.getId(), request);
        }

        return "redirect:/vacinas"; // Redireciona para a lista
    }

    /**
     * EXCLUIR (DELETE)
     * URL: GET /vacinas/excluir/{id}
     */
    @GetMapping("/excluir/{id}")
    public String excluirVacina(@PathVariable Integer id) {
        vacinaService.deletarVacinaPorId(id);

        return "redirect:/vacinas";
    }
}