package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.City;
import ch.hearc.ig.guideresto.business.IBusinessObject;
import ch.hearc.ig.guideresto.business.RestaurantType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class CityMapper extends AbstractMapper<City>{
    private Connection connection;

    public CityMapper(Connection connection) {
        this.connection = connection;
    }

    @Override
    public City findById(int id) {
        if (cache.containsKey(id)) {
            return cache.get(id);
        }
        String query = "SELECT * FROM VILLES WHERE numero = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                City city = new City(
                        rs.getInt("numero"),
                        rs.getString("code_postal"),
                        rs.getString("nom_ville")
                );
                addToCache(city);
                return city;
            }
        } catch (Exception e) {
            logger.error("Erreur dans CityMapper.findById", e);
        }
        return null;
    }

    public City findByZipCode(String namePart) {
        String query = "SELECT * FROM VILLES WHERE UPPER(CODE_POSTAL)= ?";
        City city = new City();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, namePart.toUpperCase());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("numero");
                city = cache.get(id);

                if (city == null) {
                   city = new City(
                            rs.getInt("numero"),
                            rs.getString("code_postal"),
                            rs.getString("nom_ville")
                    );
                    addToCache(city);
                }
            }
        } catch (SQLException e) {
            logger.error("Erreur lors de la recherche de la ville avec code postal '{}': {}", namePart, e.getMessage());
            throw new RuntimeException(e);
        }
        return city;
    }

    @Override
    public Set<City> findAll() {
        String query = "SELECT * FROM VILLES";
        Set<City> cities = new HashSet<>();
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("numero");
                City city = cache.get(id);
                if (city == null) {
                    city = new City(
                            id,
                            rs.getString("code_postal"),
                            rs.getString("nom_ville")
                    );
                    addToCache(city);
                }
                cities.add(city);
            }
            return cities;
        } catch (Exception e) {
            logger.error("Erreur dans CityMapper.findAll", e);
            return new HashSet<>();
        }
    }

    @Override
    public City create(City object) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO VILLES(code_postal, nom_ville) VALUES(?,?)");
            preparedStatement.setString(1, object.getZipCode());
            preparedStatement.setString(2, object.getCityName());
            preparedStatement.executeUpdate();
            connection.commit();
            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT *  FROM VILLES WHERE code_postal = ?");
            preparedStatement2.setString(1, object.getZipCode());
            ResultSet resultSet = preparedStatement2.executeQuery();
            if(resultSet.next()){
                return new City(resultSet.getInt("numero"), resultSet.getString("code_postal"), resultSet.getString("nom_ville"));
            }
            resetCache();
            return null;
        }catch(Exception e){
            logger.error("Error en CityMapper.create", e);
            return null;
        }
    }

    @Override
    public boolean update(City object) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE VILLES SET nom_ville =?, code_postal = ? WHERE numero = ?");
            preparedStatement.setString(1, object.getCityName());
            preparedStatement.setString(2, object.getZipCode());
            preparedStatement.setInt(3, object.getId());
            preparedStatement.executeUpdate();
            removeFromCache(object.getId());
            return true;
        }catch(Exception e){
            logger.error("Error en CityMapper.update", e);
            return false;
        }
    }

    @Override
    public boolean delete(City object) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM VILLES WHERE numero = ?");
            preparedStatement.setInt(1, object.getId());
            preparedStatement.executeUpdate();
            removeFromCache(object.getId());
            return true;
        }catch(Exception e){
            logger.error("Error en CityMapper.delete", e);
            return false;
        }
    }

    @Override
    public boolean deleteById(int id) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM VILLES WHERE numero = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            removeFromCache(id);
            return true;
        }catch(Exception e){
            logger.error("Error en CityMapper.delete", e);
            return false;
        }
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
