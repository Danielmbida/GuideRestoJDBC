package ch.hearc.ig.guideresto.presentation;

import ch.hearc.ig.guideresto.business.EvaluationCriteria;
import ch.hearc.ig.guideresto.persistence.ConnectionUtils;
import ch.hearc.ig.guideresto.persistence.EvaluationCriteriaMapper;

import java.sql.Connection;
import java.util.Set;

public class mainTest {

    public static void main(String[] args) {
        Connection conn = ConnectionUtils.getConnection();
        EvaluationCriteriaMapper mapper = new EvaluationCriteriaMapper(conn);

        EvaluationCriteria criteria = new EvaluationCriteria("test","Etat des lieu");
        mapper.create(criteria);

         // mapper.deleteById(25);
//        EvaluationCriteria Criteria = mapper.findById(1);
//        System.out.println(Criteria.getName());

        Set<EvaluationCriteria> evaluationCriteria =mapper.findAll();
        System.out.println(evaluationCriteria.size());


//        EvaluationCriteria criteria = new EvaluationCriteria(mapper.findById(1).getId(),"Service","comportement des serveurs");
//        mapper.update(criteria);
    }
}
