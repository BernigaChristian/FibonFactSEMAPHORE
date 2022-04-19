import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Semaphore;

/**
 * @author : Christian Berniga
 * @class : 4 D
 * @created : 19/04/2022, martedì
 **/

class Thread1 extends Thread{
    Semaphore s;
    JFrame frame;

    public Thread1(Semaphore s){
        this.s = s;
        frame = new JFrame("T1");
        frame.setSize(300,300);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocation(0,0);
    }

    public void run() {
        int prec = 1;
        int suc = 1;
        JTextArea text = new JTextArea(200,200);
        text.append("1\n1\n");
        frame.getContentPane().add(BorderLayout.CENTER,text);
        try{
            s.acquire();
            while(true){
                int res = prec + suc;
                if(res >= 1000) s.release();
                text.append(Integer.toString(res) + "\n");
                prec = suc;
                suc = res;
                sleep(1000);
            }
        }catch (InterruptedException e){}

    }
}

class Thread2 extends Thread{
    Semaphore s;
    JFrame frame;

    public Thread2(Semaphore s){
        this.s = s;
        frame = new JFrame("T2");
        frame.setSize(300,300);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocation(300,0);
    }

    private int fattoriale(int num){
        int res = 1;
        for(int i = 1; i <= num; i ++)
            res *= i;
        return res;
    }

    public void run() {
        JTextArea text = new JTextArea(200,200);
        frame.getContentPane().add(BorderLayout.CENTER,text);
        int num = 1;
        try{
            s.acquire();
            while(true){
                int res = fattoriale(num ++);
                if(res >= 1000) s.release();
                text.append(res + "\n");
                sleep(1000);
            }
        }catch (InterruptedException e){}
    }
}

class Thread3 extends Thread{
    Semaphore s;
    JFrame frame;

    public Thread3(Semaphore s){
        this.s = s;
        frame = new JFrame("T3");
        frame.setSize(300,300);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocation(600,0);
    }

    public void run() {
        JTextArea text = new JTextArea(200,200);
        frame.getContentPane().add(BorderLayout.CENTER,text);
        try {
            sleep(1000);
            s.acquire();
            for(int i = 100; i >=0; i --) {
               text.append(i+"\n");
               sleep(1000);
            }
            s.release();
        }catch (InterruptedException e){}
    }
}

public class Main {
    static Semaphore sem;

    public static void main(String[] args) {
        sem = new Semaphore(2);
        Thread1 t1 = new Thread1(sem);
        Thread2 t2 = new Thread2(sem);
        Thread3 t3 = new Thread3(sem);

        t1.start();
        t2.start();
        t3.start();

        try{
            t1.join();
            t2.join();
            t3.join();
        }catch (InterruptedException e){}
    }
}
