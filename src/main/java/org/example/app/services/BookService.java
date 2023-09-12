package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final ProjectRepository<Book> bookRepo;
    private final Logger logger = Logger.getLogger(BookService.class);

    @Autowired
    public BookService(ProjectRepository<Book> bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> getAllBooks() {
        return bookRepo.retreiveAll();
    }

    public void saveBook(Book book) {
        bookRepo.store(book);
    }

    public boolean removeBookById(Integer bookIdToRemove) {
        return bookRepo.removeItemById(bookIdToRemove);
    }

    public void defaultInit() {
        logger.info("default INIT in book service");
    }

    public void defaultDestroy() {
        logger.info("default DESTROY in book service");
    }

    public void removeByRegex(String regex) {

        String[] array = regex.split(":");

        if (regex.contains("id")) {

            Integer id = Integer.parseInt(array[1]);
            bookRepo.deleteBookById(id);
        }
        if (regex.contains("author")) {
            bookRepo.deleteBookByAuthor(array[1]);
        }
        if (regex.contains("title")) {
            bookRepo.deleteBookByTitle(array[1]);
        }
        if (regex.contains("size")) {

            Integer id = Integer.parseInt(array[1]);
            bookRepo.deleteBookBySize(id);
        }
    }
}
