package com.sking.lib.res.results;

import com.sking.lib.exception.SKDataParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

/**
 * 对BaseResult的拓展，适用返回数据中有对象，集合以及集合分页的情况
 */
public abstract class SKRArrayBaseResult<T> extends SKRBaseResult {
    private ArrayList<T> objects = new ArrayList<T>();
    private int totalCount;

    public SKRArrayBaseResult(JSONObject jsonObject, Class<T> tClass) throws SKDataParseException {
        super(jsonObject);
        if (jsonObject != null) {
            try {
                if (!jsonObject.isNull("infor") && !isNull(jsonObject.getString("infor"))) {

                    Object json = new JSONTokener(jsonObject.getString("infor")).nextValue();
                    if (json instanceof JSONObject) {
                        //further actions on jsonObjects
                        JSONObject object = jsonObject.getJSONObject("infor");
                        if (!object.isNull("totalCount")) {
                            totalCount = object.getInt("totalCount");
                        }

                        if (!object.isNull("listItems") && !isNull(object.getString("listItems"))) {
                            JSONArray jsonList = object.getJSONArray("listItems");
                            int size = jsonList.length();
                            for (int i = 0; i < size; i++) {
                                objects.add(parse(jsonList.getJSONObject(i),tClass));
                            }
                        }

                    } else if (json instanceof JSONArray) {
                        //further actions on jsonArray
                        JSONArray jsonList = jsonObject.getJSONArray("infor");
                        int size = jsonList.length();
                        for (int i = 0; i < size; i++) {
                            objects.add(parse(jsonList.getJSONObject(i),tClass));
                        }
                    }
                }
            } catch (JSONException e) {
                throw new SKDataParseException(e);
            }
        }
    }

    /**
     * @return the totalCount 表示所有符合查询条件的总记录的个数（totalCount=0 表示暂无数据）
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     * 获取服务器返回的实例集合
     *
     * @return 服务器返回的实例集合
     */
    public ArrayList<T> getObjects() {
        return objects;
    }

    /**
     * 该方法将JSONObject解析为具体的数据实例
     */
    public abstract T parse(JSONObject jsonObject,Class<T> tClass) throws SKDataParseException;

}
