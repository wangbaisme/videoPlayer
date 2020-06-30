package com.zc.xxj.utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
    private static final String TAG = JsonUtil.class.getSimpleName();

    public static List<String> getConfigureJson(Context context, String module, String moduleItem){
        List<String> list = new ArrayList<>();
        if (SPUtil.getInstance(context).getString(module) == null)
            return list;
        try {
            JSONArray jsonArray = new JSONArray(SPUtil.getInstance(context).getString(module));
            JSONObject jsonObject;
            for (int i=0; i<jsonArray.length(); i++){
                jsonObject = (JSONObject) jsonArray.get(i);
                list.add(jsonObject.getString(moduleItem));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void updateConfigureJson(Context context, List<String> list, String module, String moduleItem){
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i=0; i<list.size(); i++){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(moduleItem, list.get(i));
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SPUtil.getInstance(context).putString(module, jsonArray.toString());
    }

    public static void updateHistoryJson(Context context, List<String> list, String module, String moduleItem){
        JSONArray jsonArray = new JSONArray();
        int index = list.size() >= 10 ? 10 : list.size();
        try {
            for (int i=0; i<index; i++){
                JSONObject jsonObject = new JSONObject();
                if (i == 0)
                    jsonObject.put(moduleItem, list.get(list.size()-1));
                else
                    jsonObject.put(moduleItem, list.get(i-1));
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SPUtil.getInstance(context).putString(module, jsonArray.toString());
    }


    public static String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
//				如果需要多候选结果，解析数组其他字段
//				for(int j = 0; j < items.length(); j++)
//				{
//					JSONObject obj = items.getJSONObject(j);
//					ret.append(obj.getString("w"));
//				}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }

    public static String parseGrammarResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                for(int j = 0; j < items.length(); j++)
                {
                    JSONObject obj = items.getJSONObject(j);
                    if(obj.getString("w").contains("nomatch"))
                    {
                        ret.append("没有匹配结果.");
                        return ret.toString();
                    }
                    ret.append("【结果】" + obj.getString("w"));
                    ret.append("【置信度】" + obj.getInt("sc"));
                    ret.append("\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret.append("没有匹配结果.");
        }
        return ret.toString();
    }

    public static String parseLocalGrammarResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                for(int j = 0; j < items.length(); j++)
                {
                    JSONObject obj = items.getJSONObject(j);
                    if(obj.getString("w").contains("nomatch"))
                    {
                        ret.append("没有匹配结果.");
                        return ret.toString();
                    }
                    ret.append("【结果】" + obj.getString("w"));
                    ret.append("\n");
                }
            }
            ret.append("【置信度】" + joResult.optInt("sc"));

        } catch (Exception e) {
            e.printStackTrace();
            ret.append("没有匹配结果.");
        }
        return ret.toString();
    }

    public static String parseTransResult(String json,String key) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);
            String errorCode = joResult.optString("ret");
            if(!errorCode.equals("0")) {
                return joResult.optString("errmsg");
            }
            JSONObject transResult = joResult.optJSONObject("trans_result");
            ret.append(transResult.optString(key));
			/*JSONArray words = joResult.getJSONArray("results");
			for (int i = 0; i < words.length(); i++) {
				JSONObject obj = words.getJSONObject(i);
				ret.append(obj.getString(key));
			}*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }
}
