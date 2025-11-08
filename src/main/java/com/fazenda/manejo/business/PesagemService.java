package com.fazenda.manejo.business;

import com.fazenda.manejo.infrastructure.dto.PesagemRequest;
import com.fazenda.manejo.infrastructure.dto.PesagemResponse;
import com.fazenda.manejo.infrastructure.entitys.Animal;
import com.fazenda.manejo.infrastructure.entitys.Pesagem;
import com.fazenda.manejo.infrastructure.repository.AnimalRepository;
import com.fazenda.manejo.infrastructure.repository.PesagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors; // Import necessário

@Service
@RequiredArgsConstructor
public class PesagemService {

    private final PesagemRepository pesagemRepository;
    private final AnimalRepository animalRepository;

    // --- CREATE (Salvar) ---
    @Transactional
    public void salvarPesagem(PesagemRequest request) {

        if (request.getAnimalId() == null) {
            throw new RuntimeException("Erro: Nenhuma animal foi selecionado para a pesagem.");
        }

        Animal animal = animalRepository.findById(request.getAnimalId())
                .orElseThrow(() -> new RuntimeException("Animal não encontrado com ID: " + request.getAnimalId()));

        Pesagem novaPesagem = Pesagem.builder()
                .dataPesagem(request.getDataPesagem())
                .peso(request.getPeso())
                .observacao(request.getObservacao())
                .animal(animal)
                .build();

        pesagemRepository.saveAndFlush(novaPesagem);
    }

    // --- UPDATE (Atualizar) ---
    @Transactional
    public void atualizarPesagem(Integer id, PesagemRequest request) {

        Pesagem pesagemDoBanco = pesagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pesagem não encontrada com ID: " + id));

        if (request.getAnimalId() == null) {
            throw new RuntimeException("Erro: Nenhuma animal foi selecionado para a pesagem.");
        }

        Animal animal = animalRepository.findById(request.getAnimalId())
                .orElseThrow(() -> new RuntimeException("Animal não encontrado com ID: " + request.getAnimalId()));

        pesagemDoBanco.setDataPesagem(request.getDataPesagem());
        pesagemDoBanco.setPeso(request.getPeso());
        pesagemDoBanco.setObservacao(request.getObservacao());
        pesagemDoBanco.setAnimal(animal);

        pesagemRepository.saveAndFlush(pesagemDoBanco);
    }

    // --- 💡 READ (MÉTODO 1: POR ANIMAL) ---
    /**
     * Busca o histórico de pesagens de um ANIMAL ESPECÍFICO.
     */
    public List<PesagemResponse> listarPesagensPorAnimalId(Integer animalId) {
        return pesagemRepository.findByAnimalIdOrderByDataPesagemDesc(animalId).stream()
                .map(this::converterEntidadeParaResponse)
                .collect(Collectors.toList());
    }

    // --- 💡 READ (MÉTODO 2: TODOS) ---
    /**
     * Busca TODAS as pesagens, de todos os animais.
     * (Este era o método que estava faltando!)
     */
    public List<PesagemResponse> listarTodas() {
        return pesagemRepository.findAll().stream()
                .map(this::converterEntidadeParaResponse)
                .collect(Collectors.toList());
    }


    // --- READ (Buscar por ID para Edição) ---
    public PesagemRequest buscarPorIdParaEdicao(Integer id) {
        Pesagem pesagemDoBanco = pesagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pesagem não encontrada com ID: " + id));

        return converterEntidadeParaRequest(pesagemDoBanco);
    }

    // --- DELETE ---
    public void deletarPesagemPorId(Integer id) {
        Pesagem pesagem = pesagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pesagem não encontrada com ID: " + id));

        pesagemRepository.delete(pesagem);
    }


    // -----------------------------------------------------------------
    // MÉTODOS "CONVERSORES" (MAPPERS)
    // -----------------------------------------------------------------

    private PesagemResponse converterEntidadeParaResponse(Pesagem entidade) {
        String brincoAnimal = (entidade.getAnimal() != null) ? entidade.getAnimal().getBrinco() : "Animal Desconhecido";

        return PesagemResponse.builder()
                .id(entidade.getId())
                .dataPesagem(entidade.getDataPesagem())
                .peso(entidade.getPeso())
                .observacao(entidade.getObservacao())
                .brincoAnimal(brincoAnimal)
                .build();
    }

    private PesagemRequest converterEntidadeParaRequest(Pesagem entidade) {
        Integer animalId = (entidade.getAnimal() != null) ? entidade.getAnimal().getId() : null;

        return PesagemRequest.builder()
                .id(entidade.getId())
                .dataPesagem(entidade.getDataPesagem())
                .peso(entidade.getPeso())
                .observacao(entidade.getObservacao())
                .animalId(animalId)
                .build();
    }
}