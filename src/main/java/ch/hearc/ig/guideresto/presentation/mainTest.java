package ch.hearc.ig.guideresto.presentation;

import ch.hearc.ig.guideresto.business.*;
import ch.hearc.ig.guideresto.persistence.*;

import java.sql.Connection;
import java.util.List;
import java.util.Set;

public class mainTest {
    public static void main(String[] args) {
        Connection conn = ConnectionUtils.getConnection();
        RestaurantMapper restaurantMapper = new RestaurantMapper(conn);

       // Test - Affichage de tous les restaurants
        Set<Restaurant> restaurants = restaurantMapper.findAll();
        restaurants.forEach(restaurant -> {
            System.out.println(restaurant.getName());
        });

    }
}
