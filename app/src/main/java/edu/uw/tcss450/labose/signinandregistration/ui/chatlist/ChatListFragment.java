package edu.uw.tcss450.labose.signinandregistration.ui.chatlist;

import android.os.Bundle;
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
import edu.uw.tcss450.labose.signinandregistration.databinding.FragmentContactsBinding;

public class ChatListFragment extends Fragment {

//    private ChatListViewModel mModel;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        mModel = new ViewModelProvider(getActivity()).get(ChatListViewModel.class);
//        mModel.connectGet();
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_contacts, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        FragmentContactsBinding binding = FragmentContactsBinding.bind(getView());
//
//        final RecyclerView rv = binding.recyclerContacts;
//
//        ArrayList<ChatModel> arrayList = new ArrayList<ChatModel>();
//
//        // Set the adapter to hold a ref to the list.
//        rv.setAdapter(new ChatListRecyclerViewAdapter(arrayList));
//        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
//
//        mModel.addContactListObserver(getViewLifecycleOwner(), contactList -> {
//            if(!contactList.isEmpty()) {
//                binding.recyclerContacts.setAdapter(
//                        new ChatListRecyclerViewAdapter(contactList)
//                );
//            }
//        });
//    }
}
