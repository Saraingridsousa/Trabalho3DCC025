package br.ufjf.dcc025.trabalho.franquias.service;

import br.ufjf.dcc025.trabalho.franquias.model.franquia.Franquia;
import br.ufjf.dcc025.trabalho.franquias.model.franquia.Endereco;
import br.ufjf.dcc025.trabalho.franquias.exceptions.ValidacaoException;
import br.ufjf.dcc025.trabalho.franquias.exceptions.EntidadeNaoEncontradaException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FranquiaService implements OperacoesCRUD<Franquia, Long> {
    private static final String ARQUIVO_FRANQUIAS = "franquias.dat";
    
    private List<Franquia> franquias;
    private Long proximoId = 1L;
    
    public FranquiaService() {
        this.franquias = new ArrayList<>();
        carregarFranquias();
    }
    
    @Override
    public Franquia cadastrar(Franquia franquia) throws ValidacaoException {
        validarFranquia(franquia);
        
        if (existeNome(franquia.getNome())) {
            throw new ValidacaoException("Já existe uma franquia com este nome");
        }
        
        franquia.setId(proximoId++);
        franquias.add(franquia);
        salvarFranquias();
        return franquia;
    }
    
    @Override
    public Franquia atualizar(Long id, Franquia franquia) throws ValidacaoException, EntidadeNaoEncontradaException {
        Franquia franquiaExistente = buscarPorId(id);
        if (franquiaExistente == null) {
            throw new EntidadeNaoEncontradaException("Franquia com ID " + id + " não encontrada");
        }

        validarFranquia(franquia);

        if (existeNomeParaOutraFranquia(franquia.getNome(), id)) {
            throw new ValidacaoException("Já existe uma franquia com este nome");
        }

        franquiaExistente.setNome(franquia.getNome());
        franquiaExistente.setEndereco(franquia.getEndereco());
        franquiaExistente.setGerente(franquia.getGerente());

        salvarFranquias();
        return franquiaExistente;
    }
    
    @Override
    public boolean remover(Long id) throws EntidadeNaoEncontradaException {
        Franquia franquia = buscarPorId(id);
        if (franquia == null) {
            throw new EntidadeNaoEncontradaException("Franquia com ID " + id + " não encontrada");
        }

        franquias.remove(franquia);
        salvarFranquias();
        return true;
    }
    
    @Override
    public Franquia buscarPorId(Long id) {
        return franquias.stream()
                .filter(f -> f.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public List<Franquia> listar() {
        return new ArrayList<>(franquias);
    }
    
    public List<Franquia> buscarPorCidade(String cidade) {
        return franquias.stream()
                .filter(f -> f.getEndereco().getCidade().equalsIgnoreCase(cidade))
                .collect(Collectors.toList());
    }
    
    public List<Franquia> buscarPorGerente(Long gerenteId) {
        return franquias.stream()
                .filter(f -> f.getGerente() != null && f.getGerente().getId().equals(gerenteId))
                .collect(Collectors.toList());
    }
    
    public List<Franquia> buscarPorNome(String nome) {
        return franquias.stream()
                .filter(f -> f.getNome().toLowerCase().contains(nome.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    public void adicionarReceita(Long franquiaId, double valor) throws EntidadeNaoEncontradaException {
        Franquia franquia = buscarPorId(franquiaId);
        if (franquia == null) {
            throw new EntidadeNaoEncontradaException("Franquia com ID " + franquiaId + " não encontrada");
        }
        
        franquia.adicionarReceita(valor);
        salvarFranquias();
    }
    
    public void atualizarTickets(Long franquiaId, int novosTickets) throws EntidadeNaoEncontradaException {
        Franquia franquia = buscarPorId(franquiaId);
        if (franquia == null) {
            throw new EntidadeNaoEncontradaException("Franquia com ID " + franquiaId + " não encontrada");
        }
        
        franquia.setTotalPedidos(franquia.getTotalPedidos() + novosTickets);
        salvarFranquias();
    }
    
    public double getReceitaTotal() {
        return franquias.stream()
                .mapToDouble(Franquia::getReceitaAcumulada)
                .sum();
    }
    
    private void validarFranquia(Franquia franquia) throws ValidacaoException {
        if (franquia == null) {
            throw new ValidacaoException("Franquia não pode ser nula");
        }
        
        if (franquia.getNome() == null || franquia.getNome().trim().isEmpty()) {
            throw new ValidacaoException("Nome da franquia é obrigatório");
        }
        
        if (franquia.getEndereco() == null) {
            throw new ValidacaoException("Endereço da franquia é obrigatório");
        }
        
        validarEndereco(franquia.getEndereco());
    }
    
    private void validarEndereco(Endereco endereco) throws ValidacaoException {
        if (endereco.getLogradouro()== null || endereco.getLogradouro().trim().isEmpty()) {
            throw new ValidacaoException("Rua é obrigatória");
        }
        
        if (endereco.getCidade() == null || endereco.getCidade().trim().isEmpty()) {
            throw new ValidacaoException("Cidade é obrigatória");
        }
        
        if (endereco.getEstado() == null || endereco.getEstado().trim().isEmpty()) {
            throw new ValidacaoException("Estado é obrigatório");
        }
        
        if (endereco.getCep() == null || endereco.getCep().trim().isEmpty()) {
            throw new ValidacaoException("CEP é obrigatório");
        }
    }
    
    private boolean existeNome(String nome) {
        return franquias.stream().anyMatch(f -> f.getNome().equalsIgnoreCase(nome.trim()));
    }
    
    private boolean existeNomeParaOutraFranquia(String nome, Long id) {
        return franquias.stream().anyMatch(f -> f.getNome().equalsIgnoreCase(nome.trim()) && !f.getId().equals(id));
    }
    
    @SuppressWarnings("unchecked")
    private void carregarFranquias() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_FRANQUIAS))) {
            franquias = (List<Franquia>) ois.readObject();
            proximoId = (Long) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            franquias = new ArrayList<>();
        }
    }
    
    private void salvarFranquias() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_FRANQUIAS))) {
            oos.writeObject(franquias);
            oos.writeObject(proximoId);
        } catch (IOException e) {
            System.err.println("Erro ao salvar franquias: " + e.getMessage());
        }
    }
}
