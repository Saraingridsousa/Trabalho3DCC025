/*
 * Autores: Sara Ingrid - 202365056A, Angélica Coutinho - 202365064A
 */
package br.ufjf.dcc025.trabalho.franquias.test;

import br.ufjf.dcc025.trabalho.franquias.model.usuarios.*;
import br.ufjf.dcc025.trabalho.franquias.service.UsuarioService;
import br.ufjf.dcc025.trabalho.franquias.exceptions.FranquiaException;

public class UsuarioServiceTest {
    
    private UsuarioService usuarioService;
    
    public UsuarioServiceTest() {
        this.usuarioService = new UsuarioService();
    }
    
    public void testCadastrarDono() {
        System.out.println("=== Teste: Cadastrar Dono ===");
        try {
            Dono dono = new Dono("João Silva", "joao@teste.com", "123456");
            usuarioService.cadastrarUsuario(dono);
            
            assert dono != null : "Dono não deveria ser null";
            assert dono.getId() != null : "ID do dono não deveria ser null";
            assert "João Silva".equals(dono.getNome()) : "Nome incorreto";
            assert "DONO".equals(dono.getTipoUsuario()) : "Tipo de usuário incorreto";
            
            System.out.println("✓ Teste passou - Dono cadastrado com sucesso");
            System.out.println("  ID: " + dono.getId());
            System.out.println("  Nome: " + dono.getNome());
            System.out.println("  Tipo: " + dono.getTipoUsuario());
            
        } catch (Exception e) {
            System.out.println("✗ Teste falhou: " + e.getMessage());
        }
    }
    
    public void testCadastrarGerente() {
        System.out.println("\n=== Teste: Cadastrar Gerente ===");
        try {
            Gerente gerente = new Gerente("Maria Santos", "maria@teste.com", "123456");
            usuarioService.cadastrarUsuario(gerente);
            
            assert gerente != null : "Gerente não deveria ser null";
            assert gerente.getId() != null : "ID do gerente não deveria ser null";
            assert "Maria Santos".equals(gerente.getNome()) : "Nome incorreto";
            assert "GERENTE".equals(gerente.getTipoUsuario()) : "Tipo de usuário incorreto";
            assert gerente.getFranquiaId().equals(1L) : "ID da franquia incorreto";
            
            System.out.println("✓ Teste passou - Gerente cadastrado com sucesso");
            System.out.println("  ID: " + gerente.getId());
            System.out.println("  Nome: " + gerente.getNome());
            System.out.println("  Tipo: " + gerente.getTipoUsuario());
            System.out.println("  Franquia ID: " + gerente.getFranquiaId());
            
        } catch (Exception e) {
            System.out.println("✗ Teste falhou: " + e.getMessage());
        }
    }
    
    public void testCadastrarVendedor() {
        System.out.println("\n=== Teste: Cadastrar Vendedor ===");
        try {
            Vendedor vendedor = new Vendedor("Pedro Costa", "pedro@teste.com", "123456");
            usuarioService.cadastrarUsuario(vendedor);
            
            assert vendedor != null : "Vendedor não deveria ser null";
            assert vendedor.getId() != null : "ID do vendedor não deveria ser null";
            assert "Pedro Costa".equals(vendedor.getNome()) : "Nome incorreto";
            assert "VENDEDOR".equals(vendedor.getTipoUsuario()) : "Tipo de usuário incorreto";
            assert vendedor.getFranquiaId().equals(1L) : "ID da franquia incorreto";
            assert vendedor.getTotalVendas() == 0.0 : "Total de vendas deveria ser 0";
            
            System.out.println("✓ Teste passou - Vendedor cadastrado com sucesso");
            System.out.println("  ID: " + vendedor.getId());
            System.out.println("  Nome: " + vendedor.getNome());
            System.out.println("  Tipo: " + vendedor.getTipoUsuario());
            System.out.println("  Franquia ID: " + vendedor.getFranquiaId());
            
        } catch (Exception e) {
            System.out.println("✗ Teste falhou: " + e.getMessage());
        }
    }
    
    public void testValidacaoDadosInvalidos() {
        System.out.println("\n=== Teste: Validação de Dados Inválidos ===");
        
        // Teste com nome vazio
        try {
            Dono dono = new Dono("", "teste@teste.com", "123456");
            usuarioService.cadastrarUsuario(dono);
            System.out.println("✗ Teste falhou - Deveria ter lançado exceção para nome vazio");
        } catch (FranquiaException.ValidacaoException e) {
            System.out.println("✓ Teste passou - Exceção correta para nome vazio: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Teste falhou - Exceção incorreta: " + e.getMessage());
        }
        
        // Teste com email inválido
        try {
            Dono dono = new Dono("Teste", "email_invalido", "123456");
            usuarioService.cadastrarUsuario(dono);
            System.out.println("✗ Teste falhou - Deveria ter lançado exceção para email inválido");
        } catch (FranquiaException.ValidacaoException e) {
            System.out.println("✓ Teste passou - Exceção correta para email inválido: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Teste falhou - Exceção incorreta: " + e.getMessage());
        }
        
        // Teste com senha muito curta
        try {
            Dono dono = new Dono("Teste", "teste@teste.com", "123");
            usuarioService.cadastrarUsuario(dono);
            System.out.println("✗ Teste falhou - Deveria ter lançado exceção para senha curta");
        } catch (FranquiaException.ValidacaoException e) {
            System.out.println("✓ Teste passou - Exceção correta para senha curta: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Teste falhou - Exceção incorreta: " + e.getMessage());
        }
    }
    
    /**
     * Testa o login no sistema
     */
    public void testLogin() {
        System.out.println("\n=== Teste: Login ===");
        
        // Teste com credenciais válidas (usuário padrão)
        try {
            Usuario usuario = usuarioService.login("admin@franquia.com", "admin123");
            
            assert usuario != null : "Usuário não deveria ser null";
            assert usuario instanceof Dono : "Usuário deveria ser do tipo Dono";
            assert usuarioService.isLogado() : "Deveria estar logado";
            
            System.out.println("✓ Teste passou - Login realizado com sucesso");
            System.out.println("  Usuário: " + usuario.getNome());
            System.out.println("  Tipo: " + usuario.getTipoUsuario());
            
        } catch (Exception e) {
            System.out.println("✗ Teste falhou: " + e.getMessage());
        }
        
        // Teste com credenciais inválidas
        try {
            usuarioService.login("email@inexistente.com", "senha_errada");
            System.out.println("✗ Teste falhou - Deveria ter lançado exceção para credenciais inválidas");
        } catch (FranquiaException.AcessoNegadoException e) {
            System.out.println("✓ Teste passou - Exceção correta para credenciais inválidas: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Teste falhou - Exceção incorreta: " + e.getMessage());
        }
    }
    
    /**
     * Testa polimorfismo com diferentes tipos de usuário
     */
    public void testPolimorfismo() {
        System.out.println("\n=== Teste: Polimorfismo ===");
        
        try {
            // Criar usuários de diferentes tipos
            Dono dono = new Dono("Dono Teste", "dono@teste.com", "123456");
            usuarioService.cadastrarUsuario(dono);
            Gerente gerente = new Gerente("Gerente Teste", "gerente@teste.com", "123456");
            usuarioService.cadastrarUsuario(gerente);
            Vendedor vendedor = new Vendedor("Vendedor Teste", "vendedor@teste.com", "123456");
            usuarioService.cadastrarUsuario(vendedor);
            
            // Testar polimorfismo - todos são Usuarios mas comportam-se diferentemente
            Usuario[] usuarios = {dono, gerente, vendedor};
            
            System.out.println("Testando polimorfismo:");
            for (Usuario u : usuarios) {
                System.out.println("  " + u.getNome() + " é do tipo: " + u.getTipoUsuario());
                
                // Cada tipo retorna um valor diferente - polimorfismo
                assert u.getTipoUsuario() != null : "Tipo de usuário não deveria ser null";
            }
            
            System.out.println("✓ Teste passou - Polimorfismo funcionando corretamente");
            
        } catch (Exception e) {
            System.out.println("✗ Teste falhou: " + e.getMessage());
        }
    }
    
    /**
     * Executa todos os testes
     */
    public void executarTodosTestes() {
        System.out.println("=================================");
        System.out.println("EXECUTANDO TESTES DO USUARIO SERVICE");
        System.out.println("=================================");
        
        testCadastrarDono();
        testCadastrarGerente();
        testCadastrarVendedor();
        testValidacaoDadosInvalidos();
        testLogin();
        testPolimorfismo();
        
        System.out.println("\n=================================");
        System.out.println("TODOS OS TESTES CONCLUÍDOS");
        System.out.println("=================================");
    }
    
    /**
     * Método main para executar os testes
     */
    public static void main(String[] args) {
        UsuarioServiceTest teste = new UsuarioServiceTest();
        teste.executarTodosTestes();
    }
}
