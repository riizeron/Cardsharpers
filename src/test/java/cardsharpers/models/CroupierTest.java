package cardsharpers.models;

import cardsharpers.utils.RandomCard;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CroupierTest {
    static Thread croupierThread;
    static Thread gamblerThread;
    static Croupier croupier;
    static Game game;

    @BeforeAll
    static void setUp() {
        game = new Game(37, 18, false);
        croupier = new Croupier(game);
        croupierThread = new Thread(croupier);
        gamblerThread = new Thread(new Gambler(false));
    }

    @Test
    void threadsMustMoveWhenCroupierSleep() {
        croupierThread = new Thread(new Croupier(game)) {
            @Override
            public void run() {
                synchronized (RandomCard.RND) {
                    gamblerThread.start();
                }
                try {
                    sleep(10000);
                    assertSame(gamblerThread.getState(), State.RUNNABLE);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };
        croupierThread.start();
    }

    @Test
    void croupierThreadMustSleepWhenGamblerMove() {
        gamblerThread = new Thread(new Gambler(false)) {
            @Override
            public void run() {
                synchronized (RandomCard.RND) {
                    assertSame(croupierThread.getState(), State.TIMED_WAITING);
                }
            }
        };
        croupierThread = new Thread(new Croupier(game)) {
            @Override
            public void run() {
                synchronized (RandomCard.RND) {
                    gamblerThread.start();
                }
                try {
                    sleep(10000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };
        croupierThread.start();
    }
}