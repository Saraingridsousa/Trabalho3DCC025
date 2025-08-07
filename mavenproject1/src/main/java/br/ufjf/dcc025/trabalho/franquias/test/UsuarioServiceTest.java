/*
 * Autores: Sara Ingrid - 202365056A, Angélica Coutinho - 202365064A
 */
package br.ufjf.dcc025.trabalho.franquias.test;

import br.ufjf.dcc025.trabalho.franquias.exceptions.AcessoNegadoException;
import br.ufjf.dcc025.trabalho.franquias.exceptions.ValidacaoException;
import br.ufjf.dcc025.trabalho.franquias.model.usuarios.*;
import br.ufjf.dcc025.trabalho.franquias.service.UsuarioService;

public class UsuarioServiceTest {
    
    private UsuarioService usuarioService;
    
    public UsuarioServiceTest() {
        this.usuarioService = new UsuarioService();
    }
    
    public void testCadastrarDono() {
        System.out.println("=== Teste: Cadastrar Dono ===");
        try {
            Dono dono = usuarioService.cadastrarDono("João Silva", "123.456.789-00", "joao@teste.com", "123456");
            
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
            Gerente gerente = usuarioService.cadastrarGerente("Maria Santos", "987.654.321-00", "maria@teste.com", "123456", 1L);
            
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
            Vendedor vendedor = usuarioService.cadastrarVendedor("Pedro Costa", "555.666.777-88", "pedro@teste.com", "123456", 1L);
            
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
        
        try {
            usuarioService.cadastrarDono("", "123.456.789-00", "teste@teste.com", "123456");
            System.out.println("✗ Teste falhou - Deveria ter lançado exceção para nome vazio");
        } catch (ValidacaoException e) {
            System.out.println("✓ Teste passou - Exceção correta para nome vazio: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Teste falhou - Exceção incorreta: " + e.getMessage());
        }
        
        try {
            usuarioService.cadastrarDono("Teste", "123", "teste@teste.com", "123456");
            System.out.println("✗ Teste falhou - Deveria ter lançado exceção para CPF inválido");
        } catch (ValidacaoException e) {
            System.out.println("✓ Teste passou - Exceção correta para CPF inválido: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Teste falhou - Exceção incorreta: " + e.getMessage());
        }
        
        try {
            usuarioService.cadastrarDono("Teste", "123.456.789-00", "email_invalido", "123456");
            System.out.println("✗ Teste falhou - Deveria ter lançado exceção para email inválido");
        } catch (ValidacaoException e) {
            System.out.println("✓ Teste passou - Exceção correta para email inválido: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Teste falhou - Exceção incorreta: " + e.getMessage());
        }
        
        try {
            usuarioService.cadastrarDono("Teste", "123.456.789-00", "teste@teste.com", "123");
            System.out.println("✗ Teste falhou - Deveria ter lançado exceção para senha curta");
        } catch (ValidacaoException e) {
            System.out.println("✓ Teste passou - Exceção correta para senha curta: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Teste falhou - Exceção incorreta: " + e.getMessage());
        }
    }
    
    public void testLogin() {
        System.out.println("\n=== Teste: Login ===");
        
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
        
        try {
            usuarioService.login("email@inexistente.com", "senha_errada");
            System.out.println("✗ Teste falhou - Deveria ter lançado exceção para credenciais inválidas");
        } catch (AcessoNegadoException e) {
            System.out.println("✓ Teste passou - Exceção correta para credenciais inválidas: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Teste falhou - Exceção incorreta: " + e.getMessage());
        }
    }
    
    public void testPolimorfismo() {
        System.out.println("\n=== Teste: Polimorfismo ===");
        
        try {
            Dono dono = usuarioService.cadastrarDono("Dono Teste", "111.111.111-11", "dono@teste.com", "123456");
            Gerente gerente = usuarioService.cadastrarGerente("Gerente Teste", "222.222.222-22", "gerente@teste.com", "123456", 1L);
            Vendedor vendedor = usuarioService.cadastrarVendedor("Vendedor Teste", "333.333.333-33", "vendedor@teste.com", "123456", 1L);
            
            Usuario[] usuarios = {dono, gerente, vendedor};
            
            System.out.println("Testando polimorfismo:");
            for (Usuario u : usuarios) {
                System.out.println("  " + u.getNome() + " é do tipo: " + u.getTipoUsuario());
                
                assert u.getTipoUsuario() != null : "Tipo de usuário não deveria ser null";
            }
            
            System.out.println("✓ Teste passou - Polimorfismo funcionando corretamente");
            
        } catch (Exception e) {
            System.out.println("✗ Teste falhou: " + e.getMessage());
        }
    }
    
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
    
    public static void main(String[] args) {
        UsuarioServiceTest teste = new UsuarioServiceTest();
        teste.executarTodosTestes();
    }
}