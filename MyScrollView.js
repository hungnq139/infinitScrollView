import React, {Component} from 'react';
import {
  ScrollView,
  UIManager,
  Platform,
  requireNativeComponent,
} from 'react-native';
import ScrollResponder from 'react-native/Libraries/Components/ScrollResponder';

const nullthrows = require('nullthrows');

function createScrollResponder(
  node: React.ElementRef<typeof ScrollView>,
): typeof ScrollResponder.Mixin {
  const scrollResponder = {
    ...ScrollResponder.Mixin,
    scrollResponderAutoScroll: function(options?: {
      duration?: number,
      delay?: number,
    }) {
      // set Default
      const duration = (options && options.duration) || 300;
      const delay = (options && options.delay) || 0;
      UIManager.dispatchViewManagerCommand(
        this.scrollResponderGetScrollableNode(),
        UIManager.getViewManagerConfig('RCTMyScrollView').Commands.autoScroll,
        [duration, delay],
      );
    },
    scrollResponderDisableAutoScroll: function() {
      UIManager.dispatchViewManagerCommand(
        this.scrollResponderGetScrollableNode(),
        UIManager.getViewManagerConfig('RCTMyScrollView').Commands
          .disableAutoScroll,
        [],
      );
    },

    // override old RCTScrollView
    scrollResponderScrollTo: function(
      x?: number | {x?: number, y?: number, animated?: boolean},
      y?: number,
      animated?: boolean,
    ) {
      if (typeof x === 'number') {
        console.warn(
          '`scrollResponderScrollTo(x, y, animated)` is deprecated. Use `scrollResponderScrollTo({x: 5, y: 5, animated: true})` instead.',
        );
      } else {
        ({x, y, animated} = x || {});
      }
      UIManager.dispatchViewManagerCommand(
        nullthrows(this.scrollResponderGetScrollableNode()),
        UIManager.getViewManagerConfig('RCTMyScrollView').Commands.scrollTo,
        [x || 0, y || 0, animated !== false],
      );
    },

    scrollResponderScrollToEnd: function(options?: {animated?: boolean}) {
      // Default to true
      const animated = (options && options.animated) !== false;
      UIManager.dispatchViewManagerCommand(
        this.scrollResponderGetScrollableNode(),
        UIManager.getViewManagerConfig('RCTMyScrollView').Commands.scrollToEnd,
        [animated],
      );
    },
    scrollResponderFlashScrollIndicators: function() {
      UIManager.dispatchViewManagerCommand(
        this.scrollResponderGetScrollableNode(),
        UIManager.getViewManagerConfig('RCTMyScrollView').Commands
          .flashScrollIndicators,
        [],
      );
    },
  };

  for (const key in scrollResponder) {
    if (typeof scrollResponder[key] === 'function') {
      scrollResponder[key] = scrollResponder[key].bind(node);
    }
  }

  return scrollResponder;
}

let AndroidScrollView;
let AndroidHorizontalScrollView;
let RCTScrollView;
let RCTScrollContentView;

if (Platform.OS === 'android') {
  AndroidScrollView = requireNativeComponent('RCTMyScrollView');
  AndroidHorizontalScrollView = requireNativeComponent(
    'MyAndroidHorizontalScrollView',
  );
} else if (Platform.OS === 'ios') {
  RCTScrollView = requireNativeComponent('RCTMyScrollView');
  RCTScrollContentView = requireNativeComponent('RCTMyScrollContentView');
} else {
  RCTScrollView = requireNativeComponent('RCTMyScrollView');
  RCTScrollContentView = requireNativeComponent('RCTMyScrollContentView');
}

export default class MyScrollView extends ScrollView {
  _scrollResponder: typeof ScrollResponder.Mixin = createScrollResponder(this);

  autoScroll(options?: {duration?: number, delay?: number}) {
    // Default to true
    this._scrollResponder.scrollResponderAutoScroll({
      duration: options && options.duration,
      delay: options && options.delay,
    });
  }

  disableAutoScroll() {
    // Default to true
    this._scrollResponder.scrollResponderDisableAutoScroll();
  }

  render() {
    let ScrollViewClass;
    let ScrollContentContainerViewClass;
    if (Platform.OS === 'android') {
      if (this.props.horizontal === true) {
        ScrollViewClass = AndroidHorizontalScrollView;
      } else {
        ScrollViewClass = AndroidScrollView;
      }
    } else {
      ScrollViewClass = RCTScrollView;
      ScrollContentContainerViewClass = RCTScrollContentView;
    }
    const props = super.render().props;
    const curChildren = React.Children.toArray(props.children);

    const refreshControl = this.props.refreshControl;
    if (refreshControl) {
      if (Platform.OS === 'ios') {
        delete props.children;
        const children = [
          curChildren[0],
          React.createElement(ScrollContentContainerViewClass, {
            ...curChildren[1].props,
            ref: this._setInnerViewRef,
          }),
        ];
        return React.createElement(
          ScrollViewClass,
          {...props, ref: this._setScrollViewRef},
          children,
        );
      } else if (Platform.OS === 'android') {
        const childProp = curChildren[0].props;

        const children = [
          React.createElement(ScrollViewClass, {
            ...childProp,
            ref: this._setScrollViewRef,
          }),
        ];
        return React.cloneElement(refreshControl, props, children);
      }
    }

    return React.createElement(ScrollViewClass, {
      ...props,
      ref: this._setScrollViewRef,
    });
  }
}
