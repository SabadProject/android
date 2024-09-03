package farayan.commons.QueryBuilderCore;

import java.sql.SQLException;

import com.j256.ormlite.stmt.Where;

public interface ICondition {
	<T> Where<T, Integer> Where(Where<T, Integer> where) throws SQLException;

	String Query(String prefix);
}
