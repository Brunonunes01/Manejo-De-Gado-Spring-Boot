package com.fazenda.manejo.controller;

import com.fazenda.manejo.infrastructure.repository.AnimalRepository;
import com.fazenda.manejo.infrastructure.repository.LoteRepository;
import com.fazenda.manejo.infrastructure.repository.VacinaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor // Injeta os repositórios
public class HomeController {

    // 1. Precisamos dos repositórios para contar os itens
    private final AnimalRepository animalRepository;
    private final LoteRepository loteRepository;
    private final VacinaRepository vacinaRepository;

    /**
     * Este método agora responde à URL raiz (http://localhost:8080/)
     */
    @GetMapping("/")
    public String dashboard(Model model) {

        // 2. Busca os KPIs (Indicadores)
        long totalAnimais = animalRepository.count();
        long totalLotes = loteRepository.count();
        long totalVacinas = vacinaRepository.count();

        // 3. Adiciona os KPIs ao Model para o HTML usar
        model.addAttribute("totalAnimais", totalAnimais);
        model.addAttribute("totalLotes", totalLotes);
        model.addAttribute("totalVacinas", totalVacinas);

        model.addAttribute("titulo", "Dashboard - Resumo");

        // 4. Renderiza o novo arquivo 'home.html'
        return "home";
    }
}