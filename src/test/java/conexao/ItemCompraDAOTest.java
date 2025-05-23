
package conexao;

import dados.ItemCompraDados;
import Conexao.EstoqueDAO;
import Conexao.ItemCompraDAO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ItemCompraDAOTest {
    
@Test
public void testCadastrarItem_ProdutoExistente() throws Exception {
    // Arrange
    ItemCompraDados item = new ItemCompraDados();
    item.setId_compra(1);
    item.setId_produto(100);
    item.setQuantidade_produto(5);
    item.setTotal_produto(50.0);
    item.setDesconto_produto(0.0);

    Connection mockConexao = mock(Connection.class);
    PreparedStatement mockPstm = mock(PreparedStatement.class);
    EstoqueDAO mockEstoqueDAO = mock(EstoqueDAO.class);

    // Mock comportamento do prepareStatement e execute do PreparedStatement
    when(mockConexao.prepareStatement(anyString())).thenReturn(mockPstm);
    when(mockPstm.execute()).thenReturn(true);

    // Mock do método atualizarEstoqueCompra
    when(mockEstoqueDAO.atualizarEstoqueCompra(100, 5)).thenReturn(true);

    // Cria spy da classe ItemCompraDAO
    ItemCompraDAO dao = spy(new ItemCompraDAO());

    // Mocka os métodos getConnection e getEstoqueDAO para retornar os mocks criados
    doReturn(mockConexao).when(dao).getConnection();
    doReturn(mockEstoqueDAO).when(dao).getEstoqueDAO();

    // Act
    boolean resultado = dao.cadastrarItem(item);

    // Assert
    assertTrue(resultado);
    verify(mockPstm).execute();
    verify(mockEstoqueDAO).atualizarEstoqueCompra(100, 5);
}

    
}
