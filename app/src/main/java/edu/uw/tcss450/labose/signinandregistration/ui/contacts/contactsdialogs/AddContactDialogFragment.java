package edu.uw.tcss450.labose.signinandregistration.ui.contacts.contactsdialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import edu.uw.tcss450.labose.signinandregistration.R;
import edu.uw.tcss450.labose.signinandregistration.databinding.FragmentAddContactDialogBinding;
import edu.uw.tcss450.labose.signinandregistration.model.UserViewModel;

public class AddContactDialogFragment extends DialogFragment {

    public static final String TAG = "AddContactDialog";
    AddContactDialogViewModel mModel;
    UserViewModel mUserModel;
    FragmentAddContactDialogBinding mBinding;

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserViewModel.class);
        mModel = provider.get(AddContactDialogViewModel.class);


        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mBinding = FragmentAddContactDialogBinding.inflate(inflater);

        builder.setView(mBinding.getRoot().getRootView());
        builder.setPositiveButton(R.string.confirm_new_chat, (dialog, id) -> mModel.connectPost(mUserModel.getmJwt(),
                mBinding.newContactFirstname.getText().toString(),
                mBinding.newContactLastname.getText().toString(),
                mBinding.newContactUser.getText().toString(),
                mBinding.newContactEmail.getText().toString()));
        builder.setNegativeButton(R.string.cancel,
                (dialog, id) ->
                        Objects.requireNonNull(AddContactDialogFragment.this.getDialog()).cancel());

        return builder.create();
    }
}
