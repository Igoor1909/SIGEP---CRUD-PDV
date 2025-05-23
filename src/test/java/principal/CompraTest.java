package principal;

import Conexao.CompraDAO;
import Conexao.FornecedorDAO;
import Conexao.produtoDAO;
import dados.CompraDados;
import principal.Compra;
import dados.FornecedorDados;
import dados.ProdutoDados;
import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
        fornecedor.setCnpj_fornecedor("00000000000100");
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
        verify(lblCnpj).setText("00000000000100");
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

    @Test
    public void testExecutarProdutoComIdValido() {
        // Mocks dos campos
        JTextField txtIDProduto = mock(JTextField.class);
        JTextField txtNomeProduto = mock(JTextField.class);
        JTextField txtValorProduto = mock(JTextField.class);
        JTextField txtDescontoProduto = mock(JTextField.class);
        JTextField txtEanProduto = mock(JTextField.class); // não usado no método, mas incluído
        JTextField txtValorTotalProduto = mock(JTextField.class);
        JTextField txtQuantidadeProduto = mock(JTextField.class);

        // Mock do produto e DAO
        ProdutoDados produto = new ProdutoDados();
        produto.setNome_produto("Produto Teste");
        produto.setValor(10.0);

        produtoDAO produtoDAOMock = mock(produtoDAO.class);
        when(txtIDProduto.getText()).thenReturn("1");
        when(produtoDAOMock.getProdutoDados(1)).thenReturn(produto);

        // Cria a instância da Compra injetando o mock do DAO
        Compra compra = spy(new Compra(produtoDAOMock));
        doReturn("10,00").when(compra).formatarDecimal(10.0);
        doNothing().when(compra).addValidacaoCampos(any(), any(), any(), any());
        doNothing().when(compra).calcularValorTotal(any(), any(), any(), any());

        // Executa o método
        compra.ExecutarProduto(txtIDProduto, txtNomeProduto, txtValorProduto, txtDescontoProduto,
                txtEanProduto, txtValorTotalProduto, txtQuantidadeProduto);

        // Verificações
        verify(txtQuantidadeProduto).setText("1");
        verify(txtDescontoProduto).setText("0,00");
        verify(txtNomeProduto).setText("Produto Teste");
        verify(txtValorProduto).setText("10,00");
        verify(compra).calcularValorTotal(txtValorProduto, txtQuantidadeProduto, txtDescontoProduto, txtValorTotalProduto);
    }

    @Test
    public void testExecutarProdutoComIdInexistente() {
        JTextField txtIDProduto = mock(JTextField.class);
        JTextField txtNomeProduto = mock(JTextField.class);
        JTextField txtValorProduto = mock(JTextField.class);
        JTextField txtDescontoProduto = mock(JTextField.class);
        JTextField txtEanProduto = mock(JTextField.class);
        JTextField txtValorTotalProduto = mock(JTextField.class);
        JTextField txtQuantidadeProduto = mock(JTextField.class);

        produtoDAO produtoDAOMock = mock(produtoDAO.class);
        when(txtIDProduto.getText()).thenReturn("999");
        when(produtoDAOMock.getProdutoDados(999)).thenReturn(null);

        Compra compra = spy(new Compra());
        doNothing().when(compra).addValidacaoCampos(any(), any(), any(), any());

        // Executa
        compra.ExecutarProduto(txtIDProduto, txtNomeProduto, txtValorProduto, txtDescontoProduto,
                txtEanProduto, txtValorTotalProduto, txtQuantidadeProduto);

        // Verificações
        verify(txtNomeProduto).setText("");
        verify(txtValorProduto).setText("");
        verify(txtQuantidadeProduto).setText("");
        verify(txtDescontoProduto).setText("");
        verify(txtValorTotalProduto).setText("");
    }

    @Test
    public void testExecutarProdutoComCampoIdVazio() {
        JTextField txtIDProduto = mock(JTextField.class);
        JTextField txtNomeProduto = mock(JTextField.class);
        JTextField txtValorProduto = mock(JTextField.class);
        JTextField txtDescontoProduto = mock(JTextField.class);
        JTextField txtEanProduto = mock(JTextField.class);
        JTextField txtValorTotalProduto = mock(JTextField.class);
        JTextField txtQuantidadeProduto = mock(JTextField.class);

        when(txtIDProduto.getText()).thenReturn("");

        Compra compra = spy(new Compra());
        doNothing().when(compra).addValidacaoCampos(any(), any(), any(), any());

        // Executa
        compra.ExecutarProduto(txtIDProduto, txtNomeProduto, txtValorProduto, txtDescontoProduto,
                txtEanProduto, txtValorTotalProduto, txtQuantidadeProduto);

        // Não deve interagir com os campos além dos dois primeiros setText
        verify(txtQuantidadeProduto).setText("1");
        verify(txtDescontoProduto).setText("0,00");
        verifyNoMoreInteractions(txtNomeProduto, txtValorProduto, txtValorTotalProduto);
    }

    @Test
    public void testAdicionarCarrinhoCamposValido() throws Exception {
        Compra compra = spy(new Compra());

        // Cria mocks
        JTextField txtIDProduto = mock(JTextField.class);
        JTextField txtNomeProduto = mock(JTextField.class);
        JTextField txtQuantidadeProduto = mock(JTextField.class);
        JTextField txtValorProduto = mock(JTextField.class);
        JTextField txtDescontoProduto = mock(JTextField.class);

        JTable tblProdutosCompra = mock(JTable.class);
        DefaultTableModel modelMock = mock(DefaultTableModel.class);

        // Injeta os campos privados
        injectPrivateField(compra, "txtIDProduto", txtIDProduto);
        injectPrivateField(compra, "txtNomeProduto", txtNomeProduto);
        injectPrivateField(compra, "txtQuantidadeProduto", txtQuantidadeProduto);
        injectPrivateField(compra, "txtValorProduto", txtValorProduto);
        injectPrivateField(compra, "txtDescontoProduto", txtDescontoProduto);
        injectPrivateField(compra, "tblProdutosCompra", tblProdutosCompra);

        when(tblProdutosCompra.getModel()).thenReturn(modelMock);
        when(modelMock.getRowCount()).thenReturn(0);

        when(txtIDProduto.getText()).thenReturn("1");
        when(txtNomeProduto.getText()).thenReturn("Produto Teste");
        when(txtQuantidadeProduto.getText()).thenReturn("2");
        when(txtValorProduto.getText()).thenReturn("10,00");
        when(txtDescontoProduto.getText()).thenReturn("1,00");

        doNothing().when(compra).atualizarTotalGeral();
        doNothing().when(compra).limparCampos();

        compra.adiconarCarrinho();

        verify(modelMock).insertRow(eq(0), any(Object[].class));
        verify(compra).atualizarTotalGeral();
        verify(compra).limparCampos();
    }

    @Test
    public void testAdicionarCarrinhoQuantidadeInvalida() throws Exception {
        Compra compra = spy(new Compra());

        // Mocks dos campos privados
        JTextField txtIDProduto = mock(JTextField.class);
        JTextField txtNomeProduto = mock(JTextField.class);
        JTextField txtQuantidadeProduto = mock(JTextField.class);
        JTextField txtValorProduto = mock(JTextField.class);
        JTextField txtDescontoProduto = mock(JTextField.class);
        JTable tblProdutosCompra = mock(JTable.class);

        // Injeta mocks nos campos privados
        injectPrivateField(compra, "txtIDProduto", txtIDProduto);
        injectPrivateField(compra, "txtNomeProduto", txtNomeProduto);
        injectPrivateField(compra, "txtQuantidadeProduto", txtQuantidadeProduto);
        injectPrivateField(compra, "txtValorProduto", txtValorProduto);
        injectPrivateField(compra, "txtDescontoProduto", txtDescontoProduto);
        injectPrivateField(compra, "tblProdutosCompra", tblProdutosCompra);

        when(txtIDProduto.getText()).thenReturn("1");
        when(txtNomeProduto.getText()).thenReturn("Produto Teste");
        when(txtQuantidadeProduto.getText()).thenReturn("abc");  // valor inválido
        when(txtValorProduto.getText()).thenReturn("10,00");
        when(txtDescontoProduto.getText()).thenReturn("1,00");

        compra.adiconarCarrinho();

        // Como a mensagem de erro é exibida via JOptionPane, não testamos ela aqui,
        // apenas verificamos que o método não tentou inserir linha na tabela
        verify(tblProdutosCompra, never()).getModel();
    }

    @Test
    public void testAdicionarCarrinhoCampoVazio() throws Exception {
        Compra compra = spy(new Compra());

        // Mocks dos campos privados
        JTextField txtIDProduto = mock(JTextField.class);
        JTextField txtNomeProduto = mock(JTextField.class);
        JTextField txtQuantidadeProduto = mock(JTextField.class);
        JTextField txtValorProduto = mock(JTextField.class);
        JTextField txtDescontoProduto = mock(JTextField.class);
        JTable tblProdutosCompra = mock(JTable.class);

        // Injeta mocks nos campos privados
        injectPrivateField(compra, "txtIDProduto", txtIDProduto);
        injectPrivateField(compra, "txtNomeProduto", txtNomeProduto);
        injectPrivateField(compra, "txtQuantidadeProduto", txtQuantidadeProduto);
        injectPrivateField(compra, "txtValorProduto", txtValorProduto);
        injectPrivateField(compra, "txtDescontoProduto", txtDescontoProduto);
        injectPrivateField(compra, "tblProdutosCompra", tblProdutosCompra);

        when(txtIDProduto.getText()).thenReturn("1");
        when(txtNomeProduto.getText()).thenReturn("");  // campo vazio
        when(txtQuantidadeProduto.getText()).thenReturn("2");
        when(txtValorProduto.getText()).thenReturn("10,00");
        when(txtDescontoProduto.getText()).thenReturn("1,00");

        compra.adiconarCarrinho();

        // Não deve chamar o modelo para inserir, pois os campos não estão completos
        verify(tblProdutosCompra, never()).getModel();
    }

    @Test
    public void testFinalizarCompraComTodosCamposPreenchidos() throws Exception {
        Compra compra = spy(new Compra());

        // Injeta mocks nos campos privados
        JTextField txtIDFornecedor = mock(JTextField.class);
        JComboBox<String> jComboBox1 = mock(JComboBox.class);
        JLabel lblTotalVenda = mock(JLabel.class);
        JTable tblProdutosCompra = mock(JTable.class);
        DefaultTableModel model = mock(DefaultTableModel.class);

        when(txtIDFornecedor.getText()).thenReturn("1");
        when(jComboBox1.getSelectedItem()).thenReturn("Cartão");
        when(lblTotalVenda.getText()).thenReturn("150,00");

        // Simula tabela com produtos adicionados (pelo menos 1 linha)
        when(tblProdutosCompra.getModel()).thenReturn(model);
        when(model.getRowCount()).thenReturn(1);

        // Injeção dos campos
        injectPrivateField(compra, "txtIDFornecedor", txtIDFornecedor);
        injectPrivateField(compra, "jComboBox1", jComboBox1);
        injectPrivateField(compra, "lblTotalVenda", lblTotalVenda);
        injectPrivateField(compra, "tblProdutosCompra", tblProdutosCompra);

        // Mocka DAO e métodos internos
        CompraDAO compraDAOMock = mock(CompraDAO.class);
        doReturn(123).when(compraDAOMock).cadastrarCompra(any(CompraDados.class));
        doReturn(compraDAOMock).when(compra).getCompraDAO(); // supondo que você tenha método para obter DAO

        doNothing().when(compra).salvarItensCompra(123);
        doNothing().when(compra).limparCamposCompra();

        // Executa
        compra.finalizarCompra();

        // Verificações
        verify(compraDAOMock).cadastrarCompra(any(CompraDados.class));
        verify(compra).salvarItensCompra(123);
        verify(compra).limparCamposCompra();
    }

    @Test
    public void testFinalizarCompraSemFornecedor() throws Exception {
        Compra compra = new Compra();
        CompraDAO compraDAOMock = mock(CompraDAO.class);
        compra.setCompraDAO(compraDAOMock);

        // Injeta os campos privados JTextField, JComboBox e JLabel
        injectPrivateField(compra, "txtIDFornecedor", mockTextFieldWithText(""));
        injectPrivateField(compra, "jComboBox1", mockComboBoxWithSelectedItem("Dinheiro"));
        injectPrivateField(compra, "lblTotalVenda", mockLabelWithText("100,00"));

        compra.finalizarCompra();

        // Verifica que não chamou o DAO porque faltou o fornecedor
        verify(compraDAOMock, never()).cadastrarCompra(any());
    }

    @Test
    public void testFinalizarCompraSemProdutosAdicionados() throws Exception {
        Compra compra = new Compra();
        CompraDAO compraDAOMock = mock(CompraDAO.class);
        compra.setCompraDAO(compraDAOMock);

        // Injetar campos obrigatórios que passam validação inicial
        injectPrivateField(compra, "txtIDFornecedor", mockTextFieldWithText("1"));
        injectPrivateField(compra, "jComboBox1", mockComboBoxWithSelectedItem("Cartão"));

        // Cenários de total da compra que indicam "sem produtos":
        String[] totaisInvalidos = {"", "0", "0,00"};

        for (String total : totaisInvalidos) {
            injectPrivateField(compra, "lblTotalVenda", mockLabelWithText(total));

            compra.finalizarCompra();

            // Verifica que não chamou o DAO para cadastrar compra
            verify(compraDAOMock, never()).cadastrarCompra(any());

            // Limpar interações para a próxima iteração
            clearInvocations(compraDAOMock);
        }
    }

    // Auxiliares
    private void injectPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private JTextField mockTextFieldWithText(String text) {
        JTextField tf = mock(JTextField.class);
        when(tf.getText()).thenReturn(text);
        return tf;
    }

    private JComboBox<String> mockComboBoxWithSelectedItem(String item) {
        JComboBox<String> combo = mock(JComboBox.class);
        when(combo.getSelectedItem()).thenReturn(item);
        return combo;
    }

    private JLabel mockLabelWithText(String text) {
        JLabel label = mock(JLabel.class);
        when(label.getText()).thenReturn(text);
        return label;
    }

}
