package cardsharpers.models;

import cardsharpers.utils.RandomCard;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

class GamblerTest {

    static Thread gamblerThread;
    @BeforeAll
    static void setUp() {
        gamblerThread = new Thread(new Gambler(false));
    }
    @AfterEach
    void setOff() {
        gamblerThread.interrupt();
    }
    @org.junit.jupiter.api.Test
    void gamblerShouldSleepAfterMove() {
        Thread checkStateThread = new Thread(new Gambler(false)) {
            @Override
            public void run() {
                synchronized (RandomCard.RND) {
                    Assertions.assertSame(gamblerThread.getState(), Thread.State.TERMINATED);
                }
            }
        };
        gamblerThread.start();
        checkStateThread.start();
    }

    @org.junit.jupiter.api.Test
    void gamblerShouldInterruptCorrectly() {
        Thread checkStateThread = new Thread(new Gambler(false)) {
            @Override
            public void run() {
                synchronized (RandomCard.RND) {
                    gamblerThread.interrupt();
                    Assertions.assertTrue(gamblerThread.isInterrupted());
                }
            }
        };
        gamblerThread.start();
        checkStateThread.start();
    }

    @org.junit.jupiter.api.Test
    void gamblerShouldBeBlockedIfItIsNotHisMove() {
        Thread gamblerThread = new Thread(new Gambler(false));
        Thread checkBlock = new Thread(new Gambler(false)) {
            @Override
            public void run() {
                synchronized (RandomCard.RND) {
                    Assertions.assertSame(gamblerThread.getState(), State.BLOCKED);
                }
            }
        };
        checkBlock.start();
        gamblerThread.start();
    }

}