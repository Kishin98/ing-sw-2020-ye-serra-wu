package it.polimi.ingsw.views.game.remote;

import it.polimi.ingsw.models.game.GameStatus;
import it.polimi.ingsw.models.game.Space;
import it.polimi.ingsw.models.game.gods.GodType;
import it.polimi.ingsw.views.game.GameView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

interface Message extends Serializable {
    void apply(GameView view);
}

class GameStatusMessage implements Message {
    private final GameStatus status;

    public GameStatusMessage(GameStatus status) {
        this.status = status;
    }

    @Override
    public void apply(GameView view) {
        view.notifyGameStatus(status);
    }
}

class AvailableGodsMessage implements Message {
    private final ArrayList<GodType> availableGods;

    public AvailableGodsMessage(Collection<GodType> availableGods) {
        this.availableGods = new ArrayList<>(availableGods);
    }

    @Override
    public void apply(GameView view) {
        view.notifyAvailableGods(availableGods);
    }
}

class PlayerGodsMessage implements Message {
    private final HashMap<String, GodType> playerGods;

    public PlayerGodsMessage(Map<String, GodType> playerGods) {
        this.playerGods = new HashMap<>(playerGods);
    }

    @Override
    public void apply(GameView view) {
        view.notifyPlayerGods(playerGods);
    }
}

class SpaceChangedMessage implements Message {
    private final Space space;

    public SpaceChangedMessage(Space space) {
        this.space = space;
    }

    @Override
    public void apply(GameView view) {
        view.notifySpaceChange(space);
    }
}

class PlayerTurnMessage implements Message {
    private final String player;

    public PlayerTurnMessage(String player) {
        this.player = player;
    }

    @Override
    public void apply(GameView view) {
        view.notifyPlayerTurn(player);
    }
}

class PlayerDefeatedMessage implements Message {
    private final String player;

    public PlayerDefeatedMessage(String player) {
        this.player = player;
    }

    @Override
    public void apply(GameView view) {
        view.notifyPlayerDefeat(player);
    }
}