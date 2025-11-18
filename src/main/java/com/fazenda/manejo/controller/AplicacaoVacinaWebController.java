package com.fazenda.manejo.controller;

import com.fazenda.manejo.business.AnimalService;
import com.fazenda.manejo.business.AplicacaoVacinaService;
import com.fazenda.manejo.business.VacinaService;
import com.fazenda.manejo.business.BusinessException; // Adicionado
import com.fazenda.manejo.infrastructure.dto.AnimalResponse;
import com.fazenda.manejo.infrastructure.dto.AplicacaoVacinaRequest;
import com.fazenda.manejo.infrastructure.dto.AplicacaoVacinaResponse;
import com.fazenda.manejo.infrastructure.dto.VacinaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Adicionado

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/aplicacoes")
public class AplicacaoVacinaWebController {

    private final AplicacaoVacinaService aplicacaoVacinaService;
    private final AnimalService animalService;
    private final VacinaService vacinaService;

    /**
     * LISTAR (READ)
     */
    @GetMapping
    public String listarAplicacoes(Model model) {
        List<AplicacaoVacinaResponse> aplicacoes = aplicacaoVacinaService.listarTodas();

        model.addAttribute("aplicacoes", aplicacoes);
        model.addAttribute("titulo", "Gerenciamento de Aplicações de Vacinas");

        return "lista-aplicacoes";
    }

    /**
     * MOSTRAR FORMULÁRIO DE CADASTRO (CREATE)
     */
    @GetMapping("/novo")
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("aplicacao", new AplicacaoVacinaRequest());
        List<AnimalResponse> listaDeAnimais = animalService.listarTodos();
        model.addAttribute("listaDeAnimais", listaDeAnimais);
        List<VacinaResponse> listaDeVacinas = vacinaService.listarTodos();
        model.addAttribute("listaDeVacinas", listaDeVacinas);
        model.addAttribute("titulo", "Registrar Nova Aplicação de Vacina");
        return "form-aplicacao";
    }

    /**
     * MOSTRAR FORMULÁRIO DE EDIÇÃO (UPDATE P-1)
     * // Adicionado try-catch para redirecionar em caso de ID inválido
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            AplicacaoVacinaRequest aplicacaoDto = aplicacaoVacinaService.buscarPorIdParaEdicao(id);
            model.addAttribute("aplicacao", aplicacaoDto);

            List<AnimalResponse> listaDeAnimais = animalService.listarTodos();
            model.addAttribute("listaDeAnimais", listaDeAnimais);

            List<VacinaResponse> listaDeVacinas = vacinaService.listarTodos();
            model.addAttribute("listaDeVacinas", listaDeVacinas);

            model.addAttribute("titulo", "Editar Aplicação (ID: " + id + ")");
            return "form-aplicacao";
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/aplicacoes";
        }
    }

    /**
     * SALVAR (CREATE P-2 ou UPDATE P-2)
     * // Adicionado try-catch e RedirectAttributes
     */
    @PostMapping("/salvar")
    public String salvarAplicacao(@ModelAttribute("aplicacao") AplicacaoVacinaRequest request, RedirectAttributes redirectAttributes) {

        String operacao = (request.getId() == null) ? "cadastrada" : "atualizada";

        try {
            if (request.getId() == null) {
                aplicacaoVacinaService.salvarAplicacao(request);
            } else {
                aplicacaoVacinaService.atualizarAplicacao(request.getId(), request);
            }

            // Mensagem de Sucesso
            redirectAttributes.addFlashAttribute("successMessage", "Aplicação " + operacao + " com sucesso!");

        } catch (BusinessException e) {
            // Captura erros de validação/negócio
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            // Captura erros inesperados
            redirectAttributes.addFlashAttribute("errorMessage", "Erro inesperado ao salvar a aplicação.");
        }

        return "redirect:/aplicacoes";
    }

    /**
     * EXCLUIR (DELETE)
     * // Adicionado try-catch e RedirectAttributes
     */
    @GetMapping("/excluir/{id}")
    public String excluirAplicacao(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            aplicacaoVacinaService.deletarAplicacaoPorId(id);
            // Mensagem de Sucesso
            redirectAttributes.addFlashAttribute("successMessage", "Aplicação excluída com sucesso!");
        } catch (BusinessException e) {
            // Captura erros de não encontrado
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            // Captura erros inesperados
            redirectAttributes.addFlashAttribute("errorMessage", "Erro inesperado ao excluir a aplicação.");
        }
        return "redirect:/aplicacoes";
    }
}