package com.matrix.yukun.matrix.tool_module.weather.activity;

import java.util.List;

/**
 * author: kun .
 * date:   On 2019/10/10
 */
public class CityBean {

    /**
     * areaId : 110000
     * areaName : 北京市
     * cities : [{"areaId":"110000","areaName":"北京市","counties":[{"areaId":"110101","areaName":"东城区"},{"areaId":"110102","areaName":"西城区"},{"areaId":"110105","areaName":"朝阳区"},{"areaId":"110106","areaName":"丰台区"},{"areaId":"110107","areaName":"石景山区"},{"areaId":"110108","areaName":"海淀区"},{"areaId":"110109","areaName":"门头沟区"},{"areaId":"110111","areaName":"房山区"},{"areaId":"110112","areaName":"通州区"},{"areaId":"110113","areaName":"顺义区"},{"areaId":"110114","areaName":"昌平区"},{"areaId":"110115","areaName":"大兴区"},{"areaId":"110116","areaName":"怀柔区"},{"areaId":"110117","areaName":"平谷区"},{"areaId":"110228","areaName":"密云县"},{"areaId":"110229","areaName":"延庆县"}]}]
     */

    private String areaId;
    private String areaName;
    private List<CitiesBean> cities;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public List<CitiesBean> getCities() {
        return cities;
    }

    public void setCities(List<CitiesBean> cities) {
        this.cities = cities;
    }

    public static class CitiesBean {
        /**
         * areaId : 110000
         * areaName : 北京市
         * counties : [{"areaId":"110101","areaName":"东城区"},{"areaId":"110102","areaName":"西城区"},{"areaId":"110105","areaName":"朝阳区"},{"areaId":"110106","areaName":"丰台区"},{"areaId":"110107","areaName":"石景山区"},{"areaId":"110108","areaName":"海淀区"},{"areaId":"110109","areaName":"门头沟区"},{"areaId":"110111","areaName":"房山区"},{"areaId":"110112","areaName":"通州区"},{"areaId":"110113","areaName":"顺义区"},{"areaId":"110114","areaName":"昌平区"},{"areaId":"110115","areaName":"大兴区"},{"areaId":"110116","areaName":"怀柔区"},{"areaId":"110117","areaName":"平谷区"},{"areaId":"110228","areaName":"密云县"},{"areaId":"110229","areaName":"延庆县"}]
         */

        private String areaId;
        private String areaName;
        private List<CountiesBean> counties;

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public List<CountiesBean> getCounties() {
            return counties;
        }

        public void setCounties(List<CountiesBean> counties) {
            this.counties = counties;
        }

        @Override
        public String toString() {
            return "CitiesBean{" +
                    "areaId='" + areaId + '\'' +
                    ", areaName='" + areaName + '\'' +
                    ", counties=" + counties +
                    '}';
        }

        public static class CountiesBean {
            /**
             * areaId : 110101
             * areaName : 东城区
             */

            private String areaId;
            private String areaName;

            public String getAreaId() {
                return areaId;
            }

            public void setAreaId(String areaId) {
                this.areaId = areaId;
            }

            public String getAreaName() {
                return areaName;
            }

            public void setAreaName(String areaName) {
                this.areaName = areaName;
            }

            @Override
            public String toString() {
                return "CountiesBean{" +
                        "areaId='" + areaId + '\'' +
                        ", areaName='" + areaName + '\'' +
                        '}';
            }
        }

    }

    @Override
    public String toString() {
        return "CityBean{" +
                "areaId='" + areaId + '\'' +
                ", areaName='" + areaName + '\'' +
                ", cities=" + cities +
                '}';
    }
}
