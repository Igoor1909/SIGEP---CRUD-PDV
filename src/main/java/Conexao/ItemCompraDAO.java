package Conexao;

import dados.ItemCompraDados;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ItemCompraDAO {

    private Connection conexao;
    private PreparedStatement pstm;

    // Método protegido para obter a conexão (pode ser mockado no teste)
    public Connection getConnection() throws SQLException {
        return new conexao().getConexao();
    }

    // Método protegido para obter o EstoqueDAO (pode ser mockado no teste)
    public EstoqueDAO getEstoqueDAO() {
        return new EstoqueDAO(new conexao());
    }

    public boolean cadastrarItem(ItemCompraDados item) {
        String sql = "INSERT INTO item_compra (id_compra, id_produto, quantidade_produto, total_unitario, desconto_produto) VALUES (?, ?, ?, ?, ?)";

        try {
            conexao = getConnection();  // usa método ao invés de criar direto
            pstm = conexao.prepareStatement(sql);

            pstm.setInt(1, item.getId_compra());
            pstm.setInt(2, item.getId_produto());
            pstm.setInt(3, item.getQuantidade_produto());
            pstm.setDouble(4, item.getTotal_produto());
            pstm.setDouble(5, item.getDesconto_produto());

            pstm.execute();

            EstoqueDAO estoqueDAO = getEstoqueDAO();  // usa método ao invés de criar direto

            boolean estoqueAtualizado = estoqueDAO.atualizarEstoqueCompra(item.getId_produto(), item.getQuantidade_produto());

            if (!estoqueAtualizado) {
                JOptionPane.showMessageDialog(null, "Erro ao atualizar estoque para o produto " + item.getId_produto());
                return false;
            }

            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar item da compra: " + e.getMessage());
            return false;

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

}
