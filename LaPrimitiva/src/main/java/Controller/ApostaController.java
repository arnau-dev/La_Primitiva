package Controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import Model.Aposta;
import Model.Combinacio;
import Model.Sorteig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class ApostaController {


    @FXML
    private TextField textCombi;
    @FXML
    private TextField textTipus;
    @FXML
    private Button buttonTornarJugar;
    @FXML
    private Label labelCancelar;
    @FXML
    private Label labelIdentificador;
    @FXML
    private MenuButton menuButtonTornar;
    @FXML
    private TextField textIdentificador;
    @FXML
    private CheckBox checkBoxReintegro;
    @FXML
    private MenuButton menuButtonSorteig;
    @FXML
    private MenuButton menuButtonTipus;
    @FXML
    private Label labelCombinacio;
    @FXML
    private Label labelTipusAposta;

    //initialize inicie com el seu nom indica i també ompla els botons amb els DAO de la clase Sorteig
    //Amb els sortejos no realitzats

    @FXML
    private void initialize() {


        ArrayList<Sorteig> sorteigsNoRealitzats = MainController.selectSortejosNoRealitzats();

        if (sorteigsNoRealitzats.isEmpty()) {
            menuButtonSorteig.setText("Sorteig no disponible");
        } else {
            menuButtonSorteig.getItems().clear();
            Collections.sort(sorteigsNoRealitzats);


            for (Sorteig sorteig : sorteigsNoRealitzats) {
                MenuItem menuItem = new MenuItem(sorteig.getDataSorteig().toString());
                menuItem.setOnAction(event -> menuButtonSorteig.setText(menuItem.getText()));
                menuButtonSorteig.getItems().add(menuItem);
            }
        }

        TextFormatter<Integer> textFormatterReintegrament = new TextFormatter<>(
                new IntegerStringConverter(),
                null,
                change -> {
                    String newText = change.getControlNewText();
                    return (newText.matches("\\d*")) ? change : null;
                }
        );

        textTipus.setTextFormatter(textFormatterReintegrament);
    }

    //apostaPersonal gestiona la configuració d'una aposta introduïda pel usuari
    //Controlan varis errors d'escriptura i seleccio de la data

    @FXML
    private void apostaPersonal() {
        if (menuButtonSorteig.getText().equals("Elegeix una data")) {
            mostrarAlertaDeSorteig();
        } else {
            configurarApostaUsuari();
        }
    }

    private void mostrarAlertaDeSorteig() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Selecciona una data del sorteig");
        alert.setContentText("Selecciona alguna data dels sortejos disponibles");
        ButtonType closeButton = new ButtonType("Tancar", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().add(closeButton);
        alert.showAndWait();
    }

    private void configurarApostaUsuari() {
        menuButtonTipus.setText("Aposta personal");
        labelCancelar.setVisible(false);
        textCombi.setVisible(true);
        textTipus.setVisible(true);
        textCombi.setEditable(true);
        textTipus.setEditable(true);
        checkBoxReintegro.setVisible(true);
        labelIdentificador.setVisible(false);
        menuButtonTornar.setVisible(false);
        textIdentificador.setVisible(false);
        buttonTornarJugar.setVisible(false);
        labelCombinacio.setVisible(true);
        labelTipusAposta.setVisible(true);


        textIdentificador.setText("");
        textCombi.setText("");
        textTipus.setText("");
    }

    // seleccionarApostaAleatoria genere una aposta aleatoria amb l'opció de tornar a cambiar l'aposta aleatoria
    @FXML
    private void seleccionarApostaAleatoria() {
        if (menuButtonSorteig.getText().equals("Elegeix una data")) {
            mostrarAlertaseleccioSorteig();
        } else {
            configurarApostaAleatoria();
        }
    }

    private void mostrarAlertaseleccioSorteig() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Seleccionar un Sorteig");
        alert.setContentText("Selecciona una data dels sortejos disponibles");
        ButtonType closeButton = new ButtonType("Tancar", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().add(closeButton);
        alert.showAndWait();
    }

    private void configurarApostaAleatoria() {
        menuButtonTipus.setText("Aposta aleatoria");
        labelTipusAposta.setVisible(true);
        textCombi.setVisible(true);
        textTipus.setVisible(true);
        textCombi.setEditable(false);
        textTipus.setEditable(false);
        checkBoxReintegro.setVisible(true);
        textIdentificador.setText("");
        labelCancelar.setVisible(false);
        labelIdentificador.setVisible(false);
        menuButtonTornar.setVisible(false);
        textIdentificador.setVisible(false);
        buttonTornarJugar.setVisible(true);
        labelCombinacio.setVisible(true);
        textCombi.setText("");
        textTipus.setText("");

        generarCombinacioAleatoria();
    }


    //Check box de si l'usuari vol reintegrament o no

    @FXML
    private void selectReintegrament() {
        if (!checkBoxReintegro.isSelected()) {
            desactivarReintegrament();
        } else {
            activarReintegrament();
        }
    }

    private void desactivarReintegrament() {
        textTipus.setText("-1");
        textTipus.setDisable(true);
    }

    private void activarReintegrament() {
        textTipus.setDisable(false);
        textTipus.setText("");
    }

    //generarAleatori seleccionan l'opico aleatori es va generan tot le rato combinacions aleatories

    @FXML
    private void generarCombinacioAleatoria() {
        Combinacio combinacio = new Combinacio();
        String combinacionGenerada = combinacio.getCombinacio();
        textCombi.setText(combinacionGenerada);

        if (checkBoxReintegro.isSelected()) {
            int reintegroGenerado = combinacio.getReintregrament();
            textTipus.setText(String.valueOf(reintegroGenerado));
        }
    }

    //cambiar de pantalla per anar a la pincipal
    @FXML
    private void backMenu(Event event) {
        Node arrel = (Node) event.getSource();
        Stage stage = (Stage) arrel.getScene().getWindow();
        stage.close();
    }


    //la funcio afegir obligue i corregeix a que el usuari fagi tots els passos abans d'afegir una aposta
    @FXML
    private void afegir() throws ParseException {
        String sorteigText = menuButtonSorteig.getText();
        String tipusText = menuButtonTipus.getText();

        if ("Elegeix una data".equals(sorteigText)) {
            alertaSortegjosDisponibles();
        } else if ("No disponible".equals(sorteigText)) {
            alertaSorteigNoDisponible();
        } else if ("Selecciona el tipus d'aposta".equals(tipusText)) {
            alertaTipoIncorrecte();
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            if (!"Copia".equals(tipusText)) {
                manejarApostaNormal(sdf);
            }
        }
    }

    //Configuaració d'alertes
    private void alertaSortegjosDisponibles() {
        mostrarAlerta("Seleccionar un sorteig!", "Selecciona algun dels sortejos disponibles");
    }

    private void alertaSorteigNoDisponible() {
        mostrarAlerta("No hi ha ningun sorteig! Afegeixlos en el boto Sorteig", "No hi ha sortejos disponibles, ves al menu principal per afegir Sortejos");
    }

    private void alertaTipoIncorrecte() {
        mostrarAlerta("ERROR fixat amb el nombre de números que te una aposta", "Tipus de tiquet incorrecte. Afegeix una combinació vàlida");
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    //Controlar els errors de l'aposta introduida per l'usuari
    private void manejarApostaNormal(SimpleDateFormat sdf) {
        try {
            Combinacio combi = crearCombinacio();
            if (combi != null) {
                Aposta aposta = new Aposta(combi, -1);
                String sorteigDateText = menuButtonSorteig.getText();
                Date sorteigDate = sdf.parse(sorteigDateText);
                if (MainController.afegirAposta(aposta, sorteigDate)) {
                    mostrarAlertaApostaAfegida(aposta, combi);
                } else {
                    mostrarAlerta("ERROR en afegir l'aposta", "La aposta introduida no s'ha pogut afegir. Tornau a intentar");
                }
            } else {
                mostrarAlerta("ERROR", "No s'ha creat la combinació");
            }
        } catch (ParseException e) {
            mostrarAlerta("ERROR", "Selecciona la data del sorteig " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            mostrarAlerta("ERROR", "Format incorrecte introdueix un espai cada 2  números");
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            mostrarAlerta("ERROR", "Format incorrecte");
            e.printStackTrace();
        }
    }

    //avisar de si l'aposta s'ha introduit correctament i amb quins valors
    private void mostrarAlertaApostaAfegida(Aposta aposta, Combinacio combi) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Aposta afegida exitosament");
        String sorteig = menuButtonSorteig.getText();
        String identificador = String.valueOf(aposta.getIdentificador());
        String combinacio = combi.getCombinacio();
        String reintegrament = (combi.getReintregrament() == -1) ? "" : "\nReintegrament: " + combi.getReintregrament();
        alert.setContentText("Identificador: " + identificador + "\nData Sorteig: " + sorteig + "\nCombinació: " + combinacio + reintegrament);
        ButtonType closeButton = new ButtonType("Tancar", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().add(closeButton);
        alert.showAndWait();
    }

    //funció per crear la combinacio del usuari
    private Combinacio crearCombinacio() {
        int reintegro = (checkBoxReintegro.isSelected()) ? Integer.parseInt(textTipus.getText()) : -1;
        String[] textUsuari = textCombi.getText().split(" ");
        int[] num = new int[6];
        for (int i = 0; i < num.length; i++) {
            num[i] = Integer.parseInt(textUsuari[i]);
        }
        return new Combinacio(num, reintegro);
    }

}
