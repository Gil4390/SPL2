package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.objects.Model;

public class TrainModelEvent implements Event<Model> {

    private int senderID;
    private Future<Model> future;
    private Model model;

    public TrainModelEvent(int senderID) {
        this.senderID = senderID;
    }

    public String getType(){
        return "Model";
    }

    public Future<Model> getFuture() {
        return future;
    }

    public int getSenderID() {
        return senderID;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }
}
