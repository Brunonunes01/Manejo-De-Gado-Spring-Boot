package com.fazenda.manejo.business;

import com.fazenda.manejo.infrastructure.dto.VacinaRequest;
import com.fazenda.manejo.infrastructure.dto.VacinaResponse;
import com.fazenda.manejo.infrastructure.entitys.Vacina;
import com.fazenda.manejo.infrastructure.repository.AplicacaoVacinaRepository; // 1. Novo Import
import com.fazenda.manejo.infrastructure.repository.VacinaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Boa prática adicionar

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VacinaService {

    private final VacinaRepository vacinaRepository;
    // 2. Injetamos o repositório de aplicações para verificar o uso
    private final AplicacaoVacinaRepository aplicacaoVacinaRepository;

    // ... (Métodos salvar, atualizar, listar e buscar permanecem iguais) ...

    public void salvarVacina(VacinaRequest request) {
        // ... (código igual ao anterior)
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

    public void atualizarVacina(Integer id, VacinaRequest request) {
        // ... (código igual ao anterior, trocando RuntimeException por BusinessException se quiser)
        Vacina vacinaDoBanco = vacinaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Vacina não encontrada com ID: " + id));
        // ... resto do código de update
        vacinaDoBanco.setNome(request.getNome());
        vacinaDoBanco.setFabricante(request.getFabricante());
        vacinaDoBanco.setLoteFabricacao(request.getLoteFabricacao());
        vacinaDoBanco.setDataValidade(request.getDataValidade());
        vacinaDoBanco.setDiasCarenciaAbate(request.getDiasCarenciaAbate());
        vacinaDoBanco.setTipo(request.getTipo());
        vacinaRepository.saveAndFlush(vacinaDoBanco);
    }

    public List<VacinaResponse> listarTodos() {
        return vacinaRepository.findAll().stream()
                .map(this::converterEntidadeParaResponse)
                .collect(Collectors.toList());
    }

    public VacinaRequest buscarPorIdParaEdicao(Integer id) {
        Vacina vacinaDoBanco = vacinaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Vacina não encontrada com ID: " + id));
        return converterEntidadeParaRequest(vacinaDoBanco);
    }

    // --- DELETE (COM A MELHORIA) ---
    @Transactional // Garante atomicidade
    public void deletarVacinaPorId(Integer id) {
        Vacina vacina = vacinaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Vacina não encontrada com ID: " + id));

        // 3. 💡 A REGRA DE OURO: Verifica se a vacina já foi usada
        boolean existeAplicacao = !aplicacaoVacinaRepository.findByVacinaId(id).isEmpty();

        if (existeAplicacao) {
            throw new BusinessException("Não é possível excluir a vacina '" + vacina.getNome() +
                    "'. Ela já foi aplicada em animais e faz parte do histórico sanitário.");
        }

        vacinaRepository.delete(vacina);
    }

    // ... (Conversores permanecem iguais) ...
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