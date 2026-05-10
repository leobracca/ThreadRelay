package threadrelay;
import java.util.*;

public class Staffetta extends Thread{
    int n;
    private Thread precedente;
    private List<Corridore> corridori = new ArrayList<>();

    public static interface Corridore{
        void update(int n, int valore);
    }

    public Staffetta(int n, Thread precedente){
        this.n = n;
        this.precedente = precedente;
    }

    void addCorridore(Corridore corridore){
        corridori.add(corridore);
    }

    private void notifyCorridori(int valore){
        for(Corridore corridore : corridori){
            corridore.update(n, valore);
        }
    }

    @Override
    public void run(){
        try{
            if(precedente != null){
                precedente.join();
            }

            for(int i = 0; i < 100; i++){
                notifyCorridori(i);
            Thread.sleep(100);
            }
        } 
        
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }    
}
