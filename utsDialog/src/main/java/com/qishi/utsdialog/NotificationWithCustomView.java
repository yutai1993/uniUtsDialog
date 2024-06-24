package com.qishi.utsdialog;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.kongzue.dialogx.dialogs.PopNotification;
import com.kongzue.dialogx.interfaces.OnBindView;

public class NotificationWithCustomView {

    public static void show(Context context, String url, String title, String message, String btnText, String btnColor, final View.OnClickListener replyButtonClickListener) {
        PopNotification.build()
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
                                dialog.dismiss();
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
}
