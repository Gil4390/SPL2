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
    private PriorityQueue<String> succesfulModels;
    int date;

    public ConferenceService(ConfrenceInformation conf) {
        super("Conference - " + conf.getName() + " Service");
        this.conf = conf;
        this.succesfulModels = new PriorityQueue<>();
        date=0;
    }

    @Override
    protected void initialize() {
        subscribeEvent(PublishResultsEvent.class, (PublishResultsEvent)->{PublishResults(PublishResultsEvent.getModelName());});
        subscribeBroadcast(TickBroadcast.class, (TickBroadcast)->{PublishConference();});
    }

    public void PublishResults(String modelName){
        this.succesfulModels.add(modelName);
    }

    public void PublishConference(){
        date++;
        if(date==conf.getDate()) {
            PublishConferenceBroadcast publish = new PublishConferenceBroadcast(this.succesfulModels);
            sendBroadcast(publish);
            this.terminate();
        }
    }
}
