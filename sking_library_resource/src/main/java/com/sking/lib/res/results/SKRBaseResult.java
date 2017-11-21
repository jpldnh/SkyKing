package com.sking.lib.res.results;

import com.sking.lib.base.SKObject;
import com.sking.lib.exception.SKDataParseException;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 最基本的服务器返回结果
 */
public class SKRBaseResult extends SKObject {
    private boolean success;// 服务器处理状态
    private String msg;// 服务器返回的描述信息
    private int error_code;// 当status==0时，会有一个对应的error_code。详见错误编码表
    private String info;// 服务器返回info的数据
    private JSONObject json;//服务器返回整个数据

    /**
     * 实例化一个最基本的服务器返回结果
     *
     * @param jsonObject 一个JSONObject实例
     * @throw SKDataParseException
     * 数据解析异常
     */
    public SKRBaseResult(JSONObject jsonObject) throws SKDataParseException {
        if (jsonObject != null) {
            try {
                json = jsonObject;

                if (!jsonObject.isNull("success")) {
                    success = jsonObject.getBoolean("success");
                }
                msg = get(jsonObject, "msg");
                if (!jsonObject.isNull("error_code")) {
                    error_code = jsonObject.getInt("error_code");
                }
                if (!jsonObject.isNull("infor")) {
                    info = jsonObject.getString("infor");
                }
            } catch (JSONException e) {
                throw new SKDataParseException(e);
            }
        }
    }

    /**
     * @return 服务器执行状态
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @return 服务器返回的描述信息
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 获取error_code值
     *
     * @return 一个整数(当status==0时，会有一个对应的error_code。详见错误编码表)
     */
    public int getError_code() {
        return error_code;
    }

    /**
     * 获取服务器返回info数据
     *
     * @return 若无返回值 info为null
     */
    public String getInfo() {
        return info;
    }

    /**
     * 获取服务器返回Json数据
     *
     * @return 若无返回值 json为null 返回new JsonObject
     */
    public JSONObject getJsonObject() {
        if (json == null)
            return new JSONObject();
        else
            return json;
    }

}