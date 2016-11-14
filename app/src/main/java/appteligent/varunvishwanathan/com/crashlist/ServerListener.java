package appteligent.varunvishwanathan.com.crashlist;

import com.android.volley.VolleyError;

import java.util.ArrayList;

public interface ServerListener {
    public void onSuccess(ArrayList<CrashDetail> details);
    public void onError(VolleyError e);
}
