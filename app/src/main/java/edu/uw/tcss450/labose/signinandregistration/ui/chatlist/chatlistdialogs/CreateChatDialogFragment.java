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
import edu.uw.tcss450.labose.signinandregistration.model.UserViewModel;

public class CreateChatDialogFragment extends DialogFragment {

    public static final String TAG = "CreateChatDialog";
    CreateChatDialogViewModel mModel;
    UserViewModel mUserModel;
    FragmentCreateChatDialogBinding mBinding;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserViewModel.class);
        mModel = provider.get(CreateChatDialogViewModel.class);


        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mBinding = FragmentCreateChatDialogBinding.inflate(inflater);

        builder.setView(mBinding.getRoot().getRootView());
        builder.setPositiveButton(R.string.confirm_new_chat, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                mModel.connectPost(mUserModel.getmJwt(),
                        mBinding.newChatName.getText().toString(),
                        mBinding.newChatId.getText().toString());
            }
        });
        builder.setNegativeButton(R.string.cancel,
                (dialog, id) ->
                        CreateChatDialogFragment.this.getDialog().cancel());

        return builder.create();
    }
}
