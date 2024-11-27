package com.example.libraryapp.view;

import com.example.libraryapp.LibraryApplication;
import com.example.libraryapp.utils.ThreadController;
import com.example.libraryapp.datetime.CustomDateTimeFormatter;
import com.example.libraryapp.controller.BookController;
import com.example.libraryapp.model.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.ResourceBundle;

public class BookManagementPanel implements Initializable {
    public static final String EMPTY_STRING = "";
    private static final MenuPanel mainMenuPanel = new MenuPanel();
    private static Stage currentStage = MenuPanel.getStage();
    private static final BookController bookController = ThreadController.getBookControllerInstance();

    @FXML
    private TextField bookIdField;
    @FXML
    private TextField bookNameField;
    @FXML
    private TextField bookAuthorField;
    @FXML
    private TextField bookCategoryField;
    @FXML
    private TextField publisherField;
    @FXML
    private TextField publicationYearField;
    @FXML
    private TextField editionField;
    @FXML
    private TextField bookCountField;
    @FXML
    private TextField searchField;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button searchButton;
    @FXML
    private TableView<Book> bookTableView;
    @FXML
    private TableColumn<Book, String> idColumn;
    @FXML
    private TableColumn<Book, String> nameColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, String> categoryColumn;
    @FXML
    private TableColumn<Book, String> publisherColumn;
    @FXML
    private TableColumn<Book, Year> publicationYearColumn;
    @FXML
    private TableColumn<Book, Integer> editionColumn;
    @FXML
    private TableColumn<Book, Integer> bookCountColumn;

    private void displayBooksInTable(List<Book> books) {
        ObservableList<Book> bookList = FXCollections.observableArrayList(books);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        publicationYearColumn.setCellValueFactory(new PropertyValueFactory<>("publicationYear"));
        editionColumn.setCellValueFactory(new PropertyValueFactory<>("edition"));
        bookCountColumn.setCellValueFactory(new PropertyValueFactory<>("bookCount"));
        bookTableView.setItems(bookList);
    }

    private void resetFormState() {
        bookIdField.setDisable(true);
        bookNameField.setDisable(true);
        bookAuthorField.setDisable(true);
        bookCategoryField.setDisable(true);
        publisherField.setDisable(true);
        publicationYearField.setDisable(true);
        editionField.setDisable(true);
        bookCountField.setDisable(true);
        addButton.setDisable(false);
        editButton.setDisable(true);
        deleteButton.setDisable(true);
        saveButton.setDisable(true);
        displayBooksInTable(BookController.getBooksList());
    }

    private void clearFormFields() {
        bookIdField.clear();
        bookNameField.clear();
        bookAuthorField.clear();
        bookCategoryField.clear();
        publisherField.clear();
        publicationYearField.clear();
        editionField.clear();
        bookCountField.clear();
        searchField.clear();
    }

    private void showAlertDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Alert");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String generateBookId() {
        LocalDateTime currentDate = LocalDateTime.now();
        return "BOOK_" + CustomDateTimeFormatter.formatDateTime(currentDate, CustomDateTimeFormatter.getCreateIdPattern());
    }

    @FXML
    public void onAddButtonClick() {
        enableFormFields();
        addButton.setDisable(true);
        saveButton.setDisable(false);
        editButton.setDisable(true);
        deleteButton.setDisable(true);
        clearFormFields();
        bookIdField.setText(generateBookId());
    }

    @FXML
    public void onSaveButtonClick() {
        if (bookNameField.getText().isEmpty()) {
            showAlertDialog("Book name is required.");
        } else if (bookCountField.getText().isEmpty()) {
            showAlertDialog("Number of books is required.");
        } else {
            try {
                int edition;
                Year publicationYear;
                if (editionField.getText().isEmpty()) {
                    edition = 1;
                } else {
                    edition = Integer.parseInt(editionField.getText().trim());
                }
                if (publicationYearField.getText().isEmpty()) {
                    publicationYear = null;
                } else {
                    publicationYear = Year.parse(publicationYearField.getText().trim());
                }
                bookController.addBook(new Book(bookIdField.getText().trim(), bookNameField.getText().toUpperCase().trim(), bookAuthorField.getText().toUpperCase().trim(), bookCategoryField.getText().trim(),
                        publisherField.getText().toLowerCase().trim(), publicationYear, edition, Integer.parseInt(bookCountField.getText().trim())));
                clearFormFields();
                resetFormState();
            } catch (NumberFormatException e) {
                showAlertDialog("Please enter valid numbers for edition and book count.");
            }
        }
    }

    @FXML
    public void onEditButtonClick() {
        if (!bookIdField.getText().isEmpty()) {
            if (bookNameField.getText().isEmpty()) {
                showAlertDialog("Book name is required.");
            } else if (bookCountField.getText().isEmpty()) {
                showAlertDialog("Number of books is required.");
            } else {
                int edition;
                Year publicationYear;
                if (editionField.getText().isEmpty()) {
                    edition = 0;
                } else {
                    edition = Integer.parseInt(editionField.getText().trim());
                }
                if (publicationYearField.getText().isEmpty()) {
                    publicationYear = null;
                } else {
                    publicationYear = Year.parse(publicationYearField.getText().trim());
                }
                bookController.updateBook(bookIdField.getText(), new Book(bookIdField.getText().trim(), bookNameField.getText().toUpperCase().trim(), bookAuthorField.getText().toUpperCase().trim(), bookCategoryField.getText().trim(),
                        publisherField.getText().toLowerCase().trim(), publicationYear, edition, Integer.parseInt(bookCountField.getText().trim())));
                clearFormFields();
                resetFormState();
            }
        }
    }

    @FXML
    public void onDeleteButtonClick() {
        if (!bookIdField.getText().isEmpty()) {
            bookController.deleteBook(bookIdField.getText());
            clearFormFields();
            resetFormState();
        }
    }

    @FXML
    public void onRowSelect() {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            bookIdField.setText(selectedBook.getId());
            bookNameField.setText(selectedBook.getName());
            bookAuthorField.setText(selectedBook.getAuthor());
            bookCategoryField.setText(selectedBook.getCategory());
            publisherField.setText(selectedBook.getPublisher());
            publicationYearField.setText(selectedBook.getPublicationYear() != null ? selectedBook.getPublicationYear().toString() : "");
            editionField.setText(String.valueOf(selectedBook.getEdition()));
            bookCountField.setText(String.valueOf(selectedBook.getBookCount()));
            enableFormFields();
            addButton.setDisable(false);
            saveButton.setDisable(true);
            editButton.setDisable(false);
            deleteButton.setDisable(false);
        }
    }

    @FXML
    public void onSearchButtonClick() {
        if (searchField.getText().isEmpty()) {
            displayBooksInTable(BookController.getBooksList());
        } else {
            displayBooksInTable(bookController.searchBooks(searchField.getText().toLowerCase().trim()));
        }
    }

    private void enableFormFields() {
        bookNameField.setDisable(false);
        bookAuthorField.setDisable(false);
        bookCategoryField.setDisable(false);
        publisherField.setDisable(false);
        publicationYearField.setDisable(false);
        editionField.setDisable(false);
        bookCountField.setDisable(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resetFormState();
        clearFormFields();
    }

    public static void exitApplication() {
        try {
            currentStage.close();
            currentStage = new Stage();
            MenuPanel.setStage(currentStage);
            FXMLLoader loader = new FXMLLoader(LibraryApplication.class.getResource("menu.fxml"));
            Scene scene = new Scene(loader.load(), 1500, 800);
            currentStage.setTitle("Library Menu");
            currentStage.setScene(scene);
            currentStage.setX(10);
            currentStage.setY(15);
            currentStage.setOnCloseRequest(event -> MenuPanel.exit());
            currentStage.show();
        } catch (IOException e) {
            System.out.println("Error loading menu.");
        }
    }
}
