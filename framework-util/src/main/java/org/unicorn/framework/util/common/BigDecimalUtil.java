/**
 * Title: CookieUtil.java<br/>
 * Description: <br/>
 * Copyright: Copyright (c) 2015<br/>
 * 
 *
 */
package org.unicorn.framework.util.common;

import java.math.BigDecimal;

/**
 * Description: <br/>
 * 
 * @author xiebin
 *
 */
public class BigDecimalUtil {  
  
	 public static BigDecimal bigDecimal2RoundHalfUp(BigDecimal target){
	    	return target.setScale(0,BigDecimal.ROUND_HALF_UP);
	 }
	 
	 public static BigDecimal bigDecimalDivideRoundHalfUp(BigDecimal divided,BigDecimal divide){
	    	return divided.divide(divide,0,BigDecimal.ROUND_HALF_UP);
	 }
	 
	 public static void main(String[] args) {
		 BigDecimal a=new BigDecimal(10.1);
		 BigDecimal b=new BigDecimal(5.1);
		 System.out.println(a.multiply(b));
		 System.out.println(bigDecimal2RoundHalfUp(a.multiply(b)));
		
		 
		 BigDecimal bg= bigDecimalDivideRoundHalfUp(new BigDecimal(10.1),new BigDecimal(5.1).multiply(new BigDecimal(3)));
		System.out.println(bigDecimal2RoundHalfUp(bg.add(a)));
	}
}  