/*
 * Autores: Sara Ingrid - 202365056A, Angélica Coutinho - 202365064A
 */
package br.ufjf.dcc025.trabalho.franquias.test;

import br.ufjf.dcc025.trabalho.franquias.exceptions.ValidacaoException;
import br.ufjf.dcc025.trabalho.franquias.model.franquia.Endereco;
import br.ufjf.dcc025.trabalho.franquias.model.franquia.Franquia;
import br.ufjf.dcc025.trabalho.franquias.model.pedido.Pedido;
import br.ufjf.dcc025.trabalho.franquias.model.produto.ItemPedido;
import br.ufjf.dcc025.trabalho.franquias.model.produto.Produto;
import br.ufjf.dcc025.trabalho.franquias.model.usuarios.*;
import br.ufjf.dcc025.trabalho.franquias.service.*;

public class TestesBasicos {
    
    private static int testesExecutados = 0;
    private static int testesPassaram = 0;
    
    public static void executarTodos() {
        System.out.println("=== Executando Testes do Sistema ===");
        System.out.println();
        
        testarUsuarios();
        testarFranquias();
        testarProdutos();
        testarPedidos();
        testarPersistencia();
        
        System.out.println("=== Resumo dos Testes ===");
        System.out.println("Testes executados: " + testesExecutados);
        System.out.println("Testes passaram: " + testesPassaram);
        System.out.println("Testes falharam: " + (testesExecutados - testesPassaram));
        System.out.println("Taxa de sucesso: " + (testesPassaram * 100 / testesExecutados) + "%");
    }
    
    private static void testarUsuarios() {
        System.out.println("--- Testando Usuários ---");
        
        teste("Criação de Dono", () -> {
            Dono dono = new Dono("João Silva", "123.456.789-00", "joao@email.com", "senha123");
            return dono.getNome().equals("João Silva") && dono.getTipoUsuario().equals("DONO");
        });
        
        teste("Polimorfismo de Usuários", () -> {
            Usuario dono = new Dono("Admin", "000.000.000-00", "admin@test.com", "123");
            Usuario gerente = new Gerente("Gerente", "111.111.111-11", "gerente@test.com", "123", 1L);
            Usuario vendedor = new Vendedor("Vendedor", "222.222.222-22", "vendedor@test.com", "123", 1L);
            
            return dono.getTipoUsuario().equals("DONO") &&
                   gerente.getTipoUsuario().equals("GERENTE") &&
                   vendedor.getTipoUsuario().equals("VENDEDOR");
        });
        
        teste("Validação de Credenciais", () -> {
            Dono dono = new Dono("Test", "123.456.789-00", "test@email.com", "senha123");
            return dono.validarCredenciais("test@email.com", "senha123") &&
                   !dono.validarCredenciais("test@email.com", "senhaErrada");
        });
        
        System.out.println();
    }
    
    private static void testarFranquias() {
        System.out.println("--- Testando Franquias ---");
        
        teste("Criação de Franquia", () -> {
            Franquia franquia = new Franquia("Franquia Teste", new Endereco("Rua A, 123", "Juiz de Fora", "MG", "36000-000"));
            return franquia.getNome().equals("Franquia Teste");
        });
        
        teste("Cálculo de Ticket Médio", () -> {
            Franquia franquia = new Franquia("Test", new Endereco("Rua A", "JF", "MG", "36000-000"));
            franquia.registrarVenda(100.0);
            franquia.registrarVenda(200.0);
            return Math.abs(franquia.calcularTicketMedio() - 150.0) < 0.01;
        });
        
        System.out.println();
    }
    
    private static void testarProdutos() {
        System.out.println("--- Testando Produtos ---");
        
        teste("Criação de Produto", () -> {
            Produto produto = new Produto("Pizza", "Pizza Margherita", 25.90, 10, 2, "Alimentação");
            return produto.getNome().equals("Pizza") && produto.getPreco() == 25.90;
        });
        
        teste("Controle de Estoque", () -> {
            Produto produto = new Produto("Hambúrguer", "Hambúrguer clássico", 15.90, 5, 2, "Alimentação");
            boolean reduziu = produto.reduzirEstoque(3);
            return reduziu && produto.getEstoque() == 2;
        });
        
        teste("Detecção de Estoque Baixo", () -> {
            Produto produto = new Produto("Refrigerante", "Coca-Cola 350ml", 5.90, 1, 2, "Bebida");
            return produto.isEstoqueBaixo();
        });
        
        System.out.println();
    }
    
    private static void testarPedidos() {
        System.out.println("--- Testando Pedidos ---");
        
        teste("Criação de Pedido", () -> {
            Vendedor vendedor = new Vendedor("João", "123.456.789-00", "joao@test.com", "123", 1L);
            Pedido pedido = new Pedido("Cliente Teste", vendedor, 1L);
            pedido.setFormaPagamento("Cartão");
            pedido.setModalidadeEntrega("Balcão");
            return pedido.getNomeCliente().equals("Cliente Teste");
        });
        
        teste("Cálculo Total do Pedido", () -> {
            Vendedor vendedor = new Vendedor("Maria", "987.654.321-00", "maria@test.com", "123", 1L);
            Pedido pedido = new Pedido("Cliente", vendedor, 1L);
            
            Produto produto1 = new Produto("Item 1", "Desc 1", 10.0, 5, 1, "Cat1");
            Produto produto2 = new Produto("Item 2", "Desc 2", 20.0, 3, 1, "Cat2");
            
            pedido.adicionarItem(new ItemPedido(produto1, 2));
            pedido.adicionarItem(new ItemPedido(produto2, 1));
            
            return Math.abs(pedido.getSubtotal() - 40.0) < 0.01;
        });
        
        System.out.println();
    }
    
    private static void testarPersistencia() {
        System.out.println("--- Testando Persistência ---");
        
        teste("UsuarioService - Criação de usuário padrão", () -> {
            UsuarioService service = new UsuarioService();
            return service.listarUsuarios().size() > 0;
        });
        
        teste("Validação de Dados de Usuário", () -> {
            UsuarioService service = new UsuarioService();
            try {
                service.cadastrarDono("", "123.456.789-00", "email@test.com", "123456");
                return false;
            } catch (ValidacaoException e) {
                return true;
            } catch (Exception e) {
                return false;
            }
        });
        
        System.out.println();
    }
    
    private static void teste(String nome, TestFunction teste) {
        testesExecutados++;
        try {
            boolean resultado = teste.executar();
            if (resultado) {
                testesPassaram++;
                System.out.println("✓ " + nome);
            } else {
                System.out.println("✗ " + nome + " - FALHOU");
            }
        } catch (Exception e) {
            System.out.println("✗ " + nome + " - ERRO: " + e.getMessage());
        }
    }
    
    @FunctionalInterface
    interface TestFunction {
        boolean executar() throws Exception;
    }
}