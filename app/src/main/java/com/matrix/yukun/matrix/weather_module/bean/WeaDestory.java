package com.matrix.yukun.matrix.weather_module.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/3/5.
 */
public class WeaDestory {


    /**
     * alarms : [{"level":"蓝色","stat":"预警中","title":"天津市气象台发布大风蓝色预警","txt":"预计今天后半夜员作好防范准备","type":"大风"}]
     * basic : {"city":"天津","cnty":"中国","id":"CN101030100","lat":"39.117000","lon":"117.246000","prov":"天津"}
     * update : {"loc":"2017-03-0616: 50","utc":"2017-03-0608: 50"}
     * status : ok
     */

    private List<HeWeather5Bean> HeWeather5;

    public List<HeWeather5Bean> getHeWeather5() {
        return HeWeather5;
    }

    public void setHeWeather5(List<HeWeather5Bean> HeWeather5) {
        this.HeWeather5 = HeWeather5;
    }

    public static class HeWeather5Bean {
        /**
         * city : 天津
         * cnty : 中国
         * id : CN101030100
         * lat : 39.117000
         * lon : 117.246000
         * prov : 天津
         */

        private BasicBean basic;
        /**
         * loc : 2017-03-0616: 50
         * utc : 2017-03-0608: 50
         */

        private UpdateBean update;
        private String status;
        /**
         * level : 蓝色
         * stat : 预警中
         * title : 天津市气象台发布大风蓝色预警
         * txt : 预计今天后半夜员作好防范准备
         * type : 大风
         */

        private List<AlarmsBean> alarms;

        public BasicBean getBasic() {
            return basic;
        }

        public void setBasic(BasicBean basic) {
            this.basic = basic;
        }

        public UpdateBean getUpdate() {
            return update;
        }

        public void setUpdate(UpdateBean update) {
            this.update = update;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<AlarmsBean> getAlarms() {
            return alarms;
        }

        public void setAlarms(List<AlarmsBean> alarms) {
            this.alarms = alarms;
        }

        public static class BasicBean {
            private String city;
            private String cnty;
            private String id;
            private String lat;
            private String lon;
            private String prov;

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getCnty() {
                return cnty;
            }

            public void setCnty(String cnty) {
                this.cnty = cnty;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public String getLon() {
                return lon;
            }

            public void setLon(String lon) {
                this.lon = lon;
            }

            public String getProv() {
                return prov;
            }

            public void setProv(String prov) {
                this.prov = prov;
            }
        }

        public static class UpdateBean {
            private String loc;
            private String utc;

            public String getLoc() {
                return loc;
            }

            public void setLoc(String loc) {
                this.loc = loc;
            }

            public String getUtc() {
                return utc;
            }

            public void setUtc(String utc) {
                this.utc = utc;
            }
        }

        public static class AlarmsBean {
            private String level;
            private String stat;
            private String title;
            private String txt;
            private String type;

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getStat() {
                return stat;
            }

            public void setStat(String stat) {
                this.stat = stat;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }

    @Override
    public String toString() {
        return "WeaDestory{" +
                "HeWeather5=" + HeWeather5 +
                '}';
    }
}
