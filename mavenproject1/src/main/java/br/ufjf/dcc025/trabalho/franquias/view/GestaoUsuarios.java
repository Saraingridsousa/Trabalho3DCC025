package br.ufjf.dcc025.trabalho.franquias.view;

import br.ufjf.dcc025.trabalho.franquias.model.usuarios.Usuario;
import br.ufjf.dcc025.trabalho.franquias.model.usuarios.Dono;
import br.ufjf.dcc025.trabalho.franquias.model.usuarios.Gerente;
import br.ufjf.dcc025.trabalho.franquias.model.usuarios.Vendedor;
import br.ufjf.dcc025.trabalho.franquias.service.UsuarioService;
import br.ufjf.dcc025.trabalho.franquias.exceptions.ValidacaoException;
import br.ufjf.dcc025.trabalho.franquias.exceptions.EntidadeNaoEncontradaException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GestaoUsuarios extends JFrame {
    private final UsuarioService usuarioService;
    private JTable tableUsuarios;
    private DefaultTableModel tableModel;
    private JTextField txtNome, txtEmail, txtSenha;
    private JComboBox<String> cbTipoUsuario;
    private JButton btnSalvar, btnEditar, btnExcluir, btnLimpar;
    private Long usuarioEditandoId = null;
    
    public GestaoUsuarios() {
        this.usuarioService = new UsuarioService();
        initComponents();
        carregarUsuarios();
    }
    
    private void initComponents() {
        setTitle("Gestão de Usuários");
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
        panel.setBorder(BorderFactory.createTitledBorder("Cadastro de Usuários"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nome
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nome:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNome = new JTextField(20);
        panel.add(txtNome, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtEmail = new JTextField(20);
        panel.add(txtEmail, gbc);
        
        // Senha
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Senha:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtSenha = new JPasswordField(20);
        panel.add(txtSenha, gbc);
        
        // Tipo de usuário
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Tipo:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        cbTipoUsuario = new JComboBox<>(new String[]{"Dono", "Gerente", "Vendedor"});
        panel.add(cbTipoUsuario, gbc);
        
        // Botões
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvarUsuario());
        buttonPanel.add(btnSalvar);
        
        btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> editarUsuarioSelecionado());
        btnEditar.setEnabled(false);
        buttonPanel.add(btnEditar);
        
        btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(e -> excluirUsuarioSelecionado());
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
        panel.setBorder(BorderFactory.createTitledBorder("Lista de Usuários"));
        
        // Modelo da tabela
        String[] colunas = {"ID", "Nome", "Email", "Tipo"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableUsuarios = new JTable(tableModel);
        tableUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableUsuarios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableUsuarios.getSelectedRow();
                btnEditar.setEnabled(selectedRow >= 0);
                btnExcluir.setEnabled(selectedRow >= 0);
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableUsuarios);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de filtros
        JPanel filterPanel = new JPanel(new FlowLayout());
        JButton btnTodos = new JButton("Todos");
        JButton btnDonos = new JButton("Donos");
        JButton btnGerentes = new JButton("Gerentes");
        JButton btnVendedores = new JButton("Vendedores");
        
        btnTodos.addActionListener(e -> carregarUsuarios());
        btnDonos.addActionListener(e -> {
            List<Dono> donos = usuarioService.listarDonos();
            atualizarTabela(donos.toArray(Usuario[]::new));
        });
        btnGerentes.addActionListener(e -> {
            List<Gerente> gerentes = usuarioService.listarGerentes();
            atualizarTabela(gerentes.toArray(Usuario[]::new));
        });
        btnVendedores.addActionListener(e -> {
            List<Vendedor> vendedores = usuarioService.listarVendedores();
            atualizarTabela(vendedores.toArray(Usuario[]::new));
        });
        
        filterPanel.add(new JLabel("Filtrar por tipo:"));
        filterPanel.add(btnTodos);
        filterPanel.add(btnDonos);
        filterPanel.add(btnGerentes);
        filterPanel.add(btnVendedores);
        
        panel.add(filterPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void salvarUsuario() {
        try {
            String nome = txtNome.getText().trim();
            String email = txtEmail.getText().trim();
            String senha = txtSenha.getText().trim();
            String tipo = (String) cbTipoUsuario.getSelectedItem();
            
            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos");
                return;
            }
            
            Usuario usuario;
            switch (tipo) {
                case "Dono" -> usuario = new Dono(nome, email, senha);
                case "Gerente" -> usuario = new Gerente(nome, email, senha);
                case "Vendedor" -> usuario = new Vendedor(nome, email, senha);
                default -> throw new ValidacaoException("Tipo de usuário inválido");
            }
            
            if (usuarioEditandoId == null) {
                // Criando novo usuário
                usuarioService.cadastrarUsuario(usuario);
                JOptionPane.showMessageDialog(this, "Usuário criado com sucesso!");
            } else {
                // Editando usuário existente
                usuarioService.editarUsuario(usuarioEditandoId, nome, email, senha);
                JOptionPane.showMessageDialog(this, "Usuário atualizado com sucesso!");
                usuarioEditandoId = null;
                btnSalvar.setText("Salvar");
            }
            
            limparFormulario();
            carregarUsuarios();
            
        } catch (ValidacaoException e) {
            JOptionPane.showMessageDialog(this, "Erro de validação: " + e.getMessage());
        } catch (EntidadeNaoEncontradaException e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }
    
    private void editarUsuarioSelecionado() {
        int selectedRow = tableUsuarios.getSelectedRow();
        if (selectedRow >= 0) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            Usuario usuario = usuarioService.buscarUsuarioPorId(id);
            
            if (usuario != null) {
                txtNome.setText(usuario.getNome());
                txtEmail.setText(usuario.getEmail());
                txtSenha.setText(""); // Por segurança, não mostra senha
                cbTipoUsuario.setSelectedItem(usuario.getTipoUsuario());
                
                usuarioEditandoId = id;
                btnSalvar.setText("Atualizar");
            }
        }
    }
    
    private void excluirUsuarioSelecionado() {
        int selectedRow = tableUsuarios.getSelectedRow();
        if (selectedRow >= 0) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            String nome = (String) tableModel.getValueAt(selectedRow, 1);
            
            int opcao = JOptionPane.showConfirmDialog(this,
                "Deseja realmente excluir o usuário '" + nome + "'?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION);
            
            if (opcao == JOptionPane.YES_OPTION) {
                try {
                    usuarioService.removerUsuario(id);
                    JOptionPane.showMessageDialog(this, "Usuário excluído com sucesso!");
                    carregarUsuarios();
                } catch (EntidadeNaoEncontradaException e) {
                    JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
                }
            }
        }
    }
    
    private void limparFormulario() {
        txtNome.setText("");
        txtEmail.setText("");
        txtSenha.setText("");
        cbTipoUsuario.setSelectedIndex(0);
        usuarioEditandoId = null;
        btnSalvar.setText("Salvar");
    }
    
    private void carregarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        atualizarTabela(usuarios.toArray(Usuario[]::new));
    }
    
    private void atualizarTabela(Usuario[] usuarios) {
        tableModel.setRowCount(0);
        
        for (Usuario usuario : usuarios) {
            Object[] row = {
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTipoUsuario()
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
            
            new GestaoUsuarios().setVisible(true);
        });
    }
}