package edu.uw.tcss450.labose.signinandregistration.ui.chatlist.createchatdialog;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntFunction;

import edu.uw.tcss450.labose.signinandregistration.R;
import edu.uw.tcss450.labose.signinandregistration.ui.chatlist.ChatModel;

public class CreateChatDialogViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mResponse;


    public CreateChatDialogViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

    private void handleError(final VolleyError error) {
        Log.e("CONNECTION ERROR", error.getLocalizedMessage());
        throw new IllegalStateException(error.getMessage());
    }
//
//    private void handleResult(final JSONObject result) {
//        IntFunction<String> getString =
//                getApplication().getResources()::getString;
//
//        try {
//            JSONObject root = result;
//            if (root.has(getString.apply(R.string.keys_json_chat_rowcount))
//                    && root.has(getString.apply(R.string.keys_json_chat_rows))) {
//                JSONArray rows =
//                        root.getJSONArray(getString.apply(R.string.keys_json_chat_rows));
//                for (int i = 0; i < rows.length(); i++) {
//                    //successful response acquired.
//                    JSONObject jsonChat = rows.getJSONObject(i);
//                    //get relevant fields
//                    int chatNumber = jsonChat.getInt(
//                            getString.apply(R.string.keys_json_chat_id));
//                    String chatName = jsonChat.getString(
//                            getString.apply(R.string.keys_json_chat_name));
//                    //create chat and add to list
//                    ChatModel chat = new ChatModel(chatNumber, chatName);
//                    if (!mChatIDs.contains(chat.getChatID())) {
//                        mChatList.getValue().add(chat);
//                        mChatIDs.add(chat.getChatID());
//                    }
//                }
//            } else {
//                Log.e("ERROR!", "No response");
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Log.e("ERROR!", e.getMessage());
//        }
//
//        mChatList.setValue(mChatList.getValue());
//
//    }
//
    public void connectPost(final String jwt, final String chatName, final String chatID) {
        String url = "https://team-2-tcss450-server-m-c.herokuapp.com/chats";

        JSONObject body = new JSONObject();

        String HARD_CODED_CHAT_NAME = "New Chat (7)";
        int HARD_CODED_CHAT_ID = 7;

        try {
            body.put("name", HARD_CODED_CHAT_NAME);
            body.put("chatID", HARD_CODED_CHAT_ID);
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
//        Log.e("CreateChatDialog", "connectPost");
    }

}
