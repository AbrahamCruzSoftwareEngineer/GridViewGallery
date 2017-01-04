package com.evolutiondso.www.verizontestgrid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Abe Cruz on 31/12/2016.
 */

public class MainActivity extends AppCompatActivity {

    private int count;
    private Bitmap[] thumbnails;
    private boolean[] thumbnailsselection;
    private String[] arrPath;
    private ImageAdapter imageAdapter;
    private String[] columns;
    private String orderBy;
    private GridView gridview;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoadImages();


        final Button selectBtn = (Button) findViewById(R.id.selectBtn);
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "", "Loading. Please wait...", true,true);
                String btnTxt = selectBtn.getText().toString();
                if (btnTxt.equals("LOAD VIDEOS")){
                    LoadVideos();
                    selectBtn.setText("LOAD PICTURES");
                }
                if (btnTxt.equals("LOAD PICTURES")){
                    LoadImages();
                    selectBtn.setText("LOAD VIDEOS");
                }

                new android.os.Handler().postDelayed(new Runnable() {public void run() {dialog.dismiss();}}, 3000);



            }
        });
    }

    public void LoadImages(){
        columns = new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        orderBy = MediaStore.Images.Media._ID;
        Cursor imagecursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                columns,
                null,
                null,
                orderBy);

        int image_column_index = imagecursor.getColumnIndex(MediaStore.Images.Media._ID);
        count = imagecursor.getCount();
        thumbnails = new Bitmap[count];
        arrPath = new String[count];
        thumbnailsselection = new boolean[count];

        for (int i = 0; i < count; i++) {
            imagecursor.moveToPosition(i);
            int id = imagecursor.getInt(image_column_index);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
            thumbnails[i] = MediaStore.Images.Thumbnails.getThumbnail(getApplicationContext().getContentResolver(), id, MediaStore.Images.Thumbnails.MINI_KIND, null);
            arrPath[i]= imagecursor.getString(dataColumnIndex);
        }

        gridview = (GridView) findViewById(R.id.PhoneImageGrid);
        imageAdapter = new ImageAdapter();
        gridview.setAdapter(imageAdapter);
        imagecursor.close();
    }

    public void LoadVideos(){
        columns = new String[]{MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID};
        orderBy = MediaStore.Video.Media._ID;
        Cursor imagecursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                columns,
                null,
                null,
                orderBy);

        int image_column_index = imagecursor.getColumnIndex(MediaStore.Video.Media._ID);
        count = imagecursor.getCount();
        thumbnails = new Bitmap[count];
        arrPath = new String[count];
        thumbnailsselection = new boolean[count];

        for (int i = 0; i < count; i++) {
            imagecursor.moveToPosition(i);
            int id = imagecursor.getInt(image_column_index);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Video.Media.DATA);
            thumbnails[i] = MediaStore.Video.Thumbnails.getThumbnail(getApplicationContext().getContentResolver(), id, MediaStore.Video.Thumbnails.MINI_KIND, null);
            arrPath[i]= imagecursor.getString(dataColumnIndex);
        }

        gridview = (GridView) findViewById(R.id.PhoneImageGrid);
        imageAdapter = new ImageAdapter();
        gridview.setAdapter(imageAdapter);
        imagecursor.close();
    }



    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ImageAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return count;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item, null);
                holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.imageview.setId(position);
            holder.imageview.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    int id = v.getId();
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse("file://" + arrPath[id]), "image/*");
                    startActivity(intent);
                }
            });
            holder.imageview.setImageBitmap(thumbnails[position]);
            holder.id = position;
            return convertView;
        }
    }

    public class ViewHolder {
        ImageView imageview;
        int id;
    }

}
