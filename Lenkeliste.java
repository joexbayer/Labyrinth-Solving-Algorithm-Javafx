//package sample;
import java.util.Iterator;

public class Lenkeliste<T> implements Liste<T> {
    @SuppressWarnings("unchecked")
    
    Node startNode;
    Node lastNode;
    int counter = 0;

    //Oppretter en Node-klasse
    class Node{
        T data;
        Node next;
        Node previous;
        
        public Node(T d){
            data = d;
            next = null;
            previous = null;
        }
    }
    //Oppretter klassen LenkelisteIterator
    public class LenkelisteIterator implements Iterator<T> {
        private Node nyNode;
    
        public LenkelisteIterator() {
            nyNode = startNode;
        }
        @Override
        public boolean hasNext(){
            if(nyNode == null){
                return false;
            }
            return true;
        }

        @Override
        public T next(){
            T return_verdi = nyNode.data;
            nyNode = nyNode.next;
            return return_verdi;
        }

        @Override
        public void remove(){
            
        }
    }

    public Iterator<T> iterator(){
        return new LenkelisteIterator();
    }


    @Override
    public int stoerrelse(){
        return counter;
    }

    @Override
    public void leggTil(T x){   //Legger til bakerst
        Node newNode = new Node(x);
        if(counter != 0){
            lastNode.next = newNode;    //Forrige node peker på ny node
            newNode.previous = lastNode;    //Ny node peker tilbake på forrige node
            lastNode = newNode;
        } else {    //Dersom objektet er den første i listen
            startNode = newNode;
            lastNode = newNode;
        }
        counter++;  //Oppdaterer teller
    }

    @Override
    public T fjern(){   //Fjerner første element i liste
        if(counter -1 <= 0){   //Sjekker at man ikke fjerner fra en tom liste
            throw new UgyldigListeIndeks(counter);  //Kaster ute-av-indeks-feilkode
        }
        T sNd = startNode.data; 
        Node newStartN = startNode.next;    //Oppretter en ny første-node
        startNode.next = null;
        newStartN.previous = null;
        startNode = newStartN;
        counter--;
        return sNd;   
    }

    @Override
    public void leggTil(int pos, T x){ 
        if(counter < pos){  //Sjekker at posisjonen ikke er utenfor indeks
            throw new UgyldigListeIndeks(counter);
        }
        Node newNode = new Node(x);
        if(counter > 0){    //Sjekker at listen ikke er tom
            if(counter == pos){ //Posisjon skal være bakerst
                lastNode.next = newNode;
                newNode.previous = lastNode;
                lastNode = newNode;
            } else{
                Node currentNode = retPos(pos); //Finner node som skal skyves bakover
                if(pos == 0){   //Legge til noe som første element
                    startNode.previous = newNode;
                    newNode.next = startNode;
                    startNode = newNode;
                } else if(startNode == lastNode){   //Kun et element i liste fra før
                    startNode = newNode;
                    newNode.next = currentNode;
                    currentNode.previous = newNode;
                    lastNode = currentNode;
                }  else {    //Dersom ny node er imellom to noder
                    Node previousN = currentNode.previous;
                    previousN.next = newNode;
                    currentNode.previous = newNode;
                    newNode.next = currentNode;
                    newNode.previous = previousN;
                }
            }     
        } else{ //Legge til første og eneste element i listen
            startNode = newNode;
            lastNode = newNode;
        }
        counter++;
    }

    @Override
    public void sett(int pos, T x){
        Node n = retPos(pos);
        n.data = x;    
    }

    private Node retPos(int pos){   //Metode for å finne et objekt på gitt posisjon
        if(pos > counter - 1|| pos < 0 || counter <= 0){    //Sjekker at forespurt pos er i indeks
            throw new UgyldigListeIndeks(counter);
        }
        Node p = startNode;
        for (int i = 0; i < pos; i++) { //Finner node på posisjon
            p = p.next;
        }
        return p;    
    }

    @Override
    public T hent(int pos){
        Node n = retPos(pos);
        return n.data;
    } 

    @Override
    public T fjern(int pos){
        Node currN = retPos(pos);   //Noden som skal fjernes
        if(counter != 0){   //Dersom liste ikke er tom
            Node prevN = currN.previous;
            Node nextN = currN.next;
            if(currN == startNode || currN== lastNode){ //Dersom noden enten er første eller siste i liste 
                if(counter == 1){   //Dersom vi sletter den eneste noden i en liste (får da en tom liste)
                    startNode = null; lastNode = null;
                } else if(currN == startNode){  //Om noden er første element i liste
                    nextN.previous = null;
                    startNode = nextN;
                } else if(currN == lastNode){   //Om noden er siste element i listen
                    prevN.next = null;
                    lastNode = prevN;
                }
            } else {
                prevN.next = nextN;
                nextN.previous = prevN;
            }  
        } else {            
            throw new UgyldigListeIndeks(counter);
        }
        counter--;
        return currN.data;
    }
}