package bgu.spl.mics.application.objects;

import Callbacks.Gpu_Callback;
import java.util.*;

/**
 * Passive object representing a single GPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class GPU {
    /**
     * Enum representing the type of the GPU.
     */
    enum Type {RTX3090, RTX2080, GTX1080}

    private Type type;
    private Model model;
    private Cluster cluster;

    private Queue<DataBatch> processingDataBatch;

    private DataBatch[] unProcessedDataBatch;
    private Gpu_Callback callback;

    private int indexUPDB;
    private int countPDB;
    private int capacity;
    private int timeClock;
    private int trainingTime;
    private boolean ready;

    public GPU(Type type,Cluster cluster){
        this.type=type;
        switch(type){
            case GTX1080: {capacity = 8; trainingTime=4;}
            case RTX2080: {capacity = 16; trainingTime=2;}
            case RTX3090: {capacity = 32; trainingTime=1;}
        }
        processingDataBatch= new PriorityQueue<DataBatch>();
        this.cluster = cluster;
        countPDB=0;
        model = null;
        indexUPDB = 0;
        timeClock=0;
        ready=false;
    }

    /**
     * this function initializes the gpu
     * <p>
     * @param callback the callback of finishing training the model
     * @pre this.ready=false;
     * @pre this.callback == null
     * @post this.callback == callback
     */
    public void Initialize(Gpu_Callback callback)
    {
        this.callback=callback;
        ready=true;
    }

    /**
     * represent a tick for the cpu.
     * calls TrainModel() every tick
     * <p>
     * @post this.timeClock == @pre(this.timeClock)+1
     */
    public void tick(){
        timeClock++;
        TrainModel();
    }

    /**
     * this function store the model the gpu needs to train
     * divide the data to data's batch's by calling DivideDataBatch();
     * <p>
     * @param model the model the gpu needs to train
     * @pre this.model == null
     * @post this.model == model
     * @post processingDataBatch.isEmpty() != true;
     */
    public void ReciveModel(Model model){
        this.model = model;
        DivideDataBatch();
    }

    /**
     * this train the process data the gpu holds
     * if in this tick finished training the model than send a message back to MessageBus and calls finish()
     * <p>
     * @pre this.ready == false
     * @pre processingDataBatch.isEmpty() != true;
     * @post processingDataBatch.size <= @pre(processingDataBatch.size)
     * @post countPDB >= @pre(countPDB)
     */
    private void TrainModel(){
        //if()


        //finish()
    }

    /**
     * this function sends back a call back that the gpu finished training the model
     * <p>
     * @pre this.model != null
     * @pre this.ready == false
     * @pre countPDB ==  indexUPDB
     * @pre processingDataBatch.isEmpty() == true;
     * @post this.ready == true
     * @post this.model == null
     * @post this.processingDataBatch == null
     * @post this.unProcessedDataBatch == null
     * @post this.indexUPDB == 0
     * @post this.countPDB ==0
     * @post this.trainingTime =0;
     */
    private void finish(){
        //callback.call();
    }

    /**
     * this function divides the data to data batch's.
     * after finish call sendDataBatch()
     * <p>
     * @pre this.model != null
     * @pre this.ready == false
     * @pre processingDataBatch.isEmpty() == true;
     * @post processingDataBatch.isEmpty() == false;
     */
    private void DivideDataBatch(){
        /*
        Data data = model.getData();
        unProcessedDataBatch = new unProcessedDataBatch[];
        for(0....data/1000)
            new databatch
        unProcessedDataBatch[i]=datacath;
        */
        sendDataBatch();
    }

    /**
     * if left a data's batch's un processed, sent it to cluster if the gpu have a place to store it when comes back.
     * <p>
     * @pre this.model != null
     * @pre this.ready == false
     * @pre processingDataBatch.size() <= capacity
     * @post this.indexUPDB >= @pre(indexUPDB)
     */
    private void sendDataBatch(){

    }

    /**
     * this function receives a process data and store it
     * <p>
     * @pre this.model != null
     * @pre this.ready == false
     * @post this.countPDB > @pre(countPDB)
     */
    public void ReceiveProcessedData(DataBatch databatch){
    }
}
