/*
 * Autores: Sara Ingrid - 202365056A, Angélica Coutinho - 202365064A
 */
package br.ufjf.dcc025.trabalho.franquias.service;

import br.ufjf.dcc025.trabalho.franquias.exceptions.EntidadeNaoEncontradaException;
import br.ufjf.dcc025.trabalho.franquias.model.usuarios.Gerente;
import br.ufjf.dcc025.trabalho.franquias.model.usuarios.Vendedor;
import br.ufjf.dcc025.trabalho.franquias.model.franquia.Franquia;
import br.ufjf.dcc025.trabalho.franquias.model.pedido.Cliente;
import br.ufjf.dcc025.trabalho.franquias.model.pedido.Pedido;
import br.ufjf.dcc025.trabalho.franquias.exceptions.FranquiaException;
import br.ufjf.dcc025.trabalho.franquias.exceptions.ValidacaoException;
import br.ufjf.dcc025.trabalho.franquias.model.franquia.Endereco;
import br.ufjf.dcc025.trabalho.franquias.model.produto.Produto;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FranquiaService {
    private static final String ARQUIVO_FRANQUIAS = "franquias.dat";
    private static final String ARQUIVO_PRODUTOS = "produtos.dat";
    private static final String ARQUIVO_PEDIDOS = "pedidos.dat";
    private static final String ARQUIVO_CLIENTES = "clientes.dat";
    
    private List<Franquia> franquias;
    private List<Produto> produtos;
    private List<Pedido> pedidos;
    private List<Cliente> clientes;
    private Long proximoIdFranquia = 1L;
    private Long proximoIdProduto = 1L;
    private Long proximoIdPedido = 1L;
    private Long proximoIdCliente = 1L;
    private UsuarioService usuarioService;
    
    public FranquiaService() {
        this.franquias = new ArrayList<>();
        this.produtos = new ArrayList<>();
        this.pedidos = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.usuarioService = new UsuarioService();
        carregarDados();
    }
    
    // ========== MÉTODOS DE FRANQUIA ==========
    
    public Franquia cadastrar(Franquia franquia) 
            throws ValidacaoException {
        validarDadosFranquia(franquia.getNome(), franquia.getEndereco().getLogradouro(), 
                franquia.getEndereco().getCidade(), franquia.getEndereco().getEstado(), franquia.getEndereco().getCep());
        
        franquia.setId(proximoIdFranquia++);
        
        if (franquia.getGerente() != null) {
            franquia.getGerente().setFranquiaId(franquia.getId());
            
            try {
                usuarioService.editarUsuario(franquia.getGerente().getId(), franquia.getGerente().getNome(), franquia.getGerente().getEmail());
                usuarioService.atualizarFranquiaIdGerente(franquia.getGerente().getId(), franquia.getId());
            } catch (Exception e) {
                System.err.println("Erro ao sincronizar gerente com franquia: " + e.getMessage());
            }
        }
        
        franquias.add(franquia);
        salvarFranquias();
        return franquia;
    }
    
    public boolean remover(Long id) throws EntidadeNaoEncontradaException {
        Franquia franquia = buscarFranquiaPorId(id);
        if (franquia == null) {
            throw new EntidadeNaoEncontradaException("Franquia", id);
        }
        
        boolean removida = franquias.remove(franquia);
        if (removida) {
            salvarFranquias();
        }
        return removida;
    }
    
    public void atualizar(Franquia franquia) throws FranquiaException {
        if (franquia == null || franquia.getId() == null) {
            throw new ValidacaoException("Franquia inválida");
        }
        
        for (int i = 0; i < franquias.size(); i++) {
            if (franquias.get(i).getId().equals(franquia.getId())) {
                franquias.set(i, franquia);
                salvarFranquias();
                return;
            }
        }
        
        throw new EntidadeNaoEncontradaException("Franquia", franquia.getId());
    }
    
    public List<Franquia> listar() {
        return new ArrayList<>(franquias);
    }
    
    public Franquia buscarFranquiaPorId(Long id) {
        return franquias.stream()
                .filter(f -> f.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    // ========== MÉTODOS DE PRODUTO ==========
    
    public Produto cadastrarProduto(String nome, String descricao, double preco, int quantidadeEstoque, 
            int estoqueMinimo, String categoria) throws ValidacaoException {
        validarDadosProduto(nome, preco, quantidadeEstoque);
        
        Produto produto = new Produto(nome, descricao, preco, quantidadeEstoque, estoqueMinimo, categoria);
        produto.setId(proximoIdProduto++);
        
        produtos.add(produto);
        salvarProdutos();
        return produto;
    }
    
    public List<Produto> listarProdutosEstoqueBaixo() {
        return produtos.stream()
                .filter(Produto::isEstoqueBaixo)
                .collect(Collectors.toList());
    }
    
    public List<Produto> listarProdutos() {
        return new ArrayList<>(produtos);
    }
    
    public Produto buscarProdutoPorId(Long id) {
        return produtos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    // ========== MÉTODOS DE PEDIDO ==========
    
    public Pedido cadastrarPedido(Pedido pedido) throws ValidacaoException {
        if (!pedido.isValido()) {
            throw new ValidacaoException("Dados do pedido são inválidos");
        }
        
        pedido.setId(proximoIdPedido++);
        pedidos.add(pedido);
        
        // Atualizar estatísticas da franquia e vendedor
        Franquia franquia = buscarFranquiaPorId(pedido.getFranquia().getId());
        if (franquia != null) {
            franquia.adicionarVenda(pedido.getValorTotal());
        }
        
        if (pedido.getVendedor() != null) {
            pedido.getVendedor().adicionarVenda(pedido.getValorTotal());
        }
        
        salvarPedidos();
        salvarFranquias();
        return pedido;
    }
    
    public List<Pedido> listarPedidosPorFranquia(Long franquiaId) {
        return pedidos.stream()
                .filter(p -> p.getFranquia().getId().equals(franquiaId))
                .collect(Collectors.toList());
    }
    
    public List<Pedido> listarPedidosPorVendedor(Long vendedorId) {
        return pedidos.stream()
                .filter(p -> p.getVendedor() != null && p.getVendedor().getId().equals(vendedorId))
                .collect(Collectors.toList());
    }
    
    // ========== MÉTODOS DE RELATÓRIO ==========
    
    public List<Vendedor> getRankingVendedores(Long franquiaId) {
        Franquia franquia = buscarFranquiaPorId(franquiaId);
        if (franquia == null) {
            return new ArrayList<>();
        }
        
        return franquia.getVendedores().stream()
                .sorted((v1, v2) -> Double.compare(v2.getTotalVendas(), v1.getTotalVendas()))
                .collect(Collectors.toList());
    }
    
    public Map<String, Object> gerarRelatorioFranquia(Long franquiaId) {
        Franquia franquia = buscarFranquiaPorId(franquiaId);
        Map<String, Object> relatorio = new HashMap<>();
        
        if (franquia != null) {
            relatorio.put("franquia", franquia);
            relatorio.put("faturamentoBruto", franquia.getReceitaAcumulada());
            relatorio.put("totalPedidos", franquia.getTotalPedidos());
            relatorio.put("ticketMedio", franquia.getTicketMedio());
            relatorio.put("rankingVendedores", getRankingVendedores(franquiaId));
        }
        
        return relatorio;
    }
    
    // ========== MÉTODOS DE VALIDAÇÃO ==========
    
    private void validarDadosFranquia(String nome, String endereco, String cidade, String estado, String cep) 
            throws ValidacaoException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidacaoException("Nome da franquia é obrigatório");
        }
        if (endereco == null || endereco.trim().isEmpty()) {
            throw new ValidacaoException("Endereço é obrigatório");
        }
        if (cidade == null || cidade.trim().isEmpty()) {
            throw new ValidacaoException("Cidade é obrigatória");
        }
        if (estado == null || estado.trim().isEmpty()) {
            throw new ValidacaoException("Estado é obrigatório");
        }
        if (cep == null || !cep.matches("\\d{5}-?\\d{3}")) {
            throw new ValidacaoException("CEP deve ter formato válido (00000-000)");
        }
    }
    
    private void validarDadosProduto(String nome, double preco, int quantidadeEstoque) 
            throws ValidacaoException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidacaoException("Nome do produto é obrigatório");
        }
        if (preco < 0) {
            throw new ValidacaoException("Preço não pode ser negativo");
        }
        if (quantidadeEstoque < 0) {
            throw new ValidacaoException("Quantidade em estoque não pode ser negativa");
        }
    }
    
    // ========== MÉTODOS DE PERSISTÊNCIA ==========
    
    private void carregarDados() {
        try {
            carregarFranquias();
            carregarProdutos();
            carregarPedidos();
            carregarClientes();
        } catch (Exception e) {
            System.err.println("Erro ao carregar dados: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private void carregarFranquias() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_FRANQUIAS))) {
            Object[] dados = (Object[]) ois.readObject();
            this.franquias = (List<Franquia>) dados[0];
            this.proximoIdFranquia = (Long) dados[1];
        } catch (FileNotFoundException e) {
            this.franquias = new ArrayList<>();
            this.proximoIdFranquia = 1L;
        } catch (Exception e) {
            System.err.println("Erro ao carregar franquias: " + e.getMessage());
            this.franquias = new ArrayList<>();
            this.proximoIdFranquia = 1L;
        }
    }
    
    private void salvarFranquias() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_FRANQUIAS))) {
            Object[] dados = {franquias, proximoIdFranquia};
            oos.writeObject(dados);
        } catch (IOException e) {
            System.err.println("Erro ao salvar franquias: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private void carregarProdutos() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_PRODUTOS))) {
            Object[] dados = (Object[]) ois.readObject();
            this.produtos = (List<Produto>) dados[0];
            this.proximoIdProduto = (Long) dados[1];
        } catch (FileNotFoundException e) {
            this.produtos = new ArrayList<>();
            this.proximoIdProduto = 1L;
        } catch (Exception e) {
            System.err.println("Erro ao carregar produtos: " + e.getMessage());
            this.produtos = new ArrayList<>();
            this.proximoIdProduto = 1L;
        }
    }
    
    private void salvarProdutos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_PRODUTOS))) {
            Object[] dados = {produtos, proximoIdProduto};
            oos.writeObject(dados);
        } catch (IOException e) {
            System.err.println("Erro ao salvar produtos: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private void carregarPedidos() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_PEDIDOS))) {
            Object[] dados = (Object[]) ois.readObject();
            this.pedidos = (List<Pedido>) dados[0];
            this.proximoIdPedido = (Long) dados[1];
        } catch (FileNotFoundException e) {
            this.pedidos = new ArrayList<>();
            this.proximoIdPedido = 1L;
        } catch (Exception e) {
            System.err.println("Erro ao carregar pedidos: " + e.getMessage());
            this.pedidos = new ArrayList<>();
            this.proximoIdPedido = 1L;
        }
    }
    
    private void salvarPedidos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_PEDIDOS))) {
            Object[] dados = {pedidos, proximoIdPedido};
            oos.writeObject(dados);
        } catch (IOException e) {
            System.err.println("Erro ao salvar pedidos: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private void carregarClientes() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_CLIENTES))) {
            Object[] dados = (Object[]) ois.readObject();
            this.clientes = (List<Cliente>) dados[0];
            this.proximoIdCliente = (Long) dados[1];
        } catch (FileNotFoundException e) {
            this.clientes = new ArrayList<>();
            this.proximoIdCliente = 1L;
        } catch (Exception e) {
            System.err.println("Erro ao carregar clientes: " + e.getMessage());
            this.clientes = new ArrayList<>();
            this.proximoIdCliente = 1L;
        }
    }
    
    private void salvarClientes() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_CLIENTES))) {
            Object[] dados = {clientes, proximoIdCliente};
            oos.writeObject(dados);
        } catch (IOException e) {
            System.err.println("Erro ao salvar clientes: " + e.getMessage());
        }
    }
}