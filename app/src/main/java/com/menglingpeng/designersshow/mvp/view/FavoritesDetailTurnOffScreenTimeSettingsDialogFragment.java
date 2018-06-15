package com.menglingpeng.designersshow.mvp.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.utils.Constants;

public class FavoritesDetailTurnOffScreenTimeSettingsDialogFragment extends AppCompatDialogFragment {

    private Dialog dialog;
    private ImageView backgroundSettingsDialogCloseIv;
    private Button backgroundSettingsDialogBt;
    private ProgressBar backgroundSettingsDialogPb;
    private FavoritesDetailTurnOffScreenTimeSettingsDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getContext(), R.style.ThemeLoginDialog);
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_login, null);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.LoginDialog);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);
        dialog.setContentView(dialogView);
        backgroundSettingsDialogCloseIv = (ImageView) dialogView.findViewById(R.id.dialog_login_close_iv);
        backgroundSettingsDialogBt = (Button) dialogView.findViewById(R.id.dialog_login_bt);
        backgroundSettingsDialogPb = (ProgressBar) dialogView.findViewById(R.id.dialog_login_pb);
        dialog.show();
        backgroundSettingsDialogCloseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        backgroundSettingsDialogBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onTurnOffScreenTimeSettingsListener(backgroundSettingsDialogBt, backgroundSettingsDialogPb,
                        dialog);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(Constants.REDIRECT_USERS_TO_REQUEST_DRIBBBLE_ACCESS_URL);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        return dialog;
    }

    public interface FavoritesDetailTurnOffScreenTimeSettingsDialogListener{
        void onTurnOffScreenTimeSettingsListener(Button button, ProgressBar progressBar, Dialog dialog);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(!(getActivity() instanceof LoginDialogFragment.LoginDialogListener)){
            throw new IllegalStateException(context.getString(R.string.login_dialog_fragment_exception));
        }
        listener = (FavoritesDetailTurnOffScreenTimeSettingsDialogListener)
                getActivity();
    }
}
