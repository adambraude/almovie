import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A priority queue class implemented using a max heap.
 * Priorities cannot be negative.
 *
 * @author Eli Corpron and Adam Braude
 * @version 9.21.18
 *
 */
public class PriorityQueue {

    private Map<Integer, Integer> location;
    private List<Pair<Integer, Integer>> heap;

    /**
     *  Constructs an empty priority queue
     */
    public PriorityQueue() {
        location = new HashMap<Integer, Integer>();
        heap = new ArrayList<Pair<Integer, Integer>>();
    }

    /**
     *  Insert a new element into the queue with the
     *  given priority.
     *
     *	@param priority priority of element to be inserted
     *	@param element element to be inserted
     *
     *	<dt><b>Preconditions:</b><dd>
     *	<ul>
     *	<li> The element does not already appear in the priority queue.</li>
     *	<li> The priority is non-negative.</li>
     *	</ul>
     *
     */
    public void push(int priority, int element) {
        assert priority >= 0;
        assert location.containsKey(element) == false;
        heap.add(new Pair<Integer, Integer>(new Integer(priority), new Integer (element)));
        location.put(new Integer(element), heap.size()-1);
        percolateUp(heap.size()-1);
    }

    /**
     *  Remove the highest priority element
     *
     *	<dt><b>Preconditions:</b><dd>
     *	<ul>
     *	<li> The priority queue is non-empty.</li>
     *	</ul>
     *
     */
    public void pop(){
        assert heap.isEmpty() == false;
        swap(0, heap.size()-1);
        location.remove(heap.get(heap.size()-1).element);
        heap.remove(heap.size()-1);
        pushDown(0);
    }


    /**
     *  Returns the highest priority in the queue
     *  @return highest priority value
     *
     *	<dt><b>Preconditions:</b><dd>
     *	<ul>
     *	<li> The priority queue is non-empty.</li>
     *	</ul>
     */
    public int topPriority() {
        assert heap.isEmpty() == false;
        return heap.get(0).priority;
    }


    /**
     *  Returns the element with the highest priority
     *  @return element with highest priority
     *
     *	<dt><b>Preconditions:</b><dd>
     *	<ul>
     *	<li> The priority queue is non-empty.</li>
     *	</ul>
     */
    public int topElement() {
        assert heap.isEmpty() == false;
        return heap.get(0).element;
    }


    /**
     *  Change the priority of an element already in the
     *  priority queue, and restore heap so it has the heap
     *  priority
     *
     *  @param element element whose priority is to be changed
     *  @param newpriority the new priority
     *
     *	<dt><b>Preconditions:</b><dd>
     *	<ul>
     *	<li> The element exists in the priority queue</li>
     *	</ul>
     */
    public void changePriority(int element, int newpriority) {
        assert location.containsKey(element);
        heap.get(location.get(element)).priority = newpriority;
        percolateUp(location.get(element));
        pushDown(location.get(element));
    }


    /**
     *  Gets the priority of the element
     *
     *  @param element the element whose priority is returned
     *  @return the priority value
     *
     *	<dt><b>Preconditions:</b><dd>
     *	<ul>
     *	<li> The element exists in the priority queue</li>
     *	</ul>
     */
    public int getPriority(int element) {
        assert location.containsKey(element);
        return heap.get(location.get(element)).priority;
    }

    /**
     *  Returns true if the priority queue contains no elements
     *  @return true if the queue contains no elements, false otherwise
     */
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    /**
     *  Returns true if the element exists in the priority queue.
     *  @return true if the element exists, false otherwise
     */
    public boolean isPresent(int element) {
        return location.containsKey(element);
    }

    /**
     *  Removes all elements from the priority queue
     */
    public void clear() {
        heap.clear();
        location.clear();
    }

    /**
     *  Returns the number of elements in the priority queue
     *  @return number of elements in the priority queue
     */
    public int size() {
        return heap.size();
    }



    /*********************************************************
     * 				Private helper methods
     *********************************************************/


    /**
     * Push down a given element
     * @param start_index the index of the element to be pushed down
     * @return the index in the list where the element is finally stored
     */
    private int pushDown(int start_index) {
        int newindex = start_index;
        while(true) { //while one child is larger
            if (isLeaf(newindex) == true) { //if the node has no children
                break; //Node in correct location
            }

            if (hasTwoChildren(newindex) == false) { //Node has one child
                if (heap.get(newindex).priority < heap.get(left(newindex)).priority) { //If the parent has lower priority than the single child
                    swap(newindex, left(newindex));
                    newindex = left(newindex);
                }
                else { //Parent has larger priority, so is in the correct location
                    break; //Node in correct location
                }
            } else { //Know that the node has two children
                if (heap.get(left(newindex)).priority > heap.get(right(newindex)).priority) { //If left child has greater priority
                    if (heap.get(newindex).priority < heap.get(left(newindex)).priority) { //If the parent has lower priority than the left child
                        swap(newindex, left(newindex));
                        newindex = left(newindex);
                    } else { //Parent has larger priority, so is in the correct location
                        break; //Node in correct location
                    }
                } else { //right child is larger
                    if (heap.get(newindex).priority < heap.get(right(newindex)).priority) { //If the parent has lower priority than the right child
                        swap(newindex, right(newindex));
                        newindex = right(newindex);
                    } else { //Parent has larger priority, so is in the correct location
                        break; //Node in correct location
                    }
                }
            }
        }
        return newindex;
    }

    /**
     * Percolate up a given element until heap priority is restored
     * @param start_index the element to be percolated up
     * @return the index in the list where the element is finally stored
     */
    private int percolateUp(int start_index) {
        int newindex = start_index;
        while (heap.get(newindex).priority > heap.get(parent(newindex)).priority) {
            swap(newindex, parent(newindex));
            newindex = parent(newindex);

        }
        return newindex;
    }


    /**
     * Swaps two elements in the priority queue by updating BOTH
     * the list representing the heap AND the map
     * @param i element to be swapped
     * @param j element to be swapped
     */
    private void swap(int i, int j) {
        Pair<Integer, Integer> temp = heap.get(i); //Swapping locations in Pair
        heap.set(i, heap.get(j));
        heap.set(j, temp);

        location.replace(heap.get(i).element, i);
        location.replace(heap.get(j).element, j);
    }

    /**
     * Computes the index of the element's left child
     * @param parent index of element in list
     * @return index of element's left child in list
     */
    private int left(int parent) {
        return 2*parent + 1;
    }

    /**
     * Computes the index of the element's right child
     * @param parent index of element in list
     * @return index of element's right child in list
     */
    private int right(int parent) {
        return 2*parent + 2;
    }

    /**
     * Computes the index of the element's parent
     * @param child index of element in list
     * @return index of element's parent in list
     */

    private int parent(int child) {
        return (child - 1) / 2;
    }


    /*********************************************************
     * 	These are optional private methods that may be useful
     *********************************************************/
    /**
     * Returns true if element is a leaf in the heap
     * @param i index of element in heap
     * @return true if element is a leaf
     */
    private boolean isLeaf(int i){
        if ((2*i + 1) >= heap.size()) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Returns true if element has two children in the heap
     * @param i index of element in the heap
     * @return true if element in heap has two children
     */
    private boolean hasTwoChildren(int i) {
        if ((2*i + 2) < heap.size()) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Print the underlying list representation
     */
    private void printHeap() {
        for (int i = 0; i < heap.size(); i++) {
            System.out.print("("+heap.get(i).priority+", "+heap.get(i).element+")");
        }
        System.out.println("");
    }

    /**
     * Print the entries in the location map
     */
    private void printMap() {
        System.out.println(location.toString());
    }


    public static void main(String[] args) {
        System.out.println("Creating heap q1");
        PriorityQueue q1 = new PriorityQueue();

        System.out.println("Is the array empty: "+q1.isEmpty());

        System.out.println("pushing element 1 with priority 1");
        q1.push(1, 1);
        System.out.println("pushing element 2 with priority 2");
        q1.push(2, 2);
        System.out.println("pushing element 3 with priority 3");
        q1.push(3, 3);
        System.out.println("pushing element 4 with priority 4");
        q1.push(4, 4);
        System.out.println("pushing element 5 with priority 5");
        q1.push(5, 5);
        System.out.println("pushing element 6 with priority 6");
        q1.push(6, 6);
        System.out.println("pushing element 7 with priority 7");
        q1.push(7, 7);

        System.out.println("Popping");
        q1.pop();
        System.out.println("Popping");
        q1.pop();

        System.out.println("The top priority is: "+q1.topPriority());

        System.out.println("The top element is: "+q1.topElement());

        System.out.println("The priority of 4 is: "+q1.getPriority(4));
        System.out.println("Changing then priority of element 4 to 8:");
        q1.changePriority(4, 8);
        System.out.println("The priority of 4 is now: "+q1.getPriority(4));
        System.out.println("The top element is now: "+q1.topElement());

        System.out.println("Element 9 is in the array: "+q1.isPresent(9));
        System.out.println("Element 5 is in the array: "+q1.isPresent(5));

        System.out.println("The size of the array is: "+q1.size());

        System.out.println("Is the array empty: "+q1.isEmpty());

        System.out.println("Clearing the array.");
        q1.clear();
        System.out.println("The size is now: "+q1.size());
    }
}
