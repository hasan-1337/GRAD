package edu.uw.tcss450.labose.signinandregistration.ui.chatlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.labose.signinandregistration.R;
import edu.uw.tcss450.labose.signinandregistration.databinding.ItemContactBinding;

public class ChatListRecyclerViewAdapter { //extends RecyclerView.Adapter<ChatListRecyclerViewAdapter.ContactViewHolder>{
//    //Store all contacts to present
//    private final List<ChatModel> mContacts;
//
//    public ChatListRecyclerViewAdapter(List<ChatModel> items) {
//        this.mContacts = items;
//        notifyDataSetChanged();
//    }
//
//    @NonNull
//    @Override
//    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new ContactViewHolder(LayoutInflater
//                .from(parent.getContext())
//                .inflate(R.layout.item_contact, parent, false));
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
//        holder.setContact(mContacts.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return mContacts.size();
//    }
//
//    public class ContactViewHolder extends RecyclerView.ViewHolder {
//        public final View mView;
//        public final ItemContactBinding binding;
//        private ChatModel mContact;
//
//        public ContactViewHolder(View view) {
//            super(view);
//            mView = view;
//            binding = ItemContactBinding.bind(view);
//        }
//
//        public void setContact(ChatModel contact) {
//            mContact = contact;
//            binding.contactName.setText(contact.getEmail());
//        }
//    }
}
