package edu.uw.tcss450.labose.signinandregistration.ui.contacts;

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
import java.util.List;

import edu.uw.tcss450.labose.signinandregistration.R;
import edu.uw.tcss450.labose.signinandregistration.databinding.FragmentContactsBinding;
import edu.uw.tcss450.labose.signinandregistration.ui.chat.ChatRecyclerViewAdapter;

public class ContactsListFragment extends Fragment {

    private ContactsListViewModel mModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = new ViewModelProvider(getActivity()).get(ContactsListViewModel.class);
        mModel.connectGet();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentContactsBinding binding = FragmentContactsBinding.bind(getView());

        final RecyclerView rv = binding.recyclerContacts;

        ArrayList<ContactModel> arrayList = new ArrayList<ContactModel>();

//        for (int i = 0; i < 5; i++) {
//            ContactModel model = new ContactModel("Contact " + i);
//            arrayList.add(model);
//        }

        // Set the adapter to hold a ref to the list.
        rv.setAdapter(new ContactsRecyclerViewAdapter(arrayList));
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));

        mModel.addContactListObserver(getViewLifecycleOwner(), list -> {
            rv.getAdapter().notifyDataSetChanged();
            rv.scrollToPosition(rv.getAdapter().getItemCount() - 1);
        });

//        mModel.addContactListObserver(getViewLifecycleOwner(), contactList -> {
//            if(!contactList.isEmpty()) {
//                binding.recyclerContacts.setAdapter(
//                        new ContactsRecyclerViewAdapter(contactList)
//                );
//                binding.recyclerContacts.setVisibility(View.GONE);
//            }
//        });
    }
}