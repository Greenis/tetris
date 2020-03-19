package tetris.components;

import tetris.enumeration.ManagerMessage;

public interface IObserver {
	
	public void notify(IObservable obj, ManagerMessage s);
	
}
