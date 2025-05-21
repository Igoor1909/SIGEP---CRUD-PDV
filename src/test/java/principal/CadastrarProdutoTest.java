package principal;

import Conexao.produtoDAO;
import dados.ProdutoDados;
import org.junit.jupiter.api.Test;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

public class CadastrarProdutoTest {

    @Test
    public void testCadastrarProdutoComDadosValidosTest() throws Exception {
        // Mock dos campos JTextField
        JTextField txtNomeMock = mock(JTextField.class);
        JTextField txtValorMock = mock(JTextField.class);
        JTextField txtEANMock = mock(JTextField.class);

        // Mock do DAO
        produtoDAO daoMock = mock(produtoDAO.class);

        // Dados simulados
        when(txtNomeMock.getText()).thenReturn("Produto Teste");
        when(txtValorMock.getText()).thenReturn("12,50");
        when(txtEANMock.getText()).thenReturn("7891234567890");

        // Instancia da classe que tem o método cadastrarProduto
        CadastrarProduto cadastro = new CadastrarProduto();

        // Injeta mocks nos campos privados via reflexão
        injectPrivateField(cadastro, "txtNome", txtNomeMock);
        injectPrivateField(cadastro, "txtValor", txtValorMock);
        injectPrivateField(cadastro, "txtEAN", txtEANMock);

        // Injeta DAO mockado (crie setter ou campo)
        injectPrivateField(cadastro, "produtoDAO", daoMock);

        // Mocka JOptionPane para não mostrar janelas
        try (MockedStatic<JOptionPane> mocked = mockStatic(JOptionPane.class)) {
            cadastro.cadastrarProduto();

            // Verifica se o DAO foi chamado com os dados corretos
            ArgumentCaptor<ProdutoDados> captor = ArgumentCaptor.forClass(ProdutoDados.class);
            verify(daoMock).cadastrarProduto(captor.capture());

            ProdutoDados capturado = captor.getValue();
            assertEquals("Produto Teste", capturado.getNome_produto());
            assertEquals(12.50, capturado.getValor(), 0.001);
            assertEquals("7891234567890", capturado.getEan_produto());

            // Verifica que nenhuma mensagem de erro foi mostrada
            mocked.verifyNoInteractions();
        }
    }

    @Test
    public void testCadastrarProdutoComEanDuplicado() throws Exception {
        // Mock dos campos JTextField
        JTextField txtNomeMock = mock(JTextField.class);
        JTextField txtValorMock = mock(JTextField.class);
        JTextField txtEANMock = mock(JTextField.class);

        // Dados simulados
        when(txtNomeMock.getText()).thenReturn("Produto Teste");
        when(txtValorMock.getText()).thenReturn("12,50");
        when(txtEANMock.getText()).thenReturn("1234567890123");

        // Mock do DAO que simula EAN duplicado com mensagem
        produtoDAO daoMock = mock(produtoDAO.class);
        doAnswer(invocation -> {
            JOptionPane.showMessageDialog(null, "Código EAN já cadastrado!");
            return null;
        }).when(daoMock).cadastrarProduto(any());

        // Instância da classe principal
        CadastrarProduto cadastro = new CadastrarProduto();

        // Injeta mocks nos campos privados via reflexão
        injectPrivateField(cadastro, "txtNome", txtNomeMock);
        injectPrivateField(cadastro, "txtValor", txtValorMock);
        injectPrivateField(cadastro, "txtEAN", txtEANMock);
        injectPrivateField(cadastro, "produtoDAO", daoMock);

        // Mocka JOptionPane
        try (MockedStatic<JOptionPane> mocked = mockStatic(JOptionPane.class)) {
            cadastro.cadastrarProduto();

            // Verifica que o DAO foi chamado
            verify(daoMock).cadastrarProduto(any());

            // Verifica que a mensagem de EAN duplicado foi exibida
            mocked.verify(() -> JOptionPane.showMessageDialog(null, "Código EAN já cadastrado!"));
        }
    }

    @Test
    public void testCadastrarProdutoComCamposVazios() throws Exception {
        // Mock dos campos JTextField
        JTextField txtNomeMock = mock(JTextField.class);
        JTextField txtValorMock = mock(JTextField.class);
        JTextField txtEANMock = mock(JTextField.class);

        // Dados simulados - todos vazios
        when(txtNomeMock.getText()).thenReturn("");
        when(txtValorMock.getText()).thenReturn("");
        when(txtEANMock.getText()).thenReturn("");

        // Mock do DAO
        produtoDAO daoMock = mock(produtoDAO.class);

        // Instância da classe principal
        CadastrarProduto cadastro = new CadastrarProduto();

        // Injeta mocks nos campos privados via reflexão
        injectPrivateField(cadastro, "txtNome", txtNomeMock);
        injectPrivateField(cadastro, "txtValor", txtValorMock);
        injectPrivateField(cadastro, "txtEAN", txtEANMock);
        injectPrivateField(cadastro, "produtoDAO", daoMock);

        // Mocka JOptionPane
        try (MockedStatic<JOptionPane> mocked = mockStatic(JOptionPane.class)) {
            cadastro.cadastrarProduto();

            // Verifica que o DAO *não* foi chamado
            verify(daoMock, never()).cadastrarProduto(any());

            // Verifica que a mensagem de erro foi exibida
            mocked.verify(() -> JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos!"));
        }
    }

    private void injectPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

}
