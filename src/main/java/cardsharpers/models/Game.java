package cardsharpers.models;

import java.util.*;
import java.util.stream.Stream;

/**
 * Game or batch class.
 */
public class Game {
    private final List<Spieler> spielers;
    private final List<Gambler> gamblers;

    /**
     * Game constructor.
     *
     * @param honestAmount Amount of honest gamblers.
     * @param lyingAmount  Amount of spielers.
     * @param logFlag      "Is commenting batch necessary" flag.
     */
    public Game(int honestAmount, int lyingAmount, boolean logFlag) {
        gamblers = new ArrayList<>(honestAmount);
        for (int i = 0; i < honestAmount; i++) {
            gamblers.add(new Gambler(logFlag));
        }
        spielers = new ArrayList<>(lyingAmount);
        for (int i = 0; i < lyingAmount; i++) {
            spielers.add(new Spieler(gamblers, logFlag));
        }
    }

    /**
     * Launches the batch method.
     */
    public void start() {
        Croupier croupier = new Croupier(this);
        Thread croupierThread = new Thread(croupier);
        croupierThread.start();
        try {
            croupierThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public List<Gambler> getGamblers() {
        return gamblers;
    }

    public List<Spieler> getSpielers() {
        return spielers;
    }

    @Override
    public String toString() {
        String str = System.lineSeparator() + "Game results: " + System.lineSeparator();
        for (Gambler gambler : Stream.concat(gamblers.stream(), spielers.stream()).sorted().toList()) {
            str += gambler + System.lineSeparator();
        }

        // It is the second way to find gambler with max balance.
        // I used it first, but then I prefer to implement Comparable interface for Gambler class.
        /*Comparator<Gambler> comparator = Comparator.comparing(Gambler::getBalance);
        Gambler winner = Stream.concat(gamblers.stream(), spielers.stream()).max(comparator).get();*/

        // Find gambler with max balance on unity of two lists.
        // Based on implementation of Comparable interface.
        // But this way can find only one winner.
        // So it is cannot be applied by us.
        /*Gambler winner = Collections.max(Stream.concat(gamblers.stream(), spielers.stream()).toList());*/

        // So this is Christmas!
        // Final way to find all winners.
        // Streams are not banned I suppose))))))
        int maxBalance = Stream.concat(gamblers.stream(), spielers.stream())
                .mapToInt(Gambler::getBalance)
                .max()
                .orElse(-1);
        List<Gambler> winners = Stream.concat(gamblers.stream(), spielers.stream())
                .filter(g -> g.getBalance() == maxBalance).toList();

        str += "So the winner is: ";
        for (Gambler elem : winners) {
            str += elem + System.lineSeparator();
        }
        return str;
    }
}
