package com.example.sleepapp;

import static java.nio.charset.StandardCharsets.*;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

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
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;

import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import com.example.sleepapp.DataModel;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.activation.FileDataSource;
import javax.mail.Multipart;
import javax.mail.internet.MimeMultipart;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

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

                        // + Вика  19.03.2022 Обработка полей начало периода и окончание периода
                            // Может лучше выделить в функцию
                        // Надо перевести строки в даты, понять какой нужен формат,
                        // формат - "16-мар.-2022 00:00"

                        // Получить начало периода

                        Date dateStart;
                        Date dateEnd;
                            try {
                                dateStart = new SimpleDateFormat("dd-MMM.-yyyy HH:mm",
                                        Locale.getDefault()).parse(fieldDateStart.getText().toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            // Получить конец периода
                            try {
                                dateEnd = new SimpleDateFormat("dd-MMM.-yyyy HH:mm",
                                        Locale.getDefault()).parse(fieldDateEnd.getText().toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            // - Вика  19.03.2022 Обработка полей начало периода и окончание периода

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

    public void getDataFromFile(){

            BufferedReader reader = null;
            try {
                reader = new BufferedReader(
                        new InputStreamReader(getAssets().open("numbers.csv")));

                // выполняется чтение, цикл до последней строки файла
                String mLine;
                while ((mLine = reader.readLine()) != null) {
                    System.out.format("%s ", mLine);
                }
                // файл только с"Сон"
                // файл записываться будет во временное приложения
                // файл только с "Сон" и датами начала и конца
                //

                // Не реализовано
                // Считать данные из файла в какой-то массив
            }
             catch (IOException e) {
                    // обработка исключения
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            // обработка исключения
                        }
                    }
                }

    }
    // заполнение строки (rowNum) определенного листа (sheet)
    // данными  из dataModel созданного в памяти Excel файла
    private static void createSheetHeader(HSSFSheet sheet, int rowNum, DataModel dataModel) {
        Row row = sheet.createRow(rowNum);

        row.createCell(0).setCellValue(dataModel.getName());
        row.createCell(1).setCellValue(dataModel.getSurname());
        row.createCell(2).setCellValue(dataModel.getCity());
        row.createCell(3).setCellValue(dataModel.getSalary());
    }

    // заполняем список рандомными данными
    // в реальных приложениях данные будут из БД или интернета
    private static List<DataModel> fillData() {
        List<DataModel> dataModels = new ArrayList<>();
        dataModels.add(new DataModel("Howard", "Wolowitz", "Massachusetts", 90000.0));
        dataModels.add(new DataModel("Leonard", "Hofstadter", "Massachusetts", 95000.0));
        dataModels.add(new DataModel("Sheldon", "Cooper", "Massachusetts", 120000.0));

        return dataModels;
    }

    public void loadDataInTableXLS() {


            // создание самого excel файла в памяти
            HSSFWorkbook workbook = new HSSFWorkbook();
            // создание листа с названием "Карта сна"
            HSSFSheet sheet = workbook.createSheet("Карта сна");

            // заполняем список какими-то данными
            List<DataModel> dataList = fillData();

            // счетчик для строк
            int rowNum = 0;

            // создаем подписи к столбцам (это будет первая строчка в листе Excel файла)
            Row row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue("Имя");
            row.createCell(1).setCellValue("Фамилия");
            row.createCell(2).setCellValue("Город");
            row.createCell(3).setCellValue("Зарплата");

            // заполняем лист данными
            for (DataModel dataModel : dataList) {
                createSheetHeader(sheet, ++rowNum, dataModel);
            }
            String filename = "Apache_POI_.xls";

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

            String FILE_PATH = getFilesDir().getAbsolutePath();
            try {

                File file = new File(FILE_PATH);
                FileOutputStream stream = new FileOutputStream(file);
                workbook.write(stream);
                stream.close();
            } catch (Exception e) {

            }
            try {
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
            }
    }

}



