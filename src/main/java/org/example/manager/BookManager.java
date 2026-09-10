package org.example.manager;

import org.example.dao.BookDAO;
import org.example.model.Book;

import java.util.List;

public class BookManager {
    private static BookDAO bookDAO ;
    public BookManager(BookDAO bookDAO) {
        BookManager.bookDAO = bookDAO;
    }
    public void addBook(Book book){
        bookDAO.addBook(book);
    }
    public void printAllBooks(){
        bookDAO.printAllBooks();
    }
    public List<Book> getBookByTitle(String title){
       return bookDAO.getBookByTitle(title);
    }
    public Book getBookByID(int id){
      return bookDAO.getBookByID(id);
    }
    public void DeleteBookByID(int id){
        bookDAO.deleteBook(id);
    }
    public static void updateBook(Book book){
        bookDAO.updateBook(book);
    }
}
