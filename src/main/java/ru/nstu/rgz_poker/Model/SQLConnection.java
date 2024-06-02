package ru.nstu.rgz_poker.Model;

import javafx.scene.control.ListView;
import javafx.scene.control.TableView;

import java.sql.*;

public class SQLConnection {
    public static final String URL = "jdbc:postgresql://localhost:5432/Poker_players_stat";
    public static final String USERNAME = "postgres";
    public static final String PASSWORD = "199ovohah";

    public static void insertPlayer(String playerName, Timestamp lastLogin, int outChip) {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement ps = conn.prepareStatement("INSERT INTO Player (playerName, lastLogin, outChip) VALUES (?, ?, ?)")) {

            ps.setString(1, playerName);
            ps.setTimestamp(2, lastLogin);
            ps.setInt(3, outChip);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getPlayer(String name, TableView table) {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement ps = conn.prepareStatement("SELECT playerName, lastLogin, outChip FROM Player WHERE playerName = ?")) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String playerName = rs.getString("playerName");
                Timestamp lastLogin = rs.getTimestamp("lastLogin");
                int outChip = rs.getInt("outChip");
                System.out.println("Player Name: " + playerName);
                System.out.println("Last Login: " + lastLogin);
                System.out.println("Out Chip: " + outChip);
            } else {
                System.out.println("Player not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
