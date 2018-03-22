import java.util.List;

import com.br.common.bean.CommonParam;

public class TestCommon {
	
	public static void main(String[] args) {
		testCommonParam();
	}
	
	public static void testCommonParam(){
		CommonParam p = new CommonParam();
		String jsonData = "{\"bank_id\":\"\",\"brand_top_num\":\"0\",\"cell\":[\"777777\",\"18871051887\"],\"cell_md5\":[\"18871051887\"],\"cus_num\":\"4333\",\"id\":\"420683198904285423\",\"mail\":[],\"mail_md5\":[],\"meal\":\"SpecialList_c,ApplyLoan,AccountchangeMonth\",\"name\":\"宁美龄\",\"observe_date\":\"2016-09-14\",\"other_var1\":\"20153270000520\",\"other_var2\":\"现金担保贷\",\"other_var3\":\"200000\",\"other_var4\":\"100000\",\"user_date\":\"2015-11-23\"}";
		p.setJsonData(jsonData);
		
		System.out.println("==="+p.getStringByName("meal") + "---");
		
		List<String> cell = p.getStringListByName("cell");
		System.out.println(cell.get(0));
	}
}
