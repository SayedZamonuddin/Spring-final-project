package org.ucentralasia.NovelNest.model.book;

import jakarta.persistence.*;
import org.ucentralasia.NovelNest.model.user.User;

import java.util.Set;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String author;
    private String genre;
    private String description;
    private String filePath;

    @ManyToMany(mappedBy = "likedBooks")
    private Set<User> likedByUsers;

    public Book(){}

    public Book(String name, String Author, String genre, String description, String filePath){
        this.name = name;
        this.author = Author;
        this.genre = genre;
        this.description = description;
        this.filePath = filePath;
    }

    public Book(Long id, String name, String author, String description, String genre, String filePath) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.description = description;
        this.genre = genre;
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}