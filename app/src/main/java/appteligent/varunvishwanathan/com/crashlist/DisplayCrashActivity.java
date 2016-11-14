package appteligent.varunvishwanathan.com.crashlist;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DisplayCrashActivity extends AppCompatActivity {
    private static String CRITTERCISM_LOGS = "https://developers.crittercism.com/v1.0/app/519d53101386202089000007/crash/summaries";
    private static String KEY = "5a839540a09f12373e52c7c82680318e";
    private static String TAG = "DisplayCrash";
    private static String NAME = "name";
    private static String STATUS = "status";

    private ListView mListView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_crash);
        mListView = (ListView) findViewById(R.id.list);

        ServerConnection conn = new ServerConnection(this, CRITTERCISM_LOGS, KEY, 2000, new ServerListener() {
            @Override
            public void onSuccess(ArrayList<CrashDetail> details) {
                try {
                    displayContents(details);
                } catch (Exception e) {
                    e.printStackTrace();;
                }
            }

            @Override
            public void onError(VolleyError e) {
                displayError(e);
            }
        });
        conn.makeRequest();
    }


    private void displayContents(final ArrayList<CrashDetail> details) throws Exception {
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_2, android.R.id.text1, details) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setTextColor(getResources().getColor(R.color.textColorPrimary));
                text2.setTextColor(getResources().getColor(R.color.textColorSecondary));


                text1.setText(details.get(position).crashName);
                text2.setText("Status: " + details.get(position).crashStatus);
                return view;
            }
        };
        mListView.setAdapter(adapter);
    }

    private void displayError(VolleyError e) {
        Toast t = Toast.makeText(getApplicationContext(), getString(R.string.no_results), Toast.LENGTH_LONG);
        t.show();
    }

}
