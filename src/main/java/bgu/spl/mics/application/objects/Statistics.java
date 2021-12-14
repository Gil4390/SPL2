package bgu.spl.mics.application.objects;

import java.util.Stack;

public class Statistics {
    private Stack<String> modelNames;
    private int gpu_TimeUsed;
    private int cpu_TimeUsed;
    private int numberOfDataBatchProcessedByCpu;

    public Statistics(){
        modelNames = new Stack<String>();
        gpu_TimeUsed=0;
        cpu_TimeUsed=0;
        numberOfDataBatchProcessedByCpu=0;
    }
    public void AddNumberOfDataBatchProcessedByCpu(){
        numberOfDataBatchProcessedByCpu++;
    }

    public void AddGpu_TimeUsed(){
        gpu_TimeUsed++;
    }

    public void AddCpu_TimeUsed(){
        cpu_TimeUsed++;
    }

    public Stack<String> getModelNames() {
        return modelNames;
    }

    public void AddModelName(String modelName) {
        this.modelNames.add(modelName);
    }


    public int getGpu_TimeUsed() {
        return gpu_TimeUsed;
    }

    public int getCpu_TimeUsed() {
        return cpu_TimeUsed;
    }

    public int getNumberOfDataBatchProcessedByCpu() {
        return numberOfDataBatchProcessedByCpu;
    }
}
