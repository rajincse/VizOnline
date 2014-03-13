package perspectives.base;

public interface TaskObserver {

	public void addTask(Task t);
	
	public void progressChanged(Task t, double d);
}
