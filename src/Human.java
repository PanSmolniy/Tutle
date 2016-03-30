
/*
Класс хранит данные о готовности театра, а также является родителем для классов Volunteer и Boss.
Является потомком класса Timer - наследует время класса таймер и методы класса Thread.
 */

public abstract class Human extends Timer
{
    String name;
    int age;
    boolean sex;

    protected static int hour;
    protected static int dayOfWeek;

    public static double thyatro = 0;
    protected static boolean isTheatreReady = false;
    public static double presentation = 0;
    protected static boolean isPresentationReady = false;

    public Human(String name, int age, boolean sex)
    {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public int checkTimer()
    {
        return hour;
    }
    public int checkDayOfTheWeek() {return dayOfWeek;}

    public static void setHour(int hour)
    {
        Human.hour = hour;
    }

    public static void setDayOfWeek(int dayOfWeek) {
        Human.dayOfWeek = dayOfWeek;
    }
}
