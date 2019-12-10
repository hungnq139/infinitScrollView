/**
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * <p>This source code is licensed under the MIT license found in the LICENSE file in the root
 * directory of this source tree.
 */
package qe.infinitScrollView;

import androidx.annotation.Nullable;
import com.facebook.infer.annotation.Assertions;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.PixelUtil;
import java.util.Map;

public class MyReactScrollViewCommandHelper {

    public static final int COMMAND_SCROLL_TO = 1;
    public static final int COMMAND_SCROLL_TO_END = 2;
    public static final int COMMAND_AUTO_SCROLL = 4;
    public static final int COMMAND_DISABLE_AUTO_SCROLL = 5;
    public static final int COMMAND_FLASH_SCROLL_INDICATORS = 3;

    public interface ScrollCommandHandler<T> {
        void scrollTo(T scrollView, ScrollToCommandData data);

        void scrollToEnd(T scrollView, ScrollToEndCommandData data);

        void autoScroll(T scrollView, AutoScrollCommandData data);

        void disableAutoScroll(T scrollView);

        

        void flashScrollIndicators(T scrollView);
    }

    public static class ScrollToCommandData {

        public final int mDestX, mDestY;
        public final boolean mAnimated;

        ScrollToCommandData(int destX, int destY, boolean animated) {
            mDestX = destX;
            mDestY = destY;
            mAnimated = animated;
        }
    }

    public static class ScrollToEndCommandData {

        public final boolean mAnimated;

        ScrollToEndCommandData(boolean animated) {
            mAnimated = animated;
        }
    }

    public static class AutoScrollCommandData {

        public final float mDuration,mDelay;

        AutoScrollCommandData(float duration , float delay) {
            mDuration = duration;
            mDelay = delay;
        }
    }

    public static Map<String, Integer> getCommandsMap() {
        return MapBuilder.of(
                "scrollTo",
                COMMAND_SCROLL_TO,
                "scrollToEnd",
                COMMAND_SCROLL_TO_END,
                "autoScroll",
                COMMAND_AUTO_SCROLL,
                "disableAutoScroll",
                COMMAND_DISABLE_AUTO_SCROLL,
                "flashScrollIndicators",
                COMMAND_FLASH_SCROLL_INDICATORS);
    }

    public static <T> void receiveCommand(
            ScrollCommandHandler<T> viewManager,
            T scrollView,
            int commandType,
            @Nullable ReadableArray args) {
        Assertions.assertNotNull(viewManager);
        Assertions.assertNotNull(scrollView);
        Assertions.assertNotNull(args);
        switch (commandType) {
            case COMMAND_SCROLL_TO:
            {
                scrollTo(viewManager, scrollView, args);
                return;
            }
            case COMMAND_SCROLL_TO_END:
            {
                scrollToEnd(viewManager, scrollView, args);
                return;
            }

            case COMMAND_AUTO_SCROLL:
            {
                autoScroll(viewManager, scrollView, args);
                return;
            }

            case COMMAND_DISABLE_AUTO_SCROLL:
            {
                disableAutoScroll(viewManager, scrollView, args);
                return;
            }

            case COMMAND_FLASH_SCROLL_INDICATORS:
                viewManager.flashScrollIndicators(scrollView);
                return;

            default:
                throw new IllegalArgumentException(
                        String.format(
                                "Unsupported command %d received by %s.",
                                commandType, viewManager.getClass().getSimpleName()));
        }
    }

    public static <T> void receiveCommand(
            ScrollCommandHandler<T> viewManager,
            T scrollView,
            String commandType,
            @Nullable ReadableArray args) {
        Assertions.assertNotNull(viewManager);
        Assertions.assertNotNull(scrollView);
        Assertions.assertNotNull(args);
        switch (commandType) {
            case "scrollTo":
            {
                scrollTo(viewManager, scrollView, args);
                return;
            }
            case "scrollToEnd":
            {
                scrollToEnd(viewManager, scrollView, args);
                return;
            }
            case "flashScrollIndicators":
                viewManager.flashScrollIndicators(scrollView);
                return;

            default:
                throw new IllegalArgumentException(
                        String.format(
                                "Unsupported command %s received by %s.",
                                commandType, viewManager.getClass().getSimpleName()));
        }
    }

    private static <T> void scrollTo(
            ScrollCommandHandler<T> viewManager, T scrollView, @Nullable ReadableArray args) {
        int destX = Math.round(PixelUtil.toPixelFromDIP(args.getDouble(0)));
        int destY = Math.round(PixelUtil.toPixelFromDIP(args.getDouble(1)));
        boolean animated = args.getBoolean(2);
        viewManager.scrollTo(scrollView, new ScrollToCommandData(destX, destY, animated));
    }

    private static <T> void scrollToEnd(
            ScrollCommandHandler<T> viewManager, T scrollView, @Nullable ReadableArray args) {
        boolean animated = args.getBoolean(0);
        viewManager.scrollToEnd(scrollView, new ScrollToEndCommandData(animated));
    }

    private static <T> void autoScroll(
            ScrollCommandHandler<T> viewManager, T scrollView, @Nullable ReadableArray args) {
        float duration = Math.round(args.getDouble(0));
        float delay = Math.round(args.getDouble(1));
        viewManager.autoScroll(scrollView, new AutoScrollCommandData(duration , delay));
    }

      private static <T> void disableAutoScroll(
            ScrollCommandHandler<T> viewManager, T scrollView, @Nullable ReadableArray args) {
        viewManager.disableAutoScroll(scrollView);
    }
}
