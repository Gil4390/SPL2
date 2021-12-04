package bgu.spl.mics.application.objects;


import java.util.PriorityQueue;

/**
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Cluster {


	private PriorityQueue<CPU> CPUs;
	private PriorityQueue<GPU> GPUs;

	private PriorityQueue<DataBatch> dataBATCH_ForCPU;
	private PriorityQueue<DataBatch> dataBATCH_ForGPU;
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

	public PriorityQueue<DataBatch> getDataBATCH_ForCPU() {
		return dataBATCH_ForCPU;
	}
}
