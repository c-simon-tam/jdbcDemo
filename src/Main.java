import java.sql.*;
import java.util.Scanner;

public class Main {
    private static void runSQLandPrint(String query, Connection conn) throws SQLException {
        System.out.println("Running SQL: " + query + "\n");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        ResultSetMetaData metaData = rs.getMetaData();
        int cols = metaData.getColumnCount();

        int[] colWidths = new int[cols];
        //initialize column widths with header lengths
        for (int i=0; i < colWidths.length; i++) {
            colWidths[i] = metaData.getColumnLabel(i+1).length();
        }
        //loop through results to get maximum lengths in each column
        while (rs.next()) {
            for (int i=0; i < colWidths.length; i++) {
                try {
                    int len = rs.getString(i+1).length();
                    if (len > colWidths[i])
                        colWidths[i] = len;
                } catch (NullPointerException e) {
                    //getString() can return NULL, so ignore them
                }
            }
        }
        //re-run query to return to the first row
        rs = stmt.executeQuery(query);
        metaData = rs.getMetaData();

        //print out column headers
        for (int i=1; i <= cols; i++) {
            System.out.printf("%-" + (colWidths[i-1]+1) + "s", metaData.getColumnLabel(i));
        }
        System.out.println();
        //print each row
        while (rs.next()) {
            for (int i=1; i <= cols; i++) {
                System.out.printf("%-" + (colWidths[i-1]+1) + "s", rs.getString(i));
            }
            System.out.println();
        }
    }

    public static void printMenu() {
        System.out.print("\nThis program demonstrates how to connect to a SQLite database ");
        System.out.print("(the sakila database in this case), ");
        System.out.println("run a query, and display the results.");
        System.out.println("\nWhat would you like to do?");
        System.out.println("1. Run the sample query.");
        System.out.println("2. Enter a query and run it.");
        System.out.println("3. Quit");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:sakila_master.db");
            while (true) {
                String sql = "SELECT f.title AS film_title, a.first_name, a.last_name "
                        + " FROM actor AS a "
                        + " INNER JOIN film_actor AS af ON af.actor_id=a.actor_id"
                        + " INNER JOIN film AS f ON f.film_id = af.film_id"
                        + " ORDER BY f.title, a.first_name";
                printMenu();
                String s = sc.nextLine();

                if (s.startsWith("1")) {
                    runSQLandPrint(sql, conn);

                } else if (s.startsWith("2")) {
                    System.out.print("Enter SQL (in one line): ");
                    sql = sc.nextLine();
                    runSQLandPrint(sql, conn);

                } else if (s.startsWith("3")) {
                    break;

                } else {
                    System.out.println("Choice not recognized.");
                }
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
