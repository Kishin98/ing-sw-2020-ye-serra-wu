package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.game.WorkerActionType;
import it.polimi.ingsw.views.utils.Coordinates;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ActionDisplay extends ServerMessage implements Serializable {


    private Map<WorkerActionType, List<Coordinates>> actions;


    public ActionDisplay(Map<WorkerActionType, List<Coordinates>> actions){
        this.actions = actions;
    }


    @Override
    public void displayMessage() {
        System.out.println("Here is what you can do:");
        for (WorkerActionType action : this.actions.keySet()) {
            if(action == WorkerActionType.MOVE) {
                System.out.println(action + ": move x,y");
                System.out.println("Available spaces:");
                if(this.actions.get(action).isEmpty()){
                    System.out.println("There's no available spaces...");
                }
                else{
                    this.actions.get(action).forEach(coordinates -> System.out.println("x: [" + coordinates.getX() + "] y: [" + coordinates.getY() + "]"));
                }
            }
            else if(action == WorkerActionType.BUILD) {
                System.out.println(action + ": build x,y");
                System.out.println("Available spaces:");
                if(this.actions.get(action).isEmpty()){
                    System.out.println("There's no available spaces...");
                }
                else{
                    this.actions.get(action).forEach(coordinates -> System.out.println("x: [" + coordinates.getX() + "] y: [" + coordinates.getY() + "]"));
                }
            }
            else if(action == WorkerActionType.BUILD_DOME) {
                System.out.println(action + ": dome x,y");
                System.out.println("Available spaces:");
                if(this.actions.get(action).isEmpty()){
                    System.out.println("There's no available spaces...");
                }
                else{
                    this.actions.get(action).forEach(coordinates -> System.out.println("x: [" + coordinates.getX() + "] y: [" + coordinates.getY() + "]"));
                }
            }
            else if(action == WorkerActionType.END_TURN){
                System.out.println(action + ": end");
            }
        }
    }
}
