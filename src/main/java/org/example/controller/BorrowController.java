package org.example.controller;

import org.example.model.Book;
import org.example.service.BorrowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members/{memberId}/borrows")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(
            BorrowService borrowService) {

        this.borrowService = borrowService;
    }


    @PostMapping("/{bookId}")
    public ResponseEntity<Void> borrowBook(
            @PathVariable int memberId,
            @PathVariable int bookId) {

        borrowService.borrowBook(
                memberId,
                bookId
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }


    @GetMapping
    public ResponseEntity<List<Book>> getMemberBooks(
            @PathVariable int memberId) {

        return ResponseEntity.ok(
                borrowService.getMemberBooks(
                        memberId
                )
        );
    }


    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> returnBook(
            @PathVariable int memberId,
            @PathVariable int bookId) {

        borrowService.returnBook(
                memberId,
                bookId
        );

        return ResponseEntity.noContent().build();
    }
}
