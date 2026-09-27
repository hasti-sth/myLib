package org.example.service;

import org.example.dao.BookDAO;
import org.example.model.Book;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookDAO bookDAO ;

    public BookService(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    public Book addBook(Book book){
        bookDAO.addBook(book);
        return book;
    }
    public List<Book> getAllBooks(){return bookDAO.getAllBooks();}
    public List<Book> getBookByTitle(String title){
       return bookDAO.getBookByTitle(title);
    }
    public Book getBookByID(int id){
      return bookDAO.getBookByID(id);
    }
    public boolean DeleteBookByID(int id){
        return bookDAO.deleteBook(id);
    }
    public boolean updateBook(Book book){
       return bookDAO.updateBook(book);
    }
}
