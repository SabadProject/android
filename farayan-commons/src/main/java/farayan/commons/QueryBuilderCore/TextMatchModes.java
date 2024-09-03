package farayan.commons.QueryBuilderCore;

import java.security.PublicKey;

/**
 * Created by Homayoun on 29/05/2017.
 */

public enum TextMatchModes {
    Contains(0, "شامل"),
    ExactlySensitive(1, "دقیقا"),
    Exactly(2, "برابر"),
    Starts(3, "شروع بشود"),
    Ends(4, "پایان بشود"),
    Null(5, "خالی باشد"),
    NotNull(6, "خالی نباشد"),
    NotEqual(7, "نابرابر");

    public final int ServerCode;
    public final String Text;

    TextMatchModes(int serverCode, String text) {
        ServerCode = serverCode;
        Text = text;
    }
}
