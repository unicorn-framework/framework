package org.unicorn.framework.web.utils.image;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.util.HtmlUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ImageUtils {

    public static void compress() throws Exception {
        Thumbnails.of("D:/test.xx-s-5.jpg")
                .scale(0.5f)
                .outputQuality(0.8f)
                .toFile("D:/xx-s-2.jpg");
    }

    public static OutputStream compressOutputStream(InputStream inputStream) throws Exception {
        OutputStream out = new ByteArrayOutputStream();
        Thumbnails.of(inputStream)
                .scale(0.5f)
                .outputQuality(0.8f)
                .toOutputStream(out);
        return out;
    }


    public static void main(String args[]) throws Exception {
////        Long startTime=System.currentTimeMillis();
////        compress();
////        System.out.println(System.currentTimeMillis()-startTime);
        String ss = HtmlUtils.htmlEscape("<a href=\"javascript:alert(11)\">test</a>\n" +
                "<button onclick=\"alert(1)\">按钮</button>\n" +
                "<script>\n" +
                "    alert(\"test\")\n" +
                "</script>");
        System.out.println(ss);
//        //        System.out.println(HtmlUtils.htmlUnescape(ss));
//

//        String ssr []=new String[]{"test","555"};
//        System.out.println(StringUtils.join(ssr,"<br />"));
    }
}
