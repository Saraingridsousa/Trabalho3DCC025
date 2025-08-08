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
import java.awt.event.ActionListener;

public class LoginPainel extends javax.swing.JFrame {
    
    private UsuarioService usuarioService;
    private JTextField emailField;
    private JPasswordField senhaField;
    private JButton loginButton;
    private JLabel statusLabel;

    public LoginPainel() {
        this.usuarioService = new UsuarioService();
        initComponents();
        setupEventHandlers();
        setLocationRelativeTo(null); 
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema de Gerenciamento de Franquias - Login");
        setResizable(false);
        
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
        
        emailField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(emailField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Senha:"), gbc);
        
        senhaField = new JPasswordField(20);
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(senhaField, gbc);
        
        loginButton = new JButton("Entrar");
        loginButton.setPreferredSize(new Dimension(100, 30));
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 5, 5, 5);
        mainPanel.add(loginButton, gbc);
        
        statusLabel = new JLabel(" ");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setForeground(Color.RED);
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.insets = new Insets(10, 5, 5, 5);
        mainPanel.add(statusLabel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        pack();
    }
    
    private void setupEventHandlers() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });
        
        senhaField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });
        
        emailField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                senhaField.requestFocus();
            }
        });
    }
    
    private void realizarLogin() {
        String email = emailField.getText().trim();
        String senha = new String(senhaField.getPassword());
        
        if (email.isEmpty() || senha.isEmpty()) {
            mostrarStatus("Por favor, preencha todos os campos", Color.RED);
            return;
        }
        
        try {
            loginButton.setEnabled(false);
            mostrarStatus("Verificando credenciais...", Color.BLUE);
            
            Usuario usuario = usuarioService.login(email, senha);
            
            mostrarStatus("Login realizado com sucesso!", Color.GREEN);
            
            Timer timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    abrirTelaPrincipal(usuario);
                }
            });
            timer.setRepeats(false);
            timer.start();
            
        } catch (AcessoNegadoException e) {
            mostrarStatus("Erro: " + e.getMessage(), Color.RED);
            loginButton.setEnabled(true);
            senhaField.setText(""); 
            senhaField.requestFocus();
        } catch (Exception e) {
            mostrarStatus("Erro inesperado: " + e.getMessage(), Color.RED);
            loginButton.setEnabled(true);
        }
    }
    
    private void mostrarStatus(String mensagem, Color cor) {
        statusLabel.setText(mensagem);
        statusLabel.setForeground(cor);
    }
    
    private void abrirTelaPrincipal(Usuario usuario) {
        this.dispose(); 
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainInterface(usuario, usuarioService).setVisible(true);
            }
        });
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
            java.util.logging.Logger.getLogger(LoginPainel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginPainel().setVisible(true);
            }
        });
    }
}