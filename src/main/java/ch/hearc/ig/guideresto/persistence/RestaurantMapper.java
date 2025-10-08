package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.IBusinessObject;
import ch.hearc.ig.guideresto.business.Localisation;
import ch.hearc.ig.guideresto.business.Restaurant;
import ch.hearc.ig.guideresto.business.RestaurantType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class RestaurantMapper extends AbstractMapper<Restaurant> {
    private final Connection connection;

    private final RestaurantTypeMapper restaurantTypeMapper;
    private final CityMapper cityMapper;

    public RestaurantMapper(Connection connection, RestaurantTypeMapper restaurantTypeMapper, CityMapper cityMapper) {
        this.connection = connection;
        this.restaurantTypeMapper = restaurantTypeMapper;
        this.cityMapper = cityMapper;
    }

    @Override
    public Restaurant findById(int id) {

        String query = "select * from RESTAURANTS where NUMERO = ?";
        try{
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
            logger.error("SQLException: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Set<Restaurant> findAll() {
        return Set.of();
    }

    @Override
    public Restaurant create(Restaurant object) {
        return null;
    }

    @Override
    public boolean update(Restaurant object) {
        return false;
    }

    @Override
    public boolean delete(Restaurant object) {
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
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
