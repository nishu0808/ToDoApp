package com.example.todoapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.todoapp.model.ToDoList;

import java.io.ByteArrayOutputStream;

import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    Button mCancelButton;
    Button mDoneButton;
    EditText  mTitleEditText;
    EditText  mDescEditText;
    ImageView mImageView;
    TextView  mImageTextView;

    private static final int PICK_IMAGE = 100;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.details_activity_layout);

        initViews();
        setListeners();
    }

    /**
     * Function to initialize views
     */
    private void initViews() {
        mCancelButton  = findViewById(R.id.cancel_button);
        mDoneButton    = findViewById(R.id.done_button);
        mTitleEditText = findViewById(R.id.title_edit_text);
        mDescEditText  = findViewById(R.id.desc_edit_text);
        mImageView     = findViewById(R.id.image_view);
        mImageTextView = findViewById(R.id.image_text_view);
    }

    /**
     * Function to set listeners to the views
     */
    private void setListeners() {
        mCancelButton.setOnClickListener(mCancelButtonClickListener);
        mDoneButton.setOnClickListener(mDoneButtonClickListener);
        mImageView.setOnClickListener(mImageClickListener);
    }

    /**
     * Clear the edit text and image-view when cancel button is clicked
     */
    View.OnClickListener mCancelButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mTitleEditText.setText("");
            mDescEditText.setText("");
            mImageView.setImageDrawable(null);
            mImageTextView.setVisibility(View.VISIBLE);
        }
    };

    /**
     * Function to insert a new row into the database when done button is clicked
     */
    View.OnClickListener mDoneButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!mTitleEditText.getText().toString().equals("") &&
               !mDescEditText.getText().toString().equals("")  &&
                mImageView.getDrawable() != null) {
                String title = mTitleEditText.getText().toString();
                String desc  = mDescEditText.getText().toString();

                // create a bitmap from the image-view and convert it into byte-array
                Bitmap bitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] image = baos.toByteArray();

                // create a new instance of ToDoList and insert it into the database and finish the
                // current activity
                ToDoList toDoList = new ToDoList(title,desc,image,false);
                MainActivity.mDataBase.myDao().addData(toDoList);
                finish();
            }
        }
    };

    /**
     * Navigate to gallery to select an image when image-view is clicked
     */
    View.OnClickListener mImageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, PICK_IMAGE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        // set the image selected from gallery to the image-view and hide the image text
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            mImageView.setImageURI(data.getData());
            mImageTextView.setVisibility(View.GONE);
        } else {
            // if result code does not match, show the image text
            mImageTextView.setVisibility(View.VISIBLE);
        }
    }
}
