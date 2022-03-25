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
            dateUserEnd = new Date(2022,03,17);

            try {
                dateUserStart = new SimpleDateFormat("dd.MM.yyyy HH:mm",
                        Locale.getDefault()).parse("16.03.2022 00:01");
                String[] strarray = row.toArray(new String[0]);


                String finishDateStr = strarray[0].split("\".+?\"")[3]; // убрать тире и точки
                String startDateStr = strarray[0].split("\".+?\"")[2]; // убрать тире и точки
               Date startDate = new SimpleDateFormat("d MMM  yyyy HH:mm",
                        Locale.getDefault()).parse((startDateStr.replace("-"," ")).replaceAll("\\."," "));
                Date endDate = new SimpleDateFormat("d MMM  yyyy HH:mm",
                        Locale.getDefault()).parse((finishDateStr.replace("-"," ")).replaceAll("\\."," "));

                Long sleepDuration = (endDate.getTime() - startDate.getTime())/1000/60;//38 минут сна - продолжительность сна в минутах
                //Long durationUserEnd = (dateUserEnd.getTime() - dateEnd)/1000/60; //разница в минутах между пользовательским концом периода
                // и концом из файла

                //Long durationUserStart = (dateStart - dateUserStart.getTime())/1000/60; //разница в минутах между пользовательским началом периода

                if (endDate.before(dateUserEnd) &&
                        startDate.after(dateUserStart) &&
                        dateUserStart.before(endDate) &&
                        startDate.before(dateUserEnd)
                        && sleepDuration < 300) { //добавить элемент

                    DataModel elementDataModel = new DataModel();

                    elementDataModel.setRecordCategory(strarray[0].substring(1, 4));
                    elementDataModel.setRecordSubCategory(strarray[0].split("\".+?\"")[1]);
                    elementDataModel.setStartDate(strarray[0].split("\".+?\"")[2]);
                    elementDataModel.setFinishDate(endDate);
                    elementDataModel.setDetails(strarray[0].split("\".+?\"")[4]);

                    dataModels.add(elementDataModel);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

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