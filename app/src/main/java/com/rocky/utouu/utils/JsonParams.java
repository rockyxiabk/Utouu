package com.rocky.utouu.utils;

import com.rocky.utouu.javabean.ADdetials;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiabaikui on 2015/12/14.
 */
public class JsonParams {
    /**
     * "list": [
     * {
     * "id": 791,
     * "picture": "1449825828138.jpg",
     * "end_date": -1,
     * "award_exp": 5,
     * "project_id": 2355,
     * "name": "Rekorderlig水果酒",
     * "type_name": "",
     * "view_count": 27338,
     * "award_gold": 368,
     * "start_date": 1449782640000,
     * "type_id": 1
     *
     * @param json
     * @return
     */
    public static List<ADdetials> getListAD(String json) {

        List<ADdetials> list = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json).getJSONObject("data");
            JSONArray array = object.getJSONArray("list");
            for (int i = 0; i < array.length(); i++) {
                JSONObject array_list = array.getJSONObject(i);
                ADdetials aDdetials = new ADdetials();
                aDdetials.setId(array_list.getString("id"));
                aDdetials.setPicture(array_list.getString("picture"));
                aDdetials.setEnd_date(array_list.getString("end_date"));
                aDdetials.setAward_exp(array_list.getString("award_exp"));
                aDdetials.setProject_id(array_list.getString("project_id"));
                aDdetials.setName(array_list.getString("name"));
                aDdetials.setType_name(array_list.getString("type_name"));
                aDdetials.setView_count(array_list.getString("view_count"));
                aDdetials.setAward_gold(array_list.getString("award_gold"));
                aDdetials.setStart_date(array_list.getString("start_date"));
                aDdetials.setType_id(array_list.getString("type_id"));
                list.add(aDdetials);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
