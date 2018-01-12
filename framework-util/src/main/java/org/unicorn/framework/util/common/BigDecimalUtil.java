package org.unicorn.framework.util.common;

import java.math.BigDecimal;

/**
 * 
 * @author xiebin
 *
 */
public class BigDecimalUtil {
	  public static BigDecimal bigDecimal2RoundHalfUp(BigDecimal target){
          return target.setScale(2,BigDecimal.ROUND_HALF_UP);
}
   public static BigDecimal bigDecimalDivideRoundHalfUp(BigDecimal divided,BigDecimal divide){
          return divided.divide(divide,2,BigDecimal.ROUND_HALF_UP);
}
}
