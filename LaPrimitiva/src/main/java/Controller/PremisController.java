    package Controller;

    import javafx.event.Event;
    import javafx.fxml.FXML;
    import javafx.scene.Node;
    import javafx.scene.control.*;
    import javafx.stage.Stage;
    import Model.Aposta;
    import Model.Sorteig;

    import java.text.ParseException;
    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.Date;

    public class PremisController {

        @FXML
        private Label labelSorteig;
        @FXML
        private MenuButton menuButtonSorteig;
        @FXML
        private Label labelIdentificador;
        @FXML
        private TextField textFiledIdentificador;
        @FXML
        private Button buttonConsultar;
        @FXML
        private Label labelResultat;
        @FXML
        private Button buttonCobrar;
        @FXML
        private Button buttonTornar;
        @FXML
        private Button buttonConsultarAposta;
        @FXML
        private Button buttonCobrarAposta;

        //initialize funció que sencarrega d'afegir les dades als botons a traves dels DAO de les clases model
        @FXML
        private void initialize() {
            setVisibility(true);
            ArrayList<Sorteig> sorteigsRealitzats = MainController.selectSortejosRealitzats();

            if (sorteigsRealitzats.isEmpty()) {
                menuButtonSorteig.setText("No disponible");
            } else {
                Collections.sort(sorteigsRealitzats);
                menuButtonSorteig.getItems().clear();
                sorteigsRealitzats.forEach(sorteig -> {
                    MenuItem menuItem = new MenuItem();
                    menuItem.setText(sorteig.getDataSorteig().toString());
                    menuItem.setOnAction(e -> {
                        menuButtonSorteig.setText(menuItem.getText());
                    });
                    menuButtonSorteig.getItems().add(menuItem);
                });
            }
        }

        private void setVisibility(boolean visible) {
            labelIdentificador.visibleProperty().set(visible);
            textFiledIdentificador.visibleProperty().set(visible);
            buttonConsultar.visibleProperty().set(visible);
            labelResultat.visibleProperty().set(visible);
            buttonCobrar.visibleProperty().set(visible);

            labelSorteig.visibleProperty().set(visible);
            menuButtonSorteig.visibleProperty().set(visible);
        }


        //comprobarPremiat la primera part d'aquesta funcio gestionar els errors de l'usuari i posteriorment indica
        //només si te premi o no l'aposta consultada
        @FXML
        private void comprobarPremiat() {
            try {
                if (menuButtonSorteig.textProperty().get().equals("Sorteig")) {
                    mostrarAlerta("Cuidado fixat", "Degut a no seleccionar", "No pots realitzar el sorteig en aquesta data Sorteig");
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date sorteigDate = sdf.parse(menuButtonSorteig.getText());
                    int idAposta = Integer.parseInt(textFiledIdentificador.getText());
                    Aposta aposta = new Aposta(MainController.selectCombinacioAposta(sorteigDate, idAposta), idAposta);
                    Alert alert = new Alert(Alert.AlertType.NONE);
                    alert.setTitle("Aposta identificador " + aposta.getIdentificador());
                    if (MainController.selectComprobarApostaPremiada(aposta.getIdentificador(), sorteigDate)) {
                        alert.setContentText(MainController.selectQunatitatPremiGuanyat(aposta, new java.sql.Date(sorteigDate.getTime())));
                    } else {
                        alert.setContentText("L'aposta consultada no te premi ho sentim. Tornar a intentar sort realitza una altra aposta");
                    }

                    mostrarAlertaConBoton(alert, "Tancar");
                }
            } catch (NumberFormatException nfe) {
                mostrarAlerta("Cuidado fixat", "Aposta no afegida comprobar el identificador", "El format d'identificador introduit no es correcte: Introdueix un format d'identificador vàlid sisplau");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        private void mostrarAlerta(String titulo, String contenido, String descripcion) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(titulo);
            alert.setHeaderText(contenido);
            alert.setContentText(descripcion);
            alert.showAndWait();
        }

        private void mostrarAlertaConBoton(Alert alert, String botonTexto) {
            ButtonType closeButton = new ButtonType(botonTexto, ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().add(closeButton);
            alert.showAndWait();
        }

        //boto de tornar a la finestra principal
        @FXML
        private void cancelar(Event event) {
            Node arrel = (Node) event.getSource();
            Stage stage = (Stage) arrel.getScene().getWindow();
            stage.close();
        }

        //la funció seleccionarCobrarAposta cobra l'aposta introduïda i cumpleix una serie de valors per a poder ser cobrada
        // com la data, si ja ha estat cobrada i si te premi
        @FXML
        private void seleccionarCobrarAposta() {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                int idAposta = Integer.parseInt(textFiledIdentificador.getText());
                Date sorteigDate = sdf.parse(menuButtonSorteig.getText());

                if (menuButtonSorteig.textProperty().get().equals("Sorteig")) {
                    mostrarAlerta("Fixat be i selecciona", "no seleccionat", "No pots realitzar el sorteig en aquesta data degut a que ja hi ha un sorteig o es anterior a la data actual Sorteig");
                } else if (MainController.selectApostaCobrada(idAposta, new java.sql.Date(sorteigDate.getTime()))) {
                    mostrarAlerta("Advertencia", "Aposta ja cobrada", "Aquesta aposta ja estat cobrada. Introdueix una altra per saber el seu premi.");
                } else if (!MainController.selectComprobarApostaPremiada(idAposta, sorteigDate)) {
                    mostrarAlerta("No as tingut sort", "Aposta sense premi", "Ho sentim no ta tocat cap premi. Torna a intentar sort");
                } else {
                    if (MainController.updateCobradaAposta(idAposta, new java.sql.Date(sorteigDate.getTime()))) {
                        Alert alert = new Alert(Alert.AlertType.NONE);
                        alert.setTitle("Identificador de l'aposta: " + idAposta + " ja ha estat cobrada");
                        alert.setContentText("Aposta cobrada amb èxit.");
                        ButtonType closeButton = new ButtonType("tancar", ButtonBar.ButtonData.CANCEL_CLOSE);
                        alert.getButtonTypes().add(closeButton);
                        alert.showAndWait();
                    }
                }
            } catch (NumberFormatException nfe) {
                mostrarAlerta("Advertencia", "Identifcador incorrecte", "Format d'identificador d'aposta incorrecte. Tornal a introduir");
            } catch (ParseException e) {
                mostrarAlerta("Fixat be", "Data introduida incorrecta", "Error en la data del sorteig introduida.");
            }
        }

    }
