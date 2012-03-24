package webfile.factory;

import java.net.URL;
import java.util.HashMap;

import org.apache.log4j.Logger;

import webfile.WebFile;
import webfile.WebPage;

/**
 * @author takahirom ウェブページのインスタンスを作成する部分 一度作ったインスタンスは二度は作らない flyweightパターンを利用
 */
public class WebPageFactory extends AbstractWebFileFactory {

	protected Logger logger = Logger.getLogger(this.getClass());
	/**
	 * これまで作ったWebFile
	 */
	private HashMap<URL, WebFile> pool = new HashMap<URL, WebFile>();
	/**
	 *  Singleton
	 */
	private static WebPageFactory singleton = new WebPageFactory();

	/**
	 * @return
	 * シングルトンを使ってインスタンスを返す
	 */
	public static WebPageFactory getInstance() {
		return singleton;
	}

	public synchronized WebFile getWebFile(URL url) {
		WebFile wf = (WebFile) pool.get(url);
		if (wf == null) {
			wf = (WebFile) new WebPage();
			pool.put(url, wf);
		}
		return wf;
	}

}
