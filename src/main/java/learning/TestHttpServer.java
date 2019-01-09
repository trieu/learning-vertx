package learning;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;

public class TestHttpServer {

    static String readFile(String fileName) {

	// read file into stream, try-with-resources
	StringBuilder out = new StringBuilder();
	try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

	    stream.forEach(row -> {
		out.append(row).append("\n");
	    });

	} catch (IOException e) {
	    e.printStackTrace();
	}

	return out.toString();

    }

    public static void main(String[] args) {
	Vertx vertx = Vertx.vertx();
	HttpServer server = vertx.createHttpServer();
	server.requestHandler(request -> {

	    // This handler gets called for each request that arrives on the server
	    HttpServerResponse response = request.response();
	    response.putHeader("content-type", "text/plain");

	    // Write to the response and end it
	    String path = request.path();
	    System.out.println(path);

	    String name = request.params().get("name");
	    
	    if (path.equalsIgnoreCase("/hi")) {
		StringBuilder out = new StringBuilder();
		out.append("Hello  ").append(name);
		response.end(out.toString());
	    } else if (path.equalsIgnoreCase("/bye")) {
		StringBuilder out = new StringBuilder();
		out.append("Bye  ").append(name);
		response.end(out.toString());
	    }
	    else if (path.equalsIgnoreCase("/todo-app")) {
		response.putHeader("content-type", "text/html");
		String html = readFile("./view/todo.html");
		response.end(html);
	    }

	});

	int port = Integer.parseInt(args[0]);
	server.listen(port);
	System.out.println("Started HTTP Server at port " + port);
    }
}
