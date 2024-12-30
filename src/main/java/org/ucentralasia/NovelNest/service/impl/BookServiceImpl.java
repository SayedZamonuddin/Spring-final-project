package org.ucentralasia.NovelNest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.ucentralasia.NovelNest.dto.UpdateBookRequest;
import org.ucentralasia.NovelNest.exceptionhandling.ResourceNotFoundException;
import org.ucentralasia.NovelNest.repository.BookRepository;
import org.ucentralasia.NovelNest.model.book.Book;
import org.ucentralasia.NovelNest.service.BookService;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;


@Service
public class BookServiceImpl implements BookService {

    private static final String UPLOAD_DIR = "C:\\Users\\said\\OneDrive - University of Central Asia\\Desktop\\uploads\\";

    @Autowired
    private BookRepository bookRepository;

    @Override
    public String saveFile(MultipartFile file) throws Exception {
        if(file.isEmpty()){
            throw new Exception("Failed to store empty file");
        }

        // Create the upload directory if it does not exist
        File uploadDir = new File(UPLOAD_DIR);
        if(!uploadDir.exists()){
            uploadDir.mkdirs();
        }

        // Save the file locally
        String fileName = file.getOriginalFilename();
        if(fileName == null || fileName.isEmpty()){
            throw new Exception("File name is invalid");
        }

        String filePath = UPLOAD_DIR + fileName;
        File dest = new File(filePath);
        file.transferTo(dest);

        return filePath;
    }

    @Override
    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    //get all books
    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Get a book by its id
    @Override
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    // Delete a by book by id
    @Override
    public void deleteBook(Long id){
        if(!bookRepository.existsById(id)){
            throw new ResourceNotFoundException("Book not found with ID: " + id);
        }
        bookRepository.deleteById(id);
    }

    // implementing updatebook
    @Override
    public Book updateBookPartial(Long id, UpdateBookRequest updatedBook) throws Exception {
        return bookRepository.findById(id).map(existingBook -> {

            updatedBook.getName().ifPresent(existingBook::setName);
            updatedBook.getAuthor().ifPresent(existingBook::setAuthor);
            updatedBook.getGenre().ifPresent(existingBook::setGenre);
            updatedBook.getDescription().ifPresent(existingBook::setDescription);
            // Do not update filePath

            return bookRepository.save(existingBook);
        }).orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));
    }

    // implement downloading a book
    @Override
    public Resource loadFileAsResource(String filePath) throws Exception{
        try{
            Path uploadDir = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
            Path targetPath = Paths.get(filePath).toAbsolutePath().normalize();

            if (!targetPath.startsWith(uploadDir)) {
                throw new Exception("Invalid file path");
            }

            Resource resource = new UrlResource(targetPath.toUri());

            if(resource.exists() || resource.isReadable()){
                return resource;
            } else {
                throw new ResourceNotFoundException("File not found: " + filePath);}
        } catch (MalformedURLException ex){
                throw new Exception("File not found: " + filePath, ex);
                }
    }

    @Override
    public ResponseEntity<String> uploadBook (String name, String author, String genre, String description, MultipartFile file) throws Exception {
        try {
            // save the file and get the file path
            String filePath = saveFile(file);

            Book book = new Book(name, author, genre, description, filePath);
            saveBook(book);

            return ResponseEntity.ok("Book uploaded successfully!");
        } catch (Exception e){
            return ResponseEntity.status(500).body("Error uploading book" + e.getMessage());
        }
    }

    @Override
    public List<Book> searchBookByName(String name){
        return bookRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Book> searchBookByAuthor(String author){
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }
}
