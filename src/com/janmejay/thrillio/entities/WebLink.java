package com.janmejay.thrillio.entities;

import com.janmejay.thrillio.partner.Sharable;

public class WebLink extends Bookmark implements Sharable {
	private String url;
	private String host;
	private String htmlPage;
	private DownloadStatus downloadStatus=DownloadStatus.NOT_ATTEMPTED;
	
	public enum DownloadStatus{
		NOT_ATTEMPTED,
		SUCCESS,
		FAILED,
		NOT_ELIGIBLE;
	}
	
	public String getHtmlPage() {
		return htmlPage;
	}

	public void setHtmlPage(String htmlPage) {
		this.htmlPage = htmlPage;
	}

	public DownloadStatus getDownloadStatus() {
		return downloadStatus;
	}

	public void setDownloadStatus(DownloadStatus downloadStatus) {
		this.downloadStatus = downloadStatus;
	}

	

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	@Override
	public String toString() {
		return "WebLink [url=" + url + ", host=" + host + "]";
	}

	public boolean isKidFriendly() {
		if (url.contains("Porn") || getTitle().contains("Porn") || host.contains("adult")) {
			return false;
		}
		return true;
	}

	@Override
	public String getItemData() {
		StringBuilder sb = new StringBuilder();
		sb.append("<item>");
		sb.append("<type>Weblink</type>");
		sb.append("<title>").append(getTitle()).append("</title>");
		sb.append("<host>").append(host).append("</host>");
		sb.append("<url>").append(url).append("</url>");
		sb.append("</item>");
		return sb.toString();
	}
}
