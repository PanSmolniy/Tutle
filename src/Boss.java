import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/*
Класс Boss следит за состоянием пляжа, а аткже за готовностью презентаций и театра для школ.
Если пляж грязный - босс отправляет волонтеров на очистку.
Босс каждый день отправляет волонтеров готовить презентацию и театр.
Если театр или презентация готова, босс генерирует расписание посещения школ и детсадов.
В дни посещения школ и детсадов, прочие занятия второстепенны.
Хранит данные о всех волонтерах в списке ArrayList.
Генерирует случайный список школ и детсадов.
 */

public class Boss extends Human
{
    public Boss(String name, int age, boolean sex)
    {
        super(name, age, sex);
    }

    private  static List<School> socials = new ArrayList<>();
    static
    {
        initializeSocials();
    }


    private static ArrayList<Volunteer> volunteers = new ArrayList<>();
    static
    {
        volunteers.add(new Volunteer("Eva", 23, false));
        volunteers.add(new Volunteer("Alex", 19, true));
        volunteers.add(new Volunteer("Alice", 26, false));
    }

    private static Map<Date, School> actMap = new HashMap<>();
    private static boolean agendaSet = false;
    private static List<Date> tempList = new ArrayList<>();
    private static int tempListCount = 0;
    private int counterForMapSize = 0;

    public static Map<Date, School> tempMap = new HashMap<>();

    /*
    Создает случайный список детсадов и школ до 10 штук
     */
    private static void initializeSocials()
    {
        int size = (int) (5 +  Math.random()*5);
        for (int i = 0; i < size; i++)
        {
            double a = Math.random();
            if (a > 0.5) socials.add(new School("School " + i, (int) (20 +  Math.random()*80)));
            else socials.add(new Kindergarten("Kindergarten " + i, (int) (20 +  Math.random()*80)));
        }
    }

    /*
    Генерирует случайную дату кроме выходны и в течение следующих 7 дней, после текущей даты класса Timer.
     */
    private static Date generateActDate(School social)
    {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(Timer.date);
        int random = (int) (1 + Math.random() * 7);
        calendar.add(Calendar.DAY_OF_YEAR, +random);
        if (calendar.getTime().getDay() == 6) calendar.add(Calendar.DAY_OF_YEAR, +2);
        if (calendar.getTime().getDay() == 0) calendar.add(Calendar.DAY_OF_YEAR, +1);

        Date actDate = calendar.getTime();

        if (social instanceof Kindergarten) actDate.setHours(9);
        else if (social instanceof School) actDate.setHours(14);

        return actDate;
    }


    /*
    Генерирует карту с расписанием посещения школ и детсадов.
    Учитывает, что именно готово на данный момент - театр, презентация или оба
     */
    private void generateMapForActing()
    {
        if (agendaSet == false || actMap.size() == 0) {
            if (socials.size() > 0) {
                String s = String.format("%s генерирует расписание", this.name);
                System.out.println(s);
                Organization.write(s);


                if (isTheatreReady && !isPresentationReady) {
                    for (int i = 0; i < socials.size(); i++) {
                        if (socials.get(i) instanceof Kindergarten) {
                            School social = socials.get(i);
                            socials.remove(i);
                            Date dateToAdd = generateActDate(social);
                            actMap.put(dateToAdd, social);
                        }
                    }
                } else if (isPresentationReady && !isTheatreReady) {
                    for (int i = 0; i < socials.size(); i++) {
                        if (socials.get(i) instanceof Kindergarten) {
                        } else {
                            School social = socials.get(i);
                            socials.remove(i);
                            Date dateToAdd = generateActDate(social);
                            actMap.put(dateToAdd, social);
                        }
                    }
                } else {
                    for (int i = 0; i < socials.size(); i++) {
                        School social = socials.get(i);
                        socials.remove(i);
                        Date dateToAdd = generateActDate(social);
                        actMap.put(dateToAdd, social);
                    }
                }

                agendaSet = true;

                String s1 = String.format("%s сгенерировал расписание", name);
                System.out.println(s1);
                Organization.write(s1 + "\r\n");

                for (Map.Entry<Date, School> pair : actMap.entrySet()) {
                    String s2 = pair.getKey() + " " + pair.getValue().getName();
                    System.out.println(s2);
                    Organization.write(s2);
                }
            }
            }
        }

    /*
    Сравнивает текущую дату класса Timer со всеми датами из карты расписания.
    Возвращает true, если сегодня день похода в школу или детсад.
     */
    private boolean checkDayToAct(Map<Date, School> map)
    {
        Date date = Timer.getDate();
        int currentDate = date.getDate();
        int currentMonth = date.getMonth();
        boolean result = false;
        for (Map.Entry<Date, School> pair : map.entrySet())
        {
            Date toRemove = pair.getKey();
            School toRem = pair.getValue();
            int setDate = pair.getKey().getDate();
            int setMonth = pair.getKey().getMonth();
            if (currentDate == setDate && currentMonth == setMonth)
            {
                result = true;
                tempList.add(toRemove);
                tempMap.put(toRemove, toRem);
                break;
            }

        }
        return result;
    }


    private void sendToClean(ArrayList<Volunteer> list)
    {
        for (Volunteer a : list)
        {
                a.isCleaning = true;
                Thread thread = new Thread(a);
                thread.start();
        }
    }

    private void sendToCreate(ArrayList<Volunteer> list)
    {
        for (Volunteer a : list)
        {
            a.isCreating = true;
            Thread thread = new Thread(a);
            thread.start();
        }
    }

    private void sendToAct(ArrayList<Volunteer> list)
    {
        for (Volunteer a : list)
        {
            a.isActing = true;
            Thread thread = new Thread(a);
            thread.start();
        }
    }


    /*
    Отслеживает врем класса Timer и раздает указания волонтерам в
    зависимости от следующего:
    - выходной сегодня или нет;
    - времени суток;
    - нужно ли сегодня ехать в школу или театр.
     */
    @Override
    public void run()
    {
        while (true)
        {
            int time = checkTimer();
            int day = checkDayOfTheWeek();

            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


                if (day != 6 && day != 0)
                {

                    if (agendaSet == false && isTheatreReady == true || isPresentationReady == true)
                    {
                        if (socials.size() != 0) {
                            generateMapForActing();
                        }
                    }

                    if  (!checkDayToAct(actMap))
                    {
                        if (time == 9)
                        {
                            if (Beach.getPolutedTerritory() > Beach.getTerritory() * 0.1 && !Beach.cleanedOnce)
                            {
                                sendToClean(volunteers);
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            else if (Beach.cleanedOnce && Beach.getPolutedTerritory() > Beach.getTerritory() * 0.2) {
                                Beach.cleanedOnce = false;
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Beach.cleanedOnce = true;
                                String s = "Пляж чист";
                                System.out.println(s);
                                Organization.write(s);
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                        if (time == 14) {
                            if (thyatro < 100 || presentation < 100) {
                                sendToCreate(volunteers);
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                        }


                    }
                    else
                    {
                        int a = tempList.get(tempListCount).getHours();
                        tempListCount++;
                        if (time == a) {
                            sendToAct(volunteers);
                            counterForMapSize++;
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (counterForMapSize > actMap.size()-1) {
                                counterForMapSize = 0;
                                agendaSet = false;
                                actMap.clear();
                                generateMapForActing();
                            }
                        }
                    }
                }
            if (dayCounter == 59*24) break;
        }
    }
}

