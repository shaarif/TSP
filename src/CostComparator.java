import java.util.Comparator;

/*Reference:http://stackoverflow.com/questions/683041/java-how-do-i-use-a-priorityqueue*/
public class CostComparator implements Comparator<Node> {
	public int compare(Node x, Node y)
    {
        if ((x.g+x.h) < (y.g+y.h))
        {
            return -1;
        }
        if ((x.g+x.h) > (y.g+y.h))
        {
            return 1;
        }
        return 0;
    }
}
