package conexao;

import dados.CompraDados;
import Conexao.CompraDAO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

public class CompraDAOTest {

    @Test
    public void testCadastrarCompraSucesso() throws Exception {
        CompraDAO compraDAOMock = Mockito.mock(CompraDAO.class);

        CompraDados compra = new CompraDados();
        compra.setId_fornecedor(1);
        compra.setTotal_venda(250.75);
        compra.setPagamento_compra("Cartão");

        when(compraDAOMock.cadastrarCompra(compra)).thenReturn(101); // Simula ID gerado

        int idCompra = compraDAOMock.cadastrarCompra(compra);

        System.out.println("✅ Compra cadastrada com sucesso! ID gerado: " + idCompra);

        verify(compraDAOMock, times(1)).cadastrarCompra(compra);
    }
}
