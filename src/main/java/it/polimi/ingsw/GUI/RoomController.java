package it.polimi.ingsw.GUI;

import it.polimi.ingsw.NotExecutedException;
import it.polimi.ingsw.controller.game.GameController;
import it.polimi.ingsw.views.game.GUIGameView;
import it.polimi.ingsw.views.lobby.GUILobbyView;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

public class RoomController {

    private String username;

    private GUILobbyView view;

    private GUIClient client;

    private boolean isHost;

    private CompletableFuture<GameController> futureGame;

    public AnchorPane players;

    public Label playerLabelOne;

    public Label playerLabelTwo;

    public Label playerLabelThree;

    public Label welcomeMessage;

    public Button leaveButton;

    public Button kickButton;

    public Button startGameButton;

    public AnchorPane roomScene;

    public Label usernameLabel;

    private Label selectedPlayer;


    public void initData(String username, GUIClient client, GUILobbyView view, String hostName, CompletableFuture<GameController> futureGame) {
        this.client = client;
        this.username = username;
        this.view = view;
        this.startGameButton.setDisable(true);
        this.kickButton.setDisable(true);
        this.isHost = username.equals(hostName);
        this.futureGame = futureGame;
        this.usernameLabel.setText("Your username: " + username);
        if(this.isHost){
            welcomeMessage.setText("Welcome! You are the host of this room");
        }
        else{
            welcomeMessage.setText("Welcome! You are in " + hostName + "'s room");
            roomScene.getChildren().remove(kickButton);
            roomScene.getChildren().remove(startGameButton);
        }
        this.selectedPlayer = null;
    }


    public void playerSelected(MouseEvent mouseEvent) {
        if(isHost) {
            Label source = (Label) mouseEvent.getSource();
            if (selectedPlayer != null) {//if a player is already selected
                if (selectedPlayer.equals(source)) { //if it's the same player, deselect's it
                    source.setStyle("-fx-background-color: transparent;");
                    selectedPlayer = null;
                    kickButton.setDisable(true);
                } else { //if is another player
                    source.setStyle("-fx-background-color: rgba(99, 99, 102, 0.5);");
                    selectedPlayer.setStyle("-fx-background-color: transparent;");
                    selectedPlayer = source;
                    kickButton.setDisable(selectedPlayer.getText().equals("Empty") || selectedPlayer.getText().equals(username));
                }
            } else {//if there is not a selected player yet
                source.setStyle("-fx-background-color: rgba(99, 99, 102, 0.5);");
                selectedPlayer = source;
                kickButton.setDisable(selectedPlayer.getText().equals("Empty") || selectedPlayer.getText().equals(username));
            }
        }
    }

    public void kickPlayer(ActionEvent event) {
        this.client.viewInputExec(this.view, "kick", this.selectedPlayer.getText());
        selectedPlayer.setStyle("-fx-background-color: transparent;");
        selectedPlayer = null;
        kickButton.setDisable(true);
        if(view.getPlayersInTheRoom().size() < 2){
            startGameButton.setDisable(true);
        }
    }

    public void leaveRoom(ActionEvent event) throws IOException {
        this.client.viewInputExec(this.view, "leave", "");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/lobby.fxml"));
        Parent lobby = loader.load();

        //loader.setController(this.view.getLobbyGUIController());
        LobbyGUIController controller = loader.getController();
        controller.returnFromRoom(username, client, this.view, futureGame);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

        Scene lobbyScene = new Scene(lobby);

        window.setScene(lobbyScene);
        window.show();
    }

    public void startGame(ActionEvent event) {
        this.client.viewInputExec(this.view, "start", "");
    }

    public void updatePlayersInTheRoom() {
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        //Background work
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    if(view.getPlayersInTheRoom().size() == 1){
                                        playerLabelOne.setText(view.getPlayersInTheRoom().get(0));
                                        playerLabelTwo.setText("Empty");
                                        playerLabelThree.setText("Empty");
                                        startGameButton.setDisable(true);
                                    }
                                    else if(view.getPlayersInTheRoom().size() == 2){
                                        playerLabelOne.setText(view.getPlayersInTheRoom().get(0));
                                        playerLabelTwo.setText(view.getPlayersInTheRoom().get(1));
                                        playerLabelThree.setText("Empty");
                                        startGameButton.setDisable(false);
                                    }
                                    else if(view.getPlayersInTheRoom().size() == 3){
                                        playerLabelOne.setText(view.getPlayersInTheRoom().get(0));
                                        playerLabelTwo.setText(view.getPlayersInTheRoom().get(1));
                                        playerLabelThree.setText(view.getPlayersInTheRoom().get(2));
                                        startGameButton.setDisable(false);
                                    }
                                    else if(view.getPlayersInTheRoom().size() > 3){
                                        if(isHost){
                                            for(int i = 3; i < view.getPlayersInTheRoom().size(); i++){
                                                client.viewInputExec(view, "kick", view.getPlayersInTheRoom().get(i));
                                            }

                                        }
                                        System.out.println("Something gone wrong...");
                                    }
                                } catch (IndexOutOfBoundsException e) {
                                    e.printStackTrace();
                                } finally{
                                    latch.countDown();
                                }
                            }
                        });
                        latch.await();
                        //Keep with the background work
                        return null;
                    }
                };
            }
        };
        service.start();
    }

    public void kicked(String message)  {
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        //Background work
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try{

                                    Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.CLOSE);

                                    alert.setTitle("Message");
                                    alert.setHeaderText("You received a message!");
                                    alert.showAndWait();
                                    FXMLLoader loader = new FXMLLoader();
                                    loader.setLocation(getClass().getResource("/views/lobby.fxml"));
                                    Parent lobby = loader.load();

                                    //loader.setController(this.view.getLobbyGUIController());
                                    LobbyGUIController controller = loader.getController();
                                    controller.returnFromRoom(username, client, view, futureGame);

                                    Stage window = (Stage) roomScene.getScene().getWindow();

                                    Scene lobbyScene = new Scene(lobby);

                                    window.setScene(lobbyScene);
                                    window.show();
                                    client.viewInputExec(view, "leave", "");
                                } catch (IndexOutOfBoundsException | IOException e) {
                                    e.printStackTrace();
                                } finally{
                                    latch.countDown();
                                }
                            }
                        });
                        latch.await();
                        //Keep with the background work
                        return null;
                    }
                };
            }
        };
        service.start();
    }

    public void loadGame(GameController controller){
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        //Background work
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try{

                                    FXMLLoader loader = new FXMLLoader();
                                    loader.setLocation(getClass().getResource("/views/primary.fxml"));
                                    Parent lobby = loader.load();

                                    Stage window = (Stage) players.getScene().getWindow();
                                    GameGUIController gameController = loader.getController();
                                    gameController.init(window, view.getPlayersInTheRoom().size(), view.getPlayersInTheRoom(), controller, isHost, username, client);

                                    Scene lobbyScene = new Scene(lobby);

                                    window.setScene(lobbyScene);
                                    window.show();
                                } catch (IndexOutOfBoundsException | IOException | NotExecutedException e) {
                                    e.printStackTrace();
                                } finally{
                                    latch.countDown();
                                }
                            }
                        });
                        latch.await();
                        //Keep with the background work
                        return null;
                    }
                };
            }
        };
        service.start();
    }

    public boolean isFull(){
        return this.view.getPlayersInTheRoom().size() > 3;
    }

}
