package bgu.spl.mics.application.objects;


import java.util.Collection;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Cluster {


	private HashMap<Integer,Pair<CPU, Queue<Pair<DataBatch,Integer>>>> CPUs;
	private HashMap<Integer,GPU> GPUs;

	private Queue<Pair<DataBatch,Integer>> dataBATCH_ForGPU;

	private AtomicInteger cpuRoundIndex;

	private Statistics statistics;
	private Cluster(){//todo maby do a line of databatch and for evey cpu that get free he will poll from this line
		statistics= new Statistics();
		cpuRoundIndex=new AtomicInteger(0);

		CPUs = new HashMap<>();
		GPUs = new HashMap<>();
		dataBATCH_ForGPU = new LinkedList<>();
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
		System.out.println("received from CPU, id:" + cpuID);
		tempGPU.ReceiveProcessedData(dataBatchPair.getFirst());
		Pair<CPU, Queue<Pair<DataBatch,Integer>>> temp =CPUs.get(cpuID);
		synchronized(temp) {
			if (!temp.getSecond().isEmpty() & temp.getFirst().isReady())
				temp.getFirst().ReceiveUnProcessedData(temp.getSecond().remove());
		}
	}
	public void ReceiveDataFromGpu(Pair<DataBatch,Integer> dataBatchPair){
		Pair<CPU, Queue<Pair<DataBatch,Integer>>> temp =CPUs.get(cpuRoundIndex.intValue());
		synchronized(temp) {
			temp.getSecond().add(dataBatchPair);
			if (!temp.getSecond().isEmpty() & temp.getFirst().isReady())
				temp.getFirst().ReceiveUnProcessedData(temp.getSecond().remove());
		}
			int val;
			do{
				val=cpuRoundIndex.intValue()%CPUs.size();
			}while(!cpuRoundIndex.compareAndSet(val,(val+1)%CPUs.size()));
	}

	public void finishTrainModel(String modelName){
			statistics.AddModelName(modelName);
	}

	public void AddCPUS(Vector<CPU> cpus){
		for (CPU cpu:cpus) {
			Pair pair = new Pair(cpu, new LinkedList<Pair<DataBatch,Integer>>());
			CPUs.put(cpu.getId(),pair);
		}
	}
	public void AddGPUS(Vector<GPU> gpus){
		for (GPU gpu: gpus) {
			GPUs.put(gpu.getId(),gpu);
		}
	}

	public Statistics getStatistics() {
		synchronized (statistics) {
			return statistics;
		}
	}
}
