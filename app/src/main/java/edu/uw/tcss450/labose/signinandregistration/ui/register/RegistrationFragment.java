package edu.uw.tcss450.labose.signinandregistration.ui.register;

import static edu.uw.tcss450.labose.signinandregistration.util.PasswordValidator.*;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.labose.signinandregistration.databinding.FragmentRegistrationBinding;
import edu.uw.tcss450.labose.signinandregistration.util.PasswordValidator;

/**
 * A simple {@link Fragment} subclass, for registering new users.
 */
public class RegistrationFragment extends Fragment {

    //Binding and ViewModel fields
    private FragmentRegistrationBinding binding;
    private RegistrationViewModel mRegisterModel;

    //Password validation fields
    private PasswordValidator mNameValidator = checkPwdLength(1);
    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));
    private PasswordValidator mPassWordValidator =
            checkClientPredicate(pwd -> pwd.equals(binding.editPassword2.getText().toString()))
                    .and(checkPwdLength(7))
                    .and(checkPwdSpecialChar())
                    .and(checkExcludeWhiteSpace())
                    .and(checkPwdDigit())
                    .and(checkPwdLowerCase().or(checkPwdUpperCase()));

    /**
     * Required empty public constructor for the class.
     */
    public RegistrationFragment() {
        // Required empty public constructor
    }

    /**
     * onCreate method for the fragment.
     *
     * @param savedInstanceState Instance state to restore.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegisterModel = new ViewModelProvider(getActivity())
                .get(RegistrationViewModel.class);
    }

    /**
     * onCreateView method for the fragment.
     *
     * @param inflater LayoutInflater for the fragment.
     * @param container ViewGroup for the fragment.
     * @param savedInstanceState Instance state to restore.
     * @return The fragment's view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegistrationBinding.inflate(inflater);
        return binding.getRoot();
    }

    /**
     * onViewCreated method for the fragment.
     * @param view The fragment's view.
     * @param savedInstanceState Instance state to restore.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonRegister.setOnClickListener(this::attemptRegister);
        mRegisterModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);
    }

    /**
     * Attempts to register the account.
     * @param button The registration button.
     */
    private void attemptRegister(final View button) {
        validateFirst();
    }

    /**
     * Validates the new user's First Name.
     */
    private void validateFirst() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editFirst.getText().toString().trim()),
                this::validateLast,
                result -> binding.editFirst.setError("Please enter a first name."));
    }

    /**
     * Validates the new user's Last Name.
     */
    private void validateLast() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editLast.getText().toString().trim()),
                this::validateEmail,
                result -> binding.editLast.setError("Please enter a last name."));
    }

    /**
     * Validates the new user's Email.
     */
    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.editEmail.getText().toString().trim()),
                this::validatePasswordsMatch,
                result -> binding.editEmail.setError("Please enter a valid Email address."));
    }

    /**
     * Validates that the new user's passwords match.
     */
    private void validatePasswordsMatch() {
        PasswordValidator matchValidator =
                checkClientPredicate(
                        pwd -> pwd.equals(binding.editPassword2.getText().toString().trim()));

        mEmailValidator.processResult(
                matchValidator.apply(binding.editPassword1.getText().toString().trim()),
                this::validatePassword,
                result -> binding.editPassword1.setError("Passwords must match."));
    }

    /**
     * Validates the new user's password.
     */
    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.editPassword1.getText().toString()),
                this::verifyAuthWithServer,
                result -> binding.editPassword1.setError("Please enter a valid Password."));
    }

    /**
     * Asynchronous call that sends the registration info to the server via {@link RegistrationViewModel}.
     */
    private void verifyAuthWithServer() {
        mRegisterModel.connect(
                binding.editFirst.getText().toString(),
                binding.editLast.getText().toString(),
                binding.editEmail.getText().toString(),
                binding.editPassword1.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().

    }

    /**
     * Navigates to the login screen upon successful registration of a new user with the server.
     */
    private void navigateToLogin() {
        edu.uw.tcss450.labose.signinandregistration.ui.register.RegistrationFragmentDirections.ActionRegistrationToFragmentSign directions =
                edu.uw.tcss450.labose.signinandregistration.ui.register.RegistrationFragmentDirections.actionRegistrationToFragmentSign();

        directions.setEmail(binding.editEmail.getText().toString());
        directions.setPassword(binding.editPassword1.getText().toString());

        Navigation.findNavController(getView()).navigate(directions);
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to RegistrationViewModel.
     *
     * @param response the Response from the server
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.editEmail.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                navigateToLogin();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }

    }
}