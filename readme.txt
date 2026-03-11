🔹 ExecutorService
In real projects, we don’t create threads manually again and again.
We use ExecutorService to manage a pool of threads.
It reuses threads and improves performance.
👉 Think of it like a company having a group of workers ready.
Instead of hiring new workers every time, you give tasks to the same workers.

🔹 Future<String>
When a task runs in another thread, the result is not ready immediately.
Future is like a promise:
“I will give you the result later when the task is complete.”
Future<String> means the result will be a String.
👉 Like ordering food online — you don't get food instantly,
but you get an order ID (Future). Food (String) will come later.

🔹 Runnable
A task for a thread that does NOT return a value.
Just runs some code.
👉 Example: Print a message, write a log, send a notification.

🔹 Callable
A task for a thread that RETURNS a value.
Used when you want a result back.
👉 Example: Fetch user data from DB and return it.

🔹 ThreadLocal
A separate variable copy for each thread.
Even if many threads use the same code, each thread has its own value.
👉 Example: User session ID — every thread handling different users keeps its own session ID safely.

🔹 volatile
A shared variable between all threads, but with a guarantee:
When one thread updates the value, other threads will see the new value immediately.
👉 Without volatile, threads might read an old cached value.

🔹 synchronized
Only one thread can access the code block at a time.
Prevents multiple threads from modifying shared data at the same time.
alao use distributed lock incase of microservice 
👉 Like only one person allowed inside the washroom at a time — others must wait.

🔹 Race Condition
Two or more threads access the same shared variable at the same time.
This causes unpredictable or wrong results.
👉 Like two people writing on the same paper at the same time — the output becomes messy.

🔹 ReentrantLock
A more advanced version of synchronized that gives full manual control.
You decide exactly when to lock and when to unlock.
While one thread holds the lock, all other threads must wait.
👉 Like having a special key: one person uses it to enter a room, others wait until the key is returned.

🔹 ReadWriteLock
Allows multiple threads to read at the same time, but only one writer is allowed.
Readers don’t block each other, but a writer blocks everyone.
ReadWriteLock guarantees:
    While readers are reading → writer MUST wait
    While writer is writing → readers MUST wait
👉 Readers & writer cannot run together.
👉 Like a library: many people can read books together, but only one person can update the book at a time.

🔹 AtomicInteger -> AtomicInteger is a class in Java that lets you update an integer safely in a multi-threaded 
environment without using synchronized. It uses a technique called CAS (Compare And Swap) which is very 
fast and non-blocking. Its for integers and counters only. 
👉 Meaning:
Check if value is still what I saw.
If yes → update.
If no → someone changed it → retry(get new value and updates).

🔹 ThreadPoolTaskExecutor -> It is a thread pool used in Spring Boot to run tasks in background threads.
corePoolSize = 5 -> New tasks first go into these 5 threads, active forever
queueCapacity = 10 -> First 5 tasks → run in core threads,Next 10 tasks → stored in queue,After queue is full → then new threads (beyond core) are created
maxPoolSize = 20 -> Spring can create at most 20 threads,Only used when queue is full,After work is complete, extra threads die
@EnableAsync + @Async -> Method run on multiple threads
👉 Meaning:More task after the maxpool is rejected 

🔹 CompletableFuture<String> -> CompletableFuture<String> is a Java class that represents a 
future result of an asynchronous computation that will eventually produce a String.
->thenApply
CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 10)
    .thenApply(n -> n * 2); // transforms 10 → 20
System.out.println(future.join()); // 20
->thenCombine
CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(() -> 10);
CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(() -> 20);
CompletableFuture<Integer> sum = f1.thenCombine(f2, (a, b) -> a + b);
System.out.println(sum.join()); // 30
->thenRun
CompletableFuture.supplyAsync(() -> "Task Done")
    .thenRun(() -> System.out.println("All done!"));
👉 Can use it inplace of ThreadPoolTaskExecutor

🔹 Isolation level controls problems:
Dirty Read → reading uncommitted data  ->  @Transactional(isolation = Isolation.READ_COMMITTED)
Non-repeatable Read → row changed between two reads  ->  @Transactional(isolation = Isolation.REPEATABLE_READ)
Phantom Read → number of rows changed  ->  @Transactional(isolation = Isolation.SERIALIZABLE)
Lost Update → one update overrides another
👉 Problem caused in DB layers by different threads

🔹 Optimistic Locking (Fix Lost Update)
Optimistic locking uses a @Version field on a row.
When two threads read the same row and try to update it:
First update succeeds and increases the version.
Second update fails because the version has changed.
👉 This prevents lost updates without using DB locks → high scalability.

🔹 Pessimistic Locking (Fix Lost Update)
Pessimistic Locking (SELECT … FOR UPDATE)
A database-level lock that blocks other transactions from modifying a row until the current transaction finishes.
Used when you assume conflicts WILL happen and want strict safety.
👉 Think of it as:
“Lock the row now. Others wait.”

🔹 Concurrent Collections:
Thread-safe collection classes in java.util.concurrent that allow multiple threads to read and 
write without corrupting data or throwing ConcurrentModificationException. 
Examples: 
ConcurrentHashMap
CopyOnWriteArrayList 
ConcurrentLinkedQueue
👉 Thread safe version of then self, no manual work

🔹 WebFlux
Spring WebFlux provides FastAPI-style async/non-blocking APIs in Java using reactive streams and an event-loop model.
If a code is waiting for other api/operation response the thread meanwhile process other request. 
// Single blog creation
public Mono<Blog> createBlog(BlogRequest request) {
    return blogService.save(request); // may return one Blog
}
// Multiple blogs fetch
public Flux<Blog> getAllBlogs() {
    return blogRepository.findAll(); // may return many Blogs
}

persistent lock in db 
BEGIN;

SELECT *
FROM orders
WHERE id = 10
FOR UPDATE;

UPDATE orders
SET status = 'SHIPPED'
WHERE id = 10;

COMMIT;
