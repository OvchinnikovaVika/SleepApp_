package com.example.sleepapp;

import static java.nio.charset.StandardCharsets.*;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.widget.ArrayAdapter;
import com.example.sleepapp.ui.main.SectionsPagerAdapter;
import com.example.sleepapp.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import com.example.sleepapp.DataModel;


import java.util.Properties;
import java.io.UnsupportedEncodingException;
import java.util.Date;



public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private EditText fieldFile;
    private Button btnLoadFile;
    private Button btnChangeFile;
    private EditText fieldTable;
    private Button btnLoadData;
    private TextView result_info;
    // + Вика  19.03.2022 Добавлены поля даты начала и даты окончания
    private EditText fieldDateStart;
    private EditText fieldDateEnd;
    // - Вика  19.03.2022 Добавлены поля даты начала и даты окончания


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // + Вика  19.03.2022 Добавление комментариев
        // Инициализация элементов
        fieldFile = findViewById(R.id.fieldFile);

        // + Вика  19.03.2022 Инициализация поля даты начала и даты окончания
        fieldDateStart = findViewById(R.id.fieldDateStart);
        fieldDateEnd = findViewById(R.id.fieldDateEnd);
        // - Вика  19.03.2022 Инициализация поля даты начала и даты окончания

        btnLoadFile = findViewById(R.id.btnLoadFile);
        btnChangeFile = findViewById(R.id.btnChangeFile);
        fieldTable = findViewById(R.id.fieldTable);
        btnLoadData = findViewById(R.id.btnLoadData);
        result_info = findViewById(R.id.result_info);
        // Инициализация элементов
        // - Вика  19.03.2022 Добавление комментариев



        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = binding.fab;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        btnLoadFile.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                // + Вика  19.03.2022 Проверка на незаполненные даты
                if(fieldDateStart.getText().toString().trim().equals("")
                    || fieldDateEnd.getText().toString().trim().equals(""))

                        Toast.makeText(MainActivity.this,
                                R.string.mesErrorNoDateStartEnd,
                                Toast.LENGTH_SHORT).show();
                else {
                        // + Вика  19.03.2022 Добавление комментариев
                        // Не заполнена строка с именем файла или датой начала или датой конца
                        if (fieldFile.getText().toString().trim().equals(""))
                            Toast.makeText(MainActivity.this,
                                    R.string.mesErrorNoFileText,
                                    Toast.LENGTH_SHORT).show();
                        // - Вика  19.03.2022 Добавление комментариев
                        // Не заполнена строка с именем файла
                    else {

                        String fileName = fieldFile.getText().toString(); // Получить имя файла
                        String fileTable = fieldTable.getText().toString(); // Получить имя файла Таблицы

                        // + Вика  21.03.2022 Обработка полей начало периода и окончание периода
                        // Выделено в функцию
                        // Надо перевести строки в даты, понять какой нужен формат,
                        // формат - "16-мар.-2022 00:00" в файле, для сравнения 22.02.2022
                        Date dateStart;
                        Date dateEnd;
                        String dateStartStr = "22.02.2022";//fieldDateStart.getText().toString()
                        String dateEndStr = "23.02.2022";//fieldDateEnd.getText().toString()

                        dateStart = getStringToDate(dateStartStr);
                        dateEnd = getStringToDate(dateEndStr);


                        // Сравнение дат
                        String resultCompare = compareDates(dateStart,dateEnd);

                        new GetFileData().execute(fileName); // Считать данные из файла *.csv
                        new LoadDataInTable().execute(fileTable); // Загрузить данные в файл *.xls
                    }
                    // - Вика  19.03.2022 Проверка на незаполненные даты
                }
            }
        });

        //alt+Enter на EditText чтобы импортировать класс
    }

    // + Вика  19.03.2022 Добавление комментариев
    // Считать данные из файла *.csv
    // - Вика  19.03.2022 Добавление комментариев
    private class GetFileData extends AsyncTask<String, String, String> {


        protected void onPreExecute(){
            super.onPreExecute();
            result_info.setText("Ожидайте, идёт считывание данных...");
        }
        @Override
        protected String doInBackground(String... strings) {

            // + Вика  19.03.2022 Добавление комментариев
            // Пример данных
            // "RecordCategory","RecordSubCategory","StartDate","FinishDate","Details"
            // "Сон","","16-мар.-2022 06:42","16-мар.-2022 07:14",""
            // ("Сон", ",", "14-февр.-2022 23:23",",","15-февр.-2022 01:52","\n")
            // "Досуг","Купание","16-мар.-2022 08:50","16-мар.-2022 08:55",""
            // Считать данные из файла *.csv
            // - Вика  19.03.2022 Добавление комментариев
            getDataFromFile(); // Получить данные из файла *.csv

            return "result";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute((result));
            result_info.setText(result); // Изменение текста в панели информации
        }
    }
    private class LoadDataInTable extends AsyncTask<String, String, String> {

        protected void onPreExecute(){
            super.onPreExecute();
            result_info.setText("Ожидайте, идёт загрузка...");
        }
        @Override
        protected String doInBackground(String... strings) {

            loadDataInTableXLS(); // Загрузить данные из файла в таблицу

            return "result";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute((result));
            result_info.setText(result); // Изменение текста в панели информации
        }
    }



    // Получение файла с позицией "сон" из файла "BabyRecords"
    //Ilya 19.03
    public void getDataFromFile() {

        // reader = new BufferedReader(
        //                        new InputStreamReader(getAssets().open("BabyRecords.csv")));
        // getAssets().open("BabyRecords.csv")
        try (BufferedReader reader = new BufferedReader(new FileReader("BabyRecords.csv"));
             BufferedWriter writer = new BufferedWriter(new FileWriter("son.csv"))) {
            String line;
            String son = "Сон"; //son - sleep Vika

            while ((line = reader.readLine()) != null) {

                String flName = line.substring(1, 4);

                // Vika
                // Добавить условие - выделить только строки с нужным периодом
                if (flName.equals(son)) {//son - sleep Vika
                    writer.write(line + "\n");
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Обработать файл "son.csv"
        List<List<String>> listResult = getFileToList("son.csv");
        // 3.2 Отбирать записи с категорией "Сон"
        // и датой окончания больше даты окончания периода пользователя
        //И ( датой начала меньше даты начала периода пользователя
        //или датой начала больше даты начала периода пользователя)
    }

    //функция для получения Листа листов из файла
    //Ilya 20.03
    // 17:37
    public static List<List<String>> getFileToList(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            List<List<String>> listItog = new ArrayList<>(); // listItog на listResult Vika

            String line;
            while ((line = reader.readLine()) != null) {
                listItog.add(Collections.singletonList(line)); // listItog на listResult Vika
            }
            return listItog; // listItog на listResult Vika


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }


    // функция для получения даты из строки
    // + Вика 21.03
    public static Date getStringToDate(String strDate) {

        Date dateResult = new Date();

        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("dd.MM.yyyy");

        try {
            dateResult = format.parse(strDate);
            return dateResult;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }
    // - Вика 21.03

    // функция Сравнение дат
    // + Вика 21.03
    public static String compareDates(Date dateStart, Date dateEnd) {

        // Сравнение дат
        boolean isAfter = dateStart.after(dateEnd); //Проверяет, является ли эта дата после указанной даты.
        // true если и только если момент, представленный этим объектом Date, строго позже момента, представленного dateEnd; false в противном случае.
        boolean isBefore = dateStart.before(dateEnd);
        // true если и только если момент времени, представленный этим объектом Date, строго раньше момента, представленного dateEnd; false в противном случае.

        if (isAfter)
            return "isAfter";
        else if (isBefore)
            return "isBefore";
        else
            return null;

    }
    // - Вика 21.03

    // + Вика 21.03
    // заполнение строки (rowNum) определенного листа (sheet)
    // данными  из dataModel созданного в памяти Excel файла
    private static void createSheetHeader(HSSFSheet sheet, int rowNum, DataModel dataModel) {
        Row row = sheet.createRow(rowNum);

        row.createCell(0).setCellValue(dataModel.getRecordCategory());
        row.createCell(1).setCellValue(dataModel.getRecordSubCategory());
        row.createCell(2).setCellValue(dataModel.getStartDate());
        row.createCell(3).setCellValue(dataModel.getFinishDate());
        row.createCell(4).setCellValue(dataModel.getDetails());
    }

    // заполняем список рандомными данными
    // в реальных приложениях данные будут из БД или интернета
    private static List<DataModel> fillData() {
        List<DataModel> dataModels = new ArrayList<>();
        dataModels.add(new DataModel("Сон","","16-мар.-2022 17:03","16-мар.-2022 17:41",""));
        dataModels.add(new DataModel("Сон","","16-мар.-2022 09:23","16-мар.-2022 10:48",""));
        dataModels.add(new DataModel("Сон","","13-мар.-2022 11:06","13-мар.-2022 11:40",""));

        return dataModels;
    }

    // - Вика 21.03

    public void loadDataInTableXLS() {

            // создание самого excel файла в памяти
            HSSFWorkbook workbook = new HSSFWorkbook();
            // создание листа с названием "Карта сна"
            HSSFSheet sheet = workbook.createSheet("SleepMap");

            // заполняем список какими-то данными
            List<DataModel> dataList = fillData();

            // счетчик для строк
            int rowNum = 0;

            // создаем подписи к столбцам (это будет первая строчка в листе Excel файла)
            Row row = sheet.createRow(rowNum);

            row.createCell(0).setCellValue("RecordCategory");
            row.createCell(1).setCellValue("RecordSubCategory");
            row.createCell(2).setCellValue("StartDate");
            row.createCell(3).setCellValue("EndDate");
            row.createCell(4).setCellValue("Details");

            // createDateXLS(workbook, row, 1,"StartDate"); //для работы с ячейкой дата пригодится

        String filename = "Apache_POI_.xls";
        // Записываем всё в файл

            // заполняем лист данными
            for (DataModel dataModel : dataList) {
                createSheetHeader(sheet, ++rowNum, dataModel);
            }


            //File file = new File(getFilesDir(), filename);
            // записываем созданный в памяти Excel документ в файл
                /*String folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                String name_pic = "picture.png";
                //FileOutputStream output = new FileOutputStream(root+"/downloadedfile.jpg");
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{AndroidManifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);*/
            //try (FileOutputStream out = new FileOutputStream(new File("C:\\Users\\vika\\AndroidStudioProjects\\SleepApp\\app\\src\\main\\assets\\Apache_POI_.xls"))) {
            //writeFileSD()
            final String LOG_TAG = "myLogs";
            final String DIR_SD = "MyFiles";
            final String FILENAME_SD = "fileSD";

            String FILE_PATH = getFilesDir().getAbsolutePath() + "/" + filename;
            File file = new File(FILE_PATH);
            try {


                FileOutputStream stream = new FileOutputStream(file);
                workbook.write(stream);
                stream.close();
            } catch (Exception e) {

            }
            try {
                readFromExcel(FILE_PATH, dataList);
            } catch (IOException e) {
                e.printStackTrace();
            }
            /*try {
                // отрываем поток для записи
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput(filename, MODE_PRIVATE)));
                // пишем данные
                bw.write("Содержимое файла");
                // закрываем поток
                bw.close();
                Log.d(LOG_TAG, "Файл записан");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
    }

    // Создание ячейки - даты в файле xls
    // + Vika 21/03/22
    private static void createDateXLS(HSSFWorkbook workbook, Row row, int CellNum, String nameCell) {

        HSSFCell dateXLS = (HSSFCell) row.createCell(CellNum);
        dateXLS.setCellValue(nameCell);
        HSSFDataFormat format = workbook.createDataFormat();
        HSSFCellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(format.getFormat("dd.mm.yyyy"));

        dateXLS.setCellStyle(dateStyle);
        dateXLS.setCellValue(new Date(110, 10, 10));//подставим значение
    }
    // - Vika 21/03/22

    // Чтение файла xls, вывод в консоль
    // + Vika 21/03/22
    public static void readFromExcel(String file, List<DataModel> dataList) throws IOException{
        HSSFWorkbook myExcelBook = new HSSFWorkbook(new FileInputStream(file));
        HSSFSheet myExcelSheet = myExcelBook.getSheet("SleepMap");

        int rowNum = 0;

        for (DataModel dataModel : dataList) {

            HSSFRow row = myExcelSheet.getRow(rowNum);


            if(row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_STRING){
                String recordCategoryXLS = row.getCell(0).getStringCellValue();
                System.out.println("RecordCategory : " + recordCategoryXLS);
            }

            if(row.getCell(1).getCellType() == HSSFCell.CELL_TYPE_STRING){
                String recordSubCategoryXLS = row.getCell(1).getStringCellValue();
                System.out.println("RecordSubCategory : " + recordSubCategoryXLS);
            }

             // if(row.getCell(1).getCellType() == HSSFCell.CELL_TYPE_NUMERIC){ // когда не строка будет
            if(row.getCell(2).getCellType() == HSSFCell.CELL_TYPE_STRING){
               // Date startDate = row.getCell(1).getDateCellValue();
                String startDateXLS =  row.getCell(2).getStringCellValue();
                System.out.println("startDate :" + startDateXLS);
            }

            if(row.getCell(3).getCellType() == HSSFCell.CELL_TYPE_STRING){
                //Date startDate = row.getCell(1).getDateCellValue();
                String endDateXLS =  row.getCell(3).getStringCellValue();
                System.out.println("endDate :" + endDateXLS);
            }

            if(row.getCell(4).getCellType() == HSSFCell.CELL_TYPE_STRING){
                //Date startDate = row.getCell(1).getDateCellValue();
                String detailXLS =  row.getCell(4).getStringCellValue();
                System.out.println("Details :" + detailXLS);
            }
            rowNum = rowNum + 1;
        }
        //myExcelBook.close();

    }
    // - Vika 21/03/22
}



