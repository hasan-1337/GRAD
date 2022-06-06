package edu.uw.tcss450.labose.signinandregistration.ui.contacts;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.labose.signinandregistration.R;
import edu.uw.tcss450.labose.signinandregistration.databinding.ItemContactBinding;

/**
 * Contacts Recycler View Adapter class for displaying the contacts.
 */
public class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<ContactsRecyclerViewAdapter.ContactViewHolder>{

    //Store all contacts to present
    private final List<ContactModel> mContacts;

    /**
     * Constructor
     * @param items Contacts list
     */
    @SuppressLint("NotifyDataSetChanged")
    public ContactsRecyclerViewAdapter(List<ContactModel> items) {
        this.mContacts = items;
        notifyDataSetChanged();
    }

    /**
     * When the view holder is created
     * @param parent Parent
     * @param viewType Type
     */
    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false));
    }

    /**
     * When the view is binded
     * @param holder Holder
     * @param position Position of the contacts
     */
    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.setContact(mContacts.get(position));
    }

    /**
     * Returns the amount of contacts
     */
    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    /**
     * Class for the contacts view holder
     */
    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        // View
        public final View mView;

        // Contact binding
        public final ItemContactBinding binding;

        /**
         * Constructor
         * @param view The Fragment's view
         */
        public ContactViewHolder(View view) {
            super(view);
            mView = view;
            binding = ItemContactBinding.bind(view);
        }

        /**
         * Setting the contact.
         * @param contact The contact object
         */
        public void setContact(ContactModel contact) {
            // Contact model class
            binding.contactName.setText(contact.getName());
        }
    }
}
