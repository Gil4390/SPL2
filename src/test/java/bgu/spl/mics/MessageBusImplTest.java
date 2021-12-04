package bgu.spl.mics;

import bgu.spl.mics.application.services.GPUService;
import bgu.spl.mics.application.services.messages.ExampleBroadcast;
import bgu.spl.mics.application.services.messages.ExampleEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {

    MessageBusImpl mBus;
    ExampleEvent e;
    MicroService m;
    ExampleBroadcast  b;
    @BeforeEach
    void setUp() {
        mBus=new MessageBusImpl();
        e = new <String> ExampleEvent("test");
        m = new GPUService("gpu test1");
        b = new ExampleBroadcast("1234");
    }

    @Test
    void subscribeEvent() {
        Queue<MicroService> q = mBus.getEventsBroadcast_subscribe().get(e.getType());
        MicroService result= null;
        for (int i=0; i<q.size();i++) {
            MicroService temp = q.remove();
            q.add(temp);
            if (m.getName() == temp.getName()) {
                result=temp;
            }
        }
        assertNull(result);
        mBus.subscribeEvent(e ,m);
        result= null;
        for (int i=0; i<q.size();i++) {
            MicroService temp = q.remove();
            q.add(temp);
            if (m.getName() == temp.getName()) {
                result=temp;
            }
        }
        assertNotNull(result);
    }

    @Test
    void subscribeBroadcast() {
        Queue<MicroService> q = mBus.getEventsBroadcast_subscribe().get(b.getType());
        MicroService result= null;
        for (int i=0; i<q.size();i++) {
            MicroService temp = q.remove();
            q.add(temp);
            if (m.getName() == temp.getName()) {
                result=temp;
            }
        }
        assertNull(result);

        mBus.subscribeEvent(e ,b);

        result= null;
        for (int i=0; i<q.size();i++) {
            MicroService temp = q.remove();
            q.add(temp);
            if (m.getName() == temp.getName()) {
                result=temp;
            }
        }
        assertNotNull(result);
    }

    @Test
    void complete() {
        assertFalse(e.getFuture().isDone());
        mBus.complete(e,"result done");
        assertTrue(e.getFuture().isDone());
        assertTrue(e.getFuture().get()=="result done");
    }

    @Test
    void sendBroadcast() {
    }

    @Test
    void sendEvent() {
    }

    @Test
    void register() {
        Queue<Event> q = mBus.getMicroService_queues().get(m.getName());
        assertNull(q);
        mBus.register(m);
        q = mBus.getMicroService_queues().get(m.getName());
        assertNotNull(q);
    }

    @Test
    void unregister() {
        mBus.unregister(m);
        assertNull(mBus.getMicroService_queues().get(m.getName()));
    }

    @Test
    void awaitMessage() {
    }

    @Test
    void isSubscribed() {
        assertFalse(mBus.isSubscribed(m,e));
        mBus.subscribeEvent(e,m);
        assertTrue(mBus.isSubscribed(m,e));
    }

    @Test
    void isRegistered() {
        assertFalse(mBus.isRegistered(m));
        mBus.register(m);
        assertTrue(mBus.isRegistered(m));
        mBus.unregister(m);
        assertFalse(mBus.isRegistered(m));
    }
}