package bgu.spl.mics.application.objects;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */

public class DataBatch {
    private int processTime;
    private int startTick; //TODO check if ned to remove
    private int start_index;
    Data data;

    public DataBatch(int index,Data data) {
        this.start_index = index;
        this.data = data;
        switch(data.getType()){
            case Images: this.processTime = 4;
            case Text: this.processTime = 2;
            case Tabular: this.processTime = 1;
        }
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
