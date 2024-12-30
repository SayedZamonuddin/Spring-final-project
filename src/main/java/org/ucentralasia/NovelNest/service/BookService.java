package org.ucentralasia.NovelNest.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.ucentralasia.NovelNest.dto.UpdateBookRequest;
import org.ucentralasia.NovelNest.model.book.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    String saveFile(MultipartFile file) throws Exception;
    void saveBook(Book book);
    List<Book> getAllBooks();
    Optional<Book> getBookById(Long id);
    void deleteBook(Long id);
    Book updateBookPartial(Long id, UpdateBookRequest updatedBook) throws Exception;
    Resource loadFileAsResource(String filePath) throws Exception;
    ResponseEntity<String> uploadBook(String name, String author, String genre, String description, MultipartFile file) throws Exception;

    List<Book> searchBookByName(String name);
    List<Book> searchBookByAuthor(String author);
}
