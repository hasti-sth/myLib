package org.example.controller;

import org.example.model.Book;
import org.example.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookByID(@PathVariable int id){
        Book book=bookService.getBookByID(id);
        if(book==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(){
        List<Book> books=bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book){
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.addBook(book));
    }
    @PutMapping
    public ResponseEntity<Book> updateBook(@RequestBody Book book){
        boolean updated=bookService.updateBook(book);
        if(!updated){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }
    @GetMapping(params = "title")
    public ResponseEntity<List<Book>> getBooksByTitle(@RequestParam String title){
        return ResponseEntity.ok(bookService.getBookByTitle(title));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Book> deleteBookByID(@PathVariable int id){
        boolean deleted=bookService.DeleteBookByID(id);
        if(!deleted){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
