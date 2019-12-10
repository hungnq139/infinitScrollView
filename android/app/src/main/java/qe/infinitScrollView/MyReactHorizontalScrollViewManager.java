/**
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * <p>This source code is licensed under the MIT license found in the LICENSE file in the root
 * directory of this source tree.
 */
package qe.infinitScrollView;

import android.graphics.Color;
import android.util.DisplayMetrics;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.DisplayMetricsHolder;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.ReactClippingViewGroupHelper;
import com.facebook.react.uimanager.Spacing;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.ViewProps;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.annotations.ReactPropGroup;
import com.facebook.yoga.YogaConstants;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import android.animation.ObjectAnimator;
import android.view.animation.Interpolator;

import com.facebook.react.views.scroll.FpsListener;
import com.facebook.react.views.scroll.ReactScrollViewHelper;


@ReactModule(name = MyReactHorizontalScrollViewManager.REACT_CLASS)
public class MyReactHorizontalScrollViewManager extends ViewGroupManager<MyReactHorizontalScrollView>
    implements MyReactScrollViewCommandHelper.ScrollCommandHandler<MyReactHorizontalScrollView> {

  public static final String REACT_CLASS = "MyAndroidHorizontalScrollView";

  private ObjectAnimator animator;

  private static final int[] SPACING_TYPES = {
    Spacing.ALL, Spacing.LEFT, Spacing.RIGHT, Spacing.TOP, Spacing.BOTTOM,
  };

  private @Nullable FpsListener mFpsListener = null;

  public MyReactHorizontalScrollViewManager() {
    this(null);
  }

  public MyReactHorizontalScrollViewManager(@Nullable FpsListener fpsListener) {
    mFpsListener = fpsListener;
  }

  @Override
  public String getName() {
    return REACT_CLASS;
  }

  @Override
  public MyReactHorizontalScrollView createViewInstance(ThemedReactContext context) {
    return new MyReactHorizontalScrollView(context, mFpsListener);
  }

  @ReactProp(name = "scrollEnabled", defaultBoolean = true)
  public void setScrollEnabled(MyReactHorizontalScrollView view, boolean value) {
    view.setScrollEnabled(value);
  }

  @ReactProp(name = "showsHorizontalScrollIndicator")
  public void setShowsHorizontalScrollIndicator(MyReactHorizontalScrollView view, boolean value) {
    view.setHorizontalScrollBarEnabled(value);
  }

  @ReactProp(name = "decelerationRate")
  public void setDecelerationRate(MyReactHorizontalScrollView view, float decelerationRate) {
    view.setDecelerationRate(decelerationRate);
  }

  @ReactProp(name = "disableIntervalMomentum")
  public void setDisableIntervalMomentum(
      MyReactHorizontalScrollView view, boolean disbaleIntervalMomentum) {
    view.setDisableIntervalMomentum(disbaleIntervalMomentum);
  }

  @ReactProp(name = "snapToInterval")
  public void setSnapToInterval(MyReactHorizontalScrollView view, float snapToInterval) {
    // snapToInterval needs to be exposed as a float because of the Javascript interface.
    DisplayMetrics screenDisplayMetrics = DisplayMetricsHolder.getScreenDisplayMetrics();
    view.setSnapInterval((int) (snapToInterval * screenDisplayMetrics.density));
  }

  @ReactProp(name = "snapToOffsets")
  public void setSnapToOffsets(
      MyReactHorizontalScrollView view, @Nullable ReadableArray snapToOffsets) {
    DisplayMetrics screenDisplayMetrics = DisplayMetricsHolder.getScreenDisplayMetrics();
    List<Integer> offsets = new ArrayList<Integer>();
    for (int i = 0; i < snapToOffsets.size(); i++) {
      offsets.add((int) (snapToOffsets.getDouble(i) * screenDisplayMetrics.density));
    }
    view.setSnapOffsets(offsets);
  }

  @ReactProp(name = "snapToStart")
  public void setSnapToStart(MyReactHorizontalScrollView view, boolean snapToStart) {
    view.setSnapToStart(snapToStart);
  }

  @ReactProp(name = "snapToEnd")
  public void setSnapToEnd(MyReactHorizontalScrollView view, boolean snapToEnd) {
    view.setSnapToEnd(snapToEnd);
  }

  @ReactProp(name = ReactClippingViewGroupHelper.PROP_REMOVE_CLIPPED_SUBVIEWS)
  public void setRemoveClippedSubviews(
      MyReactHorizontalScrollView view, boolean removeClippedSubviews) {
    view.setRemoveClippedSubviews(removeClippedSubviews);
  }

  /**
   * Computing momentum events is potentially expensive since we post a runnable on the UI thread to
   * see when it is done. We only do that if {@param sendMomentumEvents} is set to true. This is
   * handled automatically in js by checking if there is a listener on the momentum events.
   *
   * @param view
   * @param sendMomentumEvents
   */
  @ReactProp(name = "sendMomentumEvents")
  public void setSendMomentumEvents(MyReactHorizontalScrollView view, boolean sendMomentumEvents) {
    view.setSendMomentumEvents(sendMomentumEvents);
  }

  /**
   * Tag used for logging scroll performance on this scroll view. Will force momentum events to be
   * turned on (see setSendMomentumEvents).
   *
   * @param view
   * @param scrollPerfTag
   */
  @ReactProp(name = "scrollPerfTag")
  public void setScrollPerfTag(MyReactHorizontalScrollView view, String scrollPerfTag) {
    view.setScrollPerfTag(scrollPerfTag);
  }

  @ReactProp(name = "pagingEnabled")
  public void setPagingEnabled(MyReactHorizontalScrollView view, boolean pagingEnabled) {
    view.setPagingEnabled(pagingEnabled);
  }

  /** Controls overScroll behaviour */
  @ReactProp(name = "overScrollMode")
  public void setOverScrollMode(MyReactHorizontalScrollView view, String value) {
    view.setOverScrollMode(ReactScrollViewHelper.parseOverScrollMode(value));
  }

  @ReactProp(name = "nestedScrollEnabled")
  public void setNestedScrollEnabled(MyReactHorizontalScrollView view, boolean value) {
    ViewCompat.setNestedScrollingEnabled(view, value);
  }

  @Override
  public void receiveCommand(
      MyReactHorizontalScrollView scrollView, int commandId, @Nullable ReadableArray args) {
    MyReactScrollViewCommandHelper.receiveCommand(this, scrollView, commandId, args);
  }

  @Override
  public void receiveCommand(
      MyReactHorizontalScrollView scrollView, String commandId, @Nullable ReadableArray args) {
    MyReactScrollViewCommandHelper.receiveCommand(this, scrollView, commandId, args);
  }

  @Override
  public void flashScrollIndicators(MyReactHorizontalScrollView scrollView) {
    scrollView.flashScrollIndicators();
  }

  @Override
  public void scrollTo(
      MyReactHorizontalScrollView scrollView, MyReactScrollViewCommandHelper.ScrollToCommandData data) {
    if (data.mAnimated) {
      scrollView.smoothScrollTo(data.mDestX, data.mDestY);
    } else {
      scrollView.scrollTo(data.mDestX, data.mDestY);
    }
  }

  @Override
  public void scrollToEnd(
      MyReactHorizontalScrollView scrollView,
      MyReactScrollViewCommandHelper.ScrollToEndCommandData data) {
    // ScrollView always has one child - the scrollable area
    int right = scrollView.getChildAt(0).getWidth() + scrollView.getPaddingRight();
    if (data.mAnimated) {
      scrollView.smoothScrollTo(right, scrollView.getScrollY());
    } else {
      scrollView.scrollTo(right, scrollView.getScrollY());
    }
  }

  public void autoScroll(
          MyReactHorizontalScrollView scrollView, MyReactScrollViewCommandHelper.AutoScrollCommandData data) {
    scrollView.autoScroll(data);
  }

  public void disableAutoScroll(MyReactHorizontalScrollView scrollView) {
    scrollView.disableAutoScroll();
  }

  /**
   * When set, fills the rest of the scrollview with a color to avoid setting a background and
   * creating unnecessary overdraw.
   *
   * @param view
   * @param color
   */
  @ReactProp(name = "endFillColor", defaultInt = Color.TRANSPARENT, customType = "Color")
  public void setBottomFillColor(MyReactHorizontalScrollView view, int color) {
    view.setEndFillColor(color);
  }

  @ReactPropGroup(
      names = {
        ViewProps.BORDER_RADIUS,
        ViewProps.BORDER_TOP_LEFT_RADIUS,
        ViewProps.BORDER_TOP_RIGHT_RADIUS,
        ViewProps.BORDER_BOTTOM_RIGHT_RADIUS,
        ViewProps.BORDER_BOTTOM_LEFT_RADIUS
      },
      defaultFloat = YogaConstants.UNDEFINED)
  public void setBorderRadius(MyReactHorizontalScrollView view, int index, float borderRadius) {
    if (!YogaConstants.isUndefined(borderRadius)) {
      borderRadius = PixelUtil.toPixelFromDIP(borderRadius);
    }

    if (index == 0) {
      view.setBorderRadius(borderRadius);
    } else {
      view.setBorderRadius(borderRadius, index - 1);
    }
  }

  @ReactProp(name = "borderStyle")
  public void setBorderStyle(MyReactHorizontalScrollView view, @Nullable String borderStyle) {
    view.setBorderStyle(borderStyle);
  }

  @ReactPropGroup(
      names = {
        ViewProps.BORDER_WIDTH,
        ViewProps.BORDER_LEFT_WIDTH,
        ViewProps.BORDER_RIGHT_WIDTH,
        ViewProps.BORDER_TOP_WIDTH,
        ViewProps.BORDER_BOTTOM_WIDTH,
      },
      defaultFloat = YogaConstants.UNDEFINED)
  public void setBorderWidth(MyReactHorizontalScrollView view, int index, float width) {
    if (!YogaConstants.isUndefined(width)) {
      width = PixelUtil.toPixelFromDIP(width);
    }
    view.setBorderWidth(SPACING_TYPES[index], width);
  }

  @ReactPropGroup(
      names = {
        "borderColor",
        "borderLeftColor",
        "borderRightColor",
        "borderTopColor",
        "borderBottomColor"
      },
      customType = "Color")
  public void setBorderColor(MyReactHorizontalScrollView view, int index, Integer color) {
    float rgbComponent =
        color == null ? YogaConstants.UNDEFINED : (float) ((int) color & 0x00FFFFFF);
    float alphaComponent = color == null ? YogaConstants.UNDEFINED : (float) ((int) color >>> 24);
    view.setBorderColor(SPACING_TYPES[index], rgbComponent, alphaComponent);
  }

  @ReactProp(name = "overflow")
  public void setOverflow(MyReactHorizontalScrollView view, @Nullable String overflow) {
    view.setOverflow(overflow);
  }

  @ReactProp(name = "persistentScrollbar")
  public void setPersistentScrollbar(MyReactHorizontalScrollView view, boolean value) {
    view.setScrollbarFadingEnabled(!value);
  }
}
