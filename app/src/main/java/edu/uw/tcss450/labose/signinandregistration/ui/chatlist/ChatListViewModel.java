package edu.uw.tcss450.labose.signinandregistration.ui.chatlist;

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
 * The Chat List View Model class to handle the background.
 */
public class ChatListViewModel extends AndroidViewModel {

    // Live data object
    private final MutableLiveData<List<ChatModel>> mChatList;

    // The Contact Ids
    private final ArrayList<Integer> mChatIDs;

    /**
     * Constructor
     * @param application Application
     */
    public ChatListViewModel(final @NonNull Application application) {
        super(application);
        mChatList = new MutableLiveData<>(new ArrayList<>());
        mChatIDs = new ArrayList<>();
    }

    /**
     * Listens to any changes for contacts.
     * @param owner Owner object
     * @param observer Observer object
     */
    public void addChatListObserver(final @NonNull LifecycleOwner owner, final @NonNull Observer<? super List<ChatModel>> observer) {
        mChatList.observe(owner, observer);
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
        IntFunction<String> getString =
                getApplication().getResources()::getString;

        try {
            if (result.has(getString.apply(R.string.keys_json_chat_rowcount))
                    && result.has(getString.apply(R.string.keys_json_chat_rows))) {
                JSONArray rows =
                        result.getJSONArray(getString.apply(R.string.keys_json_chat_rows));
                for (int i = 0; i < rows.length(); i++) {
                    //successful response acquired.
                    JSONObject jsonChat = rows.getJSONObject(i);
                    //get relevant fields
                    int chatNumber = jsonChat.getInt(
                            getString.apply(R.string.keys_json_chat_id));
                    String chatName = jsonChat.getString(
                            getString.apply(R.string.keys_json_chat_name));
                    //create chat and add to list
                    ChatModel chat = new ChatModel(chatNumber, chatName);
                    if (!mChatIDs.contains(chat.getChatID())) {
                        Objects.requireNonNull(mChatList.getValue()).add(chat);
                        mChatIDs.add(chat.getChatID());
                    }
                }
            } else {
                Log.e("ERROR!", "No response");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }

        mChatList.setValue(mChatList.getValue());

    }

    /**
     * Connects and retrieve contact data
     * @param jwt Gets the JWT key
     */
    public void connectGet(final String jwt) {
        String url = "https://team-2-tcss450-server-m-c.herokuapp.com/chats";

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