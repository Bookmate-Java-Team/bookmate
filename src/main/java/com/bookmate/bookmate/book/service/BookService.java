package com.bookmate.bookmate.book.service;

import com.bookmate.bookmate.book.dto.BookRequestDto;
import com.bookmate.bookmate.book.entity.Book;
import com.bookmate.bookmate.book.entity.UserBookRecord;
import com.bookmate.bookmate.book.repository.BookRepository;
import com.bookmate.bookmate.book.repository.UserBookRecordRepository;
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
  private final UserBookRecordRepository userBookRecordRepository;
  private final UserRepository userRepository;

  @Transactional
  public Book userSavedBook(Long userId, BookRequestDto bookRequestDto) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));

    Book book = bookRepository.findByIsbn(bookRequestDto.getIsbn())
        .orElseGet(() -> bookRepository.save(bookRequestDto.toEntity()));

    userBookRecordRepository.save(UserBookRecord.builder().user(user).book(book).readDate(bookRequestDto.getReadDate()).build());

    return book;
  }
}
