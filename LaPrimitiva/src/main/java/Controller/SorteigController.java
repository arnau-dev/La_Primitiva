package Controller;


import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Model.Sorteig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class SorteigController {


    @FXML
    private MenuButton menuButtonOpcio;
    @FXML
    private DatePicker dateTimePickerAfegir;
    @FXML
    private Label labelAfegir;
    @FXML
    private Button buttonAfegir;
    @FXML
    private Label labelRealitzar;
    @FXML
    private MenuButton menuBUttonRealitzar;
    @FXML
    private Button buttonRealitzar;
    @FXML
    private Label labelElsActius;
    @FXML
    private ListView ListViewElsActius;
    @FXML
    private Label labelCobrat;
    @FXML
    private MenuButton menubuttonCobrat;
    @FXML
    private MenuButton menubuttonEstadistiques;
    @FXML
    private Label labelNombreApostes;
    @FXML
    private Label labelNumeroDePremiades;
    @FXML
    private Label labelApostesAmbReintegrament;
    @FXML
    private TextField textFiledNumeroDapoestes;
    @FXML
    private TextField textFiledNumeroDePremiades;
    @FXML
    private TextField textFiledApostesAmbReintegrament;

    //la funcio realitza el sorteig i el elimina de la llista de sortejos a realitzar
    @FXML
    private void realitzarSorteig() throws ParseException {
        String selectedDateText = menuBUttonRealitzar.textProperty().get();

        if (selectedDateText.equals("No Disponibles")) {
            mostrarAlerta("Advertencia", "No has seleccionat", "No pots realizar el sorteig: No habilitat");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(selectedDateText);
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());

            if (MainController.realitzarSorteig(sqlDate)){
                menuBUttonRealitzar.textProperty().set("Sorteig");


                for (MenuItem item : menuBUttonRealitzar.getItems()) {
                    if (item.getText().equals(selectedDateText)) {
                        menuBUttonRealitzar.getItems().remove(item);
                        break;
                    }
                }

                mostrarAlerta2("Sorteig realitzat", "El sorteig introduït ha estat realitzat");
            } else {
                mostrarAlerta2("ERROR de SQL","Error de BD");
            }
        }
    }

    private void mostrarAlerta2(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle(titulo);
        alert.setHeaderText(contenido);
        ButtonType closeButton = new ButtonType("Cerrar", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().add(closeButton);
        alert.showAndWait();
    }

    //fica visible o invisible depent de l'opció seleccionada
    @FXML
    private void escollirTriar() {
        menuButtonOpcio.textProperty().set("Afegir");
        buttonAfegir.visibleProperty().set(true);
        labelAfegir.visibleProperty().set(true);
        dateTimePickerAfegir.visibleProperty().set(true);
        labelRealitzar.visibleProperty().set(false);
        menuBUttonRealitzar.visibleProperty().set(false);
        buttonRealitzar.visibleProperty().set(false);
        labelElsActius.visibleProperty().set(false);
        ListViewElsActius.visibleProperty().set(false);
        labelCobrat.visibleProperty().set(false);
        menubuttonCobrat.visibleProperty().set(false);
        menubuttonEstadistiques.visibleProperty().set(false);
        labelNombreApostes.visibleProperty().set(false);
        labelNumeroDePremiades.visibleProperty().set(false);
        labelApostesAmbReintegrament.visibleProperty().set(false);
        textFiledNumeroDapoestes.visibleProperty().set(false);
        textFiledNumeroDePremiades.visibleProperty().set(false);
        textFiledApostesAmbReintegrament.visibleProperty().set(false);
    }
    //fica visible o invisible depent de l'opció seleccionada

    //Omplir amb dades dels DAO el boto amb els sortejos que no estiguin realitzats
    @FXML
    private void escollirRealitzar() {
        menuButtonOpcio.textProperty().set("Realitzar");
        buttonAfegir.visibleProperty().set(false);
        labelAfegir.visibleProperty().set(false);
        dateTimePickerAfegir.visibleProperty().set(false);
        labelRealitzar.visibleProperty().set(true);
        menuBUttonRealitzar.visibleProperty().set(true);
        buttonRealitzar.visibleProperty().set(true);
        labelElsActius.visibleProperty().set(false);
        ListViewElsActius.visibleProperty().set(false);
        labelCobrat.visibleProperty().set(false);
        menubuttonCobrat.visibleProperty().set(false);
        menubuttonEstadistiques.visibleProperty().set(false);
        labelNombreApostes.visibleProperty().set(false);
        labelNumeroDePremiades.visibleProperty().set(false);
        labelApostesAmbReintegrament.visibleProperty().set(false);
        textFiledNumeroDapoestes.visibleProperty().set(false);
        textFiledNumeroDePremiades.visibleProperty().set(false);
        textFiledApostesAmbReintegrament.visibleProperty().set(false);

        ArrayList<Sorteig> sortejosNoRealitzats = MainController.selectSortejosNoRealitzats();

        if (sortejosNoRealitzats.isEmpty()) {
            menuBUttonRealitzar.textProperty().set("No habilitat");
        } else {
            Collections.sort(sortejosNoRealitzats);
            menuBUttonRealitzar.getItems().clear();
            for (int i = 0; i < sortejosNoRealitzats.size(); i++) {
                MenuItem menuItem = new MenuItem();
                menuItem.textProperty().set("" + sortejosNoRealitzats.get(i).getDataSorteig());
                menuItem.setOnAction(e -> {
                    menuBUttonRealitzar.textProperty().set(menuItem.getText());
                });
                menuBUttonRealitzar.getItems().add(menuItem);
            }
        }
    }

    //escollirElsActius posa amb invisible i invisible diferents elemets
    //mostrar els sortejos actius
    @FXML
    private void consultarSortejosActius() {
        menuButtonOpcio.textProperty().set("Consultar els sortejos actius");
        buttonAfegir.visibleProperty().set(false);
        labelAfegir.visibleProperty().set(false);
        dateTimePickerAfegir.visibleProperty().set(false);
        labelRealitzar.visibleProperty().set(false);
        menuBUttonRealitzar.visibleProperty().set(false);
        buttonRealitzar.visibleProperty().set(false);
        labelElsActius.visibleProperty().set(true);
        ListViewElsActius.visibleProperty().set(true);
        labelCobrat.visibleProperty().set(false);
        menubuttonCobrat.visibleProperty().set(false);
            menubuttonEstadistiques.visibleProperty().set(false);
        labelNombreApostes.visibleProperty().set(false);
        labelNumeroDePremiades.visibleProperty().set(false);
        labelApostesAmbReintegrament.visibleProperty().set(false);
        textFiledNumeroDapoestes.visibleProperty().set(false);
        textFiledNumeroDePremiades.visibleProperty().set(false);
        textFiledApostesAmbReintegrament.visibleProperty().set(false);

        ArrayList<Sorteig> arrayListSortejosNoRealitzats = MainController.selectSortejosNoRealitzats();

        if (!arrayListSortejosNoRealitzats.isEmpty()) {
            Collections.sort(arrayListSortejosNoRealitzats);
            ListViewElsActius.getItems().clear();
            for (int i = 0; i < arrayListSortejosNoRealitzats.size(); i++) {
                ListViewElsActius.getItems().add("" + arrayListSortejosNoRealitzats.get(i).getDataSorteig());
            }
        }
    }


    //plena els botons amb els dao solicitats per poder tenir un control de les estadistiques del nostre sorteig
    @FXML
    private void estadistiques() {
        menuButtonOpcio.textProperty().set("Estadístiques");
        buttonAfegir.visibleProperty().set(false);
        labelAfegir.visibleProperty().set(false);
        dateTimePickerAfegir.visibleProperty().set(false);
        labelRealitzar.visibleProperty().set(false);
        menuBUttonRealitzar.visibleProperty().set(false);
        buttonRealitzar.visibleProperty().set(false);
        labelElsActius.visibleProperty().set(false);
        ListViewElsActius.visibleProperty().set(false);
        labelCobrat.visibleProperty().set(false);
        menubuttonCobrat.visibleProperty().set(false);
        menubuttonEstadistiques.visibleProperty().set(true);
        labelNombreApostes.visibleProperty().set(true);
        labelNumeroDePremiades.visibleProperty().set(true);
        labelApostesAmbReintegrament.visibleProperty().set(true);
        textFiledNumeroDapoestes.visibleProperty().set(true);
        textFiledNumeroDePremiades.visibleProperty().set(true);
        textFiledApostesAmbReintegrament.visibleProperty().set(true);

        ArrayList<Sorteig> arrayListSelectTotsSortejos = MainController.selectTotsSortejos();

        if (arrayListSelectTotsSortejos.isEmpty()) {
            menubuttonEstadistiques.textProperty().set("No disponible");
        } else {
            Collections.sort(arrayListSelectTotsSortejos);
            menubuttonEstadistiques.getItems().clear();
            for (int i = 0; i < arrayListSelectTotsSortejos.size(); i++) {
                MenuItem menuItem = new MenuItem();
                menuItem.textProperty().set("" + arrayListSelectTotsSortejos.get(i).getDataSorteig());
                menuItem.setOnAction(e -> {
                    menubuttonEstadistiques.textProperty().set(menuItem.getText());
                    stadistiquesDao();
                });
                menubuttonEstadistiques.getItems().add(menuItem);
            }
        }
    }
    //A traves dels daos mirar les estadistiques del sorteig
    private void stadistiquesDao() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = sdf.parse(menubuttonEstadistiques.textProperty().get());
        } catch (ParseException e) {
            mostrarAlerta("Error", "Data invàlida", "La data seleccionada no és vàlida.");
            return;
        }

        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        int nombreApostes = MainController.nombreApostes(sqlDate);
        int countPremiades = MainController.selectCountApostesPremiades(sqlDate);
        int countReintegrament = MainController.selectCountApostesPremiadesReintegrament(sqlDate);

        if (nombreApostes >= 0) {
            textFiledNumeroDapoestes.setText("" + nombreApostes);
        } else {
            textFiledNumeroDapoestes.setText("0");
        }

        if (countPremiades >= 0) {
            textFiledNumeroDePremiades.setText("" + countPremiades);
        } else {
            textFiledNumeroDePremiades.setText("0");
        }

        if (countReintegrament >= 0) {
            textFiledApostesAmbReintegrament.setText("" + countReintegrament);
        } else {
            textFiledApostesAmbReintegrament.setText("0");
        }
    }

    //Funcio per retrocedir amb botto
    @FXML
    private void cancel(Event event) {
        Node arrel = (Node) event.getSource();
        Stage stage = (Stage) arrel.getScene().getWindow();
        stage.close();
    }
    // afegirSorteig afegeix un sorteix si es cumpleixen totes les normés i es segueixen les pautes
    @FXML
    private void afegirSorteig() {
        LocalDate selectedDate = dateTimePickerAfegir.getValue();

        if (selectedDate == null || selectedDate.isBefore(LocalDate.now())) {
            mostrarAlerta("Advertencia", "Error en la data", "La data introduida es anteorior a l'actual. Fixat be");
        } else {
            Date data = java.sql.Date.valueOf(selectedDate);
            Sorteig sorteig = new Sorteig(data);
            String titol;
            String contingut;

            if (MainController.insertarSorteig(sorteig)) {
                titol = "Sorteig Afegit";
                contingut = "El sorteig s'ha afegit amb exit";
            } else {
                titol = "Advertencia";
                contingut = "Ja hi ha un sorteig en la data introduida";
            }

            mostrarAlerta(titol, contingut, "");
        }
    }

    //funcio per alertes en funcio del titol contingut i descripcio
    private void mostrarAlerta(String titol, String contingut, String descripcion) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle(titol);
        alert.setHeaderText(contingut);
        alert.setContentText(descripcion);
        ButtonType closeButton = new ButtonType("Tancar", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().add(closeButton);
        alert.showAndWait();
    }







}
