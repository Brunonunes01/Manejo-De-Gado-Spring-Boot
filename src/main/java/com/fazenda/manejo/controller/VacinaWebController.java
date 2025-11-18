package com.fazenda.manejo.controller;

import com.fazenda.manejo.business.BusinessException; // Importar
import com.fazenda.manejo.business.VacinaService;
import com.fazenda.manejo.infrastructure.dto.VacinaRequest;
import com.fazenda.manejo.infrastructure.dto.VacinaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Importar

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/vacinas")
public class VacinaWebController {

    private final VacinaService vacinaService;

    // ... (listar, form cadastro, form edição iguais) ...

    @GetMapping
    public String listarVacinas(Model model) {
        List<VacinaResponse> vacinas = vacinaService.listarTodos();
        model.addAttribute("vacinas", vacinas);
        model.addAttribute("titulo", "Gerenciamento de Vacinas");
        return "lista-vacinas";
    }

    @GetMapping("/novo")
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("vacina", new VacinaRequest());
        model.addAttribute("titulo", "Cadastrar Nova Vacina");
        return "form-vacina";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable Integer id, Model model) {
        VacinaRequest vacinaDto = vacinaService.buscarPorIdParaEdicao(id);
        model.addAttribute("vacina", vacinaDto);
        model.addAttribute("titulo", "Editar Vacina (ID: " + id + ")");
        return "form-vacina";
    }

    @PostMapping("/salvar")
    public String salvarVacina(@ModelAttribute("vacina") VacinaRequest request, RedirectAttributes redirectAttributes) {
        try {
            if (request.getId() == null) {
                vacinaService.salvarVacina(request);
                redirectAttributes.addFlashAttribute("successMessage", "Vacina cadastrada com sucesso!");
            } else {
                vacinaService.atualizarVacina(request.getId(), request);
                redirectAttributes.addFlashAttribute("successMessage", "Vacina atualizada com sucesso!");
            }
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/vacinas/novo"; // Ou volta para o form
        }
        return "redirect:/vacinas";
    }

    // 💡 TRATAMENTO DE ERRO NO DELETE DA VACINA
    @GetMapping("/excluir/{id}")
    public String excluirVacina(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            vacinaService.deletarVacinaPorId(id);
            redirectAttributes.addFlashAttribute("successMessage", "Vacina excluída com sucesso!");
        } catch (BusinessException e) {
            // Captura nossa regra de negócio (vacina já aplicada)
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro inesperado ao excluir vacina.");
        }
        return "redirect:/vacinas";
    }
}