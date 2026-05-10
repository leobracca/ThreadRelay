package threadrelay;
import java.util.*;

public class Staffetta extends Thread{
    int n;
    private Staffetta precedente;
    private boolean start = false;
    private boolean continuo = true;
    private List<Corridore> corridori = new ArrayList<>();

    public Staffetta(int n){
        this.n = n;
    }
    
    void setPrecedente(Staffetta precedente){
        this.precedente = precedente;
    }
    
    void inizio(){
        if(n == 1){
            start = true;
        }
    }
    
    synchronized void sblocca(){
        start = true;
        this.notify();
    }
     
    public static interface Corridore{
        void update(int n, int valore);
    }

    void addCorridore(Corridore corridore){
        corridori.add(corridore);
    }

    private void notifyCorridori(int valore){
        for(Corridore corridore : corridori){
            corridore.update(n, valore);
        }
    }

    void controlloFermo(){
        if(Thread.currentThread().isInterrupted()){
            continuo = false;
        }
    }
    
    void controlloSblocco(int i){
        if(i == 90 && precedente != null){
            precedente.sblocca();
        }
    }
    
    @Override
    public void run(){
        try{
            synchronized(this){
                while(!start){
                    this.wait();
                }
            }

            for(int i = 0; i < 100 && continuo == true; i++){
                notifyCorridori(i);
                
                controlloSblocco(i);
                controlloFermo();
                Thread.sleep(100);
            }
        } 
        
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }    
}
