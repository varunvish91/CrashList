package appteligent.varunvishwanathan.com.crashlist;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
@RunWith(AndroidJUnit4.class)

public class CrashActivityTest {
    private static String CRITTERCISM_LOGS = "https://developers.crittercism.com/v1.0/app/519d53101386202089000007/crash/summaries";
    private static String KEY = "5a839540a09f12373e52c7c82680318e";
    private static String TAG = "CrashActivityTest";
    private CountDownLatch lock = new CountDownLatch(1);

    @Rule
    public ActivityTestRule<DisplayCrashActivity> mActivityRule =
            new ActivityTestRule<>(DisplayCrashActivity.class);

    @Before
    public void testWifiIsOn() {
        ConnectivityManager connManager = (ConnectivityManager) mActivityRule.getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        Assert.assertTrue(wifi.isConnected());
    }
    @Test
    public void testSuccessfulConnection() throws Exception {
        Context context = mActivityRule.getActivity().getApplicationContext();

        ServerConnection mConn = new ServerConnection(context, CRITTERCISM_LOGS, KEY, 2000, new ServerListener() {
            @Override
            public void onSuccess(ArrayList<CrashDetail> details) {
                // Assert that we are here
                ListView l = (ListView) mActivityRule.getActivity().findViewById(R.id.list);
                ListAdapter adapter = l.getAdapter();
                Assert.assertTrue(true);

                // Assert that the correct UI is in place and that the listview has contents
                Assert.assertNotNull(l);

                // Assert that the adapter is not null
                Assert.assertNotNull(adapter);
            }

            @Override
            public void onError(VolleyError e) {
                // Assert that we are not here
                Log.d(TAG, "Network Response= " + e.networkResponse);
                Assert.assertTrue(false);

            }
        });
        mConn.makeRequest();
        lock.await(3000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testConnectionTimeout() throws Exception {
        // Test connection timeout by setting the connection timeout to a fraction of a second
        Context context = mActivityRule.getActivity().getApplicationContext();
        ServerConnection mConn = new ServerConnection(context, CRITTERCISM_LOGS, KEY, 3, new ServerListener() {
            @Override
            public void onSuccess(ArrayList<CrashDetail> details) {
                Assert.assertTrue(false);
            }

            @Override
            public void onError(VolleyError e) {
                Log.d(TAG, "NetworkResponse= " + e.networkResponse);
                Assert.assertTrue(e instanceof TimeoutError);

            }
        });
        mConn.makeRequest();
        lock.await(1000, TimeUnit.MILLISECONDS);

    }

    @Test
    public void testDummyUrl() throws Exception {
        Context context = mActivityRule.getActivity().getApplicationContext();
        ServerConnection mConn = new ServerConnection(context, "www.google.com", KEY, 2000, new ServerListener() {
            @Override
            public void onSuccess(ArrayList<CrashDetail> details) {
                Assert.assertTrue(false);
            }

            @Override
            public void onError(VolleyError e) {
                // Assert that we have an error
                Assert.assertTrue(true);
            }
        });
        mConn.makeRequest();
        lock.await(2000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testDummyKey() throws Exception {
        Context context = mActivityRule.getActivity().getApplicationContext();
        ServerConnection mConn = new ServerConnection(context, CRITTERCISM_LOGS, "asd", 2000, new ServerListener() {
            @Override
            public void onSuccess(ArrayList<CrashDetail> details) {
                Assert.assertTrue(false);
            }

            @Override
            public void onError(VolleyError e) {
                // Assert that we are here
                Log.d(TAG, "" + e.networkResponse);
                Assert.assertTrue(true);
            }
        });
        mConn.makeRequest();
        lock.await(2000, TimeUnit.MILLISECONDS);
    }
}

