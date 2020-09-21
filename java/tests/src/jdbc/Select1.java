package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Select1 {
    public static void main(String[] args) {
        new Select1().listaEmpregados();
    }

    private String urlBancoDados = "jdbc:odbc:RecursosHumanos";
    private String usuario = "admin";
    private String senha = "";
    private String comandoSQL = "SELECT GEMACU_ID,GEMACU_DSC FROM GEMACU ORDER BY GEMACU_ID";

    public void listaEmpregados() {
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        } catch (ClassNotFoundException excecao) {
            excecao.printStackTrace();
            System.exit(16);
        }
        try {
            Connection conexao = DriverManager.getConnection(urlBancoDados,
                    usuario, senha);
            Statement comando = conexao.createStatement();
            ResultSet res = comando.executeQuery(comandoSQL);
            while (res.next()) {
                System.out.print("\n" + res.getString("GEMACU_ID"));
                System.out.print("\n" + res.getString("GEMACU_DSC"));
                System.out.flush();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}