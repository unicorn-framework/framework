package org.unicorn.framework.base.config.xss;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.springframework.web.util.HtmlUtils;

/**
 * @author  xiebin
 */
public class JsoupUtil {
    /**
     * 使用自带的basicWithImages 白名单
     * 允许的便签有a,b,blockquote,br,cite,code,dd,dl,dt,em,i,li,ol,p,pre,q,small,span,
     * strike,strong,sub,sup,u,ul,img
     * 以及a标签的href,img标签的src,align,alt,height,width,title属性
     */
    private static final Whitelist whitelist = Whitelist.relaxed();
    /** 配置过滤化参数,不对代码进行格式化 */
    private static final Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
    static {
        // 富文本编辑时一些样式是使用style来进行实现的
        // 比如红色字体 style="color:red;"
        // 所以需要给所有标签添加style属性
        whitelist.addAttributes(":all", "style");
//        whitelist.addTags("<br>");
    }

    public static String clean(String content) {
        if(StringUtils.isNotBlank(content)){
            content = content.trim();
        }

        return HtmlUtils.htmlUnescape(Jsoup.clean(content, "", whitelist, outputSettings));
    }

    public static void main(String [] args){
        System.out.println(JsoupUtil.clean("< a href=\"javascript:alert(11)\">test</ a> <script> alert(\"test\") < a href=\"javascript:alert(11)\">test</ a> <script> alert(\"test\") </script>\n" +
                "hshdjfj  jffxccceshi\n" +
                "测试"));
        System.out.println(HtmlUtils.htmlEscape(JsoupUtil.clean("< a href=\"javascript:alert(11)\">test</ a> <script> alert(\"test\") < a href=\"javascript:alert(11)\">test</ a> <script> alert(\"test\") </script><br>" +
                "hshdjfjjffxccceshi<br>" +
                "测试")));
    }
}
