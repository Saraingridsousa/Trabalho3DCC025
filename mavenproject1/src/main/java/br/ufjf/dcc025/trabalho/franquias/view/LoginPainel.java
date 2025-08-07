/*
 * Autores: Sara Ingrid - 202365056A, Angélica Coutinho - 202365064A
 */
package br.ufjf.dcc025.trabalho.franquias.view;

import br.ufjf.dcc025.trabalho.franquias.exceptions.AcessoNegadoException;
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
    private JLabel statusLabel;
    
    public LoginPainel() {
        this.usuarioService = new UsuarioService();
        initComponents();
        setupLayout();
    }
    
    private void initComponents() {
        setTitle("Sistema de Gerenciamento de Franquias - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        
        emailField = new JTextField(20);
        senhaField = new JPasswordField(20);
        loginButton = new JButton("Entrar");
        statusLabel = new JLabel(" ");
        
        loginButton.addActionListener((ActionEvent e) -> {
            try {
                realizarLogin();
            } catch (AcessoNegadoException ex) {
                System.getLogger(LoginPainel.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });
        
        senhaField.addActionListener((ActionEvent e) -> {
            try {
                realizarLogin();
            } catch (AcessoNegadoException ex) {
                System.getLogger(LoginPainel.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });
        
        emailField.addActionListener((ActionEvent e) -> {
            senhaField.requestFocus();
        });
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel titleLabel = new JLabel("Sistema de Gerenciamento de Franquias");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        mainPanel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(emailField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Senha:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(senhaField, gbc);
        
        loginButton.setPreferredSize(new Dimension(100, 30));
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 5, 5, 5);
        mainPanel.add(loginButton, gbc);
        
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setForeground(Color.RED);
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.insets = new Insets(10, 5, 5, 5);
        mainPanel.add(statusLabel, gbc);
                
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void realizarLogin() throws AcessoNegadoException {
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
            new MainInterface(usuario).setVisible(true);
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