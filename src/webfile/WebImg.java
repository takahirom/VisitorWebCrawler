package webfile;

import java.io.File;
import java.net.URL;
import java.util.Stack;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import webfile.util.DownloadImage;

import crawler.Visitor;

/**
 * @author takahirom
 *　画像を扱うクラス　Visitorを受け入れ可能
 */
public class WebImg extends WebFile {
	protected Logger logger = Logger.getLogger(this.getClass());
	static {
		BasicConfigurator.configure();// log4jの設定
	}

	private Integer layer;
	private URL url;
	private String savePath;
	private Stack<String> savePathStack = new Stack<String>();
	private Boolean visited = false;

	/* (非 Javadoc)
	 * @see webfile.Element#accept(crawler.Visitor)
	 */
	@Override
	public final void accept(final Visitor v) {
		// TODO 自動生成されたメソッド・スタブ
		v.visit(this);
	}

	/* (非 Javadoc)
	 * @see webfile.WebFile#setLayer(java.lang.Integer)
	 */
	@Override
	public final void setLayer(final Integer layer) {
		// TODO 自動生成されたメソッド・スタブ
		this.layer = layer;
	}

	/* (非 Javadoc)
	 * @see webfile.WebFile#getLayer()
	 */
	@Override
	public final Integer getLayer() {
		// TODO 自動生成されたメソッド・スタブ
		return this.layer;
	}

	/* (非 Javadoc)
	 * @see webfile.WebFile#setUrl(java.net.URL)
	 */
	public final void setUrl(final URL url) {
		// TODO 自動生成されたメソッド・スタブ
		this.url = url;
	}

	/**
	 * @return url この画像のurl
	 */
	public final URL getUrl() {
		// TODO 自動生成されたメソッド・スタブ
		return this.url;
	}

	/* (非 Javadoc)
	 * @see webfile.WebFile#write()
	 */
	public final void write() {
		setSavePath(popSavePathStack());
		File newimgdir = new File("./" + getSavePath() + "/img");
		newimgdir.mkdir();

		DownloadImage downloader = new DownloadImage();
		downloader.setUrl(this.url);
		String fname = null;
		fname = new File(url.toString()).getName();

		if (fname.length() > 100) {
			fname.substring(0, 100);
		}
		File f = new File("./" + savePath + "/img/" + fname);
		downloader.setDownloadPath(f.getPath());

		downloader.download();
	}

	public final String getSavePath() {
		return savePath;
	}

	public final void setSavePath(final String savePath) {
		this.savePath = savePath;
	}

	public final String popSavePathStack() {
		return savePathStack.pop();
	}

	public final void pushSavePathStack(final String push) {
		savePathStack.push(push);
	}

	public final Boolean getVisited() {
		return visited;
	}

	public final void setVisited(final Boolean visited) {
		this.visited = visited;
	}

}
