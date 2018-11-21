package com.qcc.test;

public class test {
    public static void main(String[] args) {
//        String s = null;
//        String ws = null;
//        String ss ="{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"Yuen Long\",\"ID_0\":102,\"ID_1\":18,\"ISO\":\"HKG\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[114.084511,22.519991],[114.075668,22.517466],[114.078194,22.516203],[114.079460,22.516623],[114.082825,22.519150],[114.084511,22.519991]]]}}]}";
//
//
//        JSONArray features = JSON.parseObject(ss).getJSONArray("features");// 找到features的json数组
//
//        for (int i = 0; i < features.size(); i++) {
//            JSONObject info = features.getJSONObject(i);// 获取features数组的第i个json对象
//            JSONObject properties = info.getJSONObject("properties");// 找到properties的json对象
//            String name = properties.getString("name");// 读取properties对象里的name字段值
//            System.out.println(name);
//            properties.put("NAMEID", "你好");// 添加NAMEID字段
//            // properties.append("name", list.get(i));
//            System.out.println(properties.getString("NAMEID"));
//            properties.remove("ISO");// 删除ISO字段
//        }
//        ws = features.toString();
//        System.out.println(ws);
//        List<CompanyInvestRequiredData> a1 = new ArrayList<>();
//        List<CompanyInvestRequiredData> a2 = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            CompanyInvestRequiredData companyInvestKeyNoAndPercent = new CompanyInvestRequiredData();
//            companyInvestKeyNoAndPercent.setKeyNo("123" + i);
//            companyInvestKeyNoAndPercent.setPercent("1" + i + "%");
//            a1.add(companyInvestKeyNoAndPercent);
//        }
//
//
//        for (int i = 2; i < 5; i++) {
//            CompanyInvestRequiredData companyInvestKeyNoAndPercent = new CompanyInvestRequiredData();
//            companyInvestKeyNoAndPercent.setKeyNo("123" + i);
//            companyInvestKeyNoAndPercent.setPercent("1" + i + "%");
//            a2.add(companyInvestKeyNoAndPercent);
//        }
//
//        List<CompanyInvestRequiredData> a3 = new ArrayList<>();
//        CompanyInvestRequiredData companyInvestKeyNoAndPercent = new CompanyInvestRequiredData();
//        companyInvestKeyNoAndPercent.setKeyNo("1232");
//        companyInvestKeyNoAndPercent.setPercent("12%");
//        a3.add(companyInvestKeyNoAndPercent);
//
//
//        List<CompanyInvestRequiredData> a4 = new ArrayList<CompanyInvestRequiredData>() {{
//            add(a1.get(0));
//            add(a1.get(2));
//        }};
//
//        a1.removeAll(a4);
//
//        for (CompanyInvestRequiredData a : a1) {
//            System.out.println("a1===" + a.getKeyNo());
//
//        }
//
//        for (CompanyInvestRequiredData a : a2) {
//            System.out.println("a2===" + a.getKeyNo());
//
//        }

//        Map<String,String>  abc = new HashMap<>();
//        abc.put("123","abc");
//        abc.put("147","poi");
//        abc.put("136","jkl");
//        for(Map.Entry<String, String> vo : abc.entrySet()){
//            vo.getKey();
//            vo.getValue();
//            System.out.println(vo.getKey()+"  "+vo.getValue());
//        }

        String s1 = "true";
        String s2 = "false";
        boolean aa = Boolean.parseBoolean(s1);
        boolean bb = Boolean.parseBoolean(s2);
        if (bb){
            System.out.println("你好:"+s1);
        }


    }

}
