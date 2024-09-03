package farayan.commons.Portal;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;

import farayan.commons.FarayanUtility;
import farayan.commons.PersianCalendar;
import farayan.commons.WebResponse;

/**
 * Created by Homayoun on 01/05/2017.
 */

public class PortalUtility {
    public static final long AuthCookieExpiration = 60 * 60 * 1000;

    public static LoginResult GetAuthCookie(SharedPreferences settings, String serverUrl) {
        String authCookie = settings.getString(PreferenceKeys.PortalAuthCookie.name(), "");
        long authCookieExpirationMillis = settings.getLong(PreferenceKeys.PortalAuthCookieExpiration.name(), 0);
        if (FarayanUtility.IsUsable(authCookie)) {
            if (System.currentTimeMillis() < authCookieExpirationMillis) {
                return new LoginResult(LoginResults.LoginNotExpired, authCookie);
            }
            WebResponse result = FarayanUtility.GetWebResponse(serverUrl + "/login-status.ashx", authCookie);
            if (result == null) {
                FarayanUtility.Log(false, true, true, "GetAuthCookie, result is null");
                return new LoginResult(LoginResults.ServerProblem, authCookie);
            }
            Boolean validCookie = FarayanUtility.TryParseBoolean(result.getResponse());

            // when answer is not boolean, in situations like website down or problem
            if (validCookie == null) {
                FarayanUtility.Log(false, true, true, "GetAuthCookie, validCookie (%s) is not boolean");
                return new LoginResult(LoginResults.ServerProblem, authCookie);
            }
            if (validCookie) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(PreferenceKeys.PortalAuthCookie.name(), authCookie);
                editor.putLong(PreferenceKeys.PortalAuthCookieExpiration.name(), System.currentTimeMillis() + (60 * 60 * 1000));
                editor.apply();
                return new LoginResult(LoginResults.Success, authCookie);
            }
            // reaching here means authCookie is not valid!
        }

        SharedPreferences.Editor editor = settings.edit();
        String username = settings.getString(PreferenceKeys.PortalUserName.name(), "");
        String password = settings.getString(PreferenceKeys.PortalPassword.name(), "");
        WebResponse response = null;
        if (FarayanUtility.IsUsable(username) && FarayanUtility.IsUsable(password)) {
            response = FarayanUtility.GetAuthCookie(serverUrl, username, password, true);
        }

        // when either user or pass is empty or GetAuthCookie returns null
        if (response == null) {
            return new LoginResult(LoginResults.LoginProblem, null);
        }
        // when website is unreachable, network is not connected for example
        if (response.isConnectionProblem()) {
            return new LoginResult(LoginResults.ServerProblem, null);
        }
        authCookie = response.getResponse();
        if (FarayanUtility.IsUsable(authCookie)) {
            editor.putString(PreferenceKeys.PortalAuthCookie.name(), authCookie);
            editor.putLong(PreferenceKeys.PortalAuthCookieExpiration.name(), System.currentTimeMillis() + (60 * 60 * 1000));
            editor.commit();
            return new LoginResult(LoginResults.Success, authCookie);
        } else {
            editor.remove(PreferenceKeys.PortalAuthCookie.name());
            editor.commit();
            return new LoginResult(LoginResults.LoginProblem, null);
        }
    }

    /**
     * اگر کاربر، لاگین باشد، نام‌کاربری، رمز و کوکی لاگین وجود داشته باشند بدون در نظرگرفتن معتبر بودن‌شان، مقدار مثبت و در غیر اینصورت مقدار منفی برمی‌گرداند
     * Version 1.1.0 @ 96/02/12
     *
     * @param settings
     * @return
     */
    public static boolean IsLogged(SharedPreferences settings) {
        if (settings == null)
            settings = PreferenceManager.getDefaultSharedPreferences(FarayanUtility.GlobalContext);
        String userName = settings.getString(PreferenceKeys.PortalUserName.name(), "");
        String password = settings.getString(PreferenceKeys.PortalPassword.name(), "");
        String authCookie = settings.getString(PreferenceKeys.PortalAuthCookie.name(), "");

        if (FarayanUtility.IsNullOrEmpty(userName))
            return false;
        if (FarayanUtility.IsNullOrEmpty(password))
            return false;
        if (FarayanUtility.IsNullOrEmpty(authCookie))
            return false;

        return true;
    }
}
