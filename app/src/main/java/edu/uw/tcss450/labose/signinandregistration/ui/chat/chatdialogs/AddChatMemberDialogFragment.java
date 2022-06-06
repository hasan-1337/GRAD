package edu.uw.tcss450.labose.signinandregistration.ui.chat.chatdialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.uw.tcss450.labose.signinandregistration.R;
import edu.uw.tcss450.labose.signinandregistration.databinding.FragmentAddChatMemberDialogBinding;
import edu.uw.tcss450.labose.signinandregistration.model.UserViewModel;

public class AddChatMemberDialogFragment extends DialogFragment {

    public static final String TAG = "AddChatMemberDialog";
    AddChatMemberDialogViewModel mModel;
    UserViewModel mUserModel;
    FragmentAddChatMemberDialogBinding mBinding;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserViewModel.class);
        mModel = provider.get(AddChatMemberDialogViewModel.class);


        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mBinding = FragmentAddChatMemberDialogBinding.inflate(inflater);

        builder.setView(mBinding.getRoot().getRootView());
        builder.setPositiveButton(R.string.confirm_new_chat, (dialog, id) -> mModel.connectPut(mUserModel.getmJwt(),
                mBinding.joinChatId.getText().toString()));
        builder.setNegativeButton(R.string.cancel,
                (dialog, id) ->
                        edu.uw.tcss450.labose.signinandregistration.ui.chat.chatdialogs
                                .AddChatMemberDialogFragment.this.getDialog().cancel());

        return builder.create();
    }
}
