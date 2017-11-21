package com.skingapp.commen.nettasks;


import com.sking.lib.exception.SKDataParseException;
import com.sking.lib.res.results.SKRBaseArrayParse;
import com.skingapp.commen.bases.BaseHttpInformation;
import com.skingapp.commen.bases.BaseNetTask;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 网络任务，分装无返回值，返回单个对象多个对象及分页
 * <p>
 * create bg jingwei 2016-12-02
 */
public class ExecuteNetTask<T> extends BaseNetTask {

    private Class<T> classType;

    public ExecuteNetTask(BaseHttpInformation information,
                          HashMap<String, String> params) {
        super(information, params);
    }

    public ExecuteNetTask(BaseHttpInformation information,
                          HashMap<String, String> params, Class<T> classType) {
        super(information, params);
        this.classType = classType;
    }

    public ExecuteNetTask(BaseHttpInformation information,
                          HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }
    public ExecuteNetTask(BaseHttpInformation information,

                          HashMap<String, String> params, HashMap<String, String> files, Class<T> classType) {
        super(information, params, files);
        this.classType = classType;
    }

    @Override
    public Object parse(JSONObject jsonObject) throws SKDataParseException {
        return new SKRBaseArrayParse<T>(jsonObject,classType);
    }

}
