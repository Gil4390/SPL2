package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.services.CPUService;

import java.util.LinkedList;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU {
    private int cores;
    DataBatch databatch;
    Cluster cluster;
    boolean ready;
    int processedTime;
    int endProcessedTime;

    public CPU(int numOfCores, Cluster cluster){
        cores=numOfCores;
        this.cluster=cluster;
        ready=true;
        processedTime=0;
    }

    public void Initialize(){}

    public void ReciveUnProcessedData(DataBatch databatch){
        this.databatch=databatch;
        ready=false;
        endProcessedTime=processedTime+(32/cores)*databatch.getProcessedTime();
    }

    public void tick(){
        processedTime++;
        compute();
    }

    public void compute(){
        if(!ready & processedTime==endProcessedTime){
            //cluster.rec(databatch);
            endProcessedTime=0;
            databatch=null;
            ready=true;
        }
    }

    public boolean isReady(){
        return ready;
    }
}
