package edu.uw.tcss450.labose.signinandregistration.ui.chat;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.labose.signinandregistration.R;
import edu.uw.tcss450.labose.signinandregistration.io.RequestQueueSingleton;

/**
 * The Chat Send View Model class to handle the background.
 */
public class ChatSendViewModel extends AndroidViewModel {

    // Live data object
    private final MutableLiveData<JSONObject> mResponse;

    /**
     * Constructor
     * @param application Application
     */
    public ChatSendViewModel(final @NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

    /**
     * Listens to any changes for chat.
     * @param owner Owner object
     * @param observer Observer object
     */
    public void addResponseObserver(final @NonNull LifecycleOwner owner, final @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }

    /**
     * Send the chat message
     * @param chatId The Chat's ID
     * @param jwt The JWT key
     * @param message The text message
     */
    public void sendMessage(final int chatId, final String jwt, final String message) {
        final String url = getApplication().getResources().getString(R.string.base_url_service) + "messages";
        final JSONObject body = new JSONObject();

        try {
            body.put("message", message);
            body.put("chatId", chatId);
        } catch (final JSONException e) {
            e.printStackTrace();
        }

        final Request<JSONObject> request = new JsonObjectRequest(Request.Method.POST, url, body, mResponse::setValue, this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", jwt);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10_000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext()).addToRequestQueue(request);
    }

    /**
     * Handles the errors
     * @param error Error handler
     */
    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            Log.e("NETWORK ERROR", error.getMessage());
        } else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            Log.e("CLIENT ERROR", error.networkResponse.statusCode + " " + data);
        }
    }
}

