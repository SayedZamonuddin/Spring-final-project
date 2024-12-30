package org.ucentralasia.NovelNest.model.user;

import lombok.Data;
import org.ucentralasia.NovelNest.model.book.Book;

import jakarta.persistence.*;

import java.util.Set;

@Data
@Entity
@Table(name= "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String role;

    private String email;

    private boolean enabled;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private VerificationToken verificationToken;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private PasswordResetToken passwordResetToken;

    @ManyToMany
    @JoinTable(
            name = "user_likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name="book_id")
    )

    private Set<Book> likedBooks;

    // constructors, getters and setters probably done by lombok


}
