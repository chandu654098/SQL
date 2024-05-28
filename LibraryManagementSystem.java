import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LibraryManagementSystem {
  private Connection conn;

  public LibraryManagementSystem() {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/LibraryManagement", "root", "password");
    } catch (ClassNotFoundException | SQLException e) {
      System.out.println("Error connecting to database: " + e.getMessage());
    }
  }

  public void createDatabase() {
    try (Statement stmt = conn.createStatement()) {
      stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS LibraryManagement");
      stmt.executeUpdate("USE LibraryManagement");

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Librarian (" +
        "LibrarianID int PRIMARY KEY, " +
        "Name varchar(50), " +
        "Password varchar(50)" +
      ")");

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Books (" +
        "BookID int PRIMARY KEY, " +
        "Title varchar(100), " +
        "Author varchar(50), " +
        "Publisher varchar(50), " +
        "PublicationDate date" +
      ")");

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Students (" +
        "StudentID int PRIMARY KEY, " +
        "Name varchar(50), " +
        "Email varchar(50)" +
      ")");

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS BorrowedBooks (" +
        "BorrowedBookID int PRIMARY KEY, " +
        "BookID int, " +
        "StudentID int, " +
        "BorrowDate date, " +
        "ReturnDate date, " +
        "FOREIGN KEY (BookID) REFERENCES Books(BookID), " +
        "FOREIGN KEY (StudentID) REFERENCES Students(StudentID)" +
      ")");
    } catch (SQLException e) {
      System.out.println("Error creating database: " + e.getMessage());
    }
  }

  public void addLibrarian(int librarianID, String name, String password) {
    try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Librarian (LibrarianID, Name, Password) VALUES (?,?,?)")) {
      pstmt.setInt(1, librarianID);
      pstmt.setString(2, name);
      pstmt.setString(3, password);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Error adding librarian: " + e.getMessage());
    }
  }

  public void addBook(int bookID, String title, String author, String publisher, String publicationDate) {
    try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Books (BookID, Title, Author, Publisher, PublicationDate) VALUES (?,?,?,?,?)")) {
      pstmt.setInt(1, bookID);
      pstmt.setString(2, title);
      pstmt.setString(3, author);
      pstmt.setString(4, publisher);
      pstmt.setString(5, publicationDate);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Error adding book: " + e.getMessage());
    }
  }

  public void borrowBook(int bookID, int studentID, String borrowDate, String returnDate) {
    try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO BorrowedBooks (BookID, StudentID, BorrowDate, ReturnDate) VALUES (?,?,?,?)")) {
      pstmt.setInt(1, bookID);
      pstmt.setInt(2, studentID);
      pstmt.setString(3, borrowDate);
      pstmt.setString(4, returnDate);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Error borrowing book: " + e.getMessage());
    }
  }

  public void returnBook(int bookID, int studentID) {
    try (PreparedStatement pstmt = conn.prepareStatement("UPDATE BorrowedBooks SET ReturnDate =? WHERE BookID =? AND StudentID =?")) {
      pstmt.setString(1, "2024-05-28"); // current date
      pstmt.setInt(2, bookID);
      pstmt.setInt(3, studentID);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Error returning book: " + e.getMessage());
    }
  }

  public static void main(String[] args) {
    LibraryManagementSystem lms = new LibraryManagementSystem();
    lms.createDatabase();

    lms.addLibrarian(1, "John Doe", "password");
    lms.addBook(1, "Java Programming", "John Smith", "Wiley", "2020-01-01");
    lms.addStudent(1, "Jane Doe", "jane.doe@example.com");

    lms.borrowBook(1, 1, "2024-05-20", "2024-05-27");
    lms.returnBook