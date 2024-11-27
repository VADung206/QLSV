package com.example.library.control;

import com.example.library.filehandle.FileReaderHandler;
import com.example.library.model.CallCardInfor;
import com.example.library.model.Reader;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReaderManager implements IManagement<Reader> {
    private static final List<Reader> readerList = new ArrayList<>();

    public ReaderManager() {
        System.out.println("Initializing ReaderManager...");
        FileReaderHandler.loadReaders(readerList);
        applyAutoLock();
    }

    public static List<Reader> getAllReaders() {
        return readerList;
    }

    @Override
    public void add(Reader newReader) {
        if (newReader != null) {
            readerList.add(newReader);
            FileReaderHandler.saveReaders(readerList);
            System.out.println("Reader added successfully: " + newReader.getIdReader());
        } else {
            System.err.println("Failed to add reader: Input is null.");
        }
    }

    @Override
    public void update(String readerId, Reader updatedReader) {
        int index = locateReaderIndex(readerId);
        if (index >= 0) {
            readerList.set(index, updatedReader);
            FileReaderHandler.saveReaders(readerList);
            System.out.println("Reader updated: " + readerId);
        } else {
            System.err.println("Failed to update: Reader ID not found.");
        }
    }

    @Override
    public void delete(String readerId) {
        int index = locateReaderIndex(readerId);
        if (index >= 0) {
            readerList.remove(index);
            FileReaderHandler.saveReaders(readerList);
            System.out.println("Reader deleted: " + readerId);
        } else {
            System.err.println("Failed to delete: Reader ID not found.");
        }
    }

    @Override
    public int findIndexById(String readerId) {
        return locateReaderIndex(readerId);
    }

    private int locateReaderIndex(String readerId) {
        System.out.println("Locating reader with ID: " + readerId);
        for (int i = 0; i < readerList.size(); i++) {
            if (readerList.get(i).getIdReader().equalsIgnoreCase(readerId)) {
                return i;
            }
        }
        return -1;
    }

    public List<Reader> searchReaderByIdOrName(String keyword) {
        List<Reader> foundReaders = new ArrayList<>();
        for (Reader reader : readerList) {
            if (reader.getIdReader().contains(keyword) ||
                    reader.getNameReader().toLowerCase().contains(keyword.toLowerCase())) {
                foundReaders.add(reader);
            }
        }
        return foundReaders;
    }

    public static void applyAutoLock() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        System.out.println("Applying auto-lock for expired readers...");
        for (Reader reader : readerList) {
            if (!reader.getExpiry().isAfter(currentDateTime)) {
                reader.setLock(true);
                System.out.println("Reader locked: " + reader.getIdReader());
            }
        }
        FileReaderHandler.saveReaders(readerList);
    }

    public void extendReaderExpiry(String readerId, Reader updatedReader) {
        int index = locateReaderIndex(readerId);
        if (index >= 0) {
            readerList.get(index).setExpiry(updatedReader.getExpiry());
            readerList.get(index).setLock(false);
            FileReaderHandler.saveReaders(readerList);
            System.out.println("Reader expiry extended: " + readerId);
        } else {
            System.err.println("Failed to extend expiry: Reader ID not found.");
        }
    }

    public List<String> extractReaderIds() {
        List<String> idList = new ArrayList<>();
        for (Reader reader : readerList) {
            idList.add(reader.getIdReader());
        }
        return idList;
    }

    // Thêm hàm không cần thiết để tạo nhiễu
    public void debugPrintAllReaders() {
        System.out.println("Debug: Current reader list:");
        for (Reader reader : readerList) {
            System.out.println(reader);
        }
    }
}
