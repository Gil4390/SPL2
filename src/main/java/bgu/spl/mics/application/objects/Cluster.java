package bgu.spl.mics.application.objects;


import java.util.Collection;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Cluster {


	private HashMap<Integer,Pair<CPU, PriorityQueue<Pair<DataBatch,Integer>>>> CPUs;
	private HashMap<Integer,GPU> GPUs;

	private PriorityQueue<Pair<DataBatch,Integer>> dataBATCH_ForGPU;

	private Stack<String> modelNames;

	private int cpuRoundIndex;

	private Statistics statistics;
	private Cluster(){
		statistics= new Statistics();
		cpuRoundIndex=0;
	}
	/**
     * Retrieves the single instance of this class.
     */

	private static class clusterHolder{
		private static Cluster clusterInstance = new Cluster();
	}

	public static Cluster getInstance() {
		return clusterHolder.clusterInstance;
	}

	public void ReceiveDataFromCpu(Pair<DataBatch,Integer> dataBatchPair, int cpuID){
		GPU tempGPU= GPUs.get(dataBatchPair.getSecond());
		statistics.AddNumberOfDataBatchProcessedByCpu();
		synchronized(tempGPU) {
			tempGPU.ReceiveProcessedData(dataBatchPair.getFirst());
			if (!CPUs.get(cpuID).getSecond().isEmpty())
				CPUs.get(cpuID).getFirst().ReceiveUnProcessedData(CPUs.get(cpuID).getSecond().remove());
		}
	}
	public void ReceiveDataFromGpu(Pair<DataBatch,Integer> dataBatchPair){//todo need to do a better thread save function
		Pair<CPU, PriorityQueue<Pair<DataBatch,Integer>>> temp =CPUs.get(cpuRoundIndex);
		synchronized(temp) {
			temp.getSecond().add(dataBatchPair);
			if (!CPUs.get(cpuRoundIndex).getSecond().isEmpty() & CPUs.get(cpuRoundIndex).getFirst().isReady())
				CPUs.get(cpuRoundIndex).getFirst().ReceiveUnProcessedData(CPUs.get(cpuRoundIndex).getSecond().remove());
			if (CPUs.size() == cpuRoundIndex)
				cpuRoundIndex = 0;
			else {
				cpuRoundIndex++;// todo need to solve the problem of thread here
			}
		}
	}

	public Collection<Object> getDataBATCH_ForCPU() {//todo implement this
		return null;
	}

	public void finishTrainModel(String modelName){
		modelNames.add(modelName);
	}

	public void AddCPUS(Vector<CPU> cpus){
		for (CPU cpu:cpus) {
			Pair pair = new Pair(cpu, new PriorityQueue<Pair<DataBatch,Integer>>());
			CPUs.put(cpu.getId(),pair);
		}
	}
	public void AddGPUS(Vector<GPU> gpus){
		for (GPU gpu: gpus) {
			GPUs.put(gpu.getId(),gpu);
		}
	}

	public Stack<String> getModelNames() {
		return modelNames;
	}
	public Statistics getStatistics() {return statistics;}
}
