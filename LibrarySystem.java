import java.sql.*;
import java.util.*;
import java.io.*;

public class LibrarySystem {

    // --- DATABASE CONFIGURATION ---
    private static final String URL = "jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";         // ← your MySQL username
    private static final String PASS = "sanjayaids#28"; // ← your MySQL password

    private static Connection conn;
    private static Scanner sc = new Scanner(System.in);

    // --- MAIN METHOD ---
    public static void main(String[] args) {
        try {
            connectDB();
            menu();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    // --- CONNECT TO DATABASE ---
    private static void connectDB() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(URL, USER, PASS);
        System.out.println("✅ Connected to MySQL database!");
    }

    // --- CLOSE CONNECTION ---
    private static void closeConnection() {
        try {
            if (conn != null) conn.close();
            System.out.println("🔒 Connection closed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- MENU ---
    private static void menu() throws Exception {
        while (true) {
            System.out.println("\n=== LIBRARY MANAGEMENT SYSTEM ===");
            System.out.println("1. Add Book");
            System.out.println("2. List Books");
            System.out.println("3. Add Member");
            System.out.println("4. List Members");
            System.out.println("5. Export Books to CSV");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1": addBook(); break;
                case "2": listBooks(); break;
                case "3": addMember(); break;
                case "4": listMembers(); break;
                case "5": exportBooksToCSV(); break;
                case "0": System.out.println("👋 Goodbye!"); return;
                default: System.out.println("❌ Invalid choice. Try again.");
            }
        }
    }

    // --- ADD BOOK ---
    private static void addBook() {
        try {
            System.out.print("Title: "); String title = sc.nextLine();
            System.out.print("Author: "); String author = sc.nextLine();
            System.out.print("Publisher: "); String publisher = sc.nextLine();
            System.out.print("Year: "); int year = Integer.parseInt(sc.nextLine());
            System.out.print("ISBN: "); String isbn = sc.nextLine();
            System.out.print("Quantity: "); int quantity = Integer.parseInt(sc.nextLine());

            String sql = "INSERT INTO book (title, author, publisher, year, isbn, quantity, created_at) VALUES (?,?,?,?,?,?,NOW())";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setString(3, publisher);
            ps.setInt(4, year);
            ps.setString(5, isbn);
            ps.setInt(6, quantity);
            ps.executeUpdate();

            System.out.println("✅ Book added successfully!");
            log("Added Book: " + title);
        } catch (Exception e) {
            System.out.println("❌ Error adding book: " + e.getMessage());
        }
    }

    // --- LIST BOOKS ---
    private static void listBooks() {
        String sql = "SELECT * FROM book";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            System.out.println("\n--- BOOKS IN LIBRARY ---");
            System.out.printf("%-5s %-25s %-20s %-15s %-6s %-15s %-8s\n", 
                "ID", "Title", "Author", "Publisher", "Year", "ISBN", "Qty");
            System.out.println("--------------------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-5d %-25s %-20s %-15s %-6d %-15s %-8d\n",
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("publisher"),
                        rs.getInt("year"),
                        rs.getString("isbn"),
                        rs.getInt("quantity"));
            }
        } catch (Exception e) {
            System.out.println("❌ Error listing books: " + e.getMessage());
        }
    }

    // --- ADD MEMBER ---
    private static void addMember() {
        try {
            System.out.print("Name: "); String name = sc.nextLine();
            System.out.print("Email: "); String email = sc.nextLine();
            System.out.print("Phone: "); String phone = sc.nextLine();

            String sql = "INSERT INTO member (name, email, phone, join_date) VALUES (?,?,?,CURDATE())";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.executeUpdate();

            System.out.println("✅ Member added successfully!");
            log("Added Member: " + name);
        } catch (Exception e) {
            System.out.println("❌ Error adding member: " + e.getMessage());
        }
    }

    // --- LIST MEMBERS ---
    private static void listMembers() {
        String sql = "SELECT * FROM member";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            System.out.println("\n--- LIBRARY MEMBERS ---");
            System.out.printf("%-5s %-20s %-25s %-15s %-12s\n", 
                "ID", "Name", "Email", "Phone", "Join Date");
            System.out.println("--------------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-5d %-20s %-25s %-15s %-12s\n",
                        rs.getInt("member_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getDate("join_date"));
            }
        } catch (Exception e) {
            System.out.println("❌ Error listing members: " + e.getMessage());
        }
    }

    // --- EXPORT BOOKS TO CSV ---
    private static void exportBooksToCSV() {
        System.out.print("Enter filename to export (e.g., books.csv): ");
        String filename = sc.nextLine();

        String sql = "SELECT * FROM book";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql);
             BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {

            bw.write("book_id,title,author,publisher,year,isbn,quantity");
            bw.newLine();

            while (rs.next()) {
                bw.write(rs.getInt("book_id") + "," +
                        rs.getString("title") + "," +
                        rs.getString("author") + "," +
                        rs.getString("publisher") + "," +
                        rs.getInt("year") + "," +
                        rs.getString("isbn") + "," +
                        rs.getInt("quantity"));
                bw.newLine();
            }

            System.out.println("✅ Books exported successfully to " + filename);
            log("Exported book data to " + filename);
        } catch (Exception e) {
            System.out.println("❌ Error exporting to CSV: " + e.getMessage());
        }
    }

    // --- LOGGING TO FILE ---
    private static void log(String message) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("library_log.txt", true))) {
            bw.write(java.time.LocalDateTime.now() + " - " + message);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("⚠️ Logging failed: " + e.getMessage());
        }
    }
}
