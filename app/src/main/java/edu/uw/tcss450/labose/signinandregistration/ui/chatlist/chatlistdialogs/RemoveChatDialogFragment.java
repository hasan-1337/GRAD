package edu.uw.tcss450.labose.signinandregistration.ui.chatlist.chatlistdialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.uw.tcss450.labose.signinandregistration.R;
import edu.uw.tcss450.labose.signinandregistration.databinding.FragmentCreateChatDialogBinding;
import edu.uw.tcss450.labose.signinandregistration.databinding.FragmentRemoveChatDialogBinding;
import edu.uw.tcss450.labose.signinandregistration.model.UserViewModel;

public class RemoveChatDialogFragment extends DialogFragment {

    public static final String TAG = "RemoveChatDialog";
    RemoveChatDialogViewModel mModel;
    UserViewModel mUserModel;
    FragmentRemoveChatDialogBinding mBinding;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserViewModel.class);
        mModel = provider.get(RemoveChatDialogViewModel.class);


        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mBinding = FragmentRemoveChatDialogBinding.inflate(inflater);

        builder.setView(mBinding.getRoot().getRootView());
        builder.setPositiveButton(R.string.confirm_new_chat, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                mModel.connectDelete(mUserModel.getmJwt(),
                        mBinding.removeChatId.getText().toString());
            }
        });
        builder.setNegativeButton(R.string.cancel,
                (dialog, id) ->
                        RemoveChatDialogFragment.this.getDialog().cancel());

        return builder.create();
    }

}
