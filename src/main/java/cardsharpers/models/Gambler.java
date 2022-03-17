package cardsharpers.models;

import cardsharpers.utils.RandomCard;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

/**
 * Gambler.
 * The mediocre honest player class.
 * Never steals.
 */
public class Gambler implements Runnable, Comparable<Gambler> {
    protected int balance;
    protected final boolean logFlag;

    /**
     * Gambler constructor.
     *
     * @param logFlag A flag for "is commenting batch necessary".
     */
    public Gambler(boolean logFlag) {
        this.logFlag = logFlag;
        balance = 0;
    }

    /**
     * Overriding run method form Runnable interface.
     * Inside gambler makes a move.
     * Then he sleeps from 100 to 201.
     */
    @Override
    public void run() {
        String log;
        int card;
        while (!currentThread().isInterrupted()) {
            // Since the threads are synchronized on the deck of cards,
            // they wait for their turn until the resource is released,
            // that is, until another player makes it.
            synchronized (RandomCard.RND) {
                log = toString();
                card = RandomCard.RND.nextInt(1, 11);
                balance += card;
                log += " take a " + card + " from deck. So he became " + this + ".";
                if (logFlag)
                    System.out.println(log);
            }
            try {
                sleep(RandomCard.RND.nextInt(100, 201));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Getter for balance.
     *
     * @return current balance state.
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Setter for balance.
     *
     * @param balance value that we must set.
     */
    protected void setBalance(int balance) {
        this.balance = balance;
    }

    /**
     * Balance reduction method.
     *
     * @param change value that we should subtract from balance.
     */
    protected void lowBalance(int change) {
        this.balance -= change;
    }

    @Override
    public String toString() {
        return "Gambler{" +
                "balance=" + balance +
                '}';
    }

    @Override
    public int compareTo(Gambler o) {
        return Integer.compare(balance, o.balance);
    }
}
