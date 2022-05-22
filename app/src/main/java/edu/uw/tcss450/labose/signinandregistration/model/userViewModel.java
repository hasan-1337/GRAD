package edu.uw.tcss450.labose.signinandregistration.model;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class userViewModel extends ViewModel {
    private final String mEmail;
    private final String mJwt;

    private userViewModel(String email, String jwt) {
        mEmail = email;
        mJwt = jwt;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getmJwt() {
        return mJwt;
    }

    public static class userViewModelFactory implements ViewModelProvider.Factory {

        private final String name;
        private final String jwt;

        public userViewModelFactory(String name, String jwt) {
            this.name = name;
            this.jwt = jwt;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == userViewModel.class) {
                return (T) new userViewModel(name, jwt);
            }
            throw new IllegalArgumentException(
                    "Argument must be: " + userViewModel.class);
        }
    }
}


