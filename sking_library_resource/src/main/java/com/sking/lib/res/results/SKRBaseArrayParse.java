package com.sking.lib.res.results;

import android.annotation.TargetApi;
import android.os.Build;

import com.sking.lib.exception.SKDataParseException;

import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 实例化数据解析类，
 * 如需调用私有的构造方法可使用getDeclaredConstructor方法
 *
 * Created by 谁说青春不能错 on 2016/12/7.
 */

public class SKRBaseArrayParse<T> extends SKRArrayBaseResult<T> {

    public SKRBaseArrayParse(JSONObject jsonObject, Class<T> tClass) throws SKDataParseException {
        super(jsonObject,tClass);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public T parse(JSONObject jsonObject,Class<T> classType) throws SKDataParseException {
        T classT = null;
        try {
            Constructor<T> constructor = (Constructor<T>) classType.getConstructor(JSONObject.class);
            constructor.setAccessible(true);
            classT = constructor.newInstance(jsonObject);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return classT;
    }
}
