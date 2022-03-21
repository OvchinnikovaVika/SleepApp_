package com.example.sleepapp;

public class DataModel {

    private String recordCategory;
    private String recordSubCategory;
    private String startDate; // Date
    private String finishDate; // Date
    private String details;
    // private Double salary;

    public DataModel() {
    }

    public DataModel(String recordCategory, String recordSubCategory, String startDate, String finishDate, String details) {
        this.recordCategory = recordCategory;
        this.recordSubCategory = recordSubCategory;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.details = details;
    }

    public String getRecordCategory() {
        return recordCategory;
    }

    public void setRecordCategory(String recordCategory) {
        this.recordCategory = recordCategory;
    }

    public String getRecordSubCategory() {
        return recordSubCategory;
    }

    public void setRecordSubCategory(String recordSubCategory) {
        this.recordSubCategory = recordSubCategory;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    /*public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }*/
}