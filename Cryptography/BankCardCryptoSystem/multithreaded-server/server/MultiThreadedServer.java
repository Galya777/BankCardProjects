package multithreaded.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A multi-threaded server that handles multiple client connections using Java NIO.
 */
public class MultiThreadedServer {
    private final int port;
    private ServerSocketChannel serverChannel;
    private Selector selector;
    private boolean isServerWorking;
    private final ExecutorService executorService;
    private final CommandProcessor commandProcessor;

    /**
     * Creates a new MultiThreadedServer instance.
     * @param port The port to listen on
     */
    public MultiThreadedServer(int port) {
        this.port = port;
        this.executorService = Executors.newCachedThreadPool();
        this.commandProcessor = new CommandProcessor();
        
        // Add shutdown hook to ensure clean server shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    /**
     * Starts the server and begins accepting client connections.
     */
    public void start() {
        try {
            // Open a selector
            selector = Selector.open();
            
            // Open a server socket channel
            serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(port));
            serverChannel.configureBlocking(false);
            
            // Register the server socket channel with the selector for accept operations
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            
            System.out.println("Server started on port " + port);
            isServerWorking = true;
            
            // Main server loop
            while (isServerWorking) {
                // Wait for events
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    if (!isServerWorking) {
                        break; // Server is shutting down
                    }
                    continue;
                }
                
                // Process selected keys
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
                
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    
                    if (key.isAcceptable()) {
                        // Accept a new connection
                        acceptConnection();
                    } else if (key.isReadable()) {
                        // Handle client read in a separate thread
                        handleClientRead(key);
                    }
                    
                    keyIterator.remove();
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        } finally {
            stop();
        }
    }
    
    private void acceptConnection() throws IOException {
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        
        // Create a new client handler for this connection
        ClientHandler clientHandler = new ClientHandler(clientChannel, commandProcessor);
        
        // Start the client handler in a new thread
        new Thread(clientHandler).start();
        
        System.out.println("Client connected: " + clientChannel.getRemoteAddress());
    }
    
    private void handleClientRead(SelectionKey key) {
        // This method is no longer used as we're handling clients in ClientHandler
        // Keeping it for backward compatibility
        key.interestOps(0); // Disable further read events
    }
    
    /**
     * Stops the server and releases all resources.
     */
    public void stop() {
        isServerWorking = false;
        
        // Wake up the selector to exit the select() call
        if (selector != null) {
            selector.wakeup();
        }
        
        // Shutdown the executor service
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        // Close server channel
        if (serverChannel != null) {
            try {
                serverChannel.close();
            } catch (IOException e) {
                System.err.println("Error closing server channel: " + e.getMessage());
            }
        }
        
        // Close selector
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                System.err.println("Error closing selector: " + e.getMessage());
            }
        }
        
        System.out.println("Server stopped");
    }
    
    public static void main(String[] args) {
        int port = 8080; // Default port
        if (args.length > 0) {
        buffer.clear();

        int readBytes = clientChannel.read(buffer);
        if (readBytes < 0) {
            return null;
        }

        buffer.flip();
        byte[] clientInputBytes = new byte[buffer.remaining()];
        buffer.get(clientInputBytes);

        return new String(clientInputBytes, StandardCharsets.UTF_8);
    }

    private void writeClientOutput(SocketChannel clientChannel, String output) throws IOException {
        if (output == null || output.isEmpty()) {
            return;
        }
        
        buffer.clear();
        buffer.put(output.getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        
        while (buffer.hasRemaining()) {
            clientChannel.write(buffer);
        }
    }

    private void accept(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
        System.out.println("New client connected: " + clientChannel.getRemoteAddress());
    }

    private void closeClient(SocketChannel clientChannel) {
        if (clientChannel != null && clientChannel.isOpen()) {
            try {
                System.out.println("Client disconnected: " + clientChannel.getRemoteAddress());
                clientChannel.close();
            } catch (IOException e) {
                System.err.println("Error closing client channel: " + e.getMessage());
            }
        }
    }
}
