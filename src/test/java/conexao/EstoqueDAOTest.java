package conexao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Conexao.EstoqueDAO;
import Conexao.conexao;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class EstoqueDAOTest {

    private conexao conexaoMock;
    private Connection connectionMock;
    private PreparedStatement preparedStatementMock;
    private ResultSet resultSetMock;
    private EstoqueDAO estoqueDAO;

    @Test
    public void testAtualizarEstoqueProdutoExistente() throws Exception {
        // Mocks necessários
        conexao conexaoMock = mock(conexao.class);
        Connection connectionMock = mock(Connection.class);
        PreparedStatement preparedStatementMock = mock(PreparedStatement.class);
        ResultSet resultSetMock = mock(ResultSet.class);

        // Simulações
        when(conexaoMock.getConexao()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        // DAO com mock injetado
        EstoqueDAO estoqueDAO = new EstoqueDAO(conexaoMock);

        // Executa
        boolean resultado = estoqueDAO.atualizarEstoque(1, 5);

        // Verificações
        assertTrue(resultado);
        verify(preparedStatementMock, atLeastOnce()).executeUpdate();

        
    }

}
