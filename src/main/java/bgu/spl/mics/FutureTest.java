package bgu.spl.mics;

import bgu.spl.mics.application.objects.Model;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class FutureTest {

    private Future f;

    @BeforeEach
    void setUp() {
        f = new Future<String>(null);
    }

    @Test
    void get() {
        assertNull(f.get());
        String str = "a";
        f.resolve(str);
        assertEquals(f.get(),str);
    }

    @Test
    void resolve() {
        assertNull(f.get());
        assertFalse(f.isDone());
        String str = "a";
        f.resolve(str);
        assertEquals(f.get(),str);
        assertTrue(f.isDone());
    }

    @Test
    void isDone() {
        assertFalse(f.isDone());
        String str = "a";
        f.resolve(str);
        assertTrue(f.isDone());
    }

    @Test
    void get2() {
        long Timeout = 3000;
        TimeUnit unit = TimeUnit.MILLISECONDS;

        assertNull(f.get(Timeout,unit));
        Thread k = new Thread(){
            public void run() {
                assertEquals("test",f.get(Timeout,unit));
            }
        };
        Thread t = new Thread() {
            public void run() {
                try {
                    unit.sleep(Timeout / 2);
                } catch (
                        InterruptedException e) {
                }
                f.resolve("test");
            }
        };
        k.run();
        t.run();

        f=new Future<String>(null);
        k = new Thread(){
            public void run() {
                assertNull(f.get(Timeout,unit));
            }
        };
        k.run();
    }
}