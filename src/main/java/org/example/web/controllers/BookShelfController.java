package org.example.web.controllers;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.example.app.exceptions.BookShelfLoginException;
import org.example.app.exceptions.EmptyFileException;
import org.example.app.exceptions.RegexException;
import org.example.app.services.BookService;
import org.example.app.services.RegExValidatorService;
import org.example.web.dto.Book;
import org.example.web.dto.BookIdToRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

@Slf4j
@Controller
@Scope("singleton")
@RequiredArgsConstructor
@RequestMapping(value = "/books")
public class BookShelfController {

    private final BookService bookService;

    @GetMapping("/shelf")
    public String books(Model model) {
        log.info(this.toString());
        model.addAttribute("book", new Book());
        model.addAttribute("bookIdToRemove", new BookIdToRemove());
        model.addAttribute("bookList", bookService.getAllBooks());
        return "book_shelf";
    }

    @PostMapping("/save")
    public String saveBook(@Valid Book book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("bookList", bookService.getAllBooks());
            return "book_shelf";
        } else {
            bookService.saveBook(book);
            log.info("current repository size: " + bookService.getAllBooks().size());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/remove")
    public String removeBook(@Valid BookIdToRemove bookIdToRemove, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", new Book());
            model.addAttribute("bookList", bookService.getAllBooks());
            return "book_shelf";
        } else {
            bookService.removeBookById(bookIdToRemove.getId());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/removeByRegex")
    @SneakyThrows
    public String removeBookByRegex(@RequestParam(value = "queryRegex", defaultValue = "0")
                                    String queryRegex) {
        if (new RegExValidatorService(queryRegex).regexValidator()) {
            bookService.removeByRegex(queryRegex);
            return "redirect:/books/shelf";
        } else {
            throw new RegexException("Invalid Query Regex Request!");
        }
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        String name = file.getOriginalFilename();
        byte[] bytes = file.getBytes();
        if (bytes.length == 0) {
            log.info("File not Found or Empty");
            throw new EmptyFileException("File not Found or Empty");
        }

        //create dir
        String rootPath = System.getProperty("catalina.home");
        File dir = new File(rootPath + File.separator + "external_uploads");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //create file
        File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
        stream.write(bytes);
        stream.close();

        log.info("new file saved at: " + serverFile.getAbsolutePath());

        return "redirect:/books/shelf";
    }

    @ExceptionHandler(EmptyFileException.class)
    public String handleError(Model model, EmptyFileException exception) {
        model.addAttribute("errorMessage", exception.getMessage());

        log.warn("we throw!");
        return "errors/403";
    }

    @ExceptionHandler(RegexException.class)
    public String handleError(Model model, RegexException exception) {
        model.addAttribute("errorMessage", exception.getMessage());

        log.warn("we throw new!");
        return "errors/R403";
    }
}
