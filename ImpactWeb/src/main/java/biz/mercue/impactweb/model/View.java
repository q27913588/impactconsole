package biz.mercue.impactweb.model;

public class View {
	
	public interface Public {}
	
	public interface Token extends Public {}
	
	public interface Admin extends Public {}

	public interface Partner extends Public {}
	
	public interface News extends Public {}
	
	public interface NewsFrontEnd extends News {}
	
	public interface NewsBackEnd extends News {}
	
	public interface Account extends Public {}
	
	public interface CSVTask extends Admin {}
	
	public interface SuccessCase extends Public {}
	
	public interface Downloads extends Public {}
	
	public interface Activity extends Public {}
	
	public interface Banner extends Public {}
	
	public interface Rss extends Public {}
	
	public interface Image extends Public {}
	
	public interface CkEditorUploadImage extends Public {}
	
	public interface Tag extends Public {}

	public interface Category extends Public {}
}
