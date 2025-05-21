package principal;

import Conexao.FornecedorDAO;
import dados.FornecedorDados;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import static org.mockito.Mockito.*;

public class CompraTest {

    @Test
    public void testExecutarFornecedorComIdValido() {
        // Mocks
        JTextField txtIdFornecedor = mock(JTextField.class);
        JLabel lblNome = mock(JLabel.class);
        JLabel lblCnpj = mock(JLabel.class);
        JLabel lblRua = mock(JLabel.class);
        JLabel lblComp = mock(JLabel.class);
        JLabel lblNum = mock(JLabel.class);
        JLabel lblCid = mock(JLabel.class);
        JLabel lblEst = mock(JLabel.class);
        JLabel lblBairro = mock(JLabel.class);

        FornecedorDAO fornecedorDAOMock = mock(FornecedorDAO.class);
        FornecedorDados fornecedor = new FornecedorDados();
        fornecedor.setNome_fornecedor("Fornecedor X");
        fornecedor.setCnpj_fornecedor("00.000.000/0001-00");
        fornecedor.setRua_fornecedor("Rua Y");
        fornecedor.setComplemento_fornecedor("Sala 101");
        fornecedor.setNumero_fornecedor(123);
        fornecedor.setCidade_fornecedor("Cidade Z");
        fornecedor.setEstado_fornecedor("SP");
        fornecedor.setBairro_fornecedor("Centro");

        when(txtIdFornecedor.getText()).thenReturn("1");
        when(fornecedorDAOMock.getFornecedorDados(1)).thenReturn(fornecedor);

        // Chama método
        new Compra().ExecutarFornecedor(txtIdFornecedor, lblNome, lblCnpj, lblRua, lblComp, lblNum, lblCid, lblEst, lblBairro, fornecedorDAOMock);

        // Verifica se os labels foram atualizados
        verify(lblNome).setText("Fornecedor X");
        verify(lblCnpj).setText("00.000.000/0001-00");
        verify(lblRua).setText("Rua Y");
        verify(lblComp).setText("Sala 101");
        verify(lblNum).setText("123");
        verify(lblCid).setText("Cidade Z");
        verify(lblEst).setText("SP");
        verify(lblBairro).setText("Centro");
    }

    @Test
    public void testExecutarFornecedorComIdInexistente() {
        // Mocks
        JTextField txtIdFornecedor = mock(JTextField.class);
        JLabel lblNome = mock(JLabel.class);
        JLabel lblCnpj = mock(JLabel.class);
        JLabel lblRua = mock(JLabel.class);
        JLabel lblComp = mock(JLabel.class);
        JLabel lblNum = mock(JLabel.class);
        JLabel lblCid = mock(JLabel.class);
        JLabel lblEst = mock(JLabel.class);
        JLabel lblBairro = mock(JLabel.class);

        FornecedorDAO fornecedorDAOMock = mock(FornecedorDAO.class);
        when(txtIdFornecedor.getText()).thenReturn("999");
        when(fornecedorDAOMock.getFornecedorDados(999)).thenReturn(null);

        // Chama método
        new Compra().ExecutarFornecedor(txtIdFornecedor, lblNome, lblCnpj, lblRua, lblComp, lblNum, lblCid, lblEst, lblBairro, fornecedorDAOMock);

        // Verifica se labels foram resetados
        verify(lblNome).setText("");
        verify(lblCnpj).setText("");
        verify(lblRua).setText("");
        verify(lblComp).setText("");
        verify(lblNum).setText("");
        verify(lblCid).setText("");
        verify(lblEst).setText("");
        verify(lblBairro).setText("");
    }

    @Test
    public void testExecutarFornecedorComCampoIdVazio() {
        JTextField txtIdFornecedor = mock(JTextField.class);
        JLabel lblNome = mock(JLabel.class);
        JLabel lblCnpj = mock(JLabel.class);
        JLabel lblRua = mock(JLabel.class);
        JLabel lblComp = mock(JLabel.class);
        JLabel lblNum = mock(JLabel.class);
        JLabel lblCid = mock(JLabel.class);
        JLabel lblEst = mock(JLabel.class);
        JLabel lblBairro = mock(JLabel.class);

        FornecedorDAO fornecedorDAOMock = mock(FornecedorDAO.class);
        when(txtIdFornecedor.getText()).thenReturn("");

        // Chama método
        new Compra().ExecutarFornecedor(txtIdFornecedor, lblNome, lblCnpj, lblRua, lblComp, lblNum, lblCid, lblEst, lblBairro, fornecedorDAOMock);

        // Não deve interagir com os labels
        verifyNoInteractions(lblNome, lblCnpj, lblRua, lblComp, lblNum, lblCid, lblEst, lblBairro);
    }

}
