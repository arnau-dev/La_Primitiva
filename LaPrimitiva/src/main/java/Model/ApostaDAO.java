package Model;

import Controller.MainController;

import java.sql.*;
import java.util.Date;

public class ApostaDAO {
    private static final String USER = "hqxqasks";
    private static final String URL = "jdbc:postgresql://surus.db.elephantsql.com/hqxqasks";

    private static final String PASSWD = "EAN1OdZSD9saYTKheIAYSG7J6r3PVZZw";





    public static Combinacio selectCombinacioAposta(Date data, int id) {
        String sql = "SELECT Combinacio, Reintegrament FROM Aposta WHERE identificador = ? AND sorteig = ?";
        Combinacio combi = null;

        try (Connection dbconnection = DriverManager.getConnection(URL, USER, PASSWD);
             PreparedStatement statement = dbconnection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.setDate(2, new java.sql.Date(data.getTime()));
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                String combinacioStr = result.getString("Combinacio");
                String[] combinacioArray = combinacioStr.split(" ");
                int[] combinacioInts = new int[combinacioArray.length];
                for (int i = 0; i < combinacioArray.length; i++) {
                    combinacioInts[i] = Integer.parseInt(combinacioArray[i]);
                }

                combi = new Combinacio(combinacioInts, result.getInt("Reintegrament"));
            }

            result.close();
        } catch (SQLException sqle) {
            System.err.println("Error SQL:");
            sqle.printStackTrace();
        }

        return combi;
    }
    public static boolean inserirAposta(Aposta aposta, Date data) {
        String selectSql = "SELECT MAX(Identificador) AS ID FROM Aposta WHERE sorteig = ?";
        String insertSql = "INSERT INTO Aposta VALUES(?,?,?,?)";

        try (Connection dbconnection = DriverManager.getConnection(URL, USER, PASSWD);
             PreparedStatement selectStatement = dbconnection.prepareStatement(selectSql);
             PreparedStatement insertStatement = dbconnection.prepareStatement(insertSql)) {


            selectStatement.setDate(1, new java.sql.Date(data.getTime()));
            ResultSet resultID = selectStatement.executeQuery();

            if (resultID.next()) {
                aposta.setIdentificador(resultID.getInt("ID") + 1);
            } else {
                aposta.setIdentificador(1);
            }


            insertStatement.setInt(1, aposta.getIdentificador());
            insertStatement.setDate(2, new java.sql.Date(data.getTime()));
            Combinacio combi = aposta.getCombinacio();
            insertStatement.setString(3, combi.getCombinacio());
            insertStatement.setInt(4, combi.getReintregrament());


            int result = insertStatement.executeUpdate();
            return result != 0;

        } catch (SQLException sqle) {
            System.err.println("Error SQL:");
            sqle.printStackTrace();
            return false;
        }
    }

    public static boolean selectApostaCobrada(int id, java.sql.Date data) {
        String sql = "SELECT Cobrada FROM Aposta A INNER JOIN Sorteig S ON A.sorteig = S.datasorteig WHERE A.Identificador = ? AND S.DataSorteig = ?";

        try (Connection dbconnection = DriverManager.getConnection(URL, USER, PASSWD);
             PreparedStatement statement = dbconnection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.setDate(2, data);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return result.getBoolean("Cobrada");
                }
            }
        } catch (SQLException sqle) {
            System.err.println("Error SQL:");
            sqle.printStackTrace();
        }
        return false;
    }
    public static boolean selectComprobarApostaPremiada(int id, Date data) {
        String sql = "SELECT premiada FROM Aposta WHERE identificador = ? AND sorteig = ?";

        try (Connection dbconnection = DriverManager.getConnection(URL, USER, PASSWD);
             PreparedStatement statement = dbconnection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.setDate(2, new java.sql.Date(data.getTime()));

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return result.getBoolean("premiada");
                }
            }
        } catch (SQLException sqle) {
            System.err.println("Error SQL:");
            sqle.printStackTrace();
        }
        return false;
    }

    public static boolean updateCobradaAposta(int id, java.sql.Date data) {
        String sql = "UPDATE Aposta SET cobrada = true WHERE identificador = ? AND sorteig = ?";
        int result = 0;

        try (Connection dbconnection = DriverManager.getConnection(URL, USER, PASSWD);
             PreparedStatement statement = dbconnection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.setDate(2, new java.sql.Date(data.getTime()));
            result = statement.executeUpdate();

        } catch (SQLException sqle) {
            System.err.println("Error SQL:");
            sqle.printStackTrace();
        }

        return (result > 0);
    }

    public static void selectMarcarPremiats(Date data) {
        String sqlAposta = "SELECT * FROM Aposta WHERE sorteig = ?";
        String sqlGuanyador = "SELECT * FROM Guanyador WHERE sorteig = ?";

        try (Connection dbconnection = DriverManager.getConnection(URL, USER, PASSWD);
             PreparedStatement statementAposta = dbconnection.prepareStatement(sqlAposta);
             PreparedStatement statementGuanyador = dbconnection.prepareStatement(sqlGuanyador)) {

            statementAposta.setDate(1, new java.sql.Date(data.getTime()));
            ResultSet resultAposta = statementAposta.executeQuery();

            while (resultAposta.next()) {
                int[] normal = new int[6];
                int[] guanyador = new int[6];

                String[] combinacioPremi = resultAposta.getString("Combinacio").split(" ");

                statementGuanyador.setDate(1, new java.sql.Date(data.getTime()));
                ResultSet resultGuanyador = statementGuanyador.executeQuery();

                if (resultGuanyador.next()) {
                    String[] combinacioPremi2 = resultGuanyador.getString("Combinacio").split(" ");
                    for (int i = 0; i < combinacioPremi.length; i++) {
                        normal[i] = Integer.parseInt(combinacioPremi[i]);
                        guanyador[i] = Integer.parseInt(combinacioPremi2[i]);
                    }
                    if (MainController.numeroEncerts(normal, guanyador) > 0 || resultAposta.getInt("Reintegrament") == resultGuanyador.getInt("Reintegro")) {
                        String updateSql = "UPDATE Aposta SET premiada = true WHERE identificador = ? AND sorteig = ?";
                        try (PreparedStatement updateStatement = dbconnection.prepareStatement(updateSql)) {
                            updateStatement.setInt(1, resultAposta.getInt("Identificador"));
                            updateStatement.setDate(2, new java.sql.Date(data.getTime()));
                            updateStatement.executeUpdate();
                        }
                    }
                }
                resultGuanyador.close();
            }
            resultAposta.close();
        } catch (SQLException sqle) {
            System.err.println("Error SQL:");
            sqle.printStackTrace();
        }
    }


    public static int selectCountPremiadaNoCobrada(java.sql.Date date) {
        String sql = "SELECT COUNT(Identificador) AS ID FROM Aposta WHERE sorteig = ? AND premiada = true AND cobrada = false";
        int count = -1;

        try (Connection dbconnection = DriverManager.getConnection(URL, USER, PASSWD);
             PreparedStatement statement = dbconnection.prepareStatement(sql)) {

            statement.setDate(1, date);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    count = result.getInt("ID");
                }
            }
        } catch (SQLException sqle) {
            System.err.println("Error SQL:");
            sqle.printStackTrace();
        }
        return count;
    }


}
