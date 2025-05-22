package Conexao;

import dados.CompraDados;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class CompraDAO {

    private conexao conexaoDB;

    public CompraDAO(conexao conexaoDB) {
        this.conexaoDB = conexaoDB;
    }
    

    public int cadastrarCompra(CompraDados dados) {
        String sql = "INSERT INTO compra (id_fornecedor, total_compra, pagamento_compra) VALUES (?, ?, ?)";
        int idCompraGerado = -1;

        try {
            Connection conexao = conexaoDB.getConexao();
            PreparedStatement pstm = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstm.setInt(1, dados.getId_fornecedor());
            pstm.setDouble(2, dados.getTotal_venda());
            pstm.setString(3, dados.getPagamento_compra());

            int rowsAffected = pstm.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = pstm.getGeneratedKeys();
                if (generatedKeys.next()) {
                    idCompraGerado = generatedKeys.getInt(1);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar compra: " + e.getMessage());
        }

        return idCompraGerado;
    }

}
