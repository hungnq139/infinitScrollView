package qe.infinitScrollView;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import qe.infinitScrollView.MyReactScrollViewManager;
import qe.infinitScrollView.MyReactHorizontalScrollViewManager;

public class InfinitScrollViewPackage implements ReactPackage {
  @Override
  public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
    return Arrays.<ViewManager>asList(
      new MyReactScrollViewManager(),
      new MyReactHorizontalScrollViewManager()
    );
  }

  @Override
  public List<NativeModule> createNativeModules(
          ReactApplicationContext reactContext) {
    List<NativeModule> modules = new ArrayList<>();
    return modules;
  }
}