package com.br.common.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.br.common.util.ValidateUtil;

public class NeoBean {
	private Map<String, String> map_key = new HashMap<String, String>();
	private Map<String, String> matchMap = new HashMap<String, String>();
	private String matchType = "";

	public NeoBean(List<NeoResultBean> rs) {
		super();
		if (rs == null || rs.size() <= 0) {
			return;
		}
		for (int i = 0; i < rs.size(); i++) {
			NeoResultBean tmp = rs.get(i);
			String type = tmp.getNeoKeyType();
			String key = tmp.getNeoKey();// 明文key
			if ("id".equals(type) && ValidateUtil.checkId(key)) {
				map_key.put("id", key);
			}
			if ("cell".equals(type) && ValidateUtil.checkCell(key)) {
				map_key.put("cell", key);
			}
			if ("mail".equals(type) && ValidateUtil.checkMail(key)) {
				map_key.put("mail", key);
			}
			if ("weibo".equals(type)) {
				if ("mail".equals(tmp.getRequestKeyType())) {
					matchMap.put("mail", key);
				} else if ("cell".equals(tmp.getRequestKeyType())) {
					matchMap.put("cell", key);
				}
				map_key.put("weibo", key);
			}
		}
		if (matchMap.size() == 0) {
			matchType = "0";
		} else if (matchMap.size() == 1 && matchMap.containsKey("cell")) {
			matchType = "1";
		} else if (matchMap.size() == 1 && matchMap.containsKey("mail")) {
			matchType = "2";
		} else if (matchMap.size() > 1 && matchMap.containsKey("cell")
				&& matchMap.containsKey("mail")) {
			if (matchMap.get("cell") != null
					&& matchMap.get("cell").equals(matchMap.get("mail"))) {
				matchType = "3";
			} else if (matchMap.get("cell") != null
					&& !matchMap.get("cell").equals(matchMap.get("mail"))) {
				matchType = "4";
			}
		}

	}

	public String getMatchType() {
		return matchType;
	}

	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}

	public Map<String, String> getMap_key() {
		return map_key;
	}

	public void setMap_key(Map<String, String> map_key) {
		this.map_key = map_key;
	}
}
