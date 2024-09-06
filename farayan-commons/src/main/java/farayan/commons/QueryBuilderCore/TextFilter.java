package farayan.commons.QueryBuilderCore;

import farayan.commons.FarayanUtility;

public class TextFilter implements IFilter {
    public TextFilter() {
    }

    public TextFilter(String text) {
        this.Text = text;
    }

    public TextFilter(String text, TextMatchModes mode) {
        this.Text = text;
        this.Mode = mode;
    }

    public String Text;

    public TextMatchModes Mode = TextMatchModes.Contains;

    public static boolean IsUsable(TextFilter filter) {
        return filter != null && filter.IsUsable();
    }

    public boolean IsUsable() {
        switch (Mode) {
            case Exactly:
            case ExactlySensitive:
            case Contains:
            case Starts:
            case Ends:
            case NotEqual:
                return FarayanUtility.IsUsable(Text);
            case Null:
            case NotNull:
                return true;
            default:
                throw new RuntimeException(String.format("mode {0} does not supported", Mode));
        }
    }

    private RelationalOperators Operator() {
        switch (Mode) {
            case ExactlySensitive:
            case Exactly:
                return RelationalOperators.Equal;

            case Contains:
            case Starts:
            case Ends:
                return RelationalOperators.Like;

            case NotNull:
                return RelationalOperators.IsNotNull;

            case Null:
                return RelationalOperators.IsNull;

            case NotEqual:
                return RelationalOperators.NotEqual;

            default:
                throw new RuntimeException(String.format("mode {0} does not supported", Mode));
        }
    }

    private String Value() {
        if (FarayanUtility.IsNullOrEmpty(Text))
            return null;
        switch (Mode) {
            case NotEqual:
            case Exactly:
            case ExactlySensitive:
                return Text;
            case Contains:
                return "%" + FarayanUtility.Trim(Text, "%", true, true).replace("ي", "ی").replace("ك", "ک") + "%";
            case Starts:
                return FarayanUtility.Trim(Text, "%", true, true).replace("ي", "ی").replace("ك", "ک") + "%";
            case Ends:
                return "%" + FarayanUtility.Trim(Text, "%", true, true).replace("ي", "ی").replace("ك", "ک");
            case Null:
            case NotNull:
                return null;
            default:
                throw new RuntimeException(String.format("mode {0} does not supported", Mode));
        }
    }

    public void Condition(String name, MixedCondition mixed) {
        if (!IsUsable())
            return;
        mixed.Conditions.add(new PropertyCondition(name, Operator(), Value()));
    }
}
