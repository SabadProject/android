package farayan.commons.QueryBuilderCore;

import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;

public interface ICondition {
	<T> Where<T, Integer> Where(Where<T, Integer> where) throws SQLException;

	String Query(String prefix);
}
