package crawler;

import webfile.WebImg;
import webfile.WebPage;

/**
 * @author takahirom
 *
 */
public abstract class Visitor {
	/**
	 * ウェブページをVisitした時の処理を書く
	 * @param file visitしたウェブページ
	 */
	public abstract void visit(WebPage file);
	/**
	 * 画像をVisitした時の処理を書く
	 * @param file visitした画像
	 */
	public abstract void visit(WebImg file);

}
