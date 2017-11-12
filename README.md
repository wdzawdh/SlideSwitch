## Android的滑块开关，可替换样式，可控制大小。


### 效果图
<img src="http://otjav6lvw.bkt.clouddn.com/80966843.jpg" width="400"/>

### 使用方法
#### 有四个属性：打开情况的背景图、关闭情况下的背景图、滑块图、默认开关状态。都有默认值，默认就是金黄背景白色滑块。
```
<com.cw.slideswitch.view.SlideButton
    android:id="@+id/sv_but"
    android:layout_width="25dp"
    android:layout_height="wrap_content"
    android:layout_marginTop="10dp"
    app:openBackground="@drawable/widget_icon_slidebutton_yellow_bg"
    app:closeBackground="@drawable/widget_icon_slidebutton_write_bg"
    app:slideImage="@drawable/widget_icon_slidebutton_write_slider"
    app:isOpen="true"/>

SlideButton sv_but = (SlideButton) findViewById(R.id.sv_but);
sv_but.setOnSlideButtonChangeListener(new SlideButton.OnSlideButtonChangeListener() {
    @Override
    public void onButtonChange(SlideButton view, boolean isOpen) {
        //监听开关状态
    }
});
```
