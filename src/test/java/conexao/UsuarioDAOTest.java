package conexao;

import dados.UsuarioDados;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import Conexao.usuarioDAO;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UsuarioDAOTest {

    private usuarioDAO usuarioDAO = new usuarioDAO();

    @Test
    public void testAutenticacaoUsuarioAdminSucesso() {
        UsuarioDados dados = new UsuarioDados();
        dados.setEmail_usuario("guilhere@gmail.com");
        dados.setSenha_usuario("12345");

        try {
            ResultSet rs = usuarioDAO.autenticacaoUsuario(dados);
            assertNotNull(rs, "ResultSet não deveria ser nulo para credenciais válidas");
            assertTrue(rs.next(), "Deveria encontrar o usuário admin");
            assertEquals("ADMINISTRADOR", rs.getString("tipo_usuario"));
        } catch (SQLException e) {
            fail("Exceção lançada: " + e.getMessage());
        }
    }

    @Test
    public void testAutenticacaoUsuarioAtendenteSucesso() {
        UsuarioDados dados = new UsuarioDados();
        dados.setEmail_usuario("igor@gmail.com");
        dados.setSenha_usuario("12345"); // Senha correta

        try {
            ResultSet rs = usuarioDAO.autenticacaoUsuario(dados);
            assertNotNull(rs, "ResultSet não deveria ser nulo para credenciais válidas");
            assertTrue(rs.next(), "Deveria encontrar o usuário atendente");
            assertEquals("ATENDENTE", rs.getString("tipo_usuario"));
        } catch (SQLException e) {
            fail("Exceção lançada: " + e.getMessage());
        }
    }

    @Test
    public void testAutenticacaoUsuarioEmailInvalido() {
        UsuarioDados dados = new UsuarioDados();
        dados.setEmail_usuario("naoexiste@gmail.com");
        dados.setSenha_usuario("12345");

        try {
            ResultSet rs = usuarioDAO.autenticacaoUsuario(dados);
            assertNotNull(rs, "ResultSet não deveria ser nulo mesmo para credenciais inválidas");
            assertFalse(rs.next(), "Não deveria encontrar usuário com email inválido");
        } catch (SQLException e) {
            fail("Exceção lançada: " + e.getMessage());
        }
    }

    @Test
    public void testAutenticacaoUsuarioSenhaInvalida() {
        UsuarioDados dados = new UsuarioDados();
        dados.setEmail_usuario("kaua@gmail.com");
        dados.setSenha_usuario("senha_errada");

        try {
            ResultSet rs = usuarioDAO.autenticacaoUsuario(dados);
            assertNotNull(rs, "ResultSet não deveria ser nulo mesmo para credenciais inválidas");
            assertFalse(rs.next(), "Não deveria autenticar com senha incorreta");
        } catch (SQLException e) {
            fail("Exceção lançada: " + e.getMessage());
        }
    }

    @Test
    public void testAutenticacaoUsuarioCaseSensitiveEmail() {
        UsuarioDados dados = new UsuarioDados();
        dados.setEmail_usuario("Menezes@gmail.com");
        dados.setSenha_usuario("12345");

        try {
            ResultSet rs = usuarioDAO.autenticacaoUsuario(dados);
            assertNotNull(rs, "ResultSet não deveria ser nulo");
            assertFalse(rs.next(), "Deveria falhar com email em case diferente (depende da configuração do DB)");
        } catch (SQLException e) {
            fail("Exceção lançada: " + e.getMessage());
        }
    }

    @Test
    public void testCadastrarUsuarioSucesso() throws SQLException {
        // 1. Cria os mocks
        usuarioDAO usuarioDAOMock = Mockito.mock(usuarioDAO.class);
        ResultSet resultSetMock = Mockito.mock(ResultSet.class);

        // 2. Configura os dados de teste
        UsuarioDados dados = new UsuarioDados();
        dados.setNome_usuario("Novo Teste");
        dados.setEmail_usuario("Oteste@email.com");
        dados.setSenha_usuario("senhaSegura123");
        dados.setTipo_usuario("ATENDENTE");

        // 3. Configura o comportamento dos mocks
        when(usuarioDAOMock.cadastrarUsuario(dados)).thenReturn(true);
        when(usuarioDAOMock.autenticacaoUsuario(dados)).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getString("nome_usuario")).thenReturn("Novo Teste");

        // 4. Executa o teste
        boolean resultado = usuarioDAOMock.cadastrarUsuario(dados);
        assertTrue(resultado, "Deveria cadastrar com sucesso");

        ResultSet rs = usuarioDAOMock.autenticacaoUsuario(dados);
        assertNotNull(rs);
        assertTrue(rs.next(), "Deveria encontrar o usuário cadastrado");
        assertEquals("Novo Teste", rs.getString("nome_usuario"));

        // 5. Verifica se os métodos foram chamados
        verify(usuarioDAOMock).cadastrarUsuario(dados);
        verify(usuarioDAOMock).autenticacaoUsuario(dados);
        verify(resultSetMock).next();
        verify(resultSetMock).getString("nome_usuario");
    }

    @Test
    public void testCadastrarUsuarioEmailDuplicado() throws SQLException {
        // 1. Cria os mocks
        usuarioDAO usuarioDAOMock = Mockito.mock(usuarioDAO.class);
        ResultSet resultSetMock = Mockito.mock(ResultSet.class);

        // 2. Configura os dados de teste (dois usuários com mesmo email)
        UsuarioDados primeiroUsuario = new UsuarioDados();
        primeiroUsuario.setNome_usuario("Primeiro Usuário");
        primeiroUsuario.setEmail_usuario("duplicado@teste.com");
        primeiroUsuario.setSenha_usuario("senha123");
        primeiroUsuario.setTipo_usuario("ATENDENTE");

        UsuarioDados segundoUsuario = new UsuarioDados();
        segundoUsuario.setNome_usuario("Segundo Usuário");
        segundoUsuario.setEmail_usuario("duplicado@teste.com"); // Mesmo email
        segundoUsuario.setSenha_usuario("outrasenha");
        segundoUsuario.setTipo_usuario("GERENTE");

        // 3. Configura o comportamento dos mocks
        when(usuarioDAOMock.cadastrarUsuario(primeiroUsuario)).thenReturn(true);
        when(usuarioDAOMock.cadastrarUsuario(segundoUsuario)).thenReturn(false);

        when(usuarioDAOMock.autenticacaoUsuario(any(UsuarioDados.class))).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getString("nome_usuario")).thenReturn("Primeiro Usuário");
        when(resultSetMock.getString("email_usuario")).thenReturn("duplicado@teste.com");

        // 4. Primeiro cadastro (deve funcionar)
        boolean primeiroResultado = usuarioDAOMock.cadastrarUsuario(primeiroUsuario);
        assertTrue(primeiroResultado, "Primeiro cadastro deveria ser bem-sucedido");

        // Verifica autenticação do primeiro usuário
        ResultSet rs = usuarioDAOMock.autenticacaoUsuario(primeiroUsuario);
        assertNotNull(rs);
        assertTrue(rs.next(), "Usuário deveria ser encontrado após cadastro");
        assertEquals("Primeiro Usuário", rs.getString("nome_usuario"));
        assertEquals("duplicado@teste.com", rs.getString("email_usuario"));

        // 5. Segundo cadastro (deve falhar por e-mail duplicado)
        boolean segundoResultado = usuarioDAOMock.cadastrarUsuario(segundoUsuario);
        assertFalse(segundoResultado, "Segundo cadastro deveria falhar por e-mail duplicado");

        // 6. Verificações finais
        verify(usuarioDAOMock, times(1)).cadastrarUsuario(primeiroUsuario);
        verify(usuarioDAOMock, times(1)).cadastrarUsuario(segundoUsuario);
        verify(usuarioDAOMock, atLeastOnce()).autenticacaoUsuario(any(UsuarioDados.class));
        verify(resultSetMock, atLeastOnce()).next();
        verify(resultSetMock, atLeastOnce()).getString("nome_usuario");
        verify(resultSetMock, atLeastOnce()).getString("email_usuario");
    }

}
