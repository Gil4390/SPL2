package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Pair;
import bgu.spl.mics.application.objects.Student;

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

    private Student student;

    public StudentService(Student student) {
        super("Student - " + student.getId() + " Service");
        this.student = student;

    }

    @Override
    protected void initialize() {
        subscribeBroadcast(PublishConferenceBroadcast.class, this::PublishConferenceBroadcast);
        subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast)->{terminate();});
        act();
    }

    public void act() {
        for (Model m : this.student.getModels()) {
            Model model = TrainModel(m);
            this.student.getTrainedModels().add(model);
            if (TestModel(model)) {
                PublishResults(model);
            }
            System.out.println("student: "+ student.getId()+", model:"+m.getName()+", was tested and return the result: "+ model.getResultString());
        }
    }

    public Model TrainModel(Model model){
        TrainModelEvent trainEvent = new TrainModelEvent(this.student.getId(), model);
        return sendEvent(trainEvent).get();
    }

    public Boolean TestModel(Model model){
        TestModelEvent testEvent = new TestModelEvent(this.student.getId(), model);
        return sendEvent(testEvent).get();
    }

    public void PublishResults(Model model){
        PublishResultsEvent publishEvent = new PublishResultsEvent(this.student.getId(), model.getName());
        sendEvent(publishEvent);
    }

    private void PublishConferenceBroadcast(PublishConferenceBroadcast event){
        for (Pair<String,Integer> pair:event.getModels()) {
            if(student.getId()==pair.getSecond())
                student.setPublications(student.getPublications()+1, pair.getFirst());
            else{
                student.setPapersRead(student.getPapersRead()+1);
            }
        }
    }
}
