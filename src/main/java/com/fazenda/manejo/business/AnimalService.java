package com.fazenda.manejo.business;

import com.fazenda.manejo.infrastructure.dto.AnimalRequest;
import com.fazenda.manejo.infrastructure.dto.AnimalResponse;
import com.fazenda.manejo.infrastructure.entitys.Animal;
import com.fazenda.manejo.infrastructure.entitys.Lote;
import com.fazenda.manejo.infrastructure.repository.AnimalRepository;
import com.fazenda.manejo.infrastructure.repository.LoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final LoteRepository loteRepository;

    // --- CREATE (Salvar) ---
    @Transactional
    public void salvarAnimal(AnimalRequest request) {

        // 💡 1. NOVA VERIFICAÇÃO (A CORREÇÃO DO ERRO)
        // Se o loteId for nulo, lança um erro claro ANTES de ir ao banco.
        if (request.getLoteId() == null) {
            throw new RuntimeException("Erro: Nenhum lote foi selecionado para o animal.");
        }

        Lote lote = loteRepository.findById(request.getLoteId())
                .orElseThrow(() -> new RuntimeException("Lote não encontrado com ID: " + request.getLoteId()));

        Animal novoAnimal = Animal.builder()
                .brinco(request.getBrinco())
                .dataNascimento(request.getDataNascimento())
                .dataEntrada(request.getDataEntrada())
                .sexo(request.getSexo())
                .raca(request.getRaca())
                .status(request.getStatus())
                .brincoMae(request.getBrincoMae())
                .observacao(request.getObservacao())
                .lote(lote)
                .build();

        animalRepository.saveAndFlush(novoAnimal);
    }

    // --- UPDATE (Atualizar) ---
    @Transactional
    public void atualizarAnimal(Integer id, AnimalRequest request) {

        Animal animalDoBanco = animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal não encontrado com ID: " + id));

        // 💡 2. NOVA VERIFICAÇÃO (A CORREÇÃO DO ERRO)
        if (request.getLoteId() == null) {
            throw new RuntimeException("Erro: Nenhum lote foi selecionado para o animal.");
        }

        Lote lote = loteRepository.findById(request.getLoteId())
                .orElseThrow(() -> new RuntimeException("Lote não encontrado com ID: " + request.getLoteId()));

        animalDoBanco.setBrinco(request.getBrinco());
        animalDoBanco.setDataNascimento(request.getDataNascimento());
        animalDoBanco.setDataEntrada(request.getDataEntrada());
        animalDoBanco.setSexo(request.getSexo());
        animalDoBanco.setRaca(request.getRaca());
        animalDoBanco.setStatus(request.getStatus());
        animalDoBanco.setBrincoMae(request.getBrincoMae());
        animalDoBanco.setObservacao(request.getObservacao());
        animalDoBanco.setLote(lote);

        animalRepository.saveAndFlush(animalDoBanco);
    }

    // --- READ (Listar Todos) ---
    public List<AnimalResponse> listarTodos() {
        return animalRepository.findAll().stream()
                .map(this::converterEntidadeParaResponse)
                .collect(Collectors.toList());
    }

    // --- READ (Buscar por ID para Edição) ---
    public AnimalRequest buscarPorIdParaEdicao(Integer id) {
        Animal animalDoBanco = animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal não encontrado com ID: " + id));

        return converterEntidadeParaRequest(animalDoBanco);
    }

    // --- DELETE ---
    public void deletarAnimalPorId(Integer id) {
        animalRepository.deleteById(id);
    }


    // --- CONVERSORES (MAPPERS) ---

    private AnimalResponse converterEntidadeParaResponse(Animal entidade) {
        String nomeLote = (entidade.getLote() != null) ? entidade.getLote().getNome() : "Sem Lote";

        return AnimalResponse.builder()
                .id(entidade.getId())
                .brinco(entidade.getBrinco())
                .sexo(entidade.getSexo())
                .raca(entidade.getRaca())
                .dataNascimento(entidade.getDataNascimento())
                .status(entidade.getStatus())
                .nomeLote(nomeLote)
                .build();
    }

    private AnimalRequest converterEntidadeParaRequest(Animal entidade) {
        Integer loteId = (entidade.getLote() != null) ? entidade.getLote().getId() : null;

        return AnimalRequest.builder()
                .id(entidade.getId())
                .brinco(entidade.getBrinco())
                .dataNascimento(entidade.getDataNascimento())
                .dataEntrada(entidade.getDataEntrada())
                .sexo(entidade.getSexo())
                .raca(entidade.getRaca())
                .status(entidade.getStatus())
                .brincoMae(entidade.getBrincoMae())
                .observacao(entidade.getObservacao())
                .loteId(loteId)
                .build();
    }
}