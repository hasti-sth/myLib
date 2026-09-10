package org.example.dao;

import org.example.database.DatabaseConnection;
import org.example.model.Book;
import org.example.model.Member;
import org.example.model.enums.BookSituation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BorrowDAO {
    public void  borrowBook(Book book, Member member,LocalDate borrowDate,LocalDate returnDate){
        String sql= """
                INSERT INTO Borrow
                (member_id,book_id,borrowDate,returnDate)
                VALUES(?,?,?,?)
                """;
        try(
                Connection conn= DatabaseConnection.getConnection();
                PreparedStatement statement= conn.prepareStatement(sql);
                ){
            statement.setInt(1, member.getId());
            statement.setInt(2, book.getId());
            statement.setDate(3,java.sql.Date.valueOf(borrowDate));
            statement.setDate(4,java.sql.Date.valueOf(returnDate));

            statement.executeUpdate();
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }

    public LocalDate returnBook(Book book, Member member){
        String sql= """
                DELETE FROM Borrow
                WHERE book_id=? AND member_id=?
                RETURNING returnDate
        """;
        try(
                Connection conn= DatabaseConnection.getConnection();
                PreparedStatement statement= conn.prepareStatement(sql);
        ){
            statement.setInt(2, member.getId());
            statement.setInt(1, book.getId());

            ResultSet rs=statement.executeQuery();
            if(rs.next()){return rs.getDate("returnDate").toLocalDate();}
        }

        catch (Exception e){
            e.printStackTrace();
        }
        return LocalDate.now();
    }
    public void printMemberBooks(Member member){
        String sql = """
        SELECT *
        FROM Borrow br
        JOIN Book b ON br.book_id = b.id
        WHERE br.member_id = ?
        """;


        try(
                Connection conn= DatabaseConnection.getConnection();
                PreparedStatement statement= conn.prepareStatement(sql);
        ){
            statement.setInt(1, member.getId());
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
                System.out.println("تاریخ امانت: " + rs.getDate("borrowDate"));
                System.out.println("تاریخ بازگشت: " + rs.getDouble("returnDate"));
                System.out.println("─────────────────────────────────────────────────────");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public List<Book> getMemberBooks(Member member){
        List<Book>  books = new ArrayList<>();

        String sql = """
        SELECT *
        FROM Borrow br
        JOIN Book b ON br.book_id = b.id
        WHERE br.member_id = ?
        """;

        try(
                Connection conn=DatabaseConnection.getConnection();
                PreparedStatement statement=conn.prepareStatement(sql);
                ){
            statement.setInt(1,member.getId());

            ResultSet rs=statement.executeQuery();
            while (rs.next()) {
                BookSituation situation=BookSituation.valueOf(rs.getString("bookSituation"));
                String author = rs.getString("author");
                String publisher = rs.getString("publisher");
                String genre = rs.getString("genre");
                String description = rs.getString("description");
                String title=rs.getString("title");
                LocalDate publishedDate = rs.getDate("publishedDate").toLocalDate();
                double price = rs.getDouble("price");
                int id=rs.getInt("id");


                Book book=new Book(title,author,publisher,genre,description,publishedDate,price,situation);
                book.setId(id);
                books.add(book);
            }
    }
        catch (Exception e){
            e.printStackTrace();
        }
        return books;
}}
