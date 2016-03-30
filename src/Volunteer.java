import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/*
Класс Volunteer следит за временем класса Timer, а также за командами класса Boss.
Получив команды от класса Boss, волонтеры выполняют свою работу до наступления определенного времени суток.
 */

public class Volunteer extends Human
{
    private double creativity;
    private double hardworking;
    private double lazyness;

    public boolean isCleaning;
    public boolean isCreating;
    public boolean isActing;

    private School act = null;
    private Date key = null;


    public Volunteer(String name, int age, boolean sex)
    {
        super(name, age, sex);
        generateHardworking();
        generateCreativity();
        generateLazyness();
    }

    private void generateHardworking()
    {
        double sexH;
        if (this.sex == true) sexH = 0;
        else sexH = 0.25;
        this.hardworking = ((1 - sexH)+(double) age/100);
    }

    private void generateCreativity()
    {
        double sexH;
        if (this.sex == true) sexH = 0.2;
        else sexH = 0;
        this.creativity = ((1 - sexH) - (double) age/100);
    }

    private void generateLazyness()
    {
        double sexH = 0.15;
        this.lazyness = (1 - sexH) - ((double) 1/age);
    }



    private void  toClean(Beach beach)
    {
        double time;
        StringBuilder sb;
        int cleaningAbility =  5000*100/(30*3);
        if (beach.getPolutedTerritory() > beach.getTerritory()*0.1)
        {
            if (lazyness > 0.8) time = (int) (3*lazyness);
            else time = 3*lazyness*Math.random();
            double cleaned = cleaningAbility*hardworking*time;
            double polTer = beach.getPolutedTerritory();
            beach.setPolutedTerritory(polTer - cleaned);

            sb = new StringBuilder();

            sb.append(this.name);
            if (sex == true)
            {
                sb.append(" чистил ");
            }
            else sb.append(" чистила ");
            sb.append(String.format("%.2f часов. Очищено %.2f м.кв. Сейчас загрязнено %.2f из %d м.кв", time, cleaned, beach.getPolutedTerritory(), beach.getTerritory()));

            String s = sb.toString();
            Organization.write(s);

            System.out.println(s);
        }
        else beach.beachIsClean();
    }


    /*
    Случайным образом выбирает над чем работать - над театром или над презентацией.
     */

    private void toCreate()
    {
        double choice = Math.random();

        if (thyatro < 100 && presentation < 100) {
            if (choice < 0.5) {
               toPrepareTheatre();
            } else {
                toPreparePresentation();
            }
        } else if (thyatro > 100 && presentation < 100)
        {
            toPreparePresentation();
        } else if (thyatro < 100 && presentation > 100) toPrepareTheatre();
    }



    private void toAct(Map<Date, School> map)
    {
        for (Map.Entry<Date, School> pair : map.entrySet())
        {
            act = pair.getValue();
            key = pair.getKey();
        }


        if (act instanceof Kindergarten) {
            String s = String.format("%s участвует в театре в детском саду %s. Вовлечено %d детей.", this.name, ((Kindergarten) act).getName(), ((Kindergarten) act).getKidsInvolved());
            System.out.println(s);
            Organization.write(s);
        }
        else if (act instanceof School)
        {
            String s = String.format("%s участвует в презентации в школе %s. Вовлечено %d детей.", this.name, act.getName(), act.getKidsInvolved());
            System.out.println(s);
            Organization.write(s);
        }

    }

    private void toPreparePresentation()
    {
        if (presentation < 100 && !super.isPresentationReady) {
            presentation += creativity * 5;
            String s = String.format("%s работает над презентацией %.2f", this.name, presentation);
            System.out.println(s);
            Organization.write(s);
        }
        if (presentation >= 100)
        {
            String s = "Презентация готова";
            System.out.println(s);
            Organization.write(s);
            super.isPresentationReady = true;

        }
    }

    private void toPrepareTheatre()
    {
        if (thyatro < 100 && !super.isTheatreReady) {
            thyatro += creativity * 8;
            String s = String.format("%s работает над театром %.2f", this.name, thyatro);
            System.out.println(s);
            Organization.write(s);
        }
        if (thyatro >= 100)
        {
            String s = "Театр готов";
            System.out.println(s);
            Organization.write(s);
            super.isTheatreReady = true;
        }
    }

    /*
    Следит за временем класса таймер и за состояниме собственных boolean-переменных.
    Выполняет работу, на которую отправил босс до определенного времени.
     */
    @Override
    public void run()
    {
        if (isCleaning == true)
        {
            synchronized (Organization.beach)
            {
                toClean(Organization.beach);
            }
            while (true) {
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (hour == 12) {
                    isCleaning = false;
                    break;
                }
            }
        }
        if (isCreating == true)
        {
            synchronized (this) {
                toCreate();
            }

            while (true)
            {
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (hour == 17)
                {
                    isCreating = false;
                    break;
                }
            }
        }
        if (isActing == true)
        {
            synchronized (this)
            {
                toAct(Boss.tempMap);
            }
                while (true)
                {
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (hour == (key.getHours()+1))
                    {
                        isActing = false;
                        Boss.tempMap.remove(key);
                        break;
                    }
                }

        }
    }
}
