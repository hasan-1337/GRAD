package edu.uw.tcss450.labose.signinandregistration.ui.chatlist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.uw.tcss450.labose.signinandregistration.R;
import edu.uw.tcss450.labose.signinandregistration.databinding.FragmentChatlistBinding;
import edu.uw.tcss450.labose.signinandregistration.model.UserViewModel;
import edu.uw.tcss450.labose.signinandregistration.ui.chatlist.chatlistdialogs.CreateChatDialogFragment;
import edu.uw.tcss450.labose.signinandregistration.ui.chatlist.chatlistdialogs.RemoveChatDialogFragment;

/**
 * Chat List Fragment to allow the creation of chat rooms.
 */
public class ChatListFragment extends Fragment {

    // Fragment's object
    private ChatListViewModel mModel;

    /**
     * When the fragment is created.
     * @param savedInstanceState Save object.
     */
    @Override
    public void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ViewModelProvider provider = new ViewModelProvider(getActivity());
        final UserViewModel mUserModel = provider.get(UserViewModel.class);
        mModel = provider.get(ChatListViewModel.class);
        mModel.connectGet(mUserModel.getmJwt());
    }

    /**
     * When the fragment is in the process of displaying.
     * @param inflater The layout object
     * @param container The View group object
     * @param savedInstanceState Save Object.
     */
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chatlist, container, false);
    }

    /**
     * When the fragment is displayed.
     * @param view The layout view.
     * @param savedInstanceState Save Object.
     */
    @Override
    public void onViewCreated(final @NonNull View view, final @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentChatlistBinding binding = FragmentChatlistBinding.bind(getView());

        final RecyclerView rv = binding.recyclerChatlist;
        final ArrayList<ChatModel> arrayList = new ArrayList<>();

        // Set the adapter to hold a ref to the list.
        rv.setAdapter(new ChatListRecyclerViewAdapter(arrayList));
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));

        mModel.addChatListObserver(getViewLifecycleOwner(), chatList -> {
            if(!chatList.isEmpty()) {
                binding.recyclerChatlist.setAdapter(
                        new ChatListRecyclerViewAdapter(chatList)
                );
            }
        });

        //@Override
        binding.chatAdd.setOnClickListener(v -> {

            new CreateChatDialogFragment().show(
                    getChildFragmentManager(), CreateChatDialogFragment.TAG
            );

            Log.e("Button", "Add Chat Button");
        });

        binding.chatRemove.setOnClickListener(v -> {

            new RemoveChatDialogFragment().show(
                    getChildFragmentManager(), RemoveChatDialogFragment.TAG
            );

            Log.e("Button", "Remove Chat Button");
        });
    }
}