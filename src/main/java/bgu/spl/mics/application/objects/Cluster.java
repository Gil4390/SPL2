package bgu.spl.mics.application.objects;


import java.util.Collection;
import java.util.*;

/**
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Cluster {


	private PriorityQueue<Pair<CPU, PriorityQueue<Pair<DataBatch,String>>>> CPUs;
	private HashMap<String,GPU> GPUs;

	private PriorityQueue<Pair<DataBatch,String>> dataBATCH_ForGPU;
	private String statistics;

	private Stack<String> modelNames;

	private volatile static Cluster cluster;

	private int numberOfDataBatchProcessedByCpu;

	private int gpu_TimeUsed;
	private int cpu_TimeUsed;

	private Cluster(){
		numberOfDataBatchProcessedByCpu=0;
		gpu_TimeUsed=0;
		cpu_TimeUsed=0;
	}
	/**
     * Retrieves the single instance of this class.
     */
	public static Cluster getInstance() {//check if this is good thread save singleton
		if(cluster==null){
			synchronized (cluster){
				if(cluster==null) {
					cluster = new Cluster();
				}
			}
		}
		return cluster;
	}

	public void ReceiveDataFromCpu(Pair<DataBatch, String>dataBatchPair){
		numberOfDataBatchProcessedByCpu ++;
		GPUs.get(dataBatchPair.getSecond()).ReceiveProcessedData(dataBatchPair.getFirst());
	}
	public void ReceiveDataFromGpu(Pair<DataBatch, String>dataBatchPair){//check if needs to synchronized
		Pair<CPU, PriorityQueue<Pair<DataBatch,String>>> temp =CPUs.remove();
		temp.getSecond().add(dataBatchPair);
		CPUs.add(temp);
	}

	//for gpu test.
	public Collection<Object> getDataBATCH_ForCPU() {
		return null;
	}

	public synchronized void calculateTimeUnitUsed(){
		for (Pair<CPU, PriorityQueue<Pair<DataBatch,String>>> pair:CPUs) {
			cpu_TimeUsed += pair.getFirst().getProcessedTime();
		}
		Collection <GPU> collectionOfGPUs = GPUs.values();
		for (GPU gpu:collectionOfGPUs) {
			gpu_TimeUsed += gpu.getTimeClock();
		}
	}

	public void finishTrainModel(String modelName){
		modelNames.add(modelName);
	}

	public int getNumberOfDataBatchProcessedByCpu(){
		return numberOfDataBatchProcessedByCpu;
	}
	public int getCpu_TimeUsed() {
		return cpu_TimeUsed;
	}

	public int getGpu_TimeUsed() {
		return gpu_TimeUsed;
	}
	public Stack<String> getModelNames() {
		return modelNames;
	}
}
