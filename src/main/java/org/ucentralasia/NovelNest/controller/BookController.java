package org.ucentralasia.NovelNest.controller;

import org.springframework.security.core.Authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.ucentralasia.NovelNest.dto.UpdateBookRequest;
import org.ucentralasia.NovelNest.exceptionhandling.ResourceNotFoundException;
import org.ucentralasia.NovelNest.model.book.Book;
import org.ucentralasia.NovelNest.service.BookService;
import org.ucentralasia.NovelNest.service.LikedBooksService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    //directory where is the files will be saved
    private static final String UPLOAD_DIR = "C:\\Users\\said\\OneDrive - University of Central Asia\\Desktop\\uploads\\";

    // Get all books endpoint

    @PreAuthorize("hasRole('CONTRIBUTOR')")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadBook(
            @RequestParam("name") String name,
            @RequestParam("author") String author,
            @RequestParam("genre") String genre,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        return bookService.uploadBook(name, author, genre, description, file);
    }
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    //  Get a book by id endpoint
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        Optional<Book> optionalBook = bookService.getBookById(id);
        if(optionalBook.isPresent()) {
            return ResponseEntity.ok(optionalBook.get());
        } else {
            return ResponseEntity.status(404).body("Book with id" + id + " not found");
        }
    }

    // Delete a book by id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        try{
            bookService.deleteBook(id);
            return ResponseEntity.ok("Book deleted successfully!");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    //Update
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody UpdateBookRequest updatedBook) {
        try{
            Book book = bookService.updateBookPartial(id, updatedBook);
            return ResponseEntity.ok(book);
        } catch (ResourceNotFoundException e){
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(500).body("Error updating message " + e.getMessage());
        }
    }

    // Download API
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadBookFile(@PathVariable Long id) {
        try {
            Optional<Book> optionalBook = bookService.getBookById(id);

            if (!optionalBook.isPresent()) {
                throw new ResourceNotFoundException("Book not found with ID: " + id);
            }

            Book book = optionalBook.get();
            String filePath = book.getFilePath();

            // Load file as Resource
            Resource resource = bookService.loadFileAsResource(filePath);

            // Determine the content type
            String contentType = null;
            try {
                Path path = resource.getFile().toPath();
                contentType = Files.probeContentType(path);
            } catch (IOException ex) {
                // Could not determine file type
                contentType = "application/octet-stream";
            }

            // Set default content type if not able to determine
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            // Build the response
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            throw new ResourceNotFoundException("Could not download file: " + e.getMessage());
        }
    }

    @Autowired
    private LikedBooksService likeBooksService;

    @PostMapping("/{id}/like")
    public ResponseEntity<?> likeBook(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        likeBooksService.likeBook(id, username);
        return ResponseEntity.ok("Book liked successfully!");
    }

    @PostMapping("/{id}/unlike")
    public ResponseEntity<?> unlikeBook(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        likeBooksService.unlikeBook(id, username);
        return ResponseEntity.ok("Book unliked successfully!");
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<Book>> searchByName(@RequestParam("q") String query) {
        List<Book> results = bookService.searchBookByName(query);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/search/author")
    public ResponseEntity<List<Book>> searchByAuthor(@RequestParam("q") String query) {
        List<Book> results = bookService.searchBookByAuthor(query);
        return ResponseEntity.ok(results);
    }

}
