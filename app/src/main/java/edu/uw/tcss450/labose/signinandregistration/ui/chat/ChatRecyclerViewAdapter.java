package edu.uw.tcss450.labose.signinandregistration.ui.chat;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.shape.CornerFamily;

import java.util.List;
import java.util.Objects;

import edu.uw.tcss450.labose.signinandregistration.R;
import edu.uw.tcss450.labose.signinandregistration.databinding.FragmentChatMessageBinding;

/**
 * Chat Recycler View Adapter class for displaying the chats.
 */
public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.MessageViewHolder> {

    // List of messages object
    private final List<ChatMessage> mMessages;

    // User's email
    private final String mEmail;

    /**
     * Constructor
     * @param messages The messages object
     * @param email The email given
     */
    public ChatRecyclerViewAdapter(List<ChatMessage> messages, String email) {
        this.mMessages = messages;
        mEmail = email;
    }

    /**
     * When the view holder is created
     * @param parent Parent
     * @param viewType Type
     */
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(final @NonNull ViewGroup parent, final int viewType) {
        return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_chat_message, parent, false));
    }

    /**
     * When the view is binded
     * @param holder Holder
     * @param position Position of the chat list
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(final @NonNull MessageViewHolder holder, final int position) {
        holder.setMessage(mMessages.get(position));
    }

    /**
     * Returns the amount of contacts
     */
    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    /**
     * Class for the message view holder
     */
    class MessageViewHolder extends RecyclerView.ViewHolder {

        // View
        private final View mView;

        // Chat binding
        private final FragmentChatMessageBinding binding;

        /**
         * Constructor
         * @param view The Fragment's view
         */
        public MessageViewHolder(final @NonNull View view) {
            super(view);
            mView = view;
            binding = FragmentChatMessageBinding.bind(view);
        }

        /**
         * Setting the chat message.
         * @param message The chat message object
         */
        @RequiresApi(api = Build.VERSION_CODES.O)
        @SuppressLint({"SetTextI18n", "ResourceAsColor"})
        void setMessage(final ChatMessage message) {
            final Resources res = mView.getContext().getResources();
            final MaterialCardView card = binding.cardRoot;

            int standard = (int) res.getDimension(R.dimen.chat_margin);
            int extended = (int) res.getDimension(R.dimen.chat_margin_sided);

            final String year = message.getTimeStamp().substring(2, 4);
            final String month = message.getTimeStamp().substring(5, 7);
            final String day = message.getTimeStamp().substring(8, 10);
            String time = message.getTimeStamp().substring(11, 16);
            time = month + "/" + day + "/" + year + " - " + time;

            if (mEmail.equals(message.getSender())) {
                //This message is from the user. Format it as such
                binding.textMessage.setText(time + "\n" + message.getMessage());
                customizeText(binding.textMessage, time, Color.RED);
                final ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) card.getLayoutParams();
                //Set the left margin
                layoutParams.setMargins(extended, standard, standard, standard);
                // Set this View to the right (end) side
                ((FrameLayout.LayoutParams) card.getLayoutParams()).gravity = Gravity.END;
                binding.textMessage.setTextColor(res.getColor(R.color.textColor, null));

                card.setCardBackgroundColor(res.getColor(R.color.backgroundColor, null));
                card.setStrokeWidth(standard / 5);
                card.setStrokeColor(ColorUtils.setAlphaComponent(res.getColor(R.color.textColor, null), 200));

                //Round the corners on the left side
                card.setShapeAppearanceModel(card.getShapeAppearanceModel()
                                .toBuilder()
                                .setTopLeftCorner(CornerFamily.ROUNDED,standard * 2)
                                .setBottomLeftCorner(CornerFamily.ROUNDED,standard * 2)
                                .setBottomRightCornerSize(0)
                                .setTopRightCornerSize(0)
                                .build());

            } else {
                //This message is from another user. Format it as such
                String user = message.getSender();

                for (int i = 0; i <= Objects.requireNonNull(user).length(); i++) {
                    if (user.charAt(i) == '@') {
                        user = user.substring(0, i);
                        user = user.substring(0, 1).toUpperCase() + user.substring(1);
                        break;
                    }
                }
                binding.textMessage.setText(time + "\n" + user + ": " + message.getMessage());
                customizeText(binding.textMessage, time, Color.RED);
                customizeText(binding.textMessage, user, Color.GREEN);
                final ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) card.getLayoutParams();

                //Set the right margin
                layoutParams.setMargins(standard, standard, extended, standard);
                // Set this View to the left (start) side
                ((FrameLayout.LayoutParams) card.getLayoutParams()).gravity = Gravity.START;

                card.setCardBackgroundColor(res.getColor(R.color.backgroundColor, null));
                card.setStrokeWidth(standard / 5);
                card.setStrokeColor(ColorUtils.setAlphaComponent(res.getColor(R.color.textColor, null), 200));

                binding.textMessage.setTextColor(res.getColor(R.color.textColor, null));

                //Round the corners on the right side
                card.setShapeAppearanceModel(card.getShapeAppearanceModel()
                                .toBuilder()
                                .setTopRightCorner(CornerFamily.ROUNDED,standard * 2)
                                .setBottomRightCorner(CornerFamily.ROUNDED,standard * 2)
                                .setBottomLeftCornerSize(0)
                                .setTopLeftCornerSize(0)
                                .build());
            }
            card.requestLayout();
        }

        /**
         * Customize a text in TextView
         * @param tv TextView or Edittext or Button or child of TextView class
         * @param textToChange Text to highlight
         * @param color Text color
         */
        private void customizeText(TextView tv, String textToChange, int color) {
            String tvt = tv.getText().toString();
            int ofe = tvt.indexOf(textToChange);
            SpannableString wordToSpan = new SpannableString(tv.getText());

            for (int ofs = 0; ofs < tvt.length() && ofe != -1; ofs = ofe + 1) {
                ofe = tvt.indexOf(textToChange, ofs);
                if (ofe == -1) {
                    break;
                } else {
                    wordToSpan.setSpan(new ForegroundColorSpan(color), ofe, ofe + textToChange.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv.setText(wordToSpan, TextView.BufferType.SPANNABLE);
                }
            }
        }
    }
}

