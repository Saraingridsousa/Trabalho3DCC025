/*
 * Autores: Sara Ingrid - 202365056A, Angélica Coutinho - 202365064A
 */
package br.ufjf.dcc025.trabalho.franquias.test;

import br.ufjf.dcc025.trabalho.franquias.exceptions.AcessoNegadoException;
import br.ufjf.dcc025.trabalho.franquias.exceptions.ValidacaoException;
import br.ufjf.dcc025.trabalho.franquias.model.franquia.Endereco;
import br.ufjf.dcc025.trabalho.franquias.model.franquia.Franquia;
import br.ufjf.dcc025.trabalho.franquias.model.pedido.Cliente;
import br.ufjf.dcc025.trabalho.franquias.model.pedido.Pedido;
import br.ufjf.dcc025.trabalho.franquias.model.produto.ItemPedido;
import br.ufjf.dcc025.trabalho.franquias.model.produto.Produto;
import br.ufjf.dcc025.trabalho.franquias.model.usuarios.*;
import br.ufjf.dcc025.trabalho.franquias.service.*;

public class DemonstracaoSistema {
    
    public static void executarDemonstracao() {
        System.out.println("===============================================");
        System.out.println("  DEMONSTRAÇÃO DO SISTEMA DE FRANQUIAS");
        System.out.println("  Autores: Sara Ingrid, Angélica Coutinho");
        System.out.println("===============================================");
        System.out.println();
        
        demonstrarUsuarios();
        demonstrarFranquias();
        demonstrarProdutos();
        demonstrarPedidos();
        demonstrarPersistencia();
        demonstrarExcecoes();
        demonstrarInterface();
        
        System.out.println("===============================================");
        System.out.println("         DEMONSTRAÇÃO CONCLUÍDA");
        System.out.println("===============================================");
    }
    
    private static void demonstrarUsuarios() {
        System.out.println("--- 1. DEMONSTRAÇÃO DE USUÁRIOS ---");
        System.out.println("Conceitos: Herança, Polimorfismo, Classe Abstrata");
        System.out.println();
        
        try {
            Dono dono = new Dono("João Silva", "123.456.789-00", "joao@franquia.com", "senha123");
            dono.setId(1L);
            
            Gerente gerente = new Gerente("Maria Santos", "987.654.321-00", "maria@franquia.com", "senha456", 1L);
            gerente.setId(2L);
            
            Vendedor vendedor = new Vendedor("Pedro Costa", "456.789.123-00", "pedro@franquia.com", "senha789", 1L);
            vendedor.setId(3L);
            
            Usuario[] usuarios = {dono, gerente, vendedor};
            
            System.out.println("Usuários criados (demonstrando herança):");
            for (Usuario u : usuarios) {
                System.out.println("- " + u.getNome() + " (" + u.getTipoUsuario() + ")");
            }
            
            System.out.println("\nPermissões específicas:");
            System.out.println("- Dono pode gerenciar franquias: " + dono.podeGerenciarFranquias());
            System.out.println("- Gerente pode gerenciar vendedores: " + gerente.podeGerenciarVendedores());
            System.out.println("- Vendedor pode cadastrar pedidos: " + vendedor.podeCadastrarPedidos());
            
            vendedor.adicionarVenda(100.0);
            vendedor.adicionarVenda(200.0);
            System.out.println("- Ticket médio do vendedor: R$ " + String.format("%.2f", vendedor.calcularTicketMedio()));
            
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    private static void demonstrarFranquias() {
        System.out.println("--- 2. DEMONSTRAÇÃO DE FRANQUIAS ---");
        System.out.println("Conceitos: Encapsulamento, Coleções");
        System.out.println();
        
        try {
            FranquiaService service = new FranquiaService();
            
            Gerente gerente = new Gerente("Ana Silva", "111.222.333-44", "ana@test.com", "123456", null);
            Franquia franquia = service.cadastrar(new Franquia("Franquia Centro", 
                    new Endereco("Rua Principal, 100", "Juiz de Fora", "MG", "36010-000"),
                gerente)
            );
            
            System.out.println("Franquia criada:");
            System.out.println("- Nome: " + franquia.getNome());
            System.out.println("- Endereço: " + franquia.getEndereco().toString());
            System.out.println("- Gerente: " + (franquia.getGerente() != null ? franquia.getGerente().getNome() : "Não atribuído"));
            
            franquia.adicionarVenda(150.0);
            franquia.adicionarVenda(200.0);
            franquia.adicionarVenda(100.0);
            
            System.out.println("\nEstatísticas da franquia:");
            System.out.println("- Total de pedidos: " + franquia.getTotalPedidos());
            System.out.println("- Receita acumulada: R$ " + String.format("%.2f", franquia.getReceitaAcumulada()));
            System.out.println("- Ticket médio: R$ " + String.format("%.2f", franquia.getTicketMedio()));
            
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    private static void demonstrarProdutos() {
        System.out.println("--- 3. DEMONSTRAÇÃO DE PRODUTOS ---");
        System.out.println("Conceitos: Validação, Métodos de negócio");
        System.out.println();
        
        try {
            FranquiaService service = new FranquiaService();
            
            Produto pizza = service.cadastrarProduto("Pizza Margherita", "Pizza com queijo e tomate", 25.90, 10, 3, "Alimentação");
            Produto hamburguer = service.cadastrarProduto("Hambúrguer Clássico", "Hambúrguer com carne, alface e tomate", 18.50, 2, 5, "Alimentação");
            Produto refrigerante = service.cadastrarProduto("Coca-Cola 350ml", "Refrigerante de cola", 5.90, 20, 10, "Bebida");
            
            System.out.println("Produtos cadastrados:");
            for (Produto p : service.listarProdutos()) {
                System.out.println("- " + p.getNome() + " - R$ " + String.format("%.2f", p.getPreco()) + 
                                 " (Estoque: " + p.getEstoque() + ")");
            }
            
            System.out.println("\nTeste de redução de estoque:");
            boolean reduziu = pizza.reduzirEstoque(3);
            System.out.println("- Reduziu 3 pizzas: " + reduziu + " (estoque atual: " + pizza.getEstoque() + ")");
            
            System.out.println("\nProdutos com estoque baixo:");
            for (Produto p : service.listarProdutosEstoqueBaixo()) {
                System.out.println("- " + p.getNome() + " (estoque: " + p.getEstoque() + 
                                 ", mínimo: " + p.getEstoqueMinimo() + ")");
            }
            
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    private static void demonstrarPedidos() {
        System.out.println("--- 4. DEMONSTRAÇÃO DE PEDIDOS ---");
        System.out.println("Conceitos: Composição, Cálculos automáticos");
        System.out.println();
        
        try {
            Vendedor vendedor = new Vendedor("Carlos Lima", "777.888.999-00", "carlos@test.com", "123456", 1L);
            vendedor.setId(10L);
            
            Produto pizza = new Produto("Pizza", "Pizza Margherita", 25.90, 10, 2, "Alimentação");
            pizza.setId(1L);
            
            Produto refrigerante = new Produto("Refrigerante", "Coca-Cola", 5.90, 20, 5, "Bebida");
            refrigerante.setId(2L);
            
            Gerente gerente = new Gerente("Ana Silva", "111.222.333-44", "ana@test.com", "123456", null);;
            Franquia franquia = new Franquia("Teste", new Endereco("Rua", "jf", "mg", "00000-000"), gerente);
            Pedido pedido = new Pedido(new Cliente("João da Silva", "000.000.000-00", "99999-9999", new Endereco("Rua", "sp", "sp", "00000-000")), 
                    vendedor, franquia);
            
            pedido.setFormaPagamento("Cartão de Crédito");
            pedido.setModalidadeEntrega("Balcão");
            pedido.setTaxaServico(2.00);
            
            pedido.adicionarItem(new ItemPedido(pizza, 2));
            pedido.adicionarItem(new ItemPedido(refrigerante, 3));
            
            System.out.println("Pedido criado:");
            System.out.println("- Cliente: " + pedido.getNomeCliente());
            System.out.println("- Vendedor: " + pedido.getVendedor().getNome());
            System.out.println("- Forma de pagamento: " + pedido.getFormaPagamento());
            System.out.println("- Modalidade: " + pedido.getModalidadeEntrega());
            
            System.out.println("\nItens do pedido:");
            for (ItemPedido item : pedido.getItens()) {
                System.out.println("- " + item.getProduto().getNome() + 
                                 " x" + item.getQuantidade() + 
                                 " = R$ " + String.format("%.2f", item.getSubtotal()));
            }
            
            System.out.println("\nTotais:");
            System.out.println("- Subtotal: R$ " + String.format("%.2f", pedido.getSubtotal()));
            System.out.println("- Taxa de serviço: R$ " + String.format("%.2f", pedido.getTaxaServico()));
            System.out.println("- Total: R$ " + String.format("%.2f", pedido.getValorTotal()));
            
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    private static void demonstrarPersistencia() {
        System.out.println("--- 5. DEMONSTRAÇÃO DE PERSISTÊNCIA ---");
        System.out.println("Conceitos: Leitura/escrita em arquivos, Serialização");
        System.out.println();
        
        try {
            UsuarioService userService = new UsuarioService();
            
            System.out.println("Usuários carregados do arquivo:");
            for (Usuario u : userService.listarUsuarios()) {
                System.out.println("- " + u.getNome() + " (" + u.getTipoUsuario() + ")");
            }
            
            System.out.println("\nArquivos de dados utilizados:");
            System.out.println("- usuarios.dat (usuários do sistema)");
            System.out.println("- franquias.dat (dados das franquias)");
            System.out.println("- produtos.dat (catálogo de produtos)");
            System.out.println("- pedidos.dat (histórico de pedidos)");
            System.out.println("- clientes.dat (dados dos clientes)");
            
            System.out.println("\nOs dados são salvos automaticamente após cada operação.");
            
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    private static void demonstrarExcecoes() {
        System.out.println("--- 6. DEMONSTRAÇÃO DE EXCEÇÕES ---");
        System.out.println("Conceitos: Tratamento de exceções, Classes de exceção personalizadas");
        System.out.println();
        
        UsuarioService userService = new UsuarioService();
        
        try {
            userService.cadastrarDono("", "123.456.789-00", "test@email.com", "123456");
        } catch (ValidacaoException e) {
            System.out.println("✓ Exceção capturada - Nome vazio: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Exceção incorreta: " + e.getMessage());
        }
        
        try {
            userService.cadastrarDono("Teste", "123", "test@email.com", "123456");
        } catch (ValidacaoException e) {
            System.out.println("✓ Exceção capturada - CPF inválido: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Exceção incorreta: " + e.getMessage());
        }
        
        try {
            userService.login("usuario@inexistente.com", "senhaErrada");
        } catch (AcessoNegadoException e) {
            System.out.println("✓ Exceção capturada - Login inválido: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Exceção incorreta: " + e.getMessage());
        }
        
        System.out.println("\nTipos de exceção implementados:");
        System.out.println("- ValidacaoException (dados inválidos)");
        System.out.println("- AcessoNegadoException (credenciais incorretas)");
        System.out.println("- PersistenciaException (problemas com arquivos)");
        System.out.println("- EntidadeNaoEncontradaException (busca sem resultado)");
        
        System.out.println();
    }
    
    private static void demonstrarInterface() {
        System.out.println("--- 7. DEMONSTRAÇÃO DE INTERFACE GRÁFICA ---");
        System.out.println("Conceitos: Interface gráfica com Swing");
        System.out.println();
        
        System.out.println("Interface gráfica implementada:");
        System.out.println("- LoginPainel.java - Tela de login do sistema");
        System.out.println("- MainFrame.java - Tela principal adaptativa por usuário");
        System.out.println();
        
        System.out.println("Funcionalidades da interface:");
        System.out.println("- Autenticação de usuários");
        System.out.println("- Menus diferentes por tipo de usuário");
        System.out.println("- Formulários para cadastro de dados");
        System.out.println("- Validação em tempo real");
        System.out.println("- Mensagens de erro e sucesso");
        System.out.println();
        
        System.out.println("Para testar a interface, execute: mvn exec:java");
        System.out.println("Credenciais padrão: admin@franquia.com / admin123");
        
        System.out.println();
    }
    
    public static void main(String[] args) {
        executarDemonstracao();
    }
}