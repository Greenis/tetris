package tetris.components;

public interface IObservable {
	
	public void addListener(IObserver listener);
	
	public void removeListener(IObserver listener);
	
}
