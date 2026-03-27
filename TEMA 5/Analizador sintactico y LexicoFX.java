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

    // Palabras reservadas
    private static final Set<String> PALABRAS_RESERVADAS = new HashSet<>(
            Arrays.asList("int", "float", "double", "char", "if", "else", "while", "for", "return")
    );

    @Override
    public void start(Stage stage) {
        Label lblEntrada = new Label("Ingrese código fuente:");
        TextArea entrada = new TextArea();
        entrada.setPromptText("Ejemplo:\nint x = 10;\nfloat y = 3.14;\nint 5x = 10;\nchar c = 'a';");
        entrada.setPrefRowCount(8);

        Button analizar = new Button("Analizar");

        Label lblSalida = new Label("Resultado del análisis:");
        TextArea salida = new TextArea();
        salida.setEditable(false);
        salida.setPrefRowCount(15);

        analizar.setOnAction(e -> {
            String codigo = entrada.getText();

            String resultadoLexico = analizarCodigo(codigo);
            String resultadoSintactico = analizarSintaxis(codigo);

            salida.setText(
                 
                "=== ANALISIS LEXICO ===\n" +
                    resultadoLexico +
                    "\n=== ANALISIS SINTACTICO ===\n" +
                    resultadoSintactico
            );
        });

        VBox root = new VBox(10, lblEntrada, entrada, analizar, lblSalida, salida);
        root.setPadding(new Insets(15));

        Scene scene = new Scene(root, 700, 600);
        stage.setTitle("Analizador Léxico y Sintáctico en JavaFX");
        stage.setScene(scene);
        stage.show();
    }

   
    // ANALIZADOR LEXICO
   
    private String analizarCodigo(String codigo) {
        StringBuilder resultado = new StringBuilder();

        Pattern patron = Pattern.compile(
                "'[^']'" +                    
                "|\\d+\\.\\d+" +
                "|\\d+[a-zA-Z_]+\\w*" +
                "|[a-zA-Z_][a-zA-Z0-9_]*" +
                "|\\d+" +
                "|==|!=|<=|>=|&&|\\|\\|" +
                "|[=+\\-*/<>]" +
                "|[;:,(){}\\[\\]]"
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
        if (token.matches("'[^']'")) {
            return "CARACTER";
        } else if (PALABRAS_RESERVADAS.contains(token)) {
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

  
    // ANALIZADOR SINTACTICO
   
    private String analizarSintaxis(String codigo) {
        StringBuilder resultado = new StringBuilder();

        String[] lineas = codigo.split("\\n");

        // Gramática: tipo id = valor;
        Pattern patron = Pattern.compile(
                "(int|float|double|char)\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*=\\s*(\\d+(\\.\\d+)?|'[^']');"
        );

        for (String linea : lineas) {
            if (linea.trim().isEmpty()) continue;

            Matcher matcher = patron.matcher(linea.trim());

            if (matcher.matches()) {
                resultado.append(" Sintaxis correcta: ").append(linea).append("\n");
            } else {
                resultado.append("Error sintáctico: ").append(linea).append("\n");
            }
        }

        return resultado.toString();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
