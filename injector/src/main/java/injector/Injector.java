package percent.test.android.injector;

import android.app.Activity;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by nnero on 15/11/26.
 */
//this class is static
public class Injector {

  private static final String CLASS_STUFF = "$$Injector";
  private static final Map<String, ViewBind> viewBinds = new LinkedHashMap<>();

  public static <T> void inject(T source) {
    if (source instanceof Activity) {
      bindActivity(source);
    }
  }

  private static <T> void bindActivity(T source) {
    ViewBind viewBind = viewBinds.get(source.getClass().getName());
    if(viewBind == null){
      try {
        Class<?> clazz = Class.forName(source.getClass().getName()+CLASS_STUFF);
        viewBind = (ViewBind) clazz.newInstance();
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
      viewBinds.put(source.getClass().getName(),viewBind);
    }
    viewBind.bind(source,Finder.ACTIVITY);
  }
}
