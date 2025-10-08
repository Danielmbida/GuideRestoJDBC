package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.City;
import ch.hearc.ig.guideresto.business.IBusinessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

public class CityMapper extends AbstractMapper<City>{
    private Connection connection;

    public CityMapper(Connection connection) {
        this.connection = connection;
    }

    @Override
    public City findById(int id){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT *  FROM VILLES WHERE numero = ?");

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return new City(resultSet.getInt("numero"), resultSet.getString("code_postal"), resultSet.getString("nom_ville"));
            }
            return null;

        }catch(Exception e){
            logger.error("Error en CityMapper.findById", e);
            return null;
        }


    }

    @Override
    public Set<City> findAll() {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT *  FROM VILLES");
            ResultSet resultSet = preparedStatement.executeQuery();
            Set<City> set = new HashSet<>();
            while(resultSet.next()){
                set.add(new City(resultSet.getInt("numero"), resultSet.getString("code_postal"), resultSet.getString("nom_ville")));
            }
            return set;
        }catch(Exception e){
            logger.error("Error en CityMapper.findAll", e);
            return null;
        }

    }

    @Override
    public City create(City object) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO VILLES('code_postal', 'nom_ville') VALUES(?,?)");
            preparedStatement.setString(1, object.getZipCode());
            preparedStatement.setString(2, object.getCityName());
            preparedStatement.executeUpdate();

            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT *  FROM VILLES WHERE code_postal = ?");
            preparedStatement2.setString(1, object.getZipCode());
            ResultSet resultSet = preparedStatement2.executeQuery();
            while(resultSet.next()){
                return new City(resultSet.getInt("numero"), resultSet.getString("code_postal"), resultSet.getString("nom_ville"));
            }
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
