import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 Программа имитирует деятельность некоммерческой организации.
 Организация занимается очисткой пляжа а также работает со школами и детсадами.
 Организацией управляет босс, а всю деятельность осуществляют волонтеры.
 */


public class Organization
{
    public static Organization thirdEye = new Organization();

    public static Boss seyhan = new Boss("Seyhan", 38, true);
    public static Beach beach = new Beach();
    public static Timer timer = new Timer();


    public static void main(String[] args)
    {
        thirdEye.run();
    }

    public void run()
    {
        Thread timerThread = new Thread(timer);
        timerThread.start();
        Thread bossThread = new Thread(seyhan);
        bossThread.start();
        Thread beachThread = new Thread(beach);
        beachThread.start();
    }

    /*
    Сетод создает лог программы и сохраняет туда текстовые комментарии происходящего.
     */
    public static void write(String text)
    {
        String fileName ="c:/caplumba/";
        File filePath = new File(fileName);
        filePath.mkdirs();
        File file = new File(fileName + "log.txt");


        FileWriter out = null;
        try {
            out = new FileWriter(file, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            out.write(text + "\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
