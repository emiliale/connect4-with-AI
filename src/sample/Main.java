package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

    String splayer1;
    String splayer2;
    String salgorithm;
    String sheuristic;

    public void Menu(Stage stage){
        stage.setTitle("Connect4");

        String players[] = {"AI", "Player"};
        String algorithms[] = {"Alpha-Beta", "Min-Max"};
        String heuristics[] = {"Simple", "Complex"};

        ChoiceBox choicePlayer1 = new ChoiceBox(FXCollections.observableArrayList(players));
        ChoiceBox choicePlayer2 = new ChoiceBox(FXCollections.observableArrayList(players));
        ChoiceBox choiceAlgorithm = new ChoiceBox(FXCollections.observableArrayList(algorithms));
        ChoiceBox choiceHeuristic = new ChoiceBox(FXCollections.observableArrayList(heuristics));

        choicePlayer1.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            // if the item of the list is changed
            public void changed(ObservableValue ov, Number value, Number new_value)
            {
                splayer1 = (players[new_value.intValue()]);
            }
        });

        choicePlayer2.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            // if the item of the list is changed
            public void changed(ObservableValue ov, Number value, Number new_value)
            {
                splayer2 = (players[new_value.intValue()]);
            }
        });

        choiceAlgorithm.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            // if the item of the list is changed
            public void changed(ObservableValue ov, Number value, Number new_value)
            {
                salgorithm = (algorithms[new_value.intValue()]);
            }
        });

        choiceHeuristic.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            // if the item of the list is changed
            public void changed(ObservableValue ov, Number value, Number new_value)
            {
                sheuristic = (heuristics[new_value.intValue()]);
            }
        });

        TextField txt_depth  = new TextField("");

        Label lplayer1 = new Label("Player1: ");
        Label lplayer2 = new Label("Player2: ");
        Label depth = new Label("Depth: ");
        Label algorithm = new Label("Algorithm: ");
        Label heuristic = new Label("Heuristic: ");

        Button btnConfirm = new Button("CONFIRM");

        EventHandler<ActionEvent> event_confirm = e -> {
            int depthValue = Integer.parseInt(txt_depth.getText());
            Connect4 c4 = new Connect4();
            boolean AI1=false;
            boolean AI2=false;
            boolean minMax=true;
            boolean simple=false;

            if (splayer1.equals("AI")) {
                AI1=true;
            }
            if (splayer2.equals("AI")) {
                AI2=true;
            }

            if(salgorithm.equals("Alpha-Beta")){
                minMax=false;
            }

            if(sheuristic.equals("Simple")){
                simple=true;
            }



            stage.setScene(new Scene(c4.createContent()));
            stage.show();
            try {
                c4.play(AI1, AI2, minMax, depthValue, simple);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        };

        btnConfirm.setOnAction(event_confirm);

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(16));
        gridPane.setHgap(16);
        gridPane.setVgap(8);
        gridPane.add(lplayer1, 1, 1);
        gridPane.add(choicePlayer1, 2, 1);
        gridPane.add(lplayer2, 1, 2);
        gridPane.add(choicePlayer2, 2, 2);
        gridPane.add(algorithm, 1, 3);
        gridPane.add(choiceAlgorithm, 2, 3);
        gridPane.add(depth, 1, 4);
        gridPane.add(txt_depth, 2, 4);
        gridPane.add(heuristic, 1, 5);
        gridPane.add(choiceHeuristic, 2, 5);
        gridPane.add(btnConfirm, 1,6);

        Scene sc = new Scene(gridPane, 300, 250);

        stage.setScene(sc);

        stage.show();
    }



    @Override
    public void start(Stage stage) throws Exception {
        Menu(stage);
    }

    public static void main(String[] args){
        launch(args);
    }

}
