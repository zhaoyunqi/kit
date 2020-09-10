package com.github.zhaoyunqi.kit.util;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;


//      ┏┛ ┻━━━━━┛ ┻┓
//      ┃　　　　　　 ┃
//      ┃　　　━　　　┃
//      ┃　┳┛　  ┗┳　┃
//      ┃　　　　　　 ┃
//      ┃　　　┻　　　┃
//      ┗━┓　　　┏━━━┛
//        ┃　　　┃   神兽保佑
//        ┃　　　┃   代码无BUG！
//        ┃　　　┗━━━━━━━━━━━┓
//        ┃　　　　　　　     ┣┓
//        ┃　　　　         ┏┛
//        ┗━┓ ┓ ┏━━━┳ ┓ ┏━┛
//          ┃ ┫ ┫   ┃ ┫ ┫
//          ┗━┻━┛   ┗━┻━┛

public class HttpUtil {


    public static String post(String json, String url) {

        //链式构建请求
        String res = HttpRequest.post(url)
                .header(Header.CONTENT_TYPE, "application/json;charset=utf-8")
                .form(json)
                .timeout(30000)//超时，毫秒
                .execute().body();
        System.out.println(res);

        return res;
    }


}
