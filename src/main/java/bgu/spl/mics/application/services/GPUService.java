package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TestModelEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TrainModelEvent;
import bgu.spl.mics.application.objects.GPU;

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



        if(gpu.getCountPDB()==gpu.getUnProcessedDataBatch().length) {
            gpu.Finish();
        }
    }

    public GPU getGpu() {
        return gpu;
    }
}
