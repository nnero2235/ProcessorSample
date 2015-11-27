package percent.test.android.injector;

/**
 * Created by nnero on 15/11/26.
 */
public interface ViewBind<T> {
  public void bind(T source,Finder finder);
}
