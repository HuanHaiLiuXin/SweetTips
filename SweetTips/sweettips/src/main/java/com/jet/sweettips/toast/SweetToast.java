package com.jet.sweettips.toast;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.AnimRes;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jet.sweettips.R;
import com.jet.sweettips.util.ScreenUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * SweetToast：自定义Toast
 *      功能点:
 *          1:创建SweetToast实例
 *              {@link #makeText(Context, CharSequence)}
 *              {@link #makeText(Context, CharSequence, int)}
 *              {@link #makeText(View)}
 *          2:设置当前SweetToast实例的出入场动画(SDK系统内置资源)
 *              {@link #setWindowAnimations(SweetToastWindowAnimations)}
 *          3:设置当前SweetToast实例的出入场动画(App中自定义)
 *              {@link #setAnimations(int, int)}
 *              {@link #setAnimations(Animation, Animation)}
 *          4:设置当前SweetToast实例的对齐方式
 *              {@link #setGravity(int, int, int)}
 *          5:设置当前SweetToast实例的horizontalMargin,verticalMargin值
 *              {@link #setMargin(float, float)}
 *          6:向当前SweetToast实例的mContentView中添加View
 *              {@link #addView(View, int)}
 *          7:颜色相关
 *              {@link #messageColor(int)}  //设置当前SweetToast实例中TextView的文字颜色
 *              {@link #backgroundColor(int)}   //设置当前SweetToast实例中mContentView的背景颜色
 *              {@link #textColorAndBackground(int, int)}   //设置当前SweetToast实例的文字颜色及背景资源
 *              {@link #colors(int, int)}   //设置当前SweetToast实例的文字颜色及背景颜色
 *          8:设置当前SweetToast实例中mContentView的背景资源
 *              {@link #backgroundResource(int)}
 *          9:设置当前SweetToast实例的最小宽高
 *              {@link #minSize(int, int)}
 *          10:位置相关
 *              {@link #leftTop()}      //设置当前SweetToast实例的显示位置:左上
 *              {@link #rightTop()}     //设置当前SweetToast实例的显示位置:右上
 *              {@link #leftBottom()}   //设置当前SweetToast实例的显示位置:左下
 *              {@link #rightBottom()}  //设置当前SweetToast实例的显示位置:右下
 *              {@link #topCenter()}    //设置当前SweetToast实例的显示位置:上中
 *              {@link #bottomCenter()} //设置当前SweetToast实例的显示位置:下中
 *              {@link #leftCenter()}   //设置当前SweetToast实例的显示位置:左中
 *              {@link #rightCenter()}  //设置当前SweetToast实例的显示位置:右中
 *              {@link #center()}       //设置当前SweetToast实例的显示位置:正中
 *              {@link #layoutAbove(View, int)}     //将当前SweetToast实例显示在指定View的顶部
 *              {@link #layoutBellow(View, int)}    //将当前SweetToast实例显示在指定View的底部
 *          11:将当前SweetToast实例添加到队列{@link SweetToastManager#queue}中,若队列为空,则加入队列后直接进行展示
 *              {@link #show()}
 *          12:利用队列{@link SweetToastManager#queue}中正在展示的SweetToast实例,继续展示当前SweetToast实例的内容
 *              {@link #showByPrevious()}
 *          13:清空队列{@link SweetToastManager#queue}中已经存在的SweetToast实例,直接展示当前SweetToast实例的内容
 *              {@link #showImmediate()}
 *      注意：
 *          1:SweetToast实例的动画分为两类，且两类动画互斥，有且必有其中一种会进行展示
 *              1.1:利用{@link android.view.WindowManager.LayoutParams#windowAnimations}，指定的系统内置出入场动画资源，
 *                  见{@link SweetToast.SweetToastWindowAnimations}
 *              1.2:用户不满意系统内置的出入场动画资源，可以调用
 *                  {@link SweetToast#setAnimations(int, int)},
 *                  {@link SweetToast#setAnimations(Animation, Animation)}进行自定义
 *          2:两类动画的区别：
 *              2.1:{@link android.view.WindowManager.LayoutParams#windowAnimations}仅仅能利用系统内置的动画资源，
 *                  {@link SweetToast#setAnimations(int, int)},{@link SweetToast#setAnimations(Animation, Animation)}可自定义
 *              2.2:{@link android.view.WindowManager.LayoutParams#windowAnimations}
 *                  是针对于 mRootView 整体的出入场动画，动画展示效果更完整/不限制动画展示的位置！
 *                  {@link SweetToast#setAnimations(int, int)},{@link SweetToast#setAnimations(Animation, Animation)}
 *                  是针对 mContentView 的动画，mContentView 是 mRootView 的子项,所以自定义动画的可视范围会限制在 mRootView 区域内！！！
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
    /**
     * 将SDK内置的几种在App中可见的出入场动画资源进行封装，
     * 用于{@link SweetToast#setWindowAnimations(SweetToastWindowAnimations)},防止用户传值错误
     */
    public enum SweetToastWindowAnimations{
        AnimationDialog(android.R.style.Animation_Dialog),
        AnimationToast(android.R.style.Animation_Toast),
        AnimationInputMethod(android.R.style.Animation_InputMethod),
        AnimationActivity(android.R.style.Animation_Activity),
        AnimationTranslucent(android.R.style.Animation_Translucent);

        private int windowAnimations = android.R.style.Animation_Toast;
        private SweetToastWindowAnimations(int windowAnimations){
            this.windowAnimations = windowAnimations;
        }
    }
    private int windowAnimations = 0;
    //SweetToast默认背景色
    private static int mBackgroundColor = 0XE8484848;
    /**
     * 执行完{@link SweetToast#handleShow()}后,展示的入场动画
     */
    private Animation animEnter = null;
    /**
     * 离场动画展示完后，恰好执行{@link SweetToast#handleHide()}
     */
    private Animation animExit = null;
    //
    private View mRootView = null;      //内容区域View
    private View mContentView = null;   //内容区域直接子项
    private SweetToastConfiguration mConfiguration = null;
    private WindowManager mWindowManager = null;
    private boolean showing = false;    //是否在展示中
    private boolean showEnabled = true; //是否允许展示
    private boolean hideEnabled = true; //是否允许移除
    private boolean stateChangeEnabled = true;  //是否允许改变展示状态

    /**
     * 创建SweetToast实例
     * @param context
     * @param text  展示的文字内容
     * @return
     */
    public static SweetToast makeText(@NonNull Context context, CharSequence text){
        return makeText(context, text, LENGTH_SHORT);
    }

    /**
     * 创建SweetToast实例
     * @param mRootView 自定义内容区域
     * @return
     */
    public static SweetToast makeText(@NonNull View mRootView){
        return makeText(mRootView, LENGTH_SHORT);
    }

    /**
     * 创建SweetToast实例
     * @param context
     * @param text    展示的文字内容
     * @param duration  自定义显示时间
     * @return
     */
    public static SweetToast makeText(@NonNull Context context, CharSequence text, int duration) {
        try {
            LayoutInflater inflate = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflate.inflate(R.layout.transient_notification, null);
            TextView tv = (TextView)v.findViewById(R.id.message);
            tv.setText(text);
            SweetToast sweetToast = new SweetToast();
            sweetToast.mRootView = v;
            sweetToast.mContentView = v.findViewById(R.id.ll_content);
            sweetToast.mContentView.setBackgroundDrawable(getBackgroundDrawable(sweetToast, mBackgroundColor));
            initConfiguration(sweetToast,duration);
            return sweetToast;
        }catch (Exception e){
            Log.e("幻海流心","e:"+e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * 创建SweetToast实例
     * @param mRootView 自定义内容区域
     * @param duration  自定义显示时间
     * @return
     */
    public static SweetToast makeText(View mRootView, int duration){
        SweetToast sweetToast = new SweetToast();
        sweetToast.mRootView = mRootView;
        try {
            sweetToast.mContentView = ((ViewGroup)mRootView).getChildAt(0);
        }catch (Exception e){
            Log.e("幻海流心","e:"+e.getLocalizedMessage());
        }
        initConfiguration(sweetToast,duration);
        return sweetToast;
    }

    /**
     * 初始化
     * @param sweetToast
     * @param duration  自定义显示时间
     */
    private static void initConfiguration(SweetToast sweetToast,int duration){
        try {
            if(duration < 0){
                throw new RuntimeException("显示时长必须>=0!");
            }
            //1:初始化mWindowManager
            sweetToast.mWindowManager = (WindowManager) sweetToast.getContentView().getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            //2:初始化mConfiguration
            SweetToastConfiguration mConfiguration = new SweetToastConfiguration();
            mConfiguration.setDuration(duration);
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
//            params.windowAnimations = R.style.Anim_SweetToast;
//            params.type = WindowManager.LayoutParams.TYPE_TOAST;
//            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            params.type = WindowManager.LayoutParams.TYPE_PHONE;
            params.setTitle("Toast");
            params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
            mConfiguration.setParams(params);
            sweetToast.setConfiguration(mConfiguration);
        }catch (Exception e){
            Log.e("幻海流心","e:"+e.getLocalizedMessage());
        }
    }
    /**
     * 根据指定的背景色,获得当前SweetToast实例中mContentView的背景drawable实例
     *  //在Android 5.0以下,直接设置 DrawableCompat.setTint 未变色:DrawableCompat.setTint(shapeDrawable,backgroundColor);
     * @param backgroundColor
     * @return
     */
    /*
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
    */
    /**
     * 根据指定的背景色,获得当前SweetToast实例中mContentView的背景drawable实例
     * @param backgroundColor
     * @return
     */
    private static Drawable getBackgroundDrawable(SweetToast sweetToast, @ColorInt int backgroundColor){
        try {
            ShapeDrawable shapeDrawable = new ShapeDrawable();
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
            //在Android 5.0以下,直接设置 DrawableCompat.setTint 未变色:DrawableCompat.setTint(shapeDrawable,backgroundColor);

            //解决:不使用DrawableCompat,直接使用 Drawable.setColorFilter(backgroundColor, PorterDuff.Mode.SRC_ATOP),
            //经测试,在4.22(山寨机)和5.1(中兴)和6.0.1(小米5s)上颜色正常显示
            shapeDrawable.setColorFilter(backgroundColor, PorterDuff.Mode.SRC_ATOP);
            return shapeDrawable;
        }catch (Exception e){
            Log.e("幻海流心","e:"+e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * 设置当前SweetToast实例的出入场动画(SDK系统内置资源)
     *      1:为什么使用SweetToastWindowAnimations:
     *          经试验，在低版本的手机上，任意设置 windowAnimations 属性值无效，查看SDK源码：
     *          //
     *          A style resource defining the animations to use for this window.
     *          This must be a system resource; it can not be an application resource
     *          because the window manager does not have access to applications.
     *          设置的出入场动画设置，必须属于系统资源，因为WindowManager无法访问App.
     *          //
     *          {@link android.view.WindowManager.LayoutParams#windowAnimations}
     *          public int windowAnimations;
     *       2:使用SweetToastWindowAnimations试验结果：
     *          4.22(山寨机)上出入场动画效果正常；
     *          5.1(中兴)上无效；
     *          6.0.1(小米5s)上（暂时无法USB安装，MIUI系统原因）效果未知
     * @param animations
     * @return
     */
    public SweetToast setWindowAnimations(@NonNull SweetToastWindowAnimations animations){
        if(animations != null){
            windowAnimations = animations.windowAnimations;
            animEnter = null;
            animExit = null;
        }
        return this;
    }

    /**
     * 设置当前SweetToast实例的出入场动画(App中自定义)
     * @param animEnter
     * @param animExit
     * @return
     */
    public SweetToast setAnimations(@AnimRes int animEnter,@AnimRes int animExit){
        try {
            Animation enter = AnimationUtils.loadAnimation(mContentView.getContext(),animEnter);
            Animation exit = AnimationUtils.loadAnimation(mContentView.getContext(),animExit);
            return setAnimations(enter,exit);
        }catch (Exception e){
            //用户传入的动画资源找不到/出现异常,直接返回
            Log.e("幻海流心","setAnimations ->  e:"+e.getLocalizedMessage());
            return this;
        }
    }

    /**
     * 设置当前SweetToast实例的出入场动画(App中自定义)
     * @param enter
     * @param exit
     * @return
     */
    public SweetToast setAnimations(Animation enter, Animation exit){
        windowAnimations = 0;
        animEnter = null;
        animExit = null;
        /*
        <style name="Animation.Toast">
            <item name="windowEnterAnimation">@anim/toast_enter</item>
            <item name="windowExitAnimation">@anim/toast_exit</item>
        </style>
        android:duration="@android:integer/config_longAnimTime"
        在SDK24中,Toast默认出入场动画的持续时间:  R.integer.config_longAnimTime:500
        */
        enter.setDuration(500);
        exit.setDuration(500);
        animEnter = enter;
        animExit = exit;
        return this;
    }

    /**
     * 设置当前SweetToast实例的对齐方式
     * @param gravity
     * @param xOffset
     * @param yOffset
     * @return
     */
    public SweetToast setGravity(int gravity, int xOffset, int yOffset) {
        mConfiguration.getParams().gravity = gravity;
        mConfiguration.getParams().x = xOffset;
        mConfiguration.getParams().y = yOffset;
        return this;
    }

    /**
     * 设置当前SweetToast实例的horizontalMargin,verticalMargin值
     * @param horizontalMargin
     * @param verticalMargin
     * @return
     */
    public SweetToast setMargin(float horizontalMargin, float verticalMargin) {
        mConfiguration.getParams().horizontalMargin = horizontalMargin;
        mConfiguration.getParams().verticalMargin = verticalMargin;
        return this;
    }
    /**
     * 向当前SweetToast实例的mContentView中添加View
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
     * 设置当前SweetToast实例中TextView的文字颜色
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
     * 设置当前SweetToast实例中mContentView的背景颜色
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
     * 设置当前SweetToast实例中mContentView的背景资源
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
     * 设置当前SweetToast实例的文字颜色及背景颜色
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
     * 设置当前SweetToast实例的文字颜色及背景资源
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
     * 设置当前SweetToast实例的最小宽高
     *  很有用的功能,参考了简书上的文章:http://www.jianshu.com/p/491b17281c0a
     * @param width     SweetToast实例的最小宽度,单位是pix
     * @param height    SweetToast实例的最小高度,单位是pix
     * @return
     */
    public SweetToast minSize(int width, int height){
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
     * 设置当前SweetToast实例的显示位置:左上
     * @return
     */
    public SweetToast leftTop(){
        return setGravity(Gravity.LEFT|Gravity.TOP,0,0);
    }
    /**
     * 设置当前SweetToast实例的显示位置:右上
     * @return
     */
    public SweetToast rightTop(){
        return setGravity(Gravity.RIGHT|Gravity.TOP,0,0);
    }
    /**
     * 设置当前SweetToast实例的显示位置:左下
     * @return
     */
    public SweetToast leftBottom(){
        return setGravity(Gravity.LEFT|Gravity.BOTTOM,0,0);
    }
    /**
     * 设置当前SweetToast实例的显示位置:右下
     * @return
     */
    public SweetToast rightBottom(){
        return setGravity(Gravity.RIGHT|Gravity.BOTTOM,0,0);
    }
    /**
     * 设置当前SweetToast实例的显示位置:上中
     * @return
     */
    public SweetToast topCenter(){
        return setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
    }
    /**
     * 设置当前SweetToast实例的显示位置:下中
     * @return
     */
    public SweetToast bottomCenter(){
        return setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
    }
    /**
     * 设置当前SweetToast实例的显示位置:左中
     * @return
     */
    public SweetToast leftCenter(){
        return setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL,0,0);
    }
    /**
     * 设置当前SweetToast实例的显示位置:右中
     * @return
     */
    public SweetToast rightCenter(){
        return setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL,0,0);
    }
    /**
     * 设置当前SweetToast实例的显示位置:正中
     * @return
     */
    public SweetToast center(){
        return setGravity(Gravity.CENTER,0,0);
    }
    /**
     * 将当前SweetToast实例显示在指定View的顶部
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
     * 将当前SweetToast实例显示在指定View的底部
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
                        mWindowManager.removeView(mRootView);
                    }
                    showing = false;
                    mRootView = null;
                    mContentView = null;
                }else{
                }
            }
        }
    }

    /**
     * 向Window中添加 内容区域View：mRootView，将SweetToast实例的内容展示出来
     * 注：
     *      SweetToast实例的动画分为两类，且两类动画互斥，有且必有其中一种会进行展示
     *          1：利用{@link android.view.WindowManager.LayoutParams#windowAnimations}，指定的系统内置出入场动画资源，
     *              见{@link SweetToast.SweetToastWindowAnimations}
     *          2：用户不满意系统内置的出入场动画资源，可以调用{@link SweetToast#setAnimations(int, int)},
     *              {@link SweetToast#setAnimations(Animation, Animation)}进行自定义
     *      两类动画的区别：
     *          1：{@link android.view.WindowManager.LayoutParams#windowAnimations}仅仅能利用系统内置的动画资源，
     *             {@link SweetToast#setAnimations(int, int)},{@link SweetToast#setAnimations(Animation, Animation)}可自定义
     *          2：{@link android.view.WindowManager.LayoutParams#windowAnimations}
     *              是针对于 mRootView 整体的出入场动画，动画展示效果更完整/不限制动画展示的位置！
     *             {@link SweetToast#setAnimations(int, int)},{@link SweetToast#setAnimations(Animation, Animation)}
     *              是针对 mContentView 的动画，mContentView 是 mRootView 的子项,所以自定义动画的可视范围会限制在 mRootView 区域内！！！
     */
    protected void handleShow() {
        if(mContentView != null){
            if(stateChangeEnabled){
                if(showEnabled){
                    try {
                        long delay = (mConfiguration.getDuration() == LENGTH_LONG || mConfiguration.getDuration() == Toast.LENGTH_LONG) ? LONG_DELAY : ((mConfiguration.getDuration() == LENGTH_SHORT || mConfiguration.getDuration() == Toast.LENGTH_SHORT)? SHORT_DELAY : mConfiguration.getDuration());
                        if(animEnter == null || animExit == null){
                            Log.e("幻海流心","animEnter == null || animExit == null");
                            /**
                             * 用户没有调用过{@link SweetToast#setAnimations(int, int)}、{@link SweetToast#setAnimations(Animation, Animation)},
                             * 或
                             * 用户调用过{@link SweetToast#setWindowAnimations(SweetToastWindowAnimations)},
                             * animEnter,animExit均为null,直接按照用户设置过的系统出入场动画资源来执行/默认出入场动画资源：android.R.style.Animation_Toast
                             */
                            animEnter = null;
                            animExit = null;
                            if(windowAnimations == 0){
                                mConfiguration.getParams().windowAnimations = android.R.style.Animation_Toast;
                            }else{
                                mConfiguration.getParams().windowAnimations = windowAnimations;
                            }
                            Log.e("幻海流心","mConfiguration.getParams().windowAnimations:"+mConfiguration.getParams().windowAnimations);
                            mWindowManager.addView(mRootView,mConfiguration.getParams());
                            mHandler.postDelayed(mHide,delay);
                        }else{
                            Log.e("幻海流心","animEnter != null && animExit != null");
                            /**
                             * 用户调用过{@link SweetToast#setAnimations(int, int)}或{@link SweetToast#setAnimations(Animation, Animation)},
                             * animEnter,animExit非空，且duration均为500ms
                             */
                            windowAnimations = 0;
                            mWindowManager.addView(mRootView,mConfiguration.getParams());
                            //SweetToast实例
                            delay = delay < 1000? 1000:delay;
                            //1:先展示入场动画
                            mContentView.startAnimation(animEnter);
                            //2:然后在handleHide()执行前展示离场动画
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(mContentView != null){
                                        mContentView.startAnimation(animExit);
                                    }
                                }
                            }, delay - 500);
                            mHandler.postDelayed(mHide,delay);
                        }
                        showing = true;
                    }catch (Exception e){
                        Log.e("幻海流心","e:"+e.getLocalizedMessage());
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
            Log.e("幻海流心","e:"+e.getLocalizedMessage());
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
            Log.e("幻海流心","e:"+e.getLocalizedMessage());
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
            Log.e("幻海流心","e:"+e.getLocalizedMessage());
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