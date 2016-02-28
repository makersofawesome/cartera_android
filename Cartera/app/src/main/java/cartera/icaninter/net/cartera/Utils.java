package cartera.icaninter.net.cartera;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

/**
 * Created by Juzer on 2/26/2016.
 */
public class Utils {

    private static final String LOG_TAG = Utils.makeLogTag(Utils.class);

    public static String makeLogTag(Class c){
        return "RecSports: " + c.getSimpleName();
    }

    public static String formatRequestString(String name, String amount){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(name)
                .append(" is requesting $")
                .append(amount);
        return stringBuilder.toString();
    }


    public int timeInSeconds(String serverTimeStamp){

        int tDelim = serverTimeStamp.indexOf('T');


        String time = serverTimeStamp.substring(tDelim + 1, serverTimeStamp.length() - 1);
        Scanner sc = new Scanner(time);
        sc.useDelimiter(":");
        int seconds = 0;
        seconds += sc.nextInt() * 60 * 60;
        seconds += sc.nextInt() * 60;
        seconds += sc.nextDouble();

        return seconds;
    }

    public static String formatDate(String s) throws ParseException {
        int tDelim = s.indexOf('T');
        String date = s.substring(0, tDelim);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        simpleDateFormat.parse(date);

        Calendar c = simpleDateFormat.getCalendar();

        StringBuilder builder = new StringBuilder();

        builder
                .append(c.get(Calendar.MONTH))
                .append(" ")
                .append(c.get(Calendar.DAY_OF_MONTH))
                .append(", ")
                .append(Calendar.HOUR_OF_DAY)
                .append(Calendar.AM_PM);

        return builder.toString();
    }
}
