package appteligent.varunvishwanathan.com.crashlist;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ServerConnection {
    private static String CRITTERCISM_LOGS = "https://developers.crittercism.com/v1.0/app/519d53101386202089000007/crash/summaries";
    private static String AUTHORIZATION = "Authorization";
    private static String KEY = "5a839540a09f12373e52c7c82680318e";
    private static String NAME = "name";
    private static String STATUS = "status";

    private Context mContext;
    private ServerListener mListener;
    private String mUrl;
    private String mKey;
    private int mTimeout;

    public ServerConnection(Context context, String url, String key, int timeout, ServerListener listener) {
        this.mContext = context;
        this.mUrl = url;
        this.mKey = key;
        this.mListener = listener;
        this.mTimeout = timeout;

    }

    public void makeRequest() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(mUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<CrashDetail> crashNames = new ArrayList<CrashDetail>();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject j = response.getJSONObject(i);
                        String name = j.getString(NAME);
                        String status = j.getString(STATUS);
                        crashNames.add(new CrashDetail(name, status));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mListener.onSuccess(crashNames);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                mListener.onError(e);
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap< String, String > headers = new HashMap <String, String>();
                headers.put("Content-Type", "application/json");
                headers.put(AUTHORIZATION, "Bearer " + mKey);
                return headers;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(mTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(mContext).add(jsonArrayRequest);

    }
}
