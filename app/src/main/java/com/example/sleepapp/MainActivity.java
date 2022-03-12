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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
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

    // Array of strings that's used to display on screen
    String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //инициализация
        fieldFile = findViewById(R.id.fieldFile);
        btnLoadFile = findViewById(R.id.btnLoadFile);
        btnChangeFile = findViewById(R.id.btnChangeFile);
        fieldTable = findViewById(R.id.fieldTable);
        btnLoadData = findViewById(R.id.btnLoadData);
        result_info = findViewById(R.id.result_info);
        //



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
               if(fieldFile.getText().toString().trim().equals(""))
                   Toast.makeText(MainActivity.this,R.string.mesErrorNoFileText,Toast.LENGTH_SHORT).show();
               else {
                   String fileT = fieldFile.getText().toString();
                   String key = "true&sd=true";
                   String nameTable = fieldTable.getText().toString();
                   nameTable = "1MUah4CHdXSDhPtY6tT2xPJdUMK5DBW1H";
                   String url = "https://drive.google.com/file/d/1MUah4CHdXSDhPtY6tT2xPJdUMK5DBW1H/view?usp=sharing";
                           //"https://docs.google.com/spreadsheets/d/" + nameTable + "/edit?usp=sharing&ouid=111352352244330456557&rtpof=" + key;

                   new GetURLData().execute(url);
               }
            }
        });

        //alt+Enter на EditText чтобы импортировать класс
    }

    private class GetURLData extends AsyncTask<String, String, String> {

        protected void onPreExecute(){
            super.onPreExecute();
            result_info.setText("Ожидайте...");
        }
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connectionSite = null;
            BufferedReader reader = null;

            try {
                test();
                // Пример данных

                //Arrays.asList("Сон", ",", "14-февр.-2022 23:23",",","15-февр.-2022 01:52","\n"),

                URL urlConnection = new URL(strings[0]); // Создать ссылку на соединение
                connectionSite = (HttpURLConnection) urlConnection.openConnection();
                connectionSite.connect();

                InputStream stream = connectionSite.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line= reader.readLine()) != null)
                    buffer.append(line).append("/n");

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connectionSite != null)
                    connectionSite.disconnect();

                try {
                    if(reader != null)
                        reader.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute((result));
            //result_info.setText(result);
        }
    }

    public void test(){
        String[] stringArray = {"a","b","c","d","e","f","g","h","t","k","k","k","l","k"};

            BufferedReader reader = null;
            try {
                reader = new BufferedReader(
                        new InputStreamReader(getAssets().open("numbers.csv")));

                // do reading, usually loop until end of file reading
                String mLine;
                while ((mLine = reader.readLine()) != null) {
                    System.out.format("%s ", mLine);
                }
                // создание самого excel файла в памяти
                HSSFWorkbook workbook = new HSSFWorkbook();
                // создание листа с названием "Просто лист"
                HSSFSheet sheet = workbook.createSheet("Просто лист");

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

                final String fromEmail = "gimnastvika@gmail.com"; //requires valid gmail id
                final String password = "ovv166-2016"; // correct password for gmail id
                final String toEmail = "gimnast-96@mail.ru"; // can be any email id

                System.out.println("SSLEmail Start");
                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
                props.put("mail.smtp.socketFactory.port", "465"); //SSL Port
                props.put("mail.smtp.socketFactory.class",
                        "javax.net.ssl.SSLSocketFactory"); //SSL Factory Class
                props.put("mail.smtp.auth", "true"); //Enabling SMTP Authentication
                props.put("mail.smtp.port", "465"); //SMTP Port

                Authenticator auth = new Authenticator() {
                    //override the getPasswordAuthentication method
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromEmail, password);
                    }
                };

                Session session = Session.getDefaultInstance(props, auth);
                System.out.println("Session created");
                sendEmail(session, toEmail,"SSLEmail Testing Subject", "SSLEmail Testing Body");

                sendAttachmentEmail(session, toEmail,"SSLEmail Testing Subject with Attachment", "SSLEmail Testing Body with Attachment");


                    /*// проверяем доступность SD
                    if (!Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {
                        Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
                        return;
                    }
                    // получаем путь к SD
                    File sdPath = Environment.getExternalStorageDirectory();
                    // добавляем свой каталог к пути
                    sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
                    // создаем каталог
                    sdPath.mkdirs();
                    // формируем объект File, который содержит путь к файлу
                    File sdFile = new File(sdPath, FILENAME_SD);
                    try {
                        // открываем поток для записи
                        BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
                        // пишем данные
                        bw.write("Содержимое файла на SD");

                        // закрываем поток
                        bw.close();
                        Log.d(LOG_TAG, "Файл записан на SD: " + sdFile.getAbsolutePath());
                        result_info.setText("Файл записан на SD");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/

                /*try (FileOutputStream out = new FileOutputStream(sdFile)) {

                    workbook.write(out);
                    System.out.println("Excel файл успешно создан!");
                    result_info.setText("Готово...");
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

            }

         catch (IOException e) {
                //log the exception
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        //log the exception
                    }
                }
            }
            //ReadCSVWithScanner.main(stringArray);

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
    public class EmailAuthenticator extends javax.mail.Authenticator
    {
        private String login   ;
        private String password;
        public EmailAuthenticator (final String login, final String password)
        {
            this.login    = login;
            this.password = password;
        }
        public PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication(login, password);
        }
    }
    private MimeBodyPart createFileAttachment(String filepath)
            throws MessagingException
    {
        // Создание MimeBodyPart
        MimeBodyPart mbp = new MimeBodyPart();

        // Определение файла в качестве контента
        FileDataSource fds = new FileDataSource(filepath);
        mbp.setDataHandler(new DataHandler(fds));
        mbp.setFileName(fds.getName());
        return mbp;
    }

    public static void sendEmail(Session session, String toEmail, String subject, String body){
        try
        {
            MimeMessage msg = new MimeMessage(session);
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("no_reply@example.com", "NoReply-JD"));

            msg.setReplyTo(InternetAddress.parse("no_reply@example.com", false));

            msg.setSubject(subject, "UTF-8");

            msg.setText(body, "UTF-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            System.out.println("Message is ready");
            Transport.send(msg);

            System.out.println("EMail Sent Successfully!!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void sendAttachmentEmail(Session session, String toEmail, String subject, String body){
        try{
            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("no_reply@example.com", "NoReply-JD"));

            msg.setReplyTo(InternetAddress.parse("gimnast-96@mail.ru", false));

            msg.setSubject(subject, "UTF-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

            // Create the message body part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message
            messageBodyPart.setText(body);

            // Create a multipart message for attachment
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Second part is attachment
            messageBodyPart = new MimeBodyPart();
            String filename = "abc.txt";
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            msg.setContent(multipart);

            // Send message
            Transport.send(msg);
            System.out.println("EMail Sent Successfully with attachment!!");
        }catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}



