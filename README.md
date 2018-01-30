  # CenterFlowLayout
  让你的子View居中显示，并且会在没有足够空间的时候，显示在下一行。
  
 <p align="center">
<img src="images/1.png" width="360"/>
 </p>

 ## 使用方法
 ```
  <com.zw.centerflowlayout.CenterFlowLayout
            android:id="@+id/id_custom_tag_group1"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            >

            <android.support.v7.widget.AppCompatCheckBox
                android:layout_width="wrap_content"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="再别康桥"
                />

            <android.support.v7.widget.AppCompatCheckBox
                android:layout_width="wrap_content"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="你曾是少年"
                />
   </com.zw.centerflowlayout.CenterFlowLayout>
   
   或者
  
        for(int i=0;i< str.length;i++) {
            TextView tv= (TextView) LayoutInflater.from(this).inflate(R.layout.custom_checked_item_view, null,false);
            tv.setText(str[i]);
            tagGroup.addView(tv);
        }
        tagGroup.setChildSpacing(dp2px(this,10));
        tagGroup.setRowSpacing(dp2px(this,20));
   
 ```
 这里我使用的是`android.support.v7.widget.AppCompatCheckBox`，你可以替换自己想要的控件（TextView、CheckBox等等）。
# 属性介绍
| Attribute                | Format                       | Description                                                                                                                                          |
|--------------------------|------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------|
| childSpacing                   | dimension                      | 子View之间的间距                                                         |
| rowSpacing           | dimension             | 子View的marginTop值   

ps:具体使用方法请查看Demo

