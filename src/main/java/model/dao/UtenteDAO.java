package model.dao;

import model.bean.Utente;
import java.sql.*;
import java.time.Instant;
import java.util.Optional;

public class UtenteDAO {
    private Connection connection;

    public UtenteDAO(Connection connection) {
        this.connection = connection;
    }

    public Optional<Utente> getUtenteByEmail(String email) {
        String query = "SELECT * FROM utenti WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Utente(
                        rs.getInt("idUtente"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("password"),
                        Instant.ofEpochSecond(rs.getInt("dataIscrizione"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean registerUtente(Utente utente) {
        String query = "INSERT INTO utenti (nome, cognome, email, password, dataIscrizione) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, utente.getNome());
            stmt.setString(2, utente.getCognome());
            stmt.setString(3, utente.getEmail());
            stmt.setString(4, utente.getPassword());  // La password sarà hashata prima di essere passata
            stmt.setInt(5, (int) utente.getDataIscrizione().getEpochSecond());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
