package br.ufjf.dcc025.trabalho.franquias.view;

import br.ufjf.dcc025.trabalho.franquias.model.produto.Produto;
import br.ufjf.dcc025.trabalho.franquias.service.ProdutoService;
import br.ufjf.dcc025.trabalho.franquias.exceptions.ValidacaoException;
import br.ufjf.dcc025.trabalho.franquias.exceptions.EntidadeNaoEncontradaException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GestaoProduto extends JFrame {
    private final ProdutoService produtoService;
    private JTable tableProdutos;
    private DefaultTableModel tableModel;
    private JTextField txtNome, txtDescricao, txtPreco, txtEstoque;
    private JButton btnSalvar, btnEditar, btnExcluir, btnLimpar;
    private Long produtoEditandoId = null;
    
    public GestaoProduto() {
        this.produtoService = new ProdutoService();
        initComponents();
        carregarProdutos();
    }
    
    private void initComponents() {
        setTitle("Gestão de Produtos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Panel de formulário
        JPanel formPanel = criarFormPanel();
        mainPanel.add(formPanel, BorderLayout.NORTH);
        
        // Panel da tabela
        JPanel tablePanel = criarTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel criarFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Cadastro de Produtos"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nome
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nome:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNome = new JTextField(20);
        panel.add(txtNome, gbc);
        
        // Descrição
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Descrição:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtDescricao = new JTextField(20);
        panel.add(txtDescricao, gbc);
        
        // Preço
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Preço:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtPreco = new JTextField(20);
        panel.add(txtPreco, gbc);
        
        // Estoque
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Estoque:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtEstoque = new JTextField(20);
        panel.add(txtEstoque, gbc);
        
        // Botões
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvarProduto());
        buttonPanel.add(btnSalvar);
        
        btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> editarProdutoSelecionado());
        btnEditar.setEnabled(false);
        buttonPanel.add(btnEditar);
        
        btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(e -> excluirProdutoSelecionado());
        btnExcluir.setEnabled(false);
        buttonPanel.add(btnExcluir);
        
        btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(e -> limparFormulario());
        buttonPanel.add(btnLimpar);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    private JPanel criarTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Lista de Produtos"));
        
        // Modelo da tabela
        String[] colunas = {"ID", "Nome", "Descrição", "Preço", "Estoque", "Disponível"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableProdutos = new JTable(tableModel);
        tableProdutos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableProdutos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableProdutos.getSelectedRow();
                btnEditar.setEnabled(selectedRow >= 0);
                btnExcluir.setEnabled(selectedRow >= 0);
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableProdutos);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de filtros
        JPanel filterPanel = new JPanel(new FlowLayout());
        JTextField txtFiltro = new JTextField(15);
        JButton btnFiltrar = new JButton("Filtrar por Nome");
        JButton btnTodos = new JButton("Mostrar Todos");
        JButton btnDisponiveis = new JButton("Só Disponíveis");
        
        btnFiltrar.addActionListener(e -> {
            String filtro = txtFiltro.getText().trim();
            if (!filtro.isEmpty()) {
                List<Produto> produtosFiltrados = produtoService.buscarPorNome(filtro);
                atualizarTabela(produtosFiltrados);
            }
        });
        
        btnTodos.addActionListener(e -> carregarProdutos());
        
        btnDisponiveis.addActionListener(e -> {
            List<Produto> produtosDisponiveis = produtoService.buscarDisponiveis();
            atualizarTabela(produtosDisponiveis);
        });
        
        filterPanel.add(new JLabel("Filtro:"));
        filterPanel.add(txtFiltro);
        filterPanel.add(btnFiltrar);
        filterPanel.add(btnTodos);
        filterPanel.add(btnDisponiveis);
        
        panel.add(filterPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void salvarProduto() {
        try {
            String nome = txtNome.getText().trim();
            String descricao = txtDescricao.getText().trim();
            String precoStr = txtPreco.getText().trim();
            String estoqueStr = txtEstoque.getText().trim();
            
            if (nome.isEmpty() || precoStr.isEmpty() || estoqueStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios");
                return;
            }
            
            double preco = Double.parseDouble(precoStr);
            int estoque = Integer.parseInt(estoqueStr);
            
            Produto produto = new Produto(nome, descricao, preco, estoque);
            
            if (produtoEditandoId == null) {
                // Criando novo produto
                produtoService.cadastrar(produto);
                JOptionPane.showMessageDialog(this, "Produto criado com sucesso!");
            } else {
                // Editando produto existente
                produtoService.atualizar(produtoEditandoId, produto);
                JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
                produtoEditandoId = null;
                btnSalvar.setText("Salvar");
            }
            
            limparFormulario();
            carregarProdutos();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Preço e estoque devem ser números válidos");
        } catch (ValidacaoException e) {
            JOptionPane.showMessageDialog(this, "Erro de validação: " + e.getMessage());
        } catch (EntidadeNaoEncontradaException e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }
    
    private void editarProdutoSelecionado() {
        int selectedRow = tableProdutos.getSelectedRow();
        if (selectedRow >= 0) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            Produto produto = produtoService.buscarPorId(id);
            
            if (produto != null) {
                txtNome.setText(produto.getNome());
                txtDescricao.setText(produto.getDescricao());
                txtPreco.setText(String.valueOf(produto.getPreco()));
                txtEstoque.setText(String.valueOf(produto.getEstoque()));
                
                produtoEditandoId = id;
                btnSalvar.setText("Atualizar");
            }
        }
    }
    
    private void excluirProdutoSelecionado() {
        int selectedRow = tableProdutos.getSelectedRow();
        if (selectedRow >= 0) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            String nome = (String) tableModel.getValueAt(selectedRow, 1);
            
            int opcao = JOptionPane.showConfirmDialog(this,
                "Deseja realmente excluir o produto '" + nome + "'?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION);
            
            if (opcao == JOptionPane.YES_OPTION) {
                try {
                    produtoService.remover(id);
                    JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!");
                    carregarProdutos();
                } catch (EntidadeNaoEncontradaException e) {
                    JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
                }
            }
        }
    }
    
    private void limparFormulario() {
        txtNome.setText("");
        txtDescricao.setText("");
        txtPreco.setText("");
        txtEstoque.setText("");
        produtoEditandoId = null;
        btnSalvar.setText("Salvar");
    }
    
    private void carregarProdutos() {
        List<Produto> produtos = produtoService.listar();
        atualizarTabela(produtos);
    }
    
    private void atualizarTabela(List<Produto> produtos) {
        tableModel.setRowCount(0);
        
        for (Produto produto : produtos) {
            Object[] row = {
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                String.format("R$ %.2f", produto.getPreco()),
                produto.getEstoque(),
                produto.isDisponivel() ? "Sim" : "Não"
            };
            tableModel.addRow(row);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            new GestaoProduto().setVisible(true);
        });
    }
}