package com.jet.sweettips.toast;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.IntegerRes;
import android.support.annotation.StyleRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jet.sweettips.R;
import com.jet.sweettips.util.ScreenUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 自定义Toast
 *
 * 作者:幻海流心
 * GitHub:https://github.com/HuanHaiLiuXin
 * 邮箱:wall0920@163.com
 * 2016/12/13
 */

public final class SweetToast {
    public static final int LENGTH_SHORT = 0;
    public static final int LENGTH_LONG = 1;
    public static final long SHORT_DELAY = 2000; // 2 seconds
    public static final long LONG_DELAY = 3500; // 3.5 seconds
    //SweetToast默认背景色
    private static int mBackgroundColor = 0XE8484848;
    //
    private View mContentView = null;   //内容区域View
    private SweetToastConfiguration mConfiguration = null;
    private WindowManager mWindowManager = null;
    private boolean showing = false;    //是否在展示中
    private boolean showEnabled = true; //是否允许展示
    private boolean hideEnabled = true; //是否允许移除
    private boolean stateChangeEnabled = true;  //是否允许改变展示状态

    public static SweetToast makeText(Context context, CharSequence text){
        return makeText(context, text, LENGTH_SHORT);
    }
    public static SweetToast makeText(View mContentView){
        return makeText(mContentView, LENGTH_SHORT);
    }
    public static SweetToast makeText(Context context, CharSequence text, int duration) {
        try {
            LayoutInflater inflate = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflate.inflate(R.layout.transient_notification, null);
            TextView tv = (TextView)v.findViewById(R.id.message);
            tv.setText(text);
            SweetToast sweetToast = new SweetToast();
            sweetToast.mContentView = v;
            sweetToast.mContentView.setBackgroundDrawable(getBackgroundDrawable(sweetToast, mBackgroundColor));
            initConfiguration(sweetToast,duration);
            return sweetToast;
        }catch (Exception e){
            Log.e("幻海流心","e:"+e.getLocalizedMessage()+":69");
        }
        return null;
    }
    public static SweetToast makeText(View mContentView, int duration){
        SweetToast sweetToast = new SweetToast();
        sweetToast.mContentView = mContentView;
        initConfiguration(sweetToast,duration);
        return sweetToast;
    }
    private static void initConfiguration(SweetToast sweetToast,int duration){
        try {
            if(duration < 0){
                throw new RuntimeException("显示时长必须>=0!");
            }
            //1:初始化mWindowManager
            sweetToast.mWindowManager = (WindowManager) sweetToast.getContentView().getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            //2:初始化mConfiguration
            SweetToastConfiguration mConfiguration = new SweetToastConfiguration();
            //2.1:设置显示时间
            mConfiguration.setDuration(duration);
            //2.2:设置WindowManager.LayoutParams属性
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            final Configuration config = sweetToast.getContentView().getContext().getResources().getConfiguration();
            final int gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
            params.gravity = gravity;
            if ((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.FILL_HORIZONTAL) {
                params.horizontalWeight = 1.0f;
            }
            if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.FILL_VERTICAL) {
                params.verticalWeight = 1.0f;
            }
            params.x = 0;
            params.y = sweetToast.getContentView().getContext().getResources().getDimensionPixelSize(R.dimen.toast_y_offset);
            params.verticalMargin = 0.0f;
            params.horizontalMargin = 0.0f;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.format = PixelFormat.TRANSLUCENT;
            params.windowAnimations = R.style.Anim_SweetToast;
            //在小米5S上实验,前两种type均会报错
            params.type = WindowManager.LayoutParams.TYPE_TOAST;
//            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
//            params.type = WindowManager.LayoutParams.TYPE_PHONE;
            params.setTitle("Toast");
            params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
            mConfiguration.setParams(params);
            sweetToast.setConfiguration(mConfiguration);
        }catch (Exception e){
            Log.e("幻海流心","e:"+e.getLocalizedMessage()+":120");
        }
    }
    /**
     * 根据指定的背景色,获得mToastView的背景drawable实例
     * @param backgroundColor
     * @return
     */
    private static ShapeDrawable getBackgroundDrawable(SweetToast sweetToast, @ColorInt int backgroundColor){
        try {
            ShapeDrawable shapeDrawable = new ShapeDrawable();
            DrawableCompat.setTint(shapeDrawable,backgroundColor);
            //获取当前设备的屏幕尺寸
            //实验发现不同的设备上面,Toast内容区域的padding值并不相同,根据屏幕的宽度分别进行处理,尽量接近设备原生Toast的体验
            int widthPixels = sweetToast.getContentView().getResources().getDisplayMetrics().widthPixels;
            int heightPixels = sweetToast.getContentView().getResources().getDisplayMetrics().heightPixels;
            float density = sweetToast.getContentView().getResources().getDisplayMetrics().density;
            if(widthPixels >= 1070){
                //例如小米5S:1920 x 1080
                shapeDrawable.setPadding((int)(density*13),(int)(density*12),(int)(density*13),(int)(density*12));
            }else {
                //例如红米2:1280x720
                shapeDrawable.setPadding((int)(density*14),(int)(density*13),(int)(density*14),(int)(density*13));
            }
            float radius = density*8;
            float[] outerRadii = new float[]{radius,radius,radius,radius,radius,radius,radius,radius};
            int width = sweetToast.getContentView().getWidth();
            int height = sweetToast.getContentView().getHeight();
            RectF rectF = new RectF(1,1,width-1,height-1);
            RoundRectShape roundRectShape = new RoundRectShape(outerRadii,rectF,null);
            shapeDrawable.setShape(roundRectShape);
            DrawableCompat.setTint(shapeDrawable,backgroundColor);
            return shapeDrawable;
        }catch (Exception e){
            Log.e("幻海流心","e:"+e.getLocalizedMessage()+":154");
        }
        return null;
    }
    /**
     * 自定义SweetToast实例的入场出场动画
     * @param windowAnimations
     * @return
     */
    public SweetToast setWindowAnimations(@StyleRes int windowAnimations){
        mConfiguration.getParams().windowAnimations = windowAnimations;
        return this;
    }
    public SweetToast setGravity(int gravity, int xOffset, int yOffset) {
        mConfiguration.getParams().gravity = gravity;
        mConfiguration.getParams().x = xOffset;
        mConfiguration.getParams().y = yOffset;
        return this;
    }
    public SweetToast setMargin(float horizontalMargin, float verticalMargin) {
        mConfiguration.getParams().horizontalMargin = horizontalMargin;
        mConfiguration.getParams().verticalMargin = verticalMargin;
        return this;
    }
    /**
     * 向mContentView中添加View
     *
     * @param view
     * @param index
     * @return
     */
    public SweetToast addView(View view, int index) {
        if(mContentView != null && mContentView instanceof ViewGroup){
            ((ViewGroup)mContentView).addView(view,index);
        }
        return this;
    }
    /**
     * 设置SweetToast实例中TextView的文字颜色
     *
     * @param messageColor
     * @return
     */
    public SweetToast messageColor(@ColorInt int messageColor){
        if(mContentView !=null && mContentView.findViewById(R.id.message) != null && mContentView.findViewById(R.id.message) instanceof TextView){
            TextView textView = ((TextView) mContentView.findViewById(R.id.message));
            textView.setTextColor(messageColor);
        }
        return this;
    }
    /**
     * 设置SweetToast实例的背景颜色
     *
     * @param backgroundColor
     * @return
     */
    public SweetToast backgroundColor(@ColorInt int backgroundColor){
        if(mContentView!=null){
            mContentView.setBackgroundDrawable(getBackgroundDrawable(this, backgroundColor));
        }
        return this;
    }
    /**
     * 设置SweetToast实例的背景资源
     *
     * @param background
     * @return
     */
    public SweetToast backgroundResource(@DrawableRes int background){
        if(mContentView!=null){
            mContentView.setBackgroundResource(background);
        }
        return this;
    }
    /**
     * 设置SweetToast实例的文字颜色及背景颜色
     *
     * @param messageColor
     * @param backgroundColor
     * @return
     */
    public SweetToast colors(@ColorInt int messageColor, @ColorInt int backgroundColor) {
        messageColor(messageColor);
        backgroundColor(backgroundColor);
        return this;
    }
    /**
     * 设置SweetToast实例的文字颜色及背景资源
     *
     * @param messageColor
     * @param background
     * @return
     */
    public SweetToast textColorAndBackground(@ColorInt int messageColor, @DrawableRes int background) {
        messageColor(messageColor);
        backgroundResource(background);
        return this;
    }

    /**
     * 设置SweetToast实例的宽高
     *  很有用的功能,参考了简书上的文章:http://www.jianshu.com/p/491b17281c0a
     * @param width     SweetToast实例的宽度,单位是pix
     * @param height    SweetToast实例的高度,单位是pix
     * @return
     */
    public SweetToast size(int width, int height){
        if(mContentView!=null && mContentView instanceof LinearLayout){
            mContentView.setMinimumWidth(width);
            mContentView.setMinimumHeight(height);
            ((LinearLayout)mContentView).setGravity(Gravity.CENTER);
            try {
                TextView textView = ((TextView) mContentView.findViewById(R.id.message));
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
                params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                params.height = LinearLayout.LayoutParams.MATCH_PARENT;
                textView.setLayoutParams(params);
                textView.setGravity(Gravity.CENTER);
            }catch (Exception e){
                Log.e("幻海流心","e:"+e.getLocalizedMessage());
            }
        }
        return this;
    }

    /**
     * 设置SweetToast实例的显示位置:左上
     * @return
     */
    public SweetToast leftTop(){
        return setGravity(Gravity.LEFT|Gravity.TOP,0,0);
    }
    /**
     * 设置SweetToast实例的显示位置:右上
     * @return
     */
    public SweetToast rightTop(){
        return setGravity(Gravity.RIGHT|Gravity.TOP,0,0);
    }
    /**
     * 设置SweetToast实例的显示位置:左下
     * @return
     */
    public SweetToast leftBottom(){
        return setGravity(Gravity.LEFT|Gravity.BOTTOM,0,0);
    }
    /**
     * 设置SweetToast实例的显示位置:右下
     * @return
     */
    public SweetToast rightBottom(){
        return setGravity(Gravity.RIGHT|Gravity.BOTTOM,0,0);
    }
    /**
     * 设置SweetToast实例的显示位置:上中
     * @return
     */
    public SweetToast topCenter(){
        return setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
    }
    /**
     * 设置SweetToast实例的显示位置:下中
     * @return
     */
    public SweetToast bottomCenter(){
        return setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
    }
    /**
     * 设置SweetToast实例的显示位置:左中
     * @return
     */
    public SweetToast leftCenter(){
        return setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL,0,0);
    }
    /**
     * 设置SweetToast实例的显示位置:右中
     * @return
     */
    public SweetToast rightCenter(){
        return setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL,0,0);
    }
    /**
     * 设置SweetToast实例的显示位置:正中
     * @return
     */
    public SweetToast center(){
        return setGravity(Gravity.CENTER,0,0);
    }
    /**
     * 将SweetToast实例显示在指定View的顶部
     * @param targetView    指定View
     * @param statusHeight  状态栏显示情况下,状态栏的高度
     * @return
     */
    public SweetToast layoutAbove(View targetView, int statusHeight){
        if(mContentView!=null){
            int[] locations = new int[2];
            targetView.getLocationOnScreen(locations);
            //必须保证指定View的顶部可见
            int screenHeight = ScreenUtil.getScreenHeight(mContentView.getContext());
            if(locations[1] > statusHeight&&locations[1]<screenHeight){
                setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,screenHeight - locations[1]);
            }
        }
        return this;
    }
    /**
     * 将SweetToast实例显示在指定View的底部
     * @param targetView
     * @param statusHeight
     * @return
     */
    public SweetToast layoutBellow(View targetView, int statusHeight){
        if(mContentView!=null){
            int[] locations = new int[2];
            targetView.getLocationOnScreen(locations);
            //必须保证指定View的底部可见
            int screenHeight = ScreenUtil.getScreenHeight(mContentView.getContext());
            if(locations[1]+targetView.getHeight() > statusHeight&&locations[1]+targetView.getHeight()<screenHeight){
                setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,locations[1]+targetView.getHeight()-statusHeight);
            }
        }
        return this;
    }


    /**********************************************  SweetToast显示及移除  **********************************************/
    Handler mHandler = new Handler();
    Runnable mHide = new Runnable() {
        @Override
        public void run() {
            handleHide();
        }
    };
    protected void handleHide() {
        if(this != null && mContentView != null){
            if(stateChangeEnabled){
                if(hideEnabled){
                    if(showing){
                        mWindowManager.removeView(mContentView);
                    }
                    showing = false;
                    mContentView = null;
                }else{
                }
            }
        }
    }
    protected void handleShow() {
        if(mContentView != null){
            if(stateChangeEnabled){
                if(showEnabled){
                    try {
                        mWindowManager.addView(mContentView,mConfiguration.getParams());
                        long delay = (mConfiguration.getDuration() == LENGTH_LONG || mConfiguration.getDuration() == Toast.LENGTH_LONG) ? LONG_DELAY : ((mConfiguration.getDuration() == LENGTH_SHORT || mConfiguration.getDuration() == Toast.LENGTH_SHORT)? SHORT_DELAY : mConfiguration.getDuration());
                        mHandler.postDelayed(mHide,delay);
                        showing = true;
                    }catch (Exception e){
                        Log.e("幻海流心","e:"+e.getLocalizedMessage()+":213");
                    }
                }
            }
        }
    }

    /**
     * 保持当前实例的显示状态:不允许向Window中添加或者移除View
     */
    protected void removeCallbacks(){
        stateChangeEnabled = false;
    }

    /**
     * 设置是否允许展示当前实例
     * @param showEnabled
     */
    public void setShowEnabled(boolean showEnabled) {
        this.showEnabled = showEnabled;
    }

    /**
     * 设置是否允许移除当前实例中的View
     * @param hideEnabled
     */
    public void setHideEnabled(boolean hideEnabled) {
        this.hideEnabled = hideEnabled;
    }

    /**
     * 设置是否允许改变当前实例的展示状态
     * @param stateChangeEnabled
     */
    public void setStateChangeEnabled(boolean stateChangeEnabled) {
        this.stateChangeEnabled = stateChangeEnabled;
    }

    /**
     * 将当前实例添加到队列{@link SweetToastManager#queue}中,若队列为空,则加入队列后直接进行展示
     */
    public void show(){
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                //Android6.0以上，需要动态声明权限
                if(mContentView!=null && !Settings.canDrawOverlays(mContentView.getContext().getApplicationContext())) {
                    //用户还未允许该权限
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    mContentView.getContext().startActivity(intent);
                    return;
                } else if(mContentView!=null) {
                    //用户已经允许该权限
                    SweetToastManager.show(this);
                }
            } else {
                //Android6.0以下，不用动态声明权限
                if (mContentView!=null) {
                    SweetToastManager.show(this);
                }
            }
//            SweetToastManager.show(this);
        }catch (Exception e){
            Log.e("幻海流心","e:"+e.getLocalizedMessage()+":232");
        }
    }
    /**
     * 利用队列{@link SweetToastManager#queue}中正在展示的SweetToast实例,继续展示当前实例的内容
     */
    public void showByPrevious(){
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                //Android6.0以上，需要动态声明权限
                if(mContentView!=null && !Settings.canDrawOverlays(mContentView.getContext().getApplicationContext())) {
                    //用户还未允许该权限
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    mContentView.getContext().startActivity(intent);
                    return;
                } else if(mContentView!=null) {
                    //用户已经允许该权限
                    SweetToastManager.showByPrevious(this);
                }
            } else {
                //Android6.0以下，不用动态声明权限
                if (mContentView!=null) {
                    SweetToastManager.showByPrevious(this);
                }
            }
//            SweetToastManager.showByPrevious(this);
        }catch (Exception e){
            Log.e("幻海流心","e:"+e.getLocalizedMessage()+":290");
        }
    }
    /**
     * 清空队列{@link SweetToastManager#queue}中已经存在的SweetToast实例,直接展示当前实例的内容
     */
    public void showImmediate(){
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                //Android6.0以上，需要动态声明权限
                if(mContentView!=null && !Settings.canDrawOverlays(mContentView.getContext().getApplicationContext())) {
                    //用户还未允许该权限
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    mContentView.getContext().startActivity(intent);
                    return;
                } else if(mContentView!=null) {
                    //用户已经允许该权限
                    SweetToastManager.showImmediate(this);
                }
            } else {
                //Android6.0以下，不用动态声明权限
                if (mContentView!=null) {
                    SweetToastManager.showImmediate(this);
                }
            }
//            SweetToastManager.showImmediate(this);
        }catch (Exception e){
            Log.e("幻海流心","e:"+e.getLocalizedMessage()+":252");
        }
    }
    /**
     * 移除当前SweetToast并将mContentView置空
     */
    public void hide() {
        mHandler.post(mHide);
    }
    /**********************************************  SweetToast显示及移除  **********************************************/

    //Setter&Getter
    public View getContentView() {
        return mContentView;
    }
    public void setContentView(View mContentView) {
        this.mContentView = mContentView;
    }
    public SweetToastConfiguration getConfiguration() {
        return mConfiguration;
    }
    public void setConfiguration(SweetToastConfiguration mConfiguration) {
        this.mConfiguration = mConfiguration;
    }
    public WindowManager getWindowManager() {
        return mWindowManager;
    }
    public void setWindowManager(WindowManager mWindowManager) {
        this.mWindowManager = mWindowManager;
    }
    public boolean isShowing() {
        return showing;
    }
    public void setShowing(boolean showing) {
        this.showing = showing;
    }
}