package br.ufjf.dcc025.trabalho.franquias.view;

import br.ufjf.dcc025.trabalho.franquias.model.produto.Produto;
import br.ufjf.dcc025.trabalho.franquias.model.pedido.Pedido;
import br.ufjf.dcc025.trabalho.franquias.model.franquia.Franquia;
import br.ufjf.dcc025.trabalho.franquias.service.ProdutoService;
import br.ufjf.dcc025.trabalho.franquias.service.PedidoService;
import br.ufjf.dcc025.trabalho.franquias.service.FranquiaService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.time.format.DateTimeFormatter;

public class RelatoriosInterface extends JFrame {
    private final ProdutoService produtoService;
    private final PedidoService pedidoService;
    private final FranquiaService franquiaService;
    
    private JTabbedPane tabbedPane;
    private JTable tableVendas;
    private JTable tableEstoque;
    private JTable tableFranquias;
    private DefaultTableModel modelVendas;
    private DefaultTableModel modelEstoque;
    private DefaultTableModel modelFranquias;
    
    private JLabel lblReceitaTotal;
    private JLabel lblPedidosEntregues;
    private JLabel lblProdutosCadastrados;
    private JLabel lblFranquiasAtivas;
    private JLabel lblEstoqueBaixo;
    
    public RelatoriosInterface() {
        this.produtoService = new ProdutoService();
        this.franquiaService = new FranquiaService();
        this.pedidoService = new PedidoService(produtoService, franquiaService);
        
        initComponents();
        carregarDados();
    }
    
    private void initComponents() {
        setTitle("Relatórios e Dashboard");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Dashboard no topo
        JPanel dashboardPanel = criarDashboard();
        mainPanel.add(dashboardPanel, BorderLayout.NORTH);
        
        // Abas de relatórios
        tabbedPane = new JTabbedPane();
        
        // Aba de Vendas
        JPanel vendaPanel = criarAbaVendas();
        tabbedPane.addTab("Relatório de Vendas", vendaPanel);
        
        // Aba de Estoque
        JPanel estoquePanel = criarAbaEstoque();
        tabbedPane.addTab("Relatório de Estoque", estoquePanel);
        
        // Aba de Franquias
        JPanel franquiaPanel = criarAbaFranquias();
        tabbedPane.addTab("Relatório de Franquias", franquiaPanel);
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel criarDashboard() {
        JPanel panel = new JPanel(new GridLayout(1, 5, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Dashboard"));
        panel.setPreferredSize(new Dimension(0, 100));
        
        // Card Receita Total
        JPanel cardReceita = criarCard("Receita Total", "R$ 0,00", Color.GREEN);
        lblReceitaTotal = (JLabel) ((JPanel) cardReceita.getComponent(1)).getComponent(0);
        panel.add(cardReceita);
        
        // Card Pedidos Entregues
        JPanel cardPedidos = criarCard("Pedidos Entregues", "0", Color.BLUE);
        lblPedidosEntregues = (JLabel) ((JPanel) cardPedidos.getComponent(1)).getComponent(0);
        panel.add(cardPedidos);
        
        // Card Produtos Cadastrados
        JPanel cardProdutos = criarCard("Produtos", "0", Color.ORANGE);
        lblProdutosCadastrados = (JLabel) ((JPanel) cardProdutos.getComponent(1)).getComponent(0);
        panel.add(cardProdutos);
        
        // Card Franquias Ativas
        JPanel cardFranquias = criarCard("Franquias", "0", Color.CYAN);
        lblFranquiasAtivas = (JLabel) ((JPanel) cardFranquias.getComponent(1)).getComponent(0);
        panel.add(cardFranquias);
        
        // Card Estoque Baixo
        JPanel cardEstoque = criarCard("Estoque Baixo", "0", Color.RED);
        lblEstoqueBaixo = (JLabel) ((JPanel) cardEstoque.getComponent(1)).getComponent(0);
        panel.add(cardEstoque);
        
        return panel;
    }
    
    private JPanel criarCard(String titulo, String valor, Color cor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(cor, 2));
        card.setBackground(Color.WHITE);
        
        JLabel lblTitulo = new JLabel(titulo, JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 12));
        lblTitulo.setForeground(cor);
        card.add(lblTitulo, BorderLayout.NORTH);
        
        JPanel valorPanel = new JPanel();
        JLabel lblValor = new JLabel(valor, JLabel.CENTER);
        lblValor.setFont(new Font("Arial", Font.BOLD, 18));
        valorPanel.add(lblValor);
        card.add(valorPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel criarAbaVendas() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Controles de filtro
        JPanel filterPanel = new JPanel(new FlowLayout());
        JButton btnTodosPedidos = new JButton("Todos os Pedidos");
        JButton btnPendentes = new JButton("Pendentes");
        JButton btnConfirmados = new JButton("Confirmados");
        JButton btnEntregues = new JButton("Entregues");
        JButton btnCancelados = new JButton("Cancelados");
        
        btnTodosPedidos.addActionListener(e -> carregarVendas(null));
        btnPendentes.addActionListener(e -> carregarVendas(Pedido.StatusPedido.PENDENTE));
        btnConfirmados.addActionListener(e -> carregarVendas(Pedido.StatusPedido.CONFIRMADO));
        btnEntregues.addActionListener(e -> carregarVendas(Pedido.StatusPedido.FINALIZADO));
        btnCancelados.addActionListener(e -> carregarVendas(Pedido.StatusPedido.CANCELADO));
        
        filterPanel.add(btnTodosPedidos);
        filterPanel.add(btnPendentes);
        filterPanel.add(btnConfirmados);
        filterPanel.add(btnEntregues);
        filterPanel.add(btnCancelados);
        
        panel.add(filterPanel, BorderLayout.NORTH);
        
        // Tabela de vendas
        String[] colunas = {"ID", "Data", "Cliente", "Vendedor", "Franquia", "Total", "Status"};
        modelVendas = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableVendas = new JTable(modelVendas);
        JScrollPane scrollVendas = new JScrollPane(tableVendas);
        panel.add(scrollVendas, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel criarAbaEstoque() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Controles de filtro
        JPanel filterPanel = new JPanel(new FlowLayout());
        JButton btnTodosProdutos = new JButton("Todos os Produtos");
        JButton btnDisponiveis = new JButton("Disponíveis");
        JButton btnEstoqueBaixo = new JButton("Estoque Baixo (≤10)");
        JButton btnSemEstoque = new JButton("Sem Estoque");
        
        btnTodosProdutos.addActionListener(e -> carregarEstoque("todos"));
        btnDisponiveis.addActionListener(e -> carregarEstoque("disponiveis"));
        btnEstoqueBaixo.addActionListener(e -> carregarEstoque("baixo"));
        btnSemEstoque.addActionListener(e -> carregarEstoque("sem"));
        
        filterPanel.add(btnTodosProdutos);
        filterPanel.add(btnDisponiveis);
        filterPanel.add(btnEstoqueBaixo);
        filterPanel.add(btnSemEstoque);
        
        panel.add(filterPanel, BorderLayout.NORTH);
        
        // Tabela de estoque
        String[] colunas = {"ID", "Produto", "Preço", "Estoque", "Valor Total", "Status"};
        modelEstoque = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableEstoque = new JTable(modelEstoque);
        JScrollPane scrollEstoque = new JScrollPane(tableEstoque);
        panel.add(scrollEstoque, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel criarAbaFranquias() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Controles
        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton btnAtualizar = new JButton("Atualizar Dados");
        btnAtualizar.addActionListener(e -> carregarFranquias());
        controlPanel.add(btnAtualizar);
        
        panel.add(controlPanel, BorderLayout.NORTH);
        
        // Tabela de franquias
        String[] colunas = {"ID", "Nome", "Cidade", "Gerente", "Receita Total", "Total Tickets", "Ticket Médio"};
        modelFranquias = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableFranquias = new JTable(modelFranquias);
        JScrollPane scrollFranquias = new JScrollPane(tableFranquias);
        panel.add(scrollFranquias, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void carregarDados() {
        atualizarDashboard();
        carregarVendas(null);
        carregarEstoque("todos");
        carregarFranquias();
    }
    
    private void atualizarDashboard() {
        // Receita total
        double receitaTotal = pedidoService.getReceitaTotal();
        lblReceitaTotal.setText(String.format("R$ %.2f", receitaTotal));
        
        // Pedidos entregues
        long pedidosEntregues = pedidoService.getQuantidadePedidosEntregues();
        lblPedidosEntregues.setText(String.valueOf(pedidosEntregues));
        
        // Produtos cadastrados
        int totalProdutos = produtoService.listar().size();
        lblProdutosCadastrados.setText(String.valueOf(totalProdutos));
        
        // Franquias ativas
        int totalFranquias = franquiaService.listar().size();
        lblFranquiasAtivas.setText(String.valueOf(totalFranquias));
        
        // Produtos com estoque baixo
        List<Produto> estoqueBaixo = produtoService.buscarComEstoqueBaixo(10);
        lblEstoqueBaixo.setText(String.valueOf(estoqueBaixo.size()));
    }
    
    private void carregarVendas(Pedido.StatusPedido status) {
        modelVendas.setRowCount(0);
        
        List<Pedido> pedidos;
        if (status == null) {
            pedidos = pedidoService.listar();
        } else {
            pedidos = pedidoService.buscarPorStatus(status);
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        for (Pedido pedido : pedidos) {
            Object[] row = {
                pedido.getId(),
                pedido.getDataCriacao().format(formatter),
                pedido.getCliente().getNome(),
                pedido.getVendedor().getNome(),
                pedido.getFranquia().getNome(),
                String.format("R$ %.2f", pedido.getTotal()),
                pedido.getStatus().toString()
            };
            modelVendas.addRow(row);
        }
    }
    
    private void carregarEstoque(String filtro) {
        modelEstoque.setRowCount(0);
        
        List<Produto> produtos;
        produtos = switch (filtro) {
            case "disponiveis" -> produtoService.buscarDisponiveis();
            case "baixo" -> produtoService.buscarComEstoqueBaixo(10);
            case "sem" -> produtoService.buscarComEstoqueBaixo(0);
            default -> produtoService.listar();
        };
        
        for (Produto produto : produtos) {
            double valorTotal = produto.getPreco() * produto.getEstoque();
            String status = produto.getEstoque() == 0 ? "SEM ESTOQUE" :
                           produto.getEstoque() <= 10 ? "ESTOQUE BAIXO" : "OK";
            
            Object[] row = {
                produto.getId(),
                produto.getNome(),
                String.format("R$ %.2f", produto.getPreco()),
                produto.getEstoque(),
                String.format("R$ %.2f", valorTotal),
                status
            };
            modelEstoque.addRow(row);
        }
    }
    
    private void carregarFranquias() {
        modelFranquias.setRowCount(0);
        
        List<Franquia> franquias = franquiaService.listar();
        
        for (Franquia franquia : franquias) {
            String nomeGerente = franquia.getGerente() != null ? franquia.getGerente().getNome() : "Sem gerente";
            
            Object[] row = {
                franquia.getId(),
                franquia.getNome(),
                franquia.getEndereco().getCidade(),
                nomeGerente,
                String.format("R$ %.2f", franquia.getReceitaAcumulada()),
                franquia.getTotalPedidos(),
                String.format("R$ %.2f", franquia.getTicketMedio())
            };
            modelFranquias.addRow(row);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            new RelatoriosInterface().setVisible(true);
        });
    }
}