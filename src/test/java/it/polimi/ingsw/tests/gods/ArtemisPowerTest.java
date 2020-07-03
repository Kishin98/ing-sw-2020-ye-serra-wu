package it.polimi.ingsw.tests.gods;

import it.polimi.ingsw.NotExecutedException;
import it.polimi.ingsw.controller.game.WorkerActionType;
import it.polimi.ingsw.models.game.*;
import it.polimi.ingsw.models.game.gods.GodFactory;
import it.polimi.ingsw.models.game.gods.GodType;
import it.polimi.ingsw.tests.TestGameController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ArtemisPowerTest {
    Game game;
    TestGameController controller;
    Player player1;
    Player player2;

    @BeforeEach
    public void init() {
        List<String> names = List.of("player 1","player2");
        controller = new TestGameController(names);
        game = new Game(names);
        game.findPlayerByName("player2").setGod(new GodFactory().getGod(GodType.ARTEMIS));
        game.setStatus(GameStatus.PLAYING);
        game.setCurrentPlayer(1);
        player1 = game.getCurrentPlayer();
        spaceSetup();
        game.getCurrentPlayer().selectWorker(0);
        game.getCurrentPlayer().getAllWorkers().get(0).setStartPosition(game.getWorld().get(1,1));
        game.getCurrentPlayer().getAllWorkers().get(1).setStartPosition(game.getWorld().get(3,2));
        game.clearPreviousWorlds();
    }

    @Test
    @DisplayName("artemis power test")
    public void artemisPowerTest() throws NotExecutedException {
        game.getCurrentPlayer().selectWorker(0);
        game.getCurrentPlayer().getAllWorkers().get(0).move(game.getWorld().get(0,0));
        assertTrue(game.getCurrentPlayer().getAllWorkers().get(0).computeAvailableSpaces().contains(game.getWorld().get(0,1)));
        assertFalse(game.getCurrentPlayer().getAllWorkers().get(0).computeAvailableSpaces().contains(game.getWorld().get(1,1)));
    }


    void spaceSetup() {
        World world = game.getWorld();
        world.update(world.get(1, 1).addLevel());//[1][1] level 1
        for (int i = 0; i < 3; i++) world.update(world.get(2, 1).addLevel()); //[2][1] level 3
        for (int i = 0; i < 2; i++) world.update(world.get(2, 2).addLevel()); //[2][2] level 2
        for (int i = 0; i < 3; i++) world.update(world.get(1, 2).addLevel()); //[1][2] level 3 with dome
        world.update(world.get(1, 2).setDome());
    }
}
