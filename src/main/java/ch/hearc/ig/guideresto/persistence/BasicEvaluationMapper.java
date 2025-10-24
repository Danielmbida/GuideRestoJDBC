package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.BasicEvaluation;
import ch.hearc.ig.guideresto.business.City;
import ch.hearc.ig.guideresto.business.IBusinessObject;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

public class BasicEvaluationMapper extends AbstractMapper<BasicEvaluation>{
    private Connection connection;
    private RestaurantMapper restaurantMapper;

    public BasicEvaluationMapper(Connection connection){
        this.connection = connection;
        this.restaurantMapper = new RestaurantMapper(connection);
    }
    @Override
    public BasicEvaluation findById(int id) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT *  FROM LIKES WHERE numero = ?");

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                //permet de récupérer a l'aide d'autre mapper les objets business nécéssaire et convertit le boolean qui est un char dans la base de données
                return new BasicEvaluation(resultSet.getDate("date_eval"), restaurantMapper.findById(resultSet.getInt("fk_rest")), (resultSet.getString("APPRECIATION")=="T" ? true : false), resultSet.getString("ADRESSE_IP"));
            }
            return null;

        }catch(Exception e){
            logger.error("Error en CityMapper.findById", e);
            return null;
        }
    }

    @Override
    public Set findAll() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT *  FROM LIKES");
            ResultSet resultSet = preparedStatement.executeQuery();
            Set<BasicEvaluation> set = new HashSet();
            while(resultSet.next()){
                set.add(new BasicEvaluation(resultSet.getDate("date_eval"), restaurantMapper.findById(resultSet.getInt("fk_rest")), (resultSet.getString("APPRECIATION")=="T" ? true : false), resultSet.getString("ADRESSE_IP")));
            }
            return set;
        }catch(Exception e){
            logger.error("Error en CityMapper.findAll", e);
            return null;
        }

    }

    @Override
    public BasicEvaluation create(BasicEvaluation object) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO LIKES(appreciation, date_eval, adresse_ip, fk_rest) VALUES (?, ?, ?, ?)");
            //opérateur ternaire pour gérer la conversion booléen -> string
            preparedStatement.setString(1, (object.getLikeRestaurant()) ? "T" : "F" );
            //caster la date en date sql
            preparedStatement.setDate(2, (Date) object.getVisitDate());
            preparedStatement.setString(3, object.getIpAddress());
            preparedStatement.setInt(4, object.getRestaurant().getId());
            preparedStatement.executeUpdate();
            connection.commit();
            //pour rechercher ce que l'on vient de créer on part du principe que une personne ne peut mettre que un seul commentaire par jour et par restaurant
            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT *  FROM LIKES WHERE date_eval = ? AND adresse_ip = ? AND fk_rest = ?");
            preparedStatement1.setDate(1, (Date) object.getVisitDate());
            preparedStatement1.setString(2, object.getIpAddress());
            preparedStatement1.setInt(3, object.getRestaurant().getId());
            ResultSet resultSet = preparedStatement1.executeQuery();
            if(resultSet.next()){
                return new BasicEvaluation(resultSet.getDate("date_eval"), restaurantMapper.findById(resultSet.getInt("fk_rest")), (resultSet.getString("APPRECIATION")=="T" ? true : false), resultSet.getString("ADRESSE_IP"));
            }
            return null;
        }catch(Exception e){
            logger.error("Error en CityMapper.create", e);
            return null;
        }

    }

    @Override
    public boolean update(BasicEvaluation object) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE LIKES SET appreciation = ?, date_eval = ?,  adresse_ip = ?, fk_rest = ? WHERE numero = ?");
            preparedStatement.setString(1, (object.getLikeRestaurant()) ? "T" : "F" );
            preparedStatement.setDate(2, (Date) object.getVisitDate());
            preparedStatement.setString(3, object.getIpAddress());
            preparedStatement.setInt(4, object.getRestaurant().getId());
            preparedStatement.setInt(5, object.getId());
            preparedStatement.executeUpdate();
            return  true;
        }catch(Exception e){
            logger.error("Error en CityMapper.update", e);
            return false;
        }
    }

    @Override
    public boolean delete(BasicEvaluation object) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM LIKES WHERE numero = ?");
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
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM LIKES WHERE numero = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            return true;
        }catch(Exception e){
            logger.error("Error en CityMapper.deleteById", e);
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
