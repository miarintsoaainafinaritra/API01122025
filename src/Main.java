import dao.StudyDAO;
import models.Study;
import database.DatabaseConnection;
import database.DatabaseConfig;

import java.util.List;
import java.util.Scanner;

public class Main {
    private StudyDAO studyDAO;
    private Scanner scanner;

    public Main(StudyDAO studyDAO) {
        this.studyDAO = studyDAO;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        // Créer la table si elle n'existe pas
        studyDAO.createTable();

        boolean running = true;

        while (running) {
            showMenu();
            int choice = getIntInput("Choisissez une option: ");

            switch (choice) {
                case 1:
                    addStudy();
                    break;
                case 2:
                    listAllStudies();
                    break;
                case 3:
                    searchStudyById();
                    break;
                case 4:
                    searchStudiesByTitle();
                    break;
                case 5:
                    updateStudy();
                    break;
                case 6:
                    deleteStudy();
                    break;
                case 7:
                    showStatistics();
                    break;
                case 8:
                    running = false;
                    System.out.println("Au revoir!");
                    break;
                default:
                    System.out.println("Option invalide!");
            }

            pause();
        }

        scanner.close();
    }

    private void showMenu() {
        System.out.println("\n=== GESTION DES ÉTUDES ===");
        System.out.println("1. Ajouter une nouvelle étude");
        System.out.println("2. Lister toutes les études");
        System.out.println("3. Rechercher une étude par ID");
        System.out.println("4. Rechercher des études par titre");
        System.out.println("5. Mettre à jour une étude");
        System.out.println("6. Supprimer une étude");
        System.out.println("7. Statistiques");
        System.out.println("8. Quitter");
    }

    private void addStudy() {
        System.out.println("\n=== AJOUTER UNE NOUVELLE ÉTUDE ===");

        System.out.print("Titre: ");
        String title = scanner.nextLine();

        System.out.print("Description: ");
        String description = scanner.nextLine();

        int duration = getIntInput("Durée (en heures): ");

        Study newStudy = new Study(0, title, description, duration);
        boolean success = studyDAO.addStudy(newStudy);

        if (success) {
            System.out.println("Étude ajoutée avec succès!");
        } else {
            System.out.println("Erreur lors de l'ajout de l'étude.");
        }
    }

    private void listAllStudies() {
        System.out.println("\n=== LISTE DES ÉTUDES ===");

        List<Study> studies = studyDAO.getAllStudies();

        if (studies.isEmpty()) {
            System.out.println("Aucune étude trouvée.");
        } else {
            System.out.println("Nombre total d'études: " + studies.size());
            System.out.println("----------------------------------------");

            for (Study study : studies) {
                System.out.println("ID: " + study.getId());
                System.out.println("Titre: " + study.getTitle());
                System.out.println("Description: " + study.getDescription());
                System.out.println("Durée: " + study.getDuration() + " heures");
                System.out.println("----------------------------------------");
            }
        }
    }

    private void searchStudyById() {
        System.out.println("\n=== RECHERCHER UNE ÉTUDE PAR ID ===");

        int id = getIntInput("ID de l'étude: ");
        Study study = studyDAO.getStudyById(id);

        if (study != null) {
            System.out.println("\nÉtude trouvée:");
            System.out.println(study);
        } else {
            System.out.println("Aucune étude trouvée avec l'ID: " + id);
        }
    }

    private void searchStudiesByTitle() {
        System.out.println("\n=== RECHERCHER DES ÉTUDES PAR TITRE ===");

        System.out.print("Mot-clé à rechercher: ");
        String keyword = scanner.nextLine();

        List<Study> studies = studyDAO.searchStudiesByTitle(keyword);

        if (studies.isEmpty()) {
            System.out.println("Aucune étude trouvée avec le mot-clé: " + keyword);
        } else {
            System.out.println("Résultats de la recherche (" + studies.size() + " études):");
            System.out.println("----------------------------------------");

            for (Study study : studies) {
                System.out.println("ID: " + study.getId());
                System.out.println("Titre: " + study.getTitle());
                System.out.println("Description: " + study.getDescription());
                System.out.println("Durée: " + study.getDuration() + " heures");
                System.out.println("----------------------------------------");
            }
        }
    }

    private void updateStudy() {
        System.out.println("\n=== METTRE À JOUR UNE ÉTUDE ===");

        int id = getIntInput("ID de l'étude à mettre à jour: ");

        Study studyToUpdate = studyDAO.getStudyById(id);
        if (studyToUpdate != null) {
            System.out.println("\nÉtude actuelle:");
            System.out.println(studyToUpdate);

            System.out.println("\nNouvelles valeurs (laissez vide pour conserver la valeur actuelle):");

            System.out.print("Nouveau titre: ");
            String newTitle = scanner.nextLine();
            if (!newTitle.isEmpty()) {
                studyToUpdate.setTitle(newTitle);
            }

            System.out.print("Nouvelle description: ");
            String newDescription = scanner.nextLine();
            if (!newDescription.isEmpty()) {
                studyToUpdate.setDescription(newDescription);
            }

            System.out.print("Nouvelle durée (0 pour conserver): ");
            int newDuration = getIntInput("");
            if (newDuration > 0) {
                studyToUpdate.setDuration(newDuration);
            }

            boolean success = studyDAO.updateStudy(studyToUpdate);
            if (success) {
                System.out.println("Étude mise à jour avec succès!");
            } else {
                System.out.println("Erreur lors de la mise à jour de l'étude.");
            }
        } else {
            System.out.println("Aucune étude trouvée avec l'ID: " + id);
        }
    }

    private void deleteStudy() {
        System.out.println("\n=== SUPPRIMER UNE ÉTUDE ===");

        int id = getIntInput("ID de l'étude à supprimer: ");

        System.out.print("Êtes-vous sûr de vouloir supprimer cette étude? (O/N): ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("O") || confirmation.equalsIgnoreCase("OUI")) {
            boolean success = studyDAO.deleteStudy(id);
            if (success) {
                System.out.println("Étude supprimée avec succès!");
            } else {
                System.out.println("Erreur lors de la suppression de l'étude.");
            }
        } else {
            System.out.println("Suppression annulée.");
        }
    }

    private void showStatistics() {
        System.out.println("\n=== STATISTIQUES ===");

        int totalStudies = studyDAO.countStudies();
        List<Study> studies = studyDAO.getAllStudies();

        System.out.println("Nombre total d'études: " + totalStudies);

        if (!studies.isEmpty()) {
            int totalDuration = 0;
            for (Study study : studies) {
                totalDuration += study.getDuration();
            }

            double averageDuration = (double) totalDuration / totalStudies;

            System.out.println("Durée totale de toutes les études: " + totalDuration + " heures");
            System.out.println("Durée moyenne d'une étude: " + String.format("%.2f", averageDuration) + " heures");
        }
    }

    private int getIntInput(String message) {
        System.out.print(message);
        while (!scanner.hasNextInt()) {
            System.out.print("Veuillez entrer un nombre valide: ");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne
        return value;
    }

    private void pause() {
        System.out.print("\nAppuyez sur Entrée pour continuer...");
        scanner.nextLine();
    }

    public static void main(String[] args) {
        // Configuration de la base de données
        DatabaseConfig config = new DatabaseConfig();

        // Vous pouvez modifier la configuration ici si nécessaire
        // config.setUrl("jdbc:mysql://localhost:3306/studydb");
        // config.setUsername("votre_utilisateur");
        // config.setPassword("votre_mot_de_passe");

        // Création de la connexion à la base de données
        DatabaseConnection dbConnection = new DatabaseConnection(config);

        // Création du DAO
        StudyDAO studyDAO = new StudyDAO(dbConnection);

        // Création et démarrage de l'application
        Main app = new Main(studyDAO);
        app.start();

        // Fermeture de la connexion à la base de données
        dbConnection.closeConnection();
    }
}