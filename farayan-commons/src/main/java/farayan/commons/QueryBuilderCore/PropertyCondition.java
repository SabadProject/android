package farayan.commons.QueryBuilderCore;

import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;

/**
 * شرط برروی یک فیلد را تعریف می‌کند
 */
public class PropertyCondition implements ICondition {

    /**
     * اسم فیلد یا پراپرتی
     */
    private String PropertyName;

    /**
     * نوع شرط که می‌تواند برابر، نابرابر و .... باشد
     */
    private RelationalOperators Operator;

    /**
     * مقداری که باید یا آن مقایسه شود
     */
    private Object Value;

    public String getPropertyName() {
        return PropertyName;
    }

    public void setPropertyName(String propertyName) {
        PropertyName = propertyName;
    }

    public RelationalOperators getOperator() {
        return Operator;
    }

    public void setOperator(RelationalOperators operator) {
        Operator = operator;
    }

    public Object getValue() {
        return Value;
    }

    public void setValue(Object value) {
        Value = value;
    }

    public PropertyCondition() {
    }

    public PropertyCondition(String property, RelationalOperators operator, Object value) {
        this.setPropertyName(property);
        this.setOperator(operator);
        this.setValue(value);
    }

    /**
     * عبارت شرط را می‌سازد
     * در صورتی که مقدار عددی باشد، به صورت عددی در عبارت شرط قرار می‌دهد
     * در غیر اینصورت مقدار شرط را به صورت متن و در بین کوتیشن در متن شرط قرار می‌دهد
     * در صورتی نوع شرط «بین» یا «در» باشد، مقدار شرط در بین پرانتز قرار می‌گیرد
     *
     * @param prefix پیشوند نام فیلد یا پراپرتی هست
     * @return
     */
    public String Query(String prefix) {
        String valueText = "";
        if (Value != null) {
            String valueTypeName = Value.getClass().getName();
            if (valueTypeName.equalsIgnoreCase("java.lang.Integer"))
                valueText = Value + "";
            else if (valueTypeName.equalsIgnoreCase("java.lang.Long"))
                valueText = Value + "";
            else if (valueTypeName.equalsIgnoreCase("java.lang.Double"))
                valueText = Value + "";
            else if (valueTypeName.equalsIgnoreCase("java.lang.Float"))
                valueText = Value + "";
            else
                valueText = "'" + Value + "'";
        }
        if (Operator == RelationalOperators.In) {
            if (Value instanceof QueryBuilder<?, ?>) {
                try {
                    valueText = "(" + ((QueryBuilder) Value).prepareStatementString() + ")";
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (Value instanceof Iterable<?>) {
                valueText = "(";
                for (Object o : (Iterable<?>) Value)
                    valueText += o;
                valueText += ")";
            } else {
                valueText = "(" + Value + ")";
            }
        }
        return prefix + PropertyName + " " + Operator.Query + valueText;
    }

    /**
     * براساس نوع شرط، تابع متناظر ابزار OrmLite.QueryBuilder را فراخوانی می‌کند
     *
     * @param where
     * @param <T>
     * @return
     * @throws SQLException
     */
    @Override
    public <T> com.j256.ormlite.stmt.Where<T, Integer> Where(com.j256.ormlite.stmt.Where<T, Integer> where) throws SQLException {
        switch (getOperator()) {
            case Equal:
                where = where.eq(getPropertyName(), getValue());
                break;
            case GreaterThan:
                where = where.gt(getPropertyName(), getValue());
                break;
            case GreaterThanOrEqual:
                where = where.ge(getPropertyName(), getValue());
                break;
            case LessThan:
                where = where.lt(getPropertyName(), getValue());
                break;
            case LessThanOrEqual:
                where = where.le(getPropertyName(), getValue());
                break;
            case NotEqual:
                where = where.ne(getPropertyName(), getValue());
                break;
            case Like:
                where = where.like(getPropertyName(), getValue());
                break;
            case IsNull:
                where = where.isNull(getPropertyName());
                break;
            case IsNotNull:
                where = where.isNotNull(getPropertyName());
                break;
            case In:
                if (getValue() instanceof QueryBuilder<?, ?>) {
                    where = where.in(getPropertyName(), (QueryBuilder<?, ?>) getValue());
                    break;
                }
                if (getValue() instanceof Iterable<?>) {
                    where = where.in(getPropertyName(), (Iterable<?>) getValue());
                    break;
                }
                where = where.in(getPropertyName(), getValue());
                break;
            default:
                break;
        }
        return where;
    }
}
