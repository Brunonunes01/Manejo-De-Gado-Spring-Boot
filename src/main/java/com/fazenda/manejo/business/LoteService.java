package com.fazenda.manejo.business;

import com.fazenda.manejo.infrastructure.dto.LoteRequest;
import com.fazenda.manejo.infrastructure.dto.LoteResponse;
import com.fazenda.manejo.infrastructure.entitys.Lote;
import com.fazenda.manejo.infrastructure.repository.LoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoteService {

    private final LoteRepository loteRepository;

    // --- CREATE (Salvar) ---
    public void salvarLote(LoteRequest request) {
        Lote novoLote = Lote.builder()
                .nome(request.getNome())
                .descricao(request.getDescricao())
                .localizacao(request.getLocalizacao())
                .status(request.getStatus())
                .build();
        loteRepository.saveAndFlush(novoLote);
    }

    // --- UPDATE (Atualizar) ---
    public void atualizarLote(Integer id, LoteRequest request) {
        Lote loteDoBanco = loteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lote não encontrado com ID: " + id));

        loteDoBanco.setNome(request.getNome());
        loteDoBanco.setDescricao(request.getDescricao());
        loteDoBanco.setLocalizacao(request.getLocalizacao());
        loteDoBanco.setStatus(request.getStatus());

        loteRepository.saveAndFlush(loteDoBanco);
    }


    // --- READ (Listar Todos) ---
    public List<LoteResponse> listarTodos() {
        return loteRepository.findAll().stream()
                .map(this::converterEntidadeParaResponse)
                .collect(Collectors.toList());
    }

    // --- READ (Buscar por ID para Edição) ---
    public LoteRequest buscarPorIdParaEdicao(Integer id) {
        Lote loteDoBanco = loteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lote não encontrado com ID: " + id));

        return converterEntidadeParaRequest(loteDoBanco);
    }


    // --- DELETE (A Regra de Negócio) ---
    public void deletarLotePorId(Integer id) {
        Lote loteParaDeletar = loteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lote não encontrado com ID: " + id));

        // 🧠 A REGRA:
        if (loteParaDeletar.getAnimais() != null && !loteParaDeletar.getAnimais().isEmpty()) {

            // 💡 MUDANÇA AQUI: Trocamos RuntimeException por BusinessException
            throw new BusinessException("Não é possível excluir o Lote '" +
                    loteParaDeletar.getNome() + "'. Ele está associado a " +
                    loteParaDeletar.getAnimais().size() + " animal(is).");
        }

        loteRepository.delete(loteParaDeletar);
    }


    // --- CONVERSORES (MAPPERS) ---

    private LoteResponse converterEntidadeParaResponse(Lote entidade) {
        int qtdAnimais = (entidade.getAnimais() != null) ? entidade.getAnimais().size() : 0;

        return LoteResponse.builder()
                .id(entidade.getId())
                .nome(entidade.getNome())
                .descricao(entidade.getDescricao())
                .localizacao(entidade.getLocalizacao())
                .status(entidade.getStatus())
                .quantidadeAnimais(qtdAnimais)
                .build();
    }

    private LoteRequest converterEntidadeParaRequest(Lote entidade) {
        return LoteRequest.builder()
                .id(entidade.getId())
                .nome(entidade.getNome())
                .descricao(entidade.getDescricao())
                .localizacao(entidade.getLocalizacao())
                .status(entidade.getStatus())
                .build();
    }
}