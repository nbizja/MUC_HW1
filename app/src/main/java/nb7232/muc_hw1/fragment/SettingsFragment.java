package nb7232.muc_hw1.fragment;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;

import nb7232.muc_hw1.R;
import nb7232.muc_hw1.activity.MainActivity;
import nb7232.muc_hw1.model.User;
import nb7232.muc_hw1.model.UserHandler;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.settings_fragment, container, false);
        UserHandler uh = new UserHandler(getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE));
        User user = uh.getUser();

        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/fri/avatar.jpg", null);


        ((EditText) fragment.findViewById(R.id.registration_first_name_editable)).setText(user.getFirstName());
        ((EditText) fragment.findViewById(R.id.registration_last_name_editable)).setText(user.getLastName());
        ((EditText) fragment.findViewById(R.id.registration_email_editable)).setText(user.getEmail());
        ((EditText) fragment.findViewById(R.id.settings_sampling_interval)).setText(user.getSamplingInterval().toString());

        ImageView avatar = (ImageView) fragment.findViewById(R.id.settings_image);
        if (bitmap != null) {
            avatar.setBackgroundResource(0);
            avatar.setRotation(90);
            avatar.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 320, 480, false));
        }
        avatar.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity activity = (MainActivity) getActivity();
                        activity.dispatchTakePictureIntent();
                    }
                }
        );

        fragment.findViewById(R.id.settings_confirm_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity activity = (MainActivity) getActivity();
                        activity.changeSettings();
                    }
                }
        );

        return fragment;
    }
}
