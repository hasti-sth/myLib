package org.example.manager;

import org.example.dao.BorrowDAO;
import org.example.model.Book;
import org.example.model.Member;
import org.example.model.enums.BookSituation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BorrowManager {
    private final BorrowDAO borrowDAO;
     public BorrowManager(BorrowDAO borrowDAO) {
         this.borrowDAO = borrowDAO;
     }

     public void borrowBook(Book book,Member member) {
      if(MemberManager.hasMembership(member)) {
         if(book.getSituation()!= BookSituation.FREE) {
             System.out.println("book is not available");
             return ;
         }
         if(borrowDAO.getMemberBooks(member).size() >= member.getBookLimit()) {
             System.out.println("you have reached the maximum amount of books");
             return ;
         }
         borrowDAO.borrowBook(book,member,LocalDate.now(),LocalDate.now().plusDays(member.getReturnLimit()));
         book.setSituation(BookSituation.BORROWED);}
      else System.out.println("your membership is finished");
     }

     public void returnBook(Book book,Member member) {
         if(borrowDAO.getMemberBooks(member).contains(book)) {
             LocalDate returnDate = borrowDAO.returnBook(book,member);
             if(returnDate.isBefore(LocalDate.now())) {
                 System.out.println("you have returned a book late for"+ChronoUnit.DAYS.between(LocalDate.now(),returnDate));
             }
             book.setSituation(BookSituation.FREE);
             BookManager.updateBook(book);
         }
         else{
             System.out.println("book is not available");
         }
     }

     public List<Book> returnMemberBooks(Member member) {
         return borrowDAO.getMemberBooks(member);
     }
}

