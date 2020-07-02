package it.polimi.ingsw.controller.game.remote;

import it.polimi.ingsw.NotExecutedException;
import it.polimi.ingsw.controller.game.GameController;
import it.polimi.ingsw.controller.game.WorkerActionType;
import it.polimi.ingsw.models.game.GameStatus;
import it.polimi.ingsw.models.game.Vector2;
import it.polimi.ingsw.models.game.WorkerData;
import it.polimi.ingsw.models.game.gods.GodType;
import it.polimi.ingsw.views.game.GameView;

import java.io.IOException;
import java.io.Serializable;

enum RemoteCommandType {
    JOIN_GAME,

}

interface RemoteCommand extends Serializable {
    void apply(GameController controller)
            throws NotExecutedException, IOException;
}

/**
 * A special command which corresponds to
 * {@link GameController#joinGame(String, GameView)}
 */
final class JoinGameCommand implements Serializable {
    private final String nickname;

    public JoinGameCommand(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}

/**
 * A command which corresponds to
 * {@link GameController#workerAction(WorkerData, WorkerActionType, int, int)}
 */
final class WorkerAction implements RemoteCommand {
    private final WorkerData worker;
    private final WorkerActionType type;
    private final Vector2 position;

    public WorkerAction(WorkerData worker, WorkerActionType type, Vector2 position) {
        this.worker = worker;
        this.type = type;
        this.position = position;
    }
    @Override
    public void apply(GameController controller)
            throws NotExecutedException, IOException {
        controller.workerAction(worker, type, position.getX(), position.getY());
    }
}

/**
 * A command which can be used for
 * {@link GameController#addAvailableGods(GodType)} and
 * {@link GameController#removeAvailableGod(GodType)}
 */
final class GodCommand implements RemoteCommand {
    public enum Type {
        ADD,
        REMOVE
    }
    private final Type type;
    private final GodType god;

    public GodCommand(Type type, GodType god) {
        this.type = type;
        this.god = god;
    }

    @Override
    public void apply(GameController controller)
            throws NotExecutedException, IOException {
        switch (type) {
            case ADD -> controller.addAvailableGods(god);
            case REMOVE -> controller.removeAvailableGod(god);
        }
    }
}

/**
 * A command which is used for
 * {@link GameController#setPlayerGod(String, GodType)}
 */
final class ChooseGodCommand implements RemoteCommand {
    private final String player;
    private final GodType god;

    public ChooseGodCommand(String player, GodType god) {
        this.player = player;
        this.god = god;
    }
    @Override
    public void apply(GameController controller)
            throws NotExecutedException, IOException {
        controller.setPlayerGod(player, god);
    }
}

/**
 * A command which corresponds to
 * {@link GameController#setGameStatus(GameStatus)}
 */
final class GameStatusCommand implements RemoteCommand {
    private final GameStatus newStatus;

    public GameStatusCommand(GameStatus newStatus) {
        this.newStatus = newStatus;
    }

    @Override
    public void apply(GameController controller)
            throws NotExecutedException, IOException {
        controller.setGameStatus(newStatus);
    }
}

/**
 * Can be used to represent all
 * {@link GameController}'s commands without a parameter.
 * For example {@link GameController#nextTurn()},
 * {@link GameController#resetTurn()} and
 * {@link GameController#undo()}
 */
final class MiscellaneousCommand implements RemoteCommand {
    public enum Type {
        NEXT_TURN,
        RESET_TURN,
        UNDO
    }

    private final Type type;

    public MiscellaneousCommand(Type type) {
        this.type = type;
    }

    @Override
    public void apply(GameController controller) throws NotExecutedException, IOException {
        switch (type) {
            case RESET_TURN -> controller.resetTurn();
            case NEXT_TURN -> controller.nextTurn();
            case UNDO -> controller.undo();
        }
    }
}

/**
 * A command which correspons to {@link GameController#setCurrentPlayer(int)}
 */
final class SetCurrentPlayerCommand implements RemoteCommand {
    private final int index;

    public SetCurrentPlayerCommand(int index) {
        this.index = index;
    }

    @Override
    public void apply(GameController controller) throws NotExecutedException, IOException {
        controller.setCurrentPlayer(index);
    }
}

/**
 * A command which correspons to {@link GameController#selectWorker(int)} (int)}
 */
final class SelectWorkerCommand implements RemoteCommand {
    private final int index;

    public SelectWorkerCommand(int index) {
        this.index = index;
    }

    @Override
    public void apply(GameController controller)
            throws NotExecutedException, IOException {
        controller.selectWorker(index);
    }
}