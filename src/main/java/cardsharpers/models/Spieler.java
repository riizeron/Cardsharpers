package cardsharpers.models;

import cardsharpers.utils.RandomCard;

import java.util.List;

import static java.lang.Math.min;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

/**
 * Spieler, in other words, cardsharper class.
 */
public class Spieler extends Gambler {

    // A list of players from whom he can steal cards.
    private final List<Gambler> gamblers;

    /**
     * Spieler constructor.
     *
     * @param gamblers A list of players from whom he can steal cards.
     * @param logFlag  A flag for "is commenting batch necessary".
     */
    public Spieler(List<Gambler> gamblers, boolean logFlag) {
        super(logFlag);
        this.gamblers = gamblers;
    }

    /**
     * Overriding run method form Runnable interface.
     * Inside spieler makes a move and decides whether to steal or not.
     * Then he sleeps for a defined time.
     * If he stole then he sleeps from 180 to 301 milliseconds.
     * If not then from 100 to 201.
     */
    @Override
    public void run() {
        Gambler aim;
        int card;
        String log;
        int stealCount;
        boolean isStealer;
        while (!currentThread().isInterrupted()) {
            isStealer = false;
            // Since the threads are synchronized on the deck of cards,
            // they wait for their turn until the resource is released,
            // that is, until another player makes it.
            synchronized (RandomCard.RND) {
                // With Random object I want to replace the probability.
                // If random generated from  0 to 10 number will be lower than 4,
                // spieler will steal.
                if (!gamblers.isEmpty() && RandomCard.RND.nextInt(10) < 4) {
                    isStealer = true;
                    log = toString();
                    aim = gamblers.get(RandomCard.RND.nextInt(gamblers.size()));
                    log += " stole from " + aim + " ";
                    stealCount = min(aim.getBalance(), RandomCard.RND.nextInt(1, 9));
                    log += stealCount + " points. ";
                    aim.lowBalance(stealCount);
                    balance += stealCount;
                    log += "So he became " + this + '.';
                    if (logFlag)
                        System.out.println(log);
                } else {
                    log = toString();
                    card = RandomCard.RND.nextInt(1, 11);
                    balance += card;
                    log += " take a " + card + " from the deck. So he became: " + this + '.';
                    if (logFlag)
                        System.out.println(log);
                }
            }
            if (isStealer) {
                try {
                    sleep(RandomCard.RND.nextInt(180, 301));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } else {
                try {
                    sleep(RandomCard.RND.nextInt(100, 201));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Spieler{" +
                "balance=" + balance +
                '}';
    }
}
