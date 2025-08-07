/*
 * Autores: Sara Ingrid - 202365056A, Angélica Coutinho - 202365064A
 */
package br.ufjf.dcc025.trabalho.franquias;

import br.ufjf.dcc025.trabalho.franquias.view.LoginPainel;
import br.ufjf.dcc025.trabalho.franquias.test.UsuarioServiceTest;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    
    public static void main(String[] args) {
        // Verifica se deve executar testes
        if (args.length > 0 && "--test".equals(args[0])) {
            executarTestes();
            return;
        }
        
        // Verifica se deve mostrar ajuda
        if (args.length > 0 && "--help".equals(args[0])) {
            mostrarAjuda();
            return;
        }
        
        configurarLookAndFeel();
        
        // Iniciar aplicação GUI
        SwingUtilities.invokeLater(() -> {
            iniciarAplicacao();
        });
    }
    
    private static void configurarLookAndFeel() {
        try {
            // Tentar usar Nimbus se disponível
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    return;
                }
            }
            // Se Nimbus não estiver disponível, usar o padrão
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            System.err.println("Erro ao configurar Look and Feel: " + e.getMessage());
            // Continuar com o Look and Feel padrão
        }
    }
    
    private static void iniciarAplicacao() {
        try {
            System.out.println("=================================");
            System.out.println("SISTEMA DE GERENCIAMENTO DE FRANQUIAS");
            System.out.println("=================================");
            System.out.println("Autores: Sara Ingrid - 202376049");
            System.out.println("         Angélica Coutinho - 202376046");
            System.out.println("=================================");
            System.out.println("Iniciando aplicação...");
            System.out.println();
            
            LoginPainel loginPanel = new LoginPainel();
            loginPanel.setVisible(true);
            
            System.out.println("Tela de login aberta.");
            System.out.println("Use as credenciais padrão:");
            System.out.println("Email: admin@franquia.com");
            System.out.println("Senha: admin123");
            
        } catch (Exception e) {
            System.err.println("Erro ao iniciar aplicação: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private static void executarTestes() {
        System.out.println("=================================");
        System.out.println("MODO DE TESTE");
        System.out.println("=================================");
        
        try {
            UsuarioServiceTest usuarioTest = new UsuarioServiceTest();
            usuarioTest.executarTodosTestes();
            
            System.out.println("\nTodos os testes foram executados.");
            
        } catch (Exception e) {
            System.err.println("Erro durante execução dos testes: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void mostrarAjuda() {
        System.out.println("Sistema de Gerenciamento de Franquias");
        System.out.println("=====================================");
        System.out.println();
        System.out.println("Uso:");
        System.out.println("  java -jar franquias.jar           # Inicia a aplicação GUI");
        System.out.println("  java -jar franquias.jar --test    # Executa testes de unidade");
        System.out.println("  java -jar franquias.jar --help    # Mostra esta ajuda");
        System.out.println();
        System.out.println("Funcionalidades implementadas:");
        System.out.println("• Três tipos de usuários: Dono, Gerente, Vendedor");
        System.out.println("• Sistema de login e autenticação");
        System.out.println("• Gestão de franquias, produtos e pedidos");
        System.out.println("• Interface gráfica com Swing");
        System.out.println("• Persistência de dados em arquivos");
        System.out.println("• Validação de dados de entrada");
        System.out.println("• Tratamento de exceções");
        System.out.println("• Testes de unidade");
        System.out.println();
        System.out.println("Conceitos de OO implementados:");
        System.out.println("• Herança (Usuario -> Dono, Gerente, Vendedor)");
        System.out.println("• Polimorfismo (getTipoUsuario())");
        System.out.println("• Abstração (classe Usuario abstrata)");
        System.out.println("• Interfaces (OperacoesCRUD)");
        System.out.println("• Encapsulamento (getters/setters)");
        System.out.println("• Coleções (List, Map)");
        System.out.println("• Exceções personalizadas");
        System.out.println();
        System.out.println("Credenciais padrão:");
        System.out.println("Email: admin@franquia.com");
        System.out.println("Senha: admin123");
    }
}