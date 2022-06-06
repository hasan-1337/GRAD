package edu.uw.tcss450.labose.signinandregistration.ui.contacts;

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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntFunction;

import edu.uw.tcss450.labose.signinandregistration.R;

/**
 * The Contact List View Model class to handle the background.
 */
public class ContactsListViewModel extends AndroidViewModel {

    // Live data object
    private final MutableLiveData<List<ContactModel>> mContactList;

    // The Contact Ids
    private final ArrayList<Integer> mContactIDs;

    /**
     * Constructor
     * @param application Application
     */
    public ContactsListViewModel(final @NonNull Application application) {
        super(application);
        mContactList = new MutableLiveData<>(new ArrayList<>());
        mContactIDs = new ArrayList<>();
    }

    /**
     * Listens to any changes for contacts.
     * @param owner Owner object
     * @param observer Observer object
     */
    public void addContactListObserver(final @NonNull LifecycleOwner owner, final @NonNull Observer<? super List<ContactModel>> observer) {
        mContactList.observe(owner, observer);
    }

    /**
     * Handles the errors
     * @param error Error handler
     */
    private void handleError(final VolleyError error) {
        Log.e("CONNECTION ERROR", error.getLocalizedMessage());
        throw new IllegalStateException(error.getMessage());
    }

    /**
     * Handles the result
     * @param result the result's object
     */
    private void handleResult(final JSONObject result) {
        final IntFunction<String> getString =
                getApplication().getResources()::getString;

        try {
            if (result.has(getString.apply(R.string.keys_json_chat_rows))) {
                JSONArray data = result.getJSONArray(
                        getString.apply(R.string.keys_json_chat_rows));
                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonContact = data.getJSONObject(i);
                    ContactModel contact = new ContactModel(
                            jsonContact.getString(
                                    getString.apply(
                                            R.string.keys_json_contact_email)));
                    contact.setId(
                            jsonContact.getInt(
                                    getString.apply(
                                            R.string.keys_json_contact_id)));
                    contact.setName(
                            jsonContact.getString(
                                    getString.apply(
                                            R.string.keys_json_contact_firstname)) + " " +
                            jsonContact.getString(
                                    getString.apply(
                                            R.string.keys_json_contact_lastname)));

                    if (!mContactIDs.contains(contact.getId())) {
                        Objects.requireNonNull(mContactList.getValue()).add(contact);
                        mContactIDs.add(contact.getId());
                    }
                }
            } else {
                Log.e("ERROR!", "No data array");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }

        mContactList.setValue(mContactList.getValue());
    }

    /**
     * Connects and retrieve contact data
     * @param jwt Gets the JWT key
     */
    public void connectGet(final String jwt) {
        final String url = "https://team-2-tcss450-server-m-c.herokuapp.com/contacts";

        final Request<JSONObject> request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handleResult,
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