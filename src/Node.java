import java.util.ArrayList;
public class Node {
public String currentCity;
public double g;  //pathcost
public double h;  //heuristic
public ArrayList<String> ancestorList=new ArrayList<String>();
public Node(String nodeName, double pathCost, double heuristicCost, ArrayList<String> aList )
{

	currentCity=nodeName;
	g=pathCost;
	h=heuristicCost;
	ancestorList.addAll(aList);
}
}
