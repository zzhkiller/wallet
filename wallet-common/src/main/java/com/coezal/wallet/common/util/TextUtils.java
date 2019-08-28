package com.coezal.wallet.common.util;

import javax.annotation.Nullable;

/**
 * Version 1.0
 * Created by lll on 2019-08-28.
 * Description
 * copyright generalray4239@gmail.com
 */
public class TextUtils {

  public static boolean isEmpty(@Nullable CharSequence str) {
    return str == null || str.length() == 0;
  }

}
