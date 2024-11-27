package com.example.library.control;

import com.example.library.filehandle.FileStaffHandler;
import com.example.library.model.Staff;

import java.util.ArrayList;
import java.util.List;

public class StaffManager implements IManagement<Staff> {
    private static final List<Staff> staffList = new ArrayList<>();
    private Staff activeStaff;

    public static List<Staff> getAllStaff() {
        return staffList;
    }

    public Staff getActiveStaff() {
        return activeStaff;
    }

    public void setActiveStaff(Staff activeStaff) {
        this.activeStaff = activeStaff;
    }

    public StaffManager() {
        System.out.println("Initializing StaffManager...");
        FileStaffHandler.loadStaffData(staffList);
    }

    @Override
    public void add(Staff newStaff) {
        if (newStaff != null) {
            staffList.add(newStaff);
            FileStaffHandler.saveStaffData(staffList);
            System.out.println("Staff added: " + newStaff.getIdStaff());
        } else {
            System.err.println("Failed to add staff: Input is null.");
        }
    }

    @Override
    public void update(String id, Staff updatedStaff) {
        int index = locateStaffIndexById(id);
        if (index >= 0) {
            staffList.set(index, updatedStaff);
            FileStaffHandler.saveStaffData(staffList);
            System.out.println("Staff updated: " + id);
        } else {
            System.err.println("Failed to update staff: ID not found.");
        }
    }

    @Override
    public void delete(String id) {
        int index = locateStaffIndexById(id);
        if (index >= 0) {
            staffList.remove(index);
            FileStaffHandler.saveStaffData(staffList);
            System.out.println("Staff deleted: " + id);
        } else {
            System.err.println("Failed to delete staff: ID not found.");
        }
    }

    @Override
    public int findIndexById(String id) {
        return locateStaffIndexById(id);
    }

    private int locateStaffIndexById(String id) {
        try {
            int staffId = Integer.parseInt(id);
            for (int i = 0; i < staffList.size(); i++) {
                if (staffList.get(i).getIdStaff() == staffId) {
                    return i;
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format: " + id);
        }
        return -1;
    }

    public List<Staff> searchStaffByIdOrName(String keyword) {
        List<Staff> foundStaff = new ArrayList<>();
        for (Staff staff : staffList) {
            if (String.valueOf(staff.getIdStaff()).contains(keyword) ||
                    staff.getNameStaff().toLowerCase().contains(keyword.toLowerCase())) {
                foundStaff.add(staff);
            }
        }
        return foundStaff;
    }

    public boolean authenticate(String username, String password) {
        for (Staff staff : staffList) {
            if (staff.getUsername().equals(username) &&
                    staff.getPassword().equals(password)) {
                setActiveStaff(staff);
                System.out.println("Authentication successful for user: " + username);
                return true;
            }
        }
        System.err.println("Authentication failed for user: " + username);
        return false;
    }

    public void printAllStaffDetails() {
        System.out.println("Debug: Printing all staff details...");
        for (Staff staff : staffList) {
            System.out.println(staff);
        }
    }
}
