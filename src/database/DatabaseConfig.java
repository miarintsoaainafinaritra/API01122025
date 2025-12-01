package database;

public class DatabaseConfig {
    private String url;
    private String username;
    private String password;
    private String driver;

    // Constructeur avec paramètres
    public DatabaseConfig(String url, String username, String password, String driver) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.driver = driver;
    }

    // Constructeur par défaut avec valeurs prédéfinies
    public DatabaseConfig() {
        this.url = "jdbc:mysql://localhost:3306/studydb";
        this.username = "root";
        this.password = "";
        this.driver = "com.mysql.cj.jdbc.Driver";
    }

    // Getters
    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDriver() {
        return driver;
    }

    // Setters
    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }
}