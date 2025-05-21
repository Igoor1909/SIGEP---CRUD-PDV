package Conexao;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import dados.ProdutoDados;

public class produtoDAO {

    Connection conexao;
    PreparedStatement pstm = null;
    ResultSet rs;
    
    

    public void cadastrarProduto(ProdutoDados objProdutos) {
        Connection conexao = null;
        PreparedStatement pstmProduto = null;
        PreparedStatement pstmEstoque = null;
        ResultSet generatedKeys = null;

        try {
            if (getEanProduto(objProdutos.getEan_produto())) {
                JOptionPane.showMessageDialog(null, "Código EAN já cadastrado!");
                return;
            }

            conexao = new conexao().getConexao();
            conexao.setAutoCommit(false);

            String sqlProduto = "INSERT INTO produto (nome_produto, valor_produto, ean_produto) VALUES (?, ?, ?)";
            pstmProduto = conexao.prepareStatement(sqlProduto, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmProduto.setString(1, objProdutos.getNome_produto());
            pstmProduto.setDouble(2, objProdutos.getValor());
            pstmProduto.setString(3, objProdutos.getEan_produto());
            pstmProduto.executeUpdate();

            generatedKeys = pstmProduto.getGeneratedKeys();
            int idProduto;
            if (generatedKeys.next()) {
                idProduto = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Falha ao obter ID do produto cadastrado");
            }

            String sqlEstoque = "INSERT INTO estoque (id_produto, quantidade_produto) VALUES (?, 0)";
            pstmEstoque = conexao.prepareStatement(sqlEstoque);
            pstmEstoque.setInt(1, idProduto);
            pstmEstoque.executeUpdate();

            conexao.commit();
            JOptionPane.showMessageDialog(null, "Produto cadastrado com sucesso e estoque inicializado!");

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
                if (generatedKeys != null) {
                    generatedKeys.close();
                }
                if (pstmProduto != null) {
                    pstmProduto.close();
                }
                if (pstmEstoque != null) {
                    pstmEstoque.close();
                }
                if (conexao != null) {
                    conexao.close();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao fechar conexões: " + ex.getMessage());
            }
        }
    }

    public boolean getEanProduto(String ean) {
        String sql = "SELECT ean_produto FROM produto WHERE ean_produto = ? LIMIT 1";
        conexao = new conexao().getConexao();
        try {
            pstm = conexao.prepareStatement(sql);
            pstm.setString(1, ean);
            ResultSet rs = pstm.executeQuery();
            boolean existe = rs.next();
            rs.close();
            pstm.close();
            return existe;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ProdutoDAO eanJaCadastrado " + e);
            return true;
        }
    }

    public ProdutoDados getProdutoDados(int id_produto) {
        conexao = new conexao().getConexao();

        String sql = "SELECT * FROM produto WHERE id_produto = ?";
        try {
            PreparedStatement stmt = this.conexao.prepareStatement(sql);
            stmt.setInt(1, id_produto);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ProdutoDados objProduto = new ProdutoDados();
                objProduto.setNome_produto(rs.getString("nome_produto"));
                objProduto.setValor(rs.getDouble("valor_produto"));
                objProduto.setEan_produto(rs.getString("ean_produto"));
                
                rs.close();
                stmt.close();
                conexao.close();

                return objProduto;
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

    public void alterarProduto(ProdutoDados objProduto) {
        String sql = "update produto set nome_produto = ?, valor = ? where id_produto = ?";
        conexao = new conexao().getConexao();

        try {
            pstm = conexao.prepareStatement(sql);
            pstm.setString(1, objProduto.getNome_produto());
            pstm.setDouble(2, objProduto.getValor());
            pstm.setInt(3, objProduto.getId_produto());

            pstm.execute();
            pstm.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ClienteDAO AlterarProduto " + e);
        }
    }

    public void excluirProduto(ProdutoDados objProduto) {
        String sql = "delete from produto where id_produto = ?";
        conexao = new conexao().getConexao();

        try {
            pstm = conexao.prepareStatement(sql);
            pstm.setInt(1, objProduto.getId_produto());

            pstm.execute();
            pstm.close();
            JOptionPane.showMessageDialog(null, "Produto excluido!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Produto ja utilizado em pedidos!\nImpossivel excluir!");
        }
    }

}
