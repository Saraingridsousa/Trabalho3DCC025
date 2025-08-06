package br.ufjf.dcc025.trabalho.franquias.view;

import br.ufjf.dcc025.trabalho.franquias.model.usuarios.Usuario;
import br.ufjf.dcc025.trabalho.franquias.service.UsuarioService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginPainel extends JFrame {
    private final UsuarioService usuarioService;
    private JTextField emailField;
    private JPasswordField senhaField;
    private JButton loginButton;
    
    public LoginPainel() {
        this.usuarioService = new UsuarioService();
        initComponents();
        setupLayout();
    }
    
    private void initComponents() {
        setTitle("Sistema de Franquias - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        
        emailField = new JTextField(20);
        senhaField = new JPasswordField(20);
        loginButton = new JButton("Entrar");
        
        loginButton.addActionListener((ActionEvent e) -> {
            realizarLogin();
        });
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel titleLabel = new JLabel("Sistema de Franquias");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 20, 30, 20);
        mainPanel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 20, 10, 20);
        
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        mainPanel.add(emailField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Senha:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        mainPanel.add(senhaField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 20, 20, 20);
        mainPanel.add(loginButton, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void realizarLogin() {
        String email = emailField.getText().trim();
        String senha = new String(senhaField.getPassword());
        
        if (email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Usuario usuario = usuarioService.login(email, senha);
        
        if (usuario != null) {
            JOptionPane.showMessageDialog(this, "Login realizado com sucesso!\nBem-vindo, " + usuario.getNome());
            this.dispose();
            // TODO: Abrir MainFrame quando estiver implementado
            // new MainFrame(usuarioService).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Email ou senha incorretos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginPainel().setVisible(true);
        });
    }
}