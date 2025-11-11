package pt.sloth.stopPlantGrowth;

import org.bukkit.Location;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DatabaseManager {

    static DatabaseManager instance;
    private Connection connection;
    public static DatabaseManager getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public DatabaseManager() throws SQLException {
        this.connect();
    }

    public boolean connect() throws SQLException {
        if (this.connection == null || this.connection.isClosed()) {
            try {
                Class.forName("org.sqlite.JDBC");
                this.connection = DriverManager.getConnection("jdbc:sqlite:plugins/StopPlantGrowth/block_data.db");
                return setupDatabase();
            } catch (ClassNotFoundException | SQLException e) {
                utils.log("Connection Failed! Check output console");
                e.printStackTrace();
                return true;
            }
        } else
            return false;
    }

    public boolean setupDatabase() throws SQLException {
        if (this.connect()) return false;
        this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS block_data (x INTEGER, y INTEGER, z INTEGER, world TEXT);").executeUpdate();
        return true;
    }
    public void closeConnection() throws SQLException {
        if (this.connection != null && !this.connection.isClosed()) {
            this.connection.close();
        }
    }
    public boolean removeBlock(int x, int y, int z, String world) throws SQLException {
        if (this.connect()) return false;
        PreparedStatement statement = this.connection.prepareStatement("DELETE FROM block_data WHERE x = ? AND y = ? AND z = ? AND world = ?;");
        statement.setInt(1, x);
        statement.setInt(2, y);
        statement.setInt(3, z);
        statement.setString(4, world);
        statement.executeUpdate();
        return true;
    }
    public boolean addBlock(int x, int y, int z, String world) throws SQLException {
        if (this.connect()) return false;
        PreparedStatement statement = this.connection.prepareStatement("INSERT OR IGNORE into block_data (x, y, z, world) VALUES (?, ?, ?, ?);");
        statement.setInt(1, x);
        statement.setInt(2, y);
        statement.setInt(3, z);
        statement.setString(4, world);
        statement.executeUpdate();
        return true;
    }
    public List<Location> loadBlockData() throws SQLException {
        if (this.connect()) return null;
        java.sql.ResultSet rs = this.connection.prepareStatement("SELECT * FROM block_data;").executeQuery();
        List<Location> locations = new java.util.ArrayList<>();
        while (rs.next()) {
            int x = rs.getInt("x");
            int y = rs.getInt("y");
            int z = rs.getInt("z");
            String world = rs.getString("world");
            locations.add(new Location(org.bukkit.Bukkit.getWorld(world), x, y, z));
        }
        return locations;
    }
}