package gjj_unit_test.gjj_draw_arc;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ProgressView progressView;
    private int value;
    private Random random = new Random(System.currentTimeMillis());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressView= (ProgressView) findViewById(R.id.pregress);
        progressView.setMaxCount(100.0f);
//        value = random.nextInt(100)+1;
        progressView.setCurrentCount(65);
        progressView.setScore(value);
    }
}
