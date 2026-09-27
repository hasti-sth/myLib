package org.example.dao;

import org.example.database.DatabaseConnection;
import org.example.model.Book;
import org.example.model.enums.BookSituation;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookDAO {


    public void addBook(Book book) {

        String sql = """
                INSERT INTO Book
                (title, author, publisher, genre, description,
                 publishedDate, price, bookSituation)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                RETURNING id
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)
        ) {

            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getPublisher());
            statement.setString(4, book.getGenre());
            statement.setString(5, book.getDescription());
            statement.setDate(
                    6,
                    java.sql.Date.valueOf(book.getPublishedDate())
            );
            statement.setDouble(7, book.getPrice());
            statement.setString(8, book.getSituation().name());

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                book.setId(rs.getInt("id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<Book> getAllBooks() {

        String sql = "SELECT * FROM Book";

        List<Book> books = new ArrayList<>();

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql);
                ResultSet rs = statement.executeQuery()
        ) {

            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return books;
    }


    public Book getBookByID(int id) {

        String sql = """
                SELECT *
                FROM Book
                WHERE id = ?
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return mapResultSetToBook(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public List<Book> getBookByTitle(String title) {

        String sql = """
                SELECT *
                FROM Book
                WHERE title = ?
                """;

        List<Book> books = new ArrayList<>();

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)
        ) {

            statement.setString(1, title);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return books;
    }



    public boolean updateBook(Book book) {

        String sql = """
                UPDATE Book
                SET title = ?,
                    author = ?,
                    publisher = ?,
                    genre = ?,
                    description = ?,
                    publishedDate = ?,
                    price = ?,
                    bookSituation = ?
                WHERE id = ?
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)
        ) {

            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getPublisher());
            statement.setString(4, book.getGenre());
            statement.setString(5, book.getDescription());

            statement.setDate(
                    6,
                    java.sql.Date.valueOf(book.getPublishedDate())
            );

            statement.setDouble(7, book.getPrice());
            statement.setString(8, book.getSituation().name());
            statement.setInt(9, book.getId());

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean deleteBook(int id) {

        String sql = """
                DELETE FROM Book
                WHERE id = ?
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    private Book mapResultSetToBook(ResultSet rs) throws Exception {

        int id = rs.getInt("id");

        String title = rs.getString("title");
        String author = rs.getString("author");
        String publisher = rs.getString("publisher");
        String genre = rs.getString("genre");
        String description = rs.getString("description");

        LocalDate publishedDate =
                rs.getDate("publishedDate").toLocalDate();

        double price = rs.getDouble("price");

        BookSituation situation =
                BookSituation.valueOf(
                        rs.getString("bookSituation")
                );

        Book book = new Book(
                title,
                author,
                publisher,
                genre,
                description,
                publishedDate,
                price,
                situation
        );

        book.setId(id);

        return book;
    }
}