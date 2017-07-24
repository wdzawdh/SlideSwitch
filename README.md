### Android的滑块开关网上已经有很多，我找了好多但是我总觉得都不实用，而且实现也不够简洁不利于维护。于是我把自己以前写的Demo拿出来改了一下使用,也分享给需要的小伙伴，希望对别人有所启发。里面包含自定义View的知识点包括：根据系统测量结果确定自己想要的大小、处理与父布局的事件冲突、使用Scroller实现平滑移动等等。代码量很少，应该还是很好理解的。

## 每天都要过得开心 ( ゜- ゜)つロ乾杯 ！


### 效果图


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
sv_but.setOnSlideButtonChangeListener(
                  new SlideButton.OnSlideButtonChangeListener() {
    @Override
    public void onButtonChange(SlideButton view, boolean isOpen) {

    }
});
```

### 关键代码
#### 1.onMeasure
> 当发现宽SpecMode为MeasureSpec.EXACTLY时（也就是具体尺寸或match_parent）就根据宽高比算出高是多少。当宽高都是MeasureSpec.EXACTLY时就按系统测量的原尺寸设置测量宽高（setMeasuredDimension方法）。当没有MeasureSpec.EXACTLY时（也就是宽高都是wrap_content）就根据图片的宽高设置测量宽高。

```
//计算宽高比
float v = (float) mBackground.getWidth() / mBackground.getHeight();
if (widthSpecMode == MeasureSpec.EXACTLY
        && heightSpecMode == MeasureSpec.EXACTLY) {
    setMeasuredDimension(widthSpecSize, heightSpecSize);
} else if (widthSpecMode == MeasureSpec.EXACTLY) {
    setMeasuredDimension(widthSpecSize, (int) (widthSpecSize / v));
} else if (heightSpecMode == MeasureSpec.EXACTLY) {
    setMeasuredDimension((int) (widthSpecSize * v), heightSpecMode);
} else {
    setMeasuredDimension(mBackground.getWidth(), mBackground.getHeight());
}
```
#### 2.onSizeChanged
> ViewGroup给View分配的大小发生变化时，会回调View的onSizeChanged方法,onMeasure结束之后也会调用，所以在onSizeChanged中设置Bitmap的大小。对Bitmap进行等比例缩放，以测量长度与原图长度相差较小的一边为基准缩放bitmap。

```
if (Math.abs(w - mBackground.getWidth()) <
                Math.abs(h - mBackground.getHeight())) {
    setBitMapSize((float) w / mBackground.getWidth());
} else {
    setBitMapSize((float) h / mBackground.getHeight());
}
```
#### 3.dispatchTouchEvent
> 请求父容器不要拦截事件。由于View的dispatchTouchEvent会把事件直接交给自己的OnTouchEvent方法，所以调用super.dispatchTouchEvent即可。

```
@Override
public boolean dispatchTouchEvent(MotionEvent event) {
    getParent().requestDisallowInterceptTouchEvent(true);
    return super.dispatchTouchEvent(event);
}
```

#### PS: 如果有小伙伴觉得我写的有问题或者有优化的地方欢迎提出，一定虚心改进。
