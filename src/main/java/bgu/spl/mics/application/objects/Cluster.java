package bgu.spl.mics.application.objects;


import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;

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
	private String statistics; //TODO: decide what Data structure will hold it

	private Cluster(){

	}
	/**
     * Retrieves the single instance of this class.
     */
	public static Cluster getInstance() {
		//TODO: Implement this
		return null;
	}

	public void ReceiveDataFromCpu(Pair<DataBatch, String>dataBatchPair){
		GPUs.get(dataBatchPair.getSecond()).ReceiveProcessedData(dataBatchPair.getFirst());
	}
	public void ReceiveDataFromGpu(Pair<DataBatch, String>dataBatchPair){
		Pair<CPU, PriorityQueue<Pair<DataBatch,String>>> temp =CPUs.remove();
		temp.getSecond().add(dataBatchPair);
		CPUs.add(temp);
	}
}
