package boardgame;

import boardgame.model.BoardGameModel;
import javafx.application.Platform;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import org.tinylog.Logger;

public class BoardGameController {

    @FXML
    private GridPane board;

    @FXML
    private TextField numberOfCoinsField;

    private final BoardGameModel model = new BoardGameModel();

    @FXML
    private void initialize() {
        for (var i = 0; i < board.getRowCount(); i++) {
            for (var j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare(i, j);
                board.add(square, j, i);
            }
        }
        numberOfCoinsField.textProperty().bind(model.numberOfCoinsProperty().asString());
        model.numberOfCoinsProperty().addListener(
                (observableValue, oldValue, newValue) ->
                        Logger.debug("Number of coins is changed from {} to {}", oldValue, newValue)
        );
        model.gameOverProperty().addListener(this::handleGameOver);
    }

    private StackPane createSquare(int row, int col) {
        var square = new StackPane();
        square.getStyleClass().add("square");
        var piece = new Circle(50);
        piece.fillProperty().bind(
                new ObjectBinding<Paint>() {
                    {
                        super.bind(model.squareProperty(row, col));
                    }
                    @Override
                    protected Paint computeValue() {
                        return switch (model.squareProperty(row, col).get()) {
                            case NONE -> Color.TRANSPARENT;
                            case HEAD -> Color.RED;
                            case TAIL -> Color.BLUE;
                        };
                    }
                }
        );
        square.getChildren().add(piece);
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        Logger.debug("Click on square ({},{})", row, col);
        model.makeMove(row, col);
    }

    private void handleGameOver(ObservableValue observableValue, boolean oldValue, boolean newValue) {
        if (newValue) {
            Logger.debug("Game board is full");
            Platform.runLater(this::showGameOverAlertAndExit);
        }
    }

    private void showGameOverAlertAndExit() {
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Game Over");
        alert.setContentText("Game board is full");
        alert.showAndWait();
        Platform.exit();
    }

}
