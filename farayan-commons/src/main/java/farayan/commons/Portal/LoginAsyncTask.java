package farayan.commons.Portal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import farayan.commons.FarayanUtility;
import farayan.commons.R;
import farayan.commons.UI.Core.ICommandEvent;
import farayan.commons.WebResponse;

/**
 * Created by Homayoun on 01/05/2017.
 */


public class LoginAsyncTask extends AsyncTask<Void, Void, LoginResult> {

    final String UserName;
    final String Password;
    final Activity activity;
    final String ServerUrl;
    final ICommandEvent<LoginResult> OnLogged;

    public LoginAsyncTask(String username, String password, Activity activity, String serverUrl, ICommandEvent<LoginResult> onLogged) {
        this.UserName = username;
        this.Password = password;
        this.activity = activity;
        ServerUrl = serverUrl;
        OnLogged = onLogged;
    }

    ProgressDialog dialog;

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(activity, "", activity.getString(R.string.PleaseWait));
        super.onPreExecute();
    }

    @Override
    protected LoginResult doInBackground(Void... params) {
        WebResponse authCookie = FarayanUtility.GetAuthCookie(ServerUrl, UserName, Password, true);
        if (authCookie == null)
            return new LoginResult(LoginResults.ServerUnreachable);
        String response = authCookie.getResponse();
        if (FarayanUtility.IsUsable(response)) {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(activity);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(PreferenceKeys.PortalUserName.name(), UserName);
            editor.putString(PreferenceKeys.PortalPassword.name(), Password);
            editor.putString(PreferenceKeys.PortalAuthCookie.name(), response);
            editor.putLong(PreferenceKeys.PortalAuthCookieExpiration.name(), System.currentTimeMillis() + PortalUtility.AuthCookieExpiration);
            editor.commit();
            return new LoginResult(LoginResults.Success, response);
        }
        return new LoginResult(LoginResults.LoginProblem);
    }

    @Override
    protected void onPostExecute(LoginResult loginResult) {
        dialog.dismiss();
        OnLogged.OnEvent("", loginResult);
        super.onPostExecute(loginResult);
    }
}
