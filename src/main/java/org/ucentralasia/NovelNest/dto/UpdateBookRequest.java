package org.ucentralasia.NovelNest.dto;


import java.util.Optional;

public class UpdateBookRequest {

    private Optional<String> name = Optional.empty();
    private Optional<String> author = Optional.empty();
    private Optional<String> genre = Optional.empty();
    private Optional<String> description = Optional.empty();

    public UpdateBookRequest() {
    }

    public UpdateBookRequest(Optional<String> name, Optional<String> author, Optional<String> genre, Optional<String> description) {
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.description = description;
    }


    public Optional<String> getName() {
        return name;
    }

    public Optional<String> getAuthor() {
        return author;
    }

    public Optional<String> getGenre() {
        return genre;
    }

    public Optional<String> getDescription() {
        return description;
    }

    public void setName(Optional<String> name) {
        this.name = name;
    }

    public void setAuthor(Optional<String> author) {
        this.author = author;
    }

    public void setGenre(Optional<String> genre) {
        this.genre = genre;
    }

    public void setDescription(Optional<String> description) {
        this.description = description;
    }
}
