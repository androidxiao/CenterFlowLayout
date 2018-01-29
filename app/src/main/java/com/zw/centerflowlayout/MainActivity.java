package com.zw.centerflowlayout;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String[]str={"再别康桥","你曾是少年","不想长大","给我一首歌的时间","告白气球","算什么男人","稻香","简单爱","七里香","等你下课","蒲公英的约定","追光者","你的样子","慢慢慢","青花瓷"};
    int count=str.length;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        final CenterFlowLayout tagGroup=findViewById(R.id.id_custom_tag_group);

        for(int i=0;i< str.length;i++) {
            TextView tv= (TextView) LayoutInflater.from(this).inflate(R.layout.custom_checked_item_view, null,false);
            tv.setText(str[i]);
            tagGroup.addView(tv);
        }
        tagGroup.setChildSpacing(dp2px(this,10));
        tagGroup.setRowSpacing(dp2px(this,20));

    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
