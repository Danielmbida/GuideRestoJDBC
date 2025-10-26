package ch.hearc.ig.guideresto.service;

import ch.hearc.ig.guideresto.business.*;
import ch.hearc.ig.guideresto.persistence.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Set;

/**
 * Couche service pour gérer la logique métier et les transactions
 * entre la présentation et la persistance.
 */
public class GuideRestoService {

    private static final Logger logger = LogManager.getLogger(GuideRestoService.class);

    private Connection connection;
    private RestaurantMapper restaurantMapper;
    private CityMapper cityMapper;
    private RestaurantTypeMapper restaurantTypeMapper;
    private CompleteEvaluationMapper completeEvaluationMapper;
    private GradeMapper gradeMapper;
    private EvaluationCriteriaMapper evaluationCriteriaMapper;
    private BasicEvaluationMapper basicEvaluationMapper;

    /**
     * Constructeur : initialise la connexion et tous les mappers nécessaires
     */
    public GuideRestoService() {
        try {
            this.connection = ConnectionUtils.getConnection();
            this.cityMapper = new CityMapper(connection);
            this.restaurantTypeMapper = new RestaurantTypeMapper(connection);
            this.restaurantMapper = new RestaurantMapper(connection);
            this.evaluationCriteriaMapper = new EvaluationCriteriaMapper(connection);
            this.completeEvaluationMapper = new CompleteEvaluationMapper(connection);
            this.gradeMapper = new GradeMapper(connection);
            this.basicEvaluationMapper = new BasicEvaluationMapper(connection);
        } catch (Exception e) {
            logger.error("Erreur lors de l'initialisation du service : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // ========== RESTAURANTS ==========

    public Set<Restaurant> getAllRestaurants() {
        return restaurantMapper.findAll();
    }

    public Restaurant getRestaurantById(int id) {
        return restaurantMapper.findById(id);
    }

    public Set<Restaurant> getRestaurantByName(String name) {
        return restaurantMapper.findByNameLike(name);
    }

    public Set<Restaurant> getRestaurantByCity(String cityName) {
        return restaurantMapper.findByCity(cityName);
    }

    public Restaurant createRestaurant(Restaurant restaurant) {
        try {
            Restaurant created = restaurantMapper.create(restaurant);
            connection.commit();
            return created;
        } catch (SQLException e) {
            logger.error("Erreur lors de la création du restaurant : {}", e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException ex) {
                logger.error("Erreur lors du rollback : {}", ex.getMessage());
            }
            throw new RuntimeException(e);
        }
    }

    public boolean updateRestaurant(Restaurant restaurant) {
        try {
            boolean result = restaurantMapper.update(restaurant);
            connection.commit();
            return result;
        } catch (SQLException e) {
            logger.error("Erreur lors de la MAJ du restaurant : {}", e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException ex) {
                logger.error("Erreur lors du rollback : {}", ex.getMessage());
            }
            throw new RuntimeException(e);
        }
    }

    public boolean deleteRestaurant(Restaurant restaurant) {
        try {
            boolean result = restaurantMapper.delete(restaurant);
            connection.commit();
            return result;
        } catch (SQLException e) {
            logger.error("Erreur lors de la suppression du restaurant : {}", e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException ex) {
                logger.error("Erreur lors du rollback : {}", ex.getMessage());
            }
            throw new RuntimeException(e);
        }
    }

    // ========== VILLES ==========

    public Set<City> getAllCities() {
        return cityMapper.findAll();
    }

    public City getCitiByZipCode(String zipCode) {
        return cityMapper.findByZipCode(zipCode);
    }

    public City getCityById(int id) {
        return cityMapper.findById(id);
    }

    public City createCity(City city) {
        try {
            City created = cityMapper.create(city);
            connection.commit();
            return created;
        } catch (SQLException e) {
            logger.error("Erreur lors de la création de la ville : {}", e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException ex) {
                logger.error("Erreur lors du rollback : {}", ex.getMessage());
            }
            throw new RuntimeException(e);
        }
    }

    // ========== TYPES GASTRONOMIQUES ==========

    public Set<RestaurantType> getAllRestaurantTypes() {
        return restaurantTypeMapper.findAll();
    }

    public RestaurantType getTypeByLabel(String label) {
        return restaurantTypeMapper.findByLabel(label);
    }

    public RestaurantType getRestaurantTypeById(int id) {
        return restaurantTypeMapper.findById(id);
    }


    // ========== CRITÈRES D'ÉVALUATION ==========

    public Set<EvaluationCriteria> getAllEvaluationCriterias() {
        return evaluationCriteriaMapper.findAll();
    }

    // ========== ÉVALUATIONS BASIQUES ==========

    public BasicEvaluation createBasicEvaluation(BasicEvaluation evaluation) {
        try {
            BasicEvaluation created = basicEvaluationMapper.create(evaluation);
            connection.commit();
            return created;
        } catch (SQLException e) {
            logger.error("Erreur lors de la création de l'évaluation basique : {}", e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException ex) {
                logger.error("Erreur lors du rollback : {}", ex.getMessage());
            }
            throw new RuntimeException(e);
        }
    }

    // ========== ÉVALUATIONS COMPLÈTES ==========

    public CompleteEvaluation createCompleteEvaluation(CompleteEvaluation evaluation) {
        try {
            CompleteEvaluation created = completeEvaluationMapper.create(evaluation);

            // Créer également toutes les notes associées
            for (Grade grade : evaluation.getGrades()) {
                gradeMapper.create(grade);
            }

            connection.commit();
            return created;
        } catch (SQLException e) {
            logger.error("Erreur lors de la création de l'évaluation complète : {}", e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException ex) {
                logger.error("Erreur lors du rollback : {}", ex.getMessage());
            }
            throw new RuntimeException(e);
        }
    }

    // ========== GESTION DE LA CONNEXION ==========

    /**
     * Ferme proprement la connexion JDBC
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("Connexion fermée avec succès");
            }
        } catch (SQLException e) {
            logger.error("Erreur lors de la fermeture de la connexion : {}", e.getMessage());
        }
    }
}
