package org.example.manager;

import org.example.model.Book;
import org.example.model.enums.BookSituation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BorrowManager {
    private HashMap<Book, LocalDate> borrowedBooks = new HashMap<Book,LocalDate>();
    private final int limitBooks;
    private final int limitDays;
     public BorrowManager(int limitBooks, int limitDays) {
        this.limitBooks = limitBooks;
        this.limitDays = limitDays;
     }
     public HashMap<Book, LocalDate> getBorrowedBooks() {
        return borrowedBooks;
     }
     public void borrowBook(Book book) {
         if(book.getSituation()!= BookSituation.FREE) {
             System.out.println("book is not available");
             return ;
         }
         if(borrowedBooks.size() >= limitBooks) {
             System.out.println("you have reached the maximum amount of books");
             return ;
         }
         borrowedBooks.put(book, LocalDate.now());
         book.setSituation(BookSituation.BORROWED);
     }
     public void returnBook(Book book) {
         if(borrowedBooks.containsKey(book)) {
             if(borrowedBooks.get(book).plusDays(limitDays).isBefore(LocalDate.now())) {
                 System.out.println("book is returned late for "+
                         (ChronoUnit.DAYS.between(borrowedBooks.get(book), LocalDate.now()) - limitDays)+"days");
             }
             else{ System.out.println("book is returned successfully");}
             borrowedBooks.remove(book);
             book.setSituation(BookSituation.FREE);
         }
         else{
             System.out.println("book is not available");
         }
     }
     public List<Book> returnBooks() {
         return new ArrayList<Book>(borrowedBooks.keySet());
     }
}
