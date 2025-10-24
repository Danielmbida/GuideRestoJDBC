package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.*;
import oracle.jdbc.proxy.annotation.Pre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class RestaurantMapper extends AbstractMapper<Restaurant> {
    private final Connection connection;

    private final RestaurantTypeMapper restaurantTypeMapper;
    private final CityMapper cityMapper;

    public RestaurantMapper(Connection connection) {
        this.connection = connection;
        this.restaurantTypeMapper = new RestaurantTypeMapper(connection);
        this.cityMapper = new CityMapper(connection);
    }

    /**
     * Recherche un restaurant dans la base de données par son identifiant (NUMERO).
     *
     * @param id Identifiant unique du restaurant à rechercher.
     * @return L'objet Restaurant correspondant, ou null s'il n'existe pas.
     */
    @Override
    public Restaurant findById(int id) {
        String query = "SELECT * FROM RESTAURANTS WHERE NUMERO = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Restaurant(
                        rs.getInt("numero"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getString("site_web"),
                        rs.getString("adresse"),
                        cityMapper.findById(rs.getInt("fk_vill")),
                        restaurantTypeMapper.findById(rs.getInt("fk_type"))
                );
            }
        } catch (SQLException e) {
            logger.error("Erreur lors de la recherche du restaurant avec l'ID {} : {}", id, e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Récupère l'ensemble des restaurants enregistrés dans la base de données.
     *
     * @return Un Set contenant tous les objets Restaurant.
     */
    @Override
    public Set<Restaurant> findAll() {
        String query = "SELECT * FROM RESTAURANTS";
        try (PreparedStatement stmt = this.connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            Set<Restaurant> restaurants = new HashSet<>();

            while (rs.next()) {
                restaurants.add(new Restaurant(
                        rs.getInt("numero"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getString("site_web"),
                        rs.getString("adresse"),
                        cityMapper.findById(rs.getInt("fk_vill")),
                        restaurantTypeMapper.findById(rs.getInt("fk_type"))
                ));
            }
            return restaurants;

        } catch (SQLException e) {
            logger.error("Erreur lors de la récupération de la liste des restaurants : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Crée un nouveau restaurant dans la base de données.
     * Le champ NUMERO est alimenté automatiquement par une séquence.
     *
     * @param object L'objet Restaurant à insérer.
     * @return Le Restaurant nouvellement créé (récupéré depuis la base).
     */
    @Override
    public Restaurant create(Restaurant object) {
        String insertQuery = "INSERT INTO RESTAURANTS (NOM, DESCRIPTION, SITE_WEB, ADRESSE, FK_VILL, FK_TYPE) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setString(1, object.getName());
            insertStatement.setString(2, object.getDescription());
            insertStatement.setString(3, object.getWebsite());
            insertStatement.setString(4, object.getAddress().getStreet());
            insertStatement.setInt(5, object.getAddress().getCity().getId());
            insertStatement.setInt(6, object.getType().getId());
            insertStatement.executeUpdate();

            connection.commit();

            // Recherche du restaurant inséré
            PreparedStatement selectStatement = connection.prepareStatement(
                    "SELECT * FROM RESTAURANTS WHERE ADRESSE = ? AND FK_TYPE = ?"
            );
            selectStatement.setString(1, object.getAddress().getStreet());
            selectStatement.setInt(2, object.getType().getId());
            ResultSet rs = selectStatement.executeQuery();

            if (rs.next()) {
                return findById(rs.getInt("numero"));
            }

            connection.close();
        } catch (SQLException e) {
            logger.error("Erreur lors de la création du restaurant '{}' : {}", object.getName(), e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Met à jour les informations d'un restaurant existant dans la base de données.
     *
     * @param object L'objet Restaurant contenant les nouvelles informations.
     * @return true si la mise à jour a été effectuée avec succès, false sinon.
     */
    @Override
    public boolean update(Restaurant object) {
        String query = "UPDATE RESTAURANTS SET nom = ?, description = ?, site_web = ?, adresse = ?, fk_vill = ?, fk_type = ? WHERE numero = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, object.getName());
            stmt.setString(2, object.getDescription());
            stmt.setString(3, object.getWebsite());
            stmt.setString(4, object.getAddress().getStreet());
            stmt.setInt(5, object.getAddress().getCity().getId());
            stmt.setInt(6, object.getType().getId());
            stmt.setInt(7, object.getId());

            stmt.executeUpdate();
            connection.commit();

            return stmt.getUpdateCount() == 1;

        } catch (SQLException e) {
            logger.error("Erreur lors de la mise à jour du restaurant avec l'ID {} : {}", object.getId(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Supprime un restaurant de la base de données à partir de son identifiant.
     *
     * @param object L'objet Restaurant à supprimer.
     * @return true si la suppression a réussi, false sinon.
     */
    @Override
    public boolean delete(Restaurant object) {
        String query = "DELETE FROM RESTAURANTS WHERE numero = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, object.getId());
            stmt.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException e) {
            logger.error("Erreur lors de la suppression du restaurant avec l'ID {} : {}", object.getId(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteById(int id) {
        return delete(findById(id));
    }

    @Override
    protected String getSequenceQuery() {
        return "";
    }

    @Override
    protected String getExistsQuery() {
        return "";
    }

    @Override
    protected String getCountQuery() {
        return "";
    }
}
