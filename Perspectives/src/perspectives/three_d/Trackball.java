package perspectives.three_d;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;



public class Trackball
{
    private Vector4f v1_, v2_;    
    Sphere s;
    int viewport[] = new int[4];
    double mvmatrix[] = new double[16];
    double projmatrix[] = new double[16];
    
    Quaternion rot;
    Quaternion allRot;

    public Trackball (float x, float y, float z, float radius)
    {
        v1_ = new Vector4f(0,0,0,0);
        v2_ = new Vector4f(0,0,0,0);
        rot = new Quaternion(1,0,0,0);
        allRot = new Quaternion(1,0,0,0);
     
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
       
        if (v1_.w < 0 || v2_.w < 0) return;
        if (v1_.x == v2_.x && v1_.y == v2_.y && v1_.z == v2_.z && v1_.w == v2_.w) return;
      
        Matrix4f rotmatrix = new Matrix4f();
        rotmatrix.rotate(rot.getW(), new Vector3f(rot.getX(), rot.getY(), rot.getZ()));
        
        if  (rotmatrix.invert() == null)
        	return;
        
        Vector4f v1 = Matrix4f.transform(rotmatrix, new Vector4f(v1_.x, v1_.y, v1_.z, 0f), null);
        Vector4f v2 = Matrix4f.transform(rotmatrix, new Vector4f(v2_.x, v2_.y, v2_.z, 0f), null);
        

        Vector3f rotAxis = Vector3f.cross(new Vector3f(v1.x, v1.y, v1.z), new Vector3f(v2.x, v2.y, v2.z), null);
        if (rotAxis.length() == 0)
        	return;
        
        rotAxis = rotAxis.normalise(null); 
        
       System.out.println("rotaxis: " + rotAxis.toString());
               
        
        if (Float.isInfinite(rotAxis.x) || Float.isInfinite(rotAxis.y) || Float.isInfinite(rotAxis.z) || 
        		Float.isNaN(rotAxis.x) || Float.isNaN(rotAxis.y) || Float.isNaN(rotAxis.z) ||
        		rotAxis.x == 0 || rotAxis.y == 0 || rotAxis.z == 0)
        	return;
        
        
        double dot = Vector3f.dot(new Vector3f(v1.x, v1.y, v1.z).normalise(null),new Vector3f(v2.x, v2.y, v2.z).normalise(null));
        
        System.out.println("dot: " + dot  + " --- " + v1.toString() + " " + v2.toString());
        
        double angle = Math.acos(dot);
        
        v1_ = v2_;
        
        if (Double.isInfinite(angle) || Double.isNaN(angle))
        	return;
        
        Quaternion q = new Quaternion(rotAxis.x, rotAxis.y, rotAxis.z, (float)angle*3);
        
        rot = q;       
  
    }
  
    public Vector4f project(float x, float y)
    {
    	
    	float[] mvmatrix = new float[]{1,0,0,0, 0,1,0,0, 0,0,1,0, 0,0,-10,1};
    	int[] viewport = new int[]{0,0,700,700};
    	float[] projmatrix = new float[]{1.2124354839324951f, 0.0f, 0.0f, 0.0f, 0.0f, 1.7320507764816284f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0202020406723022f, -1.0f, 0.0f, 0.0f, -2.0202019214630127f, 0.0f};
    	
    	int realy = viewport[3] - (int) y - 1;

    	 FloatBuffer wc1 = BufferUtils.createFloatBuffer(4);
    	 FloatBuffer wc2 = BufferUtils.createFloatBuffer(4);
    	 FloatBuffer mvmatrixbuf = BufferUtils.createFloatBuffer(16);
    	 mvmatrixbuf.put(mvmatrix); mvmatrixbuf.rewind();
    	 FloatBuffer projmatrixbuf = BufferUtils.createFloatBuffer(16);
    	 projmatrixbuf.put(projmatrix); projmatrixbuf.rewind();
    	 IntBuffer viewportmatrixbuf = BufferUtils.createIntBuffer(viewport.length);
    	 viewportmatrixbuf.put(viewport); viewportmatrixbuf.rewind();
	      
	        GLU.gluUnProject((float) x, (float) realy, 0f, mvmatrixbuf, projmatrixbuf, viewportmatrixbuf, wc1);
	        GLU.gluUnProject((float) x, (float) realy, 1f, mvmatrixbuf, projmatrixbuf, viewportmatrixbuf, wc2);
	        
	       F3 dir = new F3((float)(wc2.get(0)-wc1.get(0)), (float)(wc2.get(1)-wc1.get(1)), (float)(wc2.get(2)-wc1.get(2)));
	        dir.normalize();        
    	
	    Ray r = new Ray(dir, new F3((float)wc1.get(0),(float)wc1.get(1),(float)wc1.get(2)));
	    
	    r = new Ray(new F3(0,0,-1), new F3((float)wc1.get(0),(float)wc1.get(1),10));
	  
	    F3 intersection = new F3();
	    F3 normal = new F3();
    	float i = s.intersect(r, 100, intersection, normal);
    	
    	Vector4f v = new Vector4f(intersection.x-s.center.x, intersection.y-s.center.y, intersection.z-s.center.z,i);
    	
        return v.normalise(null);
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
