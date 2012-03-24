package webfile.factory;

import java.net.URL;

import webfile.WebFile;

public abstract class AbstractWebFileFactory {

	/**
	 * WebFileのインスタンスを得るための関数
	 * @param url このWebFileのurl
	 * @return WebFile ウェブファイルのインスタンスを取得
	 */
	abstract public  WebFile getWebFile(URL url) ;
}
