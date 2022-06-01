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
import java.util.function.IntFunction;

import edu.uw.tcss450.labose.signinandregistration.R;


public class ChatListViewModel extends AndroidViewModel {

//    FragmentChatlistBinding mBinding;
//    ArrayList<ChatModel> arrayList = new ArrayList<ChatModel>();

    private MutableLiveData<List<ChatModel>> mChatList;
    private ArrayList<Integer> mChatIDs;

    public ChatListViewModel(@NonNull Application application) {
        super(application);
        mChatList = new MutableLiveData<>(new ArrayList<>());
        mChatIDs = new ArrayList<Integer>();
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
        IntFunction<String> getString =
                getApplication().getResources()::getString;

        try {
            JSONObject root = result;
            if (root.has(getString.apply(R.string.keys_json_chat_rowcount))
                    && root.has(getString.apply(R.string.keys_json_chat_rows))) { //TODO: make into string resources
//                int rowCount = Integer.parseInt(root.getJSONObject(getString.apply("rowCount")));
                JSONArray rows =
                        root.getJSONArray(getString.apply(R.string.keys_json_chat_rows));
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
                        mChatList.getValue().add(chat);
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

//        ChatModel chat = new ChatModel(1);
//        mChatList.getValue().add(chat);

        mChatList.setValue(mChatList.getValue());

    }

    public void connectGet(final String jwt) {
        String url = "https://team-2-tcss450-server-m-c.herokuapp.com/chats/chatrooms";

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

//        handleResult(new JSONObject());
    }
}
