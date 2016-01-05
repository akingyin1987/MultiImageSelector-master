package me.nereo.multi_image_selector;


import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.widget.AbsListView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.adapter.ImageViewPageAdapter;

import me.nereo.multi_image_selector.bean.Image;
import me.nereo.multi_image_selector.bean.Images;

/**
 * Created by zlcd on 2016/1/5.
 */
public class MulitImageBrowseActivity  extends FragmentActivity {

    private boolean hasFolderGened = false;
    private boolean mIsShowCamera = false;

    // 不同loader定义
    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;

    public List<Image>   imageList = new ArrayList<>();

    public ImageViewPageAdapter   adapter;

    public ViewPager    viewPager;

    public TextView  tv_left;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Images   images = (Images)getIntent().getSerializableExtra("selected");
        setContentView(R.layout.activity_viewpage_select);
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        tv_left = (TextView)findViewById(R.id.tv_left);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                 tv_left.setText((position+1)+"/"+adapter.getCount());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        adapter=new ImageViewPageAdapter(this,null);
        viewPager.setAdapter(adapter);
        adapter.setmSelectedImages(images.getImages());
        getSupportLoaderManager().initLoader(LOADER_ALL, null, mLoaderCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media._ID };

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if(id == LOADER_ALL) {
                CursorLoader cursorLoader = new CursorLoader(MulitImageBrowseActivity.this,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                    null, null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            }else if(id == LOADER_CATEGORY){
                CursorLoader cursorLoader = new CursorLoader(MulitImageBrowseActivity.this,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                    IMAGE_PROJECTION[0]+" like '%"+args.getString("path")+"%'", null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                List<Image> images = new ArrayList<>();
                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do{
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        Image image = new Image(path, name, dateTime);
                        images.add(image);
                    }while(data.moveToNext());
                    System.out.println("size==="+images.size());

                    adapter.setData(images);
                    hasFolderGened = true;
                    if(adapter.getmSelectedImages().size()>0){
                        int postion = getSelectPostion(images,adapter.getmSelectedImages().get(0));
                        viewPager.setCurrentItem(postion);
                    }

                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };


    public   int   getSelectPostion(List<Image> items,Image  image){
        int  postion = 0;
        for(Image  image1 : items){
            if(image1.equals(image)){
                return  postion;
            }
            postion++;
        }
        return  postion;

    }


}
