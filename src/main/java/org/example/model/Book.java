package org.example.model;

import org.example.model.enums.BookSituation;

import java.time.LocalDate;

public class Book {
    private final String title;
    private final String author;
    private final String publisher;
    private final String genre;
    private final String description;
    private final LocalDate publishedDate;
    private double price;
    private BookSituation situation;
    private int id;

    public Book(String title, String author, String publisher, String genre, String description, LocalDate publishedDate, double price, BookSituation situation) {
            this.title = title;
            this.author = author;
            this.publisher = publisher;
            this.genre = genre;
            this.description = description;
            this.publishedDate = publishedDate;
            this.price = price;
            this.situation= situation;
        }
        public String getTitle() {
          return title;
        }
        public String getAuthor() {
          return author;
        }
        public String getPublisher() {
          return publisher;
        }
        public String getGenre() {
          return genre;
        }
        public String getDescription() {
          return description;
        }
        public LocalDate getPublishedDate() {
          return publishedDate;
        }
        public double getPrice() {
          return price;
        }
        public BookSituation getSituation() {
          return situation;
        }
        public void setSituation(BookSituation situation) {
          this.situation = situation;
        }
        public void setPrice(double price) {
        this.price = price;
        }

        public int getId() {
        return id;
        }

        public void setId(int id) {
        this.id=id;
        }
    }
