package com.fazenda.manejo.controller;

import com.fazenda.manejo.business.BusinessException; // 1. IMPORTAR A NOVA EXCEÇÃO
import com.fazenda.manejo.business.LoteService;
import com.fazenda.manejo.infrastructure.dto.LoteRequest;
import com.fazenda.manejo.infrastructure.dto.LoteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // 2. IMPORTAR O REDIRECT ATTRIBUTES

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/lotes")
public class LoteWebController {

    private final LoteService loteService;

    /**
     * LISTAR (READ)
     */
    @GetMapping
    public String listarLotes(Model model) {
        List<LoteResponse> lotes = loteService.listarTodos();
        model.addAttribute("lotes", lotes);
        model.addAttribute("titulo", "Gerenciamento de Lotes");
        return "lista-lotes";
    }

    /**
     * MOSTRAR FORMULÁRIO DE CADASTRO (CREATE)
     */
    @GetMapping("/novo")
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("lote", new LoteRequest());
        model.addAttribute("titulo", "Cadastrar Novo Lote");
        return "form-lote";
    }

    /**
     * MOSTRAR FORMULÁRIO DE EDIÇÃO (UPDATE P-1)
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable Integer id, Model model) {
        LoteRequest loteDto = loteService.buscarPorIdParaEdicao(id);
        model.addAttribute("lote", loteDto);
        model.addAttribute("titulo", "Editar Lote (ID: " + id + ")");
        return "form-lote";
    }

    /**
     * SALVAR (CREATE P-2 ou UPDATE P-2)
     * 💡 ATUALIZADO: com mensagem de sucesso
     */
    @PostMapping("/salvar")
    public String salvarLote(@ModelAttribute("lote") LoteRequest request, RedirectAttributes redirectAttributes) {

        String operacao = (request.getId() == null) ? "criado" : "atualizado";

        if (request.getId() == null) {
            loteService.salvarLote(request);
        } else {
            loteService.atualizarLote(request.getId(), request);
        }

        // Adiciona uma mensagem de sucesso para mostrar na lista
        redirectAttributes.addFlashAttribute("successMessage", "Lote " + operacao + " com sucesso!");

        return "redirect:/lotes";
    }

    /**
     * EXCLUIR (DELETE)
     * URL: GET /lotes/excluir/{id}
     * 💡 ATUALIZADO: com tratamento de erro
     */
    @GetMapping("/excluir/{id}")
    public String excluirLote(@PathVariable Integer id, RedirectAttributes redirectAttributes) {

        try {
            // 1. Tenta deletar
            loteService.deletarLotePorId(id);

            // 2. Se conseguir, adiciona uma mensagem de SUCESSO
            redirectAttributes.addFlashAttribute("successMessage", "Lote excluído com sucesso!");

        } catch (BusinessException e) {
            // 3. Se pegar o ERRO DE NEGÓCIO, adiciona uma mensagem de ERRO
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

        } catch (Exception e) {
            // 4. Se pegar qualquer outro erro inesperado (ex: banco de dados)
            redirectAttributes.addFlashAttribute("errorMessage", "Erro inesperado ao excluir o lote: " + e.getMessage());
        }

        // 5. Redireciona de volta para a lista EM QUALQUER CASO
        return "redirect:/lotes";
    }
}