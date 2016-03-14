package gjj_unit_test.gjj_draw_arc;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

/**
 * 作者：gjj on 2016/3/14 11:18
 * 邮箱：Gujj512@163.com
 */
public class ProgressViewNew2 extends View {

    private static final int DEFAULT_MIN_WIDTH = 200; //View默认大小
    //圆弧的宽度
    private static final float DEFAULT_BORDER_WIDTH = 10f;
    //小原点的半径
    private int  DEFAULT_LITLE_WIDTH = dipToPx(5);

    private ValueAnimator progressAnimator;
    private float textSize = dipToPx(18);
    private int padding=dipToPx(10);//默认胖和瘦距离上面圆环的距离
    private int marging=(int)(Math.max(DEFAULT_BORDER_WIDTH,textSize));
    private float currentAngle=150;
    //分段颜色
//    private int[] colors = new int[]{Color.GREEN, Color.YELLOW, Color.RED, Color.RED};
    private int[] colors = new int[]{getResources().getColor(R.color.colorAccent1),
            getResources().getColor(R.color.colorAccent2),
            getResources().getColor(R.color.colorAccent3)};
    //当前的分数
    private float maxCount=50;
    private float currentCount=30;

    public ProgressViewNew2(Context context) {
        super(context);
    }

    public ProgressViewNew2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //1------绘制默认灰色的圆弧
        float r = Math.min(getHeight() / 2, getWidth() / 2);//半径
        Paint paintDefalt = new Paint();
        paintDefalt.setColor(Color.LTGRAY);
        //设置笔刷的样式 Paint.Cap.Round ,Cap.SQUARE等分别为圆形、方形
        paintDefalt.setStrokeCap(Paint.Cap.SQUARE);
        float borderWidth = DEFAULT_BORDER_WIDTH;//圆弧宽度

        RectF oval1=new RectF(marging,marging,getWidth()-marging,getHeight()-marging);
        float centerX=(getHeight()-2*marging) / 2, centerY=(getWidth()-2*marging) / 2;

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
        LinearGradient lg=new LinearGradient(0,0,100,100,colors,null, Shader.TileMode.MIRROR);  //渐变颜色
        // 创建SweepGradient对象
        // 第一个,第二个参数中心坐标
        // 后面的参数与线性渲染相同
        SweepGradient mSweepGradient = new SweepGradient(centerX, centerY, new int[] {Color.CYAN,Color.DKGRAY,Color.GRAY,Color.LTGRAY,Color.MAGENTA,
                Color.GREEN,Color.TRANSPARENT, Color.BLUE }, null);
//        paintCurrent.setShader(mSweepGradient);//渐变颜色--设置

        SweepGradient sweepGradient = new SweepGradient(centerX, centerY, colors, null);
        Matrix matrix = new Matrix();
        matrix.setRotate(130, centerX, centerY);//加上旋转还是很有必要的，每次最右边总是有一部分多余了,不太美观,也可以不加
        sweepGradient.setLocalMatrix(matrix);
        paintCurrent.setShader(sweepGradient);

        canvas.drawArc(oval1, 180, currentAngle, false, paintCurrent);//小弧形

        //3----文字
        //内容显示文字
        Paint vTextPaint = new Paint();
        vTextPaint.setTextSize(textSize);
        vTextPaint.setColor(Color.BLACK);
        vTextPaint.setAntiAlias(true);//抗锯齿功能
//        vTextPaint.setStrokeWidth((float) 3.0);
        vTextPaint.setTextAlign(Paint.Align.CENTER);

        //TODO 等待修改
        canvas.drawText("瘦", marging, centerY+marging+textSize+padding, vTextPaint);
        canvas.drawText("胖", getWidth()-marging, centerY+marging+textSize+padding, vTextPaint);
        //圆环中心的文字
        canvas.drawText("BMI指数", getWidth()/2, (int)((centerY+marging)/3*1.5+textSize), vTextPaint);
        canvas.drawText("31.5", getWidth()/2, (int)((centerY+marging)/3*2.5+textSize), vTextPaint);

        //4---绘制圆弧上的小圆球--根据currentAngle
        /**
        * Paint.Style.FILL    :填充内部
         Paint.Style.FILL_AND_STROKE  ：填充内部和描边
        Paint.Style.STROKE  ：仅描边
        */
//        canvas.translate(getWidth()/2, getHeight()/2);//这时候的画布已经移动到了中心位置
        Paint paintCircle = new Paint();
        paintCircle.setStyle(Paint.Style.FILL);//设置填充样式
        paintCircle.setAntiAlias(true);//抗锯齿功能
        paintCircle.setColor(Color.RED);
//        paintCircle.setStrokeWidth(borderLitalWidth);//设置画笔宽度
        canvas.drawCircle((float)(centerX*Math.cos(currentAngle)), (float)(centerX*Math.sin(currentAngle)), DEFAULT_LITLE_WIDTH, paintCircle);//画圆,圆心在中心位置,半径为长宽小者的一半
        canvas.drawCircle(0, 0, DEFAULT_LITLE_WIDTH, paintCircle);//这时候的画布已经移动到了中心位置
    }

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
    /**
     * dip 转换成px
     * @param dip
     * @return
     */
    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int)(dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

}
