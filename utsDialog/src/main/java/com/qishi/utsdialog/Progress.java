package com.qishi.utsdialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.kongzue.dialogx.dialogs.CustomDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class Progress {
    private static WeakReference<ProgressBar> progressBarRef;

    public static CustomDialog show(Context context, JSONObject options, ProgressCallback callback) {
        // 是否可以返回取消
        boolean cancelable = options.optBoolean("cancelable", false);
        // 进度条初始进度
        int progress = options.optInt("progress", 0);
        // 进度条高度
        int height = options.optInt("height", 50);
        int topMargin = options.optInt("topMargin", 20);
        // 标题
        String title = options.optString("title", "发现新版本");
        // 内容
        String htmlString = options.optString("htmlString", "");
        // 进度条背景色
        String backgroundColor = options.optString("bgColor", "#e5e8ea");
        // 进度条颜色
        String progressColor = options.optString("color", "#0000FF");
        return CustomDialog.build()
                .setCustomView(new OnBindView<CustomDialog>(R.layout.progress_custom) {
                    @Override
                    public void onBind(final CustomDialog dialog, View v) {
                        // 最外层容器
                        LinearLayout outerContainer = v.findViewById(R.id.outer_container);
                        outerContainer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 在这里处理点击事件
//                                dialog.dismiss();
                            }
                        });

                        // 标题
                        TextView tvTitle = v.findViewById(R.id.tv_title);
                        tvTitle.setText(title);
                        // 内容区容器
                        LinearLayout containerLayout = v.findViewById(R.id.container_layout);

                        // webview
                        CustomWebView customWebView = (CustomWebView) v.findViewById(R.id.webview);
                        customWebView.loadData(htmlString, "text/html", "UTF-8");

                        // 创建并添加水平进度条
                        ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                height); // 设置高度为50px
                        params.gravity = Gravity.CENTER;
                        params.topMargin = topMargin; // 设置paddingTop为20px
                        progressBar.setLayoutParams(params);
                        progressBar.setMax(100); // 设置最大值
                        progressBar.setProgress(progress); // 设置当前进度

                        // 设置进度条背景颜色
                        ShapeDrawable background = new ShapeDrawable(new RectShape());
                        background.getPaint().setColor(Color.parseColor(backgroundColor));

                        // 设置进度颜色
                        ShapeDrawable progress = new ShapeDrawable(new RectShape());
                        progress.getPaint().setColor(Color.parseColor(progressColor));
                        ClipDrawable progressClip = new ClipDrawable(progress, Gravity.LEFT, ClipDrawable.HORIZONTAL);

                        // 合并背景和进度
                        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{background, progressClip});
                        layerDrawable.setId(0, android.R.id.background);
                        layerDrawable.setId(1, android.R.id.progress);
                        progressBar.setProgressDrawable(layerDrawable);

                        TextView btnOk = v.findViewById(R.id.btn_ok);
                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 隐藏自身按钮
                                v.setVisibility(View.GONE);
                                containerLayout.addView(progressBar);
                                callback.onConfirm();
                            }
                        });

                        // 使用弱引用持有 ProgressBar
                        progressBarRef = new WeakReference<>(progressBar);
                    }
                })
                .setMaskColor(Color.parseColor("#4D000000"))
                .show()
                .setCancelable(cancelable)
                .setAutoUnsafePlacePadding(false);
    }

    public static void updateProgress(int progress) {
        if (progressBarRef != null) {
            ProgressBar progressBar = progressBarRef.get();
            if (progressBar != null) {
                progressBar.setProgress(progress);
            }
        }
    }

}
