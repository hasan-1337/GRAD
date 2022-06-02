package edu.uw.tcss450.labose.signinandregistration.ui.chat;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.shape.CornerFamily;

import java.util.List;

import edu.uw.tcss450.labose.signinandregistration.R;
import edu.uw.tcss450.labose.signinandregistration.databinding.FragmentChatMessageBinding;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.MessageViewHolder> {

    private final List<ChatMessage> mMessages;
    private final String mEmail;

    public ChatRecyclerViewAdapter(List<ChatMessage> messages, String email) {
        this.mMessages = messages;
        mEmail = email;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(final @NonNull ViewGroup parent, final int viewType) {
        return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_chat_message, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(final @NonNull MessageViewHolder holder, final int position) {
        holder.setMessage(mMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final FragmentChatMessageBinding binding;

        public MessageViewHolder(final @NonNull View view) {
            super(view);
            mView = view;
            binding = FragmentChatMessageBinding.bind(view);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        void setMessage(final ChatMessage message) {
            final Resources res = mView.getContext().getResources();
            final MaterialCardView card = binding.cardRoot;

            int standard = (int) res.getDimension(R.dimen.chat_margin);
            int extended = (int) res.getDimension(R.dimen.chat_margin_sided);

            final String year = message.getTimeStamp().substring(2, 4);
            final String month = message.getTimeStamp().substring(5, 7);
            final String day = message.getTimeStamp().substring(8, 10);
            String time = message.getTimeStamp().substring(11, 16);

            if (mEmail.equals(message.getSender())) {
                //This message is from the user. Format it as such
                binding.textMessage.setText(time + " - " + month + "/" + day + "/" + year + "\n" + message.getMessage());
                final ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) card.getLayoutParams();
                //Set the left margin
                layoutParams.setMargins(extended, standard, standard, standard);
                // Set this View to the right (end) side
                ((FrameLayout.LayoutParams) card.getLayoutParams()).gravity = Gravity.END;

                card.setCardBackgroundColor(ColorUtils.setAlphaComponent(res.getColor(R.color.primaryColor, null), 16));
                binding.textMessage.setTextColor(res.getColor(R.color.primaryDarkColor, null));

                card.setStrokeWidth(standard / 5);
                card.setStrokeColor(ColorUtils.setAlphaComponent(res.getColor(R.color.primaryDarkColor, null), 200));

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
                binding.textMessage.setText(month + "/" + day + "/" + year + " - " + time + "" + "\n" + message.getSender() + ": " + message.getMessage());
                final ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) card.getLayoutParams();

                //Set the right margin
                layoutParams.setMargins(standard, standard, extended, standard);
                // Set this View to the left (start) side
                ((FrameLayout.LayoutParams) card.getLayoutParams()).gravity = Gravity.START;

                card.setCardBackgroundColor(ColorUtils.setAlphaComponent(res.getColor(R.color.secondaryDarkColor, null), 16));

                card.setStrokeWidth(standard / 5);
                card.setStrokeColor(ColorUtils.setAlphaComponent(res.getColor(R.color.secondaryDarkColor, null), 200));

                binding.textMessage.setTextColor(res.getColor(R.color.secondaryColor, null));

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
    }
}

