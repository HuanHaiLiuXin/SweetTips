package com.jet.demo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.jet.sweettips.constant.Constant;
import com.jet.sweettips.snackbar.SweetSnackbar;
import com.jet.sweettips.toast.SweetToast;
import com.jet.sweettips.util.ScreenUtil;
import com.jet.sweettips.util.SnackbarUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Context context = null;
    private Button buttonBackgroundResource = null;
    private Button buttonCustomView = null;
    private Button buttonAddView = null;
    private Button buttonDuration = null;
    private Button buttonAnim = null;
    private Button buttonAnimation = null;
    private Button buttonTarget = null;
    private Button buttonUsingPrevious = null;
    private Button buttonShowImmediate = null;
    private Button buttonSize = null;
    private Button buttonBackgroundColor = null;
    private Button buttonMaxWidth = null;
    //
    private Button buttonSnackbarCustomAnim = null;
    private Button buttonSnackbarSlideInTop = null;
    private Button buttonSnackbarActionCallback = null;
    private Button buttonSnackbarUtils = null;
    //
    private Button buttonGo2 = null;

    private int durationIndex = 0;
    private String[] durationTags = new String[]{
            "Toast.LENGTH_SHORT",
            "Toast.LENGTH_LONG",
            "Custom Duration :8 seconds"
    };
    private int[] durationValues = new int[]{
            (int) SweetToast.SHORT_DELAY,
            (int) SweetToast.LONG_DELAY,
            1000 * 8
    };
    private int animIndex = 0;
    private String[] animTags = new String[]{
            "Custom Animation 1",
            "Custom Animation 2",
            "Custom Animation 3",
            "Custom Animation 4"
    };
    private SweetToast.SweetToastWindowAnimations[] animValues = new SweetToast.SweetToastWindowAnimations[]{
            SweetToast.SweetToastWindowAnimations.AnimationDialog,
            SweetToast.SweetToastWindowAnimations.AnimationTranslucent,
            SweetToast.SweetToastWindowAnimations.AnimationToast,
            SweetToast.SweetToastWindowAnimations.AnimationActivity
    };
    private int[][] animationValues = new int[][]{
            {R.anim.toast_enter,R.anim.toast_exit},
            {R.anim.scale_enter,R.anim.scale_exit},
            {R.anim.slide_in_left,R.anim.slide_out_left},
            {R.anim.scale_enter2,R.anim.scale_exit2}
    };
    private int positionIndex = 0;
    private String[] positionTags = new String[]{
            "Custom Display position :leftTop",
            "Custom Display position :rightTop",
            "Custom Display position :leftBottom",
            "Custom Display position :rightBottom",
            "Custom Display position :topCenter",
            "Custom Display position :bottomCenter",
            "Custom Display position :leftCenter",
            "Custom Display position :rightCenter",
            "Custom Display position :center",
            "Custom Display position :layoutAbove",
            "Custom Display position :layoutBellow"
    };
    private int previousIndex = 0;
    private String[] previousTags = new String[]{
            "Display content in the previous instance :1 seconds",
            "Display content in the previous instance :2 seconds",
            "Display content in the previous instance :3 seconds",
    };
    private int[] previousValues = new int[]{
            1000 * 1,
            1000 * 2,
            1000 * 3
    };
    private int sizeIndex = 0;
    private String[] sizeTags = new String[]{
            "Custom minSize : 200 * 200",
            "Custom minSize : 400 * 400",
            "Custom minSize : 600 * 600"
    };
    private int[] sizeValues = new int[]{
            200,
            400,
            600
    };
    private int colorIndex = 0;
    private String[] colorTags = new String[]{
            "Custom BackgroundColor :信息",
            "Custom BackgroundColor :确认",
            "Custom BackgroundColor :警告",
            "Custom BackgroundColor :危险",
            "Custom BackgroundColor :竹青",
            "Custom BackgroundColor :紫檀",
            "Custom BackgroundColor :蓝灰",
            "Custom BackgroundColor :水绿",
            "Custom BackgroundColor :胭脂",
            "Custom BackgroundColor :翡翠",
            "Custom BackgroundColor :天蓝",
            "Custom BackgroundColor :鲜红"
    };
    private int[] colorValues = new int[]{
            Constant.color_info,
            Constant.color_confirm,
            Constant.color_warning,
            Constant.color_danger,
            Constant.color_cn_zhuqing,
            Constant.color_cn_zitan,
            Constant.color_cn_lanhui,
            Constant.color_cn_shuilv,
            Constant.color_cn_yanzhi,
            Constant.color_cn_feicui,
            Constant.color_flyme_blue_sky,
            Constant.color_flyme_red_bright
    };
    private int snackbarAnimIndex = 0;
    private int[][] snackbarAnims = new int[][]{
            {R.anim.scale_enter,R.anim.scale_exit},
            {R.anim.slide_in_left,R.anim.slide_out_left},
            {R.anim.toast_enter_miui,R.anim.toast_exit}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        buttonBackgroundResource = (Button) findViewById(R.id.buttonBackgroundResource);
        buttonCustomView = (Button) findViewById(R.id.buttonCustomView);
        buttonAddView = (Button) findViewById(R.id.buttonAddView);
        buttonDuration = (Button) findViewById(R.id.buttonDuration);
        buttonAnim = (Button) findViewById(R.id.buttonAnim);
        buttonAnimation = (Button) findViewById(R.id.buttonAnimation);
        buttonTarget = (Button) findViewById(R.id.buttonTarget);
        buttonUsingPrevious = (Button) findViewById(R.id.buttonUsingPrevious);
        buttonShowImmediate = (Button) findViewById(R.id.buttonShowImmediate);
        buttonSize = (Button) findViewById(R.id.buttonSize);
        buttonBackgroundColor = (Button) findViewById(R.id.buttonBackgroundColor);
        buttonMaxWidth = (Button) findViewById(R.id.buttonMaxWidth);
        buttonSnackbarCustomAnim = (Button) findViewById(R.id.buttonSnackbarCustomAnim);
        buttonSnackbarSlideInTop = (Button) findViewById(R.id.buttonSnackbarSlideInTop);
        buttonSnackbarActionCallback = (Button) findViewById(R.id.buttonSnackbarActionCallback);
        buttonSnackbarUtils = (Button) findViewById(R.id.buttonSnackbarUtils);
        buttonGo2 = (Button) findViewById(R.id.buttonGo2);
        findViewById(R.id.buttonOri).setOnClickListener(this);
        buttonBackgroundResource.setOnClickListener(this);
        buttonCustomView.setOnClickListener(this);
        buttonAddView.setOnClickListener(this);
        buttonDuration.setOnClickListener(this);
        buttonAnim.setOnClickListener(this);
        buttonAnimation.setOnClickListener(this);
        buttonTarget.setOnClickListener(this);
        buttonUsingPrevious.setOnClickListener(this);
        buttonShowImmediate.setOnClickListener(this);
        buttonSize.setOnClickListener(this);
        buttonBackgroundColor.setOnClickListener(this);
        buttonMaxWidth.setOnClickListener(this);
        buttonSnackbarCustomAnim.setOnClickListener(this);
        buttonSnackbarSlideInTop.setOnClickListener(this);
        buttonSnackbarActionCallback.setOnClickListener(this);
        buttonSnackbarUtils.setOnClickListener(this);
        buttonGo2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonOri:
                //TODO implement
                Toast.makeText(context,"Original Toast : Toast.LENGTH_SHORT",Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonBackgroundResource:
                try {
                    SweetToast.makeText(context,"backgroundResource",Toast.LENGTH_SHORT).backgroundResource(R.drawable.shape_drawable1).showImmediate();
                }catch (Exception e){
                    Log.e("幻海流心","e:"+e.getLocalizedMessage());
                }
                break;
            case R.id.buttonCustomView:
                View customView = LayoutInflater.from(context).inflate(R.layout.layout_customview,null);
                SweetToast.makeText(customView).showImmediate();
                break;
            case R.id.buttonAddView:
                ImageView iv = new ImageView(context);
                iv.setImageResource(R.mipmap.ic_launcher);
                SweetToast.makeText(context,"Add View").addView(iv,0).showImmediate();
                break;
            case R.id.buttonDuration:
                //TODO implement
                for(int i = 0;i<durationTags.length;i++){
                    SweetToast.makeText(context,durationTags[durationIndex],durationValues[durationIndex]).backgroundColor(Constant.color_cn_tuoyan).show();
                    durationIndex = (durationIndex + 1)%durationTags.length;
                }
                break;
            case R.id.buttonAnim:
                //TODO implement
                SweetToast.makeText(context,animTags[animIndex],Toast.LENGTH_SHORT).setWindowAnimations(animValues[animIndex]).backgroundColor(colorValues[animIndex]).show();
                animIndex = (animIndex + 1)%animTags.length;
                break;
            case R.id.buttonAnimation:
                SweetToast.makeText(context,animTags[animIndex],Toast.LENGTH_SHORT).setAnimations(animationValues[animIndex][0],animationValues[animIndex][1]).backgroundColor(colorValues[animIndex]).show();
                animIndex = (++animIndex)%(animTags.length);
                break;
            case R.id.buttonTarget:
                //TODO implement
                //如果不是全屏,则计算状态栏的高度
                int statusHeight = ScreenUtil.getStatusHeight(MainActivity.this);
                SweetToast.makeText(context,positionTags[0], 1200).leftTop().backgroundColor(Constant.color_cn_tuoyan).show();
                SweetToast.makeText(context,positionTags[1], 1200).rightTop().backgroundColor(Constant.color_cn_tuoyan).show();
                SweetToast.makeText(context,positionTags[2], 1200).leftBottom().backgroundColor(Constant.color_cn_tuoyan).show();
                SweetToast.makeText(context,positionTags[3], 1200).rightBottom().backgroundColor(Constant.color_cn_tuoyan).show();
                SweetToast.makeText(context,positionTags[4], 1200).topCenter().backgroundColor(Constant.color_cn_tuoyan).show();
                SweetToast.makeText(context,positionTags[5], 1200).bottomCenter().backgroundColor(Constant.color_cn_tuoyan).show();
                SweetToast.makeText(context,positionTags[6], 1200).leftCenter().backgroundColor(Constant.color_cn_tuoyan).show();
                SweetToast.makeText(context,positionTags[7], 1200).rightCenter().backgroundColor(Constant.color_cn_tuoyan).show();
                SweetToast.makeText(context,positionTags[8], 1200).center().backgroundColor(Constant.color_cn_tuoyan).show();
                SweetToast.makeText(context,positionTags[9], 1200).layoutAbove(buttonTarget,statusHeight).backgroundColor(Constant.color_cn_tuoyan).show();
                SweetToast.makeText(context,positionTags[10], 1200).layoutBellow(buttonTarget,statusHeight).backgroundColor(Constant.color_cn_tuoyan).show();
                break;
            case R.id.buttonUsingPrevious:
                SweetToast.makeText(context,previousTags[previousIndex],previousValues[previousIndex]).backgroundColor(Constant.color_cn_tuoyan).showByPrevious();
                previousIndex = (previousIndex + 1)%previousTags.length;
                break;
            case R.id.buttonShowImmediate:
                //TODO implement
                SweetToast.makeText(context,"ShowImmediate",Toast.LENGTH_SHORT).backgroundColor(Constant.color_cn_tuoyan).showImmediate();
                break;
            case R.id.buttonSize:
                //TODO implement
                for(int i=0;i<sizeTags.length;i++){
                    SweetToast.makeText(context,sizeTags[sizeIndex],Toast.LENGTH_SHORT).backgroundColor(Constant.color_cn_tuoyan).minSize(sizeValues[sizeIndex],sizeValues[sizeIndex]).show();
                    sizeIndex = (sizeIndex + 1)%sizeTags.length;
                }
                break;
            case R.id.buttonBackgroundColor:
                for(int i=0;i<colorTags.length;i++){
                    SweetToast.makeText(context,colorTags[colorIndex],1200).backgroundColor(colorValues[colorIndex]).show();
                    colorIndex = (colorIndex + 1)%colorTags.length;
                }
                break;
            case R.id.buttonMaxWidth:
                SweetToast.makeText(context,"kfjklahjklfhskldfhgklshgklhsdjklhgjklshdgfhsdkldghsldhgklhsdjklhgklhsdklghklsdhgjklhsdklhkhsdkhgkfsdjklhfglsdhflghklsdhgklhsdjkghkllshdklghaklhfklahkfjklahjklfhskldfhgklshgklhsdjklhgjklshdgfhsdkldghsldhgklhsdjklhgklhsdklghklsdhgjklhsdklhkhsdkhgkfsdjklhfglsdhflghklsdhgklhsdjkghkllshdklghaklhfklah",Toast.LENGTH_LONG).showImmediate();
                break;
            case R.id.buttonSnackbarCustomAnim:
                SnackbarUtils.Long(buttonSnackbarCustomAnim,"Snackbar自定义动画").anim(snackbarAnims[snackbarAnimIndex][0],snackbarAnims[snackbarAnimIndex][1]).backColor(Constant.color_cn_zhuqing).show();
                snackbarAnimIndex = (++snackbarAnimIndex)%(snackbarAnims.length);
                break;
            case R.id.buttonSnackbarSlideInTop:
                SnackbarUtils.Long(buttonSnackbarSlideInTop,"从顶部滑入界面").gravityFrameLayout(Gravity.TOP).anim(R.anim.push_down_in,R.anim.push_up_out).backColor(Constant.color_flyme_blue_sky).show();
                break;
            case R.id.buttonSnackbarActionCallback:
                SnackbarUtils.Long(buttonSnackbarActionCallback,"设置监听").gravityFrameLayout(Gravity.CENTER).anim(R.anim.slide_in_left,R.anim.slide_out_left).backColor(Constant.color_cn_dailan).setAction("按钮", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SweetToast.makeText(context,"点击了按钮!").showImmediate();
                    }
                }).setCallback(new SweetSnackbar.Callback() {
                    @Override
                    public void onDismissed(SweetSnackbar snackbar, @DismissEvent int event) {
                        super.onDismissed(snackbar, event);
                        try {
                            SweetToast.makeText(context,"onDismissed!").showImmediate();
                        }catch (Exception e){
                            Log.e("幻海流心","e:"+e.getLocalizedMessage());
                        }
                    }
                    @Override
                    public void onShown(SweetSnackbar snackbar) {
                        super.onShown(snackbar);
                        try {
                            SweetToast.makeText(context,"onShown!").showImmediate();
                        }catch (Exception e){
                            Log.e("幻海流心","e:"+e.getLocalizedMessage());
                        }
                    }
                }).show();
                break;
            case R.id.buttonSnackbarUtils:
                int total2 = 0;
                int[] l3 = new int[2];
                getWindow().findViewById(android.R.id.content).getLocationInWindow(l3);
                total2 = l3[1];
                SnackbarUtils.Custom(buttonSnackbarUtils,"同时设置多重属性",1000*5)
                        .leftAndRightDrawable(R.mipmap.i10,R.mipmap.i11)
                        .backColor(Constant.color_cn_huanglu)
                        .radius(16,4, Color.BLUE)
                        .bellow(buttonShowImmediate,total2,16,16)
                        .show();
                break;
            case R.id.buttonGo2:
                startActivity(new Intent(context,Activity2.class));
                break;
            default:
                break;
        }
    }
}