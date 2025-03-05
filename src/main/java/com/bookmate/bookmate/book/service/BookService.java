package com.bookmate.bookmate.book.service;

import com.bookmate.bookmate.book.dto.BookRequestDto;
import com.bookmate.bookmate.book.entity.Book;
import com.bookmate.bookmate.book.entity.UserBookRecords;
import com.bookmate.bookmate.book.repository.BookRepository;
import com.bookmate.bookmate.book.repository.UserBookRecordsRepository;
import com.bookmate.bookmate.user.entity.User;
import com.bookmate.bookmate.user.exception.UserNotFoundException;
import com.bookmate.bookmate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {

  private final BookRepository bookRepository;
  private final UserBookRecordsRepository userBookRecordsRepository;
  private final UserRepository userRepository;

  @Transactional
  public Book userSavedBook(Long userId, BookRequestDto bookRequestDto) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));

    Book book = bookRepository.save(bookRequestDto.toEntity());

    userBookRecordsRepository.save(UserBookRecords.builder().user(user).book(book).build());

    return book;
  }
}
