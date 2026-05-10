package threadrelay;
import java.util.*;

public class Staffetta extends Thread{
    int n;
    int sleep;
    private Staffetta precedente;
    private boolean start = false;
    private boolean continuo = true;
    private List<Corridore> corridori = new ArrayList<>();

    public Staffetta(int n, int sleep){
        this.n = n;
        this.sleep = sleep;
    }
    
    void setPrecedente(Staffetta precedente){
        this.precedente = precedente;
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
                Thread.sleep(sleep);
            }
        } 
        
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }    
}
