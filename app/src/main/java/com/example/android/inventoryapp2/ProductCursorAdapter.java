package com.example.android.inventoryapp2;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.inventoryapp2.data.ProductContract.ProductEntry;

public class ProductCursorAdapter extends CursorAdapter {

    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView nameOfProductView = (TextView) view.findViewById(R.id.product_name);
        TextView quantityView = (TextView) view.findViewById(R.id.quantity);
        TextView priceView = (TextView) view.findViewById(R.id.price);
        ImageView pictureImageView = (ImageView) view.findViewById(R.id.image);

        int idColumnIndex = cursor.getColumnIndex(ProductEntry._ID);
        int imageColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_IMAGE);
        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);

        byte[] productImage = cursor.getBlob(imageColumnIndex);
        String productName = cursor.getString(nameColumnIndex);
        final int productQuantity = cursor.getInt(quantityColumnIndex);
        int productPrice = cursor.getInt(priceColumnIndex);
        final int rowID = cursor.getInt(idColumnIndex);

        nameOfProductView.setText(productName);
        quantityView.setText("Quantity: " + productQuantity);
        priceView.setText("$" + productPrice);
        Bitmap bitmapP = BitmapFactory.decodeByteArray(productImage, 0, productImage.length);
        pictureImageView.setImageBitmap(bitmapP);

        Button saleButton = (Button) view.findViewById(R.id.sale);
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, rowID);
                clickSale(context, productQuantity, currentProductUri);
            }
        });
    }


    private void clickSale(Context context, int quantity, Uri uriProduct) {
        if (quantity == 0) {
            Log.v("clickSale", "The quantity cannot be reduced");
        } else {
            int newQuantity = quantity - 1;
            Log.v("clickSale", "new quantity : " + newQuantity);

            ContentValues values = new ContentValues();
            values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, newQuantity);

            int rowsAffected = context.getContentResolver().update(uriProduct, values, null, null);
            if (rowsAffected == 0) {
                Log.v("clickSale", "no rows changed");

            } else {
                Log.v("clickSale", "rows changed : " + rowsAffected);

            }
        }
    }
}

