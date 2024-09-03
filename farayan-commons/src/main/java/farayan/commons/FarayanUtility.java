package farayan.commons;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.URLSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import farayan.commons.Commons.EmptyBitmap;
import farayan.commons.Commons.RecyclerViewOrientations;
import farayan.commons.Commons.TimeTextModes;
import farayan.commons.UI.Utils.URLSpanNoUnderline;
//import info.androidhive.fontawesome.FontTextView;
import kotlin.jvm.functions.Function0;

public class FarayanUtility {

    private static final char LTR = '\u200E';
    private static final char RTL = '\u200F';
    public static Context GlobalContext;
    public static final TimeZone IranTimeZone = TimeZone.getTimeZone("Asia/Tehran");
    private static Pattern m_emailPattern;

    public static Boolean TryParseBoolean(String result) {
        if (FarayanUtility.IsNullOrEmpty(result))
            return null;

        String[] trues = new String[]{"yes", "y", "true", "1", "t", "ok"};
        String[] falses = new String[]{"no", "n", "false", "0", "f"};

        for (String trueValue : trues) {
            if (result.equalsIgnoreCase(trueValue))
                return true;
        }
        for (String falseValue : falses) {
            if (result.equalsIgnoreCase(falseValue))
                return false;
        }
        return null;
    }

    public static void OverrideFonts(final View view, Typeface font) {
        if (view == null)
            return;
        if (font == null)
            return;

        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            for (int index = 0; index < vg.getChildCount(); index++) {
                View child = vg.getChildAt(index);
                OverrideFonts(child, font);
            }
        } else if (view instanceof TextView) {
            TextView textView = (TextView) view;
            //if (textView instanceof FontTextView)
            //	return;
            textView.setTypeface(font);
        }
    }

    public static String DumpBundle(Bundle bundle) {
        if (bundle == null)
            return "NULL";
        String result = "";
        for (String key : bundle.keySet()) {
            Object value = bundle.get(key);
            result += String.format("%s %s (%s)\r\n", key, value.toString(), value.getClass().getName());
        }

        return result;
    }

    public static Toast ShowToast(String text) {
        Toast toast = Toast.makeText(GlobalContext, text, Toast.LENGTH_LONG);
        toast.show();
        return toast;
    }

    public static void ShowToast(int text) {
        Toast.makeText(GlobalContext, GlobalContext.getString(text), Toast.LENGTH_LONG).show();
    }

    public static void ShowToastFormatted(boolean fast, String text, Object... values) {
        Toast.makeText(GlobalContext, String.format(text, values), fast ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
    }

    public static void ShowToastFormatted(boolean fast, int text, Object... values) {
        Toast.makeText(GlobalContext, String.format(GlobalContext.getString(text), values), fast ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
    }

    public static void ShowToastFormatted(String text, Object... values) {
        Toast.makeText(GlobalContext, String.format(text, values), Toast.LENGTH_LONG).show();
    }

    public static void ShowToastFormatted(int text, Object... values) {
        Toast.makeText(GlobalContext, String.format(GlobalContext.getString(text), values), Toast.LENGTH_LONG).show();
    }


    public static void ShowToast(Context ctx, String text) {
        Toast.makeText(ctx, text, Toast.LENGTH_LONG).show();
    }

    public static void ShowToast(Context ctx, int text) {
        Toast.makeText(ctx, GlobalContext.getString(text), Toast.LENGTH_LONG).show();
    }

    public static void ShowToastFormatted(Context ctx, boolean fast, String text, Object... values) {
        Toast.makeText(ctx, String.format(text, values), fast ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
    }

    public static void ShowToastFormatted(Context ctx, boolean fast, int text, Object... values) {
        Toast.makeText(ctx, String.format(ctx.getString(text), values), fast ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
    }

    public static void ShowToastFormatted(Context ctx, String text, Object... values) {
        Toast.makeText(ctx, String.format(text, values), Toast.LENGTH_LONG).show();
    }

    public static void ShowToastFormatted(Context ctx, int text, Object... values) {
        Toast.makeText(ctx, String.format(ctx.getString(text), values), Toast.LENGTH_LONG).show();
    }

    public static void PrintStackTrace(String tag) {
        try {
            throw new Exception();
        } catch (Exception e) {
            Log.i(tag, FarayanUtility.PrintException(e));
        }
    }

    public static String TextOrEmpty(String text) {
        return FarayanUtility.IsUsable(text) ? text : "";
    }

    public static void DisplayDialog(Activity activity, String title, String message, String closeText, DialogInterface.OnClickListener onClick) {
        AlertDialog.Builder builder = new Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNeutralButton(closeText, onClick);
        builder.create().show();
    }

    @Deprecated
    public static Intent TakeOrChoosePhotoIntent(PackageManager pkgManager, Uri imageFileUri, String title) {
        // Camera
        ArrayList<Intent> cameraIntents = new ArrayList<Intent>();
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        ArrayList<ResolveInfo> listCamera = (ArrayList<ResolveInfo>) pkgManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo resolveInfo : listCamera) {
            String pkgName = resolveInfo.activityInfo.packageName;
            String className = resolveInfo.activityInfo.name;
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(pkgName, className));
            intent.setPackage(pkgName);
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
            cameraIntents.add(intent);
        }

        // Gallery
        Intent galleryIntent;
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.KITKAT) {
            // KitKat Fix
            galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        } else {
            galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        }

        // Chooser
        Intent chooserIntent = Intent.createChooser(galleryIntent, title);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));

        return chooserIntent;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void ForceRTLOnSupport(Activity activity) {
        activity.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
    }

    public static <T extends Object> T GetGson(boolean log, String url, String authCookie, Class<T> type) {
        try {
            WebResponse response = GetWebResponse(url, authCookie);
            if (response == null) {
                FarayanUtility.Log(false, true, true, "Response for url (%s) is null", url);
                return null;
            }

            if (log && response != null)
                FarayanUtility.Log(false, true, true, "Gson Result: \n %s \n Name: %s", response.getResponse(), type.getName());
            return new Gson().fromJson(response.getResponse(), type);
        } catch (Exception e) {
            FarayanUtility.LogException(e);
            return null;
        }
    }


    public static void LogException(Throwable e) {
        e.printStackTrace();
        try {
            File folder = GlobalContext.getExternalFilesDir("exceptions");
            PersianCalendar now = new PersianCalendar();
            String yearText = now.getPersianYear() + "";
            String monthText = FarayanUtility.LeadingZero(now.getPersianMonthIndexFrom1(), 2);
            String dayText = FarayanUtility.LeadingZero(now.getPersianDay(), 2);
            String timeText = FarayanUtility.PersianTimeCompact(now, "-");
            String logPath = folder.getAbsolutePath()
                    + "/"
                    + yearText
                    + "/"
                    + monthText
                    + "/"
                    + dayText;
            File directory = new File(logPath);
            directory.mkdirs();
            File file = new File(logPath + "/exception-" + timeText + ".txt");
            if (!file.exists()) {
                file.createNewFile();
            }

            // String details = FarayanUtility.PrintException(e);
            // FarayanUtility.AppendTextToFile(file.getAbsolutePath(), details);
            FarayanUtility.PersistException(file.getAbsolutePath(), e);
        } catch (IOException iox) {
            e.printStackTrace();
        }
    }

    public static int TryParseInt(String textValue, int defaultValue) {
        try {
            textValue = ConvertNumbersToAscii(textValue);
            return Integer.parseInt(textValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static Integer TryParseInt(String textValue) {
        try {
            textValue = ConvertNumbersToAscii(textValue);
            return Integer.valueOf(textValue);
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer TryParseInt(String textValue, boolean removeNonDigits) {
        try {
            textValue = ConvertNumbersToAscii(textValue);
            if (removeNonDigits)
                textValue = textValue.replaceAll("\\D", "");
            return Integer.valueOf(textValue);
        } catch (Exception e) {
            return null;
        }
    }

    public static long TryParseLong(String textValue, int defaultValue) {
        try {
            textValue = ConvertNumbersToAscii(textValue);
            return Long.parseLong(textValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static Long TryParseLong(String textValue) {
        try {
            textValue = ConvertNumbersToAscii(textValue);
            return Long.valueOf(textValue);
        } catch (Exception e) {
            return null;
        }
    }

    public static Long TryParseLong(String textValue, boolean removeNonDigits) {
        try {
            textValue = ConvertNumbersToAscii(textValue);
            if (removeNonDigits)
                textValue = textValue.replaceAll("\\D", "");
            return Long.valueOf(textValue);
        } catch (Exception e) {
            return null;
        }
    }

    @Deprecated
    public static double TryParseDouble(String textValue, double defaultValue) {
        return TryParseDouble(textValue, defaultValue, false);
    }

    public static double TryParseDouble(String textValue, double defaultValue, boolean removeNonDigits) {
        try {
            if (removeNonDigits)
                textValue = textValue.replaceAll("\\D", "");
            textValue = ConvertNumbersToAscii(textValue);
            return Double.parseDouble(textValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static Double TryParseDouble(String textValue) {
        try {
            textValue = ConvertNumbersToAscii(textValue);
            return Double.valueOf(textValue);
        } catch (Exception e) {
            return null;
        }
    }

	/*public static String GetAuthKeyOld(String domain, String username, String password) {
		HttpClient httpClient = new DefaultHttpClient();
		String loginUrl;
		try {
			loginUrl = String.format("http://%s/login.ashx?username=%s&password=%s", domain, URLEncoder.encode(username, "UTF-8"), URLEncoder.encode(password, "UTF-8"));
			HttpGet httpGet = new HttpGet(loginUrl);
			HttpResponse response = httpClient.execute(httpGet);
			Header[] cookies = response.getHeaders("Set-Cookie");
			for (Header header : cookies) {
				if (header.getValue().startsWith(".ASPXAUTH"))
					return header.getValue().substring(".ASPXAUTH=".length(), header.getValue().indexOf(';'));
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}*/

	/*public static String GetAuthKeyNew(String protocolDomain, String username, String password) {
		HttpClient httpClient = new DefaultHttpClient();
		String loginUrl;
		try {
			loginUrl = String.format("%s/login.ashx?username=%s&password=%s", protocolDomain, URLEncoder.encode(username, "UTF-8"), URLEncoder.encode(password, "UTF-8"));
			HttpGet httpGet = new HttpGet(loginUrl);
			HttpResponse response = httpClient.execute(httpGet);
			Header[] cookies = response.getHeaders("Set-Cookie");
			for (Header header : cookies) {
				if (header.getValue().startsWith(".ASPXAUTH")) {
					return header.getValue().substring(".ASPXAUTH=".length(), header.getValue().indexOf(';'));
				}
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}*/

    public static WebResponse GetAuthCookie(String protocolDomain, String username, String password, boolean remember) {
        HttpClient httpClient = new DefaultHttpClient();
        String loginUrl;
        try {
            loginUrl = String.format(
                    "%s/login.ashx?username=%s&password=%s&remember=%s",
                    protocolDomain,
                    URLEncoder.encode(username, "UTF-8"),
                    URLEncoder.encode(password, "UTF-8"),
                    remember
            );
            HttpGet httpGet = new HttpGet(loginUrl);
            HttpResponse response = httpClient.execute(httpGet);
            Header[] cookies = response.getHeaders("Set-Cookie");
            for (Header header : cookies) {
                if (header.getValue().startsWith(".ASPXAUTH")) {
                    return new WebResponse(header.getValue(), false);
                }
            }
            return new WebResponse(null, false);
        } catch (Exception e) {
            e.printStackTrace();
            return new WebResponse(null, true);
        }
    }

    public static String GetAuthCookie(String authKey) {
        return ".ASPXAUTH=" + authKey;
    }

    public static WebResponse GetWebResponse(String address, String cookie) {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(address);
            urlConnection = (HttpURLConnection) url.openConnection();
            if (FarayanUtility.IsUsable(cookie))
                urlConnection.setRequestProperty("Cookie", cookie);
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String result = IOUtils.toString(in, StandardCharsets.UTF_8);
            return new WebResponse(result, false);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        return null;

		/*CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		if (IsUsable(cookie))
			httpGet.addHeader("cookie", cookie);
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			entity.writeTo(out);
			out.close();
			String result = out.toString();
			return new WebResponse(result, false);
		} catch (Exception e) {
			e.printStackTrace();
			FarayanUtility.Log(false, true, true, PrintException(e));
		} finally {
			try {
				if (response != null)
					response.close();
			} catch (IOException e) {
				FarayanUtility.Log(false, true, true, PrintException(e));
				e.printStackTrace();
			}
		}
		return null;*/

		/*String result = "";
		HttpParams httpParameters = new BasicHttpParams();

		HttpGet httpGet = new HttpGet(url);
		if (IsUsable(cookie))
			httpGet.addHeader("cookie", cookie);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		try {
			HttpResponse response = httpClient.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				entity.writeTo(out);
				out.close();
				result = out.toString();
				return new WebResponse(result, false);
			}
		} catch (ClientProtocolException e) {
			FarayanUtility.Log(false, true, true, "GetWebResponse, ClientProtocolException thrown, %s", e.getMessage());
			return new WebResponse(null, true);
		} catch (IOException e) {
			FarayanUtility.Log(false, true, true, "GetWebResponse, IOException thrown, %s", e.getMessage());
			return new WebResponse(null, true);
		} catch (Exception e) {
			FarayanUtility.Log(false, true, true, "GetWebResponse, Exception thrown, %s", e.getMessage());
			return new WebResponse(null, true);
		}

		return null;*/

        // HttpGet httpGet = new HttpGet(url);
        // httpGet.setParams(httpParameters);
        // if (IsUsable(cookie))
        // httpGet.addHeader("cookie", cookie);
        // try {
        // HttpResponse response = httpClient.execute(httpGet);
        // StatusLine statusLine = response.getStatusLine();
        // if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
        // HttpEntity entity = response.getEntity();
        // ByteArrayOutputStream out = new ByteArrayOutputStream();
        // entity.writeTo(out);
        // out.close();
        // result = out.toString();
        // return new WebResponse(result, false);
        // }
        // } catch (ClientProtocolException e) {
        // e.printStackTrace();
        // return new WebResponse(null, true);
        // } catch (IOException e) {
        // e.printStackTrace();
        // return new WebResponse(null, true);
        // } catch (Exception e) {
        // e.printStackTrace();
        // return new WebResponse(null, true);
        // }
        // return null;
    }

    public static boolean Download(String address, String destination) {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(address.replace(" ", "%20"));

            HttpResponse response = httpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                new File(destination).getParentFile().mkdirs();
                FileOutputStream out = new FileOutputStream(new File(destination));
                entity.writeTo(out);
                out.close();
                return true;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static JSONObject GetJSON(String url, String cookie) {
        WebResponse jsonText = GetWebResponse(url, cookie);
        if (jsonText == null)
            return null;
        if (IsNullOrEmpty(jsonText.getResponse()))
            return null;
        try {
            return new JSONObject(jsonText.getResponse());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Deprecated
    public static String MoneyFormatted(int value, boolean persian) {
        NumberFormat formatter = NumberFormat.getInstance();
        String result = formatter.format(value);
        return persian ? ConvertNumbersToPersian(result) : result;
    }

    @Deprecated
    public static String MoneyFormatted(long value, boolean persian) {
        NumberFormat formatter = NumberFormat.getInstance();
        String result = formatter.format(value);
        return persian ? ConvertNumbersToPersian(result) : result;
    }

    @Deprecated
    public static String MoneyFormatted(float value, boolean persian) {
        NumberFormat formatter = NumberFormat.getInstance();
        String result = formatter.format(value);
        return persian ? ConvertNumbersToPersian(result) : result;
    }

    @Deprecated
    public static String MoneyFormatted(double value, boolean persian) {
        NumberFormat formatter = NumberFormat.getInstance();
        String result = formatter.format(value);
        return persian ? ConvertNumbersToPersian(result) : result;
    }

    @Deprecated
    public static String MoneyFormatted(Integer value, boolean persian) {
        if (value == null)
            return persian ? ConvertNumbersToPersian("0") : "0";
        NumberFormat formatter = NumberFormat.getInstance();
        String result = formatter.format(value);
        return persian ? ConvertNumbersToPersian(result) : result;
    }

    @Deprecated
    public static String MoneyFormatted(Long value, boolean persian) {
        if (value == null)
            return persian ? ConvertNumbersToPersian("0") : "0";
        NumberFormat formatter = NumberFormat.getInstance();
        String result = formatter.format(value);
        return persian ? ConvertNumbersToPersian(result) : result;
    }

    @Deprecated
    public static String MoneyFormatted(Float value, boolean persian) {
        if (value == null)
            return persian ? ConvertNumbersToPersian("0") : "0";
        NumberFormat formatter = NumberFormat.getInstance();
        String result = formatter.format(value);
        return persian ? ConvertNumbersToPersian(result) : result;
    }

    @Deprecated
    public static String MoneyFormatted(Double value, boolean persian) {
        if (value == null)
            return persian ? ConvertNumbersToPersian("0") : "0";
        NumberFormat formatter = NumberFormat.getInstance();
        String result = formatter.format(value);
        return persian ? ConvertNumbersToPersian(result) : result;
    }

    //============


    public static String MoneyFormatted(int value, boolean persian, boolean prefixWithLtrAndSuffixWithRtl) {
        NumberFormat formatter = NumberFormat.getInstance();
        String result = formatter.format(value);
        result = persian ? ConvertNumbersToPersian(result) : result;
        result = prefixWithLtrAndSuffixWithRtl ? SurroundLTR(result) : result;
        return result;
    }

    public static String MoneyFormatted(long value, boolean persian, boolean prefixWithLtrAndSuffixWithRtl) {
        NumberFormat formatter = NumberFormat.getInstance();
        String result = formatter.format(value);
        result = persian ? ConvertNumbersToPersian(result) : result;
        result = prefixWithLtrAndSuffixWithRtl ? SurroundLTR(result) : result;
        return result;
    }

    public static String MoneyFormatted(float value, boolean persian, boolean prefixWithLtrAndSuffixWithRtl) {
        NumberFormat formatter = NumberFormat.getInstance();
        String result = formatter.format(value);
        result = persian ? ConvertNumbersToPersian(result) : result;
        result = prefixWithLtrAndSuffixWithRtl ? SurroundLTR(result) : result;
        return result;
    }

    public static String MoneyFormatted(double value, boolean persian, boolean prefixWithLtrAndSuffixWithRtl) {
        NumberFormat formatter = NumberFormat.getInstance();
        String result = formatter.format(value);
        result = persian ? ConvertNumbersToPersian(result) : result;
        result = prefixWithLtrAndSuffixWithRtl ? SurroundLTR(result) : result;
        return result;
    }

    public static String MoneyFormatted(Integer value, boolean persian, boolean prefixWithLtrAndSuffixWithRtl) {
        if (value == null)
            return persian ? ConvertNumbersToPersian("0") : "0";
        NumberFormat formatter = NumberFormat.getInstance();
        String result = formatter.format(value);
        result = persian ? ConvertNumbersToPersian(result) : result;
        result = prefixWithLtrAndSuffixWithRtl ? SurroundLTR(result) : result;
        return result;
    }

    public static String MoneyFormatted(Long value, boolean persian, boolean prefixWithLtrAndSuffixWithRtl) {
        if (value == null)
            return persian ? ConvertNumbersToPersian("0") : "0";
        NumberFormat formatter = NumberFormat.getInstance();
        String result = formatter.format(value);
        result = persian ? ConvertNumbersToPersian(result) : result;
        result = prefixWithLtrAndSuffixWithRtl ? SurroundLTR(result) : result;
        return result;
    }

    public static String MoneyFormatted(Float value, boolean persian, boolean prefixWithLtrAndSuffixWithRtl) {
        if (value == null)
            return persian ? ConvertNumbersToPersian("0") : "0";
        NumberFormat formatter = NumberFormat.getInstance();
        String result = formatter.format(value);
        result = persian ? ConvertNumbersToPersian(result) : result;
        result = prefixWithLtrAndSuffixWithRtl ? SurroundLTR(result) : result;
        return result;
    }

    public static String MoneyFormatted(Double value, boolean persian, boolean prefixWithLtrAndSuffixWithRtl) {
        if (value == null)
            return persian ? ConvertNumbersToPersian("0") : "0";
        NumberFormat formatter = NumberFormat.getInstance();
        String result = formatter.format(value);
        result = persian ? ConvertNumbersToPersian(result) : result;
        result = prefixWithLtrAndSuffixWithRtl ? SurroundLTR(result) : result;
        return result;
    }

    public static boolean SupportsCamera(PackageManager pm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
        } else {
            return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
        }
    }

    public static PersianCalendar NewPersianCalendar(int y, int mo, int d, int h, int mi, int s) {
        PersianCalendar calendar = new PersianCalendar();
        calendar.setPersianDateTime(y, mo, d, h, mi, s, 0, null);
//        calendar.set(Calendar.HOUR_OF_DAY, h);
//        calendar.set(Calendar.MINUTE, mi);
//        calendar.set(Calendar.SECOND, s);
//        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }

    public static PersianCalendar NewPersianCalendar(int y, int mo, int d, int h, int mi, int s, TimeZone timeZone) {
        PersianCalendar calendar = new PersianCalendar();
        calendar.setPersianDateTime(y, mo, d, h, mi, s, 0, timeZone);
//        calendar.set(Calendar.HOUR_OF_DAY, h);
//        calendar.set(Calendar.MINUTE, mi);
//        calendar.set(Calendar.SECOND, s);
//        calendar.set(Calendar.MILLISECOND, 0);
        //calendar.setTimeZone(timeZone);

        return calendar;
    }

    public static PersianCalendar NewPersianCalendar(TimeZone timeZone, int y, int mo, int d, int h, int mi, int s) {
        PersianCalendar calendar = new PersianCalendar();
        calendar.setPersianDateTime(y, mo, d, h, mi, s, 0, timeZone);
//        calendar.setTimeZone(timeZone);
//        calendar.setPersianDate(y, mo, d);
//        calendar.set(Calendar.HOUR_OF_DAY, h);
//        calendar.set(Calendar.MINUTE, mi);
//        calendar.set(Calendar.SECOND, s);
//        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }

    public static PersianCalendar ParsePersianDateTime(String dateText, String timeText) {
        String[] dateTextParts = dateText.split("/", 3);
        if (dateTextParts == null || dateTextParts.length != 3)
            return null;
        int[] dateParts = new int[3];
        for (int i = 0; i < 3; i++) {
            dateParts[i] = Integer.valueOf(dateTextParts[i]);
        }

        String[] timeTextParts = GetValueOrDefault(timeText, "").split(":", 3);
        if (timeTextParts == null)
            return NewPersianCalendar(dateParts[0], dateParts[1], dateParts[2], 0, 0, 0);
        if (timeTextParts.length == 2)
            timeTextParts = new String[]{timeTextParts[0], timeTextParts[1], "0"};
        int[] timeParts = new int[3];
        for (int i = 0; i < 3; i++)
            timeParts[i] = Integer.valueOf(timeTextParts[i]);

        TimeZone iranTimeZone = TimeZone.getTimeZone("Iran");
        if (timeParts[0] > 23)
            timeParts[0] = 0;
        PersianCalendar pdt = new PersianCalendar(dateParts[0], dateParts[1], dateParts[2], timeParts[0], timeParts[1], timeParts[2], 0, iranTimeZone);

        return pdt;
    }

    public static String CopyResources(Context context, int resourceId, String fileName) {

        InputStream in = context.getResources().openRawResource(resourceId);
        FileOutputStream out = null;
        try {
            File target = new File(context.getExternalFilesDir(null), fileName);
            out = new FileOutputStream(target);
            byte[] buff = new byte[1024];
            int read = 0;

            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }

            return target.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                in.close();
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String ReadableBytes(long size) {
        long KB = 1024;
        long MB = KB * 1024;
        long GB = MB * 1024;
        long TB = GB * 1024;

        if (size > TB)
            return (size / TB) + " \u062A\u0631\u0627\u0628\u0627\u06CC\u062A";

        if (size > GB)
            return (size / GB) + " \u06AF\u06CC\u06AF\u0627\u0628\u0627\u06CC\u062A";

        if (size > MB)
            return (size / MB) + " \u0645\u06AF\u0627\u0628\u0627\u06CC\u062A";

        if (size > KB)
            return (size / KB) + " \u06A9\u06CC\u0644\u0648\u0628\u0627\u06CC\u062A";

        return size + " \u0628\u0627\u06CC\u062A";
    }

    public static String ConvertNumbersToPersian(String result) {
        for (int i = 0; i < 10; i++)
            result = result.replace(i + "", ((char) (i + 0x6f0)) + "");
        return result.replace(".", "/");
    }

    public static String ConvertNumbersToAscii(String result) {
        for (int i = 0; i < 10; i++)
            result = result.replace(((char) (i + 1776)) + "", i + "");
        for (int i = 0; i < 10; i++)
            result = result.replace(((char) (i + 1632)) + "", i + "");
        return result;
    }

    public static String Join(String separator, List<?> list) {
        boolean usableSeparator = FarayanUtility.IsUsable(separator);
        if (IsNullOrEmpty(list))
            return null;
        String result = "";
        int length = list.size();
        for (int i = 0; i < length; i++) {
            result += list.get(i);
            if (usableSeparator && i != length - 1)
                result += separator;
        }
        return result;
    }

    public static long GetValueOrDefault(Long value, long defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static int GetValueOrDefault(Integer value, int defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static double GetValueOrDefault(Double value, double defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static String GetValueOrDefault(String main, String defaultValue) {
        return main == null || main.length() == 0 || "null".equals(main) ? defaultValue : main;
    }

    public static boolean IsNullOrEmpty(CharSequence text) {
        return text == null || text.length() == 0;
    }

    public static boolean IsNullOrEmpty(String text) {
        return text == null || text.length() == 0;
    }

    public static boolean IsUsable(String text) {
        return text != null && text.length() > 0;
    }

    public static boolean IsNullOrEmpty(List<?> list) {
        return list == null || list.size() == 0;
    }

    public static boolean IsUsable(List<?> list) {
        return list != null && list.size() > 0;
    }

    public static boolean IsUsable(Collection<?> collection) {
        return collection != null && collection.size() > 0;
    }

    public static boolean IsCafeBazaarInstalled(Context context) {
        final PackageManager pm = context.getPackageManager();

        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        List<String> packageNames = new ArrayList<String>();
        for (ApplicationInfo packageInfo : packages) {
            packageNames.add(packageInfo.packageName);
            if (packageInfo.packageName.toLowerCase().contains("com.farsitel.bazaar")) {
                return true;
            }
        }

        Log.i("IAB", "CafeBazar Not Found. All packages: \r\n" + Join("\r\n", packageNames));
        return false;
    }

    public static boolean AppendTextToFile(String absolutePath, String text) {
        try {
            File file = new File(absolutePath);
            file.getParentFile().mkdirs();
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(absolutePath, true)));
            out.println(text);
            out.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static String ReadAllText(String absolutePath) {
        String result = "";
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(absolutePath));
            String line = null;
            while ((line = reader.readLine()) != null) {
                result += line + "\n";
            }
            reader.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String PrintException(Throwable t) {

        String output = t.getMessage() + "\r\n";
        for (StackTraceElement st : t.getStackTrace()) {
            output += "FileName: " + st.getFileName() + "\r\n";
            output += "ClassName: " + st.getClassName() + "\r\n";
            output += "MethodName: " + st.getMethodName() + "\r\n";
            output += "LineNumber: " + st.getLineNumber() + "\r\n";
            output += "--------------------\r\n";
        }
        try {
            if (t.getCause() != null)
                output += PrintException(t.getCause());
        } catch (Exception e) {
        }
        return output += "\r\n=================\r\n";
    }

    public static void PersistException(String exceptionFilePath, Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        String error = sw.toString();

        FarayanUtility.AppendTextToFile(exceptionFilePath, error);
    }

    public static Uri ResourceUri(Context ctx, int resourceId) {
        return Uri.parse("android.resource://" + ctx.getPackageName() + "/" + resourceId);
    }

    public static String ReadableTime(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = (totalSeconds % 60);
        if (hours > 0)
            return hours + ":" + minutes + ":" + seconds;
        if (minutes > 0)
            return minutes + ":" + seconds;
        return seconds + "";
    }

    public static String ReadAssetsTextFile(AssetManager assets, String fileName) {
        try {
            return ReadInputStream(assets.open(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String ReadInputStream(InputStream stream) {
        String result = "";
        String line = "";
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        try {
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("\uFEFF"))
                    line = line.substring(1);
                result += line + "\n";
            }
            stream.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String NormalizePersian(String text) {
        text = text.replace('\u0643', '\u06A9'); // old KE to new KE
        text = text.replace('\u064A', '\u06CC');
        return text;
    }

    public static void ResizeBitmap(String source, String destination, int targetWidth, int rotationDegrees) {
        OutputStream outStream = null;

        if (destination.endsWith(".jpg") == false)
            destination += ".jpg";
        File file = new File(destination);
        if (file.exists()) {
            file.delete();
        }
        file = new File(destination);
        Bitmap originalBitmap = null, resizedBitmap = null;
        try {
            // make a new bitmap from your file
            originalBitmap = BitmapFactory.decodeFile(new File(source).getAbsolutePath());
            int originalWidth = originalBitmap.getWidth();
            int originalHeight = originalBitmap.getHeight();
            float scaleWidth = ((float) targetWidth / originalWidth);
            // float scaleHeight = ((float) targetHeight / originalHeight);
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleWidth);
            matrix.postRotate(rotationDegrees);
            resizedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalWidth, originalHeight, matrix, false);

            String path = FilenameUtils.getPath(destination);
            new File(path).mkdir();

            outStream = new FileOutputStream(file);
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (originalBitmap != null) {
                originalBitmap.recycle();
                originalBitmap = null;
            }
            if (resizedBitmap != null) {
                resizedBitmap.recycle();
                resizedBitmap = null;
            }
            System.gc();
        }
    }

    public static void ResizeBitmap(String srcPicture, int targetWidth, int rotationDegrees) throws IOException {
        OutputStream outStream = null;

        File targetPicture = File.createTempFile("edited", ".jpg");
        Bitmap originalBitmap = null, resizedBitmap = null;
        try {
            // make a new bitmap from your file
            File srcFile = new File(srcPicture);
            originalBitmap = BitmapFactory.decodeFile(srcFile.getAbsolutePath());
            int originalWidth = originalBitmap.getWidth();
            int originalHeight = originalBitmap.getHeight();
            float scaleWidth = ((float) targetWidth / originalWidth);
            // float scaleHeight = ((float) targetHeight / originalHeight);
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleWidth);
            matrix.postRotate(rotationDegrees);
            resizedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalWidth, originalHeight, matrix, false);

            //String path = FilenameUtils.getPath(targetPicture);
            //new File(path).mkdir();

            outStream = new FileOutputStream(targetPicture);
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outStream);
            outStream.flush();
            outStream.close();

            boolean deleted = srcFile.delete();
            FileUtils.moveFile(targetPicture, srcFile);
            FarayanUtility.Log(false, true, true, "deleted: %s, renamed: %s", deleted, "");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (originalBitmap != null) {
                originalBitmap.recycle();
                originalBitmap = null;
            }
            if (resizedBitmap != null) {
                resizedBitmap.recycle();
                resizedBitmap = null;
            }
            System.gc();
        }
    }

    public static Bitmap ReadBitmapAsset(Context context, String filePath, BitmapFactory.Options options) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        bitmap = BitmapFactory.decodeStream(istr, null, options);

        return bitmap;
    }


    /**
     * Version 1.0.1 @ 96/02/06
     *
     * @return
     */
    public static String GetPackageName() {
        return GlobalContext.getPackageName();
    }

    public static String GetPackageName(Context ctx) {
        return ctx.getPackageName();
    }

    public static PackageInfo GetPackageInfo() {
        return GetPackageInfo(GlobalContext);
    }

    @SuppressLint("NewApi")
    public static long GetPackageVersion() {
        boolean p = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P;
        return p ? GetPackageInfo().getLongVersionCode() : GetPackageInfo().versionCode;
    }


    public static PackageInfo GetPackageInfo(Context ctx) {
        try {
            return ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String PersianDateCompact(PersianCalendar now, String separator) {
        String result = "";
        result += LeadingZero(now.getPersianYear(), 4) + separator;
        result += LeadingZero(now.getPersianMonthIndexFrom1(), 2) + separator;
        result += LeadingZero(now.getPersianDay(), 2) + separator;

        return result;
    }

    public static String PersianTimeCompact(PersianCalendar now, String separator) {
        String result = "";
        result += LeadingZero(now.getHour(), 2) + separator;
        result += LeadingZero(now.getMinute(), 2) + separator;
        result += LeadingZero(now.getSecond(), 2) + separator;
        result += LeadingZero(now.getMillisecond(), 3) + separator;

        return result;
    }

    public static String PersianDateTimeCompact(PersianCalendar now, String separator) {
        String result = "";
        result += LeadingZero(now.getPersianYear(), 4) + separator;
        result += LeadingZero(now.getPersianMonthIndexFrom1(), 2) + separator;
        result += LeadingZero(now.getPersianDay(), 2) + separator;
        result += LeadingZero(now.getHour(), 2) + separator;
        result += LeadingZero(now.getMinute(), 2) + separator;
        result += LeadingZero(now.getSecond(), 2) + separator;
        result += LeadingZero(now.getMillisecond(), 3) + separator;

        return result;
    }

    public static String LeadingZero(int number, int length) {
        String result = number + "";
        while (result.length() < length) {
            result = "0" + result;
        }

        return result;
    }

    public static void Log(boolean toast, boolean log, boolean file, String template, Object... values) {
        PersianCalendar pc = new PersianCalendar();
        if (values != null && values.length > 0)
            template = stringFormat(template, values);

        if (toast) {
            try {
                ShowToast(template);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (log) {
            try {
                Log.i("FarayanUtilityLog", template);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (file) {
            try {

                String yearText = pc.getPersianYear() + "";
                String monthText = FarayanUtility.LeadingZero(pc.getPersianMonthIndexFrom1(), 2);
                String dayText = FarayanUtility.LeadingZero(pc.getPersianDay(), 2);
                String hourText = FarayanUtility.LeadingZero(pc.getHour(), 2);
				/*String minuteSecondsMillis = String.format(
						"%s.%s.%s",
						FarayanUtility.LeadingZero(pc.getMinute(), 2),
						FarayanUtility.LeadingZero(pc.getSecond(), 2),
						FarayanUtility.LeadingZero(pc.getMillisecond(), 3)
				);*/

                String fileName = String.format(
                        "%s.%s.%s.%s",
                        FarayanUtility.LeadingZero(pc.getPersianYear(), 4),
                        FarayanUtility.LeadingZero(pc.getPersianMonthIndexFrom1(), 2),
                        FarayanUtility.LeadingZero(pc.getPersianDay(), 2),
                        FarayanUtility.LeadingZero(pc.getHour(), 2)
                );

                String logFile = FarayanBaseApp
                        .Instance()
                        .getExternalFilesDir("logs")
                        + "/"
                        + yearText
                        + "/"
                        + monthText
                        + "/"
                        + dayText
                        + "/"
                        + hourText
                        + "/"
                        + fileName
                        + ".txt";

                String callerMethod = CallerMethod(2);
                AppendTextToFile(logFile, pc.getPersianDateTimeStandard(false) + "\t" + callerMethod + "/" + template + "\n------------------------------------\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String stringFormat(String template, Object[] values) {
        try {
            return String.format(template, values);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Unable to format below template with provided values:\n" +
                            "`" + template + "`\n" +
                            dump(values),
                    e
            );
        }
    }

    private static String dump(Object[] values) {
        if (values == null)
            return "NULL";
        StringBuilder sb = new StringBuilder();
        for (Object value : values) {
            sb.append(value == null ? "NULL" : value.toString());
            sb.append(",");
        }
        return sb.toString();
    }

    public static String CallerMethod(int level) {
        try {
            throw new Exception();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String error = sw.toString();
            String[] lines = error.split("[\r\n]");
            if (lines != null && lines.length >= 3 + level) {
                String line = lines[2 + level];
                if (line.startsWith("	at "))
                    return line.substring("	at ".length());
            }
        }
        return "";
    }

    public static void RenameColumns(SQLiteDatabase db, String table, Pair<String, String>... pairs) {
        ArrayList<String> columns = new ArrayList<String>();
        String cmd = "pragma table_info(" + table + ");";
        Cursor cur = db.rawQuery(cmd, null);

        while (cur.moveToNext()) {
            // String name = cur.getString(cur.getColumnIndex("name"));
            // String type = cur.getString(cur.getColumnIndex("type"));
            // String notnull = cur.getString(cur.getColumnIndex("notnull"));
            // String defaultValue = cur.getString(cur.getColumnIndex("dflt_value"));
            // String primaryKey = cur.getString(cur.getColumnIndex("pk"));
            // String definition = String.format("%s %s %s", name, type, defaultValue);
            // if (primaryKey)
            // definition += "";
            // columns.add(definition);
        }
        cur.close();

        String tableRename = "ALTER TABLE %s RENAME TO %s_old";
        db.execSQL(tableRename);
        String tableCreate = "CREATE TABLE %s";

    }


    public static int NextRandom(int includingMin, int excludingMax) {
        return (int) (Math.random() * (excludingMax - includingMin)) + includingMin;
    }


    public static boolean IsNullOrEmpty(Editable text) {
        if (text == null)
            return false;
        return FarayanUtility.IsNullOrEmpty(text.toString());
    }

    public static String PreparePattern(String text) {
        if (FarayanUtility.IsNullOrEmpty(text))
            return "";
        text = FarayanUtility.NormalizePersian(text);
        text = FarayanUtility.ConvertNumbersToAscii(text);
        /**
         * نام جاگذارهای مشخص شده درعبارت منظم را پاک می‌کنیم چرا که در نسخه‌های قدیمی‌تر پشتیبانی نمی‌شوند
         * Version 2.2.18.1 @ 96/02/14
         */
        text = text.replaceAll("\\?\\<\\w+\\>", "");

        while (text.contains("\r\n"))
            text = text.replace("\r\n", "\n");
        text = text.trim();
        return text;
    }

    public static String PersianJoin(List<String> parts) {
        String separator = "، ";
        String and = " و ";
        if (parts == null)
            return "";
        if (parts.size() == 0)
            return parts.get(0);
        String result = "";
        for (int i = 0; i < parts.size(); i++) {
            result += parts.get(i);
            if (i < (parts.size() - 2))
                result += separator;
            if (i == (parts.size() - 2))
                result += and;
        }
        return result;

    }

    public static boolean TextEquals(String text1, String text2, boolean ignoreCase) {
        if (IsNullOrEmpty(text1) && IsNullOrEmpty(text2))
            return true;
        if (IsNullOrEmpty(text1) || IsNullOrEmpty(text2))
            return false;
        return ignoreCase ? text1.equalsIgnoreCase(text2) : text1.equals(text2);
    }

    public static boolean TextNotEquals(String text1, String text2, boolean ignoreCase) {
        return !TextEquals(text1, text2, ignoreCase);
    }

    public static String Or(String value, String substitue) {
        return IsUsable(value) ? value : substitue;
    }

    public static String DepartNumber(String input, int start, int length, String separator) {
        if (FarayanUtility.IsNullOrEmpty(separator))
            return input;
        if (length == 0)
            return "";
        input = input.replaceAll("\\D", "");
        String output = "";
        if (start > 0 && input.length() < start)
            return input;
        if (start > 0 && input.length() >= start) {
            output += input.substring(0, start) + separator;
            input = input.substring(start, input.length());
        }
        for (int i = 0; i < input.length(); i += length)
            output += input.substring(i, Math.min(input.length(), i + length)) + separator;

        if (output.endsWith(separator))
            output = output.substring(0, output.length() - separator.length());

        return output;
    }

    public static String Trim(String text, String what, boolean start, boolean finish) {
        if (FarayanUtility.IsNullOrEmpty(text))
            return text;
        if (FarayanUtility.IsNullOrEmpty(what))
            return text;
        if (start)
            text = TrimStart(text, what);
        if (finish)
            text = TrimFinish(text, what);
        return text;
    }

    public static String TrimStart(String text, String what) {
        if (FarayanUtility.IsNullOrEmpty(text))
            return text;
        if (FarayanUtility.IsNullOrEmpty(what))
            return text;
        if (text.startsWith(what))
            text = text.substring(what.length());
        return text;
    }

    public static String TrimFinish(String text, String what) {
        if (FarayanUtility.IsNullOrEmpty(text))
            return text;
        if (FarayanUtility.IsNullOrEmpty(what))
            return text;
        if (text.endsWith(what))
            text = text.substring(0, text.length() - what.length());
        return text;
    }

    public static View.OnFocusChangeListener HideKeyboardOnFocusLose = (v, hasFocus) -> {
		/*InputMethodManager imm = (InputMethodManager) GlobalContext.getSystemService(Context.INPUT_METHOD_SERVICE);

		if (!hasFocus) {
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		} else {
			imm.showSoftInput(v, 0);
		}*/
    };

    public static boolean IsPackageInstalled(String packageName) {
        PackageManager pm = GlobalContext.getPackageManager();
        try {
            pm.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * تک تک کاراکترهای یک متن را در آوده و در یک متن قرار می‌دهد
     *
     * @param text
     * @return
     */
    public static String CharDump(String text) {
        List<String> result = new ArrayList<String>();
        for (char c : text.toCharArray())
            result.add(((int) c) + "");
        return FarayanUtility.Join(",", result);
    }

    public static boolean IsUsable(Editable text) {
        return text != null && IsUsable(text.toString());
    }

    /**
     * use {@link PersianCalendar#getPersianWeekStart()}
     *
     * @return
     */
    @Deprecated
    public static PersianCalendar WeekStart() {
        PersianCalendar pc = new PersianCalendar();
        pc.addDays(-pc.PersianWeekDay().PersianDayIndex + 1);
        pc.setTime(0, 0, 0, 0);
        return pc;
    }

    /**
     * use {@link PersianCalendar#getPersianToday()}
     *
     * @return
     */
    @Deprecated
    public static PersianCalendar Today() {
        PersianCalendar pc = new PersianCalendar();
        pc.setTime(0, 0, 0, 0);
        return pc;
    }

    /**
     * use {@link PersianCalendar#getPersianYesterday()}
     *
     * @return
     */
    @Deprecated
    public static PersianCalendar Yesterday() {
        PersianCalendar pc = Today();
        pc.addDays(-1);
        return pc;
    }

    /**
     * use {@link PersianCalendar#getPersianMonthStart()}
     *
     * @return
     */
    @Deprecated
    public static PersianCalendar MonthStart() {
        PersianCalendar today = Today();
        PersianCalendar monthStart = new PersianCalendar(today.getPersianYear(), today.getPersianMonthIndexFrom1(), 1, false);
        return monthStart;
    }

    /**
     * use {@link PersianCalendar#getPersianSeasonStart()}
     *
     * @return
     */
    @Deprecated
    public static PersianCalendar SeasonStart() {
        PersianCalendar today = Today();
        int seasonStartMonth = SeasonStartMonth(today.getPersianMonthIndexFrom1());
        PersianCalendar monthStart = new PersianCalendar(today.getPersianYear(), seasonStartMonth, 1, false);
        return monthStart;
    }

    /**
     * use {@link PersianCalendar#getPersianSeasonStartMonth(int)}
     *
     * @return
     */
    @Deprecated
    private static int SeasonStartMonth(int persianMonth) {
        switch (persianMonth) {
            case 1:
            case 2:
            case 3:
                return 1;
            case 4:
            case 5:
            case 6:
                return 4;
            case 7:
            case 8:
            case 9:
                return 7;
            case 10:
            case 11:
            case 12:
                return 10;
            default:
                return -1;
        }
    }

    /**
     * use {@link PersianCalendar#getPersianYearStart()} ()}
     *
     * @return
     */
    @Deprecated
    public static PersianCalendar YearStart() {
        return new PersianCalendar(Today().getPersianYear(), 1, 1, false);
    }

    @Deprecated
    public static PersianCalendar Tomorrow() {
        PersianCalendar today = Today();
        today.addDays(1);
        return today;
    }

    public static boolean IsNullOrEmpty(Collection<?> collection) {
        if (collection == null)
            return true;
        return collection.size() <= 0;
    }

    public static int ConvertDensityPixelToPixel(Context context, int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    public static boolean IsEmail(String text) {
        return EmailPattern().matcher(text).find();
    }

    public static Pattern EmailPattern() {
        if (m_emailPattern == null)
            m_emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        return m_emailPattern;
    }

    /**
     * https://stackoverflow.com/questions/4096851/remove-underline-from-links-in-textview-android
     *
     * @param textView
     */
    public static void StripUnderlines(TextView textView) {
        Spannable s = new SpannableString(textView.getText());
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        textView.setText(s);
    }

    public static <T extends Comparable<T>> T Min(List<T> values) {
        if (IsNullOrEmpty(values))
            return null;
        T min = values.get(0);
        for (T t : values) {
            if (t.compareTo(min) == -1)
                min = t;
        }
        return min;
    }

    public static <T extends Comparable<T>> T Min(T... ts) {
        if (ts == null || ts.length == 0)
            return null;
        T min = ts[0];
        for (T t : ts) {
            if (t.compareTo(min) == -1)
                min = t;
        }
        return min;
    }

    public static Long TryParseTime(String part) {
        if (FarayanUtility.IsNullOrEmpty(part))
            return null;
        String[] parts = part.split(":");
        if (parts == null || parts.length == 0 || parts.length > 3)
            return null;
        Integer hour, minute, second;
        hour = TryParseInt(parts[0]);
        if (hour == null)
            return null;
        if (hour < 0)
            return null;
        if (hour > 23)
            return null;
        if (parts.length > 1) {
            minute = FarayanUtility.TryParseInt(parts[1]);
            if (minute == null)
                return null;
            if (minute < 0)
                return null;
            if (minute > 59)
                return null;
        } else {
            minute = 0;
        }

        if (parts.length > 2) {
            second = FarayanUtility.TryParseInt(parts[2]);
            if (second == null)
                return null;
            if (second < 0)
                return null;
            if (second > 59)
                return null;
        } else {
            second = 0;
        }

        return Long.valueOf(hour * 60 * 60 * 1000 + minute * 60 * 1000 + second * 1000);
    }

    public static final long OneHourMilliseconds = 60 * 60 * 1000;
    public static final long OneMinuteMilliseconds = 60 * 1000;
    public static final long OneSecondMilliseconds = 1000;

    public static String TimeToText(Long value, TimeTextModes mode, boolean appendLeadingZero) {
        return TimeToText(value, mode, appendLeadingZero, false);
    }

    public static String TimeToText(Long value, TimeTextModes mode, boolean appendLeadingZero, boolean persianNumbers) {
        if (value == null)
            return "";
        int hours = (int) (value / OneHourMilliseconds);
        int minutes = (int) ((value % OneHourMilliseconds) / OneMinuteMilliseconds);
        int seconds = (int) ((value % OneMinuteMilliseconds) / OneSecondMilliseconds);
        switch (mode) {
            case Full:
                String fulResult = FarayanUtility.LeadingZero(hours, appendLeadingZero ? 2 : 1) + ":" + FarayanUtility.LeadingZero(minutes, appendLeadingZero ? 2 : 1) + ":" + FarayanUtility.LeadingZero(seconds, appendLeadingZero ? 2 : 1);
                if (persianNumbers)
                    fulResult = ConvertNumbersToPersian(fulResult);
                return fulResult;
            case Simple:
                String result = "" + FarayanUtility.LeadingZero(hours, appendLeadingZero ? 2 : 1);
                if (minutes > 0 || seconds > 0) {
                    result += ":" + FarayanUtility.LeadingZero(minutes, appendLeadingZero ? 2 : 1);
                    if (seconds > 0)
                        result += ":" + FarayanUtility.LeadingZero(seconds, appendLeadingZero ? 2 : 1);
                }
                if (persianNumbers)
                    result = ConvertNumbersToPersian(result);
                return result;
            default:
                throw new RuntimeException("");
        }
    }

    public static boolean IsUsable(CharSequence error) {
        return error != null && error.length() > 0;
    }

    public static <T extends Number> boolean IsBetween(T value, T min, T max) {
        FarayanUtility.Log(false, true, true, "min: %s, value: %s, max: %s", min, value, max);
        return min.doubleValue() < value.doubleValue() && value.doubleValue() < max.doubleValue();
        //return min.compareTo(value) <= 0 && 0 <= max.compareTo(value);
    }

    public static Bitmap getBitmap(String path) {

        Uri uri = Uri.fromFile(new File(path));
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 16 * 32 * 1024; // 0.2MP
            in = FarayanUtility.GlobalContext.getContentResolver().openInputStream(uri);

            // Decode image size
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();


            int scale = 1;
            while ((options.outWidth * options.outHeight) * (1 / Math.pow(scale, 2)) > IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d("", "scale = " + scale + ", orig-width: " + options.outWidth + ", orig-height: " + options.outHeight);

            Bitmap bitmap = null;
            in = FarayanUtility.GlobalContext.getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                options = new BitmapFactory.Options();
                options.inSampleSize = scale;
                bitmap = BitmapFactory.decodeStream(in, null, options);

                // resize to desired dimensions
                int height = bitmap.getHeight();
                int width = bitmap.getWidth();
                Log.d("", "1th scale operation dimenions - width: " + width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) x, (int) y, true);
                bitmap.recycle();
                bitmap = scaledBitmap;

                System.gc();
            } else {
                bitmap = BitmapFactory.decodeStream(in);
            }
            in.close();

            Log.d("", "bitmap size - width: " + bitmap.getWidth() + ", height: " + bitmap.getHeight());
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            Log.e("", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String Repeat(String text, int count, String separator) {
        if (count <= 0)
            return "";
        List<String> array = new ArrayList<>(count);
        for (int index = 0; index < count; index++) {
            array.add(text);
        }

        return Join(separator, array);
    }

    public static void RequestPermission(
            FarayanBaseCoreActivity activity,
            Permissions permission,
            Action onCommentNeeded,
            boolean commented,
            Action onGranted,
            Action onDenied
    ) {
        if (ContextCompat.checkSelfPermission(activity, permission.Name) != PackageManager.PERMISSION_GRANTED) {
            activity.setOnPermissionRequestAnswered = (requestCode, permissions, grantResults) -> {
                if (requestCode == permission.Code) {
                    if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                        if (onDenied == null) {
                            throw new RuntimeException("onDenied is null");
                        }
                        onDenied.execute();
                    } else {
                        if (onGranted == null) {
                            throw new RuntimeException("onGranted is null");
                        }
                        onGranted.execute();
                    }
                }
            };
            if (!commented && activity.shouldShowRequestPermissionRationale(permission.Name)) {
                if (onCommentNeeded == null) {
                    throw new RuntimeException("onCommentNeeded is null");
                }
                onCommentNeeded.execute();
            } else {
                activity.requestPermissions(new String[]{permission.Name}, permission.Code);
            }
        } else {
            onGranted.execute();
        }
    }

    public static void RequestPermissions(
            FarayanBaseCoreActivity activity,
            Permissions[] permissions,
            Action onCommentNeeded,
            boolean commented,
            Action onGranted,
            Action onDenied
    ) {
		/*if (ContextCompat.checkSelfPermission(activity, permission.Name) != PackageManager.PERMISSION_GRANTED) {
			activity.setOnPermissionRequestAnswered = (requestCode, permissions, grantResults) -> {
				if (requestCode == permission.Code) {
					if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

						if (onDenied == null) {
							throw new RuntimeException("onDenied is null");
						}
						onDenied.execute();
					} else {
						if (onGranted == null) {
							throw new RuntimeException("onGranted is null");
						}
						onGranted.execute();
					}
				}
			};
			if (!commented && activity.shouldShowRequestPermissionRationale(permission.Name)) {
				if (onCommentNeeded == null) {
					throw new RuntimeException("onCommentNeeded is null");
				}
				onCommentNeeded.execute();
			} else {
				activity.requestPermissions(new String[]{permission.Name}, permission.Code);
			}
		} else {
			onGranted.execute();
		}*/
    }

    public static double DivideRound(int what, int by, int precision) {
        return DivideRound((double) what, (double) by, precision);
    }

    public static double DivideRound(long what, long by, int precision) {
        return DivideRound((double) what, (double) by, precision);
    }

    public static double DivideRound(long what, int by, int precision) {
        return DivideRound((double) what, (double) by, precision);
    }

    public static double DivideRound(double what, double by, int precision) {
        double coefficient = Math.pow(10, precision);
        double v1 = what * coefficient / by;
        long round = Math.round(v1);
        double result = round / coefficient;
        return result;
    }

    public static double DivideCeil(int what, int by, int precision) {
        return DivideCeil((double) what, by, precision);
    }

    public static double DivideCeil(double what, int by, int precision) {
        double doubleBy = (double) by;
        double coefficient = Math.pow(10, precision);
        double v1 = what * coefficient / doubleBy;
        double round = Math.ceil(v1);
        double result = round / coefficient;
        return result;
    }

    public static double DivideFloor(int what, int by, int precision) {
        return DivideFloor((double) what, by, precision);
    }

    public static double DivideFloor(double what, int by, int precision) {
        double doubleBy = (double) by;
        double coefficient = Math.pow(10, precision);
        double v1 = what * coefficient / doubleBy;
        double round = Math.floor(v1);
        double result = round / coefficient;
        return result;
    }

    public static String FileUrl(String file) {
        if (IsNullOrEmpty(file))
            return null;

        String decodedUri = Uri.fromFile(new File(file)).toString();

        return decodedUri;
    }

    public static boolean HasPermission(Permissions permission) {
        int permissionStatus = ActivityCompat.checkSelfPermission(
                GlobalContext,
                permission.Name
        );
        boolean granted = permissionStatus == PackageManager.PERMISSION_GRANTED;
        return granted;
    }

    public static boolean HasAllPermission(Permissions... permissions) {
        return Arrays.stream(permissions).allMatch(FarayanUtility::HasPermission);
    }

    public static boolean HasAnyPermission(Permissions... permissions) {
        return Arrays.stream(permissions).anyMatch(FarayanUtility::HasPermission);
    }

    public static boolean HasNonePermission(Permissions... permissions) {
        return Arrays.stream(permissions).noneMatch(FarayanUtility::HasPermission);
    }

    public static void ThrowRuntimeExceptionOnException(Action callback) {
        try {
            if (callback != null)
                callback.execute();
        } catch (Exception ignored) {
            throw new RuntimeException(ignored);
        }
    }


    public static void prepareVerticalRecyclerView(RecyclerView recyclerView) {
        prepareRecyclerView(recyclerView, RecyclerViewOrientations.Vertical, null, true, true);
    }

    public static void prepareRecyclerView(
            RecyclerView recyclerView,
            RecyclerViewOrientations orientation,
            Integer divider,
            boolean defaultDivider
    ) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        linearLayoutManager.setOrientation(orientation.Value);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        if (defaultDivider || divider != null) {
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
            //dividerItemDecoration.setDrawable(recyclerView.getContext().getResources().getDrawable(divider == null ? R.drawable.divider : divider));
            divider = divider == null ? R.drawable.divider : divider;
            Drawable drawable = ContextCompat.getDrawable(recyclerView.getContext(), divider);
            assert drawable != null;
            dividerItemDecoration.setDrawable(drawable);
            recyclerView.addItemDecoration(dividerItemDecoration);
        }
    }

    public static void prepareRecyclerView(
            RecyclerView recyclerView,
            RecyclerViewOrientations orientation,
            Integer divider,
            boolean defaultDivider,
            boolean scrollable
    ) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext()) {
            @Override
            public boolean canScrollVertically() {
                return orientation == RecyclerViewOrientations.Vertical && scrollable;
            }

            @Override
            public boolean canScrollHorizontally() {
                return orientation == RecyclerViewOrientations.Horizontal && scrollable;
            }
        };
        linearLayoutManager.setOrientation(orientation.Value);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(scrollable);

        if (defaultDivider || divider != null) {
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
            dividerItemDecoration.setDrawable(recyclerView.getContext().getResources().getDrawable(divider == null ? R.drawable.divider : divider));
            recyclerView.addItemDecoration(dividerItemDecoration);
        }
    }

    public static String Queryable(String query) {
        if (query == null)
            return null;
        return FarayanUtility
                .ConvertNumbersToAscii(query.trim())
                .replaceAll("آ", "ا")
                .replaceAll("ي", "ی")
                .replaceAll("ك", "ک")
                .replaceAll("ظ", "ز")
                .replaceAll("ذ", "ز")
                .replaceAll("ظ", "ز")
                .replaceAll("ط", "ت")
                .replaceAll("ح", "ه")
                .replaceAll("ص", "س")
                .replaceAll("ث", "س")
                .replaceAll(" ", "")
                .replaceAll("‌", "")
                .replaceAll("ئ", "ی")
                .replaceAll("غ", "ق")
                ;
    }

    public static String Persistable(String text) {
        return text == null ? "" : text.trim();
    }

    public static void RequestWriteExternalStoragePermission(FarayanBaseCoreActivity activity, Action onSuccess, boolean commented) {
        RequestPermission(
                activity,
                Permissions.WriteExternalStorage,
                () -> new Builder(activity)
                        .setMessage("برای دخیره‌ی عکس‌ها نیاز به مجوز دسترسی به حافظه داریم")
                        .setPositiveButton("اعطای مجوز", (dialog, which) -> RequestWriteExternalStoragePermission(activity, onSuccess, true))
                        .setNegativeButton("دسترسی لازم نیست", null)
                        .show(),
                commented,
                onSuccess,
                () -> new Builder(activity)
                        .setMessage(
                                "شما مجوز فایل را ندادید. در صورتی که دوباره برروی دکمه‌ی عکس کلیک کنید دوباره پنجره‌ی درخواست مجوز فایل را می‌بینید"
                                        + "\n"
                                        + "در صورتی که پنجره‌ی درخواست مجوز نمایش داده نشد، باید به صورت دستی مجوز دسترسی به دوربین را بدهید."
                        )
                        .setPositiveButton("تغییر تنظیمات", (dialog, which) -> {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                            intent.setData(uri);
                            activity.startActivity(intent);
                        })
                        .setNeutralButton("خب", null)
                        .show()
        );
    }

    public static void RequestCameraPermission(FarayanBaseCoreActivity activity, Action onSuccess, boolean commented) {
        RequestCameraPermission(
                activity,
                onSuccess,
                commented,
                "برای تهیه عکس نیاز به مجوز دسترسی به دوربین داریم",
                "اعطای مجوز",
                "عکس لازم نیست",
                "شما مجوز دوربین را ندادید. در صورتی که دوباره برروی دکمه‌ی عکس کلیک کنید دوباره پنجره‌ی درخواست مجوز دوربین را می‌بینید\n"
                        + "در صورتی که پنجره‌ی درخواست مجوز نمایش داده نشد، باید به صورت دستی مجوز دسترسی به دوربین را بدهید.",
                "تغییر تنظیمات",
                "خب"
        );
    }

    public static void RequestCameraPermission(
            FarayanBaseCoreActivity activity,
            Action onSuccess,
            boolean commented,
            String reason,
            String requestPositiveCommand,
            String requestNegativeCommand,
            String deniedMessage,
            String settingChangeCommand,
            String neutralCommand
    ) {
        RequestPermission(
                activity,
                Permissions.Camera,
                () -> new Builder(activity)
                        .setMessage(reason)
                        .setPositiveButton(requestPositiveCommand, (dialog, which) -> RequestCameraPermission(
                                activity,
                                onSuccess,
                                true,
                                reason,
                                requestPositiveCommand,
                                requestNegativeCommand,
                                deniedMessage,
                                settingChangeCommand,
                                neutralCommand
                        ))
                        .setNegativeButton(requestNegativeCommand, null)
                        .show(),
                commented,
                onSuccess,
                () -> new Builder(activity)
                        .setMessage(deniedMessage)
                        .setPositiveButton(settingChangeCommand, (dialog, which) -> {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                            intent.setData(uri);
                            activity.startActivity(intent);
                        })
                        .setNeutralButton(neutralCommand, null)
                        .show()
        );
    }

    public static void RequestLocationPermission(
            FarayanBaseCoreActivity activity,
            boolean commented,
            ActionWith1Param<Boolean> onFinished,
            String reason,
            String commentDialogTitle,
            String deniedGrantByConfigDialogMessage,
            String deniedCancelMessage,
            String deniedGrantPermissionByConfigPositiveCommand,
            String deniedSkipGrantPermissionByConfigCommand
    ) {
        RequestPermission(
                activity,
                Permissions.FineLocation,
                () -> new Builder(activity)
                        .setMessage(reason)
                        .setPositiveButton(
                                commentDialogTitle,
                                (dialog, which) -> RequestLocationPermission(
                                        activity,
                                        true,
                                        onFinished,
                                        reason,
                                        commentDialogTitle,
                                        deniedGrantByConfigDialogMessage,
                                        deniedCancelMessage,
                                        deniedGrantPermissionByConfigPositiveCommand,
                                        deniedSkipGrantPermissionByConfigCommand
                                )
                        )
                        .setNegativeButton(deniedCancelMessage, null)
                        .show(),
                commented,
                () -> {
                    if (onFinished != null)
                        onFinished.execute(true);
                },
                () -> new Builder(activity)
                        .setMessage(deniedGrantByConfigDialogMessage)
                        .setPositiveButton(deniedGrantPermissionByConfigPositiveCommand, (dialog, which) -> {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                            intent.setData(uri);
                            activity.startActivity(intent);
                        })
                        .setNeutralButton(deniedSkipGrantPermissionByConfigCommand, (dialog, which) -> {
                            if (onFinished != null)
                                onFinished.execute(false);
                        })
                        .show()
        );
    }

    public static int Compare(double v1, double v2) {
        if (v1 > v2)
            return 1;
        if (v1 < v2)
            return -1;
        return 0;
    }

    public static void zipFolder(String inputFolderPath, String outZipPath) {
        try {
            FileOutputStream fos = new FileOutputStream(outZipPath);
            ZipOutputStream zos = new ZipOutputStream(fos);
            File srcFile = new File(inputFolderPath);
            File[] files = srcFile.listFiles();
            Log.d("", "Zip directory: " + srcFile.getName());
            for (int i = 0; i < files.length; i++) {
                Log.d("", "Adding file: " + files[i].getName());
                byte[] buffer = new byte[1024];
                FileInputStream fis = new FileInputStream(files[i]);
                zos.putNextEntry(new ZipEntry(files[i].getName()));
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();
        } catch (IOException ioe) {
            Log.e("", ioe.getMessage());
        }
    }

    public static void unzip(Uri src, String dest) {

        final int BUFFER_SIZE = 4096;

        BufferedOutputStream bufferedOutputStream = null;
        InputStream fileInputStream;
        try {
            fileInputStream = GlobalContext.getContentResolver().openInputStream(src);// new FileInputStream(src);
            ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(fileInputStream));
            ZipEntry zipEntry;

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {

                String zipEntryName = zipEntry.getName();

                File destFolder = new File(dest);
                destFolder.mkdirs();

                File file = new File(dest + "/" + zipEntryName);

                if (file.exists()) {

                } else {
                    if (zipEntry.isDirectory()) {
                        file.mkdirs();
                    } else {
                        byte buffer[] = new byte[BUFFER_SIZE];
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        bufferedOutputStream = new BufferedOutputStream(fileOutputStream, BUFFER_SIZE);
                        int count;

                        while ((count = zipInputStream.read(buffer, 0, BUFFER_SIZE)) != -1) {
                            bufferedOutputStream.write(buffer, 0, count);
                        }

                        bufferedOutputStream.flush();
                        bufferedOutputStream.close();
                    }
                }
            }
            zipInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static EmptyBitmap Rotate(String photo, int degree, ImageView imageView) {
        if (IsNullOrEmpty(photo))
            return null;
        File photoFile = new File(photo);
        if (!photoFile.exists())
            return null;
        Bitmap src = BitmapFactory.decodeFile(photo);
        Matrix m = new Matrix();
        m.postRotate(degree);
        Bitmap destBitmap = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), m, true);
        EmptyBitmap emptyBitmap = new EmptyBitmap(destBitmap.getWidth(), destBitmap.getHeight());
        photoFile.delete();
        OutputStream outStream;
        try {
            outStream = new FileOutputStream(photoFile);
            destBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outStream);
            outStream.flush();
            outStream.close();
            destBitmap.recycle();
            destBitmap = null;
            System.gc();
            if (imageView != null)
                ImageLoader.getInstance().displayImage(Uri.fromFile(photoFile).toString(), imageView);
            return emptyBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void maximizePictureWithAspectRatio(Activity activity, Bitmap loadedImage, ImageView imageView) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        double ratio = DivideFloor(loadedImage.getWidth(), displayMetrics.widthPixels, 2);
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = displayMetrics.widthPixels;
        layoutParams.height = (int) (loadedImage.getHeight() / ratio);
        activity.runOnUiThread(() -> imageView.setLayoutParams(layoutParams));
    }

    public static void maximizePictureWithAspectRatio(Activity activity, int width, int height, ImageView imageView) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        double ratio = DivideFloor(width, displayMetrics.widthPixels, 2);
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = displayMetrics.widthPixels;
        layoutParams.height = (int) (height / ratio);
        //getActivity().runOnUiThread(() -> PreviewImageView().setLayoutParams(layoutParams));
    }

    public static String SurroundLTR(String string) {
        return LTR + string + RTL;
    }

    public static String Displayable(String title) {
        if (title == null)
            return "";
        return title.trim();
    }

    public static <TResult> TResult CatchException(Function0<TResult> callable, Function<Exception, TResult> handler) {
        try {
            return callable.invoke();
        } catch (Exception e) {
            if (handler == null)
                throw new RuntimeException(e);
            return handler.apply(e);
        }
    }

    public static void KeepScreenOn(Window window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public static void ReleaseScreenOn(Window window) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public static double Round(double value, int precision) {
        double scale = Math.pow(10, precision);
        return Math.round(value * scale) / scale;
    }
}
