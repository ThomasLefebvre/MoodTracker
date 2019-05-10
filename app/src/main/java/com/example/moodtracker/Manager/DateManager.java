package com.example.moodtracker.Manager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateManager {

    public static String  getCurrentDate(){/**Method for recover current date*/
        Date date;
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat formater= new SimpleDateFormat("EEEE dd MM yyyy");
        calendar.add(calendar.DATE,0);
        date=calendar.getTime();
        return formater.format(date);
    }

    public static String  testDateDayToDay(int indexDay){ /**Method for recover date in term of index*/
        Date date;
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat formater= new SimpleDateFormat("EEEE dd MM yyyy");
        calendar.add(calendar.DATE,-indexDay);
        date=calendar.getTime();
        return formater.format(date);
    }

    public static String listDayText (int index){/**Method for recover string day*/
        String listDayText []={"Hier","Avant-hier","Il y a trois jours","Il y a quatre jours","Il y a cinq jours","Il y a six jours","Il y a une semaine","Il y a plus d'une semaine"};
        return listDayText[index];
    }
}
