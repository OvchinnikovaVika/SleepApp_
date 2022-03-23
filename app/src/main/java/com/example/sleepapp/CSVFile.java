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

            //getStringToDate
            // String finishDateStr = strarray[0].split("\".+?\"")[3];
            String finishDateStr = strarray[0].split("\".+?\"")[3]; // убрать тире и точки

            if (finishDateStr.length()==18) { // при условии, что месяц записан 3 буквами
                String finishDateStrSubstring1 = finishDateStr.substring(0,12);  // получаем строку формата:
                String finishDateStrSubstring2 = finishDateStr.substring(13,18);

                finishDateStrSubstring1 = finishDateStrSubstring1.replaceAll("-", " ");
//                finishDateStrSubstring1 = finishDateStrSubstring1.replaceAll("\\.\\s", ".");
                finishDateStrSubstring1 = finishDateStrSubstring1.replaceAll("\\.", " ");

                finishDateStr = finishDateStrSubstring1 + " " + finishDateStrSubstring2;
            }
            else {
                String finishDateStrSubstring1 = finishDateStr.substring(0,13);  // получаем строку формата:
                String finishDateStrSubstring2 = finishDateStr.substring(14,19);

                finishDateStrSubstring1 = finishDateStrSubstring1.replaceAll("-", " ");
//                finishDateStrSubstring1 = finishDateStrSubstring1.replaceAll("\\.\\s", ".");
                finishDateStrSubstring1 = finishDateStrSubstring1.replaceAll("\\.", " ");

                finishDateStr = finishDateStrSubstring1 + " " + finishDateStrSubstring2;
            }

            Date startDate = new Date();
            Date endDate = new Date();
            try {
                endDate = new SimpleDateFormat("d MMM  yyyy HH:mm", Locale.getDefault()).parse(finishDateStr);

            } catch (ParseException e) {
                e.printStackTrace();
            }


            if (endDate.before(dateUserEnd) && (startDate.after(dateUserStart))
                    || endDate.after(dateUserStart) && (startDate.after(dateUserStart))){

            }
                // если дата окончания из файла позже даты пользователя


            elementDataModel.setFinishDate(endDate);
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