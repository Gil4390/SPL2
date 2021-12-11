package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.PublishConferenceBroadcast;
import bgu.spl.mics.application.messages.PublishResultsEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TrainModelEvent;
import bgu.spl.mics.application.objects.ConfrenceInformation;

import java.util.PriorityQueue;

/**
 * Conference service is in charge of
 * aggregating good results and publishing them via the {@link PublishConfrenceBroadcast},
 * after publishing results the conference will unregister from the system.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ConferenceService extends MicroService {

    private ConfrenceInformation conf;
    private final MessageBus messageBus;
    private PriorityQueue<String> succesfulModels;

    public ConferenceService(ConfrenceInformation conf) {
        super("Conference - " + conf.getName() + " Service");
        this.conf = conf;
        this.messageBus = MessageBusImpl.getInstance();
        this.messageBus.register(this);
        this.succesfulModels = new PriorityQueue<>();

        this.messageBus.subscribeEvent(PublishResultsEvent.class, this);

        this.messageBus.subscribeBroadcast(TickBroadcast.class, this);
    }

    @Override
    protected void initialize() {
        // TODO Implement this

    }

    public void PublishResults(String modelName){
        this.succesfulModels.add(modelName);
    }

    public void PublishConference(){
        PublishConferenceBroadcast publish = new PublishConferenceBroadcast(this.succesfulModels);
        this.messageBus.sendBroadcast(publish);

        this.messageBus.unregister(this);
    }
}
