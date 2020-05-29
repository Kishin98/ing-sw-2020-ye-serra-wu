package it.polimi.ingsw.GameTest;

import it.polimi.ingsw.models.game.Game;
import it.polimi.ingsw.models.game.Player;
import it.polimi.ingsw.models.game.Space;
import it.polimi.ingsw.models.game.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class clearPreviusWorldsTest {
    Game game;
    Player player1;
    @BeforeEach
    void init(){
        List<String> names = List.of("player 1", "player 2");
        game = new Game(names);
        game.setCurrentPlayer(1);
        player1 = game.getCurrentPlayer();
        spaceSetup();
        Space firstWorkerPosition = game.getWorld().getSpaces(1, 1);
        Space secondWorkerPosition = game.getWorld().getSpaces(2, 4);
        player1.getAllWorkers().get(0).setStartPosition(firstWorkerPosition);
        player1.getAllWorkers().get(1).setStartPosition(secondWorkerPosition);
    }
    @Test
    @DisplayName("test pulizia stati precedenti")
    void clearPreviusWorldsTest(){
        game.savePreviousWorld();
        game.getWorld().getSpaces(2,2).setDome();
        assertFalse(worldsComparison());
        game.clearPreviousWorlds();
        assertTrue(game.getPreviousWorld()== null);

    }
    boolean worldsComparison(){
        for(int i=0 ; i<5;  i = i+1)  {
            for (int j=0; j<5; j=j+1)
            {
               if(!game.getPreviousWorld().getSpaces(i,j).equals(game.getWorld().getSpaces(i,j)))
               {
                   return false;
               }
            }
        }
        return true;
    }


    void spaceSetup(){
        World world = game.getWorld();
        world.getSpaces(1, 1).addLevel();//[1][1] level 1
        for(int i = 0; i < 3; i++) world.getSpaces(2, 1).addLevel(); //[2][1] level 3
        for(int i = 0; i < 2; i++) world.getSpaces(2, 2).addLevel(); //[2][2] level 2
        for(int i = 0; i < 3; i++) world.getSpaces(1, 2).addLevel(); //[1][2] level 3 with dome
        world.getSpaces(1, 2).setDome();
    }
}
