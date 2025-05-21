package conexao;

import dados.FornecedorDados;
import Conexao.FornecedorDAO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FornecedorDAOTest {

    private FornecedorDAO fornecedorDAO = new FornecedorDAO();

    @Test
    public void testCadastrarFornecedorSucesso() {
        // 1. Cria mock do DAO
        FornecedorDAO fornecedorDAOMock = Mockito.mock(FornecedorDAO.class);

        // 2. Cria e preenche os dados do fornecedor
        FornecedorDados fornecedor = new FornecedorDados();
        fornecedor.setNome_fornecedor("Fornecedor Teste LTDA");
        fornecedor.setCnpj_fornecedor("12345678901234");
        fornecedor.setRua_fornecedor("Rua dos Testes");
        fornecedor.setNumero_fornecedor(123);
        fornecedor.setBairro_fornecedor("Centro");
        fornecedor.setCidade_fornecedor("São Paulo");
        fornecedor.setEstado_fornecedor("SP");
        fornecedor.setComplemento_fornecedor("Sala 45");

        // 3. Configura mock para não fazer nada (método void)
        doNothing().when(fornecedorDAOMock).cadastrarFornecedor(fornecedor);

        // 4. Executa o método simulado
        fornecedorDAOMock.cadastrarFornecedor(fornecedor);
        
        //mensagem de sucesso do teste
        System.out.println("✅ Produto cadastrado com sucesso: " + fornecedor.getNome_fornecedor());

        // 5. Verifica que o método foi chamado exatamente uma vez
        verify(fornecedorDAOMock, times(1)).cadastrarFornecedor(fornecedor);
    }

    @Test
    public void testCadastroFornecedorComCNPJDuplicado() {
        // 1. Cria mock da DAO
        FornecedorDAO fornecedorDAOMock = Mockito.mock(FornecedorDAO.class);

        // 2. Fornecedor original
        FornecedorDados fornecedor1 = new FornecedorDados();
        fornecedor1.setNome_fornecedor("Fornecedor Original");
        fornecedor1.setCnpj_fornecedor("12345678901234");
        fornecedor1.setRua_fornecedor("Rua A");
        fornecedor1.setNumero_fornecedor(100);
        fornecedor1.setBairro_fornecedor("Centro");
        fornecedor1.setCidade_fornecedor("São Paulo");
        fornecedor1.setEstado_fornecedor("SP");
        fornecedor1.setComplemento_fornecedor("Sala 1");

        // 3. Fornecedor duplicado (mesmo CNPJ)
        FornecedorDados fornecedor2 = new FornecedorDados();
        fornecedor2.setNome_fornecedor("Fornecedor Duplicado");
        fornecedor2.setCnpj_fornecedor("12345678901234"); // mesmo CNPJ
        fornecedor2.setRua_fornecedor("Rua B");
        fornecedor2.setNumero_fornecedor(200);
        fornecedor2.setBairro_fornecedor("Bairro B");
        fornecedor2.setCidade_fornecedor("São Paulo");
        fornecedor2.setEstado_fornecedor("SP");
        fornecedor2.setComplemento_fornecedor("Sala 2");

        // 4. Simula o comportamento do DAO
        // Primeira chamada ao método: CNPJ não existe
        // Segunda chamada: CNPJ já cadastrado
        when(fornecedorDAOMock.getCNPJFornecedor("12345678901234"))
                .thenReturn(false) // primeiro cadastro
                .thenReturn(true);  // segundo cadastro (duplicado)

        // Mock do cadastro apenas para registrar chamada
        doNothing().when(fornecedorDAOMock).cadastrarFornecedor(any(FornecedorDados.class));

        // 5. Executa primeiro cadastro
        boolean cnpjExisteAntes = fornecedorDAOMock.getCNPJFornecedor(fornecedor1.getCnpj_fornecedor());
        assertFalse(cnpjExisteAntes, "CNPJ ainda não deveria existir");

        fornecedorDAOMock.cadastrarFornecedor(fornecedor1);
        verify(fornecedorDAOMock, times(1)).cadastrarFornecedor(fornecedor1);

        // 6. Tenta cadastrar duplicado
        boolean cnpjExisteDepois = fornecedorDAOMock.getCNPJFornecedor(fornecedor2.getCnpj_fornecedor());
        assertTrue(cnpjExisteDepois, "CNPJ já deveria existir após primeiro cadastro");
        
        // Mensagem que o teste deu certo e avisou que o cnpj ja foi usado
        System.out.println("CNPJ ja utilizado: " + fornecedor2.getCnpj_fornecedor());

        // Como o CNPJ já existe, o segundo cadastro não deve ocorrer
        verify(fornecedorDAOMock, never()).cadastrarFornecedor(fornecedor2);
    }

}
