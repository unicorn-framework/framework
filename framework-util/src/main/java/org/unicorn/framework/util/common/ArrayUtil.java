/**
 * 
 * Copyright (c) 2015 All Rights Reserved.
 */
package org.unicorn.framework.util.common;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 有关数组处理的工具类。
 * 
 * <p>
 * 这个类中的每个方法都可以“安全”地处理<code>null</code>，而不会抛出<code>NullPointerException</code>。
 * </p>
 * 
 * @author Go
 * @version $Id: ArrayUtil.java,v 0.1 2015年10月15日 18:58 mingbo.tang $Exp
 */
public class ArrayUtil {

    /* ============================================================================ */
    /* 将数组转换成易于阅读的字符串表示。 */
    /*                                                                              */
    /* 支持多维数组。 */
    /* ============================================================================ */
	
    /**
     * 将数组转换成易于阅读的字符串表示。
     * 
     * <p>
     * 如果数组是<code>null</code>则返回<code>[]</code>，支持多维数组。 如果数组元素为<code>null</code>，则显示<code>&lt;null&gt;</code>。
     * 
     * <pre>
     * ArrayUtil.toString(null)                              = "[]"
     * ArrayUtil.toString(new int[] {1, 2, 3})               = "[1, 2, 3]"
     * ArrayUtil.toString(new boolean[] {true, false, true}) = "[true, false, true]"
     * ArrayUtil.toString(new Object[] {
     *                       {1, 2, 3},  // 嵌套数组
     *                       hello,      // 嵌套非数组
     *                       null,       // 嵌套null
     *                       {},         // 嵌套空数组
     *                       {2, 3, 4}   // 嵌套数组
     *                    })                                 = "[[1, 2, 3], hello, <null>, [], [2, 3, 4]]"
     * </pre>
     * 
     * </p>
     *
     * @param array 要转换的数组
     *
     * @return 字符串表示，<code>"[]"</code>表示空数组或<code>null</code>
     */
    public static String toString(Object array) {
        return toString(array, "[]", "<null>");
    }

    /**
     * 将数组转换成易于阅读的字符串表示。
     * 
     * <p>
     * 如果数组是<code>null</code>则返回指定字符串，支持多维数组。 如果数组元素为<code>null</code>，则显示<code>&lt;null&gt;</code>。
     * 
     * <pre>
     * ArrayUtil.toString(null, "null")                              = "null"
     * ArrayUtil.toString(new int[] {1, 2, 3}, "null")               = "[1, 2, 3]"
     * ArrayUtil.toString(new boolean[] {true, false, true}, "null") = "[true, false, true]"
     * ArrayUtil.toString(new Object[] {
     *                       {1, 2, 3},  // 嵌套数组
     *                       hello,      // 嵌套非数组
     *                       null,       // 嵌套null
     *                       {},         // 嵌套空数组
     *                       {2, 3, 4}   // 嵌套数组
     *                    }, "null")                                 = "[[1, 2, 3], hello, <null>, [], [2, 3, 4]]"
     * </pre>
     * 
     * </p>
     *
     * @param array 要转换的数组
     * @param nullArrayStr 如果数组是<code>null</code>，则返回此字符串
     *
     * @return 字符串表示，或返回指定字符串表示<code>null</code>
     */
    public static String toString(Object array, String nullArrayStr) {
        return toString(array, nullArrayStr, "<null>");
    }

    /**
     * 将数组转换成易于阅读的字符串表示。
     * 
     * <p>
     * 如果数组是<code>null</code>则返回指定字符串，支持多维数组。 如果数组元素为<code>null</code>，则显示指定字符串。
     * 
     * <pre>
     * ArrayUtil.toString(null, "null", "NULL")                              = "null"
     * ArrayUtil.toString(new int[] {1, 2, 3}, "null", "NULL")               = "[1, 2, 3]"
     * ArrayUtil.toString(new boolean[] {true, false, true}, "null", "NULL") = "[true, false, true]"
     * ArrayUtil.toString(new Object[] {
     *                       {1, 2, 3},  // 嵌套数组
     *                       hello,      // 嵌套非数组
     *                       null,       // 嵌套null
     *                       {},         // 嵌套空数组
     *                       {2, 3, 4}   // 嵌套数组
     *                    }, "null", "NULL")                                 = "[[1, 2, 3], hello, NULL, [], [2, 3, 4]]"
     * </pre>
     * 
     * </p>
     *
     * @param array 要转换的数组
     * @param nullArrayStr 如果数组是<code>null</code>，则返回此字符串
     * @param nullElementStr 如果数组中的元素为<code>null</code>，则返回此字符串
     *
     * @return 字符串表示，或返回指定字符串表示<code>null</code>
     */
    public static String toString(Object array, String nullArrayStr, String nullElementStr) {
        if (array == null) {
            return nullArrayStr;
        }

        StringBuffer buffer = new StringBuffer();

        toString(buffer, array, nullArrayStr, nullElementStr);

        return buffer.toString();
    }

    /**
     * 将数组转换成易于阅读的字符串表示。<code>null</code>将被看作空数组。 支持多维数组。
     *
     * @param buffer 将转换后的字符串加入到这个<code>StringBuffer</code>中
     * @param array 要转换的数组
     * @param nullArrayStr 如果数组是<code>null</code>，则返回此字符串
     * @param nullElementStr 如果数组中的元素为<code>null</code>，则返回此字符串
     */
    private static void toString(StringBuffer buffer, Object array, String nullArrayStr, String nullElementStr) {
        if (array == null) {
            buffer.append(nullElementStr);
            return;
        }

        if (!array.getClass().isArray()) {
            buffer.append(ObjecttoString(array, nullElementStr));
            return;
        }

        buffer.append('[');

        // array为数组
        if (array instanceof long[]) {
            long[] longArray = (long[]) array;
            int length = longArray.length;

            for (int i = 0; i < length; i++) {
                if (i > 0) {
                    buffer.append(", ");
                }

                buffer.append(longArray[i]);
            }
        } else if (array instanceof int[]) {
            int[] intArray = (int[]) array;
            int length = intArray.length;

            for (int i = 0; i < length; i++) {
                if (i > 0) {
                    buffer.append(", ");
                }

                buffer.append(intArray[i]);
            }
        } else if (array instanceof short[]) {
            short[] shortArray = (short[]) array;
            int length = shortArray.length;

            for (int i = 0; i < length; i++) {
                if (i > 0) {
                    buffer.append(", ");
                }

                buffer.append(shortArray[i]);
            }
        } else if (array instanceof byte[]) {
            byte[] byteArray = (byte[]) array;
            int length = byteArray.length;

            for (int i = 0; i < length; i++) {
                if (i > 0) {
                    buffer.append(", ");
                } else {
                    buffer.append("0x");
                }

                String hexStr = Integer.toHexString(0xFF & byteArray[i]).toUpperCase();

                if (hexStr.length() == 0) {
                    buffer.append("00");
                } else if (hexStr.length() == 1) {
                    buffer.append("0");
                }

                buffer.append(hexStr);
            }
        } else if (array instanceof double[]) {
            double[] doubleArray = (double[]) array;
            int length = doubleArray.length;

            for (int i = 0; i < length; i++) {
                if (i > 0) {
                    buffer.append(", ");
                }

                buffer.append(doubleArray[i]);
            }
        } else if (array instanceof float[]) {
            float[] floatArray = (float[]) array;
            int length = floatArray.length;

            for (int i = 0; i < length; i++) {
                if (i > 0) {
                    buffer.append(", ");
                }

                buffer.append(floatArray[i]);
            }
        } else if (array instanceof boolean[]) {
            boolean[] booleanArray = (boolean[]) array;
            int length = booleanArray.length;

            for (int i = 0; i < length; i++) {
                if (i > 0) {
                    buffer.append(", ");
                }

                buffer.append(booleanArray[i]);
            }
        } else if (array instanceof char[]) {
            char[] charArray = (char[]) array;
            int length = charArray.length;

            for (int i = 0; i < length; i++) {
                if (i > 0) {
                    buffer.append(", ");
                }

                buffer.append(charArray[i]);
            }
        } else {
            Object[] objectArray = (Object[]) array;
            int length = objectArray.length;

            for (int i = 0; i < length; i++) {
                if (i > 0) {
                    buffer.append(", ");
                }

                toString(buffer, objectArray[i], nullArrayStr, nullElementStr);
            }
        }

        buffer.append(']');
    }

    /**
     * 取得对象的<code>toString()</code>的值，如果对象为<code>null</code>，则返回指定字符串。
     * 
     * <pre>
     * ObjectUtil.toString(null, null)           = null
     * ObjectUtil.toString(null, "null")         = "null"
     * ObjectUtil.toString("", "null")           = ""
     * ObjectUtil.toString("bat", "null")        = "bat"
     * ObjectUtil.toString(Boolean.TRUE, "null") = "true"
     * ObjectUtil.toString([1, 2, 3], "null")    = "[1, 2, 3]"
     * </pre>
     *
     * @param object 对象
     * @param nullStr 如果对象为<code>null</code>，则返回该字符串
     *
     * @return 对象的<code>toString()</code>的返回值，或指定字符串
     */
    private static String ObjecttoString(Object object, String nullStr) {
        return (object == null) ? nullStr : (object.getClass().isArray() ? ArrayUtil.toString(object) : object.toString());
    }

    /**
    * @Description: 提取字符串数组中包含的整形元素，并返回整形数组.
    * @param aryStr
    * @return
    */
    public static Integer[] toArray(String[] aryStr){
		if (aryStr == null){
			return null;
		}
		
		int length = aryStr.length;
		
		if(length == 0){
			return null;
		}
		
		List<Integer> list = new ArrayList<Integer>();
		Pattern pattern = Pattern.compile("[0-9]*");
		for(int i = 0;i<length;i++){
			if(pattern.matcher(aryStr[i]).matches()){
				list.add(Integer.parseInt(aryStr[i]));
			}
		}
		
		return (Integer[])list.toArray(new Integer[list.size()]);
	}
    
    
    
  //java 合并两个byte数组  
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){  
        byte[] byte_3 = new byte[byte_1.length+byte_2.length];  
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);  
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);  
        return byte_3;  
    }  
}
