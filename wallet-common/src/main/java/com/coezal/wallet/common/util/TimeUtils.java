package com.coezal.wallet.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Version 1.0
 * Created by lll on 2019-10-31.
 * Description
 * copyright generalray4239@gmail.com
 */
public class TimeUtils {


  public static String getLoggerFullTime() {
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }

}
