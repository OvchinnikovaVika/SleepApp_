package com.example.sleepapp;

import static java.nio.charset.StandardCharsets.*;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.sleepapp.CSVFile.*;


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

    private ImageView ivOpenedFile;
    private static final int PERMISSION_STORAGE = 101;
    private TextView tvPermission; // тестовое окно со статусом







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

        tvPermission = findViewById(R.id.tvPermission);


        //Ilya 27.03  - ниже код для проверки разрешения для использования памяти
        if (PermissionUtils.hasPermissions(this)) {
            tvPermission.setText("Разрешение получено");




        } else {
            tvPermission.setText("Разрешение не предоставлено");
            btnChangeFile.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (PermissionUtils.hasPermissions(MainActivity.this))
                        return;
                    PermissionUtils.requestPermissions(MainActivity.this, PERMISSION_STORAGE);
                }
            });
        }




            SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

            //Октрыть файл
            OpenFileDialog fileDialog = new OpenFileDialog(this)
                    //.setFilter(".*\\.csv")
                    .setOpenDialogListener(new OpenFileDialog.OpenDialogListener() {
                        @Override
                        public void OnSelectedFile(String fileName) {
                            Toast.makeText(getApplicationContext(), fileName, Toast.LENGTH_LONG).show();
                            result_info.setText(fileName);
                        }
                    });
            fileDialog.show();


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


                            new GetFileData().execute(fileName); // Считать данные из файла *.csv
                            new LoadDataInTable().execute(fileTable); // Загрузить данные в файл *.xls
                        }
                        // - Вика  19.03.2022 Проверка на незаполненные даты
                    }
                }
            });







        // Инициализация элементов
        // - Вика  19.03.2022 Добавление комментариев



//        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
//
//        //Октрыть файл
//        OpenFileDialog fileDialog = new OpenFileDialog(this)
//                //.setFilter(".*\\.csv")
//                .setOpenDialogListener(new OpenFileDialog.OpenDialogListener() {
//                    @Override
//                    public void OnSelectedFile(String fileName) {
//                        Toast.makeText(getApplicationContext(), fileName, Toast.LENGTH_LONG).show();
//                        result_info.setText(fileName);
//                    }
//                });
//        fileDialog.show();
//
//
//        btnLoadFile.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void onClick(View view) {
//
//
//
//                // + Вика  19.03.2022 Проверка на незаполненные даты
//                if(fieldDateStart.getText().toString().trim().equals("")
//                    || fieldDateEnd.getText().toString().trim().equals(""))
//
//                        Toast.makeText(MainActivity.this,
//                                R.string.mesErrorNoDateStartEnd,
//                                Toast.LENGTH_SHORT).show();
//                else {
//                        // + Вика  19.03.2022 Добавление комментариев
//                        // Не заполнена строка с именем файла или датой начала или датой конца
//                        if (fieldFile.getText().toString().trim().equals(""))
//                            Toast.makeText(MainActivity.this,
//                                    R.string.mesErrorNoFileText,
//                                    Toast.LENGTH_SHORT).show();
//                        // - Вика  19.03.2022 Добавление комментариев
//                        // Не заполнена строка с именем файла
//                    else {
//
//
//
//                        String fileName = fieldFile.getText().toString(); // Получить имя файла
//
//
//
//
//                        String fileTable = fieldTable.getText().toString(); // Получить имя файла Таблицы
//
//                        // + Вика  21.03.2022 Обработка полей начало периода и окончание периода
//                        // Выделено в функцию
//                        // Надо перевести строки в даты, понять какой нужен формат,
//                        // формат - "16-мар.-2022 00:00" в файле, для сравнения 22.02.2022
//
//
//                        new GetFileData().execute(fileName); // Считать данные из файла *.csv
//                        new LoadDataInTable().execute(fileTable); // Загрузить данные в файл *.xls
//                    }
//                    // - Вика  19.03.2022 Проверка на незаполненные даты
//                }
//            }
//        });

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

            //loadDataInTableXLS(); // Загрузить данные из файла в таблицу

            return "result";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute((result));
            result_info.setText(result); // Изменение текста в панели информации
        }
    }

    // Ilya 21.03
    // Получение данных из файла пользователя BabyRecords.csv
    // Формируется список строк listSleepResult
    public void getDataFromFile() {

        //OpenFileDialog failureDialog = OpenDialogListener;

        List<List<String>> listSleepResult = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getAssets().open("BabyRecords.csv")))) {

            String line;
            String sleep = "Сон"; //son - sleep Vika

            while ((line = reader.readLine()) != null) {

                String flName = line.substring(1, 4);

                if (flName.equals(sleep)) //son - sleep Vika
                    listSleepResult.add(Collections.singletonList(line));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Получить даты от пользователя, которые он ввёл
        String dateStart = fieldDateStart.getText().toString();
        String dateEnd = fieldDateEnd.getText().toString();

        // + Vika 23.03.22
        // Появился новый класс CSVFile
        CSVFile workWithCSV = new CSVFile();
        List<DataModel> dataModels =
                workWithCSV.getDataModelFromListSleepResult(listSleepResult, dateStart, dateEnd);
        // - Vika 23.03.22

        // + Vika  25.03.22
        // Выгрузка в таблицу xls данных
        loadDataInTableXLS(dataModels);
        // - Vika  25.03.22
    }

    //функция для получения Листа листов из файла
    //Ilya 20.03
    //
    // Ilya 21.03 - функция немного изменена, функционал тот же
    public List<List<String>> getFileToList(String fileName) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(fileName)))) {

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





    // + Вика 21.03
    // заполнение строки (rowNum) определенного листа (sheet)
    // данными  из dataModel созданного в памяти Excel файла
    private static void createSheetHeader(HSSFWorkbook workbook, HSSFSheet sheet, int rowNum, DataModel dataModel) {
        Row row = sheet.createRow(rowNum);

        row.createCell(0).setCellValue(dataModel.getRecordCategory());
        row.createCell(1).setCellValue(dataModel.getRecordSubCategory());
        //row.createCell(2).setCellValue(dataModel.getStartDate());
        //row.createCell(3).setCellValue(dataModel.getFinishDate());
        Date sDate = dataModel.getFinishDate();
        createDateXLS(workbook, row, 2, sDate);
        Date fDate = dataModel.getFinishDate();
        createDateXLS(workbook, row, 3, fDate);
        row.createCell(4).setCellValue(dataModel.getDetails());
    }

    // - Вика 21.03

    public void loadDataInTableXLS(List<DataModel> dataList) {

        // создание самого excel файла в памяти
        HSSFWorkbook workbook = new HSSFWorkbook();
        // создание листа с названием "Карта сна"
        HSSFSheet sheet = workbook.createSheet("SleepMap");

        // счетчик для строк
        int rowNum = 0;

        // создаем подписи к столбцам (это будет первая строчка в листе Excel файла)
        //Row row = sheet.createRow(rowNum);

        String filename = "Apache_POI_.xls";
        // Записываем всё в файл

        // заполняем лист данными
        for (DataModel dataModel : dataList) {
            createSheetHeader(workbook, sheet, rowNum++, dataModel);
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
            /*try {
                readFromExcel(FILE_PATH, dataList);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
    }

    // Создание ячейки - даты в файле xls
    // + Vika 21/03/22
    private static void createDateXLS(HSSFWorkbook workbook, Row row, int CellNum, Date dateInCell) {

        HSSFCell dateXLS = (HSSFCell) row.createCell(CellNum);

        HSSFDataFormat format = workbook.createDataFormat();
        HSSFCellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(format.getFormat("m/d/yy h:mm"));

        dateXLS.setCellValue(dateInCell);//подставим значение
        dateXLS.setCellStyle(dateStyle);
    }
    // - Vika 21/03/22

    // Чтение файла xls, вывод в консоль
    // + Vika 21/03/22
    public static void readFromExcel(String file, List<DataModel> dataList) throws IOException{
        HSSFWorkbook myExcelBook = new HSSFWorkbook(new FileInputStream(file));
        HSSFSheet myExcelSheet = myExcelBook.getSheet("SleepMap");

        int rowNum = 0;

        //dataList.size(), надо проходить на один больше, так как есть заголовок
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

            if(row.getCell(3).getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
                Date endDateXLS = row.getCell(3).getDateCellValue();
                //String endDateXLS =  row.getCell(3).getStringCellValue();
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


    //Ilya 27.03 методы проверки результата (для разных API)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PERMISSION_STORAGE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (PermissionUtils.hasPermissions(this)) {
                    tvPermission.setText("Разрешение получено");
                } else {
                    tvPermission.setText("Разрешение не предоставлено");
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tvPermission.setText("Разрешение получено");
            } else {
                tvPermission.setText("Разрешение не предоставлено");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}



