package org.example.web.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/books")
public class BookShelfController {

   // private Logger logger = Logger.getLogger(BookShelfController.class);
    private final BookService bookService;

//    @Autowired
//    public BookShelfController(BookService bookService) {
//        this.bookService = bookService;
//    }

    @GetMapping("/shelf")
    public String books(Model model) {
        log.info("got book shelf");
        model.addAttribute("book", new Book());
        model.addAttribute("bookList", bookService.getAllBooks());
        return "book_shelf";
    }

    @PostMapping("/save")
    public String saveBook(Book book) {
        if (Objects.equals(book.getAuthor(), "") || Objects.equals(book.getTitle(), "")) {
            return "redirect:/books/shelf";
        }
        bookService.saveBook(book);
        log.info("current repository size: " + bookService.getAllBooks().size());
        return "redirect:/books/shelf";
    }

    @PostMapping("/remove")
    public String removeBook(@RequestParam(value = "bookIdToRemove") Integer bookIdToRemove) {
        if (bookService.removeBookById(bookIdToRemove)) {
            return "redirect:/books/shelf";
        } else {
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/removeByRegex")
    public String removeByRegex(@RequestParam(value = "queryRegex") String regex) {
        if (bookService.removeByRegex(regex)) {
            return "redirect:/books/shelf";
        } else {
            return "redirect:/books/shelf";
        }
    }
}
