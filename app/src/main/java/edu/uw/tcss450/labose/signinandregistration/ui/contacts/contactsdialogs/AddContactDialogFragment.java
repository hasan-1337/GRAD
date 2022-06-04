package edu.uw.tcss450.labose.signinandregistration.ui.contacts.contactsdialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.uw.tcss450.labose.signinandregistration.R;
import edu.uw.tcss450.labose.signinandregistration.databinding.FragmentAddContactDialogBinding;
import edu.uw.tcss450.labose.signinandregistration.model.UserViewModel;

public class AddContactDialogFragment extends DialogFragment {

    public static final String TAG = "AddContactDialog";
    AddContactDialogViewModel mModel;
    UserViewModel mUserModel;
    FragmentAddContactDialogBinding mBinding;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserViewModel.class);
        mModel = provider.get(AddContactDialogViewModel.class);


        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mBinding = FragmentAddContactDialogBinding.inflate(inflater);

        builder.setView(inflater.inflate(R.layout.fragment_add_contact_dialog, mBinding.getRoot()));
        builder.setPositiveButton(R.string.confirm_new_chat, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                mModel.connectPost(mUserModel.getmJwt(),
                        mBinding.newContactId.getText().toString(),
                        mBinding.newContactFirstname.getText().toString(),
                        mBinding.newContactLastname.getText().toString(),
                        mBinding.newContactEmail.getText().toString(),
                        mBinding.newContactUserId.getText().toString());
            }
        });
        builder.setNegativeButton(R.string.cancel,
                (dialog, id) ->
                        edu.uw.tcss450.labose.signinandregistration.ui.contacts.contactsdialogs
                                .AddContactDialogFragment.this.getDialog().cancel());

        return builder.create();
    }
}
