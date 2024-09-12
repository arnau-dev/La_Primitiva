package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Model.Sorteig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JocController {

    // La funcio iniciar. Iniciar els sortejos que s'hagin expirat la data i els que siguin actius els realitza
    //automaticament al iniciar el joc
    @FXML
    public void initialize() {
        ArrayList<Sorteig> sorteigsNoRealitzats = MainController.selectSortejosNoRealitzats();

        for (Sorteig sorteig : sorteigsNoRealitzats) {
            if (sorteig.getDataSorteig().before(new Date())) {
                java.sql.Date sqlDate = new java.sql.Date(sorteig.getDataSorteig().getTime());
                MainController.realitzarSorteig(sqlDate);
            }
        }
    }

    //realitzarAposta aquesta funcio obra la finestra aposta a traves del voto realitzar aposta
    @FXML
    public void realitzarAposta() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form2-apostar.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Apostar");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //consultarResultats obra la finestra per consultar les apostes premiades
    @FXML
    public void consultarResultats() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form3-premiats.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Premiats");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //comensarSorteig obra la finestra del boto sorteig
    @FXML
    public void comensarSorteig() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form4-sorteig.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Sorteig");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
