package com.qishi.utsdialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kongzue.dialogx.dialogs.PopNotification;
import com.kongzue.dialogx.interfaces.OnBindView;

public class NotificationWithCustomView {
    private static PopNotification notification;

   /* public static void show(Context context, String url, String title, String message, String btnText, String btnColor, final View.OnClickListener replyButtonClickListener) {
        notification = PopNotification.build()
                .setCustomView(new OnBindView<PopNotification>(R.layout.notification_custom_view) {
                    @Override
                    public void onBind(PopNotification dialog, View v) {
                        ImageView imageView = v.findViewById(R.id.user_avatar);
                        Glide.with(context)
                                .load(url)
                                .circleCrop()
                                .into(imageView);

                        TextView titleView = v.findViewById(R.id.notification_title);
                        titleView.setText(title);

                        TextView textView = v.findViewById(R.id.notification_text);
                        textView.setText(message);

                        Button replyButton = v.findViewById(R.id.reply_button);
                        replyButton.setTextColor(Color.parseColor(btnColor)); // 设置按钮的文字颜色
                        replyButton.setText(btnText);
                        replyButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dismiss();
                                if (replyButtonClickListener != null) {
                                    replyButtonClickListener.onClick(v);
                                }
                            }
                        });
                    }
                })
                .show()
                .showLong();
    }
*/
   public static void show(Context context, String url, String title, String message, String btnText, String btnColor, final View.OnClickListener replyButtonClickListener) {
       notification = PopNotification.build()
               .setCustomView(new OnBindView<PopNotification>(R.layout.notification_custom_view) {
                   @Override
                   public void onBind(PopNotification dialog, View v) {
                       ImageView imageView = v.findViewById(R.id.user_avatar);

                       // 使用Glide加载图片，并在加载成功后显示通知
                       Glide.with(context)
                               .load(url)
                               .circleCrop()
                               .listener(new RequestListener<Drawable>() {
                                   @Override
                                   public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                       // 图片加载失败的处理
                                       return false; // 返回false让Glide处理错误的占位符
                                   }

                                   @Override
                                   public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                       // 图片加载成功，显示通知
                                       dialog.showLong().show(); // 显示通知
                                       return false; // 返回false让Glide处理图片的显示
                                   }
                               })
                               .into(imageView);

                       TextView titleView = v.findViewById(R.id.notification_title);
                       titleView.setText(title);

                       TextView textView = v.findViewById(R.id.notification_text);
                       textView.setText(message);

                       Button replyButton = v.findViewById(R.id.reply_button);
                       replyButton.setTextColor(Color.parseColor(btnColor)); // 设置按钮的文字颜色
                       replyButton.setText(btnText);
                       replyButton.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               dismiss(); // 注意这里的dismiss调用
                               if (replyButtonClickListener != null) {
                                   replyButtonClickListener.onClick(v);
                               }
                           }
                       });
                   }
               });

       // 注意：由于Glide的加载是异步的，所以不在这里调用.show()或.showLong()
       // 这些操作已移至Glide的onResourceReady回调中
   }
    public static void dismiss() {
        if (notification != null) {
            notification.dismiss();
            notification = null;
        }
    }
}
