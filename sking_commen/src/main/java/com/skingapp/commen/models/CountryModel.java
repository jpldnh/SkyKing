package com.skingapp.commen.models;

import java.util.List;

/**
 * Created by Im_jingwei on 2017/9/8.
 */
public class CountryModel {


    /**
     * ProcessResultOfCountry : {"xmlns:xsi":"http://www.w3.org/2001/XMLSchema-instance","xmlns:xsd":"http://www.w3.org/2001/XMLSchema","xmlns":"http://tempuri.org/","APIStatus":10,"SessionID":"544a8dd1-1a3c-444a-9a90-c5a68cb1b24d","List":{"Country":[{"CountryID":4,"CountryName":"美国","CountryEName":"United States"},{"CountryID":5,"CountryName":"德国","CountryEName":"Germany"},{"CountryID":6,"CountryName":"日本","CountryEName":"Japan"},{"CountryID":7,"CountryName":"澳大利亚","CountryEName":"Australian"},{"CountryID":10,"CountryName":"英国","CountryEName":"England"},{"CountryID":11,"CountryName":"新西兰","CountryEName":"New Zealand"},{"CountryID":12,"CountryName":"波多黎各","CountryEName":"Puerto Rico"},{"CountryID":13,"CountryName":"法国","CountryEName":"France"},{"CountryID":14,"CountryName":"韩国","CountryEName":"South Korea"},{"CountryID":15,"CountryName":"印尼","CountryEName":"Indonesia"},{"CountryID":16,"CountryName":"墨西哥","CountryEName":"Mexico"},{"CountryID":17,"CountryName":"加拿大","CountryEName":"Canada"},{"CountryID":18,"CountryName":"瑞士","CountryEName":"Switzerland"},{"CountryID":19,"CountryName":"泰国","CountryEName":"Thailand"},{"CountryID":20,"CountryName":"越南","CountryEName":"Vietnam"},{"CountryID":21,"CountryName":"柬埔寨","CountryEName":"Cambodia"},{"CountryID":22,"CountryName":"菲律宾","CountryEName":"Philippines"},{"CountryID":23,"CountryName":"百慕大","CountryEName":"Bermuda"},{"CountryID":24,"CountryName":"马来西亚","CountryEName":"Malaysia"},{"CountryID":25,"CountryName":"新加坡","CountryEName":"Singapore"},{"CountryID":26,"CountryName":"奥地利","CountryEName":"Austria"},{"CountryID":27,"CountryName":"比利时","CountryEName":"Belgium"},{"CountryID":28,"CountryName":"意大利","CountryEName":"Italy"},{"CountryID":29,"CountryName":"西班牙","CountryEName":"Spain"},{"CountryID":30,"CountryName":"葡萄牙","CountryEName":"Portugal"},{"CountryID":31,"CountryName":"哥伦比亚","CountryEName":"Colombia"},{"CountryID":32,"CountryName":"新喀里多尼亚","CountryEName":"New Caledonia"},{"CountryID":33,"CountryName":"卢森堡","CountryEName":"The Grand Duchy of Luxembourg"},{"CountryID":34,"CountryName":"毛里求斯","CountryEName":"Mauritius"},{"CountryID":35,"CountryName":"阿联酋","CountryEName":"UAE"},{"CountryID":36,"CountryName":"南非","CountryEName":"The Republic of South Africa"},{"CountryID":37,"CountryName":"朝鲜","CountryEName":"Democratic People's Republic of Korea"},{"CountryID":38,"CountryName":"丹麦","CountryEName":"Danmark"}]},"Total":0,"ResultOther":0,"ReturnCode":0}
     */

    private ProcessResultOfCountryBean ProcessResultOfCountry;

    public ProcessResultOfCountryBean getProcessResultOfCountry() {
        return ProcessResultOfCountry;
    }

    public void setProcessResultOfCountry(ProcessResultOfCountryBean ProcessResultOfCountry) {
        this.ProcessResultOfCountry = ProcessResultOfCountry;
    }

    public static class ProcessResultOfCountryBean {
        private String _$XmlnsXsi320; // FIXME check this code
        private String _$XmlnsXsd201; // FIXME check this code
        private String xmlns;
        private int APIStatus;
        private String SessionID;
        private ListBean List;
        private int Total;
        private int ResultOther;
        private int ReturnCode;

        public String get_$XmlnsXsi320() {
            return _$XmlnsXsi320;
        }

        public void set_$XmlnsXsi320(String _$XmlnsXsi320) {
            this._$XmlnsXsi320 = _$XmlnsXsi320;
        }

        public String get_$XmlnsXsd201() {
            return _$XmlnsXsd201;
        }

        public void set_$XmlnsXsd201(String _$XmlnsXsd201) {
            this._$XmlnsXsd201 = _$XmlnsXsd201;
        }

        public String getXmlns() {
            return xmlns;
        }

        public void setXmlns(String xmlns) {
            this.xmlns = xmlns;
        }

        public int getAPIStatus() {
            return APIStatus;
        }

        public void setAPIStatus(int APIStatus) {
            this.APIStatus = APIStatus;
        }

        public String getSessionID() {
            return SessionID;
        }

        public void setSessionID(String SessionID) {
            this.SessionID = SessionID;
        }

        public ListBean getList() {
            return List;
        }

        public void setList(ListBean List) {
            this.List = List;
        }

        public int getTotal() {
            return Total;
        }

        public void setTotal(int Total) {
            this.Total = Total;
        }

        public int getResultOther() {
            return ResultOther;
        }

        public void setResultOther(int ResultOther) {
            this.ResultOther = ResultOther;
        }

        public int getReturnCode() {
            return ReturnCode;
        }

        public void setReturnCode(int ReturnCode) {
            this.ReturnCode = ReturnCode;
        }

        public static class ListBean {

            private java.util.List<CountryBean> Country;

            public List<CountryBean> getCountry() {
                return Country;
            }

            public void setCountry(List<CountryBean> Country) {
                this.Country = Country;
            }

            public static class CountryBean {
                /**
                 * CountryID : 4
                 * CountryName : 美国
                 * CountryEName : United States
                 */

                private int CountryID;
                private String CountryName;
                private String CountryEName;

                public int getCountryID() {
                    return CountryID;
                }

                public void setCountryID(int CountryID) {
                    this.CountryID = CountryID;
                }

                public String getCountryName() {
                    return CountryName;
                }

                public void setCountryName(String CountryName) {
                    this.CountryName = CountryName;
                }

                public String getCountryEName() {
                    return CountryEName;
                }

                public void setCountryEName(String CountryEName) {
                    this.CountryEName = CountryEName;
                }
            }
        }
    }
}
