package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.application.objects.Model;

public class TestedBroadcast implements Broadcast {
    private Model model;
    public TestedBroadcast (Model m){
        model = m;
    }

    public Model getModel() {
        return model;
    }
}
