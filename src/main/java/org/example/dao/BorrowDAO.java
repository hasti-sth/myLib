package org.example.dao;

import org.example.database.DatabaseConnection;
import org.example.model.Book;
import org.example.model.Member;
import org.example.model.enums.BookSituation;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Repository
public class BorrowDAO {

    public void borrowBook(
            Book book,
            Member member,
            LocalDate borrowDate,
            LocalDate returnDate) {

        String sql = """
                INSERT INTO Borrow
                (member_id, book_id, borrowDate, returnDate)
                VALUES (?, ?, ?, ?)
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement =
                        conn.prepareStatement(sql)
        ) {

            statement.setInt(1, member.getId());
            statement.setInt(2, book.getId());
            statement.setDate(
                    3,
                    java.sql.Date.valueOf(borrowDate)
            );
            statement.setDate(
                    4,
                    java.sql.Date.valueOf(returnDate)
            );

            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public LocalDate returnBook(
            Book book,
            Member member) {

        String sql = """
                DELETE FROM Borrow
                WHERE book_id = ?
                AND member_id = ?
                RETURNING returnDate
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement =
                        conn.prepareStatement(sql)
        ) {

            statement.setInt(1, book.getId());
            statement.setInt(2, member.getId());

            ResultSet rs =
                    statement.executeQuery();

            if (rs.next()) {
                return rs
                        .getDate("returnDate")
                        .toLocalDate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public List<Book> getMemberBooks(Member member) {

        List<Book> books = new ArrayList<>();

        String sql = """
                SELECT b.*
                FROM Borrow br
                JOIN Book b
                    ON br.book_id = b.id
                WHERE br.member_id = ?
                """;

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        conn.prepareStatement(sql)
        ) {

            statement.setInt(1, member.getId());

            ResultSet rs =
                    statement.executeQuery();

            while (rs.next()) {

                BookSituation situation =
                        BookSituation.valueOf(
                                rs.getString("bookSituation")
                        );

                Book book =
                        new Book(
                                rs.getString("title"),
                                rs.getString("author"),
                                rs.getString("publisher"),
                                rs.getString("genre"),
                                rs.getString("description"),
                                rs.getDate("publishedDate")
                                        .toLocalDate(),
                                rs.getDouble("price"),
                                situation
                        );

                book.setId(
                        rs.getInt("id")
                );

                books.add(book);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return books;
    }
}