package com.carlospienovi.contactsparttwo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


public class CreateUpdateContactActivity extends ActionBarActivity {

    public static final String NEW_CONTACT = "NEW_CONTACT";
    public static final String CONTACT_TO_EDIT = "EDIT_CONTACT";
    public static final String CONTACT_POSITION = "CONTACT_POSITION";

    private static final int REQUEST_CODE_CAMERA = 1337;
    public static final int DELETE_CONTACT = 123;

    Button mDoneButton, mButtonTakePicture, mButtonDeleteContact;
    EditText mFirstName, mLastName, mNickname;
    ImageView mContactImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);
        init();
        firstNameEditText();
        prepareDoneButton();
        prepareDeleteButton();
        editedContact();
        takePicture();
    }

    private void editedContact() {
        Intent i = getIntent();
        if (i.hasExtra(CONTACT_TO_EDIT)) {
            mButtonDeleteContact.setVisibility(View.VISIBLE);
            Contact existingContact = i.getExtras().getParcelable(CONTACT_TO_EDIT);
            mFirstName.setText(existingContact.getFirstName());
            mLastName.setText(existingContact.getLastName());
            mNickname.setText(existingContact.getNickname());
            mContactImage.setImageBitmap(existingContact.getImage());
        } else {
            mButtonDeleteContact.setVisibility(View.GONE);
        }
    }

    private void takePicture() {
        mButtonTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
            }
        });
    }

    private void firstNameEditText() {
        mFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mDoneButton.setEnabled(!TextUtils.isEmpty(s));
            }
        });
    }

    private void init() {
        mDoneButton = (Button) findViewById(R.id.button_new_contact_save);
        mButtonDeleteContact = (Button) findViewById(R.id.button_delete_contact);
        mFirstName = (EditText) findViewById(R.id.new_contact_name);
        mLastName = (EditText) findViewById(R.id.new_contact_last_name);
        mNickname = (EditText) findViewById(R.id.new_contact_nickname);
        mContactImage = (ImageView) findViewById(R.id.new_contact_image);
        mButtonTakePicture = (Button) findViewById(R.id.button_take_picture);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                    mContactImage.setImageBitmap(imageBitmap);
                }
                break;
        }
    }

    private void prepareDeleteButton() {
        mButtonDeleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra(CONTACT_POSITION, getIntent().getIntExtra(CONTACT_POSITION, -1));
                setResult(DELETE_CONTACT, data);
                finish();
            }
        });
    }

    private void prepareDoneButton() {
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                Contact contact = new Contact(
                        mFirstName.getText().toString(),
                        mLastName.getText().toString(),
                        mNickname.getText().toString(),
                        drawableToBitmap(mContactImage.getDrawable())
                );
                data.putExtra(NEW_CONTACT, contact);
                data.putExtra(CONTACT_POSITION, getIntent().getIntExtra(CONTACT_POSITION, -1));
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
