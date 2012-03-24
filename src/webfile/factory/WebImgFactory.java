package webfile.factory;

import java.net.URL;
import java.util.HashMap;

import org.apache.log4j.Logger;

import webfile.WebFile;
import webfile.WebImg;

/**
 * @author takahirom
 * ウェブの画像のインスタンスを作成する部分
 * 一度作ったインスタンスは二度は作らない
 * flyweightパターンを利用
 */
public class WebImgFactory extends AbstractWebFileFactory {

	protected Logger logger = Logger.getLogger(this.getClass());

	/**
	 *  これまで作ったWebFile
	 */
	private HashMap<URL, WebFile> pool = new HashMap<URL, WebFile>();

	/**
	 *  Singleton
	 */
	private static WebImgFactory singleton = new WebImgFactory();

	/**
	 * @return
	 * シングルトンを使ってインスタンスを帰す
	 */
	public static WebImgFactory getInstance() {
		return singleton;
	}

	/* (非 Javadoc)
	 * @see webfile.factory.AbstractWebFileFactory#getWebFile(java.net.URL)
	 */
	public synchronized WebFile getWebFile(URL url) {
		WebFile wf = (WebFile) pool.get(url);
		if (wf == null) {
			wf = (WebFile) new WebImg();
			pool.put(url, wf);
		}
		return wf;
	}
}
