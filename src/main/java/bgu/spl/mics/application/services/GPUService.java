package bgu.spl.mics.application.services;

import bgu.spl.mics.Message;
import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TestModelEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TrainModelEvent;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Model;

/**
 * GPU service is responsible for handling the
 * {@link TrainModelEvent} and {@link TestModelEvent},
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class GPUService extends MicroService {

    private GPU gpu;

    public GPUService(GPU gpu) {
        super("GPU - " + (gpu.getId()) + " Service");
        this.gpu = gpu;//todo check where to active the run methode, from here or from main and then we need to create the services in mein
    }

    @Override
    protected void initialize() {
        subscribeEvent(TrainModelEvent.class, (TrainModelEvent)->{
            Model model = TrainModelEvent.getModel();
            StartTrainModel(model);});
        subscribeEvent(TestModelEvent.class, (TestModelEvent)->{Model model = TestModelEvent.getModel();
            TestModel(model);});
        subscribeBroadcast(TickBroadcast.class, (TickBroadcast)->{tick();});
    }

    private void tick(){
        gpu.tick();
        notifyAll();
    }

    private void StartTrainModel(Model m){
        gpu.setReady(false);
        gpu.setModel(m);
        gpu.DivideDataBatch();
        ContinueTrainModel();
    }

    private void ContinueTrainModel(){
        if(!gpu.isReady()){
            while(gpu.getUnProcessedDataBatch()!=null && gpu.getCountPDB() < gpu.getUnProcessedDataBatch().length) {
                while (gpu.getProcessingDataBatch().size() < gpu.getCapacity()) {
                    gpu.SendDataBatch();
                }
                gpu.TrainModel();
            }
            if(gpu.getUnProcessedDataBatch()!=null && gpu.getCountPDB()==gpu.getUnProcessedDataBatch().length) {
                gpu.Finish();
            }
            else{//todo this wait is not good, need to think how to make tick and train model run together
                try{
                    wait();
                }
                catch (InterruptedException e){}
                ContinueTrainModel();
            }
        }
    }

    private void TestModel(Model model){
        gpu.TestModel(model);
        gpu.Finish();
        //TODO need to notify that model result changed
    }

    public GPU getGpu() {
        return gpu;
    }
}
