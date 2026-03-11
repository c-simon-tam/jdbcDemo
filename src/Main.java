import java.sql.*;

public class Main {
    private static void printResultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int cols = metaData.getColumnCount();

        for (int i=1; i <= cols; i++) {
            System.out.print(metaData.getColumnLabel(i));
            System.out.print("\t");
        }
        System.out.println();

        while (rs.next()) {
            for (int i=1; i <= cols; i++) {
                System.out.print(rs.getString(i));
                System.out.print("\t");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:sakila_master.db");
            Statement stmt = conn.createStatement();
            String sql = "select * from actor";
            ResultSet rs = stmt.executeQuery(sql);
            printResultSet(rs);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
