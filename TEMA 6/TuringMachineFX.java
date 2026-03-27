import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TuringMachineFX extends Application {

    private TextField inputField;
    private TextArea outputArea;

    @Override
    public void start(Stage stage) {

        Label label = new Label("Ingrese cadena (a^n b^n):");
        inputField = new TextField();
        Button runButton = new Button("Ejecutar");

        outputArea = new TextArea();
        outputArea.setEditable(false);

        runButton.setOnAction(e -> runMachine());

        VBox root = new VBox(10, label, inputField, runButton, outputArea);
        Scene scene = new Scene(root, 400, 400);

        stage.setTitle("Máquina de Turing");
        stage.setScene(scene);
        stage.show();
    }

    // Lógica de validación
    private void runMachine() {
        String input = inputField.getText();
        outputArea.clear();

        if (input == null || input.isEmpty()) {
            outputArea.setText("Cadena vacía");
            return;
        }

        if (!input.matches("a+b+")) {
            outputArea.setText("Cadena inválida (solo a^n b^n)");
            return;
        }

        int countA = 0;
        int countB = 0;
        int i = 0;

        while (i < input.length() && input.charAt(i) == 'a') {
            countA++;
            i++;
        }

        while (i < input.length() && input.charAt(i) == 'b') {
            countB++;
            i++;
        }

        if (countA == countB) {
            outputArea.setText("Cadena ACEPTADA");
        } else {
            outputArea.setText("Cadena RECHAZADA");
        }
    }

    
    public static void main(String[] args) {
        launch(args);
    }
}