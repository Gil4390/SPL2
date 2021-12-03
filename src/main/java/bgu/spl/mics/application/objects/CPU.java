package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.services.CPUService;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU {
    private int cores;
    DataBatch databatch;
    Cluster cluster;
    CPUService cpuService;



    public CPU(int numOfCores, Cluster cluster){
        cores=numOfCores;
        this.cluster=cluster;
    }

    public void Initialize(CPUService microService){
        this.cpuService=microService;
    }

    public void ReciveUnProcessedData(DataBatch databatch){

    }

    public void compute(DataBatch databatch){

    }
}
