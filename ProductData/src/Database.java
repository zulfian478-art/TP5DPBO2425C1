import java.sql.*;
import javax.swing.JOptionPane;

public class Database {
    private Connection connection;
    private Statement statement;

    // Constructor
    public Database() {
        try {
            // Load driver (opsional tapi aman)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Koneksi ke database
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/db_product",
                    "root",
                    ""
            );
            statement = connection.createStatement();

        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Koneksi database gagal: " + e.getMessage());
        }
    }

    // SELECT query
    public ResultSet selectQuery(String sql) {
        try {
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error pada selectQuery: " + e.getMessage());
            return null;
        }
    }

    // INSERT, UPDATE, DELETE query
//    public int InsertUpdateDelete(String sql) {
//        try {
//            return statement.executeUpdate(sql);
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error pada insert/update/delete: " + e.getMessage());
//            return 0;
//        }
//    }

    public void InsertUpdateDelete(String query) {
        try {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }



    // Tutup koneksi (opsional tapi disarankan)
    public void closeConnection() {
        try {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            System.out.println("Gagal menutup koneksi: " + e.getMessage());
        }
    }

    // Getter (jika dibutuhkan)
    public Statement getStatement() {
        return statement;
    }
}
