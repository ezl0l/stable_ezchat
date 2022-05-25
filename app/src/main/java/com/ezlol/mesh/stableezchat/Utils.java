package com.ezlol.mesh.stableezchat;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Utils {
    public static String timestampToDatetime(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp * 1000);
        return new SimpleDateFormat("HH:mm", Locale.ROOT).format(calendar.getTime());
    }

    public static List<String> listOfImages(Context context) {
        List<String> externalImages = listOfImages(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        List<String> internalImages = listOfImages(context, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        List<String> allImages = new ArrayList<>();
        allImages.addAll(externalImages);
        allImages.addAll(internalImages);
        return allImages;
    }

    public static List<String> listOfImages(Context context, Uri uri){
        List<String> listOfAllImages = new ArrayList<>();

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        Cursor cursor = context.getContentResolver().query(uri, projection, null,
                null, MediaStore.Video.Media.DATE_TAKEN + " DESC");
        if(cursor != null && cursor.moveToFirst()) {

            int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            //column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

            String absolutePathOfImage;
            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(column_index_data);
                listOfAllImages.add(absolutePathOfImage);
            }
            cursor.close();
        }
        Log.d("Images", Arrays.toString(listOfAllImages.toArray()));
        return listOfAllImages;
    }
}
