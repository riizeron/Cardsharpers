package cardsharpers;

import cardsharpers.models.Game;

import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Game game;
        int honestAmount;
        int lyingAmount;
        boolean endFlag;
        boolean logFlag;

        // Continuous play loop
        do {
            System.out.print("Input amount of honest gamblers: ");
            honestAmount = scanner.nextInt();
            System.out.print("Input amount of spielers: ");
            lyingAmount = scanner.nextInt();
            System.out.print("Would you like to see the process(yes or smth else): ");
            logFlag = Objects.equals(scanner.next(), "yes");
            game = new Game(honestAmount, lyingAmount, logFlag);
            game.start();

            endFlag = false;
            System.out.println("Would you like to start another batch?");
            System.out.println("(print yes if you want)");
            System.out.print(":");
            if (Objects.equals(scanner.next(), "yes")) {
                endFlag = true;
                System.out.println(System.lineSeparator() + System.lineSeparator());
            }
        } while (endFlag);
        System.out.println("End.");
    }
}
