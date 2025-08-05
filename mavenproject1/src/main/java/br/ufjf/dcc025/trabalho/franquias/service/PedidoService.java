package br.ufjf.dcc025.trabalho.franquias.service;

import br.ufjf.dcc025.trabalho.franquias.model.pedido.Pedido;
import br.ufjf.dcc025.trabalho.franquias.model.produto.ItemPedido;
import br.ufjf.dcc025.trabalho.franquias.exceptions.ValidacaoException;
import br.ufjf.dcc025.trabalho.franquias.exceptions.EntidadeNaoEncontradaException;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PedidoService implements OperacoesCRUD<Pedido, Long> {
    private static final String ARQUIVO_PEDIDOS = "pedidos.dat";
    
    private List<Pedido> pedidos;
    private Long proximoId = 1L;
    private ProdutoService produtoService;
    private FranquiaService franquiaService;
    
    public PedidoService(ProdutoService produtoService, FranquiaService franquiaService) {
        this.pedidos = new ArrayList<>();
        this.produtoService = produtoService;
        this.franquiaService = franquiaService;
        carregarPedidos();
    }
    
    @Override
    public Pedido cadastrar(Pedido pedido) throws ValidacaoException {
        validarPedido(pedido);
        verificarEstoque(pedido);
        
        pedido.setId(proximoId++);
        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setStatus(Pedido.StatusPedido.PENDENTE);
        
        pedidos.add(pedido);
        salvarPedidos();
        return pedido;
    }
    
    @Override
    public Pedido atualizar(Long id, Pedido pedido) throws ValidacaoException, EntidadeNaoEncontradaException {
        Pedido pedidoExistente = buscarPorId(id);
        if (pedidoExistente == null) {
            throw new EntidadeNaoEncontradaException("Pedido com ID " + id + " não encontrado");
        }
        
        // Só permite alterar pedidos pendentes
        if (pedidoExistente.getStatus() != Pedido.StatusPedido.PENDENTE) {
            throw new ValidacaoException("Só é possível alterar pedidos pendentes");
        }
        
        validarPedido(pedido);
        verificarEstoque(pedido);
        
        pedidoExistente.setCliente(pedido.getCliente());
        pedidoExistente.setItens(pedido.getItens());
        pedidoExistente.setVendedor(pedido.getVendedor());
        pedidoExistente.setFranquia(pedido.getFranquia());
        
        salvarPedidos();
        return pedidoExistente;
    }
    
    @Override
    public boolean remover(Long id) throws ValidacaoException, EntidadeNaoEncontradaException {
        Pedido pedido = buscarPorId(id);
        if (pedido == null) {
            throw new EntidadeNaoEncontradaException("Pedido com ID " + id + " não encontrado");
        }
        
        if (pedido.getStatus() != Pedido.StatusPedido.PENDENTE) {
            throw new ValidacaoException("Só é possível cancelar pedidos pendentes");
        }
        
        pedidos.remove(pedido);
        salvarPedidos();
        return true;
    }
    
    @Override
    public Pedido buscarPorId(Long id) {
        return pedidos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public List<Pedido> listar() {
        return new ArrayList<>(pedidos);
    }
    
    public Pedido confirmarPedido(Long pedidoId) throws EntidadeNaoEncontradaException, ValidacaoException {
        Pedido pedido = buscarPorId(pedidoId);
        if (pedido == null) {
            throw new EntidadeNaoEncontradaException("Pedido com ID " + pedidoId + " não encontrado");
        }
        
        if (pedido.getStatus() != Pedido.StatusPedido.PENDENTE) {
            throw new ValidacaoException("Só é possível confirmar pedidos pendentes");
        }
        
        for (ItemPedido item : pedido.getItens()) {
            try {
                produtoService.retirarEstoque(item.getProduto().getId(), item.getQuantidade());
            } catch (Exception e) {
                throw new ValidacaoException("Erro ao retirar estoque do produto " + item.getProduto().getNome() + ": " + e.getMessage());
            }
        }
        
        franquiaService.adicionarReceita(pedido.getFranquia().getId(), pedido.getTotal());
        franquiaService.atualizarTickets(pedido.getFranquia().getId(), 1);
        
        pedido.setStatus(Pedido.StatusPedido.CONFIRMADO);
        salvarPedidos();
        return pedido;
    }
    
    public Pedido entregarPedido(Long pedidoId) throws EntidadeNaoEncontradaException, ValidacaoException {
        Pedido pedido = buscarPorId(pedidoId);
        if (pedido == null) {
            throw new EntidadeNaoEncontradaException("Pedido com ID " + pedidoId + " não encontrado");
        }
        
        if (pedido.getStatus() != Pedido.StatusPedido.CONFIRMADO) {
            throw new ValidacaoException("Só é possível entregar pedidos confirmados");
        }
        
        pedido.setStatus(Pedido.StatusPedido.FINALIZADO);
        pedido.setDataFinalizacao(LocalDateTime.now());
        salvarPedidos();
        return pedido;
    }
    
    public Pedido cancelarPedido(Long pedidoId) throws EntidadeNaoEncontradaException, ValidacaoException {
        Pedido pedido = buscarPorId(pedidoId);
        if (pedido == null) {
            throw new EntidadeNaoEncontradaException("Pedido com ID " + pedidoId + " não encontrado");
        }
        
        if (pedido.getStatus() == Pedido.StatusPedido.FINALIZADO) {
            throw new ValidacaoException("Não é possível cancelar pedidos já entregues");
        }
        
        if (pedido.getStatus() == Pedido.StatusPedido.CONFIRMADO) {
            for (ItemPedido item : pedido.getItens()) {
                try {
                    produtoService.adicionarEstoque(item.getProduto().getId(), item.getQuantidade());
                } catch (Exception e) {
                    throw new ValidacaoException("Erro ao devolver estoque do produto " + item.getProduto().getNome() + ": " + e.getMessage());
                }
            }
        }
        
        pedido.setStatus(Pedido.StatusPedido.CANCELADO);
        salvarPedidos();
        return pedido;
    }
    
    public List<Pedido> buscarPorCliente(String nomeCliente) {
        return pedidos.stream()
                .filter(p -> p.getCliente().getNome().toLowerCase().contains(nomeCliente.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    public List<Pedido> buscarPorVendedor(Long vendedorId) {
        return pedidos.stream()
                .filter(p -> p.getVendedor().getId().equals(vendedorId))
                .collect(Collectors.toList());
    }
    
    public List<Pedido> buscarPorFranquia(Long franquiaId) {
        return pedidos.stream()
                .filter(p -> p.getFranquia().getId().equals(franquiaId))
                .collect(Collectors.toList());
    }
    
    public List<Pedido> buscarPorStatus(Pedido.StatusPedido status) {
        return pedidos.stream()
                .filter(p -> p.getStatus() == status)
                .collect(Collectors.toList());
    }
    
    public List<Pedido> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return pedidos.stream()
                .filter(p -> p.getDataCriacao().isAfter(inicio) && p.getDataCriacao().isBefore(fim))
                .collect(Collectors.toList());
    }
    
    // Relatórios e estatísticas
    public double getReceitaTotal() {
        return pedidos.stream()
                .filter(p -> p.getStatus() == Pedido.StatusPedido.FINALIZADO)
                .mapToDouble(Pedido::getTotal)
                .sum();
    }
    
    public double getReceitaPorFranquia(Long franquiaId) {
        return pedidos.stream()
                .filter(p -> p.getFranquia().getId().equals(franquiaId) && p.getStatus() == Pedido.StatusPedido.FINALIZADO)
                .mapToDouble(Pedido::getTotal)
                .sum();
    }
    
    public double getReceitaPorVendedor(Long vendedorId) {
        return pedidos.stream()
                .filter(p -> p.getVendedor().getId().equals(vendedorId) && p.getStatus() == Pedido.StatusPedido.FINALIZADO)
                .mapToDouble(Pedido::getTotal)
                .sum();
    }
    
    public long getQuantidadePedidosEntregues() {
        return pedidos.stream()
                .filter(p -> p.getStatus() == Pedido.StatusPedido.FINALIZADO)
                .count();
    }
    
    private void validarPedido(Pedido pedido) throws ValidacaoException {
        if (pedido == null) {
            throw new ValidacaoException("Pedido não pode ser nulo");
        }
        
        if (pedido.getCliente() == null) {
            throw new ValidacaoException("Cliente é obrigatório");
        }
        
        if (pedido.getVendedor() == null) {
            throw new ValidacaoException("Vendedor é obrigatório");
        }
        
        if (pedido.getFranquia() == null) {
            throw new ValidacaoException("Franquia é obrigatória");
        }
        
        if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
            throw new ValidacaoException("Pedido deve ter pelo menos um item");
        }
        
        for (ItemPedido item : pedido.getItens()) {
            if (item.getQuantidade() <= 0) {
                throw new ValidacaoException("Quantidade do item deve ser positiva");
            }
        }
    }
    
    private void verificarEstoque(Pedido pedido) throws ValidacaoException {
        for (ItemPedido item : pedido.getItens()) {
            try {
                boolean disponivel = produtoService.verificarDisponibilidade(
                    item.getProduto().getId(), item.getQuantidade());
                
                if (!disponivel) {
                    throw new ValidacaoException(
                        "Estoque insuficiente para o produto: " + item.getProduto().getNome());
                }
            } catch (EntidadeNaoEncontradaException e) {
                throw new ValidacaoException("Produto não encontrado: " + item.getProduto().getNome());
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    private void carregarPedidos() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_PEDIDOS))) {
            pedidos = (List<Pedido>) ois.readObject();
            proximoId = (Long) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // Arquivo não existe, criar lista vazia
            pedidos = new ArrayList<>();
        }
    }
    
    private void salvarPedidos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_PEDIDOS))) {
            oos.writeObject(pedidos);
            oos.writeObject(proximoId);
        } catch (IOException e) {
            System.err.println("Erro ao salvar pedidos: " + e.getMessage());
        }
    }
}