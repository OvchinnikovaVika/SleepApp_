package com.example.sleepapp;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadCSVWithScanner {

    public static void main(String[] args) throws IOException, CsvValidationException {

        String fileName = "numbers.csv";


        //"c:/path/a.txt"
        /*try (InputStreamReader fr = new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8);
             CSVReader reader = new CSVReader(fr)) {

            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {


            }
        }*/
    }

}