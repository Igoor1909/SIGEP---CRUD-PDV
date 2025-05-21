package principal;

import Conexao.FornecedorDAO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.lang.reflect.Field;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

public class CadastrarFornecedorTest {

    @Test
    public void testCadastrarFornecedorComDadosValidos() throws Exception {
        // Mocks dos campos da interface
        JTextField txtNomeMock = mock(JTextField.class);
        JTextField txtCNPJMock = mock(JTextField.class);
        JTextField txtRuaMock = mock(JTextField.class);
        JTextField txtNumeroMock = mock(JTextField.class);
        JTextField txtComplementoMock = mock(JTextField.class);
        JTextField txtCidadeMock = mock(JTextField.class);
        JTextField txtEstadoMock = mock(JTextField.class);
        JTextField txtBairroMock = mock(JTextField.class);
        FornecedorDAO daoMock = mock(FornecedorDAO.class);

        // Dados simulados
        when(txtNomeMock.getText()).thenReturn("Fornecedor X");
        when(txtCNPJMock.getText()).thenReturn("12345678900012");
        when(txtRuaMock.getText()).thenReturn("Rua Exemplo");
        when(txtNumeroMock.getText()).thenReturn("123");
        when(txtComplementoMock.getText()).thenReturn("Bloco A");
        when(txtCidadeMock.getText()).thenReturn("São Paulo");
        when(txtEstadoMock.getText()).thenReturn("São Paulo");
        when(txtBairroMock.getText()).thenReturn("Centro");

        // Instância da tela de cadastro e injeção dos mocks
        CadastrarFornecedor tela = new CadastrarFornecedor(); // sua classe principal
        injectPrivateField(tela, "txtNomeFornecedor", txtNomeMock);
        injectPrivateField(tela, "txtCNPJFornecedor", txtCNPJMock);
        injectPrivateField(tela, "txtRuaFornecedor", txtRuaMock);
        injectPrivateField(tela, "txtNumeroFornecedor", txtNumeroMock);
        injectPrivateField(tela, "txtComplementoFornecedor", txtComplementoMock);
        injectPrivateField(tela, "txtCidadeFornecedor", txtCidadeMock);
        injectPrivateField(tela, "txtEstadoFornecedor", txtEstadoMock);
        injectPrivateField(tela, "txtBairroFornecedor", txtBairroMock);

        // Executa o método de cadastro
        tela.cadastrarFornecedor();

        // Verifica se o DAO foi chamado (você precisará ajustar a classe para injetar o DAO mockado, se desejar testar isso também)
        // verify(daoMock, times(1)).cadastrarFornecedor(any(FornecedorDados.class));
    }

    @Test
    public void testCadastrarFornecedorComNomeVazio() throws Exception {
        CadastrarFornecedor cadastro = new CadastrarFornecedor();

        // Mock dos campos
        JTextField mockNome = mock(JTextField.class);
        JTextField mockCNPJ = mock(JTextField.class);
        JTextField mockRua = mock(JTextField.class);
        JTextField mockNumero = mock(JTextField.class);
        JTextField mockCidade = mock(JTextField.class);
        JTextField mockEstado = mock(JTextField.class);
        JTextField mockBairro = mock(JTextField.class);
        JTextField mockComplemento = mock(JTextField.class);

        when(mockNome.getText()).thenReturn(""); // Nome vazio
        when(mockCNPJ.getText()).thenReturn("12345678000100");
        when(mockRua.getText()).thenReturn("Rua A");
        when(mockNumero.getText()).thenReturn("100");
        when(mockCidade.getText()).thenReturn("São Paulo");
        when(mockEstado.getText()).thenReturn("São Paulo");
        when(mockBairro.getText()).thenReturn("Centro");
        when(mockComplemento.getText()).thenReturn("Apto 10");

        injectPrivateField(cadastro, "txtNomeFornecedor", mockNome);
        injectPrivateField(cadastro, "txtCNPJFornecedor", mockCNPJ);
        injectPrivateField(cadastro, "txtRuaFornecedor", mockRua);
        injectPrivateField(cadastro, "txtNumeroFornecedor", mockNumero);
        injectPrivateField(cadastro, "txtCidadeFornecedor", mockCidade);
        injectPrivateField(cadastro, "txtEstadoFornecedor", mockEstado);
        injectPrivateField(cadastro, "txtBairroFornecedor", mockBairro);
        injectPrivateField(cadastro, "txtComplementoFornecedor", mockComplemento);

        FornecedorDAO mockDAO = mock(FornecedorDAO.class);
        cadastro.setFornecedorDAO(mockDAO);

        try (MockedStatic<JOptionPane> mocked = mockStatic(JOptionPane.class)) {
            cadastro.cadastrarFornecedor();

            // Captura o segundo argumento passado à chamada (a mensagem de erro)
            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
            mocked.verify(() -> JOptionPane.showMessageDialog(any(), captor.capture()));

            // Verifica se a mensagem capturada contém o texto esperado
            String mensagem = captor.getValue();
            assertTrue(mensagem.contains("preencha todos os campos"), "Mensagem não esperada: " + mensagem);

            // Verifica que o DAO não foi chamado
            verify(mockDAO, never()).cadastrarFornecedor(any());
        }
    }

    @Test
    public void testCadastrarFornecedorComEstadoInvalido() throws Exception {
        CadastrarFornecedor cadastro = new CadastrarFornecedor();

        // Mock dos campos
        JTextField mockNome = mock(JTextField.class);
        JTextField mockCNPJ = mock(JTextField.class);
        JTextField mockRua = mock(JTextField.class);
        JTextField mockNumero = mock(JTextField.class);
        JTextField mockCidade = mock(JTextField.class);
        JTextField mockEstado = mock(JTextField.class);
        JTextField mockBairro = mock(JTextField.class);
        JTextField mockComplemento = mock(JTextField.class);

        when(mockNome.getText()).thenReturn("Fornecedor X");
        when(mockCNPJ.getText()).thenReturn("12345678000199");
        when(mockRua.getText()).thenReturn("Rua A");
        when(mockNumero.getText()).thenReturn("123");
        when(mockCidade.getText()).thenReturn("Campinas");
        when(mockEstado.getText()).thenReturn("sp"); // estado inválido
        when(mockBairro.getText()).thenReturn("Centro");
        when(mockComplemento.getText()).thenReturn("Apto 1");

        injectPrivateField(cadastro, "txtNomeFornecedor", mockNome);
        injectPrivateField(cadastro, "txtCNPJFornecedor", mockCNPJ);
        injectPrivateField(cadastro, "txtRuaFornecedor", mockRua);
        injectPrivateField(cadastro, "txtNumeroFornecedor", mockNumero);
        injectPrivateField(cadastro, "txtCidadeFornecedor", mockCidade);
        injectPrivateField(cadastro, "txtEstadoFornecedor", mockEstado);
        injectPrivateField(cadastro, "txtBairroFornecedor", mockBairro);
        injectPrivateField(cadastro, "txtComplementoFornecedor", mockComplemento);

        // Mock do DAO
        FornecedorDAO mockDAO = mock(FornecedorDAO.class);
        cadastro.setFornecedorDAO(mockDAO);

        try (MockedStatic<JOptionPane> mocked = mockStatic(JOptionPane.class)) {
            cadastro.cadastrarFornecedor();

            // Captura os argumentos passados ao showMessageDialog
            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
            mocked.verify(() -> JOptionPane.showMessageDialog(any(), captor.capture(), any(), anyInt()));

            // Valida se a mensagem de erro de estado inválido foi mostrada
            String mensagem = captor.getValue();
            assertTrue(mensagem.contains("Estado inválido"), "A mensagem não contém 'Estado inválido'");
        }

        // Garante que o DAO não foi chamado
        verify(mockDAO, never()).cadastrarFornecedor(any());
    }

    private void injectPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

}
