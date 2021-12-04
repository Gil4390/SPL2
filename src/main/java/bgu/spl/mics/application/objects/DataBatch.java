package bgu.spl.mics.application.objects;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */

public class DataBatch {
    private int processTime;
    private int startTick;
    private int index;
    Data data;

    public DataBatch(int index,Data data) {

    }

    public int getProcessTime(){
        return processTime;
    }

    public int getStartTick() {
        return startTick;
    }

    public void setStartTick(int startTick) {
        this.startTick = startTick;
    }
}
