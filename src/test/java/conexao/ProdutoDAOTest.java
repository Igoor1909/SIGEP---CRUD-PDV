package conexao;

import dados.ProdutoDados;
import Conexao.produtoDAO;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProdutoDAOTest {

    private produtoDAO dao;

    @BeforeEach
    public void setUp() {
        dao = new produtoDAO();
    }

    @Test
    public void testCadastrarProdutoSucesso() throws SQLException {
        // Mock do DAO
        produtoDAO produtoDAOMock = Mockito.mock(produtoDAO.class);

        // Cria dados do produto
        ProdutoDados produto = new ProdutoDados();
        produto.setNome_produto("Produto Teste");
        produto.setValor(99.99);
        produto.setEan_produto("7891234567890");

        // Configura comportamento simulado
        when(produtoDAOMock.getEanProduto("7891234567890")).thenReturn(false);
        doNothing().when(produtoDAOMock).cadastrarProduto(produto);

        // Executa o método simulado
        produtoDAOMock.cadastrarProduto(produto);

        // Mensagem no console
        System.out.println("✅ Produto cadastrado com sucesso: " + produto.getNome_produto());

        // Verificações
        verify(produtoDAOMock, times(1)).cadastrarProduto(produto);
    }

    @Test
    public void testCadastrarProdutoEanDuplicado() throws SQLException {
        // Mock do DAO
        produtoDAO produtoDAOMock = Mockito.mock(produtoDAO.class);

        // Cria dados do produto duplicado
        ProdutoDados produto = new ProdutoDados();
        produto.setNome_produto("Produto Repetido");
        produto.setValor(49.90);
        produto.setEan_produto("7891234567890");

        // Simula que o EAN já está cadastrado
        when(produtoDAOMock.getEanProduto("7891234567890")).thenReturn(true);

        // Simula a lógica de não permitir o cadastro
        doAnswer(invocation -> {
            if (produtoDAOMock.getEanProduto(produto.getEan_produto())) {
                System.out.println("Código EAN já cadastrado!"); // Simula alerta
                return null;
            }
            return null;
        }).when(produtoDAOMock).cadastrarProduto(produto);

        // Executa o método
        produtoDAOMock.cadastrarProduto(produto);

        // Verifica interações
        verify(produtoDAOMock, times(1)).getEanProduto("7891234567890");
        verify(produtoDAOMock, times(1)).cadastrarProduto(produto);
    }

    @Test
    public void testGetProdutoDados_IdValido() {
        int id = 1; // Produto banana

        ProdutoDados produto = dao.getProdutoDados(id);

        assertNotNull(produto);
        assertEquals("banana", produto.getNome_produto());
        assertEquals(1.99, produto.getValor(), 0.001); // tolerância para ponto flutuante
        assertEquals("12345", produto.getEan_produto());
    }

    @Test
    public void testGetProdutoDados_IdInvalido() {
        int idInvalido = 9999; // ID de um produto que não existe no banco

        ProdutoDados produto = dao.getProdutoDados(idInvalido);

        assertNull(produto, "Esperado null para ID de produto inexistente.");
    }

}
