package bgu.spl.mics.application.objects;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU {
    private int id;
    private final int cores;
    private Pair<DataBatch,String> databatchPair;
    private final Cluster cluster;
    private boolean ready;
    private int processedTime;
    private int endProcessedTime;

    public CPU(int numOfCores){
        cores=numOfCores;
        this.cluster=Cluster.getInstance();
        ready=true;
        processedTime=0;
    }

    public void setId(int id){
        this.id = id;
    }
    /**
     * this function receiving unProcessed data and store it.
     * also calculate the time it will take to process the data.
     * <p>
     * @param databatchPair a pair that contains databatch from the cluster and the gpu name the data came from;
     * @pre this.ready = true;
     * @pre this.databatch=null;
     * @post this.databatch != null;
     * @post this.endProcessedTime=@pre(processedTime)+(32/@pre(cores))*databatch.getProcessedTime();
     * @post ths.ready = false;
     */
    public synchronized void ReceiveUnProcessedData(Pair<DataBatch,String> databatchPair){
        if(ready) {
            this.databatchPair = databatchPair;
            ready = false;
            endProcessedTime = processedTime + (32 / cores) * databatchPair.getFirst().getProcessTime();
        }
        else{
            System.out.println("entered function ReceiveUnProcessedData when cpu ready was false");
        }
    }

    /**
     * represent a tick for the cpu.
     * if in this tick the data finished processed than send it back to cluster, and make cpu ready for next databatch
     * <p>
     * @post this.processedTime == @prethis.processedTime+1
     * @post this.processedTime <= @pre(endProcessedTime) | @post(endProcessedTime) == 0
     * @post this.databatch == @pre(databatch) | @post(databatch) == null
     */
    public void tickAndCompute(){
        processedTime++;
        if(!ready & processedTime==endProcessedTime){
            cluster.ReceiveDataFromCpu(databatchPair, this.id);
            endProcessedTime=0;
            databatchPair=null;
            ready=true;
        }
    }

    /**
     * @return true or false if the cpu is ready to receive new databatch
     */
    public boolean isReady(){
        return ready;
    }

    /**
     * @return the databatch
     */
    public DataBatch getDataBatch(){return databatchPair.getFirst();};

    /**
     * @return the processedTime, the amount of ticks that the cpu received
     */
    public int getProcessedTime(){return processedTime;};

    /**
     * @return the amount of tick that needed to process the databath
     */
    public int getEndProcessedTime(){return endProcessedTime;};

}
