package Model;

import Controller.MainController;

import java.sql.*;
import java.util.ArrayList;

public class SorteigDAO {
    private static final String USER = "hqxqasks";
    private static final String URL = "jdbc:postgresql://surus.db.elephantsql.com/hqxqasks";

    private static final String PASSWD = "EAN1OdZSD9saYTKheIAYSG7J6r3PVZZw";


    public static boolean insertSorteig(Sorteig sorteig) {
        Connection dbconnection;
        PreparedStatement statement;
        ResultSet result;
        int result2;
        String sql;
        Date dataTemporal;

        try {
            sql = "SELECT DataSorteig FROM Sorteig WHERE DataSorteig = ?";
            dbconnection = DriverManager.getConnection(URL, USER, PASSWD);
            statement = dbconnection.prepareStatement(sql);
            dataTemporal = new Date(sorteig.getDataSorteig().getTime());
            statement.setDate(1, dataTemporal);
            result = statement.executeQuery();

            if (result.next()) {
                return false;
            } else {
                sql = "INSERT INTO Sorteig (DataSorteig) VALUES (?);";
                statement = dbconnection.prepareStatement(sql);
                statement.setDate(1, dataTemporal);
                result2 = statement.executeUpdate();
            }

            result.close();
            statement.close();
            dbconnection.close();
        } catch (SQLException sqle) {
            System.err.println("Error SQL tet ");
            sqle.printStackTrace();
            return false;
        }
        return result2 != -1;
    }


    public static ArrayList<Sorteig> selectSortejosNoRealitzats() {
        String sql = "SELECT DataSorteig FROM Sorteig WHERE Realitzat = false";
        ArrayList<Sorteig> sortjos = new ArrayList<>();

        try (Connection dbconnection = DriverManager.getConnection(URL, USER, PASSWD);
             PreparedStatement statement = dbconnection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                sortjos.add(new Sorteig(result.getDate(1)));
            }

        } catch (SQLException sqle) {
            System.err.println("SQL error occurred while retrieving non-executed Sorteigs");
            sqle.printStackTrace();
        }

        return sortjos;
    }


    public static ArrayList<Sorteig> selectSortejosRealitzats() {
        String sql = "SELECT DataSorteig FROM Sorteig WHERE Realitzat = true";
        ArrayList<Sorteig> sortejosRealitzats = new ArrayList<>();

        try (Connection dbconnection = DriverManager.getConnection(URL, USER, PASSWD);
             PreparedStatement statement = dbconnection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                sortejosRealitzats.add(new Sorteig(result.getDate(1)));
            }

        } catch (SQLException sqle) {
            System.err.println("SQL error occurred while retrieving executed Sorteigs");
            sqle.printStackTrace();
        }

        return sortejosRealitzats;
    }

    public static ArrayList<Sorteig> selectTotsSortejos() {
        String sql = "SELECT DataSorteig, Realitzat FROM Sorteig";
        ArrayList<Sorteig> sortejos = new ArrayList<>();

        try (Connection dbconnection = DriverManager.getConnection(URL, USER, PASSWD);
             PreparedStatement statement = dbconnection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                sortejos.add(new Sorteig(result.getDate(1)));
            }

        } catch (SQLException sqle) {
            System.err.println("SQL error occurred while retrieving all Sorteigs");
            sqle.printStackTrace();
        }

        return sortejos;
    }

    public static boolean realitzarSorteig(Date diadelsorteig) {
        String updateSql = "UPDATE Sorteig SET Realitzat = true WHERE DataSorteig = ?";
        String insertSql = "INSERT INTO Premiada VALUES (?, ?, ?)";

        try (Connection dbconnection = DriverManager.getConnection(URL, USER, PASSWD);
             PreparedStatement updateStatement = dbconnection.prepareStatement(updateSql);
             PreparedStatement insertStatement = dbconnection.prepareStatement(insertSql)) {

            // Realizar la actualización del Sorteig
            updateStatement.setDate(1, diadelsorteig);
            int result1 = updateStatement.executeUpdate();

            // Insertar datos del ganador
            Combinacio combi = new Combinacio();
            insertStatement.setDate(1, diadelsorteig);
            insertStatement.setString(2, combi.getCombinacio());
            insertStatement.setInt(3, combi.getReintregrament());
            int result2 = insertStatement.executeUpdate();


            if (result1 != 0 && result2 != 0) {
                MainController.selectMarcarPremiats(diadelsorteig);
            }

            return result1 != 0;

        } catch (SQLException sqle) {
            System.err.println("Error SQL tet ");
            sqle.printStackTrace();
            return false;
        }

    }

    public static int nombreApostes(Date date) {
        String sql = "SELECT COUNT(*) AS Nombre FROM Aposta WHERE sorteig = ?";
        int nombreApostes = -1;

        try (Connection dbconnection = DriverManager.getConnection(URL, USER, PASSWD);
             PreparedStatement statement = dbconnection.prepareStatement(sql)) {

            statement.setDate(1, date);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    nombreApostes = result.getInt("Nombre");
                }
            }

        } catch (SQLException sqle) {
            System.err.println("Error SQL tet");
            sqle.printStackTrace();
            nombreApostes = -2;
        }

        return nombreApostes;
    }

    public static int selectCountApostesPremiades(Date date) {
        String sql = "SELECT COUNT(*) AS Numero FROM Aposta WHERE premiada = true AND sorteig = ?";
        int num = -1;

        try (Connection dbconnection = DriverManager.getConnection(URL, USER, PASSWD);
             PreparedStatement statement = dbconnection.prepareStatement(sql)) {

            statement.setDate(1, date);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    num = result.getInt("Nombre");
                }
            }

        } catch (SQLException sqle) {
            System.err.println("Error SQL tet");
            sqle.printStackTrace();
            return -2;
        }

        return num;
    }
    public static int selectCountApostesPremiadesReintegrament(Date date) {
        String sql = "SELECT COUNT(*) AS Numero FROM Aposta WHERE reintegrament != -1 AND sorteig = ?";
        int tmp = -1;

        try (Connection dbconnection = DriverManager.getConnection(URL, USER, PASSWD);
             PreparedStatement statement = dbconnection.prepareStatement(sql)) {

            statement.setDate(1, date);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    tmp = result.getInt("Nombre");
                }
            }

        } catch (SQLException sqle) {
            System.err.println("Error SQL tet");
            sqle.printStackTrace();
            return -2;
        }

        return tmp;
    }

//premi corresponent a l'aposta
    public static String selectQunatitatPremiGuanyat(Aposta aposta, Date data) {
        String sql = "SELECT Combinacio, Reintegro FROM Premiada WHERE sorteig = ?";
        String premi = "";
        int[] guanyador = new int[6];
        int[] combi = new int[6];
        int reintegro;

        try (Connection dbconnection = DriverManager.getConnection(URL, USER, PASSWD);
             PreparedStatement statement = dbconnection.prepareStatement(sql)) {

            statement.setDate(1, data);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    String[] tmp = result.getString("Combinacio").split(" ");
                    reintegro = result.getInt("Reintegro");
                    for (int i = 0; i < guanyador.length; i++) {
                        guanyador[i] = Integer.parseInt(tmp[i]);
                    }
                    tmp = aposta.getCombinacio().getCombinacio().split(" ");
                    for (int i = 0; i < combi.length; i++) {
                        combi[i] = Integer.parseInt(tmp[i]);
                    }

                    int encerts = MainController.numeroEncerts(combi, guanyador);
                    boolean reintegroGuanyat = (reintegro == aposta.getCombinacio().getReintregrament());

                    if (encerts == 6) {
                        premi = "PREMI!!! COMBINACIO COMPLETA!";
                    } else if (encerts == 5) {
                        premi = "PREMI!!! 5 ENCERTS!!";
                    } else if (encerts == 4) {
                        premi = "PREMI!!! 4 ENCERTS!!";
                    } else if (encerts == 3) {
                        premi = "No premiat... 3 encerts...";
                    } else if (encerts == 2) {
                        premi = "No premiat... 2 encerts...";
                    } else if (encerts == 1) {
                        premi = "No premiat... 1 encert...";
                    }

                    if (reintegroGuanyat) premi += "\nREINTEGRAMENT!!!";
                    else premi += "\nNo reintegro...";
                }
            }

        } catch (SQLException sqle) {
            System.err.println("Error SQL tet ");
            sqle.printStackTrace();
        }
        return premi;
    }




}
