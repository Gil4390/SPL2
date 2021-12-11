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
    private final MessageBus messageBus;

    public GPUService(GPU gpu) {
        super("GPU - " + (gpu.getId()) + " Service");
        this.gpu = gpu;
        this.messageBus = MessageBusImpl.getInstance();
        this.messageBus.register(this);

        this.messageBus.subscribeEvent(TrainModelEvent.class, this);
        this.messageBus.subscribeEvent(TestModelEvent.class, this);

        this.messageBus.subscribeBroadcast(TickBroadcast.class, this);
    }

    @Override
    protected void initialize() {
        // TODO Implement this

    }

    public void tick(){//todo
        gpu.tick();

        if(gpu.isReady()){
            try {
                Message message =  this.messageBus.awaitMessage(this);
                act(message);
            }
            catch (InterruptedException ex){

            }
            catch (IllegalArgumentException e){
                //TODO throw exception?
            }
        }
        else{
            while(gpu.getUnProcessedDataBatch()!=null && gpu.getCountPDB() < gpu.getUnProcessedDataBatch().length) {
                while (gpu.getIndexUPDB() < gpu.getCapacity()) {
                    gpu.SendDataBatch();
                }
                gpu.TrainModel();
            }

            if(gpu.getUnProcessedDataBatch()!=null && gpu.getCountPDB()==gpu.getUnProcessedDataBatch().length) {
                gpu.Finish();
            }
        }


    }

    public void act(Message m){
       m.act(this);
    }

    public void act(TrainModelEvent e){
        TrainModel(e.getModel());
    }

    public void act(TestModelEvent e){
        TestModel(e.getModel());
    }

    public void TrainModel(Model m){
        gpu.setReady(false);
        gpu.setModel(m);
        gpu.DivideDataBatch();


    }

    public void TestModel(Model model){
        gpu.TestModel(model);
        gpu.Finish();

        //TODO await?
    }

    public GPU getGpu() {
        return gpu;
    }
}
