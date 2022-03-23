package com.example.sleepapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.datatype.Duration;

public class CSVFile {

    public CSVFile() {
    }

    public List<DataModel> getDataModelFromListSleepResult(List<List<String>> listSleepResult,Date dateUserStart, Date dateUserEnd) {

        // + Vika 22.03.22
        // Перевести в dataModels, разбить на подстроки

        List<DataModel> dataModels = new ArrayList<>();

        for (List<String> row : listSleepResult)
        {
            DataModel elementDataModel = new DataModel();

            String[] strarray = row.toArray(new String[0]);

            elementDataModel.setRecordCategory(strarray[0].substring(1, 4));
            elementDataModel.setRecordSubCategory(strarray[0].split("\".+?\"")[1]);
            elementDataModel.setStartDate(strarray[0].split("\".+?\"")[2]);

            String finishDateStr = strarray[0].split("\".+?\"")[3]; // убрать тире и точки
            String startDateStr = strarray[0].split("\".+?\"")[2]; // убрать тире и точки

            try {
                Date startDate = new SimpleDateFormat("d MMM  yyyy HH:mm",
                        Locale.getDefault()).parse((startDateStr.replace("-"," ")).replaceAll("\\."," "));
                Date endDate = new SimpleDateFormat("d MMM  yyyy HH:mm",
                        Locale.getDefault()).parse((finishDateStr.replace("-"," ")).replaceAll("\\."," "));

                Long dateEnd = endDate.getTime(); // заданная дата в Unix-epoch в мс
                Long dateStart = startDate.getTime(); // заданная дата в Unix-epoch в мс


                Long sleepDuration = (dateEnd - dateStart)/1000/60;//38 минут сна - продолжительность сна в минутах
                Long durationUserEnd = (dateUserEnd.getTime() - dateEnd)/1000/60/60; //разница в минутах между пользовательским концом периода
                // и концом из файла

                Long durationUserStart = (dateUserStart.getTime() - dateStart)/1000/60; //разница в минутах между текущим временем и заданным
                elementDataModel.setFinishDate(endDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }


            //before(dateUserEnd) && (startDate.after(dateUserStart))
                   // || endDate.after(dateUserStart) && (startDate.after(dateUserStart))){

           // }
                // если дата окончания из файла позже даты пользователя



            elementDataModel.setDetails(strarray[0].split("\".+?\"")[4]);

            dataModels.add(elementDataModel);//
        }

        // - Vika 22.03.22
        // 3.2 Отбирать записи с категорией "Сон"
        // и датой окончания больше даты окончания периода пользователя
        //И ( датой начала меньше даты начала периода пользователя
        //или датой начала больше даты начала периода пользователя)
        return dataModels;
    }

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


}