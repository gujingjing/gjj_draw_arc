项目最近需要添加一个体质监测模块，需要用到渐变的圆弧，先上效果图


![体质监测.png](http://upload-images.jianshu.io/upload_images/1387450-5efe561071405012.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

下面，讲讲我个人的绘制心得。

其实这个功能并不是很难，首先我们新建一个ProgressViewNew,在onMeasure()方法中设置控件的大小,代码里的注解应该很详细

    @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec, false));
        }
    /**
     *
     * @param origin
     * @param isWidth
     * @return
     */
    private int measure(int origin, boolean isWidth){
        int result = DEFAULT_MIN_WIDTH;
        int specMode = MeasureSpec.getMode(origin);//得到模式
        int specSize = MeasureSpec.getSize(origin);//得到尺寸

        switch (specMode) {
            //EXACTLY是精确尺寸，当我们将控件的layout_width或layout_height指定为具体数值时如andorid:layout_width="50dip"，或者为FILL_PARENT是，都是控件大小已经确定的情况，都是精确尺寸。
            case MeasureSpec.EXACTLY:
                //AT_MOST是最大尺寸，当控件的layout_width或layout_height指定为WRAP_CONTENT时，控件大小一般随着控件的子空间或内容进行变化，此时控件尺寸只要不超过父控件允许的最大尺寸即可
            case MeasureSpec.AT_MOST:
                result = specSize;
                if (isWidth) {
    //                    widthForUnspecified = result;
                } else {
    //                    heightForUnspecified = result;
                }
                break;
            //UNSPECIFIED是未指定尺寸，这种情况不多，一般都是父控件是AdapterView，通过measure方法传入的模式。
            case MeasureSpec.UNSPECIFIED:
            default:
                result = Math.min(result, specSize);
                if (isWidth) {//宽或高未指定的情况下，可以由另一端推算出来 - -如果两边都没指定就用默认值
    //                    result = (int) (heightForUnspecified * BODY_WIDTH_HEIGHT_SCALE);
                } else {
    //                    result = (int) (widthForUnspecified / BODY_WIDTH_HEIGHT_SCALE);
                }
                if (result == 0) {
                    result = DEFAULT_MIN_WIDTH;
                }
                break;
        }

        return result;
    }


####下面就是主要的核心代码了，onDraw（）方法中进行绘制

#####1. 绘制默认灰色的圆弧

        //1------绘制默认灰色的圆弧
        float r = Math.min(getHeight() / 2, getWidth() / 2);//半径
        Paint paintDefalt = new Paint();
        paintDefalt.setColor(getResources().getColor(R.color.colordefault));
        //设置笔刷的样式 Paint.Cap.Round ,Cap.SQUARE等分别为圆形、方形
        paintDefalt.setStrokeCap(Paint.Cap.SQUARE);
        float borderWidth = DEFAULT_BORDER_WIDTH;//圆弧宽度

        float centerX=(getHeight()-2*marging) / 2;
        //        RectF oval1=new RectF(marging,marging,getWidth()-marging,getHeight()-marging);
          RectF oval1=new RectF(marging,marging,2*centerX+marging,2*centerX+marging);
        /**
         * Paint.Style.FILL    :填充内部
         Paint.Style.FILL_AND_STROKE  ：填充内部和描边
         Paint.Style.STROKE  ：仅描边
         */
        paintDefalt.setStyle(Paint.Style.STROKE);//设置填充样式
        paintDefalt.setAntiAlias(true);//抗锯齿功能
        paintDefalt.setStrokeWidth(borderWidth);//设置画笔宽度
        //        该类是第二个参数是角度的开始，第三个参数是多少度
        //        canvas.drawCircle(getWidth() / 2, getHeight() / 2, r, paintCircle);//画圆,圆心在中心位置,半径为长宽小者的一半

        /**
         * drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint)//画弧，
         参数一是RectF对象，一个矩形区域椭圆形的界限用于定义在形状、大小、电弧，
         参数二是起始角(度)在电弧的开始，
         参数三扫描角(度)开始顺时针测量的，参数四是如果这是真的话,包括椭圆中心的电弧,并关闭它,如果它是假这将是一个弧线,参数五是Paint对象；
         */
        //绘制默认灰色的圆弧
        canvas.drawArc(oval1, 180, 180, false, paintDefalt);//小弧形
        //        参数一为渐变起初点坐标x位置，参数二为y轴位置，参数三和四分辨对应渐变终点，最后参数为平铺方式，这里设置为镜像.


#####2. 绘制当前进度的圆弧

        //2-----绘制当前进度的圆弧
        Paint paintCurrent = new Paint();
        paintCurrent.setStyle(Paint.Style.STROKE);//设置填充样式
        paintCurrent.setAntiAlias(true);//抗锯齿功能
        paintCurrent.setStrokeWidth(borderWidth);//设置画笔宽度
        //设置笔刷的样式 Paint.Cap.Round ,Cap.SQUARE等分别为圆形、方形
        paintCurrent.setStrokeCap(Paint.Cap.SQUARE);
        /**
         * static final Shader.TileMode CLAMP: 边缘拉伸.
         static final Shader.TileMode MIRROR：在水平方向和垂直方向交替景象, 两个相邻图像间没有缝隙.
         Static final Shader.TillMode REPETA：在水平方向和垂直方向重复摆放,两个相邻图像间有缝隙缝隙.
         */

        LinearGradient lg=new LinearGradient(0,0,100,100,colors,positions, Shader.TileMode.MIRROR);  //渐变颜色
        // 创建SweepGradient对象
        // 第一个,第二个参数中心坐标
        // 后面的参数与线性渲染相同

        //18.5/24.0/28.0/35.0
        positions[0]=Float.parseFloat(df.format(position_line[0]/maxCount));
        positions[1]=Float.parseFloat(df.format(position_line[1]/maxCount));
        positions[2]=Float.parseFloat(df.format(position_line[2]/maxCount));
        positions[3]=Float.parseFloat(df.format(position_line[3]/maxCount));
        positions[4]=Float.parseFloat(df.format(position_line[4]/maxCount));

        SweepGradient sweepGradient = new SweepGradient(centerX+padding, centerX+padding, colors, positions);
        Matrix matrix = new Matrix();
        matrix.setRotate(130, centerX, centerX);//加上旋转还是很有必要的，每次最右边总是有一部分多余了,不太美观,也可以不加
        sweepGradient.setLocalMatrix(matrix);
        paintCurrent.setShader(sweepGradient);

        //        canvas.drawArc(oval1, 180, currentAngle, false, paintCurrent);//小弧形
        //当前进度
        canvas.drawArc(oval1, startAngle, currentAngle, false, paintCurrent);

###注意:应为圆弧是根据不同的数值而变化的，所以里面加入了，每个color显示对应的区域点
     //18.5/24.0/28.0/35.0
        positions[0]=Float.parseFloat(df.format(position_line[0]/maxCount));
        positions[1]=Float.parseFloat(df.format(position_line[1]/maxCount));
        positions[2]=Float.parseFloat(df.format(position_line[2]/maxCount));
        positions[3]=Float.parseFloat(df.format(position_line[3]/maxCount));
        positions[4]=Float.parseFloat(df.format(position_line[4]/maxCount));


#####4.绘制相关文字

        //3----文字
        //内容显示文字
        Paint vTextPaint = new Paint();
        vTextPaint.setTextSize(textSize);
        vTextPaint.setColor(Color.BLACK);
        vTextPaint.setAntiAlias(true);//抗锯齿功能
    //        vTextPaint.setStrokeWidth((float) 3.0);
        vTextPaint.setTextAlign(Paint.Align.CENTER);

        //TODO 等待修改
        canvas.drawText("瘦", marging, centerX+marging+textSize+padding, vTextPaint);
        canvas.drawText("胖", getWidth()-marging, centerX+marging+textSize+padding, vTextPaint);
        //圆环中心的文字
        vTextPaint.setTextSize(BIM_textSize);
        canvas.drawText("BMI指数", getWidth()/2, (int)((centerX+marging)/3*1.5+textSize), vTextPaint);

        //设置肥胖指数
        /**
         * 常用的字体风格名称还有：
         * Typeface.BOLD //粗体
         * Typeface.BOLD_ITALIC //粗斜体
         * Typeface.ITALIC //斜体
         * Typeface.NORMAL //常规
         */
        vTextPaint.setTextSize(Number_textSize);
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        vTextPaint.setTypeface( font );

        canvas.drawText(PANG_NUMBER, getWidth()/2, (int)((centerX+marging)/3*2.5+textSize), vTextPaint);



#####5.最后在绘制圆上的小点，就完成了啊

这里绘制园上的点有两种方法：
1. 也就是本文中用到的，把画布旋转相应的角度，绘制点，然后，在旋转到原来的位置，就可以了，
   为什么要这么做呢，因为我们绘制的是一个圆，之前也想通过计算来绘制的，但是一直不太准确，最后，使用了这种方法
 
2. 第二种就是通过计算啦，已知一个圆的圆心，半径，以及旋转的角度，求圆上的一点，哈哈，应该还是挺好算的呀

        //4---绘制圆弧上的小圆球--根据currentAngle
        /**
         * Paint.Style.FILL    :填充内部
         Paint.Style.FILL_AND_STROKE  ：填充内部和描边
         Paint.Style.STROKE  ：仅描边
         */
        canvas.translate(getWidth()/2, getHeight()/2);//这时候的画布已经移动到了中心位置
        Paint paintCircle = new Paint();
        paintCircle.setStyle(Paint.Style.FILL);//设置填充样式
        paintCircle.setAntiAlias(true);//抗锯齿功能
        paintCircle.setColor(Color.WHITE);
        //        paintCircle.setStrokeWidth(borderLitalWidth);//设置画笔宽度

        //            canvas.drawCircle((float)(centerX+padding-centerX*Math.cos(currentAngle)), (float)(centerX+padding-centerX*Math.sin(currentAngle)), DEFAULT_LITLE_WIDTH, paintCircle);//画圆,圆心在中心位置,半径为长宽小者的一半
        //        canvas.drawCircle(0, 0, DEFAULT_LITLE_WIDTH, paintCircle);//这时候的画布已经移动到了中心位置
        /**
        * 第一个参数为正则顺时针，否则逆时针
        * 后面两个参数是圆心
        * 画布的旋转一定要在，画图形之前进行旋转
        */
        //        canvas.rotate(-currentAngle, centerX+padding, centerX+padding);//以圆心旋转的
        //        canvas.drawCircle(padding,centerX+padding,DEFAULT_LITLE_WIDTH,paintCircle);
        //        canvas.rotate(currentAngle, centerX+padding, centerX+padding);//以圆心旋转的
        canvas.rotate(currentAngle);
        canvas.drawCircle(-centerX,0,DEFAULT_LITLE_WIDTH,paintCircle);
        canvas.rotate(-currentAngle);

###最后在加上动画就ok啦

/**
     * 为进度设置动画
     * @param last
     * @param current
     */
    private void setAnimation(float last, float current, int length) {
        progressAnimator = ValueAnimator.ofFloat(last, current);
        Log.e("last=====",last+"");
        Log.e("current=====",current+"");
        progressAnimator.setDuration(length);
    //        progressAnimator.setTarget(currentAngle);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentAngle= (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        progressAnimator.start();
    }

注意:

        第一个参数是需要动画的起始位置角度，
        第二个参数是最终的位置角度,
        第三个参数是，运动的速度

项目已经上传到[github](https://github.com/gujingjing/gjj_draw_arc.git)上了,欢迎大家start和fork
