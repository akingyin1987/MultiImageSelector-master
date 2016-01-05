package me.nereo.multi_image_selector.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.R;
import me.nereo.multi_image_selector.bean.Image;


/**
 * Created by zlcd on 2016/1/5.
 */
public class ImageViewPageAdapter  extends PagerAdapter {

    public   Context   context;

    public ViewPager   viewPager;

    public List<Image>   images = new ArrayList<>();

    public   ViewHolder[]  mImageViews  = new ViewHolder[4];

    public ImageViewPageAdapter(Context context,List<Image> images) {
        this.context = context;
        if(null != images){
            this.images = images;
        }



    }

    public   void  setData(List<Image>  images){
        this.images = images;
        notifyDataSetChanged();
    }



    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewHolder   holder = mImageViews[position % mImageViews.length];
        if(null != holder){
            container.removeView(holder.view);
        }

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        System.out.println("postion==="+position);
        ViewHolder  holde = mImageViews[position % mImageViews.length];
        Image  image = images.get(position);
        if(null == holde){
            View   view = LayoutInflater.from(context).inflate(R.layout.list_item_bigimage,null);
            holde = new ViewHolder();
            holde.view = view;
            holde.imageView=(ImageView)view.findViewById(R.id.image);
            ((ViewPager)container).addView(view, 0);
            mImageViews[position % mImageViews.length]=holde;
        }
        Picasso
             .with(context)
            .load(new File(image.path))
            .placeholder(R.drawable.default_error)
            .error(R.drawable.default_error)

            .into(holde.imageView);
        return  holde;
    }



    public   static  class  ViewHolder{

        View    view;

        CheckBox   checkBox;

        ImageView   imageView;
    }
}
