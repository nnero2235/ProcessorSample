package percent.test.android.injector;

import android.app.Activity;
import android.view.View;

/**
 * Created by nnero on 15/11/26.
 */
public enum  Finder {
  ACTIVITY {
    @Override
    public View findView(Object source, int id) {
      return ((Activity)source).findViewById(id);
    }
  };

  public <T> T castView(View v){
    return (T)v;
  }

  public abstract  <T> View findView(T source,int id); //枚举 实例 可以实现该抽象方法
}
