package it.polimi.ingsw.models.game.gods;

import it.polimi.ingsw.models.game.Space;
import it.polimi.ingsw.models.game.Worker;

/**
 * Problema con potere di APHRODITE:
 * Se un giocatore ha APOLLO e si muove su uno spazio occupato da un Worker del giocatore con APHRODITE,
 * quello spazio è considerato adiacente a APHRODITE?
 */
public class Aphrodite extends God {


    @Override
    public void activateGodPower(Worker worker) {

    }

    @Override
    public void deactivateGodPower(Worker worker) {

    }

    @Override
    public void forcePower(Worker worker, Space targetSpace) {
        throw new UnsupportedOperationException("Should be a fatal error");
    }


}
