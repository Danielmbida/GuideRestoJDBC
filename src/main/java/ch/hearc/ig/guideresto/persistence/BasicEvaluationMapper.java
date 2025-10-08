package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.BasicEvaluation;
import ch.hearc.ig.guideresto.business.City;
import ch.hearc.ig.guideresto.business.IBusinessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Set;

public class BasicEvaluationMapper extends AbstractMapper{
    private Connection connection;

    public BasicEvaluationMapper(Connection connection){
        this.connection = connection;
    }
    @Override
    public IBusinessObject findById(int id) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT *  FROM LIKES WHERE numero = ?");

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT *  FROM RESTAURANTS WHERE numero = ?");
                return new BasicEvaluation(resultSet.getDate("date_eval"));
            }
            return null;

        }catch(Exception e){
            logger.error("Error en CityMapper.findById", e);
            return null;
        }
    }

    @Override
    public Set findAll() {
        return Set.of();
    }

    @Override
    public IBusinessObject create(IBusinessObject object) {
        return null;
    }

    @Override
    public boolean update(IBusinessObject object) {
        return false;
    }

    @Override
    public boolean delete(IBusinessObject object) {
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
