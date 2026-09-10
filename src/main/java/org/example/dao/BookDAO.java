package org.example.dao;

import org.example.database.DatabaseConnection;
import org.example.model.Book;
import org.example.model.enums.BookSituation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    public void addBook(Book book){
        String sql= """
                INSERT INTO Book
                 (title, author, publisher, genre, description,
                 publishedDate, price, bookSituation)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                RETURNING id;
                """;
        try(
                Connection conn= DatabaseConnection.getConnection();
                PreparedStatement statement= conn.prepareStatement(sql)
                ){
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getPublisher());
            statement.setString(4, book.getGenre());
            statement.setString(5, book.getDescription());
            statement.setDate(6,java.sql.Date.valueOf(book.getPublishedDate()));
            statement.setDouble(7, book.getPrice());
            statement.setString(8,book.getSituation().name());

            ResultSet rs=statement.executeQuery();
            if(rs.next())book.setId(rs.getInt("id"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteBook(int id){
        String sql= """
                DELETE FROM Book
                WHERE id=?
        """;

        try(
                Connection conn= DatabaseConnection.getConnection();
                PreparedStatement statement= conn.prepareStatement(sql)
        ){
            statement.setInt(1,id);
            statement.executeUpdate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateBook(Book book){
        String sql= """
                UPDATE Book
                SET bookSituation=? WHERE id=?
        """;

        try(
                Connection conn= DatabaseConnection.getConnection();
                PreparedStatement statement= conn.prepareStatement(sql)
        ){
            statement.setString(1, book.getSituation().name());
            statement.setInt(2, book.getId());

            statement.executeUpdate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public Book getBookByID(int id) {
        String sql = """
                SELECT * FROM Book
                WHERE id=?
        """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql);
        ) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                BookSituation situation = BookSituation.valueOf(rs.getString("bookSituation"));
                String author = rs.getString("author");
                String publisher = rs.getString("publisher");
                String genre = rs.getString("genre");
                String description = rs.getString("description");
                LocalDate publishedDate = rs.getDate("publishedDate").toLocalDate();
                double price = rs.getDouble("price");
                String title = rs.getString("title");

                Book book = new Book(title, author, publisher, genre, description, publishedDate, price, situation);
                book.setId(id);

                return book;

            }
        }
       catch (Exception e){
            e.printStackTrace();
       }
        return null;
    }

    public List<Book> getBookByTitle(String title) {
        String sql = """
                SELECT * FROM Book
                WHERE title=?
        """;

        List<Book> bookList = new ArrayList<>();
        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)
        ) {
            statement.setString(1, title);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                BookSituation situation=BookSituation.valueOf(rs.getString("bookSituation"));
              String author = rs.getString("author");
              String publisher = rs.getString("publisher");
                String genre = rs.getString("genre");
              String description = rs.getString("description");
              LocalDate publishedDate = rs.getDate("publishedDate").toLocalDate();
              double price = rs.getDouble("price");
              int id=rs.getInt("id");


              Book book=new Book(title,author,publisher,genre,description,publishedDate,price,situation);
              book.setId(id);
              bookList.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookList;
    }

    public void printAllBooks()  {
        String sql = "SELECT * FROM Book";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)
        ) {
            ResultSet rs= statement.executeQuery();
            System.out.println("\n📚 لیست کتاب‌ها:");
            System.out.println("─────────────────────────────────────────────────────");

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("عنوان: " + rs.getString("title"));
                System.out.println("نویسنده: " + rs.getString("author"));
                System.out.println("ناشر: " + rs.getString("publisher"));
                System.out.println("ژانر: " + rs.getString("genre"));
                System.out.println("توضیحات: " + rs.getString("description"));
                System.out.println("تاریخ انتشار: " + rs.getDate("publishedDate"));
                System.out.println("قیمت: " + rs.getDouble("price"));
                System.out.println("وضعیت: " + rs.getString("bookSituation"));
                System.out.println("─────────────────────────────────────────────────────");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


}

