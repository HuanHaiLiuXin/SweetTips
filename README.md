# SweetTips
快意灵动的提示库,自定义Toast,Snackbar,一行代码搞定多重属性设置!    
### 为什么写这个库
详见简书: [**SweetTips: 快意灵动的Android提示库!**](http://www.jianshu.com/p/26dfafc5410f)
Android原生Toast及Design包中的Snackbar，实现一些较常见的需求比较繁琐：  

**Toast:**
- 原生Toast无法/不方便自定义显示时间;
- 原生Toast,需要等待队列中前面的Toast实例显示完毕之后才可以显示,实时性差;
- 原生Toast,想在正在显示的Toast实例上显示新的内容并设置新内容的显示时间,实现较繁琐;
- 原生Toast,无法/不方便自定义动画;
- Android系统版本过多,不同的厂商对系统的定制也很不同,同一段代码在不同的机器上,Toast的样式差异很大,不利于App的一致性体验;

**Snackbar:**
- Design包中的Snackbar,无法自定义动画;

### 截屏
&emsp;&emsp;![](https://github.com/HuanHaiLiuXin/SweetTips/blob/master/%E5%BD%95%E5%B1%8F/SweetToast%E5%8F%8ASweetSnackbar%E6%95%88%E6%9E%9C%E5%BD%95%E5%B1%8F.gif)   

### 下载
[APK](https://github.com/HuanHaiLiuXin/SweetTips/blob/master/APK/sweetTips.apk)

### 使用
##### SweetToast:
- 创建SweetToast实例
```
SweetToast toast = SweetToast.makeText(context,"backgroundResource")；
SweetToast toast = SweetToast.makeText(context,"backgroundResource",Toast.LENGTH_SHORT)；
SweetToast toast = SweetToast.makeText(customView);
```
- 设置当前SweetToast实例的出入场动画(SDK系统内置资源)
```
SweetToast toast = SweetToast.makeText(context,"setWindowAnimations").setWindowAnimations(SweetToast.SweetToastWindowAnimations.AnimationTranslucent);
```
- 设置当前SweetToast实例的出入场动画(App中自定义)
```
SweetToast toast = SweetToast.makeText(context,"setAnimations").setAnimations(R.anim.slide_in_left,R.anim.slide_out_left);
```
- 设置当前SweetToast实例的显示位置
```
//左上
SweetToast.makeText(context,"leftTop", 1200).leftTop().show();
//右上
SweetToast.makeText(context,"rightTop", 1200).rightTop().show();
//左下
SweetToast.makeText(context,"leftBottom",1200).leftBottom().show();
//右下
SweetToast.makeText(context,"rightBottom",1200).rightBottom().show();
//上中
SweetToast.makeText(context,"topCenter",1200).topCenter().show();
//下中
SweetToast.makeText(context,"bottomCenter",1200).bottomCenter().show();
//左中
SweetToast.makeText(context,"leftCenter",1200).leftCenter().show();
//右中
SweetToast.makeText(context,"rightCenter",1200).rightCenter().show();
//正中
SweetToast.makeText(context,"center", 1200).center().show();
//指定View的上方
SweetToast.makeText(context,"layoutAbove",1200).layoutAbove(buttonTarget,statusHeight).show();
//指定View的下方
SweetToast.makeText(context,"layoutBellow",1200).layoutBellow(buttonTarget,statusHeight)show();
```
- 设置当前SweetToast实例的对齐方式
```
SweetToast.makeText(context,"setGravity").setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0).show();
```
- 设置当前SweetToast实例的horizontalMargin,verticalMargin值
```
SweetToast.makeText(context,"setMargin").setMargin(100f,32f).show();
```
- 向当前SweetToast实例的mContentView中添加View
```
ImageView iv = new ImageView(context);
iv.setImageResource(R.mipmap.ic_launcher);
SweetToast.makeText(context,"Add View").addView(iv,0).show();
```
- 设置当前SweetToast实例中TextView的文字颜色
```
SweetToast.makeText(context,"messageColor").messageColor(Color.GREEN).show();
```
- 设置当前SweetToast实例中mContentView的背景颜色
```
SweetToast.makeText(context,"backgroundColor").backgroundColor(Color.GREEN).show();
```
- 设置当前SweetToast实例的文字颜色及背景资源
```
SweetToast.makeText(context,"textColorAndBackground").textColorAndBackground(Color.GREEN,R.mipmap.ic_launcher).show();
```
- 设置当前SweetToast实例的文字颜色及背景颜色
```
SweetToast.makeText(context,"colors").colors().show(Color.GREEN,Color.BLACK);
```
- 设置当前SweetToast实例中mContentView的背景资源
```
SweetToast.makeText(context,"backgroundResource").backgroundResource(R.drawable.bg).show();
```
- 设置当前SweetToast实例的最小宽高
```
SweetToast.makeText(context,"minSize").minSize(200,160).show();
```
- 将当前实例添加到队列,若队列为空,则加入队列后直接进行展示
```
SweetToast.makeText(context,"show").show();
```
- 利用队列中正在展示的SweetToast实例,继续展示当前SweetToast实例的内容
```
SweetToast.makeText(context,"showByPrevious").showByPrevious();
```
- 清空队列中已经存在的SweetToast实例,直接展示当前SweetToast实例的内容
```
SweetToast.makeText(context,"showImmediate").showImmediate();
```

##### SweetSnackbar:
- 设置SweetSnackbar实例的 入场动画 及 离场动画
```
SnackbarUtils.Long(buttonSnackbarCustomAnim,"Snackbar自定义动画").anim(R.anim.scale_enter,R.anim.scale_exit).show();
```

##### SnackbarUtils:
- SweetSnackbar的工具类,链式调用,一行代码完成对多种属性的设置
- 效果参照[**SnackbarUtils**](https://github.com/HuanHaiLiuXin/SnackbarUtils)

### 注意:
- SweetToast实例的动画分为两类，且两类动画互斥，有且必有其中一种会进行展示
- 详见SweetToast中方法：setWindowAnimations，setAnimations

### TODO:
- 提升SweetToast出入场动画的兼容性

### 开发者:
- **幻海流心**  
- **Email:**wall0920@163.com  
- **简书:**http://www.jianshu.com/users/5702e6847f31/latest_articles  
- **GitHub:**https://github.com/HuanHaiLiuXin
