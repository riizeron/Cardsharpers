package cardsharpers.models;

import cardsharpers.utils.RandomCard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertSame;

class GameTest {
    static Thread croupierThread;
    static Thread gamblerThread;
    static Croupier croupier;
    static Game game;

    @BeforeAll
    static void setUp() {
        game = new Game(37,18,false){};
        croupierThread = new Thread(new Croupier(game));

    }
    @AfterEach
    void setOff() {
        croupierThread.interrupt();
    }


}