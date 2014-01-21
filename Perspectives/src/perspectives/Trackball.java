package perspectives;

import java.util.Arrays;

import javax.media.opengl.glu.GLU;

import com.jogamp.opengl.math.Quaternion;



public class Trackball
{
    private float[] v1_, v2_;    
    Sphere s;
    int viewport[] = new int[4];
    double mvmatrix[] = new double[16];
    double projmatrix[] = new double[16];
    
    Quaternion rot;

    public Trackball (float x, float y, float z, float radius)
    {
        v1_ = new float[4];
        v2_ = new float[4];
        rot = new Quaternion(new float[]{1,0,0},0);
     
        s = new Sphere(x,y,z,radius);
    }
    public Quaternion getRot()
    {
    	return this.rot;
    }
    
    public void setCenter(float x, float y, float z)
    {
    	s = new Sphere(x,y,z, s.radius);
    }
    
    public void setRadius(float r)
    {
    	s.radius = r;
    }

    public void click (int x, int y)
    {
       v1_ = project(x, y);         
    }

    public void drag(int x, int y)
    {
       v2_ = project(x, y);
       
        if (v1_[3] < 0 || v2_[3] < 0) return;
        if (Arrays.equals(v1_, v2_)) return;
        
        Quaternion invq = new Quaternion(rot);
        invq.inverse();
        float[] v1 = invq.mult(v1_);
        float[] v2 = invq.mult(v2_);


        float[] rotAxis1 = Vectors.cross(v1, v2, null);       
        float[] rotAxis = new float[4];
        
        Vectors.norm(rotAxis1, rotAxis);
        
        //rotAxis = rot2_.mult(rotAxis2);
        
        
        if (Float.isInfinite(rotAxis[0]) || Float.isInfinite(rotAxis[1]) || Float.isInfinite(rotAxis[2]) || 
        		Float.isNaN(rotAxis[0]) || Float.isNaN(rotAxis[1]) || Float.isNaN(rotAxis[2]) ||
        		rotAxis[0] == 0 || rotAxis[1] == 0 || rotAxis[2] == 0)
        	return;
        
        
        double dot = Vectors.dot(v1,v2);
        double angle = Math.acos(dot);
        
        v1_ = v2_;
        System.out.println("Angle :"+angle);
        if (Double.isInfinite(angle) || Double.isNaN(angle))
        	return;
        
        System.out.println("rot: " + angle + "   " + rotAxis[0] + "," + rotAxis[1] + "," + rotAxis[2]);
        
        Quaternion q = new Quaternion(new float[]{rotAxis[0], rotAxis[1], rotAxis[2]}, (float)angle);
        
        rot.mult(q);
      
        
    }
  
    public float[] project(float x, float y)
    {
    	
    	int realy = viewport[3] - (int) y - 1;
    	
    	 double wc1[] = new double[4];
    	 double wc2[] = new double[4];
	      
	        (new GLU()).gluUnProject((double) x, (double) realy, 0,         mvmatrix, 0,  projmatrix, 0, viewport, 0, wc1, 0);
	        (new GLU()).gluUnProject((double) x, (double) realy, 1,         mvmatrix, 0,  projmatrix, 0, viewport, 0, wc2, 0);
	        
	       F3 dir = new F3((float)(wc2[0]-wc1[0]), (float)(wc2[1]-wc1[1]), (float)(wc2[2]-wc1[2]));
	        dir.normalize();        
    	
	    Ray r = new Ray(dir, new F3((float)wc1[0],(float)wc1[1],(float)wc1[2]));
	    
	    r = new Ray(new F3(0,0,-1), new F3((float)wc1[0],(float)wc1[1],10));
	  
	    F3 intersection = new F3();
	    F3 normal = new F3();
    	float i = s.intersect(r, 100, intersection, normal);
    	
    	//System.out.println(i + " " + intersection.toString() + " " + normal.toString());
    	
        float[] v = new float[] {intersection.x-s.center.x, intersection.y-s.center.y, intersection.z-s.center.z,i};
        float[] dest = new float[4];
        Vectors.norm(v, dest);
        return dest;
    }
    
    class Ray {
        public F3 m, b;

        /**
         * Represents a ray in 3D space. Formula: v = m*t + b, so b is the origin and m is the dir. t >= 0.
         */
        public Ray(F3 m, F3 b) {
                this.m = m;
                this.b = b;
        }

}
    
    public class F3 {
        public float x, y, z;

        public F3() {
                this(0, 0, 0);
        }

        public F3(float x, float y, float z) {
                this.x = x;
                this.y = y;
                this.z = z;
        }

        public float norm() {
                return (float) Math.sqrt(x * x + y * y + z * z);
        }

        public float dot(F3 other) {
                return x * other.x + y * other.y + z * other.z;
        }
        
        public void normalize()
        {
        	x /= norm();
        	y /= norm();
        	z /= norm();
        }
        
        public String toString()
        {
        	return "(" + x + "," + y + "," + z + ")"; 
        }
}
    
    class Sphere {
        public F3 center, velocity;
        public float radius;

        public Sphere(float x, float y, float z, float radius) {
                this.center = new F3(x,y,z);                
                this.radius = radius;
           
        }

        public float intersect(Ray ray, float max_t, F3 intersection, F3 normal) {
                /* compute ray-sphere intersection */
                F3 eye = ray.b;
                F3 dst = new F3(center.x - eye.x, center.y - eye.y, center.z - eye.z);
                float t = dst.dot(ray.m);
                /* is it in front of the eye? */
                if (t <= 0) {
                        return -1;
                }
                /* depth test */
                float d = t * t - dst.dot(dst) + radius * radius;
                /* does it intersect the sphere? */
                if (d <= 0) {
                        return -1;
                }
                /* is it closer than the closest thing we've hit so far */
                t -= (float) Math.sqrt(d);
                if (t >= max_t) {
                        return -1;
                }

                /* if so, then we have an intersection */
                intersection.x = eye.x + t * ray.m.x;
                intersection.y = eye.y + t * ray.m.y;
                intersection.z = eye.z + t * ray.m.z;

                normal.x = (intersection.x - center.x) / radius;
                normal.y = (intersection.y - center.y) / radius;
                normal.z = (intersection.z - center.z) / radius;
                return t;
        }
}
}
