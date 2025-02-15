package it.polimi.ingsw;

import it.polimi.ingsw.controller.lobby.LobbyController;
import it.polimi.ingsw.controller.lobby.LocalLobbyController;
import it.polimi.ingsw.views.game.MultiUserConsoleGameView;
import it.polimi.ingsw.views.lobby.MultiUserConsoleLobbyView;

import java.io.IOException;

/**
 *
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        try {
            System.out.println("Creating lobby controller and lobby view...");
            LobbyController lobbyController = new LocalLobbyController();
            MultiUserConsoleLobbyView lobbyView = new MultiUserConsoleLobbyView(lobbyController);
            MultiUserConsoleGameView gameView = lobbyView.getUserInputUntilGameStarts();
            // game created...
            System.out.println("Game should be created now ");
            gameView.play();
        }
        catch (NotExecutedException exception) {
            System.out.println("Error: " + exception);
        }
    }
}
