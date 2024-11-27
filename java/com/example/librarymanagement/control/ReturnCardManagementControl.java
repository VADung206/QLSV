package com.example.library.control;

import com.example.library.filehandle.FileReturnCardHandler;
import com.example.library.model.Book;
import com.example.library.model.CallCard;
import com.example.library.model.ReturnCard;

import java.util.ArrayList;
import java.util.List;

public class ReturnCardManager {
    private static final List<ReturnCard> returnCardList = new ArrayList<>();

    public ReturnCardManager() {
        System.out.println("Loading return card data...");
        FileReturnCardHandler.loadReturnCards(
                returnCardList,
                CallCardManager.getAllCallCards(),
                StaffManager.getAllStaff(),
                BookManager.getAllBooks()
        );
    }

    public static List<ReturnCard> getAllReturnCards() {
        return returnCardList;
    }

    public void addReturnCard(ReturnCard newReturnCard) {
        if (newReturnCard != null) {
            returnCardList.add(newReturnCard);
            FileReturnCardHandler.saveReturnCards(returnCardList);
            System.out.println("Return card added: " + newReturnCard.getId());
        } else {
            System.err.println("Failed to add return card: Input is null.");
        }
    }

    public List<ReturnCard> searchReturnCardsById(String callCardId) {
        List<ReturnCard> matchedReturnCards = new ArrayList<>();
        for (ReturnCard returnCard : returnCardList) {
            if (returnCard.getCallCard().getIdCallCard().equalsIgnoreCase(callCardId)) {
                matchedReturnCards.add(returnCard);
            }
        }
        return matchedReturnCards;
    }

    public ReturnCard searchReturnCardByCallCardAndBook(CallCard callCard, Book book) {
        for (ReturnCard returnCard : returnCardList) {
            if (returnCard.getBook().equals(book) &&
                    returnCard.getCallCard().equals(callCard)) {
                System.out.println("Return card found for call card: " + callCard.getIdCallCard());
                return returnCard;
            }
        }
        System.err.println("No matching return card found.");
        return null;
    }

    public void printAllReturnCards() {
        System.out.println("Debug: Listing all return cards...");
        for (ReturnCard returnCard : returnCardList) {
            System.out.println(returnCard);
        }
    }
}
