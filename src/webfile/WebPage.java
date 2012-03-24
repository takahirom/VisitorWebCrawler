package webfile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import webfile.factory.WebImgFactory;
import webfile.factory.WebPageFactory;

import md5.MD5;
import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;
import crawler.Visitor;

/**
 * ウェブページを扱うクラス visit可能である
 *
 * @author takahirom
 *
 */
public class WebPage extends WebFile {

	protected Logger logger = Logger.getLogger(this.getClass());
	static {
		BasicConfigurator.configure();// log4jの設定
	}

	private URL url;// WebPageのurl
	private Integer layer = -1;// 現在のレイアー
	private Source source;
	private OutputDocument outputDocument;
	private Boolean visited = false;
	private String urlmd5;
	private URL baseUrl;
	private Set<WebFile> webFiles = new HashSet<WebFile>();
	private Visitor nowVisitor;

	@Override
	public final void accept(final Visitor v) {
		v.visit(this);
	}

	@Override
	public final void setLayer(final Integer layer) {
		if (this.layer < layer) {
			this.layer = layer;
		}
	}

	@Override
	public final Integer getLayer() {
		return this.layer;
	}

	public final URL getUrl() {
		return url;
	}

	public final void setUrl(final URL url) {
		this.url = url;
	}

	/**
	 * ベースタグを取得し、現在のパスを設定する ベースタグがない場合はベースのurlはthis.urlである
	 */
	private void settingBaseUrl() {
		List<StartTag> linkStartTags = source
				.getAllStartTags(HTMLElementName.BASE);
		String src = null;
		for (Iterator<?> i = linkStartTags.iterator(); i.hasNext();) {
			StartTag startTag = (StartTag) i.next();
			Attributes imgattributes = startTag.getAttributes();
			src = imgattributes.getValue("href");// ファイル名取得用
		}
		linkStartTags = null;
		if (src == null) {
			this.baseUrl = this.url;
		} else {
			try {
				this.baseUrl = new URL(src);
			} catch (MalformedURLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
	}

	/**
	 * セッティングする　これによりhtmlのソースの取得などを行う
	 */
	public final void setting() {
		URL url = null;
		url = this.url;
		URLConnection uc = null;
		try {
			uc = url.openConnection();
			uc.setRequestProperty(
					"User-Agent",
					"Mozilla/5.0 (compatible; VisitorCrawler/1.0; +https://nanasiblog.wordpress.com/)");// ヘッダを設定
			uc.setRequestProperty("Accept-Language", "ja");// ヘッダを設定
			uc.setReadTimeout(1000);
			uc.setConnectTimeout(1000);
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		InputStream is = null;

		try {
			is = new BufferedInputStream(uc.getInputStream());

		} catch (SocketTimeoutException e) {
			// TODO 自動生成された catch ブロック
			logger.warn("Connection Time Out To:" + url);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			InputStreamReader isr = new InputStreamReader(is);
			StringBuffer sb = new StringBuffer();
			int countbyte = 0;
			for (;;) {
				final int readChar = isr.read();
				// Streamの終わりに達して読み込むデータがない場合
				if (readChar < 0 || countbyte > 90000) {
					break;
				}
				// charとして追加
				sb.append((char) readChar);
				countbyte++;
			}
			this.source = new Source(sb.toString());// ソースとしてjerichoに渡してsorceオブジェクトを取得
			this.outputDocument = new OutputDocument(source);// 出力として利用するoutputDocument
			settingBaseUrl();
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}// html取得

		try {
			this.urlmd5 = MD5.crypt(this.url.toString());

		} catch (NoSuchAlgorithmException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	/**
	 * 画像タグを書き換えして WebImgのインタンスを作成後webFilesに追加する
	 */
	public final void replaceImg() {
		if (source == null) {
			return;
		}
		List<StartTag> linkStartTags = null;
		try {
			linkStartTags = source.getAllStartTags(HTMLElementName.IMG);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return;
		}
		WebImgFactory wf = WebImgFactory.getInstance();
		for (Iterator<?> i = linkStartTags.iterator(); i.hasNext();) {
			StartTag startTag = (StartTag) i.next();
			Attributes imgattributes = startTag.getAttributes();
			Attribute srcat = imgattributes.get("src");// 交換用のattribute
			URL src = null;
			try {
				src = new URL(this.baseUrl, imgattributes.getValue("src"));
			} catch (MalformedURLException e) {
				// TODO 自動生成された catch ブロック
				logger.warn("MalformedURL:href="
						+ imgattributes.getValue("src") + " baseurl:"
						+ this.baseUrl);
				continue;
			}// ファイル名取得用

			try {
				String fname = URLEncoder.encode(
						new File(src.toString()).getName(), "UTF-8");

				if (fname.length() > 100) {
					fname.substring(0, 100);
				}
				outputDocument.replace(srcat, " src='file://"
						+ new File(".").getAbsoluteFile().getParent() + "/"
						+ urlmd5 + "/img/" + fname + "'");

				WebImg wi = (WebImg) wf.getWebFile(src);
				wi.setUrl(src);
				wi.pushSavePathStack(urlmd5);
				webFiles.add((WebFile) wi);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}

	}

	/**
	 * ソースにあるリンクを書き換えて WebPageのインタンスを作成後をwebFilesに追加する
	 */
	public final void replaceLink() {
		if (source == null) {
			return;
		}
		List<StartTag> linkStartTags = null;
		// TODO 自動生成されたメソッド・スタブ
		try {
			linkStartTags = source.getAllStartTags(HTMLElementName.A);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return;
		}
		WebPageFactory wf = WebPageFactory.getInstance();
		for (Iterator<StartTag> i = linkStartTags.iterator(); i.hasNext();) {
			StartTag startTag = (StartTag) i.next();
			Attributes imgattributes = startTag.getAttributes();
			Attribute srcat = imgattributes.get("href");// 交換用のattribute
			URL src = null;
			try {
				src = new URL(this.baseUrl, imgattributes.getValue("href"));
			} catch (MalformedURLException e) {
				// TODO 自動生成された catch ブロック
				logger.warn("MalformedURL:href="
						+ imgattributes.getValue("href") + " baseurl:"
						+ this.baseUrl);
				// e.printStackTrace();
				continue;
			}// ファイル名取得用
			try {

				WebPage wi = (WebPage) wf.getWebFile(src);
				wi.setUrl(src);
				wi.setLayer(this.layer - 1);
				webFiles.add((WebFile) wi);

				String directoryName = MD5.crypt(src.toString());

				outputDocument.replace(srcat, " href='file://"
						+ new File(".").getAbsoluteFile().getParent() + "/"
						+ directoryName + "/index.html" + "'");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		linkStartTags = null;
	}

	public final Iterator<WebFile> iterator() {
		Iterator<WebFile> i = null;
		try {
			i= webFiles.iterator();
		} catch (Exception e) {
			// TODO: handle exception
			logger.warn("No Link and Pages");
		}
		return i;
	}

	public final void write() {
		if (outputDocument == null) {
			return;
		}
		System.out.println(this.url + " : " + this.urlmd5);

		/* そのサイトのフォルダを作成 */
		File newsitedir = new File("./" + urlmd5);
		if (newsitedir.exists()) {
			return;
		}
		newsitedir.mkdir();
		/* 変更済みhtml保存処理 */
		FileWriter fw;
		try {
			fw = new FileWriter("./" + urlmd5 + "/index.html");
			outputDocument.writeTo(fw);
			fw.close();
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	public final Visitor getNowVisitor() {
		return nowVisitor;
	}

	public final void setNowVisitor(final Visitor nowVisitor) {
		this.nowVisitor = nowVisitor;
	}

	public final Boolean getVisited() {
		return visited;
	}

	public final void setVisited(final Boolean visited) {
		this.visited = visited;
	}

	public final void setOutputDocument(OutputDocument od) {
		this.outputDocument = od;
	}

	public final void setSource(Source s) {
		this.source = s;
	}

	public final void setWebFiles(Set<WebFile> s) {
		this.webFiles = s;
	}
}
