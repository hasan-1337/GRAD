package edu.uw.tcss450.labose.signinandregistration.ui.chatlist;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.labose.signinandregistration.databinding.FragmentChatlistBinding;


public class ChatListViewModel extends AndroidViewModel {

    FragmentChatlistBinding mBinding;
    ArrayList<ChatModel> arrayList = new ArrayList<ChatModel>();

    private MutableLiveData<List<ChatModel>> mChatList;

    public ChatListViewModel(@NonNull Application application) {
        super(application);
        mChatList = new MutableLiveData<>(new ArrayList<>());
    }

    public void addChatListObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super List<ChatModel>> observer) {
        mChatList.observe(owner, observer);
    }

    public List<ChatModel> getChatModels() {
        return mChatList.getValue();
    }

    private void handleError(final VolleyError error) {
        Log.e("CONNECTION ERROR", error.getLocalizedMessage());
        throw new IllegalStateException(error.getMessage());
    }

    private void handleResult(final JSONObject result) {
//        IntFunction<String> getString =
//                getApplication().getResources()::getString;
//        try {
//            JSONObject root = result;
//            if (root.has(getString.apply(R.string.keys_json_contact_response))) {
//                JSONObject response =
//                        root.getJSONObject(getString.apply(
//                                R.string.keys_json_contact_response));
//                if (response.has(getString.apply(R.string.keys_json_contact_data))) {
//                    JSONArray data = response.getJSONArray(
//                            getString.apply(R.string.keys_json_contact_data));
//                    for (int i = 0; i < data.length(); i++) {
//                        JSONObject jsonContact = data.getJSONObject(i);
//                        ContactModel contact = new ContactModel();
//                        contact.setEmail(
//                                jsonContact.getString(
//                                        getString.apply(
//                                                R.string.keys_json_contact_name)));
//                        if (!mContactList.getValue().contains(contact)) {
//                            mContactList.getValue().add(contact);
//                        }
//                    }
//                } else {
//                    Log.e("ERROR!", "No data array");
//                }
//            } else {
//                Log.e("ERROR!", "No response");
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Log.e("ERROR!", e.getMessage());
//        }

        ChatModel chat = new ChatModel(1);
        mChatList.getValue().add(chat);

        mChatList.setValue(mChatList.getValue());

    }

    public void connectGet() {
        String url = "https://team-2-tcss450-webservice.herokuapp.com/auth";


//        Request request = new JsonObjectRequest(
//                Request.Method.POST,
//                url,
//                null, //no body
//                this::handleResult,
//                this::handleError);
//
//        request.setRetryPolicy(new DefaultRetryPolicy(
//                10_000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        //Instantiate the RequestQueue and add the request to the queue
//        Volley.newRequestQueue(getApplication().getApplicationContext())
//                .add(request);

        handleResult(new JSONObject());
    }
}
