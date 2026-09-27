package org.example.service;

import org.example.dao.BookDAO;
import org.example.dao.BorrowDAO;
import org.example.dao.MemberDAO;
import org.example.model.Book;
import org.example.model.Member;
import org.example.model.enums.BookSituation;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BorrowService {

    private final BorrowDAO borrowDAO;
    private final BookDAO bookDAO;
    private final MemberDAO memberDAO;

    public BorrowService(
            BorrowDAO borrowDAO,
            BookDAO bookDAO,
            MemberDAO memberDAO) {

        this.borrowDAO = borrowDAO;
        this.bookDAO = bookDAO;
        this.memberDAO = memberDAO;
    }


    public void borrowBook(
            int memberId,
            int bookId) {

        Member member =
                memberDAO.getMemberById(memberId);

        if (member == null) {
            throw new IllegalArgumentException(
                    "Member not found"
            );
        }

        Book book =
                bookDAO.getBookByID(bookId);

        if (book == null) {
            throw new IllegalArgumentException(
                    "Book not found"
            );
        }


        if (!hasMembership(member)) {
            throw new IllegalStateException(
                    "Membership has expired"
            );
        }



        if (book.getSituation()
                != BookSituation.FREE) {

            throw new IllegalStateException(
                    "Book is not available"
            );
        }



        if (borrowDAO.getMemberBooks(member).size()
                >= member.getBookLimit()) {

            throw new IllegalStateException(
                    "Book limit reached"
            );
        }


        LocalDate borrowDate =
                LocalDate.now();

        LocalDate returnDate =
                borrowDate.plusDays(
                        member.getReturnLimit()
                );


        borrowDAO.borrowBook(
                book,
                member,
                borrowDate,
                returnDate
        );



        book.setSituation(
                BookSituation.BORROWED
        );

        bookDAO.updateBook(book);
    }



    public void returnBook(
            int memberId,
            int bookId) {

        Member member =
                memberDAO.getMemberById(memberId);

        if (member == null) {
            throw new IllegalArgumentException(
                    "Member not found"
            );
        }

        Book book =
                bookDAO.getBookByID(bookId);

        if (book == null) {
            throw new IllegalArgumentException(
                    "Book not found"
            );
        }


        List<Book> memberBooks =
                borrowDAO.getMemberBooks(member);


        boolean borrowed =
                memberBooks.stream()
                        .anyMatch(
                                b -> b.getId() == bookId
                        );

        if (!borrowed) {
            throw new IllegalStateException(
                    "This book is not borrowed by this member"
            );
        }


        LocalDate returnDate =
                borrowDAO.returnBook(
                        book,
                        member
                );


        if(returnDate.isBefore(LocalDate.now())) {
            System.out.println("the book is been late for"+ChronoUnit.DAYS.between(returnDate,LocalDate.now()));
        }
        book.setSituation(
                BookSituation.FREE
        );

        bookDAO.updateBook(book);
    }

    public List<Book> getMemberBooks(
            int memberId) {

        Member member =
                memberDAO.getMemberById(memberId);

        if (member == null) {
            throw new IllegalArgumentException(
                    "Member not found"
            );
        }

        return borrowDAO.getMemberBooks(member);
    }


    private boolean hasMembership(
            Member member) {

        long passedDays =
                ChronoUnit.DAYS.between(
                        member.getLastRenewalDate(),
                        LocalDate.now()
                );

        return member.getMembershipDays()
                - passedDays > 0;
    }
}