# NovelNest

## This application has been designed to help users find their favourites books for free

## There are two types of users that can register in this app, regular and contributor
## Regular users cannot upload any material unlike Contributors that can upload
## Works with psql database

# Lets go step by step on how the application works
1. User registration
- POST: http://localhost:8080/auth/register

- {
  "username": "Quizlet",
  "password": "letMeQuizIt",
  "email": "quizletone85@gmail.com",
  "role": "ROLE_CONTRIBUTOR"
  }

## Before login the user shall click on the confirmation link sent to their email
2. User login
- POST: http://localhost:8080/auth/login
- {
  "username": "Quizlet",
  "password": "letMeQuizIt"
  }

## User receive a jwt token as a response to get authenticated with

3. Upload a book 
- POST: http://localhost:8080/books/upload
- Body: form-data
- name: "The Bat"
- author: "Jo Nesbo"
- genre: "fiction"
- description: "A unique detective who solves the murder mystery of his colleague"
- file: A pdf file shall be inserted

4. Get all the books
- GET: http://localhost:8080/books

5. Get a book by id 
- GET http://localhost:8080/books/1

6. Download a book by its id
- GET: http://localhost:8080/books/3/download

7. Search book by its name
- GET: http://localhost:8080/books/search/name?q=successful

8. Search a book by its author (partial searches work too)
- GET: http://localhost:8080/books/search/author?q=Nesbo

9. Delete a book by id
- DELETE: http://localhost:8080/books/2

10. Update a book data
- PATCH: http://localhost:8080/books/3
- {
  "name": "Ask and it is given 2"
  }

11. Like a book
- POST: http://localhost:8080/books/1/like

12. Unlike a book
- POST: http://localhost:8080/books/1/unlike

13. Forgot password request (user receives a token through email)
- POST: http://localhost:8080/auth/forgot-password

14. Reset password
- POST: http://localhost:8080/auth/reset-password
- {
  "token": "864e7f83-d127-4643-905f-f3cc063bde0f",
  "newPassword": "AtamanHole"
  }

### Above are the functionalities the application has. There is still a lot of room for improvement
### One important consideration for future update is that users shall confirm their email before they can send a forgot-password request (As of now its missing)
### Happy New Year!!!