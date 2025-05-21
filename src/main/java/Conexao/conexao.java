package Conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class conexao {

    private String statusConexao = "Não conectado!";

    public Connection getConexao() {
        Connection conexao = null;

        try {

            String Driver = "com.mysql.cj.jdbc.Driver";

            Class.forName(Driver);

            String servidor = "localhost";
            String schema = "DB";

            String url = "jdbc:mysql://" + servidor + "/" + schema;

            String usuario = "root";
            String senha = "12345";

            conexao = (Connection) DriverManager.getConnection(url, usuario, senha);

            if (conexao != null) {
                statusConexao = "conectado!";
            } else {
                statusConexao = "Não conectado!";
            }

            return conexao;
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Driver de conexão não encontrado");
            return null;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha na conexão!");
            JOptionPane.showMessageDialog(null, e.getMessage());
            return null;
        }
    }

    public String getStatusConexao() {
        return statusConexao;
    }

    public boolean fechaConexao() {
        try {
            getConexao().close();
            statusConexao = "Conexao Fechada!";
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Connection reiniciaConexao() {
        fechaConexao();
        return getConexao();
    }

}
