
package dados;


public class ProdutoDados {
    
    private int id_produto;
    private String nome_produto, ean_produto;
    private double valor;

    public int getId_produto() {
        return id_produto;
    }

    public void setId_produto(int id_produto) {
        this.id_produto = id_produto;
    }

    public String getNome_produto() {
        return nome_produto;
    }

    public void setNome_produto(String nome_produto) {
        this.nome_produto = nome_produto;
    }
    

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getEan_produto() {
        return ean_produto;
    }

    public void setEan_produto(String ean_produto) {
        this.ean_produto = ean_produto;
    }
    
    
    
}
