package com.bookmate.bookmate.book.service;


import com.bookmate.bookmate.book.dto.AladinApiResponseDto;
import com.bookmate.bookmate.book.entity.Book;
import com.bookmate.bookmate.book.entity.SaveBook;
import com.bookmate.bookmate.book.repository.BookRepository;
import com.bookmate.bookmate.book.repository.SaveBookRepository;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class BookService {

  private final BookRepository bookRepository;
  private final SaveBookRepository saveBookRepository;

  @Transactional
  public void saveBook(AladinApiResponseDto request) {
    Book book = bookRepository.save(Book.builder()
        .isbn13(request.getIsbn13())
        .title(request.getTitle())
        .author(request.getAuthor())
        .publisher(request.getPublisher())
        .coverUrl(request.getCover())
        .publishedDate(LocalDate.parse(request.getPubDate()))
        .build());

    SaveBook saveBook = new SaveBook();
    saveBook.setBook(book);
    saveBookRepository.save(saveBook);
  }

}
