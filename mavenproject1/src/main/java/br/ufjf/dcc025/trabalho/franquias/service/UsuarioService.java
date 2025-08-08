/*
 * Autores: Sara Ingrid - 202365056A, Angélica Coutinho - 202365064A
 */
package br.ufjf.dcc025.trabalho.franquias.service;

import br.ufjf.dcc025.trabalho.franquias.exceptions.AcessoNegadoException;
import br.ufjf.dcc025.trabalho.franquias.exceptions.EntidadeNaoEncontradaException;
import br.ufjf.dcc025.trabalho.franquias.exceptions.FranquiaException;
import br.ufjf.dcc025.trabalho.franquias.exceptions.ValidacaoException;
import br.ufjf.dcc025.trabalho.franquias.model.pedido.Pedido;
import br.ufjf.dcc025.trabalho.franquias.model.usuarios.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class UsuarioService {
    private static final String ARQUIVO_USUARIOS = "usuarios.dat";
    
    private List<Usuario> usuarios;
    private Long proximoId = 1L;
    private Usuario usuarioLogado;
    
    public UsuarioService() {
        this.usuarios = new ArrayList<>();
        carregarUsuarios();
        criarUsuariosPadrao();
    }
    
    // ========== MÉTODOS DE AUTENTICAÇÃO ==========
    
    public Usuario login(String email, String senha) throws AcessoNegadoException {
        if (email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
            throw new AcessoNegadoException("Email e senha são obrigatórios");
        }
        
        Usuario usuario = usuarios.stream()
                .filter(u -> u.validarCredenciais(email, senha))
                .findFirst()
                .orElse(null);
        
        if (usuario == null) {
            throw new AcessoNegadoException("Credenciais inválidas");
        }
        
        this.usuarioLogado = usuario;
        return usuario;
    }
    
    public void logout() {
        this.usuarioLogado = null;
    }
    
    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
    
    public boolean isLogado() {
        return usuarioLogado != null;
    }
    
    // ========== MÉTODOS CRUD DE USUÁRIOS ==========
    
    public Dono cadastrarDono(String nome, String cpf, String email, String senha) 
            throws ValidacaoException {
        validarDadosUsuario(nome, cpf, email, senha);
        
        Dono dono = new Dono(nome, cpf, email, senha);
        dono.setId(proximoId++);
        
        usuarios.add(dono);
        salvarUsuarios();
        return dono;
    }
    
    public Gerente cadastrarGerente(String nome, String cpf, String email, String senha, Long franquiaId) 
            throws ValidacaoException {
        validarDadosUsuario(nome, cpf, email, senha);
        
        Gerente gerente = new Gerente(nome, cpf, email, senha, franquiaId);
        gerente.setId(proximoId++);
        
        usuarios.add(gerente);
        salvarUsuarios();
        return gerente;
    }
    
    public Vendedor cadastrarVendedor(String nome, String cpf, String email, String senha, Long franquiaId) 
            throws ValidacaoException {
        validarDadosUsuario(nome, cpf, email, senha);
        
        Vendedor vendedor = new Vendedor(nome, cpf, email, senha, franquiaId);
        vendedor.setId(proximoId++);
        
        usuarios.add(vendedor);
        salvarUsuarios();
        return vendedor;
    }
    
    public boolean editarUsuario(Long id, String nome, String email) throws ValidacaoException {
        Usuario usuario = buscarUsuarioPorId(id);
        if (usuario == null) {
            return false;
        }
        
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidacaoException("Nome é obrigatório");
        }
        if (email == null || !isEmailValido(email)) {
            throw new ValidacaoException("Email deve ter formato válido");
        }
        
        if (existeEmailParaOutroUsuario(email, id)) {
            throw new ValidacaoException("Email já cadastrado para outro usuário");
        }
        
        usuario.setNome(nome);
        usuario.setEmail(email);
        salvarUsuarios();
        return true;
    }
    
    public boolean removerUsuario(Long id) throws EntidadeNaoEncontradaException {
        Usuario usuario = buscarUsuarioPorId(id);
        if (usuario == null) {
            throw new EntidadeNaoEncontradaException("Usuário", id);
        }
        
        boolean removido = usuarios.remove(usuario);
        if (removido) {
            salvarUsuarios();
        }
        return removido;
    }
    
    public Usuario buscarUsuarioPorId(Long id) {
        return usuarios.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarios.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }
    
    // ========== MÉTODOS DE LISTAGEM ==========
    
    public List<Usuario> listarUsuarios() {
        return new ArrayList<>(usuarios);
    }
    
    public List<Usuario> listarUsuariosPorTipo(String tipo) {
        return usuarios.stream()
                .filter(u -> u.getTipoUsuario().equals(tipo))
                .collect(Collectors.toList());
    }
    
    public List<Dono> listarDonos() {
        return usuarios.stream()
                .filter(u -> u instanceof Dono)
                .map(u -> (Dono) u)
                .collect(Collectors.toList());
    }
    
    public List<Gerente> listarGerentes() {
        return usuarios.stream()
                .filter(u -> u instanceof Gerente)
                .map(u -> (Gerente) u)
                .collect(Collectors.toList());
    }
    
    public List<Gerente> listarGerentesDisponiveis() {
        return usuarios.stream()
                .filter(u -> u instanceof Gerente)
                .map(u -> (Gerente) u)
                .filter(g -> g.getFranquiaId() == null)
                .collect(Collectors.toList());
    }
    
    public List<Vendedor> listarVendedores() {
        recalcularVendasVendedores();
        return usuarios.stream()
                .filter(u -> u instanceof Vendedor)
                .map(u -> (Vendedor) u)
                .collect(Collectors.toList());
    }
    
    public List<Vendedor> listarVendedoresPorFranquia(Long franquiaId) {
        recalcularVendasVendedores();
        return usuarios.stream()
                .filter(u -> u instanceof Vendedor)
                .map(u -> (Vendedor) u)
                .filter(v -> franquiaId.equals(v.getFranquiaId()))
                .collect(Collectors.toList());
    }
    
    // ========== MÉTODOS DE VALIDAÇÃO ==========
    
    private void validarDadosUsuario(String nome, String cpf, String email, String senha) 
            throws ValidacaoException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidacaoException("Nome é obrigatório");
        }
        if (nome.trim().length() < 2) {
            throw new ValidacaoException("Nome deve ter pelo menos 2 caracteres");
        }
        if (cpf == null || !isCpfValido(cpf)) {
            throw new ValidacaoException("CPF deve ter formato válido (000.000.000-00)");
        }
        if (email == null || !isEmailValido(email)) {
            throw new ValidacaoException("Email deve ter formato válido");
        }
        if (senha == null || senha.length() < 6) {
            throw new ValidacaoException("Senha deve ter pelo menos 6 caracteres");
        }
        
        if (existeEmail(email)) {
            throw new ValidacaoException("Email já cadastrado");
        }
        
        if (existeCpf(cpf)) {
            throw new ValidacaoException("CPF já cadastrado");
        }
    }
    
    private boolean isCpfValido(String cpf) {
        return cpf != null && cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
    }
    
    private boolean isEmailValido(String email) {
        return email != null && email.matches("^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,}$");
    }
    
    private boolean existeEmail(String email) {
        return usuarios.stream().anyMatch(u -> u.getEmail().equals(email));
    }
    
    private boolean existeEmailParaOutroUsuario(String email, Long id) {
        return usuarios.stream().anyMatch(u -> u.getEmail().equals(email) && !u.getId().equals(id));
    }
    
    private boolean existeCpf(String cpf) {
        return usuarios.stream().anyMatch(u -> u.getCpf().equals(cpf));
    }
    
    // ========== MÉTODOS DE PERSISTÊNCIA ==========
    
    private void carregarUsuarios() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_USUARIOS))) {
            Object[] dados = (Object[]) ois.readObject();
            @SuppressWarnings("unchecked")
            List<Usuario> usuariosCarregados = (List<Usuario>) dados[0];
            this.usuarios = usuariosCarregados;
            this.proximoId = (Long) dados[1];
        } catch (FileNotFoundException e) {
            this.usuarios = new ArrayList<>();
            this.proximoId = 1L;
        } catch (Exception e) {
            System.err.println("Erro ao carregar usuários: " + e.getMessage());
            this.usuarios = new ArrayList<>();
            this.proximoId = 1L;
        }
    }
    
    private void salvarUsuarios() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_USUARIOS))) {
            Object[] dados = {usuarios, proximoId};
            oos.writeObject(dados);
        } catch (IOException e) {
            System.err.println("Erro ao salvar usuários: " + e.getMessage());
        }
    }
    
    private void criarUsuariosPadrao() {
        if (usuarios.isEmpty()) {
            try {
                // Criar um dono padrão
                Dono donoAdmin = new Dono("Administrador", "000.000.000-00", "admin@franquia.com", "admin123");
                donoAdmin.setId(proximoId++);
                usuarios.add(donoAdmin);
                
                salvarUsuarios();
                System.out.println("Usuário padrão criado:");
                System.out.println("Email: admin@franquia.com");
                System.out.println("Senha: admin123");
            } catch (Exception e) {
                System.err.println("Erro ao criar usuários padrão: " + e.getMessage());
            }
        }
    }
    
    public void atualizarVendedor(Vendedor vendedor) throws FranquiaException {
        if (vendedor == null || vendedor.getId() == null) {
            throw new ValidacaoException("Vendedor inválido");
        }
        
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuario = usuarios.get(i);
            if (usuario.getId().equals(vendedor.getId()) && usuario instanceof Vendedor) {
                usuarios.set(i, vendedor);
                salvarUsuarios();
                return;
            }
        }
        
        throw new EntidadeNaoEncontradaException("Vendedor", vendedor.getId());
    }
    
    public void atualizarFranquiaIdGerente(Long gerenteId, Long franquiaId) throws FranquiaException {
        Usuario usuario = buscarUsuarioPorId(gerenteId);
        if (usuario == null) {
            throw new EntidadeNaoEncontradaException("Gerente", gerenteId);
        }
        
        if (!(usuario instanceof Gerente)) {
            throw new ValidacaoException("Usuário não é um gerente");
        }
        
        Gerente gerente = (Gerente) usuario;
        gerente.setFranquiaId(franquiaId);
        
        salvarUsuarios();
    }
    
    public void recalcularVendasVendedores() {
        try {
            List<Pedido> pedidosEntregues = carregarPedidosEntregues();
            
            for (Usuario usuario : usuarios) {
                if (usuario instanceof Vendedor) {
                    Vendedor vendedor = (Vendedor) usuario;
                    vendedor.setTotalVendas(0.0);
                    vendedor.setQuantidadeVendas(0);
                }
            }
            
            for (Pedido pedido : pedidosEntregues) {
                Long vendedorId = pedido.getVendedor().getId();
                Usuario usuario = buscarUsuarioPorId(vendedorId);
                if (usuario instanceof Vendedor) {
                    Vendedor vendedor = (Vendedor) usuario;
                    vendedor.adicionarVenda(pedido.getTotal());
                }
            }
            
            salvarUsuarios();
        } catch (Exception e) {
            System.err.println("Erro ao recalcular vendas dos vendedores: " + e.getMessage());
        }
    }
    
    private List<Pedido> carregarPedidosEntregues() {
        List<Pedido> pedidosEntregues = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("pedidos.dat"))) {
            @SuppressWarnings("unchecked")
            List<Pedido> todosPedidos = (List<Pedido>) ois.readObject();
            
            for (Pedido pedido : todosPedidos) {
                if (pedido.getStatus() == Pedido.StatusPedido.ENTREGUE) {
                    pedidosEntregues.add(pedido);
                }
            }
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            System.err.println("Erro ao carregar pedidos: " + e.getMessage());
        }
        return pedidosEntregues;
    }
}