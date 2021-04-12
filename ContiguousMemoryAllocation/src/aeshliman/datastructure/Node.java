package aeshliman.datastructure;

public class Node<E extends Comparable<E>> implements Comparable<Node<E>>
{
	// Instance Variables
	private E data;
	private Node<E> next;
	
	// Constructors
	public Node(E data)
	{
		this.data = data;
		this.next = null;
	}
	
	// Getters and Setters
	public Node<E> getNext() { return this.next; }
	public E getData() { return this.data; }
	public void setNext(Node<E> node) { this.next = node; }
	
	// Operations
	public int compareTo(Node<E> node) { return data.compareTo(node.data); }
	
	// toString
	
}
