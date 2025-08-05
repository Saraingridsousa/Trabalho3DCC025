package br.ufjf.dcc025.trabalho.franquias.service;

import br.ufjf.dcc025.trabalho.franquias.model.usuarios.Usuario;
import br.ufjf.dcc025.trabalho.franquias.model.usuarios.Dono;
import br.ufjf.dcc025.trabalho.franquias.model.usuarios.Gerente;
import br.ufjf.dcc025.trabalho.franquias.model.usuarios.Vendedor;
import br.ufjf.dcc025.trabalho.franquias.exceptions.ValidacaoException;
import br.ufjf.dcc025.trabalho.franquias.exceptions.EntidadeNaoEncontradaException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UsuarioService implements OperacoesCRUD<Usuario, Long> {
    private static final String ARQUIVO_USUARIOS = "usuarios.dat";
    
    private List<Usuario> usuarios;
    private Long proximoId = 1L;
    
    public UsuarioService() {
        this.usuarios = new ArrayList<>();
        carregarUsuarios();
    }
    
    public Usuario login(String email, String senha) {
        return usuarios.stream()
                .filter(u -> u.getEmail().equals(email) && u.getSenha().equals(senha))
                .findFirst()
                .orElse(null);
    }
    
    public Usuario cadastrarUsuario(Usuario usuario) throws ValidacaoException {
        validarDadosUsuario(usuario.getNome(), "", usuario.getEmail(), usuario.getSenha());
        
        if (existeEmail(usuario.getEmail())) {
            throw new ValidacaoException("Já existe um usuário com este email");
        }
        
        usuario.setId(proximoId++);
        usuarios.add(usuario);
        salvarUsuarios();
        return usuario;
    }
    
    public boolean editarUsuario(Long id, String nome, String email, String senha) 
            throws ValidacaoException, EntidadeNaoEncontradaException {
        
        Usuario usuario = buscarUsuarioPorId(id);
        if (usuario == null) {
            throw new EntidadeNaoEncontradaException("Usuário com ID " + id + " não encontrado");
        }
        
        validarDadosUsuario(nome, "", email, senha);
        
        if (existeEmailParaOutroUsuario(email, id)) {
            throw new ValidacaoException("Já existe um usuário com este email");
        }
        
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        
        salvarUsuarios();
        return true;
    }
    
    public boolean removerUsuario(Long id) throws EntidadeNaoEncontradaException {
        Usuario usuario = buscarUsuarioPorId(id);
        if (usuario == null) {
            throw new EntidadeNaoEncontradaException("Usuário com ID " + id + " não encontrado");
        }
        
        usuarios.remove(usuario);
        salvarUsuarios();
        return true;
    }
    
    public Usuario buscarUsuarioPorId(Long id) {
        return usuarios.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    public List<Usuario> listarUsuarios() {
        return new ArrayList<>(usuarios);
    }
    
    @Override
    public Usuario cadastrar(Usuario usuario) throws ValidacaoException {
        return cadastrarUsuario(usuario);
    }
    
    @Override
    public Usuario buscarPorId(Long id) {
        return buscarUsuarioPorId(id);
    }
    
    @Override
    public List<Usuario> listar() {
        return listarUsuarios();
    }
    
    @Override
    public Usuario atualizar(Long id, Usuario usuario) throws ValidacaoException, EntidadeNaoEncontradaException {
        editarUsuario(id, usuario.getNome(), usuario.getEmail(), usuario.getSenha());
        return buscarPorId(id);
    }
    
    @Override
    public boolean remover(Long id) throws EntidadeNaoEncontradaException {
        return removerUsuario(id);
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
    
    public List<Vendedor> listarVendedores() {
        return usuarios.stream()
                .filter(u -> u instanceof Vendedor)
                .map(u -> (Vendedor) u)
                .collect(Collectors.toList());
    }
    
    private void validarDadosUsuario(String nome, String cpf, String email, String senha) throws ValidacaoException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidacaoException("Nome é obrigatório");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new ValidacaoException("Email é obrigatório");
        }
        if (senha == null || senha.trim().isEmpty()) {
            throw new ValidacaoException("Senha é obrigatória");
        }
        if (!email.contains("@")) {
            throw new ValidacaoException("Email deve ter formato válido");
        }
    }
    
    private boolean existeEmail(String email) {
        return usuarios.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email.trim()));
    }
    
    private boolean existeEmailParaOutroUsuario(String email, Long id) {
        return usuarios.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email.trim()) && !u.getId().equals(id));
    }
    
    @SuppressWarnings("unchecked")
    private void carregarUsuarios() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_USUARIOS))) {
            usuarios = (List<Usuario>) ois.readObject();
            proximoId = (Long) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            criarDadosPadrao();
        }
    }
    
    private void salvarUsuarios() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_USUARIOS))) {
            oos.writeObject(usuarios);
            oos.writeObject(proximoId);
        } catch (IOException e) {
            System.err.println("Erro ao salvar usuários: " + e.getMessage());
        }
    }
    
    private void criarDadosPadrao() {
        try {
            cadastrarUsuario(new Dono("Admin", "admin@franquia.com", "admin123"));
        } catch (ValidacaoException e) {
            System.err.println("Erro ao criar dados padrão: " + e.getMessage());
        }
    }
}
