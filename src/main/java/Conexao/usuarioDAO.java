package Conexao;

import dados.UsuarioDados;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;

public class usuarioDAO {

    Connection conexao = null;
    PreparedStatement pstm = null;

    

    // Adicione este método
    public void setConexao(Connection conexao) {
        this.conexao = conexao;
    }
    
   
    
    public ResultSet autenticacaoUsuario(UsuarioDados dados) {
        String sql = "SELECT * FROM usuario WHERE email_usuario = ? AND senha_usuario = ?";
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] senhaBytes = dados.getSenha_usuario().getBytes(StandardCharsets.UTF_8);
            byte[] hashSenha = md.digest(senhaBytes);

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashSenha) {
                hexString.append(String.format("%02x", b));
            }
            String senhaCriptografada = hexString.toString();

            conexao = new conexao().getConexao();
            pstm = conexao.prepareStatement(sql);
            pstm.setString(1, dados.getEmail_usuario());
            pstm.setString(2, senhaCriptografada);

            rs = pstm.executeQuery();
            return rs;

        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao autenticar usuário: " + erro.getMessage());
            try {
                if (pstm != null) {
                    pstm.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (conexao != null) {
                    conexao.close();
                }
            } catch (SQLException e) {
            }
            return null;
        } catch (NoSuchAlgorithmException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao criptografar senha: " + erro.getMessage());
            try {
                if (pstm != null) {
                    pstm.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (conexao != null) {
                    conexao.close();
                }
            } catch (SQLException e) {
            }
            return null;
        }
    }

    public boolean cadastrarUsuario(UsuarioDados dados) {
        // Primeiro verifica se o email já existe
        if (emailJaCadastrado(dados.getEmail_usuario())) {
            JOptionPane.showMessageDialog(null, "Erro: Este email já está cadastrado!");
            return false;
        }

        String sql = "INSERT INTO usuario (nome_usuario, email_usuario, senha_usuario, tipo_usuario) VALUES (?, ?, ?, ?)";

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] senhaBytes = dados.getSenha_usuario().getBytes(StandardCharsets.UTF_8);
            byte[] hashSenha = md.digest(senhaBytes);

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashSenha) {
                hexString.append(String.format("%02x", b));
            }
            String senhaCriptografada = hexString.toString();

            conexao = new conexao().getConexao();
            pstm = conexao.prepareStatement(sql);
            pstm.setString(1, dados.getNome_usuario());
            pstm.setString(2, dados.getEmail_usuario());
            pstm.setString(3, senhaCriptografada);
            pstm.setString(4, dados.getTipo_usuario());

            pstm.execute();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar usuário: " + e.getMessage());
            return false;
        } catch (NoSuchAlgorithmException e) {
            JOptionPane.showMessageDialog(null, "Erro ao criptografar senha: " + e.getMessage());
            return false;
        } finally {
        }
    }

    public boolean emailJaCadastrado(String email) {
        String sql = "SELECT email_usuario FROM usuario WHERE email_usuario = ?";

        try {
            conexao = new conexao().getConexao();
            pstm = conexao.prepareStatement(sql);
            pstm.setString(1, email);
            ResultSet rs = pstm.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao verificar email: " + e.getMessage());
            return true;
        } finally {
            try {
                if (pstm != null) {
                    pstm.close();
                }
                if (conexao != null) {
                    conexao.close();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    public UsuarioDados getUsuarioDados(String email_usuario) {
        conexao = new conexao().getConexao();

        String sql = "SELECT * FROM usuario WHERE email_usuario = ?";
        try {
            PreparedStatement stmt = this.conexao.prepareStatement(sql);
            stmt.setString(1, email_usuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                UsuarioDados objUsuario = new UsuarioDados();
                objUsuario.setId_usuario(rs.getInt("id_usuario"));
                objUsuario.setNome_usuario(rs.getString("nome_usuario"));
                objUsuario.setEmail_usuario(rs.getString("email_usuario"));
                objUsuario.setSenha_usuario(rs.getString("senha_usuario"));
                objUsuario.setTipo_usuario(rs.getString("tipo_usuario"));

                return objUsuario;
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
        public String gerarHashSenha(String senha) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] senhaBytes = senha.getBytes(StandardCharsets.UTF_8);
        byte[] hashSenha = md.digest(senhaBytes);

        StringBuilder hexString = new StringBuilder();
        for (byte b : hashSenha) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}
