import java.net.URL;

import crawler.Crawler;
import webfile.WebPage;

/**
 * @author takahirom
 */
public class Starter {

	/**
	 * 最初に実行してVisitorを起動する部分 クローラにVisitorであるCrawlerを読み込ませることで始まる
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		WebPage wp = new WebPage();
		String rootUrlString = "";
		String layerString;
		// URLの設定
		if (args.length == 0) {
			System.out
			.println("Usage: java Stater crawlurl depth");
			System.out
			.println("Example: java Stater http://hostname.com/example/ 3");
			System.exit(0);
		} else {
			rootUrlString = args[0];
		}

		// recursiveに取ってくる階層の設定
		if (args.length <= 1) {
			layerString = "2";
		} else {
			layerString = args[1];
		}

		try {
			wp.setUrl(new URL(rootUrlString));
		} catch (Exception e) {
			// TODO: handle exception
		}
		wp.setLayer(Integer.valueOf(layerString));
		wp.accept(new Crawler());
	}

}