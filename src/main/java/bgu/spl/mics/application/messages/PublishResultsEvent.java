package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.objects.Model;

public class PublishResultsEvent implements Event<Model> {

    private int senderID;
    private Future<Model> future;
    private String modelName;

    public PublishResultsEvent(int senderID) {
        this.senderID = senderID;
        future = null;
    }

    public String getType(){
        return "Model";
    }

    public Future<Model> getFuture() {
        return future;
    }

    public int getSenderName() {
        return senderID;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
