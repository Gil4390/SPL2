package bgu.spl.mics.application.objects;

import java.util.PriorityQueue;

/**
 * Passive object representing information on a conference.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class ConfrenceInformation {

    private String name;
    private int date;
    private PriorityQueue<Pair<String,Integer>> succesfulModels;

    public ConfrenceInformation(String name, int date) {
        this.succesfulModels = new PriorityQueue<>();
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }


    public int getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "ConfrenceInformation{" +
                "name='" + name + '\'' +
                ", date=" + date +
                '}';
    }

    public PriorityQueue<Pair<String, Integer>> getSuccesfulModels() {
        return succesfulModels;
    }

    public void addSuccesfulModel(Pair<String, Integer> succesfulModel) {
        this.succesfulModels.add(succesfulModel);
    }
}
