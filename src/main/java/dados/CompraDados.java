package dados;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class CompraDados {

    private int id_compra, id_fornecedor;
    private String pagamento_compra;
    private double total_venda;

    public int getId_compra() {
        return id_compra;
    }

    public void setId_compra(int id_compra) {
        this.id_compra = id_compra;
    }

    public int getId_fornecedor() {
        return id_fornecedor;
    }

    public void setId_fornecedor(int id_fornecedor) {
        this.id_fornecedor = id_fornecedor;
    }

    public String getPagamento_compra() {
        return pagamento_compra;
    }

    public void setPagamento_compra(String pagamento_compra) {
        this.pagamento_compra = pagamento_compra;
    }

    public double getTotal_venda() {
        return total_venda;
    }

    public void setTotal_venda(String valorFormatado) {
        try {
            NumberFormat format = NumberFormat.getInstance(new Locale("pt", "BR"));
            Number number = format.parse(valorFormatado);
            this.total_venda = number.doubleValue();
        } catch (ParseException e) {
            System.err.println("Erro ao converter valor: " + valorFormatado);
            this.total_venda = 0.0;
        }
    }

    // Mantenha o setter original para double
    public void setTotal_venda(double total_venda) {
        this.total_venda = total_venda;
    }

}
