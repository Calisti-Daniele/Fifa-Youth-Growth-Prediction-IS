package model.bean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Parametri per la connessione al database
    private static final String URL = "jdbc:mysql://localhost:3306/Fifa-youth-growth-prediction";
    private static final String USERNAME = "root"; // Sostituisci con l'utente del tuo database
    private static final String PASSWORD = ""; // Sostituisci con la password del tuo database

    private static Connection connection;

    // Metodo per ottenere la connessione al database
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Caricamento del driver JDBC per MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");
                // Creazione della connessione
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Connessione al database stabilita con successo.");
            } catch (ClassNotFoundException e) {
                System.err.println("Errore: Driver JDBC non trovato.");
                e.printStackTrace();
            } catch (SQLException e) {
                System.err.println("Errore: Impossibile connettersi al database ");
                e.printStackTrace();
            }
        }
        return connection;
    }

    // Metodo per chiudere la connessione al database
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connessione al database chiusa.");
            } catch (SQLException e) {
                System.err.println("Errore durante la chiusura della connessione.");
                e.printStackTrace();
            }
        }
    }
}
