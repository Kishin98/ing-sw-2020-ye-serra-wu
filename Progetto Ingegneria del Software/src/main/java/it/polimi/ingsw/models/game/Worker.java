package it.polimi.ingsw.models.game;

import it.polimi.ingsw.models.game.rules.ActualRule;

import java.util.ArrayList;

public class Worker implements WorkerData {
    private Space currentSpace;
    private final Player player;
    private final World world;
    private ActualRule rules;

    public Worker(Player player) {
        this.player = player;
        this.world = this.player.getGame().getWorld();
        this.rules = this.player.getGame().getRules();
        this.currentSpace = null;
    }

    /*public void startTurn() {
        player.getGod().workerActionOrder(this);
    }*/

    /**
     * Uses ActualRule.canMoveThere to check if this worker can move in a particular space according to all the active rules
     */
    public void move(Space targetSpace) {
        this.currentSpace.removeWorker();
        if (this.currentSpace != null) {
            // probably it's better to check for victory **after** worker has moved?
            victory(targetSpace); //Check win condition
        }
        setPosition(targetSpace);
    }

    /**
     * Uses ActualRule.canBuildThere to check if this worker can build in a particular space according to all the active rules
     */
    public void buildBlock(Space targetSpace) {
        targetSpace.addLevel();
    }

    public void buildDome(Space targetSpace) {
        targetSpace.setDome();
    }

    /**
     * Uses ActualRule.winCondition to check if the player wins by moving this worker into a particular space according to all the active rules
     */
    public int getX () {
        return currentSpace.getX();
    }

    public int getY () {
        return currentSpace.getY();
    }

    public void victory(Space targetSpace) { //This method is called only after checking that the worker can move to that position
        // TODO: move victory-checking code to somewhere else
        if (this.rules.winCondition(this.currentSpace, targetSpace)) {
            this.setPosition(targetSpace);
            this.player.getGame().announceVictory(this.player); //If true the game ends
        }
    }

    private void setPosition(Space targetSpace) {
        if (this.currentSpace != null) {
            this.currentSpace.removeWorker();
        }
        this.currentSpace = targetSpace;
        targetSpace.setWorker(this);
    }

    public void setStartPosition(Space targetSpace){
        if(this.currentSpace == null /*&& !this.player.isDefeated()*/){
            this.currentSpace = targetSpace;
            targetSpace.setWorker(this);
        }
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    public World getWorld() {
        return this.world;
    }

    public ActualRule getRules() {
        return this.rules;
    }

    public ArrayList<Space> computeAvailableSpaces(){
        ArrayList<Space> availableSpaces = new ArrayList<Space>();
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                if(rules.canMoveThere(this.currentSpace, this.world.getSpaces(i, j))){
                    availableSpaces.add(this.world.getSpaces(i, j));
                }
            }
        }
        return availableSpaces;
    }


    public Space getCurrentSpace() {
        return this.currentSpace;
    }
}

