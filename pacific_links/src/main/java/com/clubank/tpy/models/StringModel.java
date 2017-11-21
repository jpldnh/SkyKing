package com.clubank.tpy.models;

import com.sking.lib.base.SKObject;
import com.sking.lib.exception.SKDataParseException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 针对服务器返回数据只有一个字段，如tomptoken，一次添加即可
 */
public class StringModel extends SKObject implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String token;// token
    private String temp_token;// 临时token


    public StringModel(JSONObject jsonObject) throws SKDataParseException {
        if (jsonObject != null) {
            try {
                token = get(jsonObject, "token");
                temp_token = get(jsonObject, "temp_token");

                log_i(toString());
            } catch (JSONException e) {
                throw new SKDataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "Notice [temp_token=" + temp_token + "token=" + token + "]";
    }

    public String getTemp_token() {
        return temp_token;
    }

    public void setTemp_token(String temp_token) {
        this.temp_token = temp_token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
