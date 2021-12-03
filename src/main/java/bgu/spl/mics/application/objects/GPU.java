package bgu.spl.mics.application.objects;

import Callbacks.Gpu_Callback;
import bgu.spl.mics.application.services.GPUService;
import jdk.javadoc.internal.doclets.toolkit.util.Utils;

import java.util.PriorityQueue;
import java.util.Queue;

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

    private Queue <Utils.Pair<Integer,DataBatch>> processingDataBatch;

    private DataBatch[] unProcessedDataBatch;
    private Gpu_Callback callback;

    private int indexUPDB;
    private int countPDB;
    private int capacity;
    private int timeClock;
    private int trainingTime;

    public GPU(Type type,Cluster cluster){
        this.type=type;
        switch(type){
            case GTX1080: {capacity = 8; trainingTime=4;}
            case RTX2080: {capacity = 16; trainingTime=2;}
            case RTX3090: {capacity = 32; trainingTime=1;}
        }
        processingDataBatch= new PriorityQueue<Utils.Pair<Integer,DataBatch>>();
        this.cluster = cluster;
        countPDB=0;
        model = null;
        indexUPDB = 0;
        timeClock=0;
    }

    public void tick(){
        timeClock++;
    }

    public void Initialize(Gpu_Callback callback)
    {
        this.callback=callback;
    }

    public void ReciveModel(Model model){
        this.model = model;
        DivideDataBatch();
    }

    private void TrainModel(){
        sendDataBatch();
        //callback.call();
    }

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

    private void sendDataBatch(){

    }

    public void ReciveProcessedData(DataBatch databatch){


    }
}
