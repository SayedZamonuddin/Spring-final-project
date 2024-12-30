package org.ucentralasia.NovelNest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ucentralasia.NovelNest.model.book.Book;
import org.ucentralasia.NovelNest.model.user.User;
import org.ucentralasia.NovelNest.repository.BookRepository;
import org.ucentralasia.NovelNest.repository.UserRepository;

import javax.persistence.Id;
import java.util.List;
import java.util.Optional;

@Service
public class LikedBooksService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    public void likeBook(Long bookId, String username) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (bookOpt.isPresent() && userOpt.isPresent()) {
            Book book = bookOpt.get();
            User user = userOpt.get();

            user.getLikedBooks().add(book);
            userRepository.save(user);
        }


    }

    public void unlikeBook(Long bookId, String username) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (bookOpt.isPresent() && userOpt.isPresent()) {
            Book book = bookOpt.get();
            User user = userOpt.get();

            user.getLikedBooks().remove(book);
            userRepository.save(user);
        }
    }

    public List<Book> getRecommendedBooks(String username){
        return bookRepository.findMostLikedBooks();
    }
}
