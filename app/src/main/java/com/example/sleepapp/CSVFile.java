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

    public List<DataModel> getDataModelFromListSleepResult(List<List<String>> listSleepResult, List<List<String>> listFeedingResult,
                                                           List<List<String>> listFoodResult, List<List<String>> listLeisureResult, String userStart, String userEnd) {

        // + Vika 22.03.22
        // Перевести в dataModels, разбить на подстроки

        List<DataModel> dataModels = new ArrayList<>();

        String formatOfDate = "dd.MM.yyyy HH:mm";

        for (List<String> row : listSleepResult)
        { // сон

            Date dateUserStart = getUserDate(userStart); // получаем дату, которая увеличина на минуту (Ilya 25.03)
            Date dateUserEnd = getUserDate(userEnd);

            // Date dateUserStart = getDateFromString("14.03.2022 00:01",formatOfDate);
            // Date dateUserEnd = getDateFromString("16.03.2022 00:01",formatOfDate);

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

        for (List<String> row : listFeedingResult)
        { // кормления

            Date dateUserStart = getUserDate(userStart); // получаем дату, которая увеличина на минуту (Ilya 25.03)
            Date dateUserEnd = getUserDate(userEnd);

            // Date dateUserStart = getDateFromString("14.03.2022 00:01",formatOfDate);
            // Date dateUserEnd = getDateFromString("16.03.2022 00:01",formatOfDate);

            String[] strarray = row.toArray(new String[0]);

            formatOfDate = "d MMM  yyyy HH:mm";

            String startDateStr = strarray[0].split(",")[2]; // убрать тире и точки
            String finishDateStr = strarray[0].split(",")[3]; // убрать тире и точки

            String stDate = (startDateStr.replace("-"," ")).replaceAll("\\."," ");
            String eDate = (finishDateStr.replace("-"," ")).replaceAll("\\."," ");

            Date startDate = getDateFromString(stDate.substring(1,19),formatOfDate);
            Date endDate = getDateFromString(eDate.substring(1,19),formatOfDate);
//проверка на null startDate and endDate
            //Long durationUserStart = (dateStart - dateUserStart.getTime())/1000/60; //разница в минутах между пользовательским началом периода

            if ((dateUserStart.before(endDate) && endDate.before(dateUserEnd) ||
                            startDate.before(dateUserEnd) && dateUserStart.before(startDate))
                    || endDate.before(dateUserEnd) &&
                    dateUserStart.before(endDate) &&
                    dateUserStart.before(startDate) &&
                    startDate.before(dateUserEnd)) { //добавить элемент

                DataModel elementDataModel = new DataModel();

                elementDataModel.setRecordCategory(strarray[0].substring(1, 10));
                elementDataModel.setRecordSubCategory(strarray[0].split(",")[1]);
                elementDataModel.setStartDate(startDate);
                elementDataModel.setFinishDate(endDate);
                elementDataModel.setDetails(strarray[0].split(",")[4]);

                dataModels.add(elementDataModel);
            }
        }

        for (List<String> row : listFoodResult)
        { // еда

            Date dateUserStart = getUserDate(userStart); // получаем дату, которая увеличина на минуту (Ilya 25.03)
            Date dateUserEnd = getUserDate(userEnd);

            // Date dateUserStart = getDateFromString("14.03.2022 00:01",formatOfDate);
            // Date dateUserEnd = getDateFromString("16.03.2022 00:01",formatOfDate);

            String[] strarray = row.toArray(new String[0]);

            formatOfDate = "d MMM  yyyy HH:mm";

            String startDateStr = strarray[0].split(",")[2]; // убрать тире и точки
            String finishDateStr = strarray[0].split(",")[3]; // убрать тире и точки

            String stDate = (startDateStr.replace("-"," ")).replaceAll("\\."," ");
            String eDate = (finishDateStr.replace("-"," ")).replaceAll("\\."," ");

            Date startDate = getDateFromString(stDate.substring(1,19),formatOfDate);
            Date endDate = getDateFromString(eDate.substring(1,19),formatOfDate);

            //Long durationUserStart = (dateStart - dateUserStart.getTime())/1000/60; //разница в минутах между пользовательским началом периода

            if ((dateUserStart.before(endDate) && endDate.before(dateUserEnd) ||
                    startDate.before(dateUserEnd) && dateUserStart.before(startDate))
                    || endDate.before(dateUserEnd) &&
                    dateUserStart.before(endDate) &&
                    dateUserStart.before(startDate) &&
                    startDate.before(dateUserEnd)) { //добавить элемент

                DataModel elementDataModel = new DataModel();

                elementDataModel.setRecordCategory(strarray[0].substring(1, 10));
                elementDataModel.setRecordSubCategory(strarray[0].split(",")[1]);
                elementDataModel.setStartDate(startDate);
                elementDataModel.setFinishDate(endDate);
                elementDataModel.setDetails(strarray[0].split(",")[4]);

                dataModels.add(elementDataModel);
            }
        }

        for (List<String> row : listLeisureResult)
        { // досуг

            Date dateUserStart = getUserDate(userStart); // получаем дату, которая увеличина на минуту (Ilya 25.03)
            Date dateUserEnd = getUserDate(userEnd);

            // Date dateUserStart = getDateFromString("14.03.2022 00:01",formatOfDate);
            // Date dateUserEnd = getDateFromString("16.03.2022 00:01",formatOfDate);

            String[] strarray = row.toArray(new String[0]);

            formatOfDate = "d MMM  yyyy HH:mm";

            String startDateStr = strarray[0].split(",")[2]; // убрать тире и точки
            String finishDateStr = strarray[0].split(",")[3]; // убрать тире и точки

            String stDate = (startDateStr.replace("-"," ")).replaceAll("\\."," ");
            String eDate = (finishDateStr.replace("-"," ")).replaceAll("\\."," ");

            Date startDate = getDateFromString(stDate.substring(1,19),formatOfDate);
            Date endDate = getDateFromString(eDate.substring(1,19),formatOfDate);
            //Long durationUserStart = (dateStart - dateUserStart.getTime())/1000/60; //разница в минутах между пользовательским началом периода

            if ((dateUserStart.before(endDate) && endDate.before(dateUserEnd) ||
                    startDate.before(dateUserEnd) && dateUserStart.before(startDate))
                    || endDate.before(dateUserEnd) &&
                    dateUserStart.before(endDate) &&
                    dateUserStart.before(startDate) &&
                    startDate.before(dateUserEnd)) { //добавить элемент

                DataModel elementDataModel = new DataModel();

                elementDataModel.setRecordCategory(strarray[0].substring(1, 6));
                elementDataModel.setRecordSubCategory(strarray[0].split(",")[1]);
                elementDataModel.setStartDate(startDate);
                elementDataModel.setFinishDate(endDate);
                elementDataModel.setDetails(strarray[0].split(",")[4]);

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