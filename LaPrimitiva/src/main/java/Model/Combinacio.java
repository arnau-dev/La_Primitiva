package Model;

import javafx.scene.control.Alert;

import java.util.Arrays;
import java.util.Random;

public class Combinacio {
    int combinacio[];
    int reintregrament;


    public Combinacio() {
        Random rnd = new Random();
        combinacio = generarCombinacioAleatoria();
        reintregrament = rnd.nextInt(10);
    }

    public String getCombinacio() {
        String result = "";
        for (int i = 0; i < 6; i++) {
            if (combinacio[i] < 10) {
                result += "0" + combinacio[i];
            } else {
                result += combinacio[i];
            }
            if (i < 5) {
                result += " ";
            }
        }
        return result;
    }

    private void ordenarCombinacio(int[] numeros) {
        Arrays.sort(numeros);
    }

    public int getReintregrament() {
        return this.reintregrament;
    }

    public Combinacio(int[] combinacio, int reintregrament) {
        try {
            if (combinacio.length != 6) {
                throw new IllegalArgumentException("La combinació ha de tenir minim 6 números");
            } else {
                for (int i = 0; i < combinacio.length; i++) {
                    if (combinacio[i] < 1 || combinacio[i] > 49)
                        throw new IllegalArgumentException("Els números de la combinació han de estar entre 1 i 49");
                }
            }
            this.combinacio = new int[6];
            for (int i = 0; i < 6; i++) {
                this.combinacio[i] = combinacio[i];
            }
            ordenarCombinacio(this.combinacio);
            if (reintregrament < -1 || reintregrament > 9)
                throw new IllegalArgumentException("El número de reintegrament ha de estar entre 0 i 9");
        } catch (IllegalArgumentException iae) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Advertencia");
            alert.setHeaderText("Error en l'aposta");
            alert.setContentText("Fixat en el format de la combinació introduida\n" + iae.getMessage());
            alert.showAndWait();
        }
        this.reintregrament = reintregrament;
    }

    public int[] generarCombinacioAleatoria() {
        int[] combinacioAleatoria = new int[6];
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            int num;
            boolean repetit;
            do {
                num = random.nextInt(49) + 1;
                repetit = false;
                for (int j = 0; j < i; j++) {
                    if (combinacioAleatoria[j] == num) {
                        repetit = true;
                        break;
                    }
                }
            } while (repetit);
            combinacioAleatoria[i] = num;
        }
        ordenarCombinacio(combinacioAleatoria);
        return combinacioAleatoria;
    }
}
