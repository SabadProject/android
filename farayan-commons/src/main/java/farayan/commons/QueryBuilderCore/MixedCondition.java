package farayan.commons.QueryBuilderCore;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.j256.ormlite.stmt.Where;

import farayan.commons.FarayanUtility;

public class MixedCondition implements ICondition {

    private LogicalOperators Operator;
    public final List<ICondition> Conditions = new ArrayList<ICondition>();

    public MixedCondition(LogicalOperators op, ICondition... conditions) {
        Operator = op;
        for (ICondition pc : conditions)
            Conditions.add(pc);
    }

    @Override
    public <T> Where<T, Integer> Where(Where<T, Integer> where) {
        try {
            where = InternalWhere(where);
            //Log.i("MixedCondition", where.getStatement());
            return where;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private <T> Where<T, Integer> InternalWhere(Where<T, Integer> where) throws SQLException {
        if (Conditions.size() == 0)
            return where;
        if (Conditions.size() == 1)
            return Conditions.get(0).Where(where);
        if (Conditions.size() == 2) {
            switch (Operator) {
                case And:
                    return where.and(Conditions.get(0).Where(where), Conditions.get(1).Where(where));
                case Or:
                    return where.or(Conditions.get(0).Where(where), Conditions.get(1).Where(where));
                default:
                    throw new RuntimeException(String.format("Operator %s is not supported", Operator));
            }
        }

        Where<T, Integer>[] thirdPlusConditions = new Where[Conditions.size() - 2];
        for (int i = 2; i < Conditions.size(); i++)
            thirdPlusConditions[i - 2] = Conditions.get(i).Where(where);
        switch (Operator) {
            case And:
                return where.and(Conditions.get(0).Where(where), Conditions.get(1).Where(where), thirdPlusConditions);
            case Or:
                return where.or(Conditions.get(0).Where(where), Conditions.get(1).Where(where), thirdPlusConditions);
            default:
                throw new RuntimeException(String.format("Operator %s is not supported", Operator));
        }
    }

    public LogicalOperators getOperator() {
        return Operator;
    }

    public void setOperator(LogicalOperators operator) {
        this.Operator = operator;
    }

    @Override
    public String Query(String prefix) {
        if (FarayanUtility.IsNullOrEmpty(Conditions))
            return "";
        List<String> queries = new ArrayList<String>();
        for (int i = 0; i < Conditions.size(); i++)
            queries.add(Conditions.get(i).Query(prefix));
        String query = "(" + FarayanUtility.Join(Operator.Operator, queries) + ")";

        return query;
    }

    @Override
    public String toString() {
        return Query("");
    }
}
