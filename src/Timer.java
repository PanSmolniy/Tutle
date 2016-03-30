import java.text.SimpleDateFormat;
import java.util.Date;

/*
Класс Timer ведет отсчет 59 дней
с даты начала - 30 сентября 2015.
 */

public class Timer implements Runnable
{
    private static int year = 2015;
    private static int month = 8;
    private static int day = 30;
    private static int hour = 0;
    protected static Date date = new Date();
    public static int dayCounter = 0;

    public static Date getDate() {
        return date;
    }

    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy EEEE HH:00");



    @Override
    public void run()
    {
        date = new Date();
        int currentDay = date.getDay();
        while (true)
        {
            date.setHours(hour);
            date.setDate(day);
            date.setMonth(month);
            date.setYear(year-1900);
            hour++;
            dayCounter++;
            Human.setHour(date.getHours());
            Human.setDayOfWeek(date.getDay());

            if (date.getDate() != currentDay)
            {
                currentDay = date.getDate();
                String s = "\r\nНаступил нвоый день " + sdf.format(date);
                System.out.println(s);
                Organization.write(s);
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (dayCounter == 24*60) break;
        }
    }

}
