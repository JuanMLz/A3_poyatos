package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class Conexao {
    // URL de conexão com o banco de dados MySQL
    private static final String url = "jdbc:mysql://showzao-1.cgl8msa80fmh.us-east-1.rds.amazonaws.com:3306/showzao_1";
    
    // Usuário e senha de acesso ao banco de dados
    private static final String USER = "admin";
    private static final String PASSWORD = "showzao123";

    /**
     * Estabelece e retorna uma conexão com o banco de dados MySQL.
     * 
     * @return A conexão com o banco de dados.
     * @throws SQLException Se ocorrer um erro ao estabelecer a conexão.
     */
    public static Connection getConexao() throws SQLException {
        // Tenta estabelecer a conexão com o banco de dados e retorna a conexão
        return DriverManager.getConnection(url, USER, PASSWORD);
    }

    /**
     * Fecha a conexão com o banco de dados, se a conexão não for null.
     * 
     * @param conn A conexão a ser fechada.
     */
    public static void fecharConn(Connection conn) {
        // Verifica se a conexão não é nula antes de tentar fechá-la
        if (conn != null) {
            try {
                // Fecha a conexão com o banco de dados
                conn.close();
            } catch (SQLException e) {
                // Exibe um erro caso ocorra algum problema ao fechar a conexão
                JOptionPane.showMessageDialog(null, "Erro ao fechar conexão: " + e.getMessage());
                // Opcional: Pode-se registrar o erro em um log para maior controle
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }
}