package com.example.janethdelgado.dndnme;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.janethdelgado.dndnme.Models.Profile;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = "EditProfileActivity";

    // PICK_PHOTO_CODE is a constant integer
    public final static int PICK_PHOTO_CODE = 1046;

    private ImageView ivProfileImageEdit;
    private TextView tvUsername;
    private TextView etShortBioEdit;
    private TextView etLongBioEdit;
    private Button btnSave;
    private Button btnCancel;

    private String photoPath;
    private Profile userProfile;
    private ParseUser user = ParseUser.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ivProfileImageEdit = findViewById(R.id.ivProfileImageEdit);
        tvUsername = findViewById(R.id.tvUsername);
        etShortBioEdit = findViewById(R.id.etShortBioEdit);
        etLongBioEdit = findViewById(R.id.etLongBioEdit);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        //set views
        fillProfile();

        ivProfileImageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent for picking a photo from the gallery
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                // as long as the result is not null, it's safe to use the intent
                if (i.resolveActivity(getPackageManager()) != null) {
                    // Bring up gallery to select a photo
                    startActivityForResult(i, PICK_PHOTO_CODE);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shortBio = etShortBioEdit.getText().toString();
                String longBio = etLongBioEdit.getText().toString();

                if (shortBio.matches("") || longBio.matches("")) {
                    Toast.makeText(getApplicationContext(), "Don't leave entries blank! If you don't wish to edit, just click cancel.", Toast.LENGTH_LONG).show();
                    return;
                }

                //Log.d(TAG, "Photopath: " + photoPath);

                ParseFile file;

                if (photoPath != null) {
                    byte[] image = photoPath.getBytes();
                    file = new ParseFile("picturePath.png", image);
                }
                else {
                    file = userProfile.getProfileImage();
                }

                editProfile(user, shortBio, longBio, file);

                Intent i = new Intent(); // create a new Intent to put our data
                setResult(RESULT_OK, i); // set result code and bundle data for response
                finish(); // closes this activity and pass data to the original activity that launched this activity
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            //get Uri
            Uri photoUri = data.getData();

            // preview the photo (based on Uri)
            Glide.with(EditProfileActivity.this).load(photoUri).into(ivProfileImageEdit);

            photoPath = getPhotoFilepath(photoUri);
        }
    }

    private String getPhotoFilepath(Uri photoUri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(photoUri, projection, null, null, null);
        //int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        int column_index = cursor.getColumnIndex(projection[0]);
        String filepath = cursor.getString(column_index);
        cursor.close();

        return filepath;
    }


    private void fillProfile() {
        ParseQuery<Profile> query = ParseQuery.getQuery(Profile.class);
        query.include(Profile.KEY_USER);
        query.whereEqualTo(Profile.KEY_USER, user);
        query.findInBackground(new FindCallback<Profile>() {
            @Override
            public void done(List<Profile> profiles, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error with query");
                    e.printStackTrace();
                    return;
                }

                userProfile = profiles.get(0);

                ParseFile image = userProfile.getProfileImage();
                String username = user.getUsername();
                String shortBio = userProfile.getShortBio();
                String longBio = userProfile.getLongBio();

                //populate Views
                if(image != null) {
                    Glide.with(EditProfileActivity.this).load(image.getUrl()).into(ivProfileImageEdit);
                }

                if (username != null && !(username.equals("")))
                    tvUsername.setText("@" + username);
                else
                    tvUsername.setText("@Default");

                if (shortBio != null && !(shortBio.equals("")))
                    etShortBioEdit.setText(shortBio);
                else
                    etShortBioEdit.setText("This is the default text for the short bio.");

                if (longBio != null && !(longBio.equals("")))
                    etLongBioEdit.setText(longBio);
                else
                    etLongBioEdit.setText("This is the default text for the long bio.");
            }
        });
    }


    private void editProfile(final ParseUser user, final String sBio, final String lBio, final ParseFile file) {
        ParseQuery<Profile> query = ParseQuery.getQuery(Profile.class);
        query.include(Profile.KEY_USER);
        query.whereEqualTo(Profile.KEY_USER, user);
        query.findInBackground(new FindCallback<Profile>() {
            @Override
            public void done(List<Profile> profiles, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error with query");
                    e.printStackTrace();
                    return;
                }

                userProfile = profiles.get(0);

                userProfile.setShortBio(sBio);
                userProfile.setLongBio(lBio);

                userProfile.setProfileImage(file);

                userProfile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error while saving");
                            e.printStackTrace();
                            return;
                        }

                        Log.d(TAG, "Profile edited successfully");
                    }
                });
            }
        });
    }

    private byte[] readInFile(String path) throws IOException {

        byte[] data = null;
        File newFile = new File(path);
        InputStream input_stream = new BufferedInputStream(new FileInputStream(newFile));
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        data = new byte[16384]; // 16K
        int bytes_read;

        while ((bytes_read = input_stream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, bytes_read);
        }

        input_stream.close();
        return buffer.toByteArray();
    }
}