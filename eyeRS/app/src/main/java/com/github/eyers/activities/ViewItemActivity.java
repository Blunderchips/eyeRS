package com.github.eyers.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.eyers.DBOperations;
import com.github.eyers.R;
import com.github.eyers.info.ItemInfo;
import com.github.eyers.wrapper.ItemWrapper;
import com.vj.widgets.AutoResizeTextView;

public class ViewItemActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * ItemWrapper
     */
    public static ItemWrapper ITEM = null;
    /**
     * Content Resolver declaration.
     */
    private ContentResolver eyeRSContentResolver;

    private ImageView image;
    private AutoResizeTextView title;
    private AutoResizeTextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_view_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.title = (AutoResizeTextView) findViewById(R.id.txtItemTitle);
        this.description = (AutoResizeTextView) findViewById(R.id.txtItemDescription);
        this.image = (ImageView) findViewById(R.id.img);

        try {
            this.title.setText(ITEM.getName());
            this.description.setText(ITEM.getDescription());
            this.image.setImageBitmap(ITEM.getImage());
        } catch (NullPointerException npe) {
            Log.e("Error adding item", "Something is null");
        }

        findViewById(R.id.btnEditItem).setOnClickListener(this);
        findViewById(R.id.btnDeleteItem).setOnClickListener(this);

        Toast.makeText(this, ViewItemActivity.ITEM.getName(), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        MainActivity.STATE = "main";
        super.startActivity(new Intent(this, MainActivity.class));
        super.finish();
        super.onBackPressed();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEditItem:
                this.edit();
                break;
            case R.id.btnDeleteItem:
                this.deleteItem();
                break;
        }
    }

    private void edit() {

        /**
         * Content resolver object
         */
        eyeRSContentResolver = this.getContentResolver();


    }

    /**
     * Removes the record of the item when the user presses delete
     * Once an item is deleted it cannot be undone
     */
    private void deleteItem() {

        String itemToDelete = this.title.getText().toString();

        /**
         * Content resolver object
         */
        eyeRSContentResolver = this.getContentResolver();
        String whereClause = ItemInfo.ITEM_NAME + " = '" + itemToDelete + "'";
        String[] whereArgs = {ItemInfo.ITEM_NAME};

        try {

            eyeRSContentResolver.delete(DBOperations.CONTENT_URI_ITEMS,
                    whereClause, whereArgs);

            MainActivity.STATE = "main";
            super.startActivity(new Intent(this, MainActivity.class));
            super.finish();
        } catch (Exception ex) {

            Log.e("ViewItemActivity", ex.getMessage(), ex);
        }
    }
}
