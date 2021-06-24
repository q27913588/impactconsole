package biz.mercue.impactweb.util;

import java.util.Map;

public class YamlProperty {

	private String ip;
	private Map<String, String> jdbc;
	private Map<String, String> hibernate;
	private Map<String, String> path;
	private Map<String, String> url;
	private Map<String, String> mail;

	
	private Map<String, String> tuya;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public Map<String, String> getJdbc() {
		return jdbc;
	}
	public void setJdbc(Map<String, String> jdbc) {
		this.jdbc = jdbc;
	}
	
	public Map<String, String> getHibernate() {
		return hibernate;
	}
	public void setHibernate(Map<String, String> hibernate) {
		this.hibernate = hibernate;
	}
	
	public Map<String, String> getPath() {
		return path;
	}
	public void setPath(Map<String, String> path) {
		this.path = path;
	}

	public Map<String, String> getUrl() {
		return url;
	}
	public void setUrl(Map<String, String> url) {
		this.url = url;
	}


	public Map<String, String> getMail() {
		return mail;
	}
	public void setMail(Map<String, String> mail) {
		this.mail = mail;
	}

	
	public Map<String, String> getTuya() {
		return tuya;
	}
	public void setTuya(Map<String, String> tuya) {
		this.tuya = tuya;
	}
	
}
