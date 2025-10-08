package ch.hearc.ig.guideresto.presentation;

import ch.hearc.ig.guideresto.business.EvaluationCriteria;
import ch.hearc.ig.guideresto.business.Restaurant;
import ch.hearc.ig.guideresto.persistence.*;

import java.sql.Connection;

public class mainTest {

    public static void main(String[] args) {
        Connection conn = ConnectionUtils.getConnection();
        CityMapper cityMapper = new CityMapper(conn);
        RestaurantTypeMapper typeMapper = new RestaurantTypeMapper(conn);
        RestaurantMapper restaurantMapper = new RestaurantMapper(conn, typeMapper, cityMapper);
        restaurantMapper.findById(1);
        System.out.println(restaurantMapper.findById(1).getName());

    }
}
