package principal;

import Conexao.usuarioDAO;
import dados.UsuarioDados;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginTest {

    @Test
    public void testLoginUsuarioValido() throws Exception {
        // Mocks dos campos da interface
        JTextField txtEmailMock = mock(JTextField.class);
        JPasswordField txtSenhaMock = mock(JPasswordField.class);
        usuarioDAO daoMock = mock(usuarioDAO.class);
        ResultSet rsMock = mock(ResultSet.class);

        // Configuração dos mocks
        when(txtEmailMock.getText()).thenReturn("usuario@outlook.com");
        when(txtSenhaMock.getText()).thenReturn("123456");
        when(daoMock.autenticacaoUsuario(any(UsuarioDados.class))).thenReturn(rsMock);
        when(rsMock.next()).thenReturn(true);
        when(rsMock.getString("tipo_usuario")).thenReturn("ADMINISTRADOR");

        // Instancia a tela de login e injeta os mocks
        Login loginTela = new Login();
        loginTela.setUsuarioDAO(daoMock);
        injectPrivateField(loginTela, "txtEmail", txtEmailMock);
        injectPrivateField(loginTela, "txtSenha", txtSenhaMock);

        // Executa
        loginTela.LogarUsuario();

        // Verifica se o método do DAO foi chamado
        verify(daoMock, times(1)).autenticacaoUsuario(any(UsuarioDados.class));
    }

    @Test
    public void testLoginUsuarioInvalido() throws Exception {
        JTextField txtEmailMock = mock(JTextField.class);
        JPasswordField txtSenhaMock = mock(JPasswordField.class);
        usuarioDAO daoMock = mock(usuarioDAO.class);
        ResultSet rsMock = mock(ResultSet.class);

        when(txtEmailMock.getText()).thenReturn("usuario@outlook.com");
        when(txtSenhaMock.getText()).thenReturn("senhaErrada");
        when(daoMock.autenticacaoUsuario(any(UsuarioDados.class))).thenReturn(rsMock);
        when(rsMock.next()).thenReturn(false); // simula usuário não encontrado

        Login loginTela = new Login();
        loginTela.setUsuarioDAO(daoMock);
        injectPrivateField(loginTela, "txtEmail", txtEmailMock);
        injectPrivateField(loginTela, "txtSenha", txtSenhaMock);

        loginTela.LogarUsuario();

        verify(daoMock, times(1)).autenticacaoUsuario(any(UsuarioDados.class));
    }

    @Test
    public void testLoginComCamposVazios() throws Exception {
        JTextField txtEmailMock = mock(JTextField.class);
        JPasswordField txtSenhaMock = mock(JPasswordField.class);
        usuarioDAO daoMock = mock(usuarioDAO.class);

        when(txtEmailMock.getText()).thenReturn(""); // campo vazio
        when(txtSenhaMock.getText()).thenReturn(""); // campo vazio

        Login loginTela = new Login();
        loginTela.setUsuarioDAO(daoMock);
        injectPrivateField(loginTela, "txtEmail", txtEmailMock);
        injectPrivateField(loginTela, "txtSenha", txtSenhaMock);

        loginTela.LogarUsuario();

        // Verifica que o DAO NÃO foi chamado (porque os campos estão vazios)
        verify(daoMock, never()).autenticacaoUsuario(any(UsuarioDados.class));
    }

    private void injectPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

}
