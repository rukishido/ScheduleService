package com.example.scheduleservice;

import java.util.ArrayList;
import java.util.List;

public class ScheduleItemData {
    String categoryName;
    String fileName;
    String fileLink;

    public ScheduleItemData(String categoryName, String fileName, String fileLink) {
        this.categoryName = categoryName;
        this.fileName = fileName;
        this.fileLink = fileLink;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileLinks() {
        return fileLink;
    }
}
