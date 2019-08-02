package cs120.bdevaughn.texascounties;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * This class acts as a delegater and contains other important helper functions. The class will hold onto
 * a list of County objects and a list of Region objects that act represent exclusion zones(areas of water
 * and other things). The class will also convert the world coordinates(latitude and longitude) to pixel 
 * coordinates. It will also read the counties' attributes and coordinates from two files. 
 * @author Bryce DeVaughn
 *
 */
public class QuizManager {
	private float minx,miny,maxx,maxy;//The values for the the ends of the state 
	private int pnlWidth,pnlHeight;//The height and width of the displaying panel
	private List<Region> exclusionZones;//A list of the excluded regions
	private List<County> counties;//A list of all the counties
	private Random rand;
	private County selectedCounty;

	public QuizManager(){
		this.pnlHeight = 0;
		this.pnlWidth = 0;
		counties = new LinkedList<County>();
		exclusionZones = new LinkedList<Region>();
		rand = new Random();
		minx = 0;
		miny = 0;
		maxx = 0;
		maxy = 0;
		selectedCounty = null;
	}

	/**
	 * This method will read in counties from the counties file. Then it will fill the list of 
	 * counties with the new county information.
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public void readCounties(String filename) throws FileNotFoundException, IOException{
		File file = new File(filename);

		try(BufferedReader br = new BufferedReader(new FileReader(file))){
			String str = br.readLine();
			if(Integer.parseInt(str.trim())==0){
				br.readLine();
				br.readLine();
				br.readLine();
				br.readLine();
				br.readLine();
				br.readLine();
				str = br.readLine();
			}
			/*
			 * This while will read through the file. The sequence will go as follows:
			 * 1.read in polygon id
			 * 2.read in fips state code.
			 * 3.read in fips county code.
			 * 4.read in name.
			 * 5.skip
			 * 6.skip
			 * 7.skip
			 * -restart the process
			 */
			while(str!=null){
				str = str.trim();
				int pid = Integer.parseInt(str);
				str = br.readLine();
				str = str.replaceAll("\"","").trim();
				int fipssc = Integer.parseInt(str);
				str = br.readLine();
				str = str.replaceAll("\"","").trim();
				int fipscc = Integer.parseInt(str);
				str = br.readLine();
				String name = str.replaceAll("\"","").trim();
				str = br.readLine();//Skip lsad line
				str = br.readLine();//Skip lsad translation line
				str = br.readLine();//Skip the extra space

				/*
				 * Create and add a county with the previous info to the counties list.
				 */
				County county = new County(null,pid,fipssc,fipscc,name);
				counties.add(county);
				str = br.readLine();//Sets the line to the next polygon id
			}
		}
	}

	/**
	 * This method will read the coordinates file and create list of coordinates for the counties. In
	 * the case that an id for an exclusion zone arises, a new region will be made with those coordi-
	 * nates.
	 * @return
	 * @throws IOException 
	 */
	public void readCoordinates(String filename) throws IOException{
		File file = new File(filename);

		try(BufferedReader br = new BufferedReader(new FileReader(file))){
			String str = br.readLine();
			ArrayList<County> tempList = new ArrayList<County>(counties);
			boolean isCounty = false;
			boolean isZone = false;
			LinkedList<Coords2D> coords = new LinkedList<Coords2D>();
			int pid = 0;
			while(str!=null){
				str = str.trim().replaceAll("\\s+", " ");
				String[] strParts = str.split("\\s");

				if(strParts.length == 3){
					isCounty = true;
					pid = Integer.parseInt(strParts[0]);
				}else if(strParts.length == 2){
					Coords2D newCoord = new Coords2D(Float.parseFloat(strParts[0]),Float.parseFloat(strParts[1]));
					coords.add(newCoord);
				}else if(strParts.length==1){
					//					if(strParts[0].contains("-9")){
					//						System.out.println("Problem");
					//					}
					if("END".equals(strParts[0])){
						if(isCounty){
							tempList.get(pid-1).setPoints(coords);
							coords = new LinkedList<Coords2D>();
							isCounty = false;
						}
						if(isZone){
							Region newZone = new Region(coords);
							exclusionZones.add(newZone);
							coords = new LinkedList<Coords2D>();
							isZone = false;
						}
					}else if(strParts[0].contains("-9")){
						isZone = true;
					}
				}
				str = br.readLine();
			}
		}
	}

	/**
	 * This method will set the height and width of the panel to this instance's height and width 
	 */
	public void setDimension(int width, int height){
		pnlWidth = width;
		pnlHeight = height;
	}

	/**
	 * This method will set the boundaries to all of the counties and exclusion zones.
	 */
	public void setBoundaries(){
		ArrayList<County> tempCounties = new ArrayList<County>(counties);
		ArrayList<Region> tempZones = new ArrayList<Region>(exclusionZones);

		for(County c: tempCounties){
			c.setBoundaries();
		}

		for(Region r: tempZones){
			r.setBoundaries();
		}

		/*
		 * Finding the min x-value
		 */
		minx = counties.get(0).getMinX();
		for(County c: counties){
			if(c.getMinX() < minx){
				minx = c.getMinX();
			}
		}

		/*
		 * Finding the min y-value
		 */
		miny = counties.get(0).getMinY();
		for(County c: counties){
			if(miny > c.getMinY()){
				miny = c.getMinY();
			}
		}

		/*
		 * Finding the max x-value
		 */
		maxx = counties.get(0).getMaxX();
		for(County c: counties){
			if(c.getMaxX() > maxx){
				maxx = c.getMaxX();
			}
		}

		/*
		 * Finding the max y-value
		 */
		maxy = counties.get(0).getMaxY();
		for(County c: counties){
			if(c.getMaxY() > maxy){
				maxy = c.getMaxY();
			}
		}
	}

	/**
	 * This method will create and set polygons for all the counties based off their individual coordina-
	 * tes.
	 */
	public void makePolygons(){
		for(County c: counties){
			c.setPoly(createPolygon(c.getPoints()));
		}

		for(Region r: exclusionZones){
			r.setPoly(createPolygon(r.getPoints()));
		}
	}
	/**
	 * This method will return a polygon with the coordinates of the list of world coordinates converted 
	 * to pixel coordinates.
	 */
	public Polygon createPolygon(LinkedList<Coords2D> points){
		Polygon rtnPoly = new Polygon();
		for(Coords2D coords: points){
			Point p = toPixels(coords.getX(),coords.getY());
			rtnPoly.addPoint((int)p.getX(),(int)p.getY());
		}

		return rtnPoly;
	}

	/**
	 * This method will find the county contain the inputed position.
	 */
	public County findCounty(int x, int y){
		County rtnC = null;
		Point testP = new Point(x,y);
		for(County c: counties){
			if(c.getPoly().contains(testP)){
				rtnC = c;
			}
		}
		return rtnC;
	}

	/**
	 * This method will take floats for an x and y position and convert them into pixel coordinates.
	 * @return
	 */
	public Point toPixels(float x, float y){
		Point rtnPoint = new Point();
		int vx = (int)((x - getMinX())/(getMaxX()-getMinX())*pnlWidth);
		float yRange = getMaxY()-getMinY();
		float yDist = y - getMinY();
		float distPer = yDist / yRange;
		int vy = (int)(pnlHeight - (distPer*pnlHeight));
		rtnPoint.setLocation(vx, vy);
		return rtnPoint;
	}

	/**
	 * This method will return the county with the same inputed id.
	 * @return
	 */
	public County findCountyById(int id){
		County rtnC = null;
		for(County c: counties){
			if(c.getPID() == id){
				rtnC = c;
			}
		}

		return rtnC;
	}

	/**
	 * This method will return a specified number of counties that are randomly chosen and aren't the one that is inputed.
	 * @return
	 */
	public County[] randomCounty(int n, int id){
		County[] rtnList = new County[n];
		boolean isNull = false;
		do{
			int elements = 0;
			while(elements < n){
				int newId = id;
				do{
					newId = rand.nextInt(counties.size());
				}while(newId==id);

				boolean isInList = false;

				for(int k = 0; k < rtnList.length; k++){
					if(rtnList[k]==counties.get(newId)){
						isInList = true;
					}
				}
				if(!isInList){
					rtnList[elements] = counties.get(newId);
					elements++;
				}
			}
			
			for(int i = 0; i < rtnList.length; i++){
				if(rtnList[i]==null){
					isNull=true;
				}
			}
		}while(isNull==true);
		return rtnList;
	}

	/**
	 * This will draw all of the counties
	 * @return
	 */
	public void drawCounties(Graphics gfx){
		for(County c: counties){
			c.drawOn(gfx);
		}
	}

	public List<Region> getExclusionZones() {
		return exclusionZones;
	}

	public List<County> getCounties() {
		return counties;
	}

	public County getSelectedCounty() {
		return selectedCounty;
	}

	public void setSelectedCounty(County selectedCounty) {
		this.selectedCounty = selectedCounty;
	}

	public float getMinX() {
		return minx;
	}

	public float getMinY() {
		return miny;
	}

	public float getMaxX() {
		return maxx;
	}

	public float getMaxY() {
		return maxy;
	}



}
