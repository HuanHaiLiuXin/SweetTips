package com.jet.sweettips.toast;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jet.sweettips.R;

import java.util.LinkedList;

/**
 * SweetToast实例管理者
 *
 * 作者:幻海流心
 * GitHub:https://github.com/HuanHaiLiuXin
 * 邮箱:wall0920@163.com
 * 2016/12/23
 */

public final class SweetToastManager {
    private static LinkedList<SweetToast> queue = new LinkedList<SweetToast>();
    private static Handler queueHandler = new Handler();
    private static Runnable mShowNext = new Runnable() {
        @Override
        public void run() {
            if(queue.size() > 0){
                SweetToast previous = poll();
                previous.handleHide();
                if(queue.size() > 0){
                    SweetToast current = peek();
                    current.handleShow();
                    long delay = (current.getConfiguration().getDuration() == SweetToast.LENGTH_LONG||current.getConfiguration().getDuration() == Toast.LENGTH_LONG) ? SweetToast.LONG_DELAY : ((current.getConfiguration().getDuration() == SweetToast.LENGTH_SHORT || current.getConfiguration().getDuration() == Toast.LENGTH_SHORT)? SweetToast.SHORT_DELAY : current.getConfiguration().getDuration());
                    queueHandler.postDelayed(mShowNext,delay);
                }
            }
        }
    };
    private static SweetToast singleToast = null;
    private static Handler singleHandler = new Handler();
    private static long singleHideTimeMillis = 0L;

    private static void offer(SweetToast sweetToast){
        if(queue != null){
            queue.offer(sweetToast);
        }
    }
    private static SweetToast poll(){
        if(queue.size() > 0){
            return queue.poll();
        }
        return null;
    }
    private static SweetToast peek(){
        if(queue.size() > 0){
            return queue.peek();
        }
        return null;
    }
    private static void clear(){
        if(queue.size() > 0){
            while (queue.peek() != null){
                SweetToast item = queue.poll();
                item.setHideEnabled(true);
                item.handleHide();
                item = null;
            }
        }
        if(singleToast != null){
            singleToast.setHideEnabled(true);
            singleToast.handleHide();
            singleToast = null;
        }
    }
    private static void clearQueue(){
        if(queue.size() > 0){
            while (queue.peek() != null){
                SweetToast item = queue.poll();
                item.setHideEnabled(true);
                item.handleHide();
                item = null;
            }
        }
    }
    /**
     * 将当前SweetToast实例添加到queue中
     */
    protected static void show(@NonNull SweetToast current){
        if(queue.size() <= 0){
            clear();
            //队列为空,则将current添加到队列中,同时进行展示
            offer(current);
            current.handleShow();
            long delay = (current.getConfiguration().getDuration() == SweetToast.LENGTH_LONG||current.getConfiguration().getDuration() == Toast.LENGTH_LONG) ? SweetToast.LONG_DELAY : ((current.getConfiguration().getDuration() == SweetToast.LENGTH_SHORT || current.getConfiguration().getDuration() == Toast.LENGTH_SHORT)? SweetToast.SHORT_DELAY : current.getConfiguration().getDuration());
            queueHandler.postDelayed(mShowNext,delay);
        }else{
            offer(current);
        }
    }

    /**
     * 重用queue中的正在显示的SweetToast实例,直接更新显示的内容
     * @param current
     */
    protected static void showByPrevious(@NonNull SweetToast current){
        try {
            clearQueue();
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    if(System.currentTimeMillis() >= singleHideTimeMillis && singleToast != null){
                        singleToast.setHideEnabled(true);
                        singleToast.handleHide();
                    }
                }
            };
            if(singleToast != null && singleToast.isShowing()){
                singleToast.setHideEnabled(false);
                //暂时仅仅支持默认样式的SweetToast实例间的重用
                if(singleToast.getContentView() instanceof LinearLayout && singleToast.getContentView().findViewById(R.id.message) != null && current.getContentView() instanceof LinearLayout && current.getContentView().findViewById(R.id.message) != null){
                    singleToast.setConfiguration(current.getConfiguration());
                    TextView textView = (TextView) singleToast.getContentView().findViewById(R.id.message);
                    TextView content = (TextView) current.getContentView().findViewById(R.id.message);
                    textView.setText(content.getText());
                }
            }else {
                singleToast = null;
                singleToast = current;
                singleToast.setHideEnabled(false);
                singleToast.handleShow();
            }
            long delay = (current.getConfiguration().getDuration() == SweetToast.LENGTH_LONG||current.getConfiguration().getDuration() == Toast.LENGTH_LONG) ? SweetToast.LONG_DELAY : ((current.getConfiguration().getDuration() == SweetToast.LENGTH_SHORT || current.getConfiguration().getDuration() == Toast.LENGTH_SHORT)? SweetToast.SHORT_DELAY : current.getConfiguration().getDuration());
            singleHideTimeMillis = delay + System.currentTimeMillis();
            singleHandler.postDelayed(r,delay);
        }catch (Exception e){
            Log.e("Jet","e:"+e.getLocalizedMessage()+":146");
        }
    }
    protected static void showImmediate(@NonNull SweetToast current){
        clear();
        show(current);
    }
}
