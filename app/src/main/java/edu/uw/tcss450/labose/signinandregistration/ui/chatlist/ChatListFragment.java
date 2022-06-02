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
import edu.uw.tcss450.labose.signinandregistration.databinding.FragmentChatBinding;
import edu.uw.tcss450.labose.signinandregistration.databinding.FragmentChatlistBinding;
import edu.uw.tcss450.labose.signinandregistration.databinding.FragmentContactsBinding;
import edu.uw.tcss450.labose.signinandregistration.model.UserViewModel;
import edu.uw.tcss450.labose.signinandregistration.ui.chatlist.createchatdialog.CreateChatDialogFragment;

public class ChatListFragment extends Fragment {

    private UserViewModel mUserModel;
    private ChatListViewModel mModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserViewModel.class);
        mModel = provider.get(ChatListViewModel.class);
        mModel.connectGet(mUserModel.getmJwt());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chatlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentChatlistBinding binding = FragmentChatlistBinding.bind(getView());

        final RecyclerView rv = binding.recyclerChatlist;

        ArrayList<ChatModel> arrayList = new ArrayList<ChatModel>();

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

            Log.e("Button", "Chat Button");
        });
    }
}