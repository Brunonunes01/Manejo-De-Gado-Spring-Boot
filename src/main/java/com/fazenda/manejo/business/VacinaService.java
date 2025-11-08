package com.fazenda.manejo.business;

import com.fazenda.manejo.infrastructure.dto.VacinaRequest;
import com.fazenda.manejo.infrastructure.dto.VacinaResponse;
import com.fazenda.manejo.infrastructure.entitys.Vacina;
import com.fazenda.manejo.infrastructure.repository.VacinaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VacinaService {

    private final VacinaRepository vacinaRepository;

    // --- CREATE (Salvar) ---
    public void salvarVacina(VacinaRequest request) {
        // Converte o DTO (dados do form) para a Entidade (do banco)
        Vacina novaVacina = Vacina.builder()
                .nome(request.getNome())
                .fabricante(request.getFabricante())
                .loteFabricacao(request.getLoteFabricacao())
                .dataValidade(request.getDataValidade())
                .diasCarenciaAbate(request.getDiasCarenciaAbate())
                .tipo(request.getTipo())
                .build();

        vacinaRepository.saveAndFlush(novaVacina);
    }

    // --- UPDATE (Atualizar) ---
    public void atualizarVacina(Integer id, VacinaRequest request) {
        // 1. Busca a entidade original no banco
        Vacina vacinaDoBanco = vacinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vacina não encontrada com ID: " + id));

        // 2. Atualiza os campos da entidade com os dados do DTO
        vacinaDoBanco.setNome(request.getNome());
        vacinaDoBanco.setFabricante(request.getFabricante());
        vacinaDoBanco.setLoteFabricacao(request.getLoteFabricacao());
        vacinaDoBanco.setDataValidade(request.getDataValidade());
        vacinaDoBanco.setDiasCarenciaAbate(request.getDiasCarenciaAbate());
        vacinaDoBanco.setTipo(request.getTipo());

        // 3. Salva a entidade atualizada
        vacinaRepository.saveAndFlush(vacinaDoBanco);
    }


    // --- READ (Listar Todos) ---
    public List<VacinaResponse> listarTodos() {
        // 1. Busca todas as ENTIDADES
        return vacinaRepository.findAll().stream()
                // 2. Converte cada ENTIDADE para um DTO RESPONSE
                .map(this::converterEntidadeParaResponse)
                // 3. Coleta em uma nova lista de DTOs
                .collect(Collectors.toList());
    }

    // --- READ (Buscar por ID para Edição) ---
    public VacinaRequest buscarPorIdParaEdicao(Integer id) {
        Vacina vacinaDoBanco = vacinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vacina não encontrada com ID: " + id));

        // Converte a Entidade para um DTO Request (para preencher o form)
        return converterEntidadeParaRequest(vacinaDoBanco);
    }


    // --- DELETE ---
    public void deletarVacinaPorId(Integer id) {
        // 1. Verifica se a vacina existe antes de deletar
        Vacina vacina = vacinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vacina não encontrada com ID: " + id));

        // 2. Deleta a vacina
        // (No futuro, se 'AplicacaoVacina' existir,
        // poderíamos adicionar uma regra para não excluir
        // vacinas que já foram aplicadas)
        vacinaRepository.delete(vacina);
    }


    // -----------------------------------------------------------------
    // MÉTODOS "CONVERSORES" (MAPPERS)
    // -----------------------------------------------------------------

    /**
     * Converte uma Entidade Vacina para um DTO VacinaResponse.
     * (Usado para mostrar dados na Listagem)
     */
    private VacinaResponse converterEntidadeParaResponse(Vacina entidade) {
        return VacinaResponse.builder()
                .id(entidade.getId())
                .nome(entidade.getNome())
                .fabricante(entidade.getFabricante())
                .loteFabricacao(entidade.getLoteFabricacao())
                .dataValidade(entidade.getDataValidade())
                .diasCarenciaAbate(entidade.getDiasCarenciaAbate())
                .build();
    }

    /**
     * Converte uma Entidade Vacina para um DTO VacinaRequest.
     * (Usado para preencher o formulário de Edição)
     */
    private VacinaRequest converterEntidadeParaRequest(Vacina entidade) {
        return VacinaRequest.builder()
                .id(entidade.getId())
                .nome(entidade.getNome())
                .fabricante(entidade.getFabricante())
                .loteFabricacao(entidade.getLoteFabricacao())
                .dataValidade(entidade.getDataValidade())
                .diasCarenciaAbate(entidade.getDiasCarenciaAbate())
                .tipo(entidade.getTipo())
                .build();
    }
}