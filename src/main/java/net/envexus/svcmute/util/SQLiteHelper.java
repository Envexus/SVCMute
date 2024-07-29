package net.envexus.svcmute.util;

import net.envexus.svcmute.SVCMute;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteHelper {
    private static final String URL = "jdbc:sqlite:plugins/SVCMute/mutes.db";

    public SQLiteHelper(SVCMute svcMute) {
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS mutes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "uuid TEXT NOT NULL," +
                    "unmute_time LONG NOT NULL)";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addMute(String uuid, long unmuteTime) {
        String sql = "INSERT INTO mutes(uuid, unmute_time) VALUES(?, ?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid);
            pstmt.setLong(2, unmuteTime);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Long getUnmuteTime(String uuid) {
        String sql = "SELECT unmute_time FROM mutes WHERE uuid = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getLong("unmute_time");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removeMute(String uuid) {
        String sql = "DELETE FROM mutes WHERE uuid = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
