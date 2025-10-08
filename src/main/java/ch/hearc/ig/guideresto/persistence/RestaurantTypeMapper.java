package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.EvaluationCriteria;
import ch.hearc.ig.guideresto.business.RestaurantType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class RestaurantTypeMapper extends AbstractMapper<RestaurantType> {

    private final Connection connection;

    public RestaurantTypeMapper(Connection connection) {
        this.connection = connection;
    }

    @Override
    public RestaurantType findById(int id) {
        String query = "select * from TYPES_GASTRONOMIQUES where NUMERO = ?";
        try{
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new RestaurantType(
                        rs.getInt("NUMERO"),
                        rs.getString("libelle"),
                        rs.getString("DESCRIPTION"));
            }
        } catch (SQLException e) {
            logger.error("SQLException: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Set<RestaurantType> findAll() {
        String query = "SELECT * FROM TYPES_GASTRONOMIQUES";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            Set<RestaurantType> restaurantsTypes = new HashSet<>();
            while (rs.next()) {
                restaurantsTypes.add(new RestaurantType(
                        rs.getInt("numero"),
                        rs.getString("labelle"),
                        rs.getString("description")
                ));
            }
            return restaurantsTypes;
        } catch (SQLException ex) {
            logger.error("SQLException: {}", ex.getMessage());
        }
        return null;
    }

    @Override
    public RestaurantType create(RestaurantType object) {
        String query = "INSERT INTO TYPES_GASTRONOMIQUES (LIBELLE, description) " +
                "VALUES (?, ?)";

        String getQuery = "SELECT * FROM TYPES_GASTRONOMIQUES WHERE LIBELLE = ?";

        try (
                PreparedStatement insertStmt = connection.prepareStatement(query);
                PreparedStatement selectStmt = connection.prepareStatement(getQuery)
        ){
            insertStmt.setString(1, object.getLabel());
            insertStmt.setString(2, object.getDescription());
            insertStmt.executeUpdate();

            connection.commit();
            selectStmt.setString(1, object.getLabel());
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                return new RestaurantType(
                        rs.getInt("numero"),
                        rs.getString("libelle"),
                        rs.getString("description"));
            }
            return null;
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1) {
                logger.error("Le nom '{}' existe déjà (contrainte unique)", object.getLabel());
            } else {
                logger.error("SQLException: {}", ex.getMessage());
            }
            return null;
        }
    }

    @Override
    public boolean update(RestaurantType object) {
        String query = "UPDATE TYPES_GASTRONOMIQUES SET LIBELLE = ?, description = ? WHERE numero = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, object.getLabel());
            stmt.setString(2, object.getDescription());
            stmt.setInt(3, object.getId());
            stmt.executeUpdate();
            connection.commit();
            return stmt.getUpdateCount() == 1;
        } catch (SQLException ex) {
            logger.error("SQLException: {}", ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(RestaurantType object) {
        String query = "DELETE FROM TYPES_GASTRONOMIQUES WHERE numero = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, object.getId());
            stmt.executeUpdate();
            connection.commit();
            return true;
        } catch (SQLException ex) {
            logger.error("SQLException: {}", ex.getMessage());
        }
        return false;
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
