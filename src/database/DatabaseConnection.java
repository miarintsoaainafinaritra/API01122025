package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private Connection connection;
    private DatabaseConfig config;


    public DatabaseConnection(DatabaseConfig config) {
        this.config = config;
        this.connection = null;
    }

   
    public Connection getConnection() {
        if (connection == null) {
            try {
          
                Class.forName(config.getDriver());

                connection = DriverManager.getConnection(
                        config.getUrl(),
                        config.getUsername(),
                        config.getPassword()
                );

                System.out.println("Connexion à la base de données établie avec succès!");

            } catch (ClassNotFoundException e) {
                System.err.println("Driver JDBC non trouvé: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("Erreur de connexion à la base de données: " + e.getMessage());
            }
        }
        return connection;
    }
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Connexion à la base de données fermée.");
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
            }
        }
    }

    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
    public void reconnect() {
        closeConnection();
        getConnection();
    }
}
