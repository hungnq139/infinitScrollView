/**
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

#import "RCTMyScrollContentView.h"

#import <React/RCTAssert.h>
#import <React/UIView+React.h>

#import "RCTMyScrollView.h"

@implementation RCTMyScrollContentView

- (void)reactSetFrame:(CGRect)frame
{
  [super reactSetFrame:frame];

  RCTMyScrollView *scrollView = (RCTMyScrollView *)self.superview.superview;

  if (!scrollView) {
    return;
  }

  RCTAssert([scrollView isKindOfClass:[RCTMyScrollView class]],
            @"Unexpected view hierarchy of RCTMyScrollView component.");

  [scrollView updateContentOffsetIfNeeded];
}

@end
