package edu.uw.tcss450.labose.signinandregistration.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

/**
 * New Message count View model for display icon notification
 */
public class NewMessageCountViewModel extends ViewModel {

    // Live Data object
    private final MutableLiveData<Integer> mNewMessageCount;

    /**
     * Constructor
     */
    public NewMessageCountViewModel() {
        mNewMessageCount = new MutableLiveData<>();
        mNewMessageCount.setValue(0);
    }

    /**
     * Listens to any changes for chat.
     * @param owner Owner object
     * @param observer Observer object
     */
    public void addMessageCountObserver(final @NonNull LifecycleOwner owner,
                                        final @NonNull Observer<? super Integer> observer) {
        mNewMessageCount.observe(owner, observer);
    }

    /**
     * Increments the notification
     */
    public void increment() {
        mNewMessageCount.setValue(mNewMessageCount.getValue() + 1);
    }

    /**
     * Resets the notifications.
     */
    public void reset() {
        mNewMessageCount.setValue(0);
    }

}
