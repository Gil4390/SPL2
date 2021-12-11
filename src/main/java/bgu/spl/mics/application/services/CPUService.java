package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TestModelEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.CPU;

/**
 * CPU service is responsible for handling the {@link DataPreProcessEvent}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class CPUService extends MicroService {

    private CPU cpu;
    private final MessageBus messageBus;

    public CPUService(CPU cpu) {
        super("CPU - " + (cpu.getId()) + " Service");
        this.cpu=cpu;
        this.messageBus = MessageBusImpl.getInstance();
        this.messageBus.register(this);

        this.messageBus.subscribeBroadcast(TickBroadcast.class, this);
    }

    public void tick(){cpu.tickAndCompute();}

    @Override
    protected void initialize() {
        // TODO Implement this

    }
}
