package stimulusgen;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class RadialDistancePlotter extends StimulusGenPlotter{
	protected static final int ANGLE_DIVISION =30;
	protected static final int INFINITY =1000;

	protected ArrayList<Point> positions;
	
	public RadialDistancePlotter(int objectCount, int minDistance,
			int maxDistance) {
		super(objectCount, minDistance, maxDistance);

		this.positions = new ArrayList<Point>();
		positions.add(new Point(0,0));
		
		this.simulate();
	}
	
	@Override
	protected void simulate() 
	{
		Random random = new Random();
		int d=0;
		double minConfigScore = Double.MAX_VALUE;
		double unitAngle = Math.PI *2 / ANGLE_DIVISION;
		
		for(int i=1;i<this.objectCount;i++)
		{		
			d =Math.abs( random.nextInt()% (maxDistance-minDistance))+minDistance;
			
			Point bestPos = null;
			
			minConfigScore = Double.MAX_VALUE;
			
			for(int j=0;j<i;j++)
			{
				for(int k=0;k<ANGLE_DIVISION;k++)
				{
					Point position = this.getOnCirclePoint(this.positions.get(j), d, unitAngle*(k+1));					
					
					double score = this.getConfigurationScore(position, d);
					if(score < minConfigScore)
					{
						minConfigScore = score;
						bestPos = position;
					}
				}
			}
			
			positions.add(bestPos);
		}
		
		//center
		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		
		for(Point p: this.positions)
		{
			if(p.x < minX)
			{
				minX = p.x;
			}
			if(p.x > maxX)
			{
				maxX = p.x;
			}
			if(p.y < minY)
			{
				minY = p.y;
			}
			if(p.y > maxY)
			{
				maxY = p.y;
			}
		}
		
		int cx = (maxX +minX)/2;

		int cy = (maxY + minY)/2;
		
		for (int i=0; i<positions.size(); i++)
		{
			Point p = positions.get(i);
			p.x = p.x -cx;
			p.y = p.y - cy;
		}
		
		
	}
	private Point getOnCirclePoint(Point center, int radius, double angle)
	{
		double delX = Math.cos(angle) * radius;
		double delY = Math.sin(angle) * radius;
		
		Point position = new Point(center.x+(int)delX, center.y+(int)delY);
		
		return position;
	}
	private double getConfigurationScore(Point p, int idealDist)
	{
		double aspectRatioScore = this.getAspectRatioScore(p);
		double distanceScore = this.getDistanceScore(p, idealDist);
				
		return Math.pow((1./aspectRatioScore),2) * distanceScore;
	}
	private double getDistanceScore(Point comparingPoint, int idealDistance)
	{
		double score =0;		
		for(int i=0;i<positions.size();i++)
		{			
			Point p = this.positions.get(i);
			double distance = comparingPoint.distance(p);
			if(distance > maxDistance)
			{
					score++;
					//score = INFINITY;
			}
			else if(distance < minDistance)
			{
					score = INFINITY;
			}
			else
			{
					score += (distance-idealDistance)*(distance-idealDistance);
			}
						
		
		}
		return score;
	}
	private double getAspectRatioScore(Point cp)
	{
		positions.add(cp);
		
		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		
		for(Point p: this.positions)
		{
			if(p.x < minX)
			{
				minX = p.x;
			}
			if(p.x > maxX)
			{
				maxX = p.x;
			}
			if(p.y < minY)
			{
				minY = p.y;
			}
			if(p.y > maxY)
			{
				maxY = p.y;
			}
		}
		
		int width = maxX -minX;
		int height = maxY - minY;
		double score =0;
		if(width != 0 && height !=0)
		{
			double min=Math.min(width, height);
			double max =  Math.max(width, height);
			score = min/max;
		}		
		
		positions.remove(positions.size()-1);
		
		return score;
	}
	public void printPosition()
	{
		for(int i=0;i<this.positions.size();i++)
		{
			Point p = this.positions.get(i);
			System.out.println(""+i+"=>("+p.x+", "+p.y+")");
		}
	}
	@Override
	public Point getPosition(int index) {
		return this.positions.get(index);
	}

}
