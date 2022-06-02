package edu.uw.tcss450.labose.signinandregistration.ui.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import edu.uw.tcss450.labose.signinandregistration.MainActivityArgs;
import edu.uw.tcss450.labose.signinandregistration.R;
import edu.uw.tcss450.labose.signinandregistration.databinding.FragmentChatBinding;
import edu.uw.tcss450.labose.signinandregistration.model.UserViewModel;
import edu.uw.tcss450.labose.signinandregistration.ui.chat.chatdialogs.AddChatMemberDialogFragment;
import edu.uw.tcss450.labose.signinandregistration.ui.chatlist.chatlistdialogs.CreateChatDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    //The chat ID for "global" chat
//    private static final int HARD_CODED_CHAT_ID = 1;

    private int mChatID;
    private ChatViewModel mChatModel;
    private UserViewModel mUserModel;
    private ChatSendViewModel mSendModel;


    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ChatFragmentArgs args = ChatFragmentArgs.fromBundle(getArguments());
        ViewModelProvider provider = new ViewModelProvider(getActivity());

        mChatID = args.getChatId();
        mUserModel = provider.get(UserViewModel.class);
        mChatModel = provider.get(ChatViewModel.class);
        mChatModel.getFirstMessages(mChatID, mUserModel.getmJwt());
        mSendModel = provider.get(ChatSendViewModel.class);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(final @NonNull View view, final @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final FragmentChatBinding binding = FragmentChatBinding.bind(getView());

        //SetRefreshing shows the internal Swiper view progress bar. Show this until messages load
        binding.swipeContainer.setRefreshing(true);

        final RecyclerView rv = binding.recyclerMessages;
        //Set the Adapter to hold a reference to the list FOR THIS chat ID that the ViewModel
        //holds.
        rv.setAdapter(new ChatRecyclerViewAdapter(mChatModel.getMessageListByChatId(mChatID), mUserModel.getEmail()));

        //When the user scrolls to the top of the RV, the swiper list will "refresh"
        //The user is out of messages, go out to the service and get more
        binding.swipeContainer.setOnRefreshListener(() -> mChatModel.getNextMessages(mChatID, mUserModel.getmJwt()));

        mChatModel.addMessageObserver(mChatID, getViewLifecycleOwner(), list -> {
                    /*
                     * This solution needs work on the scroll position. As a group,
                     * you will need to come up with some solution to manage the
                     * recyclerview scroll position. You also should consider a
                     * solution for when the keyboard is on the screen.
                     */
                    //inform the RV that the underlying list has (possibly) changed
                    rv.getAdapter().notifyDataSetChanged();
                    rv.scrollToPosition(rv.getAdapter().getItemCount() - 1);
                    binding.swipeContainer.setRefreshing(false);
                });
        //Send button was clicked. Send the message via the SendViewModel
        binding.buttonSend.setOnClickListener(button -> mSendModel.sendMessage(mChatID, mUserModel.getmJwt(), binding.editMessage.getText().toString()));
//when we get the response back from the server, clear the edittext
        mSendModel.addResponseObserver(getViewLifecycleOwner(), response -> binding.editMessage.setText(""));

        binding.chatAddContacts.setOnClickListener(v -> {

            new AddChatMemberDialogFragment().show(
                    getChildFragmentManager(), AddChatMemberDialogFragment.TAG
            );

            Log.e("Button", "Chat Button");
        });
    }
}