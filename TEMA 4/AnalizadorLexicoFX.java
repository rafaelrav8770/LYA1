import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalizadorLexicoFX extends Application {

    // Palabras reservadas que reconocerá el analizador
    private static final Set<String> PALABRAS_RESERVADAS = new HashSet<>(
            Arrays.asList("int", "float", "double", "char", "if", "else", "while", "for", "return")
    );

    @Override
    public void start(Stage stage) {
        Label lblEntrada = new Label("Ingrese código fuente:");
        TextArea entrada = new TextArea();
        entrada.setPromptText("Ejemplo:\nint x = 10;\nfloat y = 3.14;\nint 5x = 10;");
        entrada.setPrefRowCount(8);

        Button analizar = new Button("Analizar");

        Label lblSalida = new Label("Resultado del análisis:");
        TextArea salida = new TextArea();
        salida.setEditable(false);
        salida.setPrefRowCount(12);

        analizar.setOnAction(e -> {
            String codigo = entrada.getText();
            String resultado = analizarCodigo(codigo);
            salida.setText(resultado);
        });

        VBox root = new VBox(10, lblEntrada, entrada, analizar, lblSalida, salida);
        root.setPadding(new Insets(15));

        Scene scene = new Scene(root, 700, 550);
        stage.setTitle("Analizador Léxico en JavaFX");
        stage.setScene(scene);
        stage.show();
    }

    
    private String analizarCodigo(String codigo) {
        StringBuilder resultado = new StringBuilder();

        // Patrón para separar correctamente números, identificadores, operadores y símbolos
        Pattern patron = Pattern.compile(
                "\\d+\\.\\d+" +                // números decimales
                "|\\d+[a-zA-Z_]+\\w*" +        // errores léxicos como 5x, 10abc
                "|[a-zA-Z_][a-zA-Z0-9_]*" +    // identificadores / palabras reservadas
                "|\\d+" +                      // números enteros
                "|==|!=|<=|>=|&&|\\|\\|" +     // operadores dobles
                "|[=+\\-*/<>]" +               // operadores simples
                "|[;:,(){}\\[\\]]"             // símbolos
        );

        Matcher matcher = patron.matcher(codigo);

        while (matcher.find()) {
            String token = matcher.group();
            resultado.append(token)
                    .append(" -> ")
                    .append(clasificarToken(token))
                    .append("\n");
        }

        return resultado.toString();
    }  
        
    private String clasificarToken(String token) {
        if (PALABRAS_RESERVADAS.contains(token)) {
            return "PALABRA_RESERVADA";
        } else if (token.matches("\\d+\\.\\d+")) {
            return "NUMERO_DECIMAL";
        } else if (token.matches("\\d+")) {
            return "NUMERO_ENTERO";
        } else if (token.matches("\\d+[a-zA-Z_]+\\w*")) {
            return "ERROR_LEXICO";
        } else if (token.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
            return "IDENTIFICADOR";
        } else if (token.matches("==|!=|<=|>=|&&|\\|\\||[=+\\-*/<>]")) {
            return "OPERADOR";
        } else if (token.matches("[;:,(){}\\[\\]]")) {
            return "SIMBOLO";
        } else {
            return "ERROR_LEXICO";
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}