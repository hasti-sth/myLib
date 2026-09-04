package org.example.manager;

import org.example.model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookManager {
    private List<Book> books=new ArrayList<Book>();
    public void addBook(Book book){
        books.add(book);
    }
    public List<Book> getBooks(){
        return books;
    }
    public Optional<Book> getBook(int id){
        return books.stream().filter(b->b.getId()==id).findFirst();
    }
}
