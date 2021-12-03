package bgu.spl.mics.application.objects;

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

    /**
     * @param databatch the databatch from the cluster;
     * @pre this.ready = true;
     * @post this.databatch != null;
     * @post this.endProcessedTime=@pre(processedTime)+(32/@pre(cores))*databatch.getProcessedTime();
     * @post ths.ready = false;
     */
    public void ReceiveUnProcessedData(DataBatch databatch){
        this.databatch=databatch;
        ready=false;
        endProcessedTime=processedTime+(32/cores)*databatch.getProcessTime();
    }

    /**
     * @post this.processedTime == @prethis.processedTime+1
     * @post this.processedTime <= @pre(endProcessedTime) | @post(endProcessedTime) == 0
     * @post this.databatch == @pre(databatch) | @post(databatch) == null
     */
    public void tickAndCompute(){
        processedTime++;
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
