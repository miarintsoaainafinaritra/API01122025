package dao;

import models.Study;
import database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudyDAO {
    private DatabaseConnection dbConnection;

    // Constructeur avec injection de dépendance
    public StudyDAO(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    // Méthode pour créer la table si elle n'existe pas
    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS studies (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "title VARCHAR(100) NOT NULL, " +
                "description TEXT, " +
                "duration INT NOT NULL)";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Table 'studies' créée ou déjà existante.");

        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la table: " + e.getMessage());
        }
    }

    // Ajouter une nouvelle étude
    public boolean addStudy(Study study) {
        String sql = "INSERT INTO studies (title, description, duration) VALUES (?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, study.getTitle());
            pstmt.setString(2, study.getDescription());
            pstmt.setInt(3, study.getDuration());

            int rowsAffected = pstmt.executeUpdate();

            // Récupérer l'ID généré
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        study.setId(generatedKeys.getInt(1));
                    }
                }
                System.out.println("Étude ajoutée avec succès! ID: " + study.getId());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'étude: " + e.getMessage());
        }
        return false;
    }

    // Récupérer toutes les études
    public List<Study> getAllStudies() {
        List<Study> studies = new ArrayList<>();
        String sql = "SELECT * FROM studies ORDER BY title";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Study study = new Study(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getInt("duration")
                );
                studies.add(study);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des études: " + e.getMessage());
        }

        return studies;
    }

    // Récupérer une étude par son ID
    public Study getStudyById(int id) {
        Study study = null;
        String sql = "SELECT * FROM studies WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                study = new Study(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getInt("duration")
                );
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'étude: " + e.getMessage());
        }

        return study;
    }

    // Rechercher des études par titre
    public List<Study> searchStudiesByTitle(String keyword) {
        List<Study> studies = new ArrayList<>();
        String sql = "SELECT * FROM studies WHERE title LIKE ? ORDER BY title";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Study study = new Study(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getInt("duration")
                );
                studies.add(study);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des études: " + e.getMessage());
        }

        return studies;
    }

    // Mettre à jour une étude
    public boolean updateStudy(Study study) {
        String sql = "UPDATE studies SET title = ?, description = ?, duration = ? WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, study.getTitle());
            pstmt.setString(2, study.getDescription());
            pstmt.setInt(3, study.getDuration());
            pstmt.setInt(4, study.getId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Étude mise à jour avec succès!");
                return true;
            } else {
                System.out.println("Aucune étude trouvée avec l'ID: " + study.getId());
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'étude: " + e.getMessage());
            return false;
        }
    }

    // Supprimer une étude
    public boolean deleteStudy(int id) {
        String sql = "DELETE FROM studies WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Étude supprimée avec succès!");
                return true;
            } else {
                System.out.println("Aucune étude trouvée avec l'ID: " + id);
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'étude: " + e.getMessage());
            return false;
        }
    }

    // Compter le nombre total d'études
    public int countStudies() {
        String sql = "SELECT COUNT(*) FROM studies";
        int count = 0;

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage des études: " + e.getMessage());
        }

        return count;
    }
}