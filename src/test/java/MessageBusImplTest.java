import bgu.spl.mics.Event;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
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
        mBus = MessageBusImpl.getInstance();
        e = new ExampleEvent("test");
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
        mBus.subscribeEvent(e.getClass() ,m);
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

        mBus.subscribeBroadcast(b.getClass() ,m);

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
        mBus.register(m);
        mBus.subscribeBroadcast(b.getClass(),m);
        Queue <MicroService> q = mBus.getEventsBroadcast_subscribe().get(b.getType());
        MicroService temp = q.peek();
        Queue temp2 = mBus.getMicroService_queues().get(temp.getName());
        assertEquals(0,temp2.size());
        mBus.sendBroadcast(b);
        assertEquals(1,temp2.size());
    }

    @Test
    void sendEvent() {
        mBus.register(m);
        mBus.subscribeEvent(e.getClass(),m);
        Queue <MicroService> q = mBus.getEventsBroadcast_subscribe().get(b.getType());
        MicroService temp = q.peek();
        Queue temp2 = mBus.getMicroService_queues().get(temp.getName());
        assertEquals(0,temp2.size());
        mBus.sendEvent(e);
        assertEquals(1,temp2.size());
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
        try{
            mBus.awaitMessage(m);
            assertTrue(false);
        }
        catch (Exception e){
            assertTrue(e.equals(new InterruptedException()));
        }

        mBus.register(m);
        Thread t = new Thread(){
            public void run(){
                try{
                    mBus.awaitMessage(m);
                }
                catch (Exception e){
                    assertTrue(false);
                }
            }
        };
        t.run();

        try {
            Thread.sleep(1000);
            assertEquals(t.getState(),Thread.State.WAITING);
            mBus.subscribeEvent(e.getClass(),m);
            mBus.sendEvent(e);
            Thread.sleep(10);
            assertNotEquals(t.getState(),Thread.State.WAITING);
        }
        catch (Exception e){
        }
    }

    @Test
    void isSubscribed() {
        assertFalse(mBus.isSubscribed(m,e));
        mBus.subscribeEvent(e.getClass(),m);
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