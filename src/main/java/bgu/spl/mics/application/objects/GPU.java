package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.services.GPUService;

import java.util.*;

import static bgu.spl.mics.application.objects.Model.Status.Trained;

/**
 * Passive object representing a single GPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 * @inv processingDataBatch.size() <= capacity
 */
public class GPU {
    /**
     * Enum representing the type of the GPU.
     */
    enum Type {RTX3090, RTX2080, GTX1080}

    private final Type type;

    private final int id;
    private Model model;
    private final Cluster cluster;

    private final Queue<Pair<DataBatch,Integer>> processingDataBatch;

    private DataBatch[] unProcessedDataBatch;

    private int indexUPDB;
    private int countPDB;
    private int capacity;
    private int timeClock;
    private int trainingTime;

    private boolean finishTrainModel;
    private boolean ready;

    public GPU(String type,Cluster cluster, int id){
        this.type=FromStringToType(type);
        this.id = id;
        if(this.type == Type.GTX1080) capacity = 8; trainingTime=4;
        if(this.type == Type.RTX2080) capacity = 16; trainingTime=2;
        if(this.type == Type.RTX3090) capacity = 32; trainingTime=1;

        processingDataBatch= new LinkedList<Pair<DataBatch,Integer>>();
        this.cluster = cluster;
        countPDB=0;
        model = null;
        indexUPDB = 0;
        timeClock=0;

        ready=true;
        finishTrainModel=false;
    }


    /**
     * represent a tick for the cpu.
     * <p>
     * @post this.timeClock == @pre(this.timeClock)+1
     */
    public void tick(){
        timeClock++;
        if(!ready) {
            cluster.getStatistics().AddGpu_TimeUsed();
            TrainModel();
            SendDataBatch();
            if (unProcessedDataBatch!=null && countPDB == unProcessedDataBatch.length & countPDB != 0) {
                cluster.finishTrainModel(model.getName());
                finishTrainModel = true;
                model.setStatusString("Trained");
                Finish();
            }
        }
    }

    public void TrainModelEvent(Model m){
        ready=false;
        finishTrainModel=false;
        model=m;
        model.setStatusString("Training");
        DivideDataBatch();
    }

    public void TestModel(Model m){
        ready = false;
        finishTrainModel=false;
        this.model = m;
        Random rnd = new Random();
        if(rnd.nextDouble() < m.getTestProbability()){
            this.model.setResultString("Good");
        }
        else{
            this.model.setResultString("Bad");
        }
        model.setStatusString("Tested");
        Finish();
    }


    /**
     * this function trains the process data the gpu holds
     * <p>
     * @pre this.ready == false
     * @pre processingDataBatch.isEmpty() != true;
     * @post processingDataBatch.size <= @pre(processingDataBatch.size)
     * @post countPDB >= @pre(countPDB)
     */
    private void TrainModel(){
        synchronized (processingDataBatch) {
            while (!processingDataBatch.isEmpty() && processingDataBatch.peek().getSecond() + trainingTime <= timeClock) {
                processingDataBatch.poll();
                countPDB++;
            }
        }
    }

    /**
     * this function resat the gpu for a new model
     * <p>
     * @pre this.model != null
     * @pre this.ready == false
     * @pre countPDB ==  indexUPDB
     * @pre processingDataBatch.isEmpty() == true;
     * @post this.ready == true
     * @post this.model == null
     * @post this.unProcessedDataBatch == null
     * @post this.indexUPDB == 0
     * @post this.countPDB ==0
     * @post this.trainingTime =0;
     */
    private void Finish(){
        //model=null;
        countPDB=0;
        indexUPDB=0;
        trainingTime=0;
        unProcessedDataBatch=null;
        this.ready=true;
    }

    /**
     * this function divides the data to data batch's.
     * <p>
     * @pre this.model != null
     * @pre this.ready == false
     * @pre unProcessedDataBatch.isEmpty() == true;
     * @post unProcessedDataBatch.isEmpty() == false;
     */
    private void DivideDataBatch(){
        int size =model.getData().getSize()/1000;
        unProcessedDataBatch = new DataBatch[size];
        for(int i=0; i<size;i++){
            DataBatch data = new DataBatch(i*1000,model.getData());
            unProcessedDataBatch[i]=data;
        }
    }

    /**
     * if left a data's batch's un processed, sent it to cluster if the gpu have a place to store it when comes back.
     * <p>
     * @pre this.model != null
     * @pre this.ready == false
     * @pre unProcessedDataBatch.size()-1 > indexUPDB
     * @post this.indexUPDB = @pre(indexUPDB)+1
     */
    private void SendDataBatch(){
        int counter = 0;
        while(counter < (capacity- processingDataBatch.size()) && indexUPDB<unProcessedDataBatch.length) {
            Pair tempPair = new  <DataBatch,Integer> Pair(unProcessedDataBatch[indexUPDB],id);
            cluster.ReceiveDataFromGpu(tempPair);
            indexUPDB++;
            counter++;
        }
    }

    /**
     * this function receives a process data and store it
     * <p>
     * @pre this.model != null
     * @pre this.ready == false
     * @post this.countPDB > @pre(countPDB)
     */
    public void ReceiveProcessedData(DataBatch databatch){
        synchronized (processingDataBatch) {
            processingDataBatch.add(new Pair<DataBatch, Integer>(databatch, timeClock));
        }
    }

    /**
     * @return the Type by the string type
     * <p>
     * @param type the type
     */
    private Type FromStringToType(String type){
        switch (type) {
            case ("RTX3090"):
                return Type.RTX3090;
            case ("RTX2080"):
                return Type.RTX2080;
            case ("GTX1080"):
                return Type.GTX1080;
            default:
                return null;
        }
    }

    public Type getType() {
        return type;
    }

    public synchronized boolean isReady() {
        return ready;
    }

    public Model getModel() {
        return model;
    }

    public int getId() {
        return id;
    }

    public boolean isFinishTrainModel() {
        return finishTrainModel;
    }

    public void setFinishTrainModel(boolean finishTrainModel) {
        this.finishTrainModel = finishTrainModel;
    }
}
