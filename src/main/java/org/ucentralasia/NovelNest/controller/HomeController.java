package org.ucentralasia.NovelNest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.ucentralasia.NovelNest.service.LikedBooksService;
import org.ucentralasia.NovelNest.model.book.Book;
import org.ucentralasia.NovelNest.service.LikedBooksService;

import java.util.List;


@RestController
public class HomeController {
    @Autowired
    private LikedBooksService likedBooksService;

    @GetMapping("/home")
    public List<Book> getHomePageBooks(Authentication authentication) {
        String username = authentication.getName();
        return likedBooksService.getRecommendedBooks(username);
    }
}
