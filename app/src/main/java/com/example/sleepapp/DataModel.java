package com.example.sleepapp;

import java.util.Date;

public class DataModel {

    private String recordCategory;
    private String recordSubCategory;
    private Date startDate; // Date
    private Date finishDate; // Date
    private String details;
    // private Double salary;

    public DataModel() {
    }

    public DataModel(String recordCategory, String recordSubCategory, Date startDate, Date finishDate, String details) {
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
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