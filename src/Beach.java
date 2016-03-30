
/*
Иметирует пляж. Пляж каждый день загрязняется на случайную небольшую величину.
Пляж считается чистым, если после работы волонтеров осталось от 10 до 30% загрязненной территории.
 */

public class Beach extends Thread
{
    private final static int length;
    private final static int width;
    private final static int territory;
    static boolean  cleanedOnce = false;
    static
    {
        length = 5000;
        width = 100;
        territory = length*width;
    }

    private static double polutedTerritory = territory*0.75;

    public static void setPolutedTerritory(double polutedTerritory)
    {
        Beach.polutedTerritory = polutedTerritory;
    }

    public static double getPolutedTerritory()
    {
        return polutedTerritory;
    }

    public static int getTerritory() {
        return territory;
    }

    public static void beachIsClean()
    {
        System.out.println("Пляж чист");
    }

    /*
    Следит за временем класса Timer.
    Каждую ночь загрязняется на случайную величину.
     */
    @Override
    public void run()
    {
        while (true)
        {
            if (Timer.dayCounter == 59*24) break;
            int time = Organization.timer.getDate().getHours();
            if (time == 1) getPoluted();
            try
            {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    Случайное загрязнение до 2% территории пляжа.
     */
    private void getPoluted()
    {
        double polution = territory*(Math.random()/50);
        this.setPolutedTerritory(this.polutedTerritory+polution);
        String s = String.format("За ночь пляж загрязнился на %.2f Загрязненная территория составляет %.2f", polution, polutedTerritory);
        System.out.println(s);
        Organization.write(s);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
