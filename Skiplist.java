import java.util.*;
import java.io.*;

public class Skiplist {
    public class Node {
        int key;
        Node above, below, prev, next;
        Node(int d) {
            key = d;
            above = below = prev = next = null;
        }
    }
    Node head, tail;
    final int NEG_INFINITY = Integer.MIN_VALUE;
    final int POS_INFINITY = Integer.MAX_VALUE;
    int height = 0;
    public Random random = new Random();
    Skiplist() {
        head = new Node(NEG_INFINITY);
        tail = new Node(POS_INFINITY);
        head.next = tail;
        tail.prev = head;
    }
    public int search(int d) {
        Node n = head;
        if ((d == n.key) || (d == n.next.key)) {
            return 1;
        }
        while (n.below != null) {
            n = n.below;
            while (d >= n.next.key) {
                if (d == n.next.key) {
                    return 1;
                }
                n = n.next;
            }
        }
        return 0;
    }
    public void traverse() {
        int h = height;
        Node p = head;
        while (p.below != null) {
            p = p.below;
            Node q = p;
            System.out.println("Height:" + (h - 1));
            int count = 0;
            while (q != null) {
                System.out.print(q.key + "-");
                q = q.next;
                count++;
            }
            System.out.print("\nNodes in current level: " + count);
            h--;
            System.out.println();
        }
    }
    public void insert(int d) {
        if (search(d) == 1) {
            return;
        }
        int current_height = 0;
        do {
            current_height++;
            check_add_level(current_height);
        } while (random.nextBoolean() == true);
        add_level_wise(d, current_height);
    }
    private void check_add_level(int current_height) {
        if (current_height >= height) {
            add_empty_level();
            height++;
        }
    }
    public void add_empty_level() {
        Node new_head = new Node(NEG_INFINITY);
        Node new_tail = new Node(POS_INFINITY);
        new_head.next = new_tail;
        new_tail.prev = new_head;
        new_head.below = head;
        new_tail.below = tail;
        head.above = new_head;
        tail.above = new_tail;
        head = new_head;
        tail = new_tail;
    }
    public void add_level_wise(int d, int current_height) {
        int h = height;
        Node p = head;
        while (current_height < h) {
            h--;
            p = p.below;
        }
        Node t = null;
        while (p.below != null) {
            p = p.below;
            Node nn = new Node(d);
            Node q = p;
            while (d >= q.next.key) {
                q = q.next;
            }
            nn.next = q.next;
            q.next.prev = nn;
            nn.prev = q;
            q.next = nn;
            if (t != null) {
                nn.above = t;
                t.below = nn;
            }
            t = nn;
        }
    }
    static void make_file() {
        try {
            File f = new File("test.txt");
            FileWriter g = new FileWriter(f);
            PrintWriter h = new PrintWriter(g);
            Random random = new Random();
            for (int i = 0; i < 1000; i++) {
                h.println(random.nextInt(100000));
            }
            h.close();
        } 
	catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
    }
    public void insert_from_file() {
        try {
            File f = new File("test.txt");
            Scanner s = new Scanner(f);
            for (int i = 0; i < 1000; i++) {
                int d = s.nextInt();
                insert(d);
            }
            s.close();
        } 
	catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
    }
    public int search_print(int d) {
        Node p = head;
        if (d == p.key) {
            System.out.println("Height: " + (height - 1));
            System.out.println("No comparisons");
            return 1;
        }
	else if (d == p.next.key) {
            System.out.println("Height: " + (height - 1));
            System.out.println("Comparisons: 1");
            return 1;
        }
        int h = height - 1;
        while (p.below != null) {
            p = p.below;
            int count = 0;
            System.out.println("Height: " + h);
            while (d >= p.next.key) {
                System.out.print(p.next.key + " ");
                count++;
                if (d == p.next.key) {
                    System.out.println("\nComparisons: " + count);
                    return 1;
                }
                p = p.next;
            }
            if (count == 0) {
                System.out.println("No comparisons");
            } 
	    else {
                System.out.println("\nComparisons: " + count);
            }
            h--;
        }
        return 0;
    }
    public void delete_node(int d) {
        if (search(d) == 0) {
            return;
        }
        Node p = head;
        while (p.below != null) {
            p = p.below;
            Node q = p;
            while (d >= q.key) {
                if (d == q.key) {
                    Node a = q.above;
                    Node b = q.below;
                    Node c = q.prev;
                    Node e = q.next;
                    c.next = e;
                    e.prev = c;
                    a = null;
                    b = null;
                }
                q = q.next;
            }
        }
    }
    public void check_level() {
        Node p = head.below;
        int count = 0;
        while (p != null) {
            p = p.next;
            count++;
        }
        if (count == 2) {
            delete_level();
            height--;
            check_level();
        }
    }
    private void delete_level() {
        Node p = head.below;
        Node q = p;
        while (p != null) {
            p.above = null;
            p = p.next;
        }
        Node r = head;
        while (r != null) {
            r.below = null;
            r = r.next;
        }
        head = q;
        while (q.next != null) {
            q = q.next;
        }
        tail = q;
    }
    public static void main(String[] args) {
        Skiplist t = new Skiplist();
        Scanner s = new Scanner(System.in);
        make_file();
        t.insert_from_file();
        t.traverse();
        System.out.println("Enter the key to search");
        int d = s.nextInt();
        if (t.search(d) == 0) {
            t.search_print(d);
            System.out.println(d + " not found");
        } 
	else {
            t.search_print(d);
            System.out.println(d + " found");
        }
        System.out.println("Enter the key to delete");
        d = s.nextInt();
        t.delete_node(d);
        t.check_level();
        t.add_empty_level();
        t.height++;
        t.traverse();
        s.close();
    }
}
