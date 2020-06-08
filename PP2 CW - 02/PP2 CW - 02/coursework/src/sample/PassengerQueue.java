package sample;

import java.util.ArrayList;
import java.util.List;

public class PassengerQueue {
    private Passenger[] queueArray = new Passenger[42];         //array and arrayList for entire queue array; queue elements ONLY that updates
    private List<Passenger> updatedQueue = new ArrayList<>();
    private int first, last, length;                            //first, last positions of queue, length is actual length of queue with passengers

    private int maxLength, leastTime, maxTimeInQueue, avgTime;  //for the report
    public int passengerCount, totalTime; //for AVG time calculation
    //max length is the max length recorded in the queue, most, least, avg times are for all queue passengers, total time is total spent by everyone
    //in queue, passenger count is total number of passengers who have joined queue - to get avg time

    //add method, to add people from waiting room into queue
    public void add(Passenger next){
        if (!isFull()){                           //only do the process if the queue isn't currently full
            queueArray[last] = next;
            length++;                             //here we are basically adding passengers to the end
            last++;
        }
    }

    //remove method to remove front person from queue
    public Passenger remove(){
        Passenger frontPassenger = queueArray[first];
        if(!isEmpty()){                           //return removed passenger to be used in Station class - representing the person added onto train
            first++;
            length--;                             //we move queue forward by incrementing "first" instance variable
        }else{
            System.out.println("Queue is currently empty");
        }
        return frontPassenger;
    }

    //setters and getters for all the instance variables of position at first, last, length and max length, least, most and avg times
    public void setFirst(int first) { this.first = first; }
    public int getFirst(){
        return first;
    }

    public void setLast(int last){ this.last = last; }
    public int getLast(){
        return last;
    }

    public void setLength(int length){
        this.length = length;
    }
    public int getLength() { return length; }


    //this section is for the variables to be used for the report, setters and getters for those. In addition a secondary setter is created as for the
    //loading process
    public void setMaxLength(){
        if (this.getLength()>this.getMaxLength()){
            this.maxLength = this.getLength();
        }
    }
    public int getMaxLength(){
        return this.maxLength;
    }
    public void setMaxLengthForLoad(int maxLength){
        this.maxLength = maxLength;
    }

    public void setLeastTime(){
        if (!getUpdatedQueue().isEmpty()) {
            this.leastTime = queueArray[0].getSecondsInQueue();     //setting an initial value
            for (Passenger passenger : getUpdatedQueue()) {
                if (this.leastTime > passenger.getSecondsInQueue()) {
                    this.leastTime = passenger.getSecondsInQueue();
                }
            }
        }
    }
    public int getLeastTime(){
        return this.leastTime;
    }
    public void setLeastTimeForLoad(int leastTime){
        this.leastTime = leastTime;
    }

    public void setMaxTimeInQueue(){
        for (Passenger passenger : getUpdatedQueue()){
            if (passenger.getSecondsInQueue()>this.maxTimeInQueue){
                this.maxTimeInQueue = passenger.getSecondsInQueue();
            }
        }
    }
    public int getMaxTimeInQueue(){ return this.maxTimeInQueue; }
    public void setMaxTimeInQueueForLoad(int maxTimeInQueue){
        this.maxTimeInQueue = maxTimeInQueue;
    }

    public void setAvgTime(){
        if (totalTime != 0) {
            this.avgTime = totalTime / passengerCount;
        }
    }
    public int getAvgTime(){
        return this.avgTime;
    }
    public void setAvgTimeForLoad(int avgTime){
        this.avgTime = avgTime;
    }

    public void setPassengerCountForLoad(int passengerCount) {
        this.passengerCount = passengerCount;
    }
    public int getPassengerCount() { return passengerCount; }

    public int getTotalTime() { return totalTime; }
    public void setTotalTimeForLoad(int totalTime) { this.totalTime = totalTime; }

    //setting and returning the updated queue as an arrayList - only the values
    public void setUpdatedQueue(){
        updatedQueue.clear();
        for (int i=0;i<length;i++){
            updatedQueue.add(queueArray[(first+i)%42]);
        }
    }
    public List<Passenger> getUpdatedQueue(){return updatedQueue;}

    //setting the queue array, this is needed for the load method, to initialize the queue array
    public void setQueueArray(Passenger[] newQueue){
        queueArray = newQueue;
    }
    public Passenger[] getQueueArray(){return queueArray;}


    //booleans returners, if queue is empty/ full
    public boolean isEmpty(){
        return getLength()==0;
    }

    //returning whether queue is full
    public boolean isFull(){
        return getLength()==42;
    }

    //simply printing out the current queue
    public void display(){
        System.out.print("Seats: ");
        for (int i=0;i<length;i++){
            System.out.print(queueArray[(first+i)].getSeatsBooked() + " - " + queueArray[(first+i)].getName() + " ");
        }
        System.out.println();
    }
}
