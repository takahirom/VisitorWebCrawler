package webfile.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import org.apache.log4j.Logger;

/**
 * @author takahirom
 * 画像をダウンロードするユーティリティ
 */
public class DownloadImage{

	protected Logger logger = Logger.getLogger(this.getClass());
	private URL url;
	private String downloadPath;



	/**
	 * ダウンロードする
	 */
	public void download() {
		File f = new File(downloadPath);

		try {
			f.createNewFile();
			URLConnection conn = url.openConnection();
			conn.setReadTimeout(1000);
			conn.setConnectTimeout(1000);
			conn.connect();
			ImageInputStream iis = ImageIO
					.createImageInputStream(new BufferedInputStream(conn
							.getInputStream(), 1024));
			ImageOutputStream ios = ImageIO.createImageOutputStream(f);
			byte[] buf = new byte[1024];
			int len;
			while ((len = iis.read(buf)) > 0) {
				ios.write(buf, 0, len);
			}
			iis.flush();
			ios.flush();
			iis.close();
			ios.close();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public String getDownloadPath() {
		return downloadPath;
	}

	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}
}
