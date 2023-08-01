package org.radium.guildsplugin.manager.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.md_5.bungee.config.Configuration;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.manager.object.guild.Guild;
import org.radium.guildsplugin.manager.object.member.GuildMember;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class DataManager {
    private final HikariConfig dataSourceConfig = new HikariConfig();
    private HikariDataSource dataSource;

    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }
    public boolean connect() {
        final boolean isConnected = initDatasource();
        if (isConnected) {
            checkTable();
        }
        return isConnected;
    }
    private void checkTable() {
        String query = "CREATE TABLE IF NOT EXISTS "+ GuildMember.TABLE_NAME +"(" +
                "`UUID` varchar(255) NOT NULL," +
                "`NAME` varchar(20) NOT NULL," +
                "`GUILD_ID` INT DEFAULT 0," +
                "`GUILD_RANK` TEXT NOT NULL" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
        String query1 = "CREATE TABLE IF NOT EXISTS "+ Guild.TABLE_NAME +"(" +
                "`ID` INT NOT NULL," +
                "`GUILD_NAME` varchar(64) NOT NULL," +
                "`GUILD_TAG` varchar(10) NOT NULL," +
                "`GUILD_COLOR` varchar(1) NOT NULL," +
                "`GUILD_OWNER` varchar(255) NOT NULL," +
                "`GUILD_KILLS` INT DEFAULT 0," +
                "`GUILD_DEATHS` INT DEFAULT 0," +
                "`GUILD_POINTS` INT DEFAULT 0" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            statement.addBatch(query);
            statement.addBatch(query1);
            statement.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean initDatasource() {
        Configuration config = Core.getInstance().getDatabaseSettings().getConfig();
        try {
            final String driver = "jdbc:mysql://";
            final String host = config.getString("Database.Host");
            final String username = config.getString("Database.Username");
            final String database = config.getString("Database.Database");
            final String databaseProperties = config.getString("Database.DatabaseProperties");
            final String password = config.getString("Database.Password");
            final int port = config.getInt("Database.Port");
            final int maximumPoolSize = config.getInt("Database.MaximumPoolSize");
            dataSourceConfig.setPoolName("Guilds");
            dataSourceConfig.setMaximumPoolSize(maximumPoolSize);
            dataSourceConfig.setIdleTimeout(0);
            dataSourceConfig.setUsername(username);
            dataSourceConfig.setPassword(password);
            dataSourceConfig.setJdbcUrl(driver + host + ":" + port + "/" + database + databaseProperties);
            dataSource = new HikariDataSource(this.dataSourceConfig);
            Core.getInstance().getLogger().log(Level.FINE, "Connected to database!");
            return true;
        } catch (final Exception exception) {
            Core.getInstance().getLogger().log(Level.INFO, "\n" +
                    "------ RADIUM GUILDS ------\n" +
                    "\n" +
                    "Failed to connect to MySQL database. Please check the following:\n" +
                    "- DatabaseSettings.yml is configured correctly\n" +
                    "- The MySQL server is running and accessible\n" +
                    "- The database and credentials specified in DatabaseSettings.yml exist and are correct\n" +
                    "\n" +
                    "------ RADIUM GUILDS ------");
            exception.printStackTrace();
            return false;
        }
    }

    public boolean isClosed() {
        return this.dataSource == null || !this.dataSource.isRunning() || this.dataSource.isClosed();
    }
    public void disconnect() {
        if (this.isClosed()) {
            Core.getInstance().getLogger().log(Level.SEVERE, "Database is already closed!");
        }
        this.dataSource.close();
        Core.getInstance().getLogger().log(Level.FINE, "Successfully disconnected from the database!");
    }
}
