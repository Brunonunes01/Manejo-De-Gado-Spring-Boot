package com.fazenda.manejo.controller;

import com.fazenda.manejo.business.AnimalService; // 1. IMPORT AnimalService
import com.fazenda.manejo.business.AplicacaoVacinaService; // 2. IMPORT AplicacaoVacinaService
import com.fazenda.manejo.business.VacinaService; // 3. IMPORT VacinaService
import com.fazenda.manejo.infrastructure.dto.AnimalResponse; // 4. DTOs necessários
import com.fazenda.manejo.infrastructure.dto.AplicacaoVacinaRequest;
import com.fazenda.manejo.infrastructure.dto.AplicacaoVacinaResponse;
import com.fazenda.manejo.infrastructure.dto.VacinaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/aplicacoes") // 5. Prefixo de URL para este CRUD
public class AplicacaoVacinaWebController {

    // 6. INJETANDO OS TRÊS SERVIÇOS
    private final AplicacaoVacinaService aplicacaoVacinaService;
    private final AnimalService animalService;   // Para o dropdown de Animais
    private final VacinaService vacinaService; // Para o dropdown de Vacinas

    /**
     * LISTAR (READ)
     * URL: GET /aplicacoes
     */
    @GetMapping
    public String listarAplicacoes(Model model) {
        List<AplicacaoVacinaResponse> aplicacoes = aplicacaoVacinaService.listarTodas();

        model.addAttribute("aplicacoes", aplicacoes);
        model.addAttribute("titulo", "Gerenciamento de Aplicações de Vacinas");

        return "lista-aplicacoes"; // (Vamos criar este HTML)
    }

    /**
     * MOSTRAR FORMULÁRIO DE CADASTRO (CREATE)
     * URL: GET /aplicacoes/novo
     */
    @GetMapping("/novo")
    public String mostrarFormularioCadastro(Model model) {
        // 1. Envia uma AplicacaoVacinaRequest VAZIA
        model.addAttribute("aplicacao", new AplicacaoVacinaRequest());

        // 2. 💡 LÓGICA DOS DROPDOWNS:
        // Busca a lista de Animais
        List<AnimalResponse> listaDeAnimais = animalService.listarTodos();
        model.addAttribute("listaDeAnimais", listaDeAnimais);

        // Busca a lista de Vacinas
        List<VacinaResponse> listaDeVacinas = vacinaService.listarTodos();
        model.addAttribute("listaDeVacinas", listaDeVacinas);

        model.addAttribute("titulo", "Registrar Nova Aplicação de Vacina");
        return "form-aplicacao"; // (Vamos criar este HTML)
    }

    /**
     * MOSTRAR FORMULÁRIO DE EDIÇÃO (UPDATE P-1)
     * URL: GET /aplicacoes/editar/{id}
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable Integer id, Model model) {
        // 1. Envia a AplicacaoVacinaRequest PREENCHIDA
        AplicacaoVacinaRequest aplicacaoDto = aplicacaoVacinaService.buscarPorIdParaEdicao(id);
        model.addAttribute("aplicacao", aplicacaoDto);

        // 2. 💡 LÓGICA DOS DROPDOWNS (também precisa aqui):
        List<AnimalResponse> listaDeAnimais = animalService.listarTodos();
        model.addAttribute("listaDeAnimais", listaDeAnimais);

        List<VacinaResponse> listaDeVacinas = vacinaService.listarTodos();
        model.addAttribute("listaDeVacinas", listaDeVacinas);

        model.addAttribute("titulo", "Editar Aplicação (ID: " + id + ")");
        return "form-aplicacao"; // Reutiliza o mesmo formulário
    }

    /**
     * SALVAR (CREATE P-2 ou UPDATE P-2)
     * URL: POST /aplicacoes/salvar
     */
    @PostMapping("/salvar")
    public String salvarAplicacao(@ModelAttribute("aplicacao") AplicacaoVacinaRequest request) {

        // O Service já sabe como lidar com o 'animalId' e 'vacinaId'
        if (request.getId() == null) {
            aplicacaoVacinaService.salvarAplicacao(request);
        } else {
            aplicacaoVacinaService.atualizarAplicacao(request.getId(), request);
        }

        return "redirect:/aplicacoes"; // Redireciona para a lista
    }

    /**
     * EXCLUIR (DELETE)
     * URL: GET /aplicacoes/excluir/{id}
     */
    @GetMapping("/excluir/{id}")
    public String excluirAplicacao(@PathVariable Integer id) {
        aplicacaoVacinaService.deletarAplicacaoPorId(id);
        return "redirect:/aplicacoes";
    }
}