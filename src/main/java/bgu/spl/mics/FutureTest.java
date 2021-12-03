package bgu.spl.mics;

import bgu.spl.mics.application.objects.Model;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FutureTest {

    private Future f;

    @BeforeEach
    void setUp() {
        f = new Future<>(null);
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
        String str = "a";
        f.resolve(str);
        assertEquals(f.get(),str);
        f.resolve(null);
        assertNull(f.get());
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
        //TODO
    }
}