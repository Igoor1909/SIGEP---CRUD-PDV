package Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class EstoqueDAO {

    private conexao conexaoDB;

    public EstoqueDAO(conexao conexaoDB) {
        this.conexaoDB = conexaoDB;
    }

    public boolean atualizarEstoqueCompra(int idProduto, int quantidadeComprada) {
        String sqlSelect = "SELECT quantidade_produto FROM estoque WHERE id_produto = ?";
        String sqlUpdate = "UPDATE estoque SET quantidade_produto = quantidade_produto + ? WHERE id_produto = ?";
        String sqlInsert = "INSERT INTO estoque (id_produto, quantidade_produto) VALUES (?, ?)";

        Connection conexao = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            conexao = conexaoDB.getConexao();

            // Verifica se já existe no estoque
            pstm = conexao.prepareStatement(sqlSelect);
            pstm.setInt(1, idProduto);
            rs = pstm.executeQuery();

            if (rs.next()) {
                rs.close();
                pstm.close();

                pstm = conexao.prepareStatement(sqlUpdate);
                pstm.setInt(1, quantidadeComprada);
                pstm.setInt(2, idProduto);
                pstm.executeUpdate();
            } else {
                rs.close();
                pstm.close();

                pstm = conexao.prepareStatement(sqlInsert);
                pstm.setInt(1, idProduto);
                pstm.setInt(2, quantidadeComprada);
                pstm.executeUpdate();
            }

            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar estoque: " + e.getMessage());
            return false;

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
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
}
