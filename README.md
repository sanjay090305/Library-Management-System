Database Connectivity (JDBC)

The core data of the library is stored in a relational database (like MySQL, PostgreSQL, or even SQLite). You'll use **Java Database Connectivity (JDBC)** to interact with it.

* **What it does:** Manages all primary records.
* **Example Tables:**
    1.  `Books`: Stores book details (ID, title, author, ISBN, quantity).
        * **Operations:** Add new books, update book details, delete books, search for books.
    2.  `Members`: Stores library member details (Member ID, name, email, phone).
        * **Operations:** Register new members, update member info, delete members.
    3.  `Transactions` (or `IssueReturn`): Tracks which member has borrowed which book.
        * **Operations:** Issue a book to a member (creating a new record), mark a book as returned (updating the record).

---

### File Handling (Java IO/NIO)

File handling is used for tasks that involve importing, exporting, or logging data, which aren't part of the main database transactions.

* **What it does:** Handles data import/export and system logging.
* **Example Features:**
    1.  **Generate Reports:** Allow the librarian to export a list of all overdue books (calculated by querying the database) into a **`.txt`** or **`.csv`** file for easy printing or review.
        * **Concepts:** `FileWriter`, `BufferedWriter`.
    2.  **Bulk Import Books:** Instead of adding books one by one, allow the user to upload a **`.csv`** file containing a list of new books, which your program will read and insert into the `Books` database table.
        * **Concepts:** `FileReader`, `BufferedReader`, parsing string data (`.split(",")`).
    3.  **Log Application Activity:** Create a log file (e.g., `library.log`) that records important events like "User 'admin' logged in" or "Book '1984' issued to Member '101'". This is great for debugging and auditing.
        * **Concepts:** `FileOutputStream`, `PrintWriter` (using `true` for append mode).
