import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;


public class tsp {

	/**
	 * @param args
	 */
	static String[][] vertices=new String[1000][3];
	static Map<String, Integer> nodeDictionary = new TreeMap<String,Integer>();
	static ArrayList<Integer> ancestors=new ArrayList<Integer>();
	static ArrayList<Integer> current=new ArrayList<Integer>();
	static double[][] disMatrix=new double[1000][1000];
	static int numOfVertices=0;
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		/* Process the input*/
		int task=Integer.parseInt(args[1]);
		String startNode=args[3];
		String inputPath=args[5];
		String outputPath=args[7];
		String outputPathLog=args[9];
		/* Process the input file*/
		processInputFile(inputPath);
		/*Make a distance matrix*/
		makeDistanceMatrix();
		switch (task)
		{
		case 1:
			greedy(startNode,outputPath,outputPathLog);
			break;
		case 2:
			AstarSLD(startNode,outputPath,outputPathLog);
			break;
		case 3:
			AstarMST(startNode,outputPath,outputPathLog);
			break;
		}
	}
	/*A star using minimum spanning tree*/
	public static void AstarMST(String startNode,String outputPath, String outputPathLog) throws FileNotFoundException, UnsupportedEncodingException
	{
		Comparator<Node> comparator = new CostComparator();
		PriorityQueue<Node> astarSLDq=new PriorityQueue<Node>(1000,comparator);
		ArrayList<String> aList=new ArrayList<String>();
		StringBuilder path=new StringBuilder();
		int origin=nodeDictionary.get(startNode);
		double hcost,gcost;
		gcost=0;
		int flag=0;
		hcost=getHeuristicMSTCost(origin,origin,aList);
		Node nodeTobePrinted=new Node(null,0,0,aList);
		Node destNode;
		Node srcNode=new Node(startNode,gcost,hcost,aList);
		astarSLDq.add(srcNode);
		PrintWriter writerOutput = new PrintWriter(outputPath, "UTF-8");
		PrintWriter writerLog = new PrintWriter(outputPathLog, "UTF-8");
		StringBuilder finalpath=new StringBuilder();
		while(!astarSLDq.isEmpty())
		{
			srcNode=astarSLDq.peek();
			nodeTobePrinted=astarSLDq.remove();
			int src=nodeDictionary.get(srcNode.currentCity);
			for(int j=0;j<numOfVertices;j++)
			{
				String dest=getKeyInDictionary(j);
				if((srcNode.ancestorList.contains(srcNode.currentCity))==false)
				{
					if(!srcNode.ancestorList.contains(dest))
					{   
						if(j!=src)
						{
					    aList.clear();
						aList.addAll(srcNode.ancestorList);
						aList.add(srcNode.currentCity);
						hcost=getHeuristicMSTCost(j,origin,aList);
						gcost=srcNode.g+disMatrix[src][j];
						destNode=new Node(dest,gcost,hcost,aList);
						astarSLDq.add(destNode);
					   }
					}
					}
			}
			if(nodeTobePrinted.currentCity.equals(startNode))
				writerLog.println(nodeTobePrinted.currentCity+","+nodeTobePrinted.g+","+nodeTobePrinted.h+","+(nodeTobePrinted.g+nodeTobePrinted.h));
				else
			{
				java.util.Iterator<String> itr1 = nodeTobePrinted.ancestorList.iterator();
				path = new StringBuilder();
				while(itr1.hasNext()){
					path.append(itr1.next());
				}
				path.append(nodeTobePrinted.currentCity);
				writerLog.println(path+","+nodeTobePrinted.g+","+nodeTobePrinted.h+","+(nodeTobePrinted.g+nodeTobePrinted.h));
				if(path.length()==numOfVertices)
				{ flag=1;
				  //write to output
				 for(int i=0;i<numOfVertices;i++)
					 writerOutput.println(path.charAt(i)); 
				 finalpath.append(path);
				}
			}
			if(flag==1)
				break;
		}
		finalpath.append(startNode);
		writerLog.println(finalpath+","+(nodeTobePrinted.g+nodeTobePrinted.h)+","+0.0+","+(nodeTobePrinted.g+nodeTobePrinted.h));
		writerOutput.println(startNode);
		writerOutput.println("Total Tour Cost: "+(nodeTobePrinted.g+nodeTobePrinted.h));
		writerOutput.close();
		writerLog.close();
	}
	/* minimum spanning tree Heuristic*/
	public static double getHeuristicMSTCost(int dest, int strtNode,ArrayList<String> aList)
	{
		int len;
		double minCost;
		int nodesNotInclude;
		for(String nodes: aList)
		{
			nodesNotInclude=nodeDictionary.get(nodes);
			if(nodesNotInclude!=strtNode)
				ancestors.add(nodesNotInclude);
		}
		if(dest==strtNode)
			len=numOfVertices;
		else
			len=numOfVertices-ancestors.size();
		/*Prims Algorithm*/
		Boolean[] visited=new Boolean[numOfVertices];
		Arrays.fill(visited, Boolean.FALSE);
	    int total,i=0;
	    double mincost=0;
	    current.add(0);
	    total=1; 
	    visited[0]=true;
	    int nodeTojump=0;
	    int j=0;
	    int a=0;
	    while(total!=len) 
        { 
	    	a=0;
	    	minCost=(double) Float.MAX_VALUE;
            while(a<current.size()){
            	 j=current.get(a);
             if(!ancestors.contains(j))
             	  { 
                    for (i=0;i<numOfVertices;i++) 
                     {  
                    	if(!ancestors.contains(i))
                          {
        	                  if(disMatrix[j][i]!=0) 
        	                  {
                                if(visited[i]==false) 
                                 {
                		           if(minCost>disMatrix[j][i])
                		             {   
                			               minCost=disMatrix[j][i];
                			               nodeTojump=i;
                		             }
                	             }
        	                  }
                           }
                      }
        	       }
             a++;
      	     }
           visited[nodeTojump]=true; 
           current.add(nodeTojump);
           mincost=mincost+minCost; 
           total++;
         } 
	    ancestors.clear();
	    current.clear();
	    /*End*/
	    return mincost;
	}
	/*A star using Straight Line Distance*/
	public static void AstarSLD(String startNode,String outputPath, String outputPathLog) throws FileNotFoundException, UnsupportedEncodingException
	{
		Comparator<Node> comparator = new CostComparator();
		PriorityQueue<Node> astarSLDq=new PriorityQueue<Node>(1000,comparator);
		ArrayList<String> aList=new ArrayList<String>();
		StringBuilder path=new StringBuilder();
		int origin=nodeDictionary.get(startNode);
		Node nodeTobePrinted=new Node(null,0,0,aList);
		Node srcNode=new Node(startNode,0,0,aList);
		astarSLDq.add(srcNode);
		double hcost,gcost;
		int flag=0;
		PrintWriter writerOutput = new PrintWriter(outputPath, "UTF-8");
		PrintWriter writerLog = new PrintWriter(outputPathLog, "UTF-8");
		StringBuilder finalpath=new StringBuilder();
		while(!astarSLDq.isEmpty())
		{
			srcNode=astarSLDq.peek();
			nodeTobePrinted=astarSLDq.remove();
			int src=nodeDictionary.get(srcNode.currentCity);
			for(int j=0;j<numOfVertices;j++)
			{
				String dest=getKeyInDictionary(j);
				if((srcNode.ancestorList.contains(srcNode.currentCity))==false)
				{
					if(!srcNode.ancestorList.contains(dest))
					{   
						if(j!=src)
						{
						aList.addAll(srcNode.ancestorList);
						aList.add(srcNode.currentCity);
						hcost=getHeuristicCost(j,origin);
						gcost=srcNode.g+disMatrix[src][j];
						Node destNode=new Node(dest,gcost,hcost,aList);
						    astarSLDq.add(destNode);
						aList.clear();
					}
				
					}}
			}
			if(nodeTobePrinted.currentCity.equals(startNode))
	        writerLog.println(nodeTobePrinted.currentCity+","+nodeTobePrinted.g+","+nodeTobePrinted.h+","+(nodeTobePrinted.g+nodeTobePrinted.h));
			else
			{
				java.util.Iterator<String> itr1 = nodeTobePrinted.ancestorList.iterator();
				path = new StringBuilder();
				while(itr1.hasNext()){
					path.append(itr1.next());
				}
				path.append(nodeTobePrinted.currentCity);
				writerLog.println(path+","+nodeTobePrinted.g+","+nodeTobePrinted.h+","+(nodeTobePrinted.g+nodeTobePrinted.h));
				if(path.length()==numOfVertices)
				{ flag=1;
				  //write to output
				 for(int i=0;i<numOfVertices;i++)
					 writerOutput.println(path.charAt(i)); 
				 finalpath.append(path);
				}
			}
			if(flag==1)
				break;
		}
		finalpath.append(startNode);
		writerLog.println(finalpath+","+(nodeTobePrinted.g+nodeTobePrinted.h)+","+0.0+","+(nodeTobePrinted.g+nodeTobePrinted.h));
		writerOutput.println(startNode);
		writerOutput.println("Total Tour Cost: "+(nodeTobePrinted.g+nodeTobePrinted.h));
		writerOutput.close();
		writerLog.close();
	}
	/* SLD Heuristic*/
	public static double getHeuristicCost(int dest, int strtNode)
	{
		return disMatrix[dest][strtNode];
	}
	/* Greedy Algorithm*/
	public static void greedy(String startNode,String outputPath, String outputPathLog) throws FileNotFoundException, UnsupportedEncodingException
	{
		Queue<Node> greedyq=new LinkedList<Node>();
		Queue<Node> greedytraversal=new LinkedList<Node>();
		ArrayList<String> aList=new ArrayList<String>();
		Node srcNode=new Node(startNode,0,0,aList);
		Node lastNode;
		greedyq.add(srcNode);
		greedytraversal.add(srcNode);
		double totalCost,returnHome;
		while(greedytraversal.size()!=numOfVertices)
		{
			double min=Float.MAX_VALUE;
			srcNode=greedyq.peek();
			int nextNode=0;
			int src=nodeDictionary.get(srcNode.currentCity);
		    for(int j=0;j<numOfVertices;j++)
		    {
		    	if(srcNode.ancestorList.contains(getKeyInDictionary(j))==false)
		    	{
		    	if(j!=src)
		    	{
		    		if(disMatrix[src][j]<min)
		    		{
		    			min=disMatrix[src][j];
		    			nextNode=j;
		    		}
		    	}	
		    	}
		    }
		    String dest=getKeyInDictionary(nextNode);
	    	if(!srcNode.ancestorList.contains(dest))
	    	{
	    		aList.addAll(srcNode.ancestorList);
	    		aList.add(srcNode.currentCity);
	    		totalCost=srcNode.g+min;
	    		Node destNode=new Node(dest,totalCost,0,aList);
	    		greedyq.add(destNode);
	    		greedytraversal.add(destNode);
		    	aList.clear();
	    	}
	    	greedyq.remove();
		}
		lastNode=greedyq.peek();
		int ln=nodeDictionary.get(lastNode.currentCity);
		returnHome=disMatrix[ln][nodeDictionary.get(startNode)];
		totalCost=lastNode.g+returnHome;
		aList.addAll(lastNode.ancestorList);
		aList.add(lastNode.currentCity);
		Node last=new Node(startNode,totalCost,0,aList);
		greedytraversal.add(last);
		PrintWriter writerOutput = new PrintWriter(outputPath, "UTF-8");
		PrintWriter writerLog = new PrintWriter(outputPathLog, "UTF-8");
		writerOutput.println(greedytraversal.peek().currentCity);
		greedytraversal.remove();
		while(!greedytraversal.isEmpty())
		{
			Node head=greedytraversal.peek();
			java.util.Iterator<String> itr = head.ancestorList.iterator();
			StringBuilder path = new StringBuilder();
			while(itr.hasNext()){
				path.append(itr.next());
			}
			path.append(head.currentCity);
			writerOutput.println(head.currentCity);
			writerLog.println(path+","+head.g+","+head.h+","+(head.g+head.h));
			greedytraversal.remove();
			totalCost=head.g+head.h;
		}
		writerOutput.println("Total Tour Cost: "+totalCost);
		writerOutput.close();
		writerLog.close();
		
	}
	public static String getKeyInDictionary(int value)
	{
		for (Entry<String, Integer> entry : nodeDictionary.entrySet()) {
		     if(entry.getValue()==value)
		    	 return entry.getKey();
		}
		return null;
	}
	/* Process input file*/
	public static void processInputFile(String path)
	{
		int i=0;
		try{
			BufferedReader br= new BufferedReader( new FileReader(path));
			String line;
	        while ((line = br.readLine()) != null&&i<1000) {
	        	 String[] tokens = line.split(",");
	        	 nodeDictionary.put(tokens[0],numOfVertices);
	        	 vertices[i][0]=tokens[0];
	        	 vertices[i][1]=tokens[1];
	        	 vertices[i][2]=tokens[2];
	        	 i++;
	        	 numOfVertices++;
	        }
	        br.close();
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}
	/* Create Distance Matrix*/
	public static void makeDistanceMatrix()
	{
		double diffX=0,diffY=0;
		for(int i=0;i<numOfVertices;i++)
			{   
			    if(i==numOfVertices-1)
			    	break;
				double firstpointX=Double.parseDouble(vertices[i][1]);
				double firstpointY=Double.parseDouble(vertices[i][2]);
				for(int j=i+1;j<numOfVertices;j++)
				{
					double secondpointX=Double.parseDouble(vertices[j][1]);
					double secondpointY=Double.parseDouble(vertices[j][2]);
				diffX=(firstpointX-secondpointX)<0?(firstpointX-secondpointX)*-1:firstpointX-secondpointX;
				diffY=(firstpointY-secondpointY)<0?(firstpointY-secondpointY)*-1:firstpointY-secondpointY;
				double dis=(double) (Math.pow(diffX, 2)+Math.pow(diffY, 2));
				disMatrix[i][j]=(double)Math.sqrt(dis);
				disMatrix[j][i]=(double)Math.sqrt(dis);
				}
				}
		//Fill the rest of the matrix
		for(int i=0;i<numOfVertices;i++)
				{
					for(int j=0;j<numOfVertices;j++)
					{
						if(i==j)
							disMatrix[i][j]=0;
					}
			    }
			}
	}
