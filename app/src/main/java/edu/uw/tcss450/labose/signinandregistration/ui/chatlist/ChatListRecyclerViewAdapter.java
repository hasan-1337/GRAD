package edu.uw.tcss450.labose.signinandregistration.ui.chatlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.labose.signinandregistration.R;
import edu.uw.tcss450.labose.signinandregistration.databinding.ItemChatBinding;

public class ChatListRecyclerViewAdapter extends
        RecyclerView.Adapter<ChatListRecyclerViewAdapter.ChatListViewHolder> {
    //Store all chats to present
    private final List<ChatModel> mChats;

    public ChatListRecyclerViewAdapter(List<ChatModel> items) {
        this.mChats = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatListViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        holder.setChat(mChats.get(position));
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public class ChatListViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ItemChatBinding binding;
        private ChatModel mChat;

        public ChatListViewHolder(View view) {
            super(view);
            mView = view;
            binding = ItemChatBinding.bind(view);
        }

        public void setChat(ChatModel chat) {
            mChat = chat;
            binding.chatName.setText(chat.getChatName());
            binding.cardRoot.setOnClickListener(
                    Navigation.createNavigateOnClickListener(
                            ChatListFragmentDirections.actionFragmentChatlistToChat(chat.getChatID()))
            );
        }
    }
}
