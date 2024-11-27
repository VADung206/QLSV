package com.example.library.control;

import com.example.library.filehandle.FileCallCardHandler;
import com.example.library.model.CallCard;

import java.util.ArrayList;
import java.util.List;

public class CallCardManager {
    private static final List<CallCard> callCardList = new ArrayList<>();

    public CallCardManager() {
        System.out.println("Loading Call Cards...");
        FileCallCardHandler.loadCallCards(callCardList,
                ReaderManager.getAllReaders(),
                StaffManager.getAllStaff());
    }

    public static List<CallCard> getAllCallCards() {
        return callCardList;
    }

    public void addNewCallCard(CallCard newCallCard) {
        if (newCallCard != null) {
            callCardList.add(newCallCard);
            System.out.println("New call card added: " + newCallCard.getIdCallCard());
        } else {
            System.err.println("Failed to add: Call card is null.");
        }
    }

    public void removeCallCard(String callCardId) {
        int indexToRemove = locateCallCardIndex(callCardId);
        if (indexToRemove >= 0) {
            callCardList.remove(indexToRemove);
            System.out.println("Call card removed: " + callCardId);
        } else {
            System.err.println("Call card ID not found: " + callCardId);
        }
    }

    public int locateCallCardIndex(String callCardId) {
        System.out.println("Searching for call card ID: " + callCardId);
        for (int i = 0; i < callCardList.size(); i++) {
            if (callCardList.get(i).getIdCallCard().equalsIgnoreCase(callCardId)) {
                return i;
            }
        }
        return -1;
    }

    public CallCard searchCallCardById(String callCardId) {
        int index = locateCallCardIndex(callCardId);
        if (index >= 0) {
            System.out.println("Call card found at index: " + index);
            return callCardList.get(index);
        } else {
            System.err.println("No call card found with ID: " + callCardId);
            return null;
        }
    }

    public void debugPrintAllCallCards() {
        System.out.println("Debugging: All current call cards:");
        for (CallCard callCard : callCardList) {
            System.out.println(callCard);
        }
    }
}
