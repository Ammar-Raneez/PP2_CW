package sample;

public class Passenger {
    private String name, NIC;                //instance variables of names, seconds, and seats booked of a passenger
    private int secondsInQueue, seatsBooked;

    //sets and returns names for all the passengers
    public void setName(String name){
        if (name.trim().equals("")){
            System.out.println("Invalid Name");     //if there's an invalid name print message, otherwise set instance
        }else{                                      //variable to that variable
            this.name = name;
        }
    }
    public String getName(){
        return name;
    }

    public void setNIC(String NIC){ this.NIC = NIC; }
    public String getNIC(){ return NIC; }

    //sets and returns seats booked for a passenger
    public void setSeatsBooked(int seatsBooked){
        this.seatsBooked = seatsBooked;
    }
    public int getSeatsBooked(){
        return seatsBooked;
    }       //"this" refers to the current instance variable of the object created; necessary only when the name of parameter and itself are the same

    //sets and returns seconds in queue for a passenger
    public void setSecondsInQueue(int secondsInQueue){
        this.secondsInQueue += secondsInQueue;
    }
    public int getSecondsInQueue(){
        return secondsInQueue;
    }
}
