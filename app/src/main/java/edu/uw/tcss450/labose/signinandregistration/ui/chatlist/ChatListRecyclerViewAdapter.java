package edu.uw.tcss450.labose.signinandregistration.ui.chatlist;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.labose.signinandregistration.R;
import edu.uw.tcss450.labose.signinandregistration.databinding.ItemChatBinding;

/**
 * Chat List Recycler View Adapter class for displaying the chat rooms.
 */
public class ChatListRecyclerViewAdapter extends RecyclerView.Adapter<ChatListRecyclerViewAdapter.ChatListViewHolder> {

    //Store all chats to present
    private final List<ChatModel> mChats;

    /**
     * Constructor
     * @param items Contacts list
     */
    @SuppressLint("NotifyDataSetChanged")
    public ChatListRecyclerViewAdapter(List<ChatModel> items) {
        this.mChats = items;
        notifyDataSetChanged();
    }

    /**
     * When the view holder is created
     * @param parent Parent
     * @param viewType Type
     */
    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatListViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false));
    }

    /**
     * When the view is binded
     * @param holder Holder
     * @param position Position of the chat list
     */
    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        holder.setChat(mChats.get(position));
    }

    /**
     * Returns the amount of contacts
     */
    @Override
    public int getItemCount() {
        return mChats.size();
    }

    /**
     * Class for the chat list view holder
     */
    public static class ChatListViewHolder extends RecyclerView.ViewHolder {

        // View
        public final View mView;

        // Chat List binding
        public final ItemChatBinding binding;

        /**
         * Constructor
         * @param view The Fragment's view
         */
        public ChatListViewHolder(View view) {
            super(view);
            mView = view;
            binding = ItemChatBinding.bind(view);
        }

        /**
         * Setting the chat list.
         * @param chat The chat object
         */
        public void setChat(ChatModel chat) {
            binding.chatName.setText(chat.getChatName());
            binding.cardRoot.setOnClickListener(
                    Navigation.createNavigateOnClickListener(
                            ChatListFragmentDirections.actionFragmentChatlistToChat(chat.getChatID()))
            );
        }
    }
}