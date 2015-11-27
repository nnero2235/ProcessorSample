package percent.test.android.annotationtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import annotations.interal.Bind;
import percent.test.android.injector.Injector;


public class MainActivity extends Activity {


  @Bind(R.id.tv) TextView  tv;
  @Bind(R.id.image) ImageView iv;
  @Bind(R.id.tv_ha) TextView tvHa;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Injector.inject(this);

    tv.setText("HEEEEEE");
    tvHa.setText("我时是");
  }

}
