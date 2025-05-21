package E2E;

import Conexao.produtoDAO;
import Conexao.usuarioDAO;
import dados.ProdutoDados;
import dados.UsuarioDados;
import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JOptionPane;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import principal.cadastroUsuario;
import principal.Login;
import principal.CadastrarProduto;

public class E2ETest {

    @Test
    public void testFluxoCompletoCadastroLoginProduto() throws Exception {
        // ----------- FASE 1: CADASTRO DO USUÁRIO -----------

        // Mocks dos componentes do cadastro de usuário
        JTextField txtNomeMock = mock(JTextField.class);
        JTextField txtEmailMock = mock(JTextField.class);
        JPasswordField txtSenhaMock = mock(JPasswordField.class);
        JRadioButton rdbAdmMock = mock(JRadioButton.class);
        JRadioButton rdbAtendenteMock = mock(JRadioButton.class);
        usuarioDAO usuarioDaoMock = mock(usuarioDAO.class);

        // Configura os valores simulados para o cadastro
        when(txtNomeMock.getText()).thenReturn("Maria");
        when(txtEmailMock.getText()).thenReturn("maria@gmail.com");
        when(txtSenhaMock.getText()).thenReturn("senha123");
        when(rdbAdmMock.isSelected()).thenReturn(true);
        when(rdbAtendenteMock.isSelected()).thenReturn(false);
        when(usuarioDaoMock.cadastrarUsuario(any(UsuarioDados.class))).thenReturn(true);

        // Instancia a tela de cadastro e injeta dependências
        cadastroUsuario telaCadastro = new cadastroUsuario();
        telaCadastro.setUsuarioDAO(usuarioDaoMock);
        injectPrivateField(telaCadastro, "txtNome", txtNomeMock);
        injectPrivateField(telaCadastro, "txtEmail", txtEmailMock);
        injectPrivateField(telaCadastro, "txtSenha", txtSenhaMock);
        injectPrivateField(telaCadastro, "rdbAdministrador", rdbAdmMock);
        injectPrivateField(telaCadastro, "rdbAtendente", rdbAtendenteMock);

        // Executa o método
        telaCadastro.cadastrarUsuario();

        // Verifica se o DAO foi chamado corretamente
        verify(usuarioDaoMock, times(1)).cadastrarUsuario(any(UsuarioDados.class));

        // ----------- FASE 2: LOGIN DO USUÁRIO -----------
        JTextField txtEmailLoginMock = mock(JTextField.class);
        JPasswordField txtSenhaLoginMock = mock(JPasswordField.class);
        usuarioDAO loginDaoMock = usuarioDaoMock; 

        ResultSet rsLoginMock = mock(ResultSet.class);

        when(txtEmailLoginMock.getText()).thenReturn("maria@gmail.com");
        when(txtSenhaLoginMock.getText()).thenReturn("senha123");

        when(loginDaoMock.autenticacaoUsuario(any(UsuarioDados.class))).thenReturn(rsLoginMock);
        when(rsLoginMock.next()).thenReturn(true);
        when(rsLoginMock.getString("tipo_usuario")).thenReturn("ADMINISTRADOR");

        Login telaLogin = new Login();
        telaLogin.setUsuarioDAO(loginDaoMock);
        injectPrivateField(telaLogin, "txtEmail", txtEmailLoginMock);
        injectPrivateField(telaLogin, "txtSenha", txtSenhaLoginMock);

        telaLogin.LogarUsuario();

        // ----------- FASE 3: CADASTRO DE PRODUTO -----------
        // Mocks dos componentes do cadastro de produto
        JTextField txtNomeProdutoMock = mock(JTextField.class);
        JTextField txtValorProdutoMock = mock(JTextField.class);
        JTextField txtEANProdutoMock = mock(JTextField.class);
        produtoDAO produtoDaoMock = mock(produtoDAO.class);

        when(txtNomeProdutoMock.getText()).thenReturn("Produto XPTO");
        when(txtValorProdutoMock.getText()).thenReturn("99,99");
        when(txtEANProdutoMock.getText()).thenReturn("7896541237890");

        // Instancia a tela de cadastro de produto e injeta dependências
        CadastrarProduto telaProduto = new CadastrarProduto();
        injectPrivateField(telaProduto, "txtNome", txtNomeProdutoMock);
        injectPrivateField(telaProduto, "txtValor", txtValorProdutoMock);
        injectPrivateField(telaProduto, "txtEAN", txtEANProdutoMock);
        injectPrivateField(telaProduto, "produtoDAO", produtoDaoMock);

        // Mocka JOptionPane para não abrir popups no teste
        try (MockedStatic<JOptionPane> mocked = mockStatic(JOptionPane.class)) {
            telaProduto.cadastrarProduto();

            // Captura o objeto ProdutoDados que foi passado para o DAO
            ArgumentCaptor<ProdutoDados> captor = ArgumentCaptor.forClass(ProdutoDados.class);
            verify(produtoDaoMock).cadastrarProduto(captor.capture());

            ProdutoDados produto = captor.getValue();
            assertEquals("Produto XPTO", produto.getNome_produto());
            assertEquals(99.99, produto.getValor(), 0.001);
            assertEquals("7896541237890", produto.getEan_produto());
        }

        // ----------- VERIFICAÇÕES FINAIS -----------
        verify(usuarioDaoMock).cadastrarUsuario(any(UsuarioDados.class));
        verify(usuarioDaoMock).autenticacaoUsuario(any(UsuarioDados.class));
    }

    private void injectPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

}
