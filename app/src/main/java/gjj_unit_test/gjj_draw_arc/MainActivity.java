package gjj_unit_test.gjj_draw_arc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

import gjj.progress.view.ProgressView;
import gjj.progress.view.ProgressViewNew;

public class MainActivity extends AppCompatActivity {

    private ProgressView progressView;
    ProgressViewNew progressViewNew;
    Button btn_test;
    EditText et_test;
    private int value;
    private Random random = new Random(System.currentTimeMillis());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressView= (ProgressView) findViewById(R.id.pregress);
        progressViewNew= (ProgressViewNew) findViewById(R.id.proViewnew);
        et_test= (EditText) findViewById(R.id.et_test);
        btn_test= (Button) findViewById(R.id.btn_test);


        progressView.setMaxCount(100.0f);
//        value = random.nextInt(100)+1;
        progressView.setCurrentCount(65);
        progressView.setScore(value);

        progressViewNew.setCurrentCount(30);

        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(et_test.getText().toString())){
                    Toast.makeText(MainActivity.this,"请输入BMI测试数据",Toast.LENGTH_LONG).show();
                    return;
                }
                progressViewNew.setCurrentCount(Float.parseFloat(et_test.getText().toString()));
            }
        });
    }
}
