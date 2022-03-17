package cardsharpers.models;

import cardsharpers.utils.RandomCard;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Croupier class.
 * He is responsible for starting and interrupting the game,
 * for publishing the results and announcing the winners.
 */
public class Croupier implements Runnable {
    Game game;

    /**
     * Croupier constructor.
     * Inside we associated the game with croupier.
     *
     * @param game The card batch.
     */
    public Croupier(Game game) {
        this.game = game;
    }

    /**
     * Overriding run method form Runnable interface.
     * Inside croupier announce the start of the game.
     * Then, after 10 seconds sleep, he interrupts it,
     * publishes the results and announces the winners.
     */
    @Override
    public void run() {
        List<Thread> threads = new ArrayList<>();
        game.getGamblers().forEach(g -> threads.add(new Thread(g)));
        game.getSpielers().forEach(s -> threads.add(new Thread(s)));
        // Synchronized in order to gambler and spielers threads
        // start before croupier is sleep.
        synchronized (RandomCard.RND) {
            threads.forEach(Thread::start);
        }
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        threads.forEach(Thread::interrupt);
        threads.forEach((thread) -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        System.out.println(game);
    }
}
