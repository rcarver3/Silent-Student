package gatech.criminals.silentstudent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

/**
 * {@link DialogFragment} that is called whenever {@link WifiDetailsFragment#onClickScanWifi} fails to
 * execute due to denied permissions more than once.
 */
public class PermissionsRationaleFragment extends DialogFragment {
    private static final String TAG = "PermissionsRationaleFragment";
    RationaleDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        Fragment parent = getParentFragment();

        if (parent instanceof RationaleDialogListener) {
            listener = (RationaleDialogListener) parent;
        } else {
            throw new ClassCastException(parent + " must implement RationaleDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext()).setMessage(getString(R.string.permission_rationale)).setPositiveButton(getString(R.string.continue_dialog), (dialog, which) -> {
            if (listener != null) {
                listener.onDialogPositiveClick(PermissionsRationaleFragment.this);
            } else {
                Log.e(TAG, "listener was null! not possible if onAttach is correct");
            }
        }).setNegativeButton(android.R.string.cancel, (dialog, which) -> dismiss()).create();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface RationaleDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
    }
}