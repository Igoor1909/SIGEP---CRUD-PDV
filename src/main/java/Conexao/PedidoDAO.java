package Conexao;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import dados.PedidoDados;

public class PedidoDAO {

    Connection conexao;
    PreparedStatement pstm = null;
    ResultSet rs;

    public void RealizarPedido(PedidoDados objPedido) {
        String sql = "insert into pedido (id_cliente, id_produto, quantidade_produto, valor_unidade, valor_total, forma_pagamento, data_retirada, status_pedido) values (?,?,?,?,?,?,?,?) ";
        conexao = new conexao().getConexao();

        try {
            pstm = conexao.prepareStatement(sql);
            pstm.setInt(1, objPedido.getId_cliente());
            pstm.setInt(2, objPedido.getId_produto());
            pstm.setInt(3, objPedido.getQuantidade());
            pstm.setDouble(4, objPedido.getValor_unitario());
            pstm.setDouble(5, objPedido.getValor_total());
            pstm.setString(6, objPedido.getForma_pagamento());
            pstm.setString(7, objPedido.getData_retirada());
            pstm.setString(8, objPedido.getStatus());

            pstm.execute();
            pstm.close();
        } catch (SQLException e) {
            System.out.println("RealizarPedido PedidoDAO" + e);
        }
    }

    public PedidoDados getPedidoDados(int id_pedido) {
        conexao = new conexao().getConexao();

        String sql = "SELECT * FROM pedido WHERE id_pedido = ?";

        try {
            PreparedStatement stmt = this.conexao.prepareStatement(sql);
            stmt.setInt(1, id_pedido);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                PedidoDados objPedido = new PedidoDados();
                objPedido.setId_cliente(rs.getInt("id_cliente"));
                objPedido.setId_produto(rs.getInt("id_produto"));
                objPedido.setQuantidade(rs.getInt("quantidade_produto"));
                objPedido.setValor_unitario(rs.getDouble("valor_unidade"));
                objPedido.setValor_total(rs.getDouble("valor_total"));
                objPedido.setForma_pagamento(rs.getString("forma_pagamento"));
                objPedido.setData_retirada(rs.getString("data_retirada"));
                objPedido.setStatus(rs.getString("status_pedido"));

                rs.close();
                stmt.close();
                conexao.close();

                return objPedido;
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
    
    public void finalizarPedido(PedidoDados objPedido){
        String sql = "update pedido set status_pedido = 'Finalizado' where id_pedido = ?";
        conexao = new conexao().getConexao();
        
        try{
            pstm = conexao.prepareStatement(sql);
            pstm.setInt(1, objPedido.getId_pedido());
            
            pstm.execute();
            pstm.close();
        }catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "PedidoDAO FinalizarPedido " + e);
        }
    }

    public void alterarPedido(PedidoDados objPedido) {
        String sql = "update pedido set id_cliente = ?, id_produto = ?, quantidade_produto = ?, valor_unidade = ?, valor_total = ?, forma_pagamento = ?, data_retirada = ?, status_pedido = ? where id_pedido = ?";
        conexao = new conexao().getConexao();

        try {
            pstm = conexao.prepareStatement(sql);
            pstm.setInt(1, objPedido.getId_cliente());
            pstm.setInt(2, objPedido.getId_produto());
            pstm.setInt(3, objPedido.getQuantidade());
            pstm.setDouble(4, objPedido.getValor_unitario());
            pstm.setDouble(5, objPedido.getValor_total());
            pstm.setString(6, objPedido.getForma_pagamento());
            pstm.setString(7, objPedido.getData_retirada());
            pstm.setString(8, objPedido.getStatus());
            pstm.setInt(9, objPedido.getId_pedido());

            pstm.execute();
            pstm.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "PedidoDAO AlterarPedido " + e);
        }
    }

    public void excluirPedido(PedidoDados objPedido) {
        String sql = "delete from pedido where id_pedido = ?";
        conexao = new conexao().getConexao();

        try {
            pstm = conexao.prepareStatement(sql);
            pstm.setInt(1, objPedido.getId_pedido());

            pstm.execute();
            pstm.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "PedidoDAO ExcluirPedido " + e);
        }
    }

}
