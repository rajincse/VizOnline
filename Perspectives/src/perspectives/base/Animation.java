package perspectives.base;

import java.util.Date;


public abstract class Animation 
{
	public abstract void step();
	public boolean done = false;
	
	public static abstract class IntegerAnimation extends Animation
	{
		int v1,v2;
		long st,et;
		public IntegerAnimation(int v1, int v2, long t)
		{
			this.v1 = v1;
			this.v2 = v2; 
			st = (new Date()).getTime();
			et = st + t;
		}

		@Override
		public void step() {
			
			double perc = ((new Date()).getTime() - st) / (double)(et-st);
			if (perc >= 1)
			{
				perc = 1;
				done = true;
			}
			int v = v1 + (int)((v2-v1)*perc);
			step(v);
			
		}
		
		public abstract void step(int v);
		
	}

}



