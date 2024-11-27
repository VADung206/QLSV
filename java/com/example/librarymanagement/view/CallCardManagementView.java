package com.example.librarymanagement.view;

import com.example.librarymanagement.utils.FxHelper;
import com.example.librarymanagement.LibraryApp;
import com.example.librarymanagement.thread.ThreadManager;
import com.example.librarymanagement.control.*;
import com.example.librarymanagement.datetime.CustomDateTimeFormatter;
import com.example.librarymanagement.filehandler.BookFileHandler;
import com.example.librarymanagement.filehandler.CallCardFileHandler;
import com.example.librarymanagement.filehandler.CallCardInfoFileHandler;
import com.example.librarymanagement.model.Book;
import com.example.librarymanagement.model.CallCard;
import com.example.librarymanagement.model.CallCardInfo;
import com.example.librarymanagement.model.Reader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class CallCardView implements Initializable {
    private static final String EMPTY_VALUE = "";
    private static Stage mainStage = MenuView.mainStage;
    private static final ReaderControl readerControl = ThreadManager.getReaderControl();
    private static final BookControl bookControl = ThreadManager.getBookControl();
    private static final StaffControl staffControl = ThreadManager.getStaffControl();
    private static final CallCardControl callCardControl = ThreadManager.getCallCardControl();
    private static final CallCardInfoControl callCardInfoControl = ThreadManager.getCallCardInfoControl();
    private static final ObservableList<String> readerList = FXCollections.observableArrayList(readerControl.getReaderIds());
    private static final ObservableList<String> bookList = FXCollections.observableArrayList(bookControl.getBookIds());
    private CallCard activeCallCard;

    @FXML private TextField bookNameField;
    @FXML private TextField publisherField;
    @FXML private Button addBookButton;
    @FXML private TextField searchField;
    @FXML private TextField callCardIdField;
    @FXML private ComboBox<String> readerIdComboBox;
    @FXML private TextField returnDateField;
    @FXML private Button addCallCardButton;
    @FXML private TableColumn<CallCardInfo, String> bookIdColumn;
    @FXML private TextField readerNameField;
    @FXML private TextField publicationYearField;
    @FXML private TextField loanedCopiesField;
    @FXML private ComboBox<String> bookIdComboBox;
    @FXML private TextField staffField;
    @FXML private TextField reprintCountField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;
    @FXML private TextField bookLoanDateField;
    @FXML private Button saveCallCardButton;
    @FXML private TextField categoryField;
    @FXML private Button cancelBookButton;
    @FXML private Button exitButton;
    @FXML private TextField authorField;
    @FXML private Button saveBookButton;
    @FXML private TableColumn<CallCardInfo, Integer> loanedCopiesColumn;
    @FXML private Button cancelCallCardButton;
    @FXML private Button searchButton;
    @FXML private TableView<CallCardInfo> callCardTableView;

    private void updateTableData(List<CallCardInfo> dataList) {
        ObservableList<CallCardInfo> callCardInfoList = FXCollections.observableArrayList(dataList);
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<CallCardInfo, String>("book"));
        loanedCopiesColumn.setCellValueFactory(new PropertyValueFactory<CallCardInfo, Integer>("loanedCopies"));
        callCardTableView.setItems(callCardInfoList);
    }

    private void clearBookForm() {
        loanedCopiesField.setText("");
        bookIdComboBox.setValue("");
        bookNameField.setText("");
        authorField.setText("");
        categoryField.setText("");
        publisherField.setText("");
        publicationYearField.setText("");
        reprintCountField.setText("");
        bookIdComboBox.setDisable(true);
        addBookButton.setDisable(true);
        saveBookButton.setDisable(true);
        cancelBookButton.setDisable(true);
        loanedCopiesField.setDisable(true);
    }

    @FXML
    void onReaderSelection(KeyEvent event) {
        resetForm();
        addBookButton.setDisable(false);
        Reader reader = readerControl.getReaders().get(readerControl.getReaderIndexById(readerIdComboBox.getValue()));
        if (!reader.isLocked()) {
            readerNameField.setText(reader.getName());
            addressField.setText(reader.getAddress());
            phoneField.setText(reader.getPhoneNumber());
            FxHelper.setComboBoxValue(readerIdComboBox);
        } else {
            showAlert("The reader's card is locked.");
        }
    }

    @FXML
    void onBookSelection(KeyEvent event) {
        clearBookForm();
        addBookButton.setDisable(false);
        Book book = bookControl.getBooks().get(bookControl.getBookIndexById(bookIdComboBox.getValue()));
        bookNameField.setText(book.getName());
        if (!book.getAuthor().isEmpty()) {
            authorField.setText(book.getAuthor());
        }
        if (!book.getCategory().isEmpty()) {
            categoryField.setText(book.getCategory());
        }
        if (!book.getPublisher().isEmpty()) {
            publisherField.setText(book.getPublisher());
        }
        if (book.getPublicationYear() != null) {
            publicationYearField.setText(book.getPublicationYear().toString());
        }
        reprintCountField.setText(String.valueOf(book.getReprintCount()));
        FxHelper.setComboBoxValue(bookIdComboBox);
    }

    private void resetForm() {
        clearBookForm();
        returnDateField.setText("");
        callCardIdField.setText("");
        readerIdComboBox.setValue("");
        readerNameField.setText("");
        addressField.setText("");
        phoneField.setText("");
        staffField.setText("");
        bookLoanDateField.setText("");
        readerIdComboBox.setDisable(true);
        addCallCardButton.setDisable(false);
        saveCallCardButton.setDisable(true);
        cancelCallCardButton.setDisable(true);
        callCardTableView.setItems(null);
    }

    private String generateCallCardId() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return "CC" + CustomDateTimeFormatter.formatDateTime(currentDateTime, CustomDateTimeFormatter.getIdPattern());
    }

    @FXML
    protected void onRowSelection(MouseEvent event) {
        CallCardInfo selectedRow = callCardTableView.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            if (event.getClickCount() == 1) {
                bookIdComboBox.setValue(selectedRow.getBook().getId());
                bookNameField.setText(selectedRow.getBook().getName());
                authorField.setText(selectedRow.getBook().getAuthor());
                categoryField.setText(selectedRow.getBook().getCategory());
                publisherField.setText(selectedRow.getBook().getPublisher());
                String year = selectedRow.getBook().getPublicationYear() != null ? selectedRow.getBook().getPublicationYear().toString() : "";
                publicationYearField.setText(year);
                reprintCountField.setText(String.valueOf(selectedRow.getBook().getReprintCount()));
                loanedCopiesField.setText(String.valueOf(selectedRow.getLoanedCopies()));
                returnDateField.setText(CustomDateTimeFormatter.formatDateTime(selectedRow.getReturnDate(), CustomDateTimeFormatter.getPattern()));
            }
            if (event.getClickCount() == 2) {
                for (CallCardInfo info : callCardInfoControl.getCallCardInfoById(callCardIdField.getText())) {
                    Book book = info.getBook();
                    if (book.getId().equals(bookIdComboBox.getValue())) {
                        book.setAvailableCopies(book.getAvailableCopies() + info.getLoanedCopies());
                    }
                }
                callCardInfoControl.removeCallCardInfoById(callCardIdField.getText(), bookIdComboBox.getValue());
                updateTableData(callCardInfoControl.getCallCardInfoById(callCardIdField.getText()));
                clearBookForm();
                addBookButton.setDisable(false);
            }
        }
    }

    @FXML
    protected void onAddBookButtonClick(ActionEvent event) {
        bookIdComboBox.setValue("");
        bookNameField.setText("");
        authorField.setText("");
        categoryField.setText("");
        publisherField.setText("");
        publicationYearField.setText("");
        reprintCountField.setText("");
        bookIdComboBox.setDisable(false);
        bookIdComboBox.setItems(bookList);
        FxHelper.autoCompleteComboBoxPlus(bookIdComboBox, (typedText, item) -> item.toUpperCase().contains(typedText.toUpperCase()));
        addBookButton.setDisable(true);
        saveBookButton.setDisable(false);
        cancelBookButton.setDisable(false);
        loanedCopiesField.setDisable(false);
    }

    @FXML
    protected void onSaveBookButtonClick(ActionEvent event) {
        int availableCopies = bookControl.getBooks().get(bookControl.getBookIndexById(bookIdComboBox.getValue())).getAvailableCopies();
        try {
            int loanedCopies = Integer.parseInt(loanedCopiesField.getText());
            if (availableCopies < loanedCopies) {
                throw new Exception();
            }
            Book book = bookControl.getBooks().get(bookControl.getBookIndexById(bookIdComboBox.getValue()));
            CallCardInfo callCardInfo = new CallCardInfo(book, loanedCopies);
            callCardInfoControl.addCallCardInfo(callCardInfo, callCardIdField.getText());
            updateTableData(callCardInfoControl.getCallCardInfoById(callCardIdField.getText()));
            book.setAvailableCopies(availableCopies - loanedCopies);
            clearBookForm();
        } catch (Exception ex) {
            showAlert("Invalid loaned copies or no available copies left.");
        }
    }

    @FXML
    protected void onSaveCallCardButtonClick(ActionEvent event) {
        try {
            LocalDateTime loanDate = LocalDateTime.parse(bookLoanDateField.getText(), CustomDateTimeFormatter.getPattern());
            CallCard newCallCard = new CallCard(callCardIdField.getText(), readerIdComboBox.getValue(), loanDate, staffField.getText());
            callCardControl.addCallCard(newCallCard);
            showAlert("Call card has been saved.");
            resetForm();
        } catch (Exception ex) {
            showAlert("An error occurred while saving the call card.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        readerIdComboBox.setItems(readerList);
        bookIdComboBox.setItems(bookList);
        bookIdComboBox.setDisable(true);
        addBookButton.setDisable(true);
        saveBookButton.setDisable(true);
        cancelBookButton.setDisable(true);
        loanedCopiesField.setDisable(true);
        saveCallCardButton.setDisable(true);
        cancelCallCardButton.setDisable(true);
        searchButton.setDisable(true);
        callCardIdField.setText(generateCallCardId());
    }
}
