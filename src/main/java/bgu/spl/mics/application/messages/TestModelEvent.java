package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.objects.Model;

public class TestModelEvent implements Event<Model> {

    private String senderName;
    private Future<Model> future;
    private Model model;

    public TestModelEvent(String senderName) {
        this.senderName = senderName;
    }

    public String getType(){
        return "Model";
    }

    public Future<Model> getFuture() {
        return future;
    }

    public String getSenderName() {
        return senderName;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }
}
