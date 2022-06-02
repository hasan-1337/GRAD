package edu.uw.tcss450.labose.signinandregistration.ui.contacts.contactsdialogs;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddContactDialogViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mResponse;

    public AddContactDialogViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

    private void handleError(final VolleyError error) {
        Log.e("CONNECTION ERROR", error.getLocalizedMessage());
        throw new IllegalStateException(error.getMessage());
    }

    public void connectPost(final String jwt, final String contactID, final String contactFName,
                            final String contactLName, final String contactEmail,
                            final String chatID) {
        String url = "https://team-2-tcss450-server-m-c.herokuapp.com/contactssql";

        JSONObject body = new JSONObject();

        try {
            body.put("contactid", Integer.parseInt(contactID));
            body.put("firstname", contactFName);
            body.put("lastname", contactLName);
            body.put("email", contactEmail);
            body.put("chatId", Integer.parseInt(chatID));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                mResponse::setValue,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

}
