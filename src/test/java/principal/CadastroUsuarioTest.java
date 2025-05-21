package principal;

import Conexao.usuarioDAO;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import javax.swing.*;
import principal.cadastroUsuario;
import dados.UsuarioDados;
import java.lang.reflect.Field;


public class CadastroUsuarioTest {

    @Test
    public void testCadastrarUsuarioComDadosValidos() throws Exception {
        // Mocks dos componentes
        JTextField txtNomeMock = mock(JTextField.class);
        JTextField txtEmailMock = mock(JTextField.class);
        JPasswordField txtSenhaMock = mock(JPasswordField.class);
        JRadioButton rdbAdmMock = mock(JRadioButton.class);
        JRadioButton rdbAtendenteMock = mock(JRadioButton.class);
        usuarioDAO daoMock = mock(usuarioDAO.class);

        // Configura retorno dos mocks
        when(txtNomeMock.getText()).thenReturn("João");
        when(txtEmailMock.getText()).thenReturn("joao@gmail.com");
        when(txtSenhaMock.getText()).thenReturn("123456");
        when(rdbAdmMock.isSelected()).thenReturn(true);
        when(rdbAtendenteMock.isSelected()).thenReturn(false);
        when(daoMock.cadastrarUsuario(any(UsuarioDados.class))).thenReturn(true);

        // Instancia a classe real e injeta dependências
        cadastroUsuario cadastro = new cadastroUsuario();
        cadastro.setUsuarioDAO(daoMock); // injeta o DAO mockado

        // Injeta os mocks via reflexão (porque os campos são privados)
        injectPrivateField(cadastro, "txtNome", txtNomeMock);
        injectPrivateField(cadastro, "txtEmail", txtEmailMock);
        injectPrivateField(cadastro, "txtSenha", txtSenhaMock);
        injectPrivateField(cadastro, "rdbAdministrador", rdbAdmMock);
        injectPrivateField(cadastro, "rdbAtendente", rdbAtendenteMock);

        // Executa o método
        cadastro.cadastrarUsuario();

        // Verifica se o DAO foi chamado corretamente
        verify(daoMock, times(1)).cadastrarUsuario(any(UsuarioDados.class));
    }

    @Test
    public void testCadastrarUsuarioEmailInvalido() throws Exception {
        // Mocks dos componentes da interface
        JTextField txtNomeMock = mock(JTextField.class);
        JTextField txtEmailMock = mock(JTextField.class);
        JPasswordField txtSenhaMock = mock(JPasswordField.class);
        JRadioButton rdbAdmMock = mock(JRadioButton.class);
        JRadioButton rdbAtendenteMock = mock(JRadioButton.class);
        usuarioDAO daoMock = mock(usuarioDAO.class);

        // Configura os valores simulados dos campos
        when(txtNomeMock.getText()).thenReturn("João");
        when(txtEmailMock.getText()).thenReturn("joao@empresa.com"); // Email inválido
        when(txtSenhaMock.getText()).thenReturn("123456");
        when(rdbAdmMock.isSelected()).thenReturn(true);
        when(rdbAtendenteMock.isSelected()).thenReturn(false);

        // Instancia a classe real e injeta os mocks
        cadastroUsuario cadastro = new cadastroUsuario();
        cadastro.setUsuarioDAO(daoMock); // injeta o DAO mockado

        injectPrivateField(cadastro, "txtNome", txtNomeMock);
        injectPrivateField(cadastro, "txtEmail", txtEmailMock);
        injectPrivateField(cadastro, "txtSenha", txtSenhaMock);
        injectPrivateField(cadastro, "rdbAdministrador", rdbAdmMock);
        injectPrivateField(cadastro, "rdbAtendente", rdbAtendenteMock);

        // Executa o método
        cadastro.cadastrarUsuario();

        // Verifica que o DAO **não** foi chamado, pois o email é inválido
        verify(daoMock, never()).cadastrarUsuario(any(UsuarioDados.class));
    }

    @Test
    public void testCadastrarUsuarioComCampoNomeVazio() throws Exception {
        // Mocks dos componentes da interface
        JTextField txtNomeMock = mock(JTextField.class);
        JTextField txtEmailMock = mock(JTextField.class);
        JPasswordField txtSenhaMock = mock(JPasswordField.class);
        JRadioButton rdbAdmMock = mock(JRadioButton.class);
        JRadioButton rdbAtendenteMock = mock(JRadioButton.class);
        usuarioDAO daoMock = mock(usuarioDAO.class);

        // Configura valores dos mocks (nome vazio)
        when(txtNomeMock.getText()).thenReturn(""); // <- Nome vazio
        when(txtEmailMock.getText()).thenReturn("joao@gmail.com");
        when(txtSenhaMock.getText()).thenReturn("123456");
        when(rdbAdmMock.isSelected()).thenReturn(true);
        when(rdbAtendenteMock.isSelected()).thenReturn(false);

        // Instancia a classe real e injeta os mocks
        cadastroUsuario cadastro = new cadastroUsuario();
        cadastro.setUsuarioDAO(daoMock);

        injectPrivateField(cadastro, "txtNome", txtNomeMock);
        injectPrivateField(cadastro, "txtEmail", txtEmailMock);
        injectPrivateField(cadastro, "txtSenha", txtSenhaMock);
        injectPrivateField(cadastro, "rdbAdministrador", rdbAdmMock);
        injectPrivateField(cadastro, "rdbAtendente", rdbAtendenteMock);

        // Executa o método
        cadastro.cadastrarUsuario();

        // Verifica que o DAO **não** foi chamado
        verify(daoMock, never()).cadastrarUsuario(any(UsuarioDados.class));
    }

// Função auxiliar para injetar mocks em campos privados
    private void injectPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

}
