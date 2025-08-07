/*
 * Autores: Sara Ingrid - 202365056A, Angélica Coutinho - 202365064A
 */
package br.ufjf.dcc025.trabalho.franquias.service;

import br.ufjf.dcc025.trabalho.franquias.model.produto.Produto;
import br.ufjf.dcc025.trabalho.franquias.exceptions.ValidacaoException;
import br.ufjf.dcc025.trabalho.franquias.exceptions.EntidadeNaoEncontradaException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProdutoService implements OperacoesCRUD<Produto, Long> {
    private static final String ARQUIVO_PRODUTOS = "produtos.dat";
    
    private List<Produto> produtos;
    private Long proximoId = 1L;
    
    public ProdutoService() {
        this.produtos = new ArrayList<>();
        carregarProdutos();
    }
    
    @Override
    public Produto cadastrar(Produto produto) throws ValidacaoException {
        validarProduto(produto);
        
        if (existeNome(produto.getNome())) {
            throw new ValidacaoException("Já existe um produto com este nome");
        }
        
        produto.setId(proximoId++);
        produtos.add(produto);
        salvarProdutos();
        return produto;
    }
    
    @Override
    public Produto atualizar(Produto produto) throws ValidacaoException, EntidadeNaoEncontradaException {
        Produto produtoExistente = buscarPorId(produto.getId());
        if (produtoExistente == null) {
            throw new EntidadeNaoEncontradaException("Produto com ID " + produto.getId() + " não encontrado");
        }
        
        validarProduto(produto);
        
        if (existeNomeParaOutroProduto(produto.getNome(), produto.getId())) {
            throw new ValidacaoException("Já existe um produto com este nome");
        }
        
        produtoExistente.setNome(produto.getNome());
        produtoExistente.setDescricao(produto.getDescricao());
        produtoExistente.setPreco(produto.getPreco());
        produtoExistente.setEstoque(produto.getEstoque());
        
        salvarProdutos();
        return produtoExistente;
    }
    
    @Override
    public boolean remover(Long id) throws EntidadeNaoEncontradaException {
        Produto produto = buscarPorId(id);
        if (produto == null) {
            throw new EntidadeNaoEncontradaException("Produto com ID " + id + " não encontrado");
        }
        
        produtos.remove(produto);
        salvarProdutos();
        return true;
    }
    
    @Override
    public Produto buscarPorId(Long id) {
        return produtos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public List<Produto> listar() {
        return new ArrayList<>(produtos);
    }
    
    public List<Produto> buscarPorNome(String nome) {
        return produtos.stream()
                .filter(p -> p.getNome().toLowerCase().contains(nome.toLowerCase()))
                .collect(Collectors.toList());
    }
        
    public List<Produto> buscarComEstoqueBaixo(int limiteMinimo) {
        return produtos.stream()
                .filter(p -> p.getEstoque() <= limiteMinimo)
                .collect(Collectors.toList());
    }
    
    public List<Produto> buscarPorFaixaPreco(double precoMin, double precoMax) {
        return produtos.stream()
                .filter(p -> p.getPreco() >= precoMin && p.getPreco() <= precoMax)
                .collect(Collectors.toList());
    }
    
    public boolean atualizarEstoque(Long produtoId, int novaQuantidade) throws EntidadeNaoEncontradaException, ValidacaoException {
        Produto produto = buscarPorId(produtoId);
        if (produto == null) {
            throw new EntidadeNaoEncontradaException("Produto com ID " + produtoId + " não encontrado");
        }
        
        if (novaQuantidade < 0) {
            throw new ValidacaoException("Quantidade em estoque não pode ser negativa");
        }
        
        produto.setEstoque(novaQuantidade);
        salvarProdutos();
        return true;
    }
    
    public boolean retirarEstoque(Long produtoId, int quantidade) throws EntidadeNaoEncontradaException, ValidacaoException {
        Produto produto = buscarPorId(produtoId);
        if (produto == null) {
            throw new EntidadeNaoEncontradaException("Produto com ID " + produtoId + " não encontrado");
        }
        
        if (quantidade <= 0) {
            throw new ValidacaoException("Quantidade a retirar deve ser positiva");
        }
        
        if (produto.getEstoque() < quantidade) {
            throw new ValidacaoException("Estoque insuficiente. Disponível: " + produto.getEstoque());
        }
        
        produto.setEstoque(produto.getEstoque() - quantidade);
        salvarProdutos();
        return true;
    }
    
    public boolean adicionarEstoque(Long produtoId, int quantidade) throws EntidadeNaoEncontradaException, ValidacaoException {
        Produto produto = buscarPorId(produtoId);
        if (produto == null) {
            throw new EntidadeNaoEncontradaException("Produto com ID " + produtoId + " não encontrado");
        }
        
        if (quantidade <= 0) {
            throw new ValidacaoException("Quantidade a adicionar deve ser positiva");
        }
        
        produto.setEstoque(produto.getEstoque() + quantidade);
        salvarProdutos();
        return true;
    }
    
    public boolean verificarDisponibilidade(Long produtoId, int quantidadeDesejada) throws EntidadeNaoEncontradaException {
        Produto produto = buscarPorId(produtoId);
        if (produto == null) {
            throw new EntidadeNaoEncontradaException("Produto com ID " + produtoId + " não encontrado");
        }
        
        return produto.isDisponivel(quantidadeDesejada) && produto.getEstoque() >= quantidadeDesejada;
    }
    
    public double getValorTotalEstoque() {
        return produtos.stream()
                .mapToDouble(p -> p.getPreco() * p.getEstoque())
                .sum();
    }
    
    public int getTotalItensEstoque() {
        return produtos.stream()
                .mapToInt(Produto::getEstoque)
                .sum();
    }
    
    private void validarProduto(Produto produto) throws ValidacaoException {
        if (produto == null) {
            throw new ValidacaoException("Produto não pode ser nulo");
        }
        
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new ValidacaoException("Nome do produto é obrigatório");
        }
        
        if (produto.getPreco() < 0) {
            throw new ValidacaoException("Preço do produto não pode ser negativo");
        }
        
        if (produto.getEstoque() < 0) {
            throw new ValidacaoException("Quantidade em estoque não pode ser negativa");
        }
    }
    
    private boolean existeNome(String nome) {
        return produtos.stream().anyMatch(p -> p.getNome().equalsIgnoreCase(nome.trim()));
    }
    
    private boolean existeNomeParaOutroProduto(String nome, Long id) {
        return produtos.stream().anyMatch(p -> p.getNome().equalsIgnoreCase(nome.trim()) && !p.getId().equals(id));
    }
    
    @SuppressWarnings("unchecked")
    private void carregarProdutos() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_PRODUTOS))) {
            produtos = (List<Produto>) ois.readObject();
            proximoId = (Long) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            criarProdutosPadrao();
        }
    }
    
    private void salvarProdutos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_PRODUTOS))) {
            oos.writeObject(produtos);
            oos.writeObject(proximoId);
        } catch (IOException e) {
            System.err.println("Erro ao salvar produtos: " + e.getMessage());
        }
    }
    
    private void criarProdutosPadrao() {
        try {
            cadastrar(new Produto("Hambúrguer Clássico", "Hambúrguer com carne, alface, tomate e queijo", 15.90, 50, 10, "Hambúrgueres"));
            cadastrar(new Produto("Batata Frita", "Porção de batata frita crocante", 8.50, 30, 5, "Acompanhamentos"));
            cadastrar(new Produto("Refrigerante Lata", "Refrigerante gelado 350ml", 4.50, 100, 20, "Bebidas"));
            cadastrar(new Produto("Pizza Margherita", "Pizza com molho de tomate, mussarela e manjericão", 22.90, 20, 3, "Pizzas"));
            cadastrar(new Produto("Sorvete Chocolate", "Sorvete cremoso sabor chocolate", 6.90, 25, 5, "Sobremesas"));
        } catch (ValidacaoException e) {
            System.err.println("Erro ao criar produtos padrão: " + e.getMessage());
        }
    }
}