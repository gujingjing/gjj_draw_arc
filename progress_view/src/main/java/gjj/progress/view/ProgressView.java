package gjj.progress.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/***
 * 自定义圆弧进度条
 * 
 * @author liujing
 */
public class ProgressView extends View {

	//分段颜色 
	private static final int[] SECTION_COLORS = { Color.GREEN, Color.YELLOW,
			Color.RED };
	private static final String[] ALARM_LEVEL = { "安全", "低危", "中危", "高危" };
	private float maxCount;
	private float currentCount;
	private int score;
	private String crrentLevel;
	private Paint mPaint;
	private Paint mTextPaint;
	private int mWidth, mHeight;

	public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public ProgressView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ProgressView(Context context) {
		this(context, null);
	}

	private void init(Context context) {
		mPaint = new Paint();
		mTextPaint = new Paint();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		initPaint();
		RectF rectBlackBg = new RectF(20, 20, mWidth - 20, mHeight - 20);
		canvas.drawArc(rectBlackBg, 0, 360, false, mPaint);
		mPaint.setColor(Color.BLACK);
		canvas.drawText(score + "分", mWidth / 2, mHeight / 2, mTextPaint);
		mTextPaint.setTextSize(40);
		if (crrentLevel != null) {
			canvas.drawText(crrentLevel, mWidth / 2, mHeight / 2 + 50,
					mTextPaint);
		}
		float section = currentCount / maxCount;
		if (section <= 1.0f / 3.0f) {
			if (section != 0.0f) {
				mPaint.setColor(SECTION_COLORS[0]);
			} else {
				mPaint.setColor(Color.TRANSPARENT);
			}
		} else {
			int count = (section <= 1.0f / 3.0f * 2) ? 2 : 3;
			int[] colors = new int[count];
			System.arraycopy(SECTION_COLORS, 0, colors, 0, count);
			float[] positions = new float[count];
			if (count == 2) {
				positions[0] = 0.0f;
				positions[1] = 1.0f - positions[0];
			} else {
				positions[0] = 0.0f;
				positions[1] = (maxCount / 3) / currentCount;
				positions[2] = 1.0f - positions[0] * 2;
			}
			positions[positions.length - 1] = 1.0f;
			LinearGradient shader = new LinearGradient(3, 3, (mWidth - 3)
					* section, mHeight - 3, colors, null,
					Shader.TileMode.MIRROR);
			mPaint.setShader(shader);
		}
		canvas.drawArc(rectBlackBg, 180, section * 360, false, mPaint);
	}

	private void initPaint() {
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth((float) 40.0);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeCap(Cap.ROUND);
		mPaint.setColor(Color.TRANSPARENT);

		mTextPaint.setAntiAlias(true);
		mTextPaint.setStrokeWidth((float) 3.0);
		mTextPaint.setTextAlign(Paint.Align.CENTER);
		mTextPaint.setTextSize(50);
		mTextPaint.setColor(Color.BLACK);

	}

	private int dipToPx(int dip) {
		float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
	}

	public int getScore() {
		return score;
	}

	public String getCrrentLevel() {
		return crrentLevel;
	}

	public void setCrrentLevel(String crrentLevel) {
		this.crrentLevel = crrentLevel;
	}

	public float getMaxCount() {
		return maxCount;
	}

	public float getCurrentCount() {
		return currentCount;
	}

	public void setScore(int score) {
		this.score = score;
		if (score == 100) {
			this.crrentLevel = ALARM_LEVEL[0];
		} else if (score >= 70 && score < 100) {
			this.crrentLevel = ALARM_LEVEL[1];
		} else if (score >= 30 && score < 70) {
			this.crrentLevel = ALARM_LEVEL[2];
		} else {
			this.crrentLevel = ALARM_LEVEL[3];
		}
		invalidate();
	}

	/***
	 * 设置最大的进度值
	 * 
	 * @param maxCount
	 */
	public void setMaxCount(float maxCount) {
		this.maxCount = maxCount;
	}

	/***
	 * 设置当前的进度值
	 * 
	 * @param currentCount
	 */
	public void setCurrentCount(float currentCount) {
		this.currentCount = currentCount > maxCount ? maxCount : currentCount;
		invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
		if (widthSpecMode == MeasureSpec.EXACTLY
				|| widthSpecMode == MeasureSpec.AT_MOST) {
			mWidth = widthSpecSize;
		} else {
			mWidth = 0;
		}
		if (heightSpecMode == MeasureSpec.AT_MOST
				|| heightSpecMode == MeasureSpec.UNSPECIFIED) {
			mHeight = dipToPx(15);
		} else {
			mHeight = heightSpecSize;
		}
		setMeasuredDimension(mWidth, mHeight);
	}

}
