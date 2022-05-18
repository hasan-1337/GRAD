package edu.uw.tcss450.labose.signinandregistration.ui.contacts;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

import edu.uw.tcss450.labose.signinandregistration.R;


public class ContactsViewModel extends AndroidViewModel {

//    FragmentContactsBinding mBinding;
//    ArrayList<ContactModel> arrayList = new ArrayList<ContactModel>();

    private MutableLiveData<List<ContactModel>> mContactList;

    public ContactsViewModel(@NonNull Application application) {
        super(application);
        mContactList = new MutableLiveData<>();
        mContactList.setValue(new ArrayList<ContactModel>());
    }

    public void addContactListObserver(@NonNull LifecycleOwner owner,
                                       @NonNull Observer<? super List<ContactModel>> observer) {
        mContactList.observe(owner, observer);
    }

    private void handleError(final VolleyError error) {
        Log.e("CONNECTION ERROR", error.getLocalizedMessage());
        throw new IllegalStateException(error.getMessage());
    }

    private void handleResult(final JSONObject result) {
        IntFunction<String> getString =
                getApplication().getResources()::getString;
        try {
            JSONObject root = result;
            if (root.has(getString.apply(R.string.keys_json_contact_response))) {
                JSONObject response =
                        root.getJSONObject(getString.apply(
                                R.string.keys_json_contact_response));
                if (response.has(getString.apply(R.string.keys_json_contact_data))) {
                    JSONArray data = response.getJSONArray(
                            getString.apply(R.string.keys_json_contact_data));
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonContact = data.getJSONObject(i);
                        ContactModel contact = new ContactModel();
                        contact.setEmail(
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
            } else {
                Log.e("ERROR!", "No response");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }

        mContactList.setValue(mContactList.getValue());
    }

    public void connectGet() {
        // TODO: Copy this over from another file thats working
    }
}
