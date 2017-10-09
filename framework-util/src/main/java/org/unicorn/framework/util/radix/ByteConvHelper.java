/**
 * Title: ByteConvHelper.java<br/>
 * Description: <br/>
 * Copyright: Copyright (c) 2015<br/>
 * 
 *
 */
package org.unicorn.framework.util.radix;

import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;



/**
 * Title: ByteConvHelper<br/>
 * Description: <br/>
 * 
 * @author xiebin
 *
 */
public class ByteConvHelper {

    
    /**
     * 把二进制格式转为十进制整数
     * 
     * @param bin
     *            二进制形式的字符串,eg: "10101010"
     * @return 十进制整数
     * @throws NumberFormatException,错误提示信息: 待转换的bin 包含非法二进制形式.
     */
    public static int binary2Int(String bin) throws NumberFormatException {
       
            return Integer.valueOf(bin, 2).intValue();
       
    }

    /**
     * 把十进制整数转化为二进制格式
     * 
     * @param val
     * @return 二进制形式的字符串,eg: "10101010"
     */
    public static String int2Binary(int val) {
        return Integer.toBinaryString(val);
    }

    /**
     * 把十六进制整数转化为二进制格式
     * 
     * @param val
     * @return 二进制形式的字符串,eg: "10101010"
     */
    public static String hex2Binary(String val) throws IllegalArgumentException {
        StringBuffer ret = new StringBuffer();

        for (int i = 0; i < val.length(); i++) {
            ret.append(hex2binary(val.charAt(i)));
        }
        return ret.toString();
    }

    public static String hex2binary(char hex) throws IllegalArgumentException {
        switch (hex) {
        case '0':
            return "0000";
        case '1':
            return "0001";
        case '2':
            return "0010";
        case '3':
            return "0011";
        case '4':
            return "0100";
        case '5':
            return "0101";
        case '6':
            return "0110";
        case '7':
            return "0111";
        case '8':
            return "1000";
        case '9':
            return "1001";
        case 'a':
        case 'A':
            return "1010";
        case 'b':
        case 'B':
            return "1011";
        case 'c':
        case 'C':
            return "1100";
        case 'd':
        case 'D':
            return "1101";
        case 'e':
        case 'E':
            return "1110";
        case 'f':
        case 'F':
            return "1111";
        default:
            throw new java.lang.IllegalArgumentException("十六进制转为二进制时, 有非法十六制字符:" + hex);
        }
    }

    /**
     * 将一串二进制转为十六进制
     * 
     * @param binary
     * @return
     */
    public static String binary2hex(String binary) {
        String hexString = "";
        int binLen = binary.length();
        if (binLen % 4 != 0) {
            binary = StringUtils.repeat("0", 4 - binLen % 4) + binary;
            binLen = binary.length();
        }
        for (int i = 0; i < binLen; i = i + 4) {
            hexString += Integer.toHexString(Integer.valueOf(
                    binary.substring(i, i + 4), 2).intValue());
        }
        return hexString;
    }
    
    
    
    /**
     * 
     * Title: byte2String<br/>
     * Description: <br/>
     * @author Go
     * @date 2015年10月8日下午1:31:17
     *
     * @param val
     * @return
     * @throws UnsupportedEncodingException  不支持指定的编码转换
     */
    public static String byte2String(byte val) throws UnsupportedEncodingException {
        byte[] arrVal = { val };
        return byte2String(arrVal, "ISO-8859-1");
    }
    
    
    /**
     * 
     * Title: byte2String<br/>
     * Description: <br/>
     * @author Go
     * @date 2015年10月8日下午1:30:39
     *
     * @param val
     * @param charset
     * @return
     * @throws UnsupportedEncodingException  不支持指定的编码转换
     */
    public static String byte2String(byte[] val, String charset) throws UnsupportedEncodingException {
       
            return new String(val, charset);
       
    }

    /**
     * bin转为对应的Ascii 形式.
     * 
     * @param binStr
     * @return
     * @throws GoException
     */
    public static String binToAscStr(String binStr)  {
        /**
         * byte[] chrBytes = binStr.getBytes(); return
         * String.valueOf(byte2uint(chrBytes, 0));
         */

        return binToAscStr(binStr.getBytes());
    }

    /**
     * bin转为对应的Ascii 形式.
     * 
     * @param binBuf
     * @return
     * @throws GoException
     */
    public static String binToAscStr(byte[] binBuf) {
        long ascVal = 0;
        for (int i = 0; i < binBuf.length; i++) {
            ascVal = (ascVal << 8) + (long) (binBuf[i] & 0xff);
        }

        return String.valueOf(ascVal);
    }

    /**
     * 将一个ASCII值转为对应的字符
     * 
     * @param strAsc
     *            ASCII值
     * @return 字符
     */

    public static String asc2bin(String strAsc) throws IllegalArgumentException {
        try {
            Integer deliInt = Integer.valueOf(strAsc);
            if (deliInt.intValue() > 255 || deliInt.intValue() < -128) {
                throw new IllegalArgumentException("转换为bin有误, asc=[" + strAsc+ "]");
            }

            byte[] asc = { deliInt.byteValue() };
            return new String(asc, "ISO-8859-1");

            // return String.valueOf((char)Byte.parseByte(strAsc));
        } catch (NumberFormatException e) {
            // TODO log asc error
            throw new IllegalArgumentException( "asc2bin执行出错, 一个ASCII值 + [" + strAsc
                    + "],转为对应的字符时失败.", e);
        } catch (UnsupportedEncodingException e) {
            // TODO 自动生成 catch 块
            throw new IllegalArgumentException( "asc2bin执行出错, 不支持相应编码集.", e);
        }
    }

    /**
     * public static byte asc2bin(String strAsc) throws HiException { try {
     * return Integer.valueOf(strAsc).byteValue(); } catch(NumberFormatException
     * e) { // TODO log asc error throw new
     * GoException("","asc2bin执行出错, 一个ASCII值 + [" + strAsc+ "],转为对应的字符时失败.",e);
     * } }
     */
    public static String asc2bin(int intAsc) throws UnsupportedEncodingException {
        byte[] aryAsc = new byte[4];

        int2byte(aryAsc, 0, intAsc);
        /**
         * String retStr = ""; boolean start = true; for (int i = 0; i < 4; i++)
         * { if (aryAsc[i] == 0x00 && start) { continue; } start = false; retStr
         * += (char)aryAsc[i]; }
         **/
        // return new String(aryAsc);
        return byte2String(aryAsc, "ISO-8859-1");
    }

    /**
     * 将ascii串转为对应的bin; binLen限制转换后的宽度, 若binLen为0,则不限制宽度;若超过,则抛异常,否则前置加0x00
     * 
     * @param intAsc
     * @param binLen
     * @return
     * @throws GoException
     */
    public static String asc2bin(int intAsc, int binLen) throws IllegalArgumentException {
        byte[] aryAsc = new byte[4];

        int2byte(aryAsc, 0, intAsc);
        String retStr = "";

        boolean start = true;
        for (int i = 0; i < 4; i++) {
            if (aryAsc[i] == 0x00 && start) {
                continue;
            }
            start = false;
            retStr += (char) aryAsc[i];
        }

        if (retStr.length() < binLen) {
            char fill_asc = (byte) 0x00;
            String fill_str = String.valueOf(fill_asc);

            retStr = StringUtils.repeat(fill_str, binLen - retStr.length())
                    + retStr;
        } else if (retStr.length() > binLen) {
            throw new IllegalArgumentException( "ascii2bin, 超过指定的宽度 " + binLen);
        }

        return retStr;
    }

    // --------------------------------------------------------------------
    // bcd ascii convHelper
    public static String bcd2AscStr(byte[] bytes) {
        return ascii2Str(bcd2Ascii(bytes));
    }

    public static byte[] ascStr2Bcd(String s) {
        return ascii2Bcd(str2Ascii(s));
    }

    final static char[] ascii = "0123456789ABCDEF".toCharArray();

    public static byte[] bcd2Ascii(byte[] bytes) {
        byte[] temp = new byte[bytes.length * 2];

        for (int i = 0; i < bytes.length; i++) {
            temp[i * 2] = (byte) ((bytes[i] >> 4) & 0x0f);
            temp[i * 2 + 1] = (byte) (bytes[i] & 0x0f);

        }
        return temp;
    }

    public static byte[] str2Ascii(String s) {
        byte[] str = s.toUpperCase().getBytes();
        byte[] ascii = new byte[str.length];
        for (int i = 0; i < ascii.length; i++) {
            ascii[i] = (byte) asciiValue(str[i]);
        }
        return ascii;
    }

    public static String ascii2Str(byte[] ascii) {
        StringBuffer res = new StringBuffer();
        for (int i = 0; i < ascii.length; i++) {
            res.append(strValue(ascii[i]));
        }
        return res.toString();
    }

    private static char strValue(byte asc) {
        if (asc < 0 || asc > 15)
            throw new InvalidParameterException();
        return ascii[asc];
    }

    public static byte[] ascii2Bcd(byte[] asc) {
        int len = asc.length / 2;
        byte[] bcd = new byte[len];
        for (int i = 0; i < len; i++) {
            bcd[i] = (byte) ((asc[2 * i] << 4) | asc[2 * i + 1]);
        }
        return bcd;
    }

    private static int asciiValue(byte b) {
        if ((b >= '0') && (b <= '9')) {
            return (b - '0');
        }
        if ((b >= 'a') && (b <= 'f')) {
            return (b - 'a') + 0x0a;
        }
        if ((b >= 'A') && (b <= 'F')) {
            return (b - 'A') + 0x0a;
        }

        throw new InvalidParameterException();
    }

    public static void printByte(byte[] b) {
        for (int i = 0; i < b.length; i++) {
            System.out.print(b[i] + " ");
        }
        System.out.println();
    }

    // ---------------------------------------------------------------------
    /**
     * Converts two bytes into a short.
     * 
     * Note: 字节数组bp,长度必须不能大于index+2
     * 
     * The short should be interpreted as an unsigned short and if you want to
     * add it to an int then do something like the code fragment below.
     * 
     * <PRE>
     * int sum = 0;
     * sum += byte2short(buf, index) &amp; 0xffff;
     * </PRE>
     */
    public static short byte2short(byte[] bp, int index) {
        return (short) (((bp[index] & 0xff) << 8) + (bp[index + 1] & 0xff));
    }

    /**
     * Converts four bytes into an int.
     * 
     * Note: 字节数组bp的长度必须不能大于index+4
     */
    public static int byte2int(byte[] bp, int index) {
        return (int) (((bp[index] & 0xff) << 24)
                + ((bp[index + 1] & 0xff) << 16)
                + ((bp[index + 2] & 0xff) << 8) + ((bp[index + 3] & 0xff)));
    }

    /**
     * Convert short into two bytes in buffer.
     **/
    public static void short2byte(byte[] bp, int index, short value) {
        bp[index] = (byte) ((value >> 8) & 0xff);
        bp[index + 1] = (byte) (value & 0xff);
    }

    /**
     * Convert int into four bytes in buffer.
     **/
    public static void int2byte(byte[] bp, int index, int value) {
        bp[index] = (byte) ((value >> 24) & 0xff);
        bp[index + 1] = (byte) ((value >> 16) & 0xff);
        bp[index + 2] = (byte) ((value >> 8) & 0xff);
        bp[index + 3] = (byte) (value & 0xff);
    }

    /**
     * Convert an integer to an unsigned integer, represented by a long type.
     **/
    public static long int2uint(int x) {
        return ((((long) x) << 32) >>> 32);
    }

    /**
     * Convert an integer to an unsigned integer, represented by a long type.
     **/
    public static long byte2uint(byte[] x, int offs) {
        long z = 0;
        for (int i = 0; i < 4; i++) {
            z = (z << 8) + (long) (x[offs + i] & 0xff);
        }
        return z;
    }

    /**
     * Convert an array of two long integers to an byte array
     **/
    public static byte[] uint2byte(long[] x) {
        byte[] res = new byte[8];
        int2byte(res, 0, (int) (x[0]));
        int2byte(res, 4, (int) (x[1]));
        return res;
    }

    /**
     * Convert an array of a long integer to an byte array
     **/
    public static byte[] long2byte(long x) {
        byte[] res = new byte[8];
        int2byte(res, 0, (int) ((x >> 32) & 0xffffffff));
        int2byte(res, 4, (int) (x & 0xffffffff));
        return res;
    }

    public static long byte2long(byte[] msg, int offs) {
        long high = byte2uint(msg, offs);
        offs += 4;
        long low = byte2uint(msg, offs);
        offs += 4;
        long ans = (high << 32) + low;
        return ans;
    }

    /*
     * Make printable form for boolean array
     */
    public static String boolean2String(boolean[] ba) {
        StringBuffer strb = new StringBuffer();
        int cnt = 0;

        if (ba == null || ba.length == 0) {
            return "(none)";
        }

        for (int i = 0; i < ba.length; i++) {
            if (ba[i]) {
                if (cnt++ != 0) {
                    strb.append("+");
                }
                strb.append(i);
            }
        }
        return strb.toString();
    }

    public static String convFlags(String equiv, byte flags) {
        char chs[] = new char[8];
        StringBuffer strb = new StringBuffer(" ");
        int bit, i;

        if (equiv.length() > 8) {
            return (">8?");
        }
        equiv.getChars(0, equiv.length(), chs, 0);

        for (bit = 0x80, i = 0; bit != 0; bit >>= 1, i++) {
            if ((flags & bit) != 0) {
                strb.setCharAt(0, '*');
                strb.append(chs[i]);
            }
        }
        return strb.toString();
    }

    public static String timer2string(long time) {
        String timeString = null;

        long msec = time % 1000;
        String ms = String.valueOf(msec);
        ms = fill(ms, 3, "0");

        long rem = time / 1000; // in seconds
        int xsec = (int) (rem % 60);
        rem = (int) ((rem - xsec) / 60); // in minutes
        int xmin = (int) (rem % 60);
        rem = (int) ((rem - xmin) / 60); // in hours
        int xhour = (int) (rem % 24);
        int xday = (int) ((rem - xhour) / 24);

        String sday = String.valueOf(xday);
        String shour = String.valueOf(xhour);
        shour = fill(shour, 2, "0");
        String smin = String.valueOf(xmin);
        smin = fill(smin, 2, "0");
        String ssec = String.valueOf(xsec);
        ssec = fill(ssec, 2, "0");

        timeString = sday + " days, " + shour + ":" + smin + ":" + ssec + "."
                + ms;
        return timeString;
    }

    private static String fill(String str, int sz, String cfill) {
        while (str.length() < sz) {
            str = cfill + str;
        }
        return str;
    }

    /**
     * decode Asc to bcd
     * 
     * @param bytes
     * @return
     * @throws GoException
     */
    public static byte[] ascByte2Bcd(byte[] bytes) throws IllegalArgumentException {
        Hex hex = new Hex();
        try {
            bytes = hex.decode(bytes);
            hex = null;
        } catch (DecoderException e) {
            // TODO Auto-generated catch block
            throw new IllegalArgumentException(e);
        }

        return bytes;
    }

    /**
     * encode bcd to Asc
     * 
     * @param bytes
     * @return
     */
    public static byte[] bcd2AscByte(byte[] bytes) {
        Hex hex = new Hex();
        bytes = hex.encode(bytes);
        hex = null;
        return bytes;
    }
}
