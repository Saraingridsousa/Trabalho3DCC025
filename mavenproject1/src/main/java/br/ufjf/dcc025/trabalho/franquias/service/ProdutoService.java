/*
 * Autores: Sara Ingrid - 202365056A, Angélica Coutinho - 202365064A
 */
package br.ufjf.dcc025.trabalho.franquias.service;

import br.ufjf.dcc025.trabalho.franquias.exceptions.EntidadeNaoEncontradaException;
import br.ufjf.dcc025.trabalho.franquias.exceptions.ValidacaoException;
import br.ufjf.dcc025.trabalho.franquias.model.produto.Produto;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ProdutoService {
    private static final String ARQUIVO_PRODUTOS = "produtos.dat";
    
    private List<Produto> produtos;
    private Long proximoId = 1L;
    
    public ProdutoService() {
        this.produtos = new ArrayList<>();
        carregarProdutos();
    }
    
    // ========== MÉTODOS CRUD ==========
    
    public Produto cadastrarProduto(String nome, String descricao, double preco, 
                                  int quantidadeEstoque, int estoqueMinimo, String categoria) 
            throws ValidacaoException {
        
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidacaoException("Nome do produto é obrigatório");
        }
        
        if (preco <= 0) {
            throw new ValidacaoException("Preço deve ser maior que zero");
        }
        
        if (quantidadeEstoque < 0) {
            throw new ValidacaoException("Quantidade em estoque não pode ser negativa");
        }
        
        if (estoqueMinimo < 0) {
            throw new ValidacaoException("Estoque mínimo não pode ser negativo");
        }
        
        boolean existe = produtos.stream()
                .anyMatch(p -> p.getNome().trim().equalsIgnoreCase(nome.trim()));
        
        if (existe) {
            throw new ValidacaoException("Já existe um produto com este nome");
        }
        
        Produto produto = new Produto(nome.trim(), descricao != null ? descricao.trim() : "", 
                                    preco, quantidadeEstoque, estoqueMinimo, 
                                    categoria != null ? categoria.trim() : "Geral");
        produto.setId(proximoId++);
        
        produtos.add(produto);
        salvarProdutos();
        
        return produto;
    }
    
    public boolean editarProduto(Long id, String nome, String descricao, double preco, 
                               int quantidadeEstoque, int estoqueMinimo, String categoria) 
            throws ValidacaoException {
        
        Produto produto = buscarProdutoPorId(id);
        if (produto == null) {
            throw new ValidacaoException("Produto não encontrado");
        }
        
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidacaoException("Nome do produto é obrigatório");
        }
        
        if (preco <= 0) {
            throw new ValidacaoException("Preço deve ser maior que zero");
        }
        
        if (quantidadeEstoque < 0) {
            throw new ValidacaoException("Quantidade em estoque não pode ser negativa");
        }
        
        boolean existe = produtos.stream()
                .anyMatch(p -> !p.getId().equals(id) && p.getNome().trim().equalsIgnoreCase(nome.trim()));
        
        if (existe) {
            throw new ValidacaoException("Já existe um produto com este nome");
        }
        
        produto.setNome(nome.trim());
        produto.setDescricao(descricao != null ? descricao.trim() : "");
        produto.setPreco(preco);
        produto.setEstoque(quantidadeEstoque);
        produto.setEstoqueMinimo(estoqueMinimo);
        produto.setCategoria(categoria != null ? categoria.trim() : "Geral");
        
        salvarProdutos();
        return true;
    }
    
    public boolean removerProduto(Long id) throws EntidadeNaoEncontradaException {
        Produto produto = buscarProdutoPorId(id);
        if (produto == null) {
            throw new EntidadeNaoEncontradaException("Produto não encontrado");
        }
        
        produtos.remove(produto);
        salvarProdutos();
        return true;
    }
    
    public Produto buscarProdutoPorId(Long id) {
        return produtos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    public List<Produto> listarProdutos() {
        return new ArrayList<>(produtos);
    }
    
    public List<Produto> listarProdutosPorCategoria(String categoria) {
        return produtos.stream()
                .filter(p -> p.getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
    }
    
    public List<Produto> listarProdutosEstoqueBaixo() {
        return produtos.stream()
                .filter(p -> p.getEstoque() <= p.getEstoqueMinimo())
                .collect(Collectors.toList());
    }
    
    public boolean atualizarEstoque(Long id, int novaQuantidade) 
            throws ValidacaoException {
        
        Produto produto = buscarProdutoPorId(id);
        if (produto == null) {
            throw new ValidacaoException("Produto não encontrado");
        }
        
        if (novaQuantidade < 0) {
            throw new ValidacaoException("Quantidade não pode ser negativa");
        }
        
        produto.setEstoque(novaQuantidade);
        salvarProdutos();
        return true;
    }
    
    // ========== MÉTODOS DE PERSISTÊNCIA ==========
    
    private void carregarProdutos() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_PRODUTOS))) {
            @SuppressWarnings("unchecked")
            List<Produto> produtosCarregados = (List<Produto>) ois.readObject();
            this.produtos = produtosCarregados;
            
            proximoId = produtos.stream()
                    .mapToLong(Produto::getId)
                    .max()
                    .orElse(0L) + 1;
                    
        } catch (FileNotFoundException e) {
            criarProdutosPadrao();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar produtos: " + e.getMessage());
            criarProdutosPadrao();
        }
    }
    
    private void salvarProdutos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_PRODUTOS))) {
            oos.writeObject(produtos);
        } catch (IOException e) {
            System.err.println("Erro ao salvar produtos: " + e.getMessage());
        }
    }
    
    private void criarProdutosPadrao() {
        if (produtos.isEmpty()) {
            try {
                cadastrarProduto("Hambúrguer Clássico", "Hambúrguer com carne, alface, tomate e queijo", 15.90, 50, 10, "Hambúrgueres");
                cadastrarProduto("Batata Frita", "Porção de batata frita crocante", 8.50, 30, 5, "Acompanhamentos");
                cadastrarProduto("Refrigerante Lata", "Refrigerante gelado 350ml", 4.50, 100, 20, "Bebidas");
                cadastrarProduto("Pizza Margherita", "Pizza com molho de tomate, mussarela e manjericão", 22.90, 20, 3, "Pizzas");
                cadastrarProduto("Sorvete Chocolate", "Sorvete cremoso sabor chocolate", 6.90, 25, 5, "Sobremesas");
            } catch (Exception e) {
                System.err.println("Erro ao criar produtos padrão: " + e.getMessage());
            }
        }
    }
}