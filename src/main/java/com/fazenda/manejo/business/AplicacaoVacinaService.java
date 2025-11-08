package com.fazenda.manejo.business;

import com.fazenda.manejo.infrastructure.dto.AplicacaoVacinaRequest;
import com.fazenda.manejo.infrastructure.dto.AplicacaoVacinaResponse;
import com.fazenda.manejo.infrastructure.entitys.Animal; // 1. IMPORT
import com.fazenda.manejo.infrastructure.entitys.AplicacaoVacina;
import com.fazenda.manejo.infrastructure.entitys.Vacina; // 2. IMPORT
import com.fazenda.manejo.infrastructure.repository.AnimalRepository; // 3. IMPORT
import com.fazenda.manejo.infrastructure.repository.AplicacaoVacinaRepository;
import com.fazenda.manejo.infrastructure.repository.VacinaRepository; // 4. IMPORT
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AplicacaoVacinaService {

    // 5. INJETANDO OS TRÊS REPOSITÓRIOS
    private final AplicacaoVacinaRepository aplicacaoVacinaRepository;
    private final AnimalRepository animalRepository;
    private final VacinaRepository vacinaRepository;

    // --- CREATE (Salvar) ---
    @Transactional
    public void salvarAplicacao(AplicacaoVacinaRequest request) {

        // 1. Validações (para os 2 dropdowns)
        if (request.getAnimalId() == null) {
            throw new RuntimeException("Erro: Nenhum animal foi selecionado.");
        }
        if (request.getVacinaId() == null) {
            throw new RuntimeException("Erro: Nenhuma vacina foi selecionada.");
        }

        // 2. Busca as ENTIDADES "Pai"
        Animal animal = animalRepository.findById(request.getAnimalId())
                .orElseThrow(() -> new RuntimeException("Animal não encontrado com ID: " + request.getAnimalId()));

        Vacina vacina = vacinaRepository.findById(request.getVacinaId())
                .orElseThrow(() -> new RuntimeException("Vacina não encontrada com ID: " + request.getVacinaId()));

        // 3. Constrói a nova Entidade (o "Evento")
        AplicacaoVacina novaAplicacao = AplicacaoVacina.builder()
                .dataAplicacao(request.getDataAplicacao())
                .dose(request.getDose())
                .aplicador(request.getAplicador())
                .observacao(request.getObservacao())
                .animal(animal)   // 4. Associa o Animal
                .vacina(vacina) // 5. Associa a Vacina
                .build();

        aplicacaoVacinaRepository.saveAndFlush(novaAplicacao);
    }

    // --- UPDATE (Atualizar) ---
    @Transactional
    public void atualizarAplicacao(Integer id, AplicacaoVacinaRequest request) {

        // 1. Busca o Evento que vamos atualizar
        AplicacaoVacina aplicacaoDoBanco = aplicacaoVacinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aplicação não encontrada com ID: " + id));

        // 2. Validações
        if (request.getAnimalId() == null) throw new RuntimeException("Erro: Nenhum animal selecionado.");
        if (request.getVacinaId() == null) throw new RuntimeException("Erro: Nenhuma vacina selecionada.");

        // 3. Busca as (novas ou as mesmas) Entidades "Pai"
        Animal animal = animalRepository.findById(request.getAnimalId())
                .orElseThrow(() -> new RuntimeException("Animal não encontrado com ID: " + request.getAnimalId()));

        Vacina vacina = vacinaRepository.findById(request.getVacinaId())
                .orElseThrow(() -> new RuntimeException("Vacina não encontrada com ID: " + request.getVacinaId()));

        // 4. Atualiza os dados do Evento
        aplicacaoDoBanco.setDataAplicacao(request.getDataAplicacao());
        aplicacaoDoBanco.setDose(request.getDose());
        aplicacaoDoBanco.setAplicador(request.getAplicador());
        aplicacaoDoBanco.setObservacao(request.getObservacao());
        aplicacaoDoBanco.setAnimal(animal);
        aplicacaoDoBanco.setVacina(vacina);

        aplicacaoVacinaRepository.saveAndFlush(aplicacaoDoBanco);
    }

    // --- READ (Listar Todos) ---
    public List<AplicacaoVacinaResponse> listarTodas() {
        return aplicacaoVacinaRepository.findAll().stream()
                .map(this::converterEntidadeParaResponse)
                .collect(Collectors.toList());
    }

    // --- READ (Histórico de um Animal) ---
    // (Este método cumpre a funcionalidade: "Listar Histórico de Vacinas de um Animal")
    public List<AplicacaoVacinaResponse> listarAplicacoesPorAnimalId(Integer animalId) {
        return aplicacaoVacinaRepository.findByAnimalIdOrderByDataAplicacaoDesc(animalId).stream()
                .map(this::converterEntidadeParaResponse)
                .collect(Collectors.toList());
    }

    // --- READ (Buscar por ID para Edição) ---
    public AplicacaoVacinaRequest buscarPorIdParaEdicao(Integer id) {
        AplicacaoVacina aplicacaoDoBanco = aplicacaoVacinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aplicação não encontrada com ID: " + id));

        return converterEntidadeParaRequest(aplicacaoDoBanco);
    }

    // --- DELETE ---
    public void deletarAplicacaoPorId(Integer id) {
        AplicacaoVacina aplicacao = aplicacaoVacinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aplicação não encontrada com ID: " + id));

        aplicacaoVacinaRepository.delete(aplicacao);
    }


    // -----------------------------------------------------------------
    // MÉTODOS "CONVERSORES" (MAPPERS)
    // -----------------------------------------------------------------

    private AplicacaoVacinaResponse converterEntidadeParaResponse(AplicacaoVacina entidade) {

        String brincoAnimal = (entidade.getAnimal() != null) ? entidade.getAnimal().getBrinco() : "Animal Desconhecido";
        String nomeVacina = (entidade.getVacina() != null) ? entidade.getVacina().getNome() : "Vacina Desconhecida";

        return AplicacaoVacinaResponse.builder()
                .id(entidade.getId())
                .dataAplicacao(entidade.getDataAplicacao())
                .dose(entidade.getDose())
                .aplicador(entidade.getAplicador())
                .brincoAnimal(brincoAnimal) // 6. Passa o BRINCO
                .nomeVacina(nomeVacina)   // 7. Passa o NOME
                .build();
    }

    private AplicacaoVacinaRequest converterEntidadeParaRequest(AplicacaoVacina entidade) {

        Integer animalId = (entidade.getAnimal() != null) ? entidade.getAnimal().getId() : null;
        Integer vacinaId = (entidade.getVacina() != null) ? entidade.getVacina().getId() : null;

        return AplicacaoVacinaRequest.builder()
                .id(entidade.getId())
                .dataAplicacao(entidade.getDataAplicacao())
                .dose(entidade.getDose())
                .aplicador(entidade.getAplicador())
                .observacao(entidade.getObservacao())
                .animalId(animalId) // 8. Passa o ID do Animal
                .vacinaId(vacinaId) // 9. Passa o ID da Vacina
                .build();
    }
}