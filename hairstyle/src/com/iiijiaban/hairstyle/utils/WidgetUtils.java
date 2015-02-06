package com.iiijiaban.hairstyle.utils;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * 图形处理的工具类
 * @author Administrator
 *
 */
public class WidgetUtils {

	/**
	 * 操作图片使图片变暗或其他
	 * @param imageview  图片view 
	 */
	public static void setImageColor(ImageView imageview){
		Drawable drawable = imageview.getDrawable();
		drawable.setColorFilter(Color.GRAY,PorterDuff.Mode.MULTIPLY);
		imageview.setImageDrawable(drawable); 
	}
}
