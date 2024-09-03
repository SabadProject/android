package farayan.commons.UI.Commons;

import farayan.commons.UI.DatePickerConfig;

public enum NumberLanguages {
    None(0),
    English(1),
    Persian(2),
    Arabic(3),;
    public final int Code;

    NumberLanguages(int code) {
        Code = code;
    }

    public static NumberLanguages ByCode(int code) {
        for (NumberLanguages mode : values()) {
            if (mode.Code == code)
                return mode;
        }
        return null;
    }
}
