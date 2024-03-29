package edu.uw.tcss450.labose.signinandregistration.ui.chat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import edu.uw.tcss450.labose.signinandregistration.R;
import edu.uw.tcss450.labose.signinandregistration.databinding.FragmentChatBinding;
import edu.uw.tcss450.labose.signinandregistration.model.UserViewModel;
import edu.uw.tcss450.labose.signinandregistration.ui.chat.chatdialogs.AddChatMemberDialogFragment;
import edu.uw.tcss450.labose.signinandregistration.ui.chat.chatdialogs.RemoveChatMemberDialogFragment;

/**
 * Chat Fragment to allow the creation of chatting.
 */
public class ChatFragment extends Fragment {

    // Room ID
    private int mChatID;

    // Chat Model class
    private ChatViewModel mChatModel;

    // User View Model class
    private UserViewModel mUserModel;

    // Chat Sending Class
    private ChatSendViewModel mSendModel;

    /**
     * Constructor
     */
    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * When the fragment is created.
     * @param savedInstanceState Save object.
     */
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

    /**
     * When the fragment is in the process of displaying.
     * @param inflater The layout object
     * @param container The View group object
     * @param savedInstanceState Save Object.
     */
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    /**
     * When the fragment is displayed.
     * @param view The layout view.
     * @param savedInstanceState Save Object.
     */
    @SuppressLint("NotifyDataSetChanged")
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
                    Objects.requireNonNull(rv.getAdapter()).notifyDataSetChanged();
                    rv.scrollToPosition(rv.getAdapter().getItemCount() - 1);
                    binding.swipeContainer.setRefreshing(false);
                    //mChatModel.getNextMessages(mChatID, mUserModel.getmJwt());
                });
        //Send button was clicked. Send the message via the SendViewModel
        binding.buttonSend.setOnClickListener(button -> mSendModel.sendMessage(mChatID,
                mUserModel.getmJwt(),
                binding.editMessage.getText().toString()));
        //when we get the response back from the server, clear the edittext
        mSendModel.addResponseObserver(getViewLifecycleOwner(), response -> binding.editMessage.setText(""));
        mSendModel.addResponseObserver(getViewLifecycleOwner(), response -> mChatModel.getNextMessages(mChatID, mUserModel.getmJwt()));

        binding.chatAddContacts.setOnClickListener(v -> {

            new AddChatMemberDialogFragment().show(
                    getChildFragmentManager(), AddChatMemberDialogFragment.TAG
            );

            Log.e("Button", "Add Chat Member Button");
        });

        binding.chatRemoveContacts.setOnClickListener(v -> {

            new RemoveChatMemberDialogFragment().show(
                    getChildFragmentManager(), RemoveChatMemberDialogFragment.TAG
            );

            Log.e("Button", "Remove Chat Member Button");
        });
    }
}