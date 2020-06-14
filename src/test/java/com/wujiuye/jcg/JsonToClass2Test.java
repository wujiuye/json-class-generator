package com.wujiuye.jcg;

import com.google.gson.Gson;
import org.junit.Test;

public class JsonToClass2Test {

    static {
        System.setProperty("jcg.classSavaPath", "/Users/wjy/MyProjects/JsonClassGenerator");
        /**
         * 是否忽略空字段
         */
        System.setProperty("jcg.ignore_null_field", "true");
    }

    static final String json = "{\n" +
            "    \"code\": 0,\n" +
            "    \"message\": \"Success\",\n" +
            "    \"data\": {\n" +
            "        \"rowset\": [\n" +
            "            {\n" +
            "                \"offer\": {\n" +
            "                    \"id\": 41320,\n" +
            "                    \"name\": \"Mashreq Neo\",\n" +
            "                    \"status\": \"active\",\n" +
            "                    \"category\": \"Non-Incent,APPLICATION\",\n" +
            "                    \"offer_approval\": 1,\n" +
            "                    \"offer_approval_msg\": \"Require Approval\",\n" +
            "                    \"tracking_link\": \"Need to apply\",\n" +
            "                    \"end_date\": 1893456061,\n" +
            "                    \"pricing_type\": \"CPI\",\n" +
            "                    \"payout\": \"7.200\",\n" +
            "                    \"percent_payout\": null,\n" +
            "                    \"preview_url\": \"https://play.google.com/store/apps/details?id=com.mashreq.NeoApp\",\n" +
            "                    \"currency\": \"USD\",\n" +
            "                    \"conversion_protocol\": 2,\n" +
            "                    \"conversion_protocol_msg\": \"Postback URL\",\n" +
            "                    \"thumbnail\": \"https://files.offerslook.com/partner_5159/campaign/thumb_sp-logo_85baf9961fdb4fac8322b19756e8ce75.png\",\n" +
            "                    \"app_id\": \"com.mashreq.NeoApp\",\n" +
            "                    \"impression_pixel\": null,\n" +
            "                    \"carrier\": [],\n" +
            "                    \"browser\": [],\n" +
            "                    \"offer_url\": []\n" +
            "                },\n" +
            "                \"offer_geo\": {\n" +
            "                    \"type\": 1,\n" +
            "                    \"target\": [\n" +
            "                        {\n" +
            "                            \"country\": \"United Arab Emirates\",\n" +
            "                            \"country_code\": \"AE\",\n" +
            "                            \"id\": 307582,\n" +
            "                            \"regions\": [],\n" +
            "                            \"cities\": []\n" +
            "                        }\n" +
            "                    ]\n" +
            "                },\n" +
            "                \"offer_platform\": {\n" +
            "                    \"target\": [\n" +
            "                        {\n" +
            "                            \"platform\": \"Mobile\",\n" +
            "                            \"system\": \"Android\",\n" +
            "                            \"version\": [\n" +
            "                                \"all\"\n" +
            "                            ],\n" +
            "                            \"is_above\": 0,\n" +
            "                            \"above_version\": \"\"\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"platform\": \"Tablet\",\n" +
            "                            \"system\": \"Android\",\n" +
            "                            \"version\": [\n" +
            "                                \"all\"\n" +
            "                            ],\n" +
            "                            \"is_above\": 0,\n" +
            "                            \"above_version\": \"\"\n" +
            "                        }\n" +
            "                    ]\n" +
            "                },\n" +
            "                \"offer_creative\": [],\n" +
            "                \"offer_event\": [\n" +
            "                    {\n" +
            "                        \"event_id\": 1,\n" +
            "                        \"conversion_protocol\": 2,\n" +
            "                        \"conversion_protocol_msg\": \"Postback URL\",\n" +
            "                        \"event_name\": \"initial event\",\n" +
            "                        \"event_payout_type\": \"CPI\",\n" +
            "                        \"event_payout\": \"7.200\",\n" +
            "                        \"event_percent_payout\": null\n" +
            "                    }\n" +
            "                ],\n" +
            "                \"offer_cap\": null,\n" +
            "                \"offer_postback\": []\n" +
            "            }\n" +
            "        ],\n" +
            "        \"totalPages\": 5724,\n" +
            "        \"totalRows\": 5724,\n" +
            "        \"offset\": 1,\n" +
            "        \"limit\": 1\n" +
            "    }\n" +
            "}";

    @Test
    public void test2() throws ClassNotFoundException {
        String className = "com.wujiuye.test2.OffersResponse";
        Class<?> tclass = JcgClassFactory.getInstance().generateResponseClassByJson(className, json);
        System.out.println(new Gson().fromJson(json, tclass));
    }

}
