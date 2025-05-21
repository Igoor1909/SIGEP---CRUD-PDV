package Conexao;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import dados.FornecedorDados;

public class FornecedorDAO {

    Connection conexao;
    PreparedStatement pstm = null;
    ResultSet rs;

    public void cadastrarFornecedor(FornecedorDados objFornecedor) {
        Connection conexao = null;
        PreparedStatement pstm = null;

        try {
            // Verifica se CNPJ já existe
            if (getCNPJFornecedor(objFornecedor.getCnpj_fornecedor())) {
                JOptionPane.showMessageDialog(null, "CNPJ já cadastrado!");
                return;
            }

            conexao = new conexao().getConexao();
            conexao.setAutoCommit(false);

            String sql = "INSERT INTO fornecedor (nome_fornecedor, cnpj_fornecedor, rua_fornecedor, "
                    + "complemento_fornecedor, numero_fornecedor, cidade_fornecedor, estado_fornecedor, bairro_fornecedor) "
                    + "VALUES (?,?,?,?,?,?,?,?)";

            pstm = conexao.prepareStatement(sql);
            pstm.setString(1, objFornecedor.getNome_fornecedor());
            pstm.setString(2, objFornecedor.getCnpj_fornecedor());
            pstm.setString(3, objFornecedor.getRua_fornecedor());
            pstm.setString(4, objFornecedor.getComplemento_fornecedor());
            pstm.setInt(5, objFornecedor.getNumero_fornecedor());
            pstm.setString(6, objFornecedor.getCidade_fornecedor());
            pstm.setString(7, objFornecedor.getEstado_fornecedor());
            pstm.setString(8, objFornecedor.getBairro_fornecedor());

            pstm.executeUpdate();
            conexao.commit();
            

        } catch (SQLException e) {
            try {
                if (conexao != null) {
                    conexao.rollback();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao reverter operação: " + ex.getMessage());
            }
            JOptionPane.showMessageDialog(null, "Erro durante o cadastro: " + e.getMessage());
        } finally {
            try {
                if (pstm != null) {
                    pstm.close();
                }
                if (conexao != null) {
                    conexao.close();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao fechar conexões: " + ex.getMessage());
            }
        }
    }

    public boolean getCNPJFornecedor(String cnpj) {
        Connection conexao = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT cnpj_fornecedor FROM fornecedor WHERE cnpj_fornecedor = ? LIMIT 1";
            conexao = new conexao().getConexao();
            pstm = conexao.prepareStatement(sql);
            pstm.setString(1, cnpj);
            rs = pstm.executeQuery();
            return rs.next(); // Retorna true se encontrou o CNPJ

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "FornecedorDAO CNPJ Ja cadastrado " + e);
            return true; // Em caso de erro, assume que já existe para evitar duplicação
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstm != null) {
                    pstm.close();
                }
                if (conexao != null) {
                    conexao.close();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao fechar conexões: " + ex.getMessage());
            }
        }
    }

    public FornecedorDados getFornecedorDados(int id_fornecedor) {
        conexao = new conexao().getConexao();

        String sql = "SELECT * FROM fornecedor WHERE id_fornecedor = ?";
        try {
            PreparedStatement stmt = this.conexao.prepareStatement(sql);
            stmt.setInt(1, id_fornecedor);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                FornecedorDados objFornecedorDados = new FornecedorDados();
                objFornecedorDados.setNome_fornecedor(rs.getString("nome_fornecedor"));
                objFornecedorDados.setCnpj_fornecedor(rs.getString("cnpj_fornecedor"));
                objFornecedorDados.setRua_fornecedor(rs.getString("rua_fornecedor"));
                objFornecedorDados.setComplemento_fornecedor(rs.getString("complemento_fornecedor"));
                objFornecedorDados.setNumero_fornecedor(rs.getInt("numero_fornecedor"));
                objFornecedorDados.setCidade_fornecedor(rs.getString("cidade_fornecedor"));
                objFornecedorDados.setEstado_fornecedor(rs.getString("estado_fornecedor"));
                objFornecedorDados.setBairro_fornecedor(rs.getString("bairro_fornecedor"));

                rs.close();
                stmt.close();
                conexao.close();

                return objFornecedorDados;
            } else {
                rs.close();
                stmt.close();
                conexao.close();
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void alterarCliente(FornecedorDados objCliente) {
        String sql = "update cliente set nome_cliente = ?, telefone = ?, id_endereco = ?, endereco = ?, medidas = ? where id_cliente = ?";
        conexao = new conexao().getConexao();

        try {
  

            pstm.execute();
            pstm.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ClienteDAO AlterarCliente " + e);
        }
    }

    public void excluirCliente(FornecedorDados objCliente) {

    }

}
