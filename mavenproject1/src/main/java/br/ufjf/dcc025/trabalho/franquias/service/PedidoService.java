/*
 * Autores: Sara Ingrid - 202365056A, Angélica Coutinho - 202365064A
 */
package br.ufjf.dcc025.trabalho.franquias.service;

import br.ufjf.dcc025.trabalho.franquias.exceptions.EntidadeNaoEncontradaException;
import br.ufjf.dcc025.trabalho.franquias.exceptions.FranquiaException;
import br.ufjf.dcc025.trabalho.franquias.exceptions.ValidacaoException;
import br.ufjf.dcc025.trabalho.franquias.model.pedido.Pedido;
import br.ufjf.dcc025.trabalho.franquias.model.produto.ItemPedido;
import br.ufjf.dcc025.trabalho.franquias.model.produto.Produto;
import br.ufjf.dcc025.trabalho.franquias.model.usuarios.Vendedor;
import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class PedidoService implements OperacoesCRUD<Pedido, Long> {
    private static final String ARQUIVO_PEDIDOS = "pedidos.dat";
    private List<Pedido> pedidos;
    private Long proximoId;
    private final ProdutoService produtoService;
    private final FranquiaService franquiaService;
    private final UsuarioService usuarioService;
    
    public PedidoService() {
        this.pedidos = new ArrayList<>();
        this.proximoId = 1L;
        this.produtoService = new ProdutoService();
        this.franquiaService = new FranquiaService();
        this.usuarioService = new UsuarioService();
        carregarDados();
    }
    
    @Override
    public Pedido cadastrar(Pedido pedido) throws ValidacaoException {
        try {
            if (pedido == null) {
                throw new ValidacaoException("Pedido não pode ser nulo");
            }
            
            if (!validar(pedido)) {
                throw new ValidacaoException("Pedido inválido");
            }
            
            pedido.setId(proximoId++);
            calcularValorTotal(pedido);
            pedidos.add(pedido);
            
            salvarDados();
            return pedido;
        } catch (ValidacaoException e) {
            throw e;
        } catch (Exception e) {
            throw new ValidacaoException("Erro ao criar pedido: " + e.getMessage());
        }
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
    
    @Override
    public Pedido atualizar(Pedido pedido) throws ValidacaoException, EntidadeNaoEncontradaException {
        try {
            if (pedido == null || pedido.getId() == null) {
                throw new ValidacaoException("Pedido ou ID não pode ser nulo");
            }
            
            if (!validar(pedido)) {
                throw new ValidacaoException("Pedido inválido");
            }
            
            buscarPorId(pedido.getId());
            
            for (int i = 0; i < pedidos.size(); i++) {
                if (pedidos.get(i).getId().equals(pedido.getId())) {
                    calcularValorTotal(pedido);
                    pedidos.set(i, pedido);
                    break;
                }
            }
            
            salvarDados();
            return pedido;
        } catch (ValidacaoException e) {
            throw e;
        } catch (Exception e) {
            throw new ValidacaoException("Erro ao atualizar pedido: " + e.getMessage());
        }
    }
    
    @Override
    public boolean remover(Long id) throws EntidadeNaoEncontradaException {
        try {
            Pedido pedido = buscarPorId(id);
            
            if (pedido.getStatus() == Pedido.StatusPedido.ENTREGUE) {
                throw new ValidacaoException("Não é possível remover pedido já entregue");
            }
            
            boolean removido = pedidos.removeIf(p -> p.getId().equals(id));
            if (removido) {
                salvarDados();
            }
            return removido;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean validar(Pedido pedido) throws ValidacaoException {
        if (pedido.getNomeCliente() == null || pedido.getNomeCliente().trim().isEmpty()) {
            throw new ValidacaoException("Nome do cliente é obrigatório");
        }
        
        if (pedido.getVendedor() == null) {
            throw new ValidacaoException("Vendedor é obrigatório");
        }
        
        if (pedido.getFranquia().getId() == null) {
            throw new ValidacaoException("Franquia é obrigatória");
        }
        
        if (pedido.getItens().isEmpty()) {
            throw new ValidacaoException("Pedido deve ter pelo menos um item");
        }
        
        try {
            franquiaService.buscarFranquiaPorId(pedido.getFranquia().getId());
        } catch (Exception e) {
            throw new ValidacaoException("Franquia não encontrada");
        }
        
        return true;
    }
    
    public void salvar(Pedido pedido) throws FranquiaException {
        if (pedido.getId() == null) {
            cadastrar(pedido);
        } else {
            try {
                atualizar(pedido);
            } catch (EntidadeNaoEncontradaException e) {
                throw new FranquiaException("Pedido não encontrado para atualização");
            }
        }
    }
    
    public void deletar(Long id) throws FranquiaException {
        try {
            if (!remover(id)) {
                throw new FranquiaException("Erro ao deletar pedido");
            }
        } catch (EntidadeNaoEncontradaException e) {
            throw new FranquiaException("Pedido não encontrado para remoção");
        }
    }
    
    public List<Pedido> listarPedidosPorVendedor(Vendedor vendedor) {
        return pedidos.stream()
                .filter(p -> p.getVendedor().getId().equals(vendedor.getId()))
                .sorted((p1, p2) -> p2.getDataCriacao().compareTo(p1.getDataCriacao()))
                .collect(Collectors.toList());
    }
    
    public List<Pedido> listarPedidosPorFranquia(Long franquiaId) {
        return pedidos.stream()
                .filter(p -> p.getFranquia().getId().equals(franquiaId))
                .sorted((p1, p2) -> p2.getDataCriacao().compareTo(p1.getDataCriacao()))
                .collect(Collectors.toList());
    }
    
    public List<Pedido> listarPedidosPorStatus(Pedido.StatusPedido status) {
        return pedidos.stream()
                .filter(p -> p.getStatus() == status)
                .sorted((p1, p2) -> p2.getDataCriacao().compareTo(p1.getDataCriacao()))
                .collect(Collectors.toList());
    }
    
    public List<Pedido> listarPedidosPorPeriodo(LocalDate inicio, LocalDate fim) {
        return pedidos.stream()
                .filter(p -> {
                    LocalDate dataPedido = p.getDataCriacao().toLocalDate();
                    return !dataPedido.isBefore(inicio) && !dataPedido.isAfter(fim);
                })
                .sorted((p1, p2) -> p2.getDataCriacao().compareTo(p1.getDataCriacao()))
                .collect(Collectors.toList());
    }
    
    public void atualizarStatus(Long pedidoId, Pedido.StatusPedido novoStatus) throws FranquiaException {
        Pedido pedido = buscarPorId(pedidoId);
        Pedido.StatusPedido statusAnterior = pedido.getStatus();
        pedido.setStatus(novoStatus);
        
        if (novoStatus == Pedido.StatusPedido.ENTREGUE && statusAnterior != Pedido.StatusPedido.ENTREGUE) {
            Vendedor vendedor = pedido.getVendedor();
            vendedor.adicionarVenda(pedido.getValorTotal());
            
            try {
                usuarioService.atualizarVendedor(vendedor);
            } catch (Exception e) {
                throw new FranquiaException("Erro ao atualizar vendas do vendedor: " + e.getMessage());
            }
        }
        
        salvar(pedido);
    }
    
    public void adicionarItem(Long pedidoId, Produto produto, int quantidade, String observacoes) throws FranquiaException {
        Pedido pedido = buscarPorId(pedidoId);
        
        if (pedido.getStatus() != Pedido.StatusPedido.PENDENTE) {
            throw new ValidacaoException("Só é possível adicionar itens a pedidos pendentes");
        }
        
        if (produto.getEstoque() < quantidade) {
            throw new ValidacaoException(
                "Estoque insuficiente. Disponível: " + produto.getEstoque()
            );
        }
        
        ItemPedido item = new ItemPedido(produto, quantidade);
        pedido.adicionarItem(item);
        
        produtoService.atualizarEstoque(produto.getId(), produto.getEstoque() - quantidade);
        
        salvar(pedido);
    }
    
    public void removerItem(Long pedidoId, int indiceItem) throws FranquiaException {
        Pedido pedido = buscarPorId(pedidoId);
        
        if (pedido.getStatus() != Pedido.StatusPedido.PENDENTE) {
            throw new ValidacaoException("Só é possível remover itens de pedidos pendentes");
        }
        
        if (indiceItem < 0 || indiceItem >= pedido.getItens().size()) {
            throw new ValidacaoException("Índice de item inválido");
        }
        
        ItemPedido item = pedido.getItens().get(indiceItem);
        
        produtoService.atualizarEstoque(
            item.getProduto().getId(), 
            item.getProduto().getEstoque() + item.getQuantidade()
        );
        
        pedido.getItens().remove(indiceItem);
        salvar(pedido);
    }
    
    public Map<String, Object> calcularEstatisticas() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalPedidos", pedidos.size());
        stats.put("pedidosPendentes", pedidos.stream().filter(p -> p.getStatus() == Pedido.StatusPedido.PENDENTE).count());
        stats.put("pedidosEntregues", pedidos.stream().filter(p -> p.getStatus() == Pedido.StatusPedido.ENTREGUE).count());
        
        double faturamentoTotal = pedidos.stream()
                .filter(p -> p.getStatus() == Pedido.StatusPedido.ENTREGUE)
                .mapToDouble(Pedido::getValorTotal)
                .sum();
        stats.put("faturamentoTotal", faturamentoTotal);
        
        long pedidosEntregues = (Long) stats.get("pedidosEntregues");
        if (pedidosEntregues > 0) {
            stats.put("ticketMedio", faturamentoTotal / pedidosEntregues);
        } else {
            stats.put("ticketMedio", 0.0);
        }
        
        return stats;
    }
    
    private void calcularValorTotal(Pedido pedido) {
        double subtotal = pedido.getItens().stream()
                .mapToDouble(ItemPedido::getSubtotal)
                .sum();
        
        double total = subtotal + pedido.getTaxaEntrega() + pedido.getTaxaServico();
        pedido.setValorTotal(total);
    }
    
    @SuppressWarnings("unchecked")
    private void carregarDados() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_PEDIDOS))) {
            pedidos = (List<Pedido>) ois.readObject();
            
            proximoId = pedidos.stream()
                    .mapToLong(Pedido::getId)
                    .max()
                    .orElse(0L) + 1;
                    
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            System.err.println("Erro ao carregar pedidos: " + e.getMessage());
        }
    }
    
    private void salvarDados() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_PEDIDOS))) {
            oos.writeObject(pedidos);
        }
    }
}