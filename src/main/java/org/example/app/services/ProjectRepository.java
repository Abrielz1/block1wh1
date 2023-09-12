package org.example.app.services;

import java.util.List;

public interface ProjectRepository<T> {
    List<T> retreiveAll();

    void store(T book);

    boolean removeItemById(Integer bookIdToRemove);

    void deleteBookById(Integer id);

    void deleteBookByAuthor(String regex);

    void deleteBookByTitle(String regex);

    void deleteBookBySize(Integer id);
}
