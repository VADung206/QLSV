package com.example.library.control;

import com.example.library.filehandle.FileBookHandler;
import com.example.library.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookManager implements IManagement<Book> {
    private static final List<Book> bookCollection = new ArrayList<>();

    public BookManager() {
        System.out.println("Initializing Book Manager...");
        FileBookHandler.loadFromFile(bookCollection);
    }

    public static List<Book> retrieveBooks() {
        return bookCollection;
    }

    @Override
    public void add(Book newBook) {
        if (newBook != null) {
            bookCollection.add(newBook);
            FileBookHandler.saveToFile(bookCollection);
            System.out.println("Book added successfully.");
        } else {
            System.err.println("Failed to add: Book is null!");
        }
    }

    @Override
    public void update(String bookId, Book updatedBook) {
        int index = locateBookIndex(bookId);
        if (index >= 0) {
            bookCollection.set(index, updatedBook);
            FileBookHandler.saveToFile(bookCollection);
        } else {
            System.err.println("Update failed: Book ID not found.");
        }
    }

    @Override
    public void delete(String bookId) {
        int indexToRemove = locateBookIndex(bookId);
        if (indexToRemove >= 0) {
            bookCollection.remove(indexToRemove);
            FileBookHandler.saveToFile(bookCollection);
        } else {
            System.err.println("Delete failed: Book ID not found.");
        }
    }

    @Override
    public int findIndexById(String bookId) {
        return locateBookIndex(bookId);
    }

    private int locateBookIndex(String bookId) {
        System.out.println("Locating book with ID: " + bookId);
        for (int i = 0; i < bookCollection.size(); i++) {
            if (bookCollection.get(i).getIdBook().equalsIgnoreCase(bookId)) {
                return i;
            }
        }
        return -1;
    }

    public List<Book> searchBooks(String keyword) {
        List<Book> result = new ArrayList<>();
        for (Book book : bookCollection) {
            if (book != null && (book.getIdBook().contains(keyword) ||
                    book.getNameBook().contains(keyword) ||
                    book.getAuthor().contains(keyword) ||
                    book.getPublishingCompany().contains(keyword))) {
                result.add(book);
            }
        }
        return result;
    }

    public List<String> extractBookIds() {
        List<String> ids = new ArrayList<>();
        for (Book book : bookCollection) {
            if (book != null) {
                ids.add(book.getIdBook());
            }
        }
        return ids;
    }

    public void debugPrintCollection() {
        System.out.println("Current collection state:");
        for (Book book : bookCollection) {
            System.out.println(book);
        }
    }
}
