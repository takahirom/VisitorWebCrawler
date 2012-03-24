package webfile;
import crawler.Visitor;

/**
 * VisitorがVisitできるようにするためのインターフェース
 * @author takahirom
 *
 */
public interface Element {
	/**
	 * @param v
	 * visitorを受け入れることを示す部分
	 */
	public abstract void accept(Visitor v);
}
