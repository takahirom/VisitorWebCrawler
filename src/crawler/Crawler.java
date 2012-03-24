package crawler;

import java.util.Iterator;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import webfile.*;

/**
 * Visitor
 *
 * @author takahirom
 */
public class Crawler extends Visitor {

	protected Logger logger = Logger.getLogger(this.getClass());
	static {
		BasicConfigurator.configure();// log4jの設定
	}

	/* (非 Javadoc)
	 * @see crawler.Visitor#visit(webfile.WebPage)
	 */
	@Override
	public void visit(WebPage file) {
		if (!file.getVisited()) {
			// urlからソースを取得しそのソースからbaseurlなどの設定をする
			if (file.getLayer() == 0) {
				return;
			}
			file.setVisited(true);
			// 初期設定
			file.setting();
			// imgとjsのSetの設定と書き換え
			file.replaceImg();
			file.replaceLink();
			file.write();
			WebFile wf = null;
			file.setOutputDocument(null);
			file.setSource(null);

			for (Iterator<WebFile> it = file.iterator(); it.hasNext();) {
				wf = it.next();

				wf.accept(this);
			}
			file.setWebFiles(null);
		}
	}

	/* (非 Javadoc)
	 * @see crawler.Visitor#visit(webfile.WebImg)
	 */
	@Override
	public void visit(WebImg file) {

		logger.info("Visit WebImg:" + file.getUrl());
		file.write();
	}

}
