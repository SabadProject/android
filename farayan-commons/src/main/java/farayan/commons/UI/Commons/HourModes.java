package farayan.commons.UI.Commons;

public enum HourModes {
    TwentyFour24Hours(24),
    Twelve12Hours(12),;

    public final int Code;

    HourModes(int code) {
        Code = code;
    }

    public static HourModes ByCode(int code) {
        for (HourModes mode : values()) {
            if (mode.Code == code)
                return mode;
        }
        return null;
    }
}
