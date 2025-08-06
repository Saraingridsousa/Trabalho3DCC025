package br.ufjf.dcc025.trabalho.franquias.view;

import br.ufjf.dcc025.trabalho.franquias.model.usuarios.Usuario;
import br.ufjf.dcc025.trabalho.franquias.model.usuarios.Dono;
import br.ufjf.dcc025.trabalho.franquias.model.usuarios.Gerente;
import br.ufjf.dcc025.trabalho.franquias.model.usuarios.Vendedor;
import br.ufjf.dcc025.trabalho.franquias.service.UsuarioService;
import br.ufjf.dcc025.trabalho.franquias.service.FranquiaService;
import br.ufjf.dcc025.trabalho.franquias.service.ProdutoService;
import br.ufjf.dcc025.trabalho.franquias.service.PedidoService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainInterface extends JFrame {
    private final Usuario usuarioLogado;
    private final UsuarioService usuarioService;
    private final FranquiaService franquiaService;
    private final ProdutoService produtoService;
    private final PedidoService pedidoService;
    
    private JLabel lblUsuarioLogado;
    private JPanel panelPrincipal;
    
    public MainInterface(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        this.usuarioService = new UsuarioService();
        this.franquiaService = new FranquiaService();
        this.produtoService = new ProdutoService();
        this.pedidoService = new PedidoService(produtoService, franquiaService);
        
        initComponents();
        configurarPermissoes();
    }
    
    private void initComponents() {
        setTitle("Sistema de Gestão de Franquias - " + usuarioLogado.getTipoUsuario());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        
        // Menu Bar
        criarMenuBar();
        
        // Panel principal
        panelPrincipal = new JPanel(new BorderLayout());
        
        // Header
        JPanel headerPanel = criarHeaderPanel();
        panelPrincipal.add(headerPanel, BorderLayout.NORTH);
        
        // Content area
        JPanel contentPanel = criarContentPanel();
        panelPrincipal.add(contentPanel, BorderLayout.CENTER);
        
        // Status bar
        JPanel statusPanel = criarStatusPanel();
        panelPrincipal.add(statusPanel, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    
    private void criarMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // Menu Arquivo
        JMenu menuArquivo = new JMenu("Arquivo");
        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(e -> sair());
        menuArquivo.add(itemSair);
        menuBar.add(menuArquivo);
        
        // Menu Cadastros
        JMenu menuCadastros = new JMenu("Cadastros");
        
        JMenuItem itemUsuarios = new JMenuItem("Usuários");
        itemUsuarios.addActionListener(e -> abrirGestaoUsuarios());
        menuCadastros.add(itemUsuarios);
        
        JMenuItem itemFranquias = new JMenuItem("Franquias");
        itemFranquias.addActionListener(e -> abrirGestaoFranquias());
        menuCadastros.add(itemFranquias);
        
        JMenuItem itemProdutos = new JMenuItem("Produtos");
        itemProdutos.addActionListener(e -> abrirGestaoProdutos());
        menuCadastros.add(itemProdutos);
        
        menuBar.add(menuCadastros);
        
        // Menu Vendas
        JMenu menuVendas = new JMenu("Vendas");
        
        JMenuItem itemNovoPedido = new JMenuItem("Novo Pedido");
        itemNovoPedido.addActionListener(e -> abrirNovoPedido());
        menuVendas.add(itemNovoPedido);
        
        JMenuItem itemPedidos = new JMenuItem("Gerenciar Pedidos");
        itemPedidos.addActionListener(e -> abrirGestaoPedidos());
        menuVendas.add(itemPedidos);
        
        menuBar.add(menuVendas);
        
        // Menu Relatórios
        JMenu menuRelatorios = new JMenu("Relatórios");
        
        JMenuItem itemVendas = new JMenuItem("Relatório de Vendas");
        itemVendas.addActionListener(e -> abrirRelatorioVendas());
        menuRelatorios.add(itemVendas);
        
        JMenuItem itemEstoque = new JMenuItem("Relatório de Estoque");
        itemEstoque.addActionListener(e -> abrirRelatorioEstoque());
        menuRelatorios.add(itemEstoque);
        
        menuBar.add(menuRelatorios);
        
        // Menu Ajuda
        JMenu menuAjuda = new JMenu("Ajuda");
        JMenuItem itemSobre = new JMenuItem("Sobre");
        itemSobre.addActionListener(e -> mostrarSobre());
        menuAjuda.add(itemSobre);
        menuBar.add(menuAjuda);
        
        setJMenuBar(menuBar);
    }
    
    private JPanel criarHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panel.setBackground(new Color(52, 152, 219));
        
        JLabel lblTitulo = new JLabel("Sistema de Gestão de Franquias");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        panel.add(lblTitulo, BorderLayout.WEST);
        
        lblUsuarioLogado = new JLabel("Usuário: " + usuarioLogado.getNome() + " (" + usuarioLogado.getTipoUsuario() + ")");
        lblUsuarioLogado.setFont(new Font("Arial", Font.PLAIN, 14));
        lblUsuarioLogado.setForeground(Color.WHITE);
        panel.add(lblUsuarioLogado, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel criarContentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        
        // Botões principais
        JButton btnNovoPedido = criarBotaoAcesso("Novo Pedido", "Criar um novo pedido de venda", 
            e -> abrirNovoPedido());
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(btnNovoPedido, gbc);
        
        JButton btnPedidos = criarBotaoAcesso("Gerenciar Pedidos", "Visualizar e gerenciar pedidos", 
            e -> abrirGestaoPedidos());
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(btnPedidos, gbc);
        
        JButton btnProdutos = criarBotaoAcesso("Produtos", "Gerenciar catálogo de produtos", 
            e -> abrirGestaoProdutos());
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(btnProdutos, gbc);
        
        JButton btnRelatorios = criarBotaoAcesso("Relatórios", "Visualizar relatórios e estatísticas", 
            e -> abrirRelatorioVendas());
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(btnRelatorios, gbc);
        
        // Botões administrativos (só para donos e gerentes)
        if (usuarioLogado instanceof Dono || usuarioLogado instanceof Gerente) {
            JButton btnFranquias = criarBotaoAcesso("Franquias", "Gerenciar franquias", 
                e -> abrirGestaoFranquias());
            gbc.gridx = 0; gbc.gridy = 2;
            panel.add(btnFranquias, gbc);
            
            JButton btnUsuarios = criarBotaoAcesso("Usuários", "Gerenciar usuários do sistema", 
                e -> abrirGestaoUsuarios());
            gbc.gridx = 1; gbc.gridy = 2;
            panel.add(btnUsuarios, gbc);
        }
        
        return panel;
    }
    
    private JButton criarBotaoAcesso(String titulo, String descricao, ActionListener action) {
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout());
        btn.setPreferredSize(new Dimension(200, 100));
        btn.setBorder(BorderFactory.createRaisedBevelBorder());
        
        JLabel lblTitulo = new JLabel(titulo, JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        btn.add(lblTitulo, BorderLayout.CENTER);
        
        JLabel lblDescricao = new JLabel("<html><center>" + descricao + "</center></html>", JLabel.CENTER);
        lblDescricao.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDescricao.setForeground(Color.GRAY);
        btn.add(lblDescricao, BorderLayout.SOUTH);
        
        btn.addActionListener(action);
        
        return btn;
    }
    
    private JPanel criarStatusPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createLoweredBevelBorder());
        
        JLabel lblStatus = new JLabel("Sistema pronto");
        panel.add(lblStatus);
        
        return panel;
    }
    
    private void configurarPermissoes() {
        // Configurar visibilidade e acessibilidade baseado no tipo de usuário
        // Implementação específica pode ser adicionada aqui
    }
    
    // Métodos de navegação
    private void abrirNovoPedido() {
        JOptionPane.showMessageDialog(this, "Funcionalidade em desenvolvimento: Novo Pedido");
    }
    
    private void abrirGestaoPedidos() {
        JOptionPane.showMessageDialog(this, "Funcionalidade em desenvolvimento: Gestão de Pedidos");
    }
    
    private void abrirGestaoProdutos() {
        JOptionPane.showMessageDialog(this, "Funcionalidade em desenvolvimento: Gestão de Produtos");
    }
    
    private void abrirGestaoFranquias() {
        if (usuarioLogado instanceof Vendedor) {
            JOptionPane.showMessageDialog(this, "Acesso negado: Vendedores não podem gerenciar franquias");
            return;
        }
        JOptionPane.showMessageDialog(this, "Funcionalidade em desenvolvimento: Gestão de Franquias");
    }
    
    private void abrirGestaoUsuarios() {
        if (usuarioLogado instanceof Vendedor) {
            JOptionPane.showMessageDialog(this, "Acesso negado: Vendedores não podem gerenciar usuários");
            return;
        }
        JOptionPane.showMessageDialog(this, "Funcionalidade em desenvolvimento: Gestão de Usuários");
    }
    
    private void abrirRelatorioVendas() {
        JOptionPane.showMessageDialog(this, "Funcionalidade em desenvolvimento: Relatório de Vendas");
    }
    
    private void abrirRelatorioEstoque() {
        JOptionPane.showMessageDialog(this, "Funcionalidade em desenvolvimento: Relatório de Estoque");
    }
    
    private void mostrarSobre() {
        String sobre = "Sistema de Gestão de Franquias\n" +
                      "Versão 1.0\n\n" +
                      "Desenvolvido por:\n" +
                      "Sara Ingrid\n" +
                      "Angélica\n\n" +
                      "Universidade Federal de Juiz de Fora\n" +
                      "DCC025 - Programação Orientada a Objetos";
        
        JOptionPane.showMessageDialog(this, sobre, "Sobre", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void sair() {
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente sair do sistema?", 
            "Confirmação", 
            JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Para teste, criar um usuário fictício
            Usuario usuarioTeste = new Dono("Admin", "admin@franquia.com", "admin123");
            usuarioTeste.setId(1L);
            
            new MainInterface(usuarioTeste).setVisible(true);
        });
    }
}