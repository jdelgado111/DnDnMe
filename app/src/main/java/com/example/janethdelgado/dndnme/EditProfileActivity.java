package com.example.janethdelgado.dndnme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView ivProfileImageEdit;
    private TextView tvUsername;
    private TextView etShortBio;
    private TextView etLongBio;
    private Button btnSave;
    private Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ivProfileImageEdit = findViewById(R.id.ivProfileImageEdit);
        tvUsername = findViewById(R.id.tvUsername);
        etShortBio = findViewById(R.id.etShortBio);
        etLongBio = findViewById(R.id.etLongBio);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        String shortBio = getIntent().getStringExtra("shortBio");
        String longBio = getIntent().getStringExtra("longBio");

        etShortBio.setText(shortBio);
        etLongBio.setText(longBio);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shortBio = etShortBio.getText().toString();
                String longBio = etLongBio.getText().toString();

                if (shortBio.matches("") || longBio.matches("")) {
                    Toast.makeText(getApplicationContext(), "Don't leave entries blank! If you don't wish to edit, just click cancel.", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent data = new Intent(); // create a new Intent to put our data
                data.putExtra("shortBio", shortBio); // puts one string into the Intent (key, string)
                data.putExtra("longBio", longBio); // puts another string into the Intent (key, string)
                setResult(RESULT_OK, data); // set result code and bundle data for response
                finish(); // closes this activity and pass data to the original activity that launched this activity
            }
        });
    }
}
