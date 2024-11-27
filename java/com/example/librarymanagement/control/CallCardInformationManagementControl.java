package com.example.library.control;

import com.example.library.filehandle.FileCallCardHandler;
import com.example.library.model.CallCardInfor;

import java.util.ArrayList;
import java.util.List;

public class CallCardInfoManager {
    private static final List<CallCardInfor> callCardInfos = new ArrayList<>();

    public CallCardInfoManager() {
        System.out.println("Initializing CallCardInfoManager...");
        FileCallCardHandler.loadFile(callCardInfos,
                CallCardManager.getAllCallCards(),
                BookManager.retrieveBooks());
    }

    public static List<CallCardInfor> getAllCallCardInfos() {
        return callCardInfos;
    }

    public void addCallCardInfo(CallCardInfor newCallCardInfo) {
        if (newCallCardInfo != null) {
            callCardInfos.add(newCallCardInfo);
            System.out.println("Call card information added.");
        } else {
            System.err.println("Failed to add: Call card information is null.");
        }
    }

    public void removeCallCardInfoById(String id) {
        List<CallCardInfor> matchingInfos = searchCallCardInfoById(id);
        if (!matchingInfos.isEmpty()) {
            callCardInfos.removeAll(matchingInfos);
            System.out.println("Removed all matching call card info with ID: " + id);
        } else {
            System.err.println("No matching call card info found for ID: " + id);
        }
    }

    public void removeCallCardInfo(String callCardId, String bookId) {
        CallCardInfor toRemove = null;
        for (CallCardInfor info : callCardInfos) {
            if (info.getCallCard().getIdCallCard().equalsIgnoreCase(callCardId) &&
                    info.getBook().getIdBook().equalsIgnoreCase(bookId)) {
                toRemove = info;
                break;
            }
        }
        if (toRemove != null) {
            callCardInfos.remove(toRemove);
            System.out.println("Successfully removed call card info with CallCard ID: "
                    + callCardId + " and Book ID: " + bookId);
        } else {
            System.err.println("No matching call card info found for CallCard ID: " + callCardId
                    + " and Book ID: " + bookId);
        }
    }

    public List<CallCardInfor> searchCallCardInfoById(String searchId) {
        List<CallCardInfor> result = new ArrayList<>();
        for (CallCardInfor info : callCardInfos) {
            if (info.getCallCard().getIdCallCard().equalsIgnoreCase(searchId)) {
                result.add(info);
            }
        }
        return result;
    }

    public void debugPrintCallCardInfo() {
        System.out.println("Debugging: Current call card info list:");
        for (CallCardInfor info : callCardInfos) {
            System.out.println(info);
        }
    }
}
