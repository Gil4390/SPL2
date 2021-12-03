package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.services.GPUService;

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

    private DataBatch[] processedDataBatch;
    private DataBatch[] unProcessedDataBatch;
    private int indexPDB;
    GPUService gpuService;

    public GPU(Type type,Cluster cluster){
        this.type=type;
        switch(type){
            case GTX1080: processedDataBatch = new DataBatch[8];
            case RTX2080: processedDataBatch = new DataBatch[16];
            case RTX3090: processedDataBatch = new DataBatch[32];
        }
        this.cluster = cluster;
        model = null;
        indexPDB = 0;
    }

    public void Initialize(GPUService gpuService)
    {
        this.gpuService=gpuService;
    }

    public void ReciveEvent(Model model){
        this.model = model;
        //initalize DataBatch
    }

    private void TrainModel(){

    }

    private void DivideDataBatch(){

    }

    public void ReciveProcessedData(DataBatch databatch){

    }
}
