package com.fazenda.manejo.controller;

import com.fazenda.manejo.business.AnimalService; // 1. IMPORT AnimalService
import com.fazenda.manejo.business.LoteService;   // 2. IMPORT LoteService
import com.fazenda.manejo.infrastructure.dto.AnimalRequest;
import com.fazenda.manejo.infrastructure.dto.LoteResponse; // 3. Precisamos do LoteResponse (para a lista)
import com.fazenda.manejo.infrastructure.dto.AnimalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // 💡 NOVO IMPORT
import org.springframework.dao.DataIntegrityViolationException; // 💡 NOVO IMPORT

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/animais")
public class AnimalWebController {

    private final AnimalService animalService;
    private final LoteService loteService;

    // --- LISTAR (READ) ---
    @GetMapping
    public String listarAnimais(Model model) {
        List<AnimalResponse> animais = animalService.listarTodos();
        model.addAttribute("animais", animais);
        model.addAttribute("titulo", "Gerenciamento de Animais");
        return "lista-animais";
    }

    // --- MOSTRAR FORMULÁRIO CADASTRO (CREATE) ---
    @GetMapping("/novo")
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("animal", new AnimalRequest());
        List<LoteResponse> listaDeLotes = loteService.listarTodos();
        model.addAttribute("listaDeLotes", listaDeLotes);
        model.addAttribute("titulo", "Cadastrar Novo Animal");
        return "form-animal";
    }

    // --- MOSTRAR FORMULÁRIO EDIÇÃO (UPDATE P-1) ---
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable Integer id, Model model) {
        AnimalRequest animalDto = animalService.buscarPorIdParaEdicao(id);
        model.addAttribute("animal", animalDto);

        List<LoteResponse> listaDeLotes = loteService.listarTodos();
        model.addAttribute("listaDeLotes", listaDeLotes);

        model.addAttribute("titulo", "Editar Animal (Brinco: " + animalDto.getBrinco() + ")");
        return "form-animal";
    }

    // --- SALVAR (CREATE P-2 ou UPDATE P-2) ---
    @PostMapping("/salvar")
    public String salvarAnimal(@ModelAttribute("animal") AnimalRequest request, RedirectAttributes redirectAttributes) {

        String operacao = (request.getId() == null) ? "criado" : "atualizado";

        if (request.getId() == null) {
            animalService.salvarAnimal(request);
        } else {
            animalService.atualizarAnimal(request.getId(), request);
        }

        // Adiciona mensagem de sucesso para a próxima requisição
        redirectAttributes.addFlashAttribute("successMessage", "Animal " + operacao + " com sucesso!");
        return "redirect:/animais";
    }

    /**
     * EXCLUIR (DELETE)
     * URL: GET /animais/excluir/{id}
     * 💡 ATUALIZADO: Com tratamento de erro (Solução para a Página Branca)
     */
    @GetMapping("/excluir/{id}")
    public String excluirAnimal(@PathVariable Integer id, RedirectAttributes redirectAttributes) {

        try {
            // 1. Tenta deletar
            animalService.deletarAnimalPorId(id);

            // 2. Se conseguir, adiciona uma mensagem de SUCESSO
            redirectAttributes.addFlashAttribute("successMessage", "Animal excluído com sucesso!");

        } catch (DataIntegrityViolationException e) {
            // 3. 💡 Captura o erro do Banco (Chave Estrangeira)
            // Isso acontece se o animal tiver registros de Pesagem ou AplicaçãoVacina.
            redirectAttributes.addFlashAttribute("errorMessage", "Não é possível excluir o Animal. Ele possui Pesagens ou Vacinas registradas.");

        } catch (Exception e) {
            // 4. Se pegar qualquer outro erro inesperado
            redirectAttributes.addFlashAttribute("errorMessage", "Erro inesperado ao excluir o animal.");
        }

        // 5. Redireciona de volta para a lista EM QUALQUER CASO
        return "redirect:/animais";
    }
}