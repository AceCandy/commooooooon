package com.br.common.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class MatchKeyBean {
	
	protected String code = "00";
	protected Set<String> all_map_key = new HashSet<String>();
	protected Set<String> all_ec_map_key = new HashSet<String>();
	protected Set<String> id_key = new HashSet<String>();
	protected Set<String> mail_key = new HashSet<String>();
	protected Set<String> cell_key = new HashSet<String>();
	protected boolean id_istarget = false;
	protected boolean mail_istarget = false;
	protected boolean cell_istarget = false;
	protected String id_value = "";
	protected String mail_value = "";
	protected String cell_value = "";
	protected List<NeoResultBean> hm;
	/**
	 * 对neo数据进行处理加工
	 * @param rs
	 */
	public MatchKeyBean(List<NeoResultBean> rs) {
		super();
		if (rs == null || rs.size() <= 0) {
			code = ResponseCode.resultIsNull;
			return;
		}
		setHm(rs);// 将结果集进行bean化
		ArrayList<HashMap<String, Boolean>> listId = new ArrayList<HashMap<String, Boolean>>();
		ArrayList<HashMap<String, Boolean>> listCell = new ArrayList<HashMap<String, Boolean>>();
		ArrayList<HashMap<String, Boolean>> listMail = new ArrayList<HashMap<String, Boolean>>();
		for (int i = 0; i < rs.size(); i++) {
			NeoResultBean tmp = rs.get(i);
			String type = tmp.getNeoKeyType();
			String key = tmp.getNeoKey().trim();
			boolean target = tmp.isTarget();
			HashMap<String, Boolean> map = new HashMap<String, Boolean>();
			map.put(key, target);
			if (type.startsWith("id")) {
				listId.add(map);
			} else if (type.startsWith("cell")) {
				listCell.add(map);
			} else if (type.startsWith("mail")) {
				listMail.add(map);
			}
		}
		if (listId != null) {
			for (HashMap<String, Boolean> m : listId) {
				for (Iterator<String> i = m.keySet().iterator(); i.hasNext();) {
					String key1 = i.next();
					if (m.get(key1)) {
						id_istarget = true;
						id_value = key1;
					}
					if (StringUtils.isNotBlank(key1)) {
						id_key.add(key1.trim());
					}
				}
			}
		}
		if (listCell != null) {
			for (HashMap<String, Boolean> m : listCell) {
				for (Iterator<String> i = m.keySet().iterator(); i.hasNext();) {
					String key1 = i.next();
					if (m.get(key1)) {
						cell_istarget = true;
						cell_value = key1;
					}
					if (StringUtils.isNotBlank(key1)) {
						cell_key.add(key1.trim());
					}
				}
			}
		}
		if (listMail != null) {
			for (HashMap<String, Boolean> m : listMail) {
				for (Iterator<String> i = m.keySet().iterator(); i.hasNext();) {
					String key1 = i.next();
					if (m.get(key1)) {
						mail_istarget = true;
						mail_value = key1;
					}
					if (StringUtils.isNotBlank(key1)) {
						mail_key.add(key1.trim());
					}
				}
			}
		}
		
		all_map_key.addAll(id_key);
		all_map_key.addAll(mail_key);
		all_map_key.addAll(cell_key);
	}

	@SuppressWarnings("unused")
	private boolean check(List<String> dsList2, String ds) {
		boolean res = false;
		for (String s : dsList2) {
			if (ds.indexOf(s) > -1) {
				res = true;
				break;
			}
		}
		return res;
	}

	public Set<String> getAll_ec_map_key() {
		return all_ec_map_key;
	}

	public void setAll_ec_map_key(Set<String> all_ec_map_key) {
		this.all_ec_map_key = all_ec_map_key;
	}
	
	/**
	 * 获取从neo数据过来的id,cell,mailKey并存储
	 * @return
	 */
	public Set<String> getAll_map_key() {
		return all_map_key;
	}

	public void setAll_map_key(Set<String> all_map_key) {
		this.all_map_key = all_map_key;
	}

	public List<NeoResultBean> getHm() {
		return hm;
	}

	public void setHm(List<NeoResultBean> hm) {
		this.hm = hm;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 从neo过来的id
	 * @return
	 */
	public Set<String> getId_key() {
		return id_key;
	}

	public void setId_key(Set<String> id_key) {
		this.id_key = id_key;
	}
	/**
	 * 获取从neo过来的mail
	 * @return
	 */
	public Set<String> getMail_key() {
		return mail_key;
	}

	public void setMail_key(Set<String> mail_key) {
		this.mail_key = mail_key;
	}
	/**
	 * 获取neo过来的cell
	 * @return
	 */
	public Set<String> getCell_key() {
		return cell_key;
	}

	public void setCell_key(Set<String> cell_key) {
		this.cell_key = cell_key;
	}

	public boolean isId_istarget() {
		return id_istarget;
	}

	public void setId_istarget(boolean id_istarget) {
		this.id_istarget = id_istarget;
	}

	public boolean isMail_istarget() {
		return mail_istarget;
	}

	public void setMail_istarget(boolean mail_istarget) {
		this.mail_istarget = mail_istarget;
	}

	public boolean isCell_istarget() {
		return cell_istarget;
	}

	public void setCell_istarget(boolean cell_istarget) {
		this.cell_istarget = cell_istarget;
	}


	public void setId_value(String id_value) {
		this.id_value = id_value;
	}

	public String getMail_value() {
		return mail_value;
	}

	public void setMail_value(String mail_value) {
		this.mail_value = mail_value;
	}

	public String getCell_value() {
		return cell_value;
	}

	public void setCell_value(String cell_value) {
		this.cell_value = cell_value;
	}

}
