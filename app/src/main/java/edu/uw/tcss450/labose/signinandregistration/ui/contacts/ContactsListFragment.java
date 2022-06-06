package edu.uw.tcss450.labose.signinandregistration.ui.contacts;

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
import edu.uw.tcss450.labose.signinandregistration.databinding.FragmentContactsBinding;
import edu.uw.tcss450.labose.signinandregistration.model.UserViewModel;
import edu.uw.tcss450.labose.signinandregistration.ui.contacts.contactsdialogs.AddContactDialogFragment;
import edu.uw.tcss450.labose.signinandregistration.ui.contacts.contactsdialogs.RemoveContactDialogFragment;

/**
 * Contacts List Fragment to allow the creation of contacts.
 */
public class ContactsListFragment extends Fragment {

    // Fragment's object
    private ContactsListViewModel mModel;

    /**
     * When the fragment is created.
     * @param savedInstanceState Save object.
     */
    @Override
    public void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ViewModelProvider provider = new ViewModelProvider(getActivity());
        final UserViewModel mUserModel = provider.get(UserViewModel.class);
        mModel = provider.get(ContactsListViewModel.class);
        mModel.connectGet(mUserModel.getmJwt());
    }

    /**
     * When the fragment is in the process of displaying.
     * @param inflater The layout object
     * @param container The View group object
     * @param savedInstanceState Save Object.
     */
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    /**
     * When the fragment is displayed.
     * @param view The layout view.
     * @param savedInstanceState Save Object.
     */
    @Override
    public void onViewCreated(final @NonNull View view, final @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentContactsBinding binding = FragmentContactsBinding.bind(getView());

        final RecyclerView rv = binding.recyclerContactlist;

        ArrayList<ContactModel> arrayList = new ArrayList<>();

        // Set the adapter to hold a ref to the list.
        rv.setAdapter(new ContactsRecyclerViewAdapter(arrayList));
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));

        mModel.addContactListObserver(getViewLifecycleOwner(), contactList -> {
            if(!contactList.isEmpty()) {
                binding.recyclerContactlist.setAdapter(
                        new ContactsRecyclerViewAdapter(contactList)
                );
            }
        });

        //@Override
        binding.contactsAdd.setOnClickListener(v -> {

            new AddContactDialogFragment().show(
                    getChildFragmentManager(), AddContactDialogFragment.TAG
            );

            // Add a contact
            Log.e("Button", "Add Contacts Button");
        });

        binding.contactsRemove.setOnClickListener(v -> {

            new RemoveContactDialogFragment().show(
                    getChildFragmentManager(), RemoveContactDialogFragment.TAG
            );

            // Add a contact
            Log.e("Button", "Remove Contacts Button");
        });
    }
}