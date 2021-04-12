package aeshliman.datastructure;

import java.util.Iterator;

public class SortedList<E extends Comparable<E>> implements Iterable<E>
{
	// Instance Variables
	private Node<E> head;
	private int size;
	
	// Constructors
	public SortedList()
	{
		this.head = null;
		size = 0;
	}
	
	// Getters and Setters
	
	
	// Operations
	public void add(E data)
	{
		Node<E> node = new Node<E>(data);
		size++;
		
		if(head==null) head = node;
		else
		{
			if(node.compareTo(head)<0)
			{
				node.setNext(head);
				head = node;
			}
			else
			{
				Node<E> current = head;
				while(current.getNext()!=null)
				{
					if(node.compareTo(current.getNext())<0)
					{
						node.setNext(current.getNext());
						current.setNext(node);
						return;
					}
					current = current.getNext();
				}
				current.setNext(node);
				return;
			}
		}
	}
	
	public E get(int index)
	{
		if(index<0||index>=size) throw new IndexOutOfBoundsException();
		else
		{
			Node<E> current = head;
			for(int i=0; i<index; i++) { current = current.getNext(); }
			return current.getData();
		}
	}
	
	public E remove(int index)
	{
		if(index<0||index>=size) throw new IndexOutOfBoundsException();
		else
		{
			if(size==1)
			{
				Node<E> node = head;
				head = null;
				size--;
				return node.getData();
			}
			else
			{
				Node<E> current = head;
				for(int i=0; i<index-1; i++) { current = current.getNext(); }
				Node<E> node = current.getNext();
				current.setNext(node.getNext());
				node.setNext(null);
				size--;
				return node.getData();
			}
		}
	}
	
	public boolean remove(E data)
	{
		if(head==null) return false;
		else
		{
			if(head.getData()==data)
			{
				Node<E> temp = head;
				head = temp.getNext();
				temp.setNext(null);
				size--;
				return true;
			}
			Node<E> current = head;
			while(current.getNext()!=null)
			{
				if(current.getNext().getData()==data)
				{
					Node<E> temp = current.getNext();
					current.setNext(temp.getNext());
					temp.setNext(null);
					size--;
					return true;
				}
				current = current.getNext();
			}
		}
		
		return false;
	}
	
	public Iterator<E> iterator()
	{
		return new SortedListIterator<E>(this);
	}
	
	// toString
	public String toString()
	{
		String toString = "[";
		Node<E> current = head;
		while(current!=null)
		{
			toString += current.getData() + ", ";
			current = current.getNext();
		}
		if(head==null) return toString + "]";
		return toString.substring(0, toString.length()-2) + "]";
	}
	
	private class SortedListIterator<E extends Comparable<E>> implements Iterator<E>
	{
		Node<E> current;
		public SortedListIterator(SortedList<E> list) { this.current = list.head; }	
		public boolean hasNext() { return current!=null; }
		public E next()
		{
			E data = current.getData();
			current = current.getNext();
			return data;
		}
		
	}
}
