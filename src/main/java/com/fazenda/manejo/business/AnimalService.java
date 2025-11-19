package com.fazenda.manejo.business;

// ... imports ...
import com.fazenda.manejo.infrastructure.dto.AnimalRequest;
import com.fazenda.manejo.infrastructure.dto.AnimalResponse;
import com.fazenda.manejo.infrastructure.entitys.Animal;
import com.fazenda.manejo.infrastructure.entitys.Lote;
import com.fazenda.manejo.infrastructure.entitys.Pesagem; // NOVO: Para usar a entidade Pesagem
import com.fazenda.manejo.infrastructure.repository.AnimalRepository;
import com.fazenda.manejo.infrastructure.repository.LoteRepository;
import com.fazenda.manejo.infrastructure.repository.PesagemRepository; // NOVO: Repositório de Pesagem
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
    // 💡 NOVO: Injeção do repositório de Pesagem
    private final PesagemRepository pesagemRepository;


    @Transactional
    public void salvarAnimal(AnimalRequest request) {
        if (request.getLoteId() == null) {
            // 💡 USO DA EXCEÇÃO PERSONALIZADA
            throw new BusinessException("Erro: Nenhum lote foi selecionado para o animal.");
        }

        Lote lote = loteRepository.findById(request.getLoteId())
                .orElseThrow(() -> new BusinessException("Lote não encontrado com ID: " + request.getLoteId()));

        // Verifica se já existe brinco (Regra extra de consistência)
        if (request.getId() == null && animalRepository.findByBrinco(request.getBrinco()).isPresent()) {
            throw new BusinessException("Já existe um animal cadastrado com o brinco: " + request.getBrinco());
        }

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

    @Transactional
    public void atualizarAnimal(Integer id, AnimalRequest request) {
        Animal animalDoBanco = animalRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Animal não encontrado com ID: " + id));

        if (request.getLoteId() == null) {
            throw new BusinessException("Erro: Nenhum lote foi selecionado para o animal.");
        }

        Lote lote = loteRepository.findById(request.getLoteId())
                .orElseThrow(() -> new BusinessException("Lote não encontrado com ID: " + request.getLoteId()));

        // Verifica duplicidade de brinco na edição (se mudou o brinco)
        if (!animalDoBanco.getBrinco().equals(request.getBrinco()) &&
                animalRepository.findByBrinco(request.getBrinco()).isPresent()) {
            throw new BusinessException("Já existe outro animal com o brinco: " + request.getBrinco());
        }

        // ... (setters permanecem iguais)
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

    // ... (Listar e Buscar permanecem iguais, apenas trocando Exception se houver) ...
    public List<AnimalResponse> listarTodos() {
        return animalRepository.findAll().stream()
                .map(this::converterEntidadeParaResponse)
                .collect(Collectors.toList());
    }

    public AnimalRequest buscarPorIdParaEdicao(Integer id) {
        Animal animalDoBanco = animalRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Animal não encontrado com ID: " + id));
        return converterEntidadeParaRequest(animalDoBanco);
    }

    public void deletarAnimalPorId(Integer id) {
        if (!animalRepository.existsById(id)) {
            throw new BusinessException("Animal não encontrado para exclusão.");
        }
        // O Controller já trata o DataIntegrityViolationException, então aqui ok.
        animalRepository.deleteById(id);
    }

    // --- CONVERSOR ATUALIZADO ---
    private AnimalResponse converterEntidadeParaResponse(Animal entidade) {
        String nomeLote = (entidade.getLote() != null) ? entidade.getLote().getNome() : "Sem Lote";

        // 💡 NOVO: Lógica para encontrar o último peso
        Double ultimoPeso = null;
        // O repositório de Pesagem já tem o método para buscar ordenado do mais recente ao mais antigo
        List<Pesagem> pesagens = pesagemRepository.findByAnimalIdOrderByDataPesagemDesc(entidade.getId());

        // Se houver pesagens, pega o peso do primeiro (o mais recente)
        if (!pesagens.isEmpty()) {
            ultimoPeso = pesagens.get(0).getPeso();
        }

        return AnimalResponse.builder()
                .id(entidade.getId())
                .brinco(entidade.getBrinco())
                .sexo(entidade.getSexo())
                .raca(entidade.getRaca())
                .dataNascimento(entidade.getDataNascimento())
                .status(entidade.getStatus())
                .nomeLote(nomeLote)
                .ultimoPeso(ultimoPeso) // 💡 NOVO: Adiciona o peso ao DTO
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