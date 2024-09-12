package Controller;

import Model.Sorteig;
import Model.*;

import java.util.ArrayList;
import java.util.Date;

public class MainController {

    public static boolean insertarSorteig(Sorteig sorteig) {
        return SorteigDAO.insertSorteig(sorteig);
    }

    public static ArrayList<Sorteig> selectSortejosNoRealitzats() {
        return SorteigDAO.selectSortejosNoRealitzats();
    }

    public static ArrayList<Sorteig> selectSortejosRealitzats() {
        return SorteigDAO.selectSortejosRealitzats();
    }

    public static ArrayList<Sorteig> selectTotsSortejos() {
        return SorteigDAO.selectTotsSortejos();
    }

    public static boolean realitzarSorteig(java.sql.Date date) {
        return SorteigDAO.realitzarSorteig(date);
    }

    public static boolean selectApostaCobrada(int id, java.sql.Date data) {
        return ApostaDAO.selectApostaCobrada(id, data);
    }

    public static boolean afegirAposta(Aposta aposta, Date data) {
        return ApostaDAO.inserirAposta(aposta, data);
    }

    public static Combinacio selectCombinacioAposta(Date data, int id) {
        return ApostaDAO.selectCombinacioAposta(data, id);
    }

    public static int nombreApostes(java.sql.Date date) {
        return SorteigDAO.nombreApostes(date);
    }

    public static int selectCountApostesPremiades(java.sql.Date date) {
        return SorteigDAO.selectCountApostesPremiades(date);
    }

    public static int selectCountApostesPremiadesReintegrament(java.sql.Date date) {
        return SorteigDAO.selectCountApostesPremiadesReintegrament(date);
    }

    public static boolean selectComprobarApostaPremiada(int id, Date data) {
        return ApostaDAO.selectComprobarApostaPremiada(id, data);
    }

    public static String selectQunatitatPremiGuanyat(Aposta aposta, java.sql.Date data) {
        return SorteigDAO.selectQunatitatPremiGuanyat(aposta, data);
    }

    public static boolean updateCobradaAposta(int id, java.sql.Date data) {
        return ApostaDAO.updateCobradaAposta(id, data);
    }

    public static void selectMarcarPremiats(Date data) {
        ApostaDAO.selectMarcarPremiats(data);
    }
    public static int numeroEncerts(int[] combinacio, int[] ticket) {
        int coincidencies = 0;
        for (int i = 0; i < combinacio.length; i++) {
            for (int j = 0; j < ticket.length; j++) {
                if (combinacio[i] == ticket[j]) {
                    coincidencies++;
                    break;
                }
            }
        }
        return coincidencies;
    }
    public static int selectCountPremiadaNoCobrada(java.sql.Date date) {
        return ApostaDAO.selectCountPremiadaNoCobrada(date);
    }


}
