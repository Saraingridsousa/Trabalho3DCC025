/*
 * Autores: Sara Ingrid - 202365056A, Angélica Coutinho - 202365064A
 */
package br.ufjf.dcc025.trabalho.franquias;

import br.ufjf.dcc025.trabalho.franquias.model.usuarios.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class TestesSimples {
    
    private Dono dono;
    private Gerente gerente;
    private Vendedor vendedor;
    
    @BeforeEach
    public void setUp() {
        dono = new Dono("João Silva", "123.456.789-00", "joao@email.com", "senha123");
        gerente = new Gerente("Maria Santos", "987.654.321-00", "maria@email.com", "senha456", 1L);
        vendedor = new Vendedor("Pedro Oliveira", "456.789.123-00", "pedro@email.com", "senha789", 1L);
    }
    
    @Test
    public void testCriacaoUsuarios() {
        assertNotNull(dono);
        assertNotNull(gerente);
        assertNotNull(vendedor);
        
        assertEquals("João Silva", dono.getNome());
        assertEquals("Maria Santos", gerente.getNome());
        assertEquals("Pedro Oliveira", vendedor.getNome());
    }
    
    @Test
    public void testTiposUsuarios() {
        assertEquals("DONO", dono.getTipoUsuario());
        assertEquals("GERENTE", gerente.getTipoUsuario());
        assertEquals("VENDEDOR", vendedor.getTipoUsuario());
    }
    
    @Test
    public void testPermissoesDono() {
        assertTrue(dono.podeGerenciarFranquias());
        assertTrue(dono.podeGerenciarGerentes());
        assertTrue(dono.podeAcessarRelatoriosConsolidados());
    }
    
    @Test
    public void testPermissoesGerente() {
        assertTrue(gerente.podeGerenciarVendedores());
        assertTrue(gerente.podeGerenciarPedidos());
        assertTrue(gerente.podeGerenciarEstoque());
        assertTrue(gerente.podeAcessarRelatoriosFranquia());
        assertEquals(Long.valueOf(1L), gerente.getFranquiaId());
    }
    
    @Test
    public void testVendedor() {
        assertTrue(vendedor.podeCadastrarPedidos());
        assertEquals(0.0, vendedor.getTotalVendas());
        assertEquals(0, vendedor.getQuantidadeVendas());
        assertEquals(0.0, vendedor.calcularTicketMedio());
        
        vendedor.adicionarVenda(100.0);
        assertEquals(100.0, vendedor.getTotalVendas());
        assertEquals(1, vendedor.getQuantidadeVendas());
        
        vendedor.adicionarVenda(200.0);
        assertEquals(300.0, vendedor.getTotalVendas());
        assertEquals(2, vendedor.getQuantidadeVendas());
        assertEquals(150.0, vendedor.calcularTicketMedio());
    }
    
    @Test
    public void testValidacaoCredenciais() {
        assertTrue(dono.validarCredenciais("joao@email.com", "senha123"));
        assertFalse(dono.validarCredenciais("joao@email.com", "senhaErrada"));
        assertFalse(dono.validarCredenciais("email@errado.com", "senha123"));
    }
}