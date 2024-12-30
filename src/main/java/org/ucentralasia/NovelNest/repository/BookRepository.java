package org.ucentralasia.NovelNest.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.ucentralasia.NovelNest.model.book.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Find the books ordered by the number of likes in descending order
    @Query("SELECT b FROM Book b LEFT JOIN b.likedByUsers u GROUP BY b ORDER BY COUNT(u) DESC")
    List<Book> findMostLikedBooks();

    // Search by book name
    List<Book> findByNameContainingIgnoreCase(String name);

    // Search by author
    List<Book> findByAuthorContainingIgnoreCase(String author);
}
