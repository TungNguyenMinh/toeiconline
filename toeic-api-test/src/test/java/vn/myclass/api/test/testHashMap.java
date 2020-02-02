package vn.myclass.api.test;

import vn.myclass.model.Book;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class testHashMap {
    @Test
    public void checkHashMap() {
        Book book1 = returnBook(101, "Let us B", "Yashwant Kanetkar1", "BPB1", 8);
        Book book2 = returnBook(102, "Let us A", "Yashwant Kanetkar2", "BPB2", 9);
        Book book3 = returnBook(103, "Let us C", "Yashwant Kanetkar3", "BPB3", 10);

        Map<Integer, Book> map = new HashMap<Integer, Book>();
        map.put(1, book1);
        map.put(2, book2);
        map.put(3, book3);
        for (Map.Entry<Integer, Book> items : map.entrySet()) {
            System.out.println("Cột: " + items.getKey());
            Book item = items.getValue();
            System.out.println(item.getId() + "-" + item.getName() + "-" + item.getAuthor() + "-" + item.getPublisher() + "-" + item.getQuantity());
        }
    }

    public Book returnBook(int id, String name, String author, String publisher, int quantity) {
        Book book = new Book();
        book.setId(id);
        book.setName(name);
        book.setAuthor(author);
        book.setPublisher(publisher);
        book.setQuantity(quantity);
        return book;
    }
}
