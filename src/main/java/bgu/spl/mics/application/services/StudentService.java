package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.Model;

/**
 * Student is responsible for sending the {@link TrainModelEvent},
 * {@link TestModelEvent} and {@link PublishResultsEvent}.
 * In addition, it must sign up for the conference publication broadcasts.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class StudentService extends MicroService {

    private final MessageBus messageBus;
    private int studentID;

    public StudentService(int id) {
        super("Student - " + id + " Service");
        this.studentID = id;
        this.messageBus = MessageBusImpl.getInstance();
        this.messageBus.register(this);

        this.messageBus.subscribeBroadcast(PublishConferenceBroadcast.class, this);
    }

    @Override
    protected void initialize() {
        // TODO Implement this
    }

    public Model TrainModel(Model model){
        TrainModelEvent trainEvent = new TrainModelEvent(this.studentID);
        messageBus.sendEvent(trainEvent);
        return trainEvent.getFuture().get();
    }

    public Boolean TestModel(Model model){
        TestModelEvent testEvent = new TestModelEvent(this.studentID);
        messageBus.sendEvent(testEvent);
        return testEvent.getFuture().get();
    }

    public Future<Model> PublishResults(Model model){
        PublishResultsEvent publishEvent = new PublishResultsEvent(this.studentID);
        return publishEvent.getFuture();
    }
}
