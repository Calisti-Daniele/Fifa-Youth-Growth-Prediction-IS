package model.dao;

import model.bean.FifaPlayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FifaPlayerDAO {
    private Connection connection;

    public FifaPlayerDAO(Connection connection) {
        this.connection = connection;
    }

    /** Aggiunge un giocatore nel database */
    public boolean addPlayer(FifaPlayer player) {
        String query = "INSERT INTO fifa_players (nome, overall, shooting, passing, dribbling, defending, physic, long_name, player_url, short_name, player_positions, nationality_name, preferred_foot) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, player.getNome());
            stmt.setString(2, player.getOverall());
            stmt.setString(3, player.getShooting());
            stmt.setString(4, player.getPassing());
            stmt.setString(5, player.getDribbling());
            stmt.setString(6, player.getDefending());
            stmt.setString(7, player.getPhysic());
            stmt.setString(8, player.getLongName());
            stmt.setString(9, player.getPlayerUrl());
            stmt.setString(10, player.getShortName());
            stmt.setString(11, player.getPlayerPositions());
            stmt.setString(12, player.getNationalityName());
            stmt.setString(13, player.getPreferredFoot());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Recupera un giocatore per nome */
    public FifaPlayer getPlayerByName(String nome) {
        String query = "SELECT * FROM fifa_players WHERE nome = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractPlayerFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Recupera tutti i giocatori */
    public List<FifaPlayer> getAllPlayers() {
        List<FifaPlayer> players = new ArrayList<>();
        String query = "SELECT * FROM fifa_players";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                players.add(extractPlayerFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }

    /** 🔥 Recupera giocatori con nome LIKE '%nome%' 🔥 */
    public List<FifaPlayer> getPlayersByNameLike(String searchQuery) {
        List<FifaPlayer> players = new ArrayList<>();
        String query = "SELECT * FROM fifa_players WHERE nome LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + searchQuery + "%"); // Aggiunge il LIKE '%nome%'
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                players.add(extractPlayerFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }

    /** Aggiorna un giocatore */
    public boolean updatePlayer(FifaPlayer player) {
        String query = "UPDATE fifa_players SET overall=?, shooting=?, passing=?, dribbling=?, defending=?, physic=?, long_name=?, player_url=?, short_name=?, player_positions=?, nationality_name=?, preferred_foot=? WHERE nome=?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, player.getOverall());
            stmt.setString(2, player.getShooting());
            stmt.setString(3, player.getPassing());
            stmt.setString(4, player.getDribbling());
            stmt.setString(5, player.getDefending());
            stmt.setString(6, player.getPhysic());
            stmt.setString(7, player.getLongName());
            stmt.setString(8, player.getPlayerUrl());
            stmt.setString(9, player.getShortName());
            stmt.setString(10, player.getPlayerPositions());
            stmt.setString(11, player.getNationalityName());
            stmt.setString(12, player.getPreferredFoot());
            stmt.setString(13, player.getNome());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Elimina un giocatore */
    public boolean deletePlayer(String nome) {
        String query = "DELETE FROM fifa_players WHERE nome = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nome);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Metodo di supporto per estrarre un giocatore da un ResultSet */
    private FifaPlayer extractPlayerFromResultSet(ResultSet rs) throws SQLException {
        return new FifaPlayer(
                rs.getString("nome"),
                rs.getString("overall"),
                rs.getString("shooting"),
                rs.getString("passing"),
                rs.getString("dribbling"),
                rs.getString("defending"),
                rs.getString("physic"),
                rs.getString("long_name"),
                rs.getString("player_url"),
                rs.getString("short_name"),
                rs.getString("player_positions"),
                rs.getString("nationality_name"),
                rs.getString("preferred_foot")
        );
    }

    public String loadImage(String playerUrl) {
        String root = "https://cdn.sofifa.net/players/{token_1}/{token_2}/24_120.png";

        // Esegue il match per estrarre l'ID del giocatore dall'URL
        Pattern pattern = Pattern.compile("player/(\\d+)");
        Matcher matcher = pattern.matcher(playerUrl);

        if (matcher.find()) {
            String playerId = matcher.group(1);

            // Controlla che l'ID sia lungo almeno 6 caratteri
            if (playerId.length() >= 6) {
                String token_1 = playerId.substring(0, 3);
                String token_2 = playerId.substring(3, 6);

                // Sostituisce i placeholder con i token corretti
                return root.replace("{token_1}", token_1).replace("{token_2}", token_2);
            }
        }

        return null; // Se non trova l'ID o è troppo corto, restituisce null
    }

}
