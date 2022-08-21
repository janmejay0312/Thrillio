package com.janmejay.thrillio.bgjobs;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.janmejay.thrillio.dao.BookmarkDao;
import com.janmejay.thrillio.entities.WebLink;
import com.janmejay.thrillio.util.HttpConnect;
import com.janmejay.thrillio.util.IOUtil;

public class WebpageDownloaderTask implements Runnable{
private static BookmarkDao bookmarkDao=new BookmarkDao();
public static final long TIME_FRAME=30000000000L;//3 SECONDS
private boolean downloadAll=false;
ExecutorService downloadExecutor = Executors.newFixedThreadPool(2);


private static class Downloader<T extends WebLink> implements Callable<T> {
	private T weblink;
	public Downloader(T weblink) {
		this.weblink = weblink;
	}
	public T call() {
		try {
			if(!weblink.getUrl().endsWith(".pdf")) {
				weblink.setDownloadStatus(WebLink.DownloadStatus.FAILED);
			String htmlPage = HttpConnect.download(weblink.getUrl());
			weblink.setHtmlPage(htmlPage);	
			}else {
				weblink.setDownloadStatus(WebLink.DownloadStatus.NOT_ELIGIBLE);
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return weblink;
	}
}



public WebpageDownloaderTask(boolean downloadAll) {
	this.downloadAll = downloadAll;
}

@Override
public void run() {
	// TODO Auto-generated method stub
	while(!Thread.currentThread().isInterrupted()) {
		List<WebLink>webLinks=getWebLinks();
		if(webLinks.size()>0) {
			download(webLinks);
		}
		else {
			System.out.println("no new weblink to download");
		}
		try {
			TimeUnit.SECONDS.sleep(15);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	downloadExecutor.shutdown();
}

private void download(List<WebLink> webLinks) {
	List<Downloader<WebLink>> tasks = getTask(webLinks);
	List<Future<WebLink>> futures = new ArrayList<>();
			
	
	try {
		futures = downloadExecutor.invokeAll(tasks, TIME_FRAME, TimeUnit.NANOSECONDS);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	for (Future<WebLink> future : futures) {
		try {
			if (!future.isCancelled()) {
				WebLink webLink=future.get();
				String webPage=webLink.getHtmlPage();
				if(webPage!=null)
				{
					IOUtil.write(webPage, webLink.getId());
					webLink.setDownloadStatus(WebLink.DownloadStatus.SUCCESS);
					System.out.println("WebPage Downloaded : "+webLink.getUrl());
				}
				else {
					System.out.println("WebPage not Downloaded : "+webLink.getUrl());
				}
			} else {
				System.out.println("\n\nTask is cancelled --> " + Thread.currentThread());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
}

private List<Downloader<WebLink>> getTask(List<WebLink> webLinks) {
	List<Downloader<WebLink>>task=new ArrayList<>();
	for(WebLink webLink:webLinks) {
		task.add(new Downloader<WebLink>(webLink));
	}
	return task;
}

private List<WebLink> getWebLinks() {
	List<WebLink>webLinks=null;
	if(downloadAll) {
		webLinks=bookmarkDao.getAllWebLinks();
		downloadAll=false;
	}else {
		webLinks=bookmarkDao.getWebLinks(WebLink.DownloadStatus.NOT_ATTEMPTED);
	}
	return webLinks;
}

}
