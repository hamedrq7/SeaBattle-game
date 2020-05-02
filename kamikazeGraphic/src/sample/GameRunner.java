package sample;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import world.GameConstants;
import world.assets.Player;

import java.util.Scanner;

public class GameRunner {
    public int size;
    public int getSize() {return this.size;}
    Player player1;
    Player player2;

    Label label = new Label("Enter Size of Board ( 8 <= Size <= 30 )");
    Label message = new Label("");

    TextField textField = new TextField();
    Button enterButton = new Button("Enter");

    public GameRunner() {
        this.size = -1;

        enterButton.setOnMouseClicked(mouseEvent -> {
            Scanner scan = new Scanner(textField.getText());
            int scannedSize = scan.nextInt();
            scan.close();
            if(scannedSize >= 8 && scannedSize <= 30) {
                this.size = scannedSize;
                startGame();
            }
            else
            {
                message.setText("Invalid Size, enter again!");
            }
        });

    }

    public void Prep() {
        VBox vBox1 = new VBox(5, label, message);
        HBox hBox1 = new HBox(10, textField, enterButton);
        Stage stage = new Stage();
        VBox vBox2 = new VBox(10, vBox1, hBox1);

        Scene scene = new Scene(vBox2);
        stage.setScene(scene);
        stage.show();
    }

    public void startGame() {

        if(this.size != -1) {
            player1 = new Player(1, size);
            player2 = new Player(2, size);

            player1.setEnemy(player2);
            player2.setEnemy(player1);

            player1.display();
            player2.display();
        }
        else
        {
            System.out.println("7769");
        }

    }
}
