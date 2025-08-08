/*
 * Autores: Sara Ingrid - 202365056A, Angélica Coutinho - 202365064A
 */
package br.ufjf.dcc025.trabalho.franquias.view;

import br.ufjf.dcc025.trabalho.franquias.exceptions.*;
import br.ufjf.dcc025.trabalho.franquias.model.franquia.*;
import br.ufjf.dcc025.trabalho.franquias.model.pedido.*;
import br.ufjf.dcc025.trabalho.franquias.model.produto.*;
import br.ufjf.dcc025.trabalho.franquias.model.usuarios.*;
import br.ufjf.dcc025.trabalho.franquias.service.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class MainInterface extends javax.swing.JFrame {
    
    private final Usuario usuarioLogado;
    private final UsuarioService usuarioService;
    private final FranquiaService franquiaService;
    private final ProdutoService produtoService;
    private JPanel contentPanel;
    private JLabel userInfoLabel;

    public MainInterface(Usuario usuario, UsuarioService usuarioService) {
        this.usuarioLogado = usuario;
        this.usuarioService = usuarioService;
        this.franquiaService = new FranquiaService();
        this.produtoService = new ProdutoService();
        
        initComponents();
        setupMenuBasedOnUserType();
        updateUserInfo();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema de Gerenciamento de Franquias");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        userInfoLabel = new JLabel();
        userInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(userInfoLabel, BorderLayout.WEST);
        
        JButton logoutButton = new JButton("Sair");
        logoutButton.addActionListener(e -> logout());
        topPanel.add(logoutButton, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);
        
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        showWelcomePanel();
        
        add(contentPanel, BorderLayout.CENTER);
        
        pack();
    }
    
    private void setupMenuBasedOnUserType() {
        JMenuBar menuBar = new JMenuBar();
        
        if (usuarioLogado instanceof Dono) {
            setupDonoMenu(menuBar);
        } else if (usuarioLogado instanceof Gerente) {
            setupGerenteMenu(menuBar);
        } else if (usuarioLogado instanceof Vendedor) {
            setupVendedorMenu(menuBar);
        }
        
        setJMenuBar(menuBar);
    }
    
    private void setupDonoMenu(JMenuBar menuBar) {
        // Menu Franquias
        JMenu franquiasMenu = new JMenu("Franquias");
        franquiasMenu.add(createMenuItem("Cadastrar Franquia", e -> showCadastroFranquiaPanel()));
        franquiasMenu.add(createMenuItem("Listar Franquias", e -> showListaFranquiasPanel()));
        franquiasMenu.add(createMenuItem("Relatórios Consolidados", e -> showRelatoriosConsolidadosPanel()));
        menuBar.add(franquiasMenu);
        
        // Menu Gerentes
        JMenu gerentesMenu = new JMenu("Gerentes");
        gerentesMenu.add(createMenuItem("Cadastrar Gerente", e -> showCadastroGerentePanel()));
        gerentesMenu.add(createMenuItem("Listar Gerentes", e -> showListaGerentesPanel()));
        menuBar.add(gerentesMenu);
        
        // Menu Usuários
        JMenu usuariosMenu = new JMenu("Usuários");
        usuariosMenu.add(createMenuItem("Listar Todos", e -> showListaUsuariosPanel()));
        menuBar.add(usuariosMenu);
        
        // Menu Sistema
        JMenu sistemaMenu = new JMenu("Sistema");
        sistemaMenu.add(createMenuItem("Reset Dados", e -> resetarDadosSistema()));
        sistemaMenu.add(createMenuItem("Sobre", e -> mostrarSobre()));
        menuBar.add(sistemaMenu);
    }
    
    private void setupGerenteMenu(JMenuBar menuBar) {
        // Menu Vendedores
        JMenu vendedoresMenu = new JMenu("Vendedores");
        vendedoresMenu.add(createMenuItem("Cadastrar Vendedor", e -> showCadastroVendedorPanel()));
        vendedoresMenu.add(createMenuItem("Listar Vendedores", e -> showListaVendedoresPanel()));
        vendedoresMenu.add(createMenuItem("Ranking Vendedores", e -> showRankingVendedoresPanel()));
        menuBar.add(vendedoresMenu);
        
        // Menu Produtos
        JMenu produtosMenu = new JMenu("Produtos");
        produtosMenu.add(createMenuItem("Cadastrar Produto", e -> showCadastroProdutoPanel()));
        produtosMenu.add(createMenuItem("Listar Produtos", e -> showListaProdutosPanel()));
        produtosMenu.add(createMenuItem("Estoque Baixo", e -> showEstoqueBaixoPanel()));
        menuBar.add(produtosMenu);
        
        // Menu Pedidos
        JMenu pedidosMenu = new JMenu("Pedidos");
        pedidosMenu.add(createMenuItem("Listar Pedidos", e -> showListaPedidosPanel()));
        pedidosMenu.add(createMenuItem("Relatório Franquia", e -> showRelatorioFranquiaPanel()));
        menuBar.add(pedidosMenu);
    }
    
    private void setupVendedorMenu(JMenuBar menuBar) {
        // Menu Pedidos
        JMenu pedidosMenu = new JMenu("Pedidos");
        pedidosMenu.add(createMenuItem("Cadastrar Pedido", e -> showCadastroPedidoPanel()));
        pedidosMenu.add(createMenuItem("Meus Pedidos", e -> showMeusPedidosPanel()));
        menuBar.add(pedidosMenu);
    }
    
    private JMenuItem createMenuItem(String text, ActionListener action) {
        JMenuItem item = new JMenuItem(text);
        item.addActionListener(action);
        return item;
    }
    
    private void updateUserInfo() {
        String tipo = usuarioLogado.getTipoUsuario();
        String nome = usuarioLogado.getNome();
        userInfoLabel.setText(String.format("Usuário: %s (%s)", nome, tipo));
    }
    
    private void showWelcomePanel() {
        contentPanel.removeAll();
        
        JPanel welcomePanel = new JPanel(new BorderLayout());
        
        JLabel welcomeLabel = new JLabel("<html><h1>Bem-vindo ao Sistema de Gerenciamento de Franquias</h1></html>");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomePanel.add(welcomeLabel, BorderLayout.NORTH);
        
        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setOpaque(false);
        infoArea.setText(getWelcomeMessage());
        infoArea.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JScrollPane scrollPane = new JScrollPane(infoArea);
        welcomePanel.add(scrollPane, BorderLayout.CENTER);
        
        contentPanel.add(welcomePanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private String getWelcomeMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Sistema desenvolvido para gerenciar franquias com três tipos de usuários:\n\n");
        
        if (usuarioLogado instanceof Dono) {
            sb.append("FUNCIONALIDADES DO DONO:\n");
            sb.append("• Gerenciar franquias (cadastrar, remover, listar)\n");
            sb.append("• Gerenciar gerentes\n");
            sb.append("• Acompanhar desempenho das unidades\n");
            sb.append("• Consultar ranking de vendedores\n");
            sb.append("• Relatórios consolidados\n\n");
        } else if (usuarioLogado instanceof Gerente) {
            sb.append("FUNCIONALIDADES DO GERENTE:\n");
            sb.append("• Gerenciar equipe de vendas\n");
            sb.append("• Controlar pedidos da franquia\n");
            sb.append("• Administrar estoque\n");
            sb.append("• Acessar relatórios da unidade\n\n");
        } else if (usuarioLogado instanceof Vendedor) {
            sb.append("FUNCIONALIDADES DO VENDEDOR:\n");
            sb.append("• Cadastrar pedidos\n");
            sb.append("• Visualizar seus pedidos\n");
            sb.append("• Solicitar alterações de pedidos\n\n");
        }
        
        sb.append("Utilize o menu superior para navegar pelas funcionalidades disponíveis.");
        
        return sb.toString();
    }
    
    // ========== PAINÉIS DE FUNCIONALIDADES ==========
    
    private void showCadastroFranquiaPanel() {
        contentPanel.removeAll();
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Cadastro de Franquia"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        JTextField nomeField = new JTextField(20);
        JTextField enderecoField = new JTextField(20);
        JTextField cidadeField = new JTextField(15);
        JTextField estadoField = new JTextField(10);
        JTextField cepField = new JTextField(12);
        
        JComboBox<Gerente> gerenteCombo = new JComboBox<>();
        gerenteCombo.addItem(null);
        
        List<Gerente> gerentes = usuarioService.listarGerentesDisponiveis();
        System.out.println("DEBUG: Gerentes disponíveis encontrados: " + gerentes.size());
        
        if (gerentes.isEmpty()) {
            JLabel avisoLabel = new JLabel("<html><font color='red'>Nenhum gerente disponível!<br>Cadastre gerentes primeiro.</font></html>");
            gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
            panel.add(avisoLabel, gbc);
            gbc.gridwidth = 1;
        }
        
        for (Gerente g : gerentes) {
            gerenteCombo.addItem(g);
        }
        
        gerenteCombo.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(javax.swing.JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("Selecione um gerente");
                } else if (value instanceof Gerente) {
                    Gerente gerente = (Gerente) value;
                    setText(gerente.getNome() + " (" + gerente.getEmail() + ")");
                }
                return this;
            }
        });
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        panel.add(nomeField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1;
        panel.add(enderecoField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Cidade:"), gbc);
        gbc.gridx = 1;
        panel.add(cidadeField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        panel.add(estadoField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("CEP:"), gbc);
        gbc.gridx = 1;
        panel.add(cepField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Gerente:"), gbc);
        gbc.gridx = 1;
        panel.add(gerenteCombo, gbc);
        
        JButton cadastrarButton = new JButton("Cadastrar");
        cadastrarButton.addActionListener(e -> {
            try {
                String nome = nomeField.getText().trim();
                String endereco = enderecoField.getText().trim();
                String cidade = cidadeField.getText().trim();
                String estado = estadoField.getText().trim();
                String cep = cepField.getText().trim();
                Gerente gerente = (Gerente) gerenteCombo.getSelectedItem();
                
                if (nome.isEmpty() || endereco.isEmpty() || cidade.isEmpty() || estado.isEmpty() || cep.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                System.out.println("DEBUG: Gerente selecionado: " + (gerente != null ? gerente.getNome() : "null"));
                
                Franquia franquia = franquiaService.cadastrarFranquia(nome, endereco, cidade, estado, cep, gerente);
                
                String mensagem = "Franquia cadastrada com sucesso!\nID: " + franquia.getId();
                if (gerente != null) {
                    mensagem += "\nGerente: " + gerente.getNome();
                } else {
                    mensagem += "\nGerente: Não atribuído";
                }
                
                JOptionPane.showMessageDialog(this, mensagem);
                
                nomeField.setText("");
                enderecoField.setText("");
                cidadeField.setText("");
                estadoField.setText("");
                cepField.setText("");
                gerenteCombo.setSelectedIndex(0); 
                
            } catch (ValidacaoException ex) {
                JOptionPane.showMessageDialog(this, "Erro de validação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        
        gbc.gridx = 0; gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(cadastrarButton, gbc);
        
        contentPanel.add(panel, BorderLayout.NORTH);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showListaFranquiasPanel() {
        contentPanel.removeAll();
        
        String[] colunas = {"ID", "Nome", "Cidade", "Estado", "Gerente", "Vendedores", "Receita"};
        
        List<Franquia> franquias = franquiaService.listarFranquias();
        Object[][] dados = new Object[franquias.size()][7];
        
        for (int i = 0; i < franquias.size(); i++) {
            Franquia f = franquias.get(i);
            dados[i][0] = f.getId();
            dados[i][1] = f.getNome();
            dados[i][2] = f.getCidade();
            dados[i][3] = f.getEstado();
            dados[i][4] = f.getGerente() != null ? f.getGerente().getNome() : "Não atribuído";
            dados[i][5] = f.getVendedores().size();
            dados[i][6] = String.format("R$ %.2f", f.getReceitaAcumulada());
        }
        
        JTable table = new JTable(dados, colunas);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Franquias"));
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton editarGerenteBtn = new JButton("Editar Gerente");
        JButton detalhesBtn = new JButton("Ver Detalhes");
        JButton voltarBtn = new JButton("Voltar");
        
        editarGerenteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Long franquiaId = (Long) table.getValueAt(selectedRow, 0);
                String nomeFranquia = (String) table.getValueAt(selectedRow, 1);
                editarGerenteFranquia(franquiaId, nomeFranquia);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma franquia para editar o gerente", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        detalhesBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Long franquiaId = (Long) table.getValueAt(selectedRow, 0);
                mostrarDetalhesFranquia(franquiaId);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma franquia para ver detalhes", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        voltarBtn.addActionListener(e -> showWelcomePanel());
        
        buttonPanel.add(editarGerenteBtn);
        buttonPanel.add(detalhesBtn);
        buttonPanel.add(voltarBtn);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showEditarFranquiaPanel() {
        contentPanel.removeAll();
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createTitledBorder("Editar Franquias"));
        
        JPanel selectionPanel = new JPanel(new FlowLayout());
        selectionPanel.add(new JLabel("Selecione a franquia para editar:"));
        
        JComboBox<Franquia> franquiaCombo = new JComboBox<>();
        List<Franquia> franquias = franquiaService.listarFranquias();
        
        if (franquias.isEmpty()) {
            JLabel semFranquias = new JLabel("<html><center><h3>Nenhuma franquia cadastrada!</h3><p>Cadastre franquias primeiro.</p></center></html>");
            contentPanel.add(semFranquias, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
            return;
        }
        
        franquias.forEach(franquiaCombo::addItem);
        
        franquiaCombo.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(javax.swing.JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Franquia) {
                    Franquia franquia = (Franquia) value;
                    String gerenteNome = franquia.getGerente() != null ? franquia.getGerente().getNome() : "Sem gerente";
                    setText(franquia.getId() + " - " + franquia.getNome() + " (" + gerenteNome + ")");
                }
                return this;
            }
        });
        
        selectionPanel.add(franquiaCombo);
        
        JButton editarButton = new JButton("Editar Selecionada");
        selectionPanel.add(editarButton);
        
        mainPanel.add(selectionPanel, BorderLayout.NORTH);
        
        JPanel editPanel = new JPanel(new BorderLayout());
        JLabel instrucaoLabel = new JLabel("<html><center><h3>Selecione uma franquia acima e clique em 'Editar Selecionada'</h3></center></html>");
        editPanel.add(instrucaoLabel, BorderLayout.CENTER);
        
        mainPanel.add(editPanel, BorderLayout.CENTER);
        
        editarButton.addActionListener(e -> {
            Franquia franquiaSelecionada = (Franquia) franquiaCombo.getSelectedItem();
            if (franquiaSelecionada != null) {
                mostrarFormularioEdicaoFranquia(franquiaSelecionada, editPanel);
            }
        });
        
        contentPanel.add(mainPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void mostrarFormularioEdicaoFranquia(Franquia franquia, JPanel editPanel) {
        editPanel.removeAll();
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        JLabel titleLabel = new JLabel("Editando Franquia: " + franquia.getNome());
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);
        gbc.gridwidth = 1;
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("ID:"), gbc);
        JLabel idLabel = new JLabel(franquia.getId().toString());
        gbc.gridx = 1;
        formPanel.add(idLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Nome:"), gbc);
        JLabel nomeLabel = new JLabel(franquia.getNome());
        gbc.gridx = 1;
        formPanel.add(nomeLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Cidade/Estado:"), gbc);
        JLabel localLabel = new JLabel(franquia.getCidade() + "/" + franquia.getEstado());
        gbc.gridx = 1;
        formPanel.add(localLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Gerente Atual:"), gbc);
        JLabel gerenteAtualLabel = new JLabel(franquia.getGerente() != null ? franquia.getGerente().getNome() : "Nenhum");
        gbc.gridx = 1;
        formPanel.add(gerenteAtualLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Novo Gerente:"), gbc);
        
        JComboBox<Gerente> novoGerenteCombo = new JComboBox<>();
        novoGerenteCombo.addItem(null); 
        
        List<Gerente> gerentesDisponiveis = usuarioService.listarGerentesDisponiveis();
        gerentesDisponiveis.forEach(novoGerenteCombo::addItem);
        
        if (franquia.getGerente() != null) {
            novoGerenteCombo.addItem(franquia.getGerente());
        }
        
        novoGerenteCombo.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(javax.swing.JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("Nenhum gerente (remover atual)");
                } else if (value instanceof Gerente) {
                    Gerente gerente = (Gerente) value;
                    String status = gerente.equals(franquia.getGerente()) ? " (ATUAL)" : " (disponível)";
                    setText(gerente.getNome() + " (" + gerente.getEmail() + ")" + status);
                }
                return this;
            }
        });
        
        gbc.gridx = 1;
        formPanel.add(novoGerenteCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Vendedores:"), gbc);
        JLabel vendedoresLabel = new JLabel(String.valueOf(franquia.getVendedores().size()));
        gbc.gridx = 1;
        formPanel.add(vendedoresLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("Receita Acumulada:"), gbc);
        JLabel receitaLabel = new JLabel(String.format("R$ %.2f", franquia.getReceitaAcumulada()));
        gbc.gridx = 1;
        formPanel.add(receitaLabel, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton salvarBtn = new JButton("Salvar Alterações");
        JButton cancelarBtn = new JButton("Cancelar");
        
        salvarBtn.addActionListener(ev -> {
            try {
                Gerente novoGerente = (Gerente) novoGerenteCombo.getSelectedItem();
                Gerente gerenteAnterior = franquia.getGerente();
                
                franquia.setGerente(novoGerente);
                
                if (gerenteAnterior != null) {
                    gerenteAnterior.setFranquiaId(null);
                    usuarioService.atualizarFranquiaIdGerente(gerenteAnterior.getId(), null);
                }
                
                if (novoGerente != null) {
                    novoGerente.setFranquiaId(franquia.getId());
                    usuarioService.atualizarFranquiaIdGerente(novoGerente.getId(), franquia.getId());
                }
                
                franquiaService.atualizarFranquia(franquia);
                
                String mensagem = "Franquia atualizada com sucesso!\n\n";
                if (gerenteAnterior != null && novoGerente == null) {
                    mensagem += "Gerente removido: " + gerenteAnterior.getNome() + " agora está disponível.";
                } else if (gerenteAnterior == null && novoGerente != null) {
                    mensagem += "Gerente atribuído: " + novoGerente.getNome();
                } else if (gerenteAnterior != null && novoGerente != null && !gerenteAnterior.equals(novoGerente)) {
                    mensagem += "Gerente alterado:\n";
                    mensagem += "Anterior: " + gerenteAnterior.getNome() + " (agora disponível)\n";
                    mensagem += "Novo: " + novoGerente.getNome();
                } else if (gerenteAnterior != null && novoGerente != null && gerenteAnterior.equals(novoGerente)) {
                    mensagem += "Gerente mantido: " + novoGerente.getNome();
                } else {
                    mensagem += "Nenhuma alteração de gerente.";
                }
                
                JOptionPane.showMessageDialog(this, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                showEditarFranquiaPanel();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar alterações: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        
        cancelarBtn.addActionListener(ev -> showEditarFranquiaPanel());
        
        buttonPanel.add(salvarBtn);
        buttonPanel.add(cancelarBtn);
        
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);
        
        editPanel.add(formPanel, BorderLayout.CENTER);
        editPanel.revalidate();
        editPanel.repaint();
    }
    
    private void showCadastroGerentePanel() {
        contentPanel.removeAll();
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        JLabel titleLabel = new JLabel("Cadastro de Gerente");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Nome:"), gbc);
        JTextField nomeField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(nomeField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("CPF:"), gbc);
        JTextField cpfField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(cpfField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Email:"), gbc);
        JTextField emailField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Senha:"), gbc);
        JPasswordField senhaField = new JPasswordField(20);
        gbc.gridx = 1;
        formPanel.add(senhaField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Franquia:"), gbc);
        
        JComboBox<String> franquiaCombo = new JComboBox<>();
        franquiaCombo.addItem("Nenhuma franquia (disponível)");
        try {
            franquiaService.listarFranquias().forEach(f -> 
                franquiaCombo.addItem(f.getId() + " - " + f.getNome())
            );
        } catch (Exception e) {
        }
        gbc.gridx = 1;
        formPanel.add(franquiaCombo, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton salvarBtn = new JButton("Salvar");
        JButton cancelarBtn = new JButton("Cancelar");
        
        salvarBtn.addActionListener(e -> {
            try {
                String nome = nomeField.getText().trim();
                String cpf = cpfField.getText().trim();
                String email = emailField.getText().trim();
                String senha = new String(senhaField.getPassword());
                
                if (nome.isEmpty() || cpf.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Long franquiaId = null;
                if (franquiaCombo.getSelectedIndex() > 0) {
                    String selectedText = (String) franquiaCombo.getSelectedItem();
                    franquiaId = Long.parseLong(selectedText.split(" - ")[0]);
                }
                
                usuarioService.cadastrarGerente(nome, cpf, email, senha, franquiaId);
                
                String mensagem = "Gerente cadastrado com sucesso!";
                if (franquiaId == null) {
                    mensagem += "\nGerente disponível para atribuição a franquias.";
                } else {
                    mensagem += "\nGerente atribuído à franquia ID: " + franquiaId;
                }
                
                JOptionPane.showMessageDialog(this, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                nomeField.setText("");
                cpfField.setText("");
                emailField.setText("");
                senhaField.setText("");
                franquiaCombo.setSelectedIndex(0);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar gerente: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelarBtn.addActionListener(e -> showWelcomePanel());
        
        buttonPanel.add(salvarBtn);
        buttonPanel.add(cancelarBtn);
        
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);
        
        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showListaGerentesPanel() {
        contentPanel.removeAll();
        
        List<Gerente> gerentes = usuarioService.listarGerentes();
        
        String[] columnNames = {"ID", "Nome", "Email", "CPF", "Franquia ID"};
        Object[][] data = new Object[gerentes.size()][5];
        
        for (int i = 0; i < gerentes.size(); i++) {
            Gerente gerente = gerentes.get(i);
            data[i][0] = gerente.getId();
            data[i][1] = gerente.getNome();
            data[i][2] = gerente.getEmail();
            data[i][3] = gerente.getCpf();
            data[i][4] = gerente.getFranquiaId();
        }
        
        JTable table = new JTable(data, columnNames);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null); 
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton editarBtn = new JButton("Editar");
        JButton removerBtn = new JButton("Remover");
        JButton voltarBtn = new JButton("Voltar");
        
        editarBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Long gerenteId = (Long) table.getValueAt(selectedRow, 0);
                editarGerente(gerenteId);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um gerente para editar", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        removerBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Long gerenteId = (Long) table.getValueAt(selectedRow, 0);
                String nomeGerente = (String) table.getValueAt(selectedRow, 1);
                
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Deseja realmente remover o gerente '" + nomeGerente + "'?", 
                    "Confirmar Remoção", 
                    JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        usuarioService.removerUsuario(gerenteId);
                        JOptionPane.showMessageDialog(this, "Gerente removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        showListaGerentesPanel(); // Recarregar lista
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Erro ao remover gerente: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um gerente para remover", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        voltarBtn.addActionListener(e -> showDashboard());
        
        buttonPanel.add(editarBtn);
        buttonPanel.add(removerBtn);
        buttonPanel.add(voltarBtn);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Gerentes"));
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showListaUsuariosPanel() {
        contentPanel.removeAll();
        
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        
        String[] columnNames = {"ID", "Nome", "Email", "CPF", "Tipo"};
        Object[][] data = new Object[usuarios.size()][5];
        
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuario = usuarios.get(i);
            data[i][0] = usuario.getId();
            data[i][1] = usuario.getNome();
            data[i][2] = usuario.getEmail();
            data[i][3] = usuario.getCpf();
            data[i][4] = usuario.getTipoUsuario();
        }
        
        JTable table = new JTable(data, columnNames);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null); 
        
        JPanel filterPanel = new JPanel(new FlowLayout());
        filterPanel.add(new JLabel("Filtrar por tipo:"));
        JComboBox<String> tipoCombo = new JComboBox<>(new String[]{"Todos", "DONO", "GERENTE", "VENDEDOR"});
        
        tipoCombo.addActionListener(e -> {
            String tipoSelecionado = (String) tipoCombo.getSelectedItem();
            List<Usuario> usuariosFiltrados;
            
            if ("Todos".equals(tipoSelecionado)) {
                usuariosFiltrados = usuarioService.listarUsuarios();
            } else {
                usuariosFiltrados = usuarioService.listarUsuariosPorTipo(tipoSelecionado);
            }
            
            Object[][] newData = new Object[usuariosFiltrados.size()][5];
            for (int i = 0; i < usuariosFiltrados.size(); i++) {
                Usuario usuario = usuariosFiltrados.get(i);
                newData[i][0] = usuario.getId();
                newData[i][1] = usuario.getNome();
                newData[i][2] = usuario.getEmail();
                newData[i][3] = usuario.getCpf();
                newData[i][4] = usuario.getTipoUsuario();
            }
            
            table.setModel(new javax.swing.table.DefaultTableModel(newData, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
        });
        
        filterPanel.add(tipoCombo);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton voltarBtn = new JButton("Voltar");
        voltarBtn.addActionListener(e -> showDashboard());
        buttonPanel.add(voltarBtn);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Usuários"));
        
        contentPanel.add(filterPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showCadastroVendedorPanel() {
        contentPanel.removeAll();
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        JLabel titleLabel = new JLabel("Cadastro de Vendedor");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Nome:"), gbc);
        JTextField nomeField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(nomeField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("CPF:"), gbc);
        JTextField cpfField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(cpfField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Email:"), gbc);
        JTextField emailField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Senha:"), gbc);
        JPasswordField senhaField = new JPasswordField(20);
        gbc.gridx = 1;
        formPanel.add(senhaField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Franquia:"), gbc);
        
        JComboBox<String> franquiaCombo = new JComboBox<>();
        franquiaCombo.addItem("Selecione uma franquia");
        try {
            franquiaService.listarFranquias().forEach(f -> 
                franquiaCombo.addItem(f.getId() + " - " + f.getNome())
            );
        } catch (Exception e) {
        }
        gbc.gridx = 1;
        formPanel.add(franquiaCombo, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton salvarBtn = new JButton("Salvar");
        JButton cancelarBtn = new JButton("Cancelar");
        
        salvarBtn.addActionListener(e -> {
            try {
                String nome = nomeField.getText().trim();
                String cpf = cpfField.getText().trim();
                String email = emailField.getText().trim();
                String senha = new String(senhaField.getPassword());
                
                if (nome.isEmpty() || cpf.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (franquiaCombo.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, "Selecione uma franquia", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String selectedText = (String) franquiaCombo.getSelectedItem();
                Long franquiaId = Long.parseLong(selectedText.split(" - ")[0]);
                
                usuarioService.cadastrarVendedor(nome, cpf, email, senha, franquiaId);
                JOptionPane.showMessageDialog(this, "Vendedor cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                nomeField.setText("");
                cpfField.setText("");
                emailField.setText("");
                senhaField.setText("");
                franquiaCombo.setSelectedIndex(0);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar vendedor: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelarBtn.addActionListener(e -> showDashboard());
        
        buttonPanel.add(salvarBtn);
        buttonPanel.add(cancelarBtn);
        
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);
        
        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showListaVendedoresPanel() {
        contentPanel.removeAll();
        
        List<Vendedor> vendedores = usuarioService.listarVendedores();
        
        String[] columnNames = {"ID", "Nome", "Email", "CPF", "Franquia ID", "Total Vendas", "Qtd Vendas"};
        Object[][] data = new Object[vendedores.size()][7];
        
        for (int i = 0; i < vendedores.size(); i++) {
            Vendedor vendedor = vendedores.get(i);
            data[i][0] = vendedor.getId();
            data[i][1] = vendedor.getNome();
            data[i][2] = vendedor.getEmail();
            data[i][3] = vendedor.getCpf();
            data[i][4] = vendedor.getFranquiaId();
            data[i][5] = String.format("R$ %.2f", vendedor.getTotalVendas());
            data[i][6] = vendedor.getQuantidadeVendas();
        }
        
        JTable table = new JTable(data, columnNames);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null); 
        
        JPanel filterPanel = new JPanel(new FlowLayout());
        filterPanel.add(new JLabel("Filtrar por franquia:"));
        JComboBox<String> franquiaCombo = new JComboBox<>();
        franquiaCombo.addItem("Todas as franquias");
        
        try {
            franquiaService.listarFranquias().forEach(f -> 
                franquiaCombo.addItem(f.getId() + " - " + f.getNome())
            );
        } catch (Exception e) {
        }
        
        franquiaCombo.addActionListener(e -> {
            String selected = (String) franquiaCombo.getSelectedItem();
            List<Vendedor> vendedoresFiltrados;
            
            if ("Todas as franquias".equals(selected)) {
                vendedoresFiltrados = usuarioService.listarVendedores();
            } else {
                Long franquiaId = Long.parseLong(selected.split(" - ")[0]);
                vendedoresFiltrados = usuarioService.listarVendedoresPorFranquia(franquiaId);
            }
            
            Object[][] newData = new Object[vendedoresFiltrados.size()][7];
            for (int i = 0; i < vendedoresFiltrados.size(); i++) {
                Vendedor vendedor = vendedoresFiltrados.get(i);
                newData[i][0] = vendedor.getId();
                newData[i][1] = vendedor.getNome();
                newData[i][2] = vendedor.getEmail();
                newData[i][3] = vendedor.getCpf();
                newData[i][4] = vendedor.getFranquiaId();
                newData[i][5] = String.format("R$ %.2f", vendedor.getTotalVendas());
                newData[i][6] = vendedor.getQuantidadeVendas();
            }
            
            table.setModel(new javax.swing.table.DefaultTableModel(newData, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
        });
        
        filterPanel.add(franquiaCombo);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton editarBtn = new JButton("Editar");
        JButton removerBtn = new JButton("Remover");
        JButton voltarBtn = new JButton("Voltar");
        
        editarBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Long vendedorId = (Long) table.getValueAt(selectedRow, 0);
                editarVendedor(vendedorId);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um vendedor para editar", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        removerBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Long vendedorId = (Long) table.getValueAt(selectedRow, 0);
                String nomeVendedor = (String) table.getValueAt(selectedRow, 1);
                
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Deseja realmente remover o vendedor '" + nomeVendedor + "'?", 
                    "Confirmar Remoção", 
                    JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        usuarioService.removerUsuario(vendedorId);
                        JOptionPane.showMessageDialog(this, "Vendedor removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        showListaVendedoresPanel();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Erro ao remover vendedor: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um vendedor para remover", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        voltarBtn.addActionListener(e -> showDashboard());
        
        buttonPanel.add(editarBtn);
        buttonPanel.add(removerBtn);
        buttonPanel.add(voltarBtn);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Vendedores"));
        
        contentPanel.add(filterPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showRankingVendedoresPanel() {
        contentPanel.removeAll();
        
        List<Vendedor> vendedores = usuarioService.listarVendedores();
        
        vendedores.sort((v1, v2) -> Double.compare(v2.getTotalVendas(), v1.getTotalVendas()));
        
        String[] columnNames = {"Posição", "Nome", "Franquia ID", "Total Vendas", "Qtd Vendas", "Ticket Médio"};
        Object[][] data = new Object[vendedores.size()][6];
        
        for (int i = 0; i < vendedores.size(); i++) {
            Vendedor vendedor = vendedores.get(i);
            data[i][0] = i + 1; 
            data[i][1] = vendedor.getNome();
            data[i][2] = vendedor.getFranquiaId();
            data[i][3] = String.format("R$ %.2f", vendedor.getTotalVendas());
            data[i][4] = vendedor.getQuantidadeVendas();
            data[i][5] = String.format("R$ %.2f", vendedor.calcularTicketMedio());
        }
        
        JTable table = new JTable(data, columnNames);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null); 
        
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    switch (row) {
                        case 0: // 1º lugar - Dourado
                            c.setBackground(new Color(255, 215, 0, 100));
                            break;
                        case 1: // 2º lugar - Prateado  
                            c.setBackground(new Color(192, 192, 192, 100));
                            break;
                        case 2: // 3º lugar - Bronze
                            c.setBackground(new Color(205, 127, 50, 100));
                            break;
                        default:
                            c.setBackground(Color.WHITE);
                            break;
                    }
                }
                
                return c;
            }
        });
        
        JPanel infoPanel = new JPanel(new FlowLayout());
        JLabel infoLabel = new JLabel("🏆 Ranking de Vendedores por Volume de Vendas");
        infoLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        infoPanel.add(infoLabel);
        
        JPanel filterPanel = new JPanel(new FlowLayout());
        filterPanel.add(new JLabel("Filtrar por franquia:"));
        JComboBox<String> franquiaCombo = new JComboBox<>();
        franquiaCombo.addItem("Todas as franquias");
        
        try {
            franquiaService.listarFranquias().forEach(f -> 
                franquiaCombo.addItem(f.getId() + " - " + f.getNome())
            );
        } catch (Exception e) {
        }
        
        franquiaCombo.addActionListener(e -> {
            String selected = (String) franquiaCombo.getSelectedItem();
            List<Vendedor> vendedoresFiltrados;
            
            if ("Todas as franquias".equals(selected)) {
                vendedoresFiltrados = usuarioService.listarVendedores();
            } else {
                Long franquiaId = Long.parseLong(selected.split(" - ")[0]);
                vendedoresFiltrados = usuarioService.listarVendedoresPorFranquia(franquiaId);
            }
            
            vendedoresFiltrados.sort((v1, v2) -> Double.compare(v2.getTotalVendas(), v1.getTotalVendas()));
            
            Object[][] newData = new Object[vendedoresFiltrados.size()][6];
            for (int i = 0; i < vendedoresFiltrados.size(); i++) {
                Vendedor vendedor = vendedoresFiltrados.get(i);
                newData[i][0] = i + 1;
                newData[i][1] = vendedor.getNome();
                newData[i][2] = vendedor.getFranquiaId();
                newData[i][3] = String.format("R$ %.2f", vendedor.getTotalVendas());
                newData[i][4] = vendedor.getQuantidadeVendas();
                newData[i][5] = String.format("R$ %.2f", vendedor.calcularTicketMedio());
            }
            
            table.setModel(new javax.swing.table.DefaultTableModel(newData, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
        });
        
        filterPanel.add(franquiaCombo);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(infoPanel, BorderLayout.CENTER);
        topPanel.add(filterPanel, BorderLayout.SOUTH);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton verDetalhesBtn = new JButton("Ver Detalhes");
        JButton voltarBtn = new JButton("Voltar");
        
        verDetalhesBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String nomeVendedor = (String) table.getValueAt(selectedRow, 1);
                String franquiaId = table.getValueAt(selectedRow, 2).toString();
                String totalVendas = (String) table.getValueAt(selectedRow, 3);
                String qtdVendas = table.getValueAt(selectedRow, 4).toString();
                String ticketMedio = (String) table.getValueAt(selectedRow, 5);
                int posicao = (Integer) table.getValueAt(selectedRow, 0);
                
                String detalhes = String.format(
                    "🏆 DETALHES DO VENDEDOR\n\n" +
                    "Posição no Ranking: %dº lugar\n" +
                    "Nome: %s\n" +
                    "Franquia: %s\n" +
                    "Total de Vendas: %s\n" +
                    "Quantidade de Vendas: %s\n" +
                    "Ticket Médio: %s\n\n" +
                    "%s",
                    posicao, nomeVendedor, franquiaId, totalVendas, qtdVendas, ticketMedio,
                    posicao <= 3 ? "🎉 Parabéns! Está entre os 3 melhores!" : "Continue se esforçando para subir no ranking!"
                );
                
                JOptionPane.showMessageDialog(this, detalhes, "Detalhes do Vendedor", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um vendedor para ver os detalhes", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        voltarBtn.addActionListener(e -> showDashboard());
        
        buttonPanel.add(verDetalhesBtn);
        buttonPanel.add(voltarBtn);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Ranking de Vendedores"));
        
        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showCadastroProdutoPanel() {
        contentPanel.removeAll();
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        JLabel titleLabel = new JLabel("Cadastro de Produto");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Nome:"), gbc);
        JTextField nomeField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(nomeField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Descrição:"), gbc);
        JTextArea descricaoArea = new JTextArea(3, 20);
        descricaoArea.setBorder(BorderFactory.createLoweredBevelBorder());
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(descricaoArea), gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Preço (R$):"), gbc);
        JTextField precoField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(precoField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Quantidade em Estoque:"), gbc);
        JTextField estoqueField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(estoqueField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Estoque Mínimo:"), gbc);
        JTextField estoqueMinField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(estoqueMinField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Categoria:"), gbc);
        JComboBox<String> categoriaCombo = new JComboBox<>(new String[]{
            "Geral", "Hambúrgueres", "Pizzas", "Bebidas", "Acompanhamentos", "Sobremesas", "Pratos Principais"
        });
        categoriaCombo.setEditable(true);
        gbc.gridx = 1;
        formPanel.add(categoriaCombo, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton salvarBtn = new JButton("Salvar");
        JButton cancelarBtn = new JButton("Cancelar");
        
        salvarBtn.addActionListener(e -> {
            try {
                String nome = nomeField.getText().trim();
                String descricao = descricaoArea.getText().trim();
                String precoText = precoField.getText().trim();
                String estoqueText = estoqueField.getText().trim();
                String estoqueMinText = estoqueMinField.getText().trim();
                String categoria = (String) categoriaCombo.getSelectedItem();
                
                if (nome.isEmpty() || precoText.isEmpty() || estoqueText.isEmpty() || estoqueMinText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Todos os campos obrigatórios devem ser preenchidos", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                double preco = Double.parseDouble(precoText.replace(",", "."));
                int estoque = Integer.parseInt(estoqueText);
                int estoqueMin = Integer.parseInt(estoqueMinText);
                
                produtoService.cadastrarProduto(nome, descricao, preco, estoque, estoqueMin, categoria);
                JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                nomeField.setText("");
                descricaoArea.setText("");
                precoField.setText("");
                estoqueField.setText("");
                estoqueMinField.setText("");
                categoriaCombo.setSelectedIndex(0);
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Verifique se os valores numéricos estão corretos", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelarBtn.addActionListener(e -> showDashboard());
        
        buttonPanel.add(salvarBtn);
        buttonPanel.add(cancelarBtn);
        
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);
        
        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showListaProdutosPanel() {
        contentPanel.removeAll();
        
        List<Produto> produtos = produtoService.listarProdutos();
        
        String[] columnNames = {"ID", "Nome", "Categoria", "Preço", "Estoque", "Est. Mín.", "Status"};
        Object[][] data = new Object[produtos.size()][7];
        
        for (int i = 0; i < produtos.size(); i++) {
            Produto produto = produtos.get(i);
            data[i][0] = produto.getId();
            data[i][1] = produto.getNome();
            data[i][2] = produto.getCategoria();
            data[i][3] = String.format("R$ %.2f", produto.getPreco());
            data[i][4] = produto.getEstoque();
            data[i][5] = produto.getEstoqueMinimo();
            data[i][6] = produto.getEstoque() <= produto.getEstoqueMinimo() ? "BAIXO" : "OK";
        }
        
        JTable table = new JTable(data, columnNames);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null); // Torna a tabela não editável
        
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                String status = (String) table.getValueAt(row, 6);
                if ("BAIXO".equals(status) && !isSelected) {
                    c.setBackground(new Color(255, 230, 230)); // Vermelho claro
                } else if (!isSelected) {
                    c.setBackground(Color.WHITE);
                }
                
                return c;
            }
        });
        
        JPanel filterPanel = new JPanel(new FlowLayout());
        filterPanel.add(new JLabel("Filtrar por categoria:"));
        JComboBox<String> categoriaCombo = new JComboBox<>();
        categoriaCombo.addItem("Todas as categorias");
        categoriaCombo.addItem("Hambúrgueres");
        categoriaCombo.addItem("Pizzas");
        categoriaCombo.addItem("Bebidas");
        categoriaCombo.addItem("Acompanhamentos");
        categoriaCombo.addItem("Sobremesas");
        
        categoriaCombo.addActionListener(e -> {
            String selected = (String) categoriaCombo.getSelectedItem();
            List<Produto> produtosFiltrados;
            
            if ("Todas as categorias".equals(selected)) {
                produtosFiltrados = produtoService.listarProdutos();
            } else {
                produtosFiltrados = produtoService.listarProdutosPorCategoria(selected);
            }
            
            Object[][] newData = new Object[produtosFiltrados.size()][7];
            for (int i = 0; i < produtosFiltrados.size(); i++) {
                Produto produto = produtosFiltrados.get(i);
                newData[i][0] = produto.getId();
                newData[i][1] = produto.getNome();
                newData[i][2] = produto.getCategoria();
                newData[i][3] = String.format("R$ %.2f", produto.getPreco());
                newData[i][4] = produto.getEstoque();
                newData[i][5] = produto.getEstoqueMinimo();
                newData[i][6] = produto.getEstoque() <= produto.getEstoqueMinimo() ? "BAIXO" : "OK";
            }
            
            table.setModel(new javax.swing.table.DefaultTableModel(newData, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
        });
        
        filterPanel.add(categoriaCombo);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton editarBtn = new JButton("Editar");
        JButton removerBtn = new JButton("Remover");
        JButton atualizarEstoqueBtn = new JButton("Atualizar Estoque");
        JButton voltarBtn = new JButton("Voltar");
        
        editarBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Long produtoId = (Long) table.getValueAt(selectedRow, 0);
                editarProduto(produtoId);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um produto para editar", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        removerBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Long produtoId = (Long) table.getValueAt(selectedRow, 0);
                String nomeProduto = (String) table.getValueAt(selectedRow, 1);
                
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Deseja realmente remover o produto '" + nomeProduto + "'?", 
                    "Confirmar Remoção", 
                    JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        produtoService.removerProduto(produtoId);
                        JOptionPane.showMessageDialog(this, "Produto removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        showListaProdutosPanel(); // Recarregar lista
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Erro ao remover produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um produto para remover", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        atualizarEstoqueBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Long produtoId = (Long) table.getValueAt(selectedRow, 0);
                String nomeProduto = (String) table.getValueAt(selectedRow, 1);
                int estoqueAtual = (Integer) table.getValueAt(selectedRow, 4);
                
                String novoEstoqueStr = JOptionPane.showInputDialog(this, 
                    "Novo estoque para '" + nomeProduto + "':", 
                    estoqueAtual);
                
                if (novoEstoqueStr != null && !novoEstoqueStr.trim().isEmpty()) {
                    try {
                        int novoEstoque = Integer.parseInt(novoEstoqueStr.trim());
                        produtoService.atualizarEstoque(produtoId, novoEstoque);
                        JOptionPane.showMessageDialog(this, "Estoque atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        showListaProdutosPanel(); // Recarregar lista
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Por favor, digite um número válido", "Erro", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Erro ao atualizar estoque: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um produto para atualizar o estoque", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        voltarBtn.addActionListener(e -> showDashboard());
        
        buttonPanel.add(editarBtn);
        buttonPanel.add(removerBtn);
        buttonPanel.add(atualizarEstoqueBtn);
        buttonPanel.add(voltarBtn);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Produtos"));
        
        contentPanel.add(filterPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showEstoqueBaixoPanel() {
        contentPanel.removeAll();
        
        List<Produto> produtosEstoqueBaixo = produtoService.listarProdutosEstoqueBaixo();
        
        if (produtosEstoqueBaixo.isEmpty()) {
            JLabel msgLabel = new JLabel("Nenhum produto com estoque baixo encontrado!", SwingConstants.CENTER);
            msgLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
            msgLabel.setForeground(new Color(0, 150, 0));
            
            JPanel msgPanel = new JPanel(new BorderLayout());
            msgPanel.add(msgLabel, BorderLayout.CENTER);
            
            JButton voltarBtn = new JButton("Voltar");
            voltarBtn.addActionListener(e -> showDashboard());
            
            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(voltarBtn);
            msgPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            contentPanel.add(msgPanel, BorderLayout.CENTER);
        } else {
            String[] columnNames = {"ID", "Nome", "Categoria", "Estoque Atual", "Estoque Mínimo", "Diferença"};
            Object[][] data = new Object[produtosEstoqueBaixo.size()][6];
            
            for (int i = 0; i < produtosEstoqueBaixo.size(); i++) {
                Produto produto = produtosEstoqueBaixo.get(i);
                data[i][0] = produto.getId();
                data[i][1] = produto.getNome();
                data[i][2] = produto.getCategoria();
                data[i][3] = produto.getEstoque();
                data[i][4] = produto.getEstoqueMinimo();
                data[i][5] = produto.getEstoqueMinimo() - produto.getEstoque();
            }
            
            JTable table = new JTable(data, columnNames);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.setDefaultEditor(Object.class, null); // Torna a tabela não editável
            
            table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    
                    if (!isSelected) {
                        c.setBackground(new Color(255, 230, 230)); // Vermelho claro
                    }
                    
                    return c;
                }
            });
            
            JPanel alertPanel = new JPanel(new FlowLayout());
            alertPanel.setBackground(new Color(255, 200, 200));
            alertPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            
            JLabel alertIcon = new JLabel("⚠️");
            alertIcon.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
            
            JLabel alertLabel = new JLabel("ATENÇÃO: " + produtosEstoqueBaixo.size() + " produto(s) com estoque baixo!");
            alertLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
            alertLabel.setForeground(Color.RED);
            
            alertPanel.add(alertIcon);
            alertPanel.add(alertLabel);
            
            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton atualizarEstoqueBtn = new JButton("Atualizar Estoque");
            JButton verTodosBtn = new JButton("Ver Todos os Produtos");
            JButton voltarBtn = new JButton("Voltar");
            
            atualizarEstoqueBtn.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    Long produtoId = (Long) table.getValueAt(selectedRow, 0);
                    String nomeProduto = (String) table.getValueAt(selectedRow, 1);
                    int estoqueAtual = (Integer) table.getValueAt(selectedRow, 3);
                    int estoqueMinimo = (Integer) table.getValueAt(selectedRow, 4);
                    
                    String novoEstoqueStr = JOptionPane.showInputDialog(this, 
                        "Novo estoque para '" + nomeProduto + "':\n" +
                        "Atual: " + estoqueAtual + " | Mínimo: " + estoqueMinimo, 
                        estoqueMinimo + 10); // Sugestão de estoque
                    
                    if (novoEstoqueStr != null && !novoEstoqueStr.trim().isEmpty()) {
                        try {
                            int novoEstoque = Integer.parseInt(novoEstoqueStr.trim());
                            produtoService.atualizarEstoque(produtoId, novoEstoque);
                            JOptionPane.showMessageDialog(this, "Estoque atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                            showEstoqueBaixoPanel(); // Recarregar lista
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "Por favor, digite um número válido", "Erro", JOptionPane.ERROR_MESSAGE);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, "Erro ao atualizar estoque: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Selecione um produto para atualizar o estoque", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            });
            
            verTodosBtn.addActionListener(e -> showListaProdutosPanel());
            voltarBtn.addActionListener(e -> showDashboard());
            
            buttonPanel.add(atualizarEstoqueBtn);
            buttonPanel.add(verTodosBtn);
            buttonPanel.add(voltarBtn);
            
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createTitledBorder("Produtos com Estoque Baixo"));
            
            contentPanel.add(alertPanel, BorderLayout.NORTH);
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        }
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showListaPedidosPanel() {
        contentPanel.removeAll();
        
        JPanel listaPanel = new JPanel(new BorderLayout());
        listaPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("📋 LISTA DE PEDIDOS", SwingConstants.CENTER);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JPanel filterPanel = new JPanel(new FlowLayout());
        JComboBox<String> statusFilter = new JComboBox<>(new String[]{
            "Todos", "PENDENTE", "APROVADO", "REJEITADO", "ENTREGUE", "CANCELADO"
        });
        JButton filtrarBtn = new JButton("Filtrar");
        JButton atualizarStatusBtn = new JButton("Atualizar Status");
        
        filterPanel.add(new JLabel("Status:"));
        filterPanel.add(statusFilter);
        filterPanel.add(filtrarBtn);
        filterPanel.add(atualizarStatusBtn);
        
        String[] columnNames = {"ID", "Cliente", "Data/Hora", "Status", "Valor Total", "Vendedor"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable pedidosTable = new JTable(tableModel);
        pedidosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(pedidosTable);
        
        Runnable carregarPedidos = () -> {
            try {
                tableModel.setRowCount(0);
                PedidoService pedidoService = new PedidoService();
                List<Pedido> pedidos;
                
                String statusSelecionado = (String) statusFilter.getSelectedItem();
                if ("Todos".equals(statusSelecionado)) {
                    if (usuarioLogado instanceof Vendedor) {
                        pedidos = pedidoService.listarPedidosPorVendedor((Vendedor) usuarioLogado);
                    } else if (usuarioLogado instanceof Gerente) {
                        pedidos = pedidoService.listarPedidosPorFranquia(((Gerente) usuarioLogado).getFranquiaId());
                    } else {
                        pedidos = pedidoService.listarTodos();
                    }
                } else {
                    Pedido.StatusPedido status = Pedido.StatusPedido.valueOf(statusSelecionado);
                    pedidos = pedidoService.listarPedidosPorStatus(status);
                    
                    if (usuarioLogado instanceof Vendedor) {
                        pedidos = pedidos.stream()
                            .filter(p -> p.getVendedor().getId().equals(usuarioLogado.getId()))
                            .collect(java.util.stream.Collectors.toList());
                    } else if (usuarioLogado instanceof Gerente) {
                        pedidos = pedidos.stream()
                            .filter(p -> p.getFranquiaId().equals(((Gerente) usuarioLogado).getFranquiaId()))
                            .collect(java.util.stream.Collectors.toList());
                    }
                }
                
                for (Pedido pedido : pedidos) {
                    Object[] row = {
                        pedido.getId(),
                        pedido.getNomeCliente(),
                        pedido.getDataHora().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                        pedido.getStatus(),
                        String.format("R$ %.2f", pedido.getTotal()),
                        pedido.getVendedor().getNome()
                    };
                    tableModel.addRow(row);
                }
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(MainInterface.this, "Erro ao carregar pedidos: " + e.getMessage());
            }
        };
        
        carregarPedidos.run();
        
        filtrarBtn.addActionListener(e -> carregarPedidos.run());
        
        atualizarStatusBtn.addActionListener(e -> {
            int selectedRow = pedidosTable.getSelectedRow();
            if (selectedRow >= 0) {
                try {
                    Long pedidoId = (Long) tableModel.getValueAt(selectedRow, 0);
                    
                    String[] statusOptions = {"PENDENTE", "APROVADO", "REJEITADO", "ENTREGUE", "CANCELADO"};
                    String novoStatus = (String) JOptionPane.showInputDialog(
                        this,
                        "Selecione o novo status:",
                        "Atualizar Status",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        statusOptions,
                        statusOptions[0]
                    );
                    
                    if (novoStatus != null) {
                        PedidoService pedidoService = new PedidoService();
                        pedidoService.atualizarStatus(pedidoId, Pedido.StatusPedido.valueOf(novoStatus));
                        
                        JOptionPane.showMessageDialog(this, "Status atualizado com sucesso!");
                        carregarPedidos.run();
                    }
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar status: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um pedido para atualizar o status");
            }
        });
        
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Detalhes do Pedido"));
        
        JTextArea detailsArea = new JTextArea(8, 40);
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane detailsScrollPane = new JScrollPane(detailsArea);
        detailsPanel.add(detailsScrollPane);
        
        pedidosTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = pedidosTable.getSelectedRow();
                if (selectedRow >= 0) {
                    try {
                        Long pedidoId = (Long) tableModel.getValueAt(selectedRow, 0);
                        PedidoService pedidoService = new PedidoService();
                        Pedido pedido = pedidoService.buscarPorId(pedidoId);
                        
                        StringBuilder details = new StringBuilder();
                        details.append("PEDIDO #").append(pedido.getId()).append("\n");
                        details.append("Cliente: ").append(pedido.getNomeCliente()).append("\n");
                        details.append("Data/Hora: ").append(pedido.getDataHora().format(
                            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))).append("\n");
                        details.append("Vendedor: ").append(pedido.getVendedor().getNome()).append("\n");
                        details.append("Status: ").append(pedido.getStatus()).append("\n");
                        details.append("Forma de Pagamento: ").append(pedido.getFormaPagamento()).append("\n");
                        details.append("Modalidade de Entrega: ").append(pedido.getModalidadeEntrega()).append("\n");
                        details.append("\nITENS:\n");
                        details.append("----------------------------------------\n");
                        
                        for (ItemPedido item : pedido.getItens()) {
                            details.append(String.format("• %s\n", item.getProduto().getNome()));
                            details.append(String.format("  Qtd: %d x R$ %.2f = R$ %.2f\n", 
                                item.getQuantidade(), item.getPrecoUnitario(), item.getSubtotal()));
                            if (item.getObservacoes() != null && !item.getObservacoes().trim().isEmpty()) {
                                details.append("  Obs: ").append(item.getObservacoes()).append("\n");
                            }
                            details.append("\n");
                        }
                        
                        details.append("----------------------------------------\n");
                        details.append(String.format("Subtotal: R$ %.2f\n", pedido.getSubtotal()));
                        details.append(String.format("Taxa de Entrega: R$ %.2f\n", pedido.getTaxaEntrega()));
                        details.append(String.format("Taxa de Serviço: R$ %.2f\n", pedido.getTaxaServico()));
                        details.append(String.format("TOTAL: R$ %.2f", pedido.getTotal()));
                        
                        detailsArea.setText(details.toString());
                        detailsArea.setCaretPosition(0);
                        
                    } catch (Exception ex) {
                        detailsArea.setText("Erro ao carregar detalhes: " + ex.getMessage());
                    }
                }
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton voltarBtn = new JButton("Voltar");
        voltarBtn.addActionListener(e -> showWelcomePanel());
        buttonPanel.add(voltarBtn);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.SOUTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(detailsPanel, BorderLayout.SOUTH);
        
        listaPanel.add(topPanel, BorderLayout.NORTH);
        listaPanel.add(centerPanel, BorderLayout.CENTER);
        listaPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        contentPanel.add(listaPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showRelatorioFranquiaPanel() {
        contentPanel.removeAll();
        
        JPanel relatorioPanel = new JPanel(new BorderLayout());
        relatorioPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("📊 RELATÓRIO DA FRANQUIA", SwingConstants.CENTER);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        try {
            FranquiaService franquiaService = new FranquiaService();
            
            Long franquiaId = null;
            if (usuarioLogado instanceof Vendedor) {
                franquiaId = ((Vendedor) usuarioLogado).getFranquiaId();
            } else if (usuarioLogado instanceof Gerente) {
                franquiaId = ((Gerente) usuarioLogado).getFranquiaId();
            } else {
                List<Franquia> franquias = franquiaService.listarFranquias();
                if (!franquias.isEmpty()) {
                    franquiaId = franquias.get(0).getId();
                } else {
                    throw new Exception("Nenhuma franquia encontrada");
                }
            }
            
            Franquia franquia = franquiaService.buscarFranquiaPorId(franquiaId);
            
            PedidoService pedidoService = new PedidoService();
            List<Pedido> pedidosFranquia = pedidoService.listarPedidosPorFranquia(franquiaId);
            
            JPanel infoPanel = new JPanel(new GridBagLayout());
            infoPanel.setBorder(BorderFactory.createTitledBorder("Informações da Franquia"));
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 10, 5, 10);
            gbc.anchor = GridBagConstraints.WEST;
            
            gbc.gridx = 0; gbc.gridy = 0;
            infoPanel.add(new JLabel("Nome:"), gbc);
            gbc.gridx = 1;
            infoPanel.add(new JLabel(franquia.getNome()), gbc);
            
            gbc.gridx = 0; gbc.gridy = 1;
            infoPanel.add(new JLabel("Endereço:"), gbc);
            gbc.gridx = 1;
            infoPanel.add(new JLabel(franquia.getEndereco().getEnderecoCompleto()), gbc);
            
            gbc.gridx = 0; gbc.gridy = 2;
            infoPanel.add(new JLabel("Cidade:"), gbc);
            gbc.gridx = 1;
            infoPanel.add(new JLabel(franquia.getCidade() + " - " + franquia.getEstado()), gbc);
            
            JPanel statsPanel = new JPanel(new GridLayout(2, 4, 10, 10));
            statsPanel.setBorder(BorderFactory.createTitledBorder("Estatísticas de Pedidos"));
            
            int totalPedidos = pedidosFranquia.size();
            long pendentes = pedidosFranquia.stream().filter(p -> p.getStatus() == Pedido.StatusPedido.PENDENTE).count();
            long aprovados = pedidosFranquia.stream().filter(p -> p.getStatus() == Pedido.StatusPedido.APROVADO).count();
            long entregues = pedidosFranquia.stream().filter(p -> p.getStatus() == Pedido.StatusPedido.ENTREGUE).count();
            long cancelados = pedidosFranquia.stream().filter(p -> p.getStatus() == Pedido.StatusPedido.CANCELADO).count();
            
            double faturamentoTotal = pedidosFranquia.stream()
                .filter(p -> p.getStatus() == Pedido.StatusPedido.ENTREGUE)
                .mapToDouble(Pedido::getTotal)
                .sum();
            
            double ticketMedio = entregues > 0 ? faturamentoTotal / entregues : 0.0;
            
            long pedidosHoje = pedidosFranquia.stream()
                .filter(p -> p.getDataHora().toLocalDate().equals(java.time.LocalDate.now()))
                .count();
            
            statsPanel.add(createStatLabel("Total de Pedidos", String.valueOf(totalPedidos)));
            statsPanel.add(createStatLabel("Pendentes", String.valueOf(pendentes)));
            statsPanel.add(createStatLabel("Aprovados", String.valueOf(aprovados)));
            statsPanel.add(createStatLabel("Entregues", String.valueOf(entregues)));
            statsPanel.add(createStatLabel("Cancelados", String.valueOf(cancelados)));
            statsPanel.add(createStatLabel("Pedidos Hoje", String.valueOf(pedidosHoje)));
            statsPanel.add(createStatLabel("Faturamento Total", String.format("R$ %.2f", faturamentoTotal)));
            statsPanel.add(createStatLabel("Ticket Médio", String.format("R$ %.2f", ticketMedio)));
            
            JPanel vendedoresPanel = new JPanel(new BorderLayout());
            vendedoresPanel.setBorder(BorderFactory.createTitledBorder("Desempenho por Vendedor"));
            
            String[] vendedorColumns = {"Vendedor", "Pedidos", "Faturamento", "Ticket Médio"};
            DefaultTableModel vendedorModel = new DefaultTableModel(vendedorColumns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            
            JTable vendedorTable = new JTable(vendedorModel);
            JScrollPane vendedorScrollPane = new JScrollPane(vendedorTable);
            vendedorScrollPane.setPreferredSize(new Dimension(0, 150));
            
            // Agrupar pedidos por vendedor
            Map<Vendedor, List<Pedido>> pedidosPorVendedor = pedidosFranquia.stream()
                .collect(java.util.stream.Collectors.groupingBy(Pedido::getVendedor));
            
            for (Map.Entry<Vendedor, List<Pedido>> entry : pedidosPorVendedor.entrySet()) {
                Vendedor vendedor = entry.getKey();
                List<Pedido> pedidosVendedor = entry.getValue();
                
                int pedidosCount = pedidosVendedor.size();
                double faturamentoVendedor = pedidosVendedor.stream()
                    .filter(p -> p.getStatus() == Pedido.StatusPedido.ENTREGUE)
                    .mapToDouble(Pedido::getTotal)
                    .sum();
                
                long entreguesVendedor = pedidosVendedor.stream()
                    .filter(p -> p.getStatus() == Pedido.StatusPedido.ENTREGUE)
                    .count();
                
                double ticketMedioVendedor = entreguesVendedor > 0 ? faturamentoVendedor / entreguesVendedor : 0.0;
                
                Object[] row = {
                    vendedor.getNome(),
                    pedidosCount,
                    String.format("R$ %.2f", faturamentoVendedor),
                    String.format("R$ %.2f", ticketMedioVendedor)
                };
                vendedorModel.addRow(row);
            }
            
            vendedoresPanel.add(vendedorScrollPane);
            
            JPanel recentesPanel = new JPanel(new BorderLayout());
            recentesPanel.setBorder(BorderFactory.createTitledBorder("Pedidos Recentes"));
            
            String[] recentesColumns = {"ID", "Cliente", "Vendedor", "Status", "Valor", "Data"};
            DefaultTableModel recentesModel = new DefaultTableModel(recentesColumns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            
            JTable recentesTable = new JTable(recentesModel);
            JScrollPane recentesScrollPane = new JScrollPane(recentesTable);
            recentesScrollPane.setPreferredSize(new Dimension(0, 200));
            
            pedidosFranquia.stream()
                .sorted((p1, p2) -> p2.getDataHora().compareTo(p1.getDataHora()))
                .limit(10)
                .forEach(pedido -> {
                    Object[] row = {
                        pedido.getId(),
                        pedido.getNomeCliente(),
                        pedido.getVendedor().getNome(),
                        pedido.getStatus(),
                        String.format("R$ %.2f", pedido.getTotal()),
                        pedido.getDataHora().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM HH:mm"))
                    };
                    recentesModel.addRow(row);
                });
            
            recentesPanel.add(recentesScrollPane);
            
            JPanel actionsPanel = new JPanel(new FlowLayout());
            JButton atualizarBtn = new JButton("Atualizar Dados");
            JButton exportarBtn = new JButton("Exportar Relatório");
            
            atualizarBtn.addActionListener(e -> showRelatorioFranquiaPanel());
            
            exportarBtn.addActionListener(e -> {
                try {
                    StringBuilder relatorio = new StringBuilder();
                    relatorio.append("=== RELATÓRIO DA FRANQUIA ===\n");
                    relatorio.append("Franquia: ").append(franquia.getNome()).append("\n");
                    relatorio.append("Data: ").append(java.time.LocalDate.now()).append("\n\n");
                    
                    relatorio.append("ESTATÍSTICAS GERAIS:\n");
                    relatorio.append("Total de Pedidos: ").append(totalPedidos).append("\n");
                    relatorio.append("Pedidos Entregues: ").append(entregues).append("\n");
                    relatorio.append("Faturamento Total: R$ ").append(String.format("%.2f", faturamentoTotal)).append("\n");
                    relatorio.append("Ticket Médio: R$ ").append(String.format("%.2f", ticketMedio)).append("\n\n");
                    
                    relatorio.append("DESEMPENHO POR VENDEDOR:\n");
                    for (Map.Entry<Vendedor, List<Pedido>> entry : pedidosPorVendedor.entrySet()) {
                        Vendedor v = entry.getKey();
                        List<Pedido> pv = entry.getValue();
                        double fv = pv.stream().filter(p -> p.getStatus() == Pedido.StatusPedido.ENTREGUE).mapToDouble(Pedido::getTotal).sum();
                        relatorio.append("- ").append(v.getNome()).append(": ").append(pv.size()).append(" pedidos, R$ ").append(String.format("%.2f", fv)).append("\n");
                    }
                    
                    JTextArea textArea = new JTextArea(relatorio.toString());
                    textArea.setEditable(false);
                    textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
                    
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(600, 400));
                    
                    JOptionPane.showMessageDialog(this, scrollPane, "Relatório da Franquia", JOptionPane.INFORMATION_MESSAGE);
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao gerar relatório: " + ex.getMessage());
                }
            });
            
            actionsPanel.add(atualizarBtn);
            actionsPanel.add(exportarBtn);
            
            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton voltarBtn = new JButton("Voltar");
            voltarBtn.addActionListener(e -> showWelcomePanel());
            buttonPanel.add(voltarBtn);
            
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.add(titleLabel, BorderLayout.NORTH);
            topPanel.add(infoPanel, BorderLayout.CENTER);
            
            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.add(statsPanel, BorderLayout.NORTH);
            
            JPanel tablesPanel = new JPanel(new GridLayout(1, 2, 10, 0));
            tablesPanel.add(vendedoresPanel);
            tablesPanel.add(recentesPanel);
            centerPanel.add(tablesPanel, BorderLayout.CENTER);
            
            JPanel bottomPanel = new JPanel(new BorderLayout());
            bottomPanel.add(actionsPanel, BorderLayout.CENTER);
            bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            relatorioPanel.add(topPanel, BorderLayout.NORTH);
            relatorioPanel.add(centerPanel, BorderLayout.CENTER);
            relatorioPanel.add(bottomPanel, BorderLayout.SOUTH);
            
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Erro ao carregar relatório: " + e.getMessage(), SwingConstants.CENTER);
            relatorioPanel.add(titleLabel, BorderLayout.NORTH);
            relatorioPanel.add(errorLabel, BorderLayout.CENTER);
            
            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton voltarBtn = new JButton("Voltar");
            voltarBtn.addActionListener(ev -> showWelcomePanel());
            buttonPanel.add(voltarBtn);
            relatorioPanel.add(buttonPanel, BorderLayout.SOUTH);
        }
        
        contentPanel.add(relatorioPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private JPanel createStatLabel(String title, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEtchedBorder());
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void showCadastroPedidoPanel() {
        contentPanel.removeAll();
        
        JPanel cadastroPanel = new JPanel(new BorderLayout());
        cadastroPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("🛒 CADASTRO DE PEDIDO", SwingConstants.CENTER);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        JTextField nomeClienteField = new JTextField(20);
        JComboBox<String> formaPagamentoCombo = new JComboBox<>(new String[]{
            "Dinheiro", "Cartão de Crédito", "Cartão de Débito", "PIX"
        });
        JComboBox<String> modalidadeEntregaCombo = new JComboBox<>(new String[]{
            "Balcão", "Delivery", "Drive-thru"
        });
        JTextField taxaEntregaField = new JTextField("0.00", 10);
        JTextField taxaServicoField = new JTextField("0.00", 10);
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nome do Cliente:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nomeClienteField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Forma de Pagamento:"), gbc);
        gbc.gridx = 1;
        formPanel.add(formaPagamentoCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Modalidade de Entrega:"), gbc);
        gbc.gridx = 1;
        formPanel.add(modalidadeEntregaCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Taxa de Entrega (R$):"), gbc);
        gbc.gridx = 1;
        formPanel.add(taxaEntregaField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Taxa de Serviço (R$):"), gbc);
        gbc.gridx = 1;
        formPanel.add(taxaServicoField, gbc);
        
        JPanel itensPanel = new JPanel(new BorderLayout());
        itensPanel.setBorder(BorderFactory.createTitledBorder("Itens do Pedido"));
        
        DefaultListModel<String> itensModel = new DefaultListModel<>();
        JList<String> itensList = new JList<>(itensModel);
        itensList.setVisibleRowCount(5);
        JScrollPane itensScrollPane = new JScrollPane(itensList);
        
        JPanel addItemPanel = new JPanel(new FlowLayout());
        JComboBox<Produto> produtoCombo = new JComboBox<>();
        JTextField quantidadeField = new JTextField("1", 5);
        JButton addItemBtn = new JButton("Adicionar Item");
        
        try {
            ProdutoService produtoService = new ProdutoService();
            List<Produto> produtos = produtoService.listarProdutos();
            for (Produto produto : produtos) {
                if (produto.getEstoque() > 0) {
                    produtoCombo.addItem(produto);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + e.getMessage());
        }
        
        List<ItemPedido> itensTemporarios = new ArrayList<>();
        
        addItemBtn.addActionListener(e -> {
            try {
                Produto produtoSelecionado = (Produto) produtoCombo.getSelectedItem();
                int quantidade = Integer.parseInt(quantidadeField.getText());
                
                if (produtoSelecionado == null) {
                    JOptionPane.showMessageDialog(this, "Selecione um produto");
                    return;
                }
                
                if (quantidade <= 0) {
                    JOptionPane.showMessageDialog(this, "Quantidade deve ser maior que zero");
                    return;
                }
                
                if (quantidade > produtoSelecionado.getEstoque()) {
                    JOptionPane.showMessageDialog(this, 
                        "Quantidade indisponível. Estoque: " + produtoSelecionado.getEstoque());
                    return;
                }
                
                ItemPedido item = new ItemPedido(produtoSelecionado, quantidade);
                itensTemporarios.add(item);
                
                String itemText = String.format("%s - Qtd: %d - R$ %.2f (Subtotal: R$ %.2f)",
                    produtoSelecionado.getNome(), quantidade, produtoSelecionado.getPreco(),
                    item.getSubtotal());
                itensModel.addElement(itemText);
                
                quantidadeField.setText("1");
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantidade deve ser um número válido");
            }
        });
        
        addItemPanel.add(new JLabel("Produto:"));
        addItemPanel.add(produtoCombo);
        addItemPanel.add(new JLabel("Quantidade:"));
        addItemPanel.add(quantidadeField);
        addItemPanel.add(addItemBtn);
        
        JButton removeItemBtn = new JButton("Remover Item Selecionado");
        removeItemBtn.addActionListener(e -> {
            int selectedIndex = itensList.getSelectedIndex();
            if (selectedIndex >= 0) {
                itensTemporarios.remove(selectedIndex);
                itensModel.remove(selectedIndex);
            }
        });
        
        itensPanel.add(addItemPanel, BorderLayout.NORTH);
        itensPanel.add(itensScrollPane, BorderLayout.CENTER);
        itensPanel.add(removeItemBtn, BorderLayout.SOUTH);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton salvarBtn = new JButton("Salvar Pedido");
        JButton cancelarBtn = new JButton("Cancelar");
        
        salvarBtn.addActionListener(e -> {
            try {
                if (nomeClienteField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nome do cliente é obrigatório");
                    return;
                }
                
                if (itensTemporarios.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Adicione pelo menos um item ao pedido");
                    return;
                }
                
                Pedido pedido = new Pedido();
                pedido.setNomeCliente(nomeClienteField.getText().trim());
                pedido.setFormaPagamento((String) formaPagamentoCombo.getSelectedItem());
                pedido.setModalidadeEntrega((String) modalidadeEntregaCombo.getSelectedItem());
                
                try {
                    pedido.setTaxaEntrega(Double.parseDouble(taxaEntregaField.getText()));
                    pedido.setTaxaServico(Double.parseDouble(taxaServicoField.getText()));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Valores de taxa devem ser números válidos");
                    return;
                }
                
                if (!(usuarioLogado instanceof Vendedor)) {
                    JOptionPane.showMessageDialog(this, "Apenas vendedores podem cadastrar pedidos");
                    return;
                }
                
                pedido.setVendedor((Vendedor) usuarioLogado);
                pedido.setFranquiaId(((Vendedor) usuarioLogado).getFranquiaId());
                
                for (ItemPedido item : itensTemporarios) {
                    pedido.adicionarItem(item);
                }
                
                PedidoService pedidoService = new PedidoService();
                pedidoService.cadastrar(pedido);
                
                JOptionPane.showMessageDialog(this, 
                    String.format("Pedido cadastrado com sucesso!\nTotal: R$ %.2f", pedido.getTotal()));
                showWelcomePanel();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar pedido: " + ex.getMessage());
            }
        });
        
        cancelarBtn.addActionListener(e -> showWelcomePanel());
        
        buttonPanel.add(salvarBtn);
        buttonPanel.add(cancelarBtn);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(itensPanel, BorderLayout.CENTER);
        
        cadastroPanel.add(titleLabel, BorderLayout.NORTH);
        cadastroPanel.add(centerPanel, BorderLayout.CENTER);
        cadastroPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        contentPanel.add(cadastroPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showMeusPedidosPanel() {
        contentPanel.removeAll();
        
        JPanel meusPanel = new JPanel(new BorderLayout());
        meusPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("🛍️ MEUS PEDIDOS", SwingConstants.CENTER);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Minhas Estatísticas"));
        
        JLabel totalPedidosLabel = new JLabel("0", SwingConstants.CENTER);
        JLabel pedidosEntreguesLabel = new JLabel("0", SwingConstants.CENTER);
        JLabel faturamentoLabel = new JLabel("R$ 0,00", SwingConstants.CENTER);
        JLabel ticketMedioLabel = new JLabel("R$ 0,00", SwingConstants.CENTER);
        
        totalPedidosLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        pedidosEntreguesLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        faturamentoLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        ticketMedioLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.add(new JLabel("Total de Pedidos", SwingConstants.CENTER), BorderLayout.NORTH);
        totalPanel.add(totalPedidosLabel, BorderLayout.CENTER);
        
        JPanel entreguesPanel = new JPanel(new BorderLayout());
        entreguesPanel.add(new JLabel("Pedidos Entregues", SwingConstants.CENTER), BorderLayout.NORTH);
        entreguesPanel.add(pedidosEntreguesLabel, BorderLayout.CENTER);
        
        JPanel faturamentoPanel = new JPanel(new BorderLayout());
        faturamentoPanel.add(new JLabel("Faturamento Total", SwingConstants.CENTER), BorderLayout.NORTH);
        faturamentoPanel.add(faturamentoLabel, BorderLayout.CENTER);
        
        JPanel ticketPanel = new JPanel(new BorderLayout());
        ticketPanel.add(new JLabel("Ticket Médio", SwingConstants.CENTER), BorderLayout.NORTH);
        ticketPanel.add(ticketMedioLabel, BorderLayout.CENTER);
        
        statsPanel.add(totalPanel);
        statsPanel.add(entreguesPanel);
        statsPanel.add(faturamentoPanel);
        statsPanel.add(ticketPanel);
        
        String[] columnNames = {"ID", "Cliente", "Data", "Status", "Valor Total"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable pedidosTable = new JTable(tableModel);
        pedidosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(pedidosTable);
        
        Runnable carregarDados = () -> {
            try {
                if (!(usuarioLogado instanceof Vendedor)) {
                    JOptionPane.showMessageDialog(this, "Esta funcionalidade é apenas para vendedores");
                    return;
                }
                
                PedidoService pedidoService = new PedidoService();
                List<Pedido> meusPedidos = pedidoService.listarPedidosPorVendedor((Vendedor) usuarioLogado);
                
                tableModel.setRowCount(0);
                for (Pedido pedido : meusPedidos) {
                    Object[] row = {
                        pedido.getId(),
                        pedido.getNomeCliente(),
                        pedido.getDataHora().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                        pedido.getStatus(),
                        String.format("R$ %.2f", pedido.getTotal())
                    };
                    tableModel.addRow(row);
                }
                
                int totalPedidos = meusPedidos.size();
                long pedidosEntregues = meusPedidos.stream()
                    .filter(p -> p.getStatus() == Pedido.StatusPedido.ENTREGUE)
                    .count();
                
                double faturamentoTotal = meusPedidos.stream()
                    .filter(p -> p.getStatus() == Pedido.StatusPedido.ENTREGUE)
                    .mapToDouble(Pedido::getTotal)
                    .sum();
                
                double ticketMedio = pedidosEntregues > 0 ? faturamentoTotal / pedidosEntregues : 0.0;
                
                totalPedidosLabel.setText(String.valueOf(totalPedidos));
                pedidosEntreguesLabel.setText(String.valueOf(pedidosEntregues));
                faturamentoLabel.setText(String.format("R$ %.2f", faturamentoTotal));
                ticketMedioLabel.setText(String.format("R$ %.2f", ticketMedio));
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(MainInterface.this, "Erro ao carregar dados: " + e.getMessage());
            }
        };
        
        carregarDados.run();
        
        JPanel actionsPanel = new JPanel(new FlowLayout());
        JButton atualizarBtn = new JButton("Atualizar");
        JButton novoBtn = new JButton("Novo Pedido");
        
        atualizarBtn.addActionListener(e -> carregarDados.run());
        novoBtn.addActionListener(e -> showCadastroPedidoPanel());
        
        actionsPanel.add(atualizarBtn);
        actionsPanel.add(novoBtn);
        
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Detalhes do Pedido Selecionado"));
        
        JTextArea detailsArea = new JTextArea(6, 40);
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        JScrollPane detailsScrollPane = new JScrollPane(detailsArea);
        detailsPanel.add(detailsScrollPane);
        
        pedidosTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = pedidosTable.getSelectedRow();
                if (selectedRow >= 0) {
                    try {
                        Long pedidoId = (Long) tableModel.getValueAt(selectedRow, 0);
                        PedidoService pedidoService = new PedidoService();
                        Pedido pedido = pedidoService.buscarPorId(pedidoId);
                        
                        StringBuilder details = new StringBuilder();
                        details.append("PEDIDO #").append(pedido.getId()).append(" - ").append(pedido.getStatus()).append("\n");
                        details.append("Cliente: ").append(pedido.getNomeCliente()).append("\n");
                        details.append("Data: ").append(pedido.getDataHora().format(
                            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");
                        details.append("Forma Pagamento: ").append(pedido.getFormaPagamento()).append("\n");
                        details.append("Entrega: ").append(pedido.getModalidadeEntrega()).append("\n\n");
                        
                        details.append("ITENS:\n");
                        for (ItemPedido item : pedido.getItens()) {
                            details.append(String.format("• %s (Qtd: %d) - R$ %.2f\n", 
                                item.getProduto().getNome(), item.getQuantidade(), item.getSubtotal()));
                        }
                        
                        details.append(String.format("\nTOTAL: R$ %.2f", pedido.getTotal()));
                        
                        detailsArea.setText(details.toString());
                        detailsArea.setCaretPosition(0);
                        
                    } catch (Exception ex) {
                        detailsArea.setText("Erro ao carregar detalhes: " + ex.getMessage());
                    }
                }
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton voltarBtn = new JButton("Voltar");
        voltarBtn.addActionListener(e -> showWelcomePanel());
        buttonPanel.add(voltarBtn);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(statsPanel, BorderLayout.CENTER);
        topPanel.add(actionsPanel, BorderLayout.SOUTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(detailsPanel, BorderLayout.SOUTH);
        
        meusPanel.add(topPanel, BorderLayout.NORTH);
        meusPanel.add(centerPanel, BorderLayout.CENTER);
        meusPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        contentPanel.add(meusPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showRelatoriosConsolidadosPanel() {
        contentPanel.removeAll();
        
        JPanel relatoriosPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        relatoriosPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel franquiasPanel = createRelatorioPanel("📊 Relatório de Franquias", 
            getFranquiasRelatoriο());
        
        JPanel usuariosPanel = createRelatorioPanel("👥 Relatório de Usuários", 
            getUsuariosRelatorio());
        
        JPanel produtosPanel = createRelatorioPanel("📦 Relatório de Produtos", 
            getProdutosRelatorio());
        
        JPanel vendedoresPanel = createRelatorioPanel("🏆 Ranking de Vendedores", 
            getVendedoresRelatorio());
        
        relatoriosPanel.add(franquiasPanel);
        relatoriosPanel.add(usuariosPanel);
        relatoriosPanel.add(produtosPanel);
        relatoriosPanel.add(vendedoresPanel);
        
        JLabel titleLabel = new JLabel("📈 RELATÓRIOS CONSOLIDADOS DO SISTEMA", SwingConstants.CENTER);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton voltarBtn = new JButton("Voltar");
        JButton atualizarBtn = new JButton("Atualizar Dados");
        
        voltarBtn.addActionListener(e -> showDashboard());
        atualizarBtn.addActionListener(e -> showRelatoriosConsolidadosPanel());
        
        buttonPanel.add(atualizarBtn);
        buttonPanel.add(voltarBtn);
        
        contentPanel.add(titleLabel, BorderLayout.NORTH);
        contentPanel.add(relatoriosPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private JPanel createRelatorioPanel(String titulo, String conteudo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), titulo));
        
        JTextArea textArea = new JTextArea(conteudo);
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textArea.setBackground(new Color(248, 248, 248));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(300, 200));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    private String getFranquiasRelatoriο() {
        try {
            List<Franquia> franquias = franquiaService.listarFranquias();
            StringBuilder sb = new StringBuilder();
            
            sb.append("TOTAL DE FRANQUIAS: ").append(franquias.size()).append("\n\n");
            
            for (Franquia f : franquias) {
                sb.append("• ").append(f.getNome()).append("\n");
                sb.append("  Endereço: ").append(f.getEndereco()).append("\n");
                sb.append("  Gerente: ").append(f.getGerente() != null ? f.getGerente().getNome() : "Não atribuído").append("\n\n");
            }
            
            return sb.toString();
        } catch (Exception e) {
            return "Erro ao carregar dados das franquias: " + e.getMessage();
        }
    }
    
    private String getUsuariosRelatorio() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        StringBuilder sb = new StringBuilder();
        
        long donos = usuarios.stream().filter(u -> u instanceof Dono).count();
        long gerentes = usuarios.stream().filter(u -> u instanceof Gerente).count();
        long vendedores = usuarios.stream().filter(u -> u instanceof Vendedor).count();
        
        sb.append("TOTAL DE USUÁRIOS: ").append(usuarios.size()).append("\n\n");
        sb.append("👑 Donos: ").append(donos).append("\n");
        sb.append("🧑‍💼 Gerentes: ").append(gerentes).append("\n");
        sb.append("🧑‍💼 Vendedores: ").append(vendedores).append("\n\n");
        
        sb.append("DISTRIBUIÇÃO:\n");
        sb.append("Donos: ").append(String.format("%.1f%%", (donos * 100.0 / usuarios.size()))).append("\n");
        sb.append("Gerentes: ").append(String.format("%.1f%%", (gerentes * 100.0 / usuarios.size()))).append("\n");
        sb.append("Vendedores: ").append(String.format("%.1f%%", (vendedores * 100.0 / usuarios.size()))).append("\n");
        
        return sb.toString();
    }
    
    private String getProdutosRelatorio() {
        List<Produto> produtos = produtoService.listarProdutos();
        List<Produto> estoqueBaixo = produtoService.listarProdutosEstoqueBaixo();
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("TOTAL DE PRODUTOS: ").append(produtos.size()).append("\n\n");
        sb.append("⚠️ Estoque Baixo: ").append(estoqueBaixo.size()).append("\n");
        sb.append("✅ Estoque OK: ").append(produtos.size() - estoqueBaixo.size()).append("\n\n");
        
        double valorTotal = produtos.stream()
                .mapToDouble(p -> p.getPreco() * p.getEstoque())
                .sum();
        
        sb.append("💰 Valor Total Estoque: R$ ").append(String.format("%.2f", valorTotal)).append("\n\n");
        
        sb.append("PRODUTOS POR CATEGORIA:\n");
        produtos.stream()
                .collect(java.util.stream.Collectors.groupingBy(Produto::getCategoria))
                .forEach((categoria, lista) -> {
                    sb.append("• ").append(categoria).append(": ").append(lista.size()).append("\n");
                });
        
        return sb.toString();
    }
    
    private String getVendedoresRelatorio() {
        List<Vendedor> vendedores = usuarioService.listarVendedores();
        
        vendedores.sort((v1, v2) -> Double.compare(v2.getTotalVendas(), v1.getTotalVendas()));
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("TOTAL DE VENDEDORES: ").append(vendedores.size()).append("\n\n");
        
        sb.append("🏆 TOP 3 VENDEDORES:\n");
        for (int i = 0; i < Math.min(3, vendedores.size()); i++) {
            Vendedor v = vendedores.get(i);
            String emoji = i == 0 ? "🥇" : i == 1 ? "🥈" : "🥉";
            sb.append(emoji).append(" ").append(v.getNome()).append("\n");
            sb.append("   Vendas: R$ ").append(String.format("%.2f", v.getTotalVendas())).append("\n");
            sb.append("   Pedidos: ").append(v.getQuantidadeVendas()).append("\n\n");
        }
        
        double totalVendas = vendedores.stream().mapToDouble(Vendedor::getTotalVendas).sum();
        int totalPedidos = vendedores.stream().mapToInt(Vendedor::getQuantidadeVendas).sum();
        
        sb.append("ESTATÍSTICAS GERAIS:\n");
        sb.append("💰 Total Vendido: R$ ").append(String.format("%.2f", totalVendas)).append("\n");
        sb.append("📦 Total Pedidos: ").append(totalPedidos).append("\n");
        
        if (!vendedores.isEmpty()) {
            double ticketMedio = totalVendas / Math.max(1, totalPedidos);
            sb.append("🎯 Ticket Médio: R$ ").append(String.format("%.2f", ticketMedio)).append("\n");
        }
        
        return sb.toString();
    }
    
    private void logout() {
        usuarioService.logout();
        this.dispose();
        
        SwingUtilities.invokeLater(() -> {
            new LoginPainel().setVisible(true);
        });
    }
    
    private void showDashboard() {
        showWelcomePanel();
    }
    
    private void editarGerente(Long gerenteId) {
        try {
            Usuario usuario = usuarioService.buscarUsuarioPorId(gerenteId);
            if (usuario instanceof Gerente) {
                Gerente gerente = (Gerente) usuario;
                
                String novoNome = JOptionPane.showInputDialog(this, "Nome:", gerente.getNome());
                if (novoNome != null && !novoNome.trim().isEmpty()) {
                    String novoEmail = JOptionPane.showInputDialog(this, "Email:", gerente.getEmail());
                    if (novoEmail != null && !novoEmail.trim().isEmpty()) {
                        usuarioService.editarUsuario(gerenteId, novoNome.trim(), novoEmail.trim());
                        JOptionPane.showMessageDialog(this, "Gerente editado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        showListaGerentesPanel();
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao editar gerente: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editarVendedor(Long vendedorId) {
        try {
            Usuario usuario = usuarioService.buscarUsuarioPorId(vendedorId);
            if (usuario instanceof Vendedor) {
                Vendedor vendedor = (Vendedor) usuario;
                
                String novoNome = JOptionPane.showInputDialog(this, "Nome:", vendedor.getNome());
                if (novoNome != null && !novoNome.trim().isEmpty()) {
                    String novoEmail = JOptionPane.showInputDialog(this, "Email:", vendedor.getEmail());
                    if (novoEmail != null && !novoEmail.trim().isEmpty()) {
                        usuarioService.editarUsuario(vendedorId, novoNome.trim(), novoEmail.trim());
                        JOptionPane.showMessageDialog(this, "Vendedor editado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        showListaVendedoresPanel();
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao editar vendedor: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editarProduto(Long produtoId) {
        try {
            Produto produto = produtoService.buscarProdutoPorId(produtoId);
            if (produto != null) {
                JDialog dialog = new JDialog(this, "Editar Produto", true);
                dialog.setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.anchor = GridBagConstraints.WEST;
                
                JTextField nomeField = new JTextField(produto.getNome(), 20);
                JTextArea descricaoArea = new JTextArea(produto.getDescricao(), 3, 20);
                descricaoArea.setBorder(BorderFactory.createLoweredBevelBorder());
                JTextField precoField = new JTextField(String.valueOf(produto.getPreco()), 20);
                JTextField estoqueField = new JTextField(String.valueOf(produto.getEstoque()), 20);
                JTextField estoqueMinField = new JTextField(String.valueOf(produto.getEstoqueMinimo()), 20);
                JTextField categoriaField = new JTextField(produto.getCategoria(), 20);
                
                gbc.gridx = 0; gbc.gridy = 0;
                dialog.add(new JLabel("Nome:"), gbc);
                gbc.gridx = 1;
                dialog.add(nomeField, gbc);
                
                gbc.gridx = 0; gbc.gridy = 1;
                dialog.add(new JLabel("Descrição:"), gbc);
                gbc.gridx = 1;
                dialog.add(new JScrollPane(descricaoArea), gbc);
                
                gbc.gridx = 0; gbc.gridy = 2;
                dialog.add(new JLabel("Preço:"), gbc);
                gbc.gridx = 1;
                dialog.add(precoField, gbc);
                
                gbc.gridx = 0; gbc.gridy = 3;
                dialog.add(new JLabel("Estoque:"), gbc);
                gbc.gridx = 1;
                dialog.add(estoqueField, gbc);
                
                gbc.gridx = 0; gbc.gridy = 4;
                dialog.add(new JLabel("Estoque Mín:"), gbc);
                gbc.gridx = 1;
                dialog.add(estoqueMinField, gbc);
                
                gbc.gridx = 0; gbc.gridy = 5;
                dialog.add(new JLabel("Categoria:"), gbc);
                gbc.gridx = 1;
                dialog.add(categoriaField, gbc);
                
                JPanel buttonPanel = new JPanel(new FlowLayout());
                JButton salvarBtn = new JButton("Salvar");
                JButton cancelarBtn = new JButton("Cancelar");
                
                salvarBtn.addActionListener(e -> {
                    try {
                        String nome = nomeField.getText().trim();
                        String descricao = descricaoArea.getText().trim();
                        double preco = Double.parseDouble(precoField.getText().replace(",", "."));
                        int estoque = Integer.parseInt(estoqueField.getText());
                        int estoqueMin = Integer.parseInt(estoqueMinField.getText());
                        String categoria = categoriaField.getText().trim();
                        
                        produtoService.editarProduto(produtoId, nome, descricao, preco, estoque, estoqueMin, categoria);
                        JOptionPane.showMessageDialog(dialog, "Produto editado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        showListaProdutosPanel();
                        
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog, "Verifique se os valores numéricos estão corretos", "Erro", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(dialog, "Erro ao editar produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                });
                
                cancelarBtn.addActionListener(e -> dialog.dispose());
                
                buttonPanel.add(salvarBtn);
                buttonPanel.add(cancelarBtn);
                
                gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
                dialog.add(buttonPanel, gbc);
                
                dialog.pack();
                dialog.setLocationRelativeTo(this);
                dialog.setVisible(true);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            UsuarioService service = new UsuarioService();
            try {
                Usuario usuario = service.login("admin@franquia.com", "admin123");
                new MainInterface(usuario, service).setVisible(true);
            } catch (Exception e) {
                new LoginPainel().setVisible(true);
            }
        });
    }
    
    private void editarGerenteFranquia(Long franquiaId, String nomeFranquia) {
        contentPanel.removeAll();
        
        Franquia franquia = franquiaService.buscarFranquiaPorId(franquiaId);
        if (franquia == null) {
            JOptionPane.showMessageDialog(this, "Franquia não encontrada!", "Erro", JOptionPane.ERROR_MESSAGE);
            showListaFranquiasPanel();
            return;
        }
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        
        JLabel titleLabel = new JLabel("Editar Gerente - " + nomeFranquia);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);
        gbc.gridwidth = 1;
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("ID da Franquia:"), gbc);
        JLabel idLabel = new JLabel(franquia.getId().toString());
        idLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        gbc.gridx = 1;
        formPanel.add(idLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Local:"), gbc);
        JLabel localLabel = new JLabel(franquia.getCidade() + "/" + franquia.getEstado());
        gbc.gridx = 1;
        formPanel.add(localLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Vendedores:"), gbc);
        JLabel vendedoresLabel = new JLabel(String.valueOf(franquia.getVendedores().size()));
        gbc.gridx = 1;
        formPanel.add(vendedoresLabel, gbc);
        
        JSeparator separador = new JSeparator();
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(separador, gbc);
        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Gerente Atual:"), gbc);
        String gerenteAtualTexto = franquia.getGerente() != null ? 
            franquia.getGerente().getNome() + " (" + franquia.getGerente().getEmail() + ")" : 
            "Nenhum gerente atribuído";
        JLabel gerenteAtualLabel = new JLabel(gerenteAtualTexto);
        gerenteAtualLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        if (franquia.getGerente() == null) {
            gerenteAtualLabel.setForeground(Color.RED);
        }
        gbc.gridx = 1;
        formPanel.add(gerenteAtualLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Alterar para:"), gbc);
        
        JComboBox<Gerente> novoGerenteCombo = new JComboBox<>();
        novoGerenteCombo.addItem(null); 
        
        List<Gerente> gerentesDisponiveis = usuarioService.listarGerentesDisponiveis();
        gerentesDisponiveis.forEach(novoGerenteCombo::addItem);
        
        if (franquia.getGerente() != null) {
            novoGerenteCombo.addItem(franquia.getGerente());
        }
        
        novoGerenteCombo.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(javax.swing.JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("🚫 Remover gerente atual");
                    setForeground(Color.RED);
                } else if (value instanceof Gerente) {
                    Gerente gerente = (Gerente) value;
                    if (gerente.equals(franquia.getGerente())) {
                        setText("✓ " + gerente.getNome() + " (ATUAL - manter)");
                        setForeground(new Color(0, 100, 0));
                    } else {
                        setText("👤 " + gerente.getNome() + " (" + gerente.getEmail() + ")");
                        setForeground(Color.BLUE);
                    }
                }
                return this;
            }
        });
        
        gbc.gridx = 1;
        formPanel.add(novoGerenteCombo, gbc);
        
        if (gerentesDisponiveis.isEmpty() && franquia.getGerente() == null) {
            JLabel avisoLabel = new JLabel("<html><font color='orange'>⚠️ Nenhum gerente disponível!<br>Cadastre gerentes sem franquia primeiro.</font></html>");
            gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
            formPanel.add(avisoLabel, gbc);
            gbc.gridwidth = 1;
        }
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton salvarBtn = new JButton("💾 Salvar Alterações");
        JButton cancelarBtn = new JButton("❌ Cancelar");
        
        salvarBtn.addActionListener(ev -> {
            try {
                Gerente novoGerente = (Gerente) novoGerenteCombo.getSelectedItem();
                Gerente gerenteAnterior = franquia.getGerente();
                
                boolean mesmoGerente = (gerenteAnterior == null && novoGerente == null) ||
                                     (gerenteAnterior != null && gerenteAnterior.equals(novoGerente));
                
                if (mesmoGerente) {
                    JOptionPane.showMessageDialog(this, "Nenhuma alteração foi feita.", "Informação", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                
                franquia.setGerente(novoGerente);
                
                if (gerenteAnterior != null) {
                    gerenteAnterior.setFranquiaId(null);
                    usuarioService.atualizarFranquiaIdGerente(gerenteAnterior.getId(), null);
                }
                
                if (novoGerente != null) {
                    novoGerente.setFranquiaId(franquia.getId());
                    usuarioService.atualizarFranquiaIdGerente(novoGerente.getId(), franquia.getId());
                }
                
                franquiaService.atualizarFranquia(franquia);
                
                String mensagem = "✅ Gerente da franquia \"" + nomeFranquia + "\" atualizado!\n\n";
                if (gerenteAnterior != null && novoGerente == null) {
                    mensagem += "📤 Gerente removido: " + gerenteAnterior.getNome() + "\n(Agora disponível para outras franquias)";
                } else if (gerenteAnterior == null && novoGerente != null) {
                    mensagem += "📥 Gerente atribuído: " + novoGerente.getNome();
                } else if (gerenteAnterior != null && novoGerente != null) {
                    mensagem += "🔄 Gerente alterado:\n";
                    mensagem += "• Anterior: " + gerenteAnterior.getNome() + " (liberado)\n";
                    mensagem += "• Novo: " + novoGerente.getNome() + " (atribuído)";
                }
                
                JOptionPane.showMessageDialog(this, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                showListaFranquiasPanel();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Erro ao salvar alterações: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        
        cancelarBtn.addActionListener(ev -> showListaFranquiasPanel());
        
        buttonPanel.add(salvarBtn);
        buttonPanel.add(cancelarBtn);
        
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);
        
        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void mostrarDetalhesFranquia(Long franquiaId) {
        Franquia franquia = franquiaService.buscarFranquiaPorId(franquiaId);
        if (franquia == null) {
            JOptionPane.showMessageDialog(this, "Franquia não encontrada!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        StringBuilder detalhes = new StringBuilder();
        detalhes.append("📋 DETALHES DA FRANQUIA\n");
        detalhes.append("═══════════════════════════════\n\n");
        detalhes.append("🏢 Nome: ").append(franquia.getNome()).append("\n");
        detalhes.append("🆔 ID: ").append(franquia.getId()).append("\n");
        detalhes.append("📍 Endereço: ").append(franquia.getEndereco()).append("\n");
        detalhes.append("🏙️ Cidade: ").append(franquia.getCidade()).append("\n");
        detalhes.append("🗺️ Estado: ").append(franquia.getEstado()).append("\n");
        detalhes.append("📮 CEP: ").append(franquia.getCep()).append("\n\n");
        
        detalhes.append("👥 GERENTE\n");
        detalhes.append("─────────────────────────\n");
        if (franquia.getGerente() != null) {
            detalhes.append("👤 Nome: ").append(franquia.getGerente().getNome()).append("\n");
            detalhes.append("📧 Email: ").append(franquia.getGerente().getEmail()).append("\n");
        } else {
            detalhes.append("❌ Nenhum gerente atribuído\n");
        }
        detalhes.append("\n");
        
        detalhes.append("🛒 VENDEDORES (").append(franquia.getVendedores().size()).append(")\n");
        detalhes.append("─────────────────────────\n");
        if (franquia.getVendedores().isEmpty()) {
            detalhes.append("❌ Nenhum vendedor cadastrado\n");
        } else {
            for (Vendedor v : franquia.getVendedores()) {
                detalhes.append("• ").append(v.getNome());
                detalhes.append(" (Vendas: R$ ").append(String.format("%.2f", v.getTotalVendas())).append(")\n");
            }
        }
        detalhes.append("\n");
        
        detalhes.append("💰 INFORMAÇÕES FINANCEIRAS\n");
        detalhes.append("─────────────────────────\n");
        detalhes.append("💵 Receita Acumulada: R$ ").append(String.format("%.2f", franquia.getReceitaAcumulada())).append("\n");
        
        double totalVendasVendedores = franquia.getVendedores().stream()
            .mapToDouble(Vendedor::getTotalVendas)
            .sum();
        detalhes.append("📊 Total Vendas Vendedores: R$ ").append(String.format("%.2f", totalVendasVendedores)).append("\n");
        
        JTextArea textArea = new JTextArea(detalhes.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Detalhes da Franquia - " + franquia.getNome(), JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void resetarDadosSistema() {
        int confirmacao = JOptionPane.showConfirmDialog(
            this,
            "ATENÇÃO: Esta ação irá deletar TODOS os dados do sistema!\n\n" +
            "Isso inclui:\n" +
            "• Todos os usuários (exceto admin padrão)\n" +
            "• Todas as franquias\n" +
            "• Todos os produtos\n" +
            "• Todos os pedidos\n\n" +
            "Esta ação NÃO PODE ser desfeita!\n\n" +
            "Deseja realmente continuar?",
            "Confirmar Reset do Sistema",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            String confirmacao_texto = JOptionPane.showInputDialog(
                this,
                "Digite 'CONFIRMAR' (em maiúsculas) para resetar o sistema:",
                "Confirmação Final",
                JOptionPane.PLAIN_MESSAGE
            );
            
            if ("CONFIRMAR".equals(confirmacao_texto)) {
                try {
                    java.io.File usuarios = new java.io.File("usuarios.dat");
                    java.io.File franquias = new java.io.File("franquias.dat");
                    java.io.File produtos = new java.io.File("produtos.dat");
                    java.io.File pedidos = new java.io.File("pedidos.dat");
                    
                    usuarios.delete();
                    franquias.delete();
                    produtos.delete();
                    pedidos.delete();
                    
                    JOptionPane.showMessageDialog(
                        this,
                        "Sistema resetado com sucesso!\n\n" +
                        "O sistema será reiniciado.\n" +
                        "Use as credenciais padrão:\n" +
                        "Email: admin@franquia.com\n" +
                        "Senha: admin123",
                        "Reset Concluído",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    
                    this.dispose();
                    new LoginPainel().setVisible(true);
                    
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Erro ao resetar sistema: " + e.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Reset cancelado - confirmação incorreta.",
                    "Cancelado",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        }
    }
    
    private void mostrarSobre() {
        String sobre = "Sistema de Gestão de Franquias\n\n" +
                      "Desenvolvido por:\n" +
                      "Sara Ingrid\n" +
                      "Angélica\n\n" +
                      "Universidade Federal de Juiz de Fora\n" +
                      "DCC025 - Programação Orientada a Objetos";
        
        JOptionPane.showMessageDialog(this, sobre, "Sobre", JOptionPane.INFORMATION_MESSAGE);
    }
}