package ch.hearc.ig.guideresto.presentation;

import ch.hearc.ig.guideresto.business.*;
import ch.hearc.ig.guideresto.persistence.*;

import java.sql.Connection;
import java.util.List;
import java.util.Set;

public class mainTest {
    public static void main(String[] args) {
        Connection conn = ConnectionUtils.getConnection();

        CityMapper cityMapper = new CityMapper(conn);
        RestaurantTypeMapper typeMapper = new RestaurantTypeMapper(conn);
        RestaurantMapper restaurantMapper = new RestaurantMapper(conn, typeMapper, cityMapper);
        EvaluationCriteriaMapper criteriaMapper = new EvaluationCriteriaMapper(conn);
        CompleteEvaluationMapper completeEvaluationMapper = new CompleteEvaluationMapper(conn, restaurantMapper);
        GradeMapper gradeMapper = new GradeMapper(conn, completeEvaluationMapper, criteriaMapper);

        // Test - CREATE CompleteEvaluation
        Restaurant rest = restaurantMapper.findById(1); // Utilise un ID existant !
        CompleteEvaluation completeEval = new CompleteEvaluation(2, new java.util.Date(), rest, "Cool!", "Testeur");
        completeEval = completeEvaluationMapper.create(completeEval);
        System.out.println("Evaluation créée : " + completeEval.getId());
//
////        // Test - FIND CompleteEvaluation
//        CompleteEvaluation fetchedEval = completeEvaluationMapper.findById(completeEval.getId());
//        System.out.println("Evaluation retrouvée : " + fetchedEval.getComment());
//
////        // Test - UPDATE CompleteEvaluation
//        fetchedEval.setComment("Mise à jour du commentaire");
//        completeEvaluationMapper.update(fetchedEval);
//        System.out.println("Commentaire MAJ : " + completeEvaluationMapper.findById(fetchedEval.getId()).getComment());
////
////        // Test - DELETE CompleteEvaluation
//        completeEvaluationMapper.deleteById(fetchedEval.getId());
//        System.out.println("Evaluation supprimée ? " + (completeEvaluationMapper.findById(fetchedEval.getId()) == null));
//
//
//        // Test - CREATE Grade
        EvaluationCriteria crit = criteriaMapper.findById(1); // Utilise un ID existant !
        Grade grade = new Grade(1, 5, completeEval, crit);
        grade = gradeMapper.create(grade);
        System.out.println("Note créée : " + grade.getId());
//
//        // Test - FIND Grade
        Grade fetchedGrade = gradeMapper.findById(grade.getId());
        System.out.println("Note retrouvée : " + fetchedGrade.getGrade());
//
//        // Test - UPDATE Grade
        fetchedGrade.setGrade(3);
        gradeMapper.update(fetchedGrade);
        System.out.println("Note MAJ : " + gradeMapper.findById(fetchedGrade.getId()).getGrade());

//        // Test - DELETE Grade
        gradeMapper.deleteById(fetchedGrade.getId());
        System.out.println("Note supprimée ? " + (gradeMapper.findById(fetchedGrade.getId()) == null));

//        // Test - Affichage de tous les restaurants
        Set<Restaurant> restaurants = restaurantMapper.findAll();
        restaurants.forEach(restaurant -> {
            System.out.println(restaurant.getName());
        });

//        // Test - Affichage de toutes les évaluations
//        Set<CompleteEvaluation> evaluations = completeEvaluationMapper.findAll();
//        evaluations.forEach(eval -> {
//            System.out.println(eval.getComment());
//        });
//
//        // Test - Affichage de toutes les notes
//        Set<Grade> grades = gradeMapper.findAll();
//        grades.forEach(g -> {
//            System.out.println(g.getGrade() + " pour critère : " + g.getCriteria().getName());
//        });
    }

//    public static void main(String[] args) {
//        Connection conn = ConnectionUtils.getConnection();
//
//        CityMapper cityMapper = new CityMapper(conn);
//        RestaurantTypeMapper typeMapper = new RestaurantTypeMapper(conn);
//        RestaurantMapper restaurantMapper = new RestaurantMapper(conn, typeMapper, cityMapper);
//
//
//
//       // restaurantMapper.create(new Restaurant(3,"Maison des brojette","Fait viande et poulet","test","rue moutier",cityMapper.create(new City("2743","Moutier")), typeMapper.findById(1)));
//       // restaurantMapper.update(new Restaurant(restaurantMapper.findById(21).getId(),"Maison des brochette","Fait viande et poulet","test","rue moutier",cityMapper.create(new City("2743","Moutier")), typeMapper.findById(1)));
//
//        restaurantMapper.deleteById(restaurantMapper.findById(21).getId());
//        Set<Restaurant> restaurants = restaurantMapper.findAll();
//        restaurants.forEach(restaurant -> {
//            System.out.println(restaurant.getName());
//
//        });
//    }
}
