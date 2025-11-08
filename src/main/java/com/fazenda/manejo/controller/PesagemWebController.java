package com.fazenda.manejo.controller;

import com.fazenda.manejo.business.AnimalService;
import com.fazenda.manejo.business.PesagemService;
import com.fazenda.manejo.infrastructure.dto.AnimalResponse;
import com.fazenda.manejo.infrastructure.dto.PesagemRequest;
import com.fazenda.manejo.infrastructure.dto.PesagemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/pesagens")
public class PesagemWebController {

    private final PesagemService pesagemService;
    private final AnimalService animalService;

    /**
     * LISTAR (READ)
     * URL: GET /pesagens
     */
    @GetMapping
    public String listarPesagens(Model model) {
        // Busca todas as pesagens (garante que não retorna null)
        List<PesagemResponse> pesagens = pesagemService.listarTodas();
        if (pesagens == null) {
            pesagens = List.of(); // lista vazia
        }

        model.addAttribute("pesagens", pesagens);
        model.addAttribute("titulo", "Histórico de Pesagens (Global)");
        return "lista-pesagens";
    }

    /**
     * MOSTRAR FORMULÁRIO DE CADASTRO (CREATE)
     * URL: GET /pesagens/novo
     */
    @GetMapping("/novo")
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("pesagem", new PesagemRequest());
        List<AnimalResponse> listaDeAnimais = animalService.listarTodos();
        model.addAttribute("listaDeAnimais", listaDeAnimais);
        model.addAttribute("titulo", "Registrar Nova Pesagem");
        return "form-pesagem";
    }

    /**
     * MOSTRAR FORMULÁRIO DE EDIÇÃO (UPDATE)
     * URL: GET /pesagens/editar/{id}
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable Integer id, Model model) {
        PesagemRequest pesagemDto = pesagemService.buscarPorIdParaEdicao(id);
        model.addAttribute("pesagem", pesagemDto);

        List<AnimalResponse> listaDeAnimais = animalService.listarTodos();
        model.addAttribute("listaDeAnimais", listaDeAnimais);
        model.addAttribute("titulo", "Editar Pesagem (ID: " + id + ")");
        return "form-pesagem";
    }

    /**
     * SALVAR (CREATE ou UPDATE)
     * URL: POST /pesagens/salvar
     */
    @PostMapping("/salvar")
    public String salvarPesagem(@ModelAttribute("pesagem") PesagemRequest request) {
        if (request.getId() == null) {
            pesagemService.salvarPesagem(request);
        } else {
            pesagemService.atualizarPesagem(request.getId(), request);
        }
        return "redirect:/pesagens";
    }

    /**
     * EXCLUIR (DELETE)
     * URL: GET /pesagens/excluir/{id}
     */
    @GetMapping("/excluir/{id}")
    public String excluirPesagem(@PathVariable Integer id) {
        pesagemService.deletarPesagemPorId(id);
        return "redirect:/pesagens";
    }
}
