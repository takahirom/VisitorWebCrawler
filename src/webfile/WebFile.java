package webfile;

import java.net.URL;
import java.util.Iterator;

/**
 * 　Webのファイルを示すスーパークラス 　片方にしかない関数を呼んだ時にエラーを起こすことが可能である
 *
 * @author takahirom
 */
public abstract class WebFile implements Element {

	/**
	 * レイアーを設定する
	 *
	 * @param integer
	 */
	public abstract void setLayer(Integer integer);

	/**
	 * レイアーを取得する
	 *
	 * @return layer このファイルのレイアー
	 */
	public abstract Integer getLayer();

	/**
	 * このWebFileのurlを設定する
	 *
	 * @param url
	 */
	public abstract void setUrl(URL url);

	/**
	 * ファイルに書き込む
	 */
	public abstract void write();

	public Iterator<WebFile> iterator() throws FileTreatmentException {
		throw new FileTreatmentException();
	}

	public void run() throws FileTreatmentException {
		throw new FileTreatmentException();
	}

}
