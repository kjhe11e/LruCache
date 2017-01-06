import java.io.*;
import java.util.*;
import java.lang.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

class Node {
	String key;
	String data;
	Node prev;
	Node next;

	public Node(String key, String data) {
		this.key = key;
		this.data = data;
	}
}

public class LruCache {
	int totalSize;
	HashMap<String, Node> hm;
	Node head, tail;

	public LruCache(int size) {
		this.totalSize = size;
		hm = new HashMap<String, Node>();
		head = new Node("dummy", "dummyVal");
		tail = new Node("dummy", "dummyVal");
		head.next = tail;
		head.prev = null;
		tail.next = null;
		tail.prev = head;
	}

	public void print() {
		Node tmp = head.next;
		while(tmp.next != null){
			Node n = hm.get(tmp.key);
			String data = n.data;
			System.out.println(tmp.key + ": " + data);
			tmp = tmp.next;
		}
	}

	public void addToHead(Node n) {
		n.next = head.next;
		n.prev = head;
		if(n.next != null) {
			n.next.prev = n;
		}
		head.next = n;
	}

	public void removeNode(Node n) {
		if(n == head) {
			head = head.next;
		}
		else {
			n.prev.next = n.next;
			n.next.prev = n.prev;
		}
	}

	public String get(String key) {
		if(hm.get(key) != null) {
			Node n = hm.get(key);
			String result = n.data;
			removeNode(n);
			addToHead(n);
			return "Got " + result;
		}
		else {
			return "Not found";
		}
	}

	public void set(String key, String data) {
		if(hm.get(key) != null) {
			Node n = hm.get(key);
			n.data = data;
			removeNode(n);
			addToHead(n);
		}
		else {
			Node n = new Node(key, data);
			hm.put(key, n);
			if(hm.size() <= totalSize) {
				addToHead(n);
			}
			else {
				if(tail.prev != head){
					hm.remove(tail.prev.key);
				}
				removeNode(tail.prev);
				addToHead(n);
			}
		}
		System.out.println("Set OK");
	}

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int size = 0;
        String sizeCmd = "";

        System.out.println("Welcome to my LRU (least recently used) cache");
        System.out.println("Once the size is entered, valid commands are GET, SET, PRINT, and EXIT");
        
        while(size <= 0) {
        	System.out.println("Size (must be greater than 0): ");
        	sizeCmd = scanner.nextLine();
        	try {
        		size = Integer.parseInt(sizeCmd);
        	}
        	catch(NumberFormatException e) {

        	}
        }
        
        LruCache cache = new LruCache(size);
        System.out.println("Size of cache is: " + cache.totalSize);
        if(cache != null) {
        	System.out.println("Size OK");
        }

        String cmd = scanner.nextLine();
        while(!cmd.equals("EXIT")) {
        	String[] input = cmd.split(" ");
        	if(input.length > 0) {
        		if(input[0].equals("GET")) {
        			if(input.length == 2) {
        				String index = input[1];
        				String result = cache.get(index);
        				System.out.println(result);
        			}
        			else {
        				System.out.println("Error: GET format is GET x");
        			}
        		}
        		else if(input[0].equals("SET")) {
        			if(input.length == 3) {
        				String key = input[1];
        				String data = input[2];
        				cache.set(key, data);
        			}
        			else {
        				System.out.println("Error: SET format is SET key data");
        			}
        		}
        		else if(input[0].equals("PRINT")) {
        			cache.print();
        		}
        		else {
        			System.out.println("Error: valid commands are GET, SET, PRINT, and EXIT");
        		}
        	}
        	else {
        		System.out.println("Error");
        	}
        	cmd = scanner.nextLine();
        }
    }
}

