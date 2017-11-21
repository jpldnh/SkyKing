package com.sking.lib.utils;

import com.sking.lib.base.SKObject;
import com.sking.lib.exception.SKDataParseException;

import org.json.JSONObject;


/**
 * JSON工具类
 */
public class SKJsonUtil extends SKObject {
    /**
     * 字符串转JSON
     *
     * @param s 需要转换的字符串
     * @return JSONObject
     * @throws SKDataParseException
     * 2017.09.08 update 新增对xml的支持
     */
    public static JSONObject toJsonObject(String s) throws SKDataParseException {
        try {
            if (s != null && s.startsWith("\ufeff")) // 避免低版本utf-8bom头问题
                s = s.substring(1);
            if (isXML(s))
                return SKTransfromUtil.getDefaultJson(true, "操作成功！", s.trim().replace("\"", "\\\""));
            else
                return new JSONObject(s.trim());
        } catch (Exception e) {
            throw new SKDataParseException(e);
        }
    }
}
