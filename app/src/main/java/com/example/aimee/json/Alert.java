package com.example.aimee.json;

/**
 * Created by Aimee on 2015/12/23.
 */
public class Alert {
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublic_time() {
        return public_time;
    }

    public void setPublic_time(String public_time) {
        this.public_time = public_time;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCity_code() {
        return city_code;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }

    public String getAbnormal() {
        return abnormal;
    }

    public void setAbnormal(String abnormal) {
        this.abnormal = abnormal;
    }

    public String getPub_time() {
        return pub_time;
    }

    public void setPub_time(String pub_time) {
        this.pub_time = pub_time;
    }

    /* "abnormal": "",
                    "city_code": "101210101",
                    "detail": "杭州市气象台12月23日9时25分发布霾黄色预警信号：预计今天杭州主城区将出现重度霾，易形成重度空气污染，请注意做好防范。",
                    "holiday": "",
                    "level": "黄色",
                    "pub_time": 1450835220000,
                    "title": "杭州市气象台23日09时发布霾黄色预警!",
                    "type": "霾"
                    */
    public String pub_time, type,title,public_time,level,holiday,detail,city_code,abnormal;

}
