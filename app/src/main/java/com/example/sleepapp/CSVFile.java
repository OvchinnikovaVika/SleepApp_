package com.example.sleepapp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.xml.datatype.Duration;

public class CSVFile {

    public CSVFile() {
    }


    // Ilya 25.03
    // Функция добавляет к дате пользователя 1 минуту
    public Date getUserDate(String userDate) {

        int userStartDayOfMonth = Integer.parseInt(userDate.substring(0,2)); // Получаем день
        int userStartMonth = Integer.parseInt(userDate.substring(3,5)); // Получаем месяц
        int userStartYear = Integer.parseInt(userDate.substring(6,10)); // Получаем год


        Calendar calendarUserStartDate = new GregorianCalendar(userStartYear, userStartMonth-1, userStartDayOfMonth); // В месяце параметр -1, т.к в календаре месяцы нумеруются с нуля

        calendarUserStartDate.set(Calendar.MINUTE, 1);  // добавляем минуту


        String userDateTimeMillSec = String.valueOf(calendarUserStartDate.getTimeInMillis()); // получаем дату в миллисекундах

        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        long milliSeconds= Long.parseLong(userDateTimeMillSec);


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);

        String str = formatter.format(calendar.getTime()); // Получаем строку типа : dd.MM.yyyy HH:mm

        Date dateUser = getDateFromString(str, "dd.MM.yyyy HH:mm"); // Получаем дату типа : dd.MM.yyyy HH:mm

        return dateUser;
    }

    public List<DataModel> getDataModelFromListSleepResult(List<List<String>> listSleepResult, String userStart, String userEnd) {

        // + Vika 22.03.22
        // Перевести в dataModels, разбить на подстроки

        List<DataModel> dataModels = new ArrayList<>();

        String formatOfDate = "dd.MM.yyyy HH:mm";

        for (List<String> row : listSleepResult)
        {

            Date dateUserStart1 = getUserDate(userStart); // получаем дату, которая увеличина на минуту (Ilya 25.03)
            Date dateUserEnd1 = getUserDate(userEnd);



//            Date dateUserStart1 = getDateFromString(userStart,"dd.MM.yyyy");
//
//            Date dateUserEnd1 = getDateFromString(userEnd,"dd.MM.yyyy");




            // Надо прибавить 1 секунду или 1 час к датам сверху, чтобы было
            // 14.03.2022 01:00:00 или 14.03.2022 00:01:00
            //Date newDate = new Date(dateUserEnd1.getTime() + TimeUnit.HOURS.toMillis(2)); // Add 2 hours

            //Instant instant = dateUserEnd1.toInstant();

            //Duration duration = Duration.ofHours( 8 );
            //String instantHourLater = dateUserEnd1.toInstant().plus( Duration.ofHours( 1 ) ).toString();

            //Date newDate = new Date(dateUserEnd1.getTime() + 2 * 3600*1000);

           /* String myString =  "09:00 12/12/2014";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            Date myDateTime = null;

            //Parse your string to SimpleDateFormat
            try
            {
                myDateTime = simpleDateFormat.parse(myString);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
            System.out.println("This is the Actual Date:"+myDateTime);
            Calendar cal = new GregorianCalendar();
            cal.setTime(myDateTime);

            //Adding 21 Hours to your Date
            cal.add(Calendar.HOUR_OF_DAY, 21);
            System.out.println("This is Hours Added Date:"+cal.getTime());*/

            Date dateUserStart = getDateFromString("14.03.2022 00:01",formatOfDate);

            Date dateUserEnd = getDateFromString("16.03.2022 00:01",formatOfDate);

            String[] strarray = row.toArray(new String[0]);

            formatOfDate = "d MMM  yyyy HH:mm";

            String startDateStr = strarray[0].split("\".+?\"")[2]; // убрать тире и точки
            String finishDateStr = strarray[0].split("\".+?\"")[3]; // убрать тире и точки

            Date startDate = getDateFromString((startDateStr.replace("-"," ")).replaceAll("\\."," "),formatOfDate);
            Date endDate = getDateFromString((finishDateStr.replace("-"," ")).replaceAll("\\."," "),formatOfDate);

            Long sleepDuration = (endDate.getTime() - startDate.getTime())/1000/60;//38 минут сна - продолжительность сна в минутах
            //Long durationUserEnd = (dateUserEnd.getTime() - dateEnd)/1000/60; //разница в минутах между пользовательским концом периода
            // и концом из файла

            //Long durationUserStart = (dateStart - dateUserStart.getTime())/1000/60; //разница в минутах между пользовательским началом периода

            if (sleepDuration > 300 &&
                    (dateUserStart.before(endDate) && endDate.before(dateUserEnd) ||
                    startDate.before(dateUserEnd) && dateUserStart.before(startDate))
                    || endDate.before(dateUserEnd) &&
                    dateUserStart.before(endDate) &&
                    dateUserStart.before(startDate) &&
                    startDate.before(dateUserEnd)
                    && sleepDuration < 300) { //добавить элемент

                DataModel elementDataModel = new DataModel();

                elementDataModel.setRecordCategory(strarray[0].substring(1, 4));
                elementDataModel.setRecordSubCategory(strarray[0].split("\".+?\"")[1]);
                elementDataModel.setStartDate(startDate);
                elementDataModel.setFinishDate(endDate);
                elementDataModel.setDetails(strarray[0].split("\".+?\"")[4]);

                dataModels.add(elementDataModel);
            }
        }
        return dataModels;
    }

    // Функция для получения даты из строки
    // + Вика 21.03
    public static Date getDateFromString(String strDate, String format) {

        Date dateResult = new Date();

        try {
            dateResult = new SimpleDateFormat(format,
                    Locale.getDefault()).parse(strDate);
            return dateResult;
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return null;

    }
    // - Вика 21.03


}