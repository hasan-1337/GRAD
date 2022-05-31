package edu.uw.tcss450.labose.signinandregistration.ui.contacts;

import android.app.Application;
import android.util.Base64;
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
import java.util.function.IntFunction;

import edu.uw.tcss450.labose.signinandregistration.R;


public class ContactsListViewModel extends AndroidViewModel {

//    FragmentContactsBinding mBinding;
//    ArrayList<ContactModel> arrayList = new ArrayList<ContactModel>();

    private MutableLiveData<List<ContactModel>> mContactList;

    public ContactsListViewModel(@NonNull Application application) {
        super(application);
        mContactList = new MutableLiveData<>(new ArrayList<>());
    }

    public void addContactListObserver(@NonNull LifecycleOwner owner,
                                       @NonNull Observer<? super List<ContactModel>> observer) {
        mContactList.observe(owner, observer);
    }

    public List<ContactModel> getContactModels() {
        return mContactList.getValue();
    }

    private void handleError(final VolleyError error) {
        Log.e("CONNECTION ERROR", error.getLocalizedMessage());
        throw new IllegalStateException(error.getMessage());
    }

    private void handleResult(final JSONObject result) {
        IntFunction<String> getString =
                getApplication().getResources()::getString;
        try {
            JSONObject response = result;
            if (response.has(getString.apply(R.string.keys_json_contact_response))) {
                JSONArray data = response.getJSONArray(
                        getString.apply(R.string.keys_json_contact_response));
                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonContact = data.getJSONObject(i);
                    ContactModel contact = new ContactModel(
                            jsonContact.getString(
                                    getString.apply(
                                            R.string.keys_json_contact_email)));
                    contact.setId(
                            jsonContact.getString(
                                    getString.apply(
                                            R.string.keys_json_contact_id)));
                    contact.setName(
                            jsonContact.getString(
                                    getString.apply(
                                            R.string.keys_json_contact_name)));
                    if (!mContactList.getValue().contains(contact)) {
                        mContactList.getValue().add(contact);
                    }
                }
            } else {
                Log.e("ERROR!", "No data array");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
//        for (int i = 0; i < 5; i++) {
//            ContactModel contact = new ContactModel("contact " + i);
//            mContactList.getValue().add(contact);
//            Log.e("for loop", mContactList.getValue().get(i).getEmail());
//        }
//
//        Log.e("test", "made it here 1");
//
//        Log.e("after for", mContactList.getValue().get(0).getEmail());

        mContactList.setValue(mContactList.getValue());

    }

    public void connectGet(final String jwt) {
        String url = "https://team-2-tcss450-server-m-c.herokuapp.com/contacts";

        Request request = new JsonObjectRequest(
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

//        handleResult(new JSONObject());
    }
}
