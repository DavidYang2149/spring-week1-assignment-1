package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TodoHttpHandler implements HttpHandler {
    private List<Task> tasks = new ArrayList<>();
    private ObjectMapper objectMapper = new ObjectMapper();
    private Long newId = 0L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        if(path.equals("/tasks")) {
            handleCollection(exchange, method);
            return;
        }

        if(path.startsWith("/tasks/")) {
            Long id = getTaskId(path);
            handleItem(exchange, method, id);
            return;
        }
    }

    private Long getTaskId(String path) {
        Pattern pattern = Pattern.compile("^\\/tasks\\/(\\d+)$");
        Matcher matcher = pattern.matcher(path);

        if(matcher.find()) {
            return Long.parseLong(matcher.group(1));
        }
        return null;
    }

    private void handleItem(HttpExchange exchange, String method, Long id) throws IOException {
        Task task = findTask(id);
        if(task == null) {
            send(exchange, 404, "");
            return;
        }

        if (method.equals("GET")) {
            handleDetail(exchange, task);
        }

        if(method.equals("DELETE")) {
            handleDelete(exchange, task);
        }

        if(method.equals("PUT") || method.equals("PATCH")) {
            handleUpdate(exchange, task);
        }
    }

    private void handleCollection(HttpExchange exchange, String method) throws IOException {
        if(method.equals("GET")) {
            handleList(exchange);
            return;
        }

        if(method.equals("POST")) {
            handleCreate(exchange);
            return;
        }

        send(exchange, 404, "");
    }

    private void handleList(HttpExchange exchange) throws IOException {
        send(exchange, 200, toJSON(tasks));
    }

    private void handleDetail(HttpExchange exchange, Task task) throws IOException {
        send(exchange, 200, toJSON(task));
    }

    private void handleCreate(HttpExchange exchange) throws IOException {
        String body = getBody(exchange);

        Task task = toTask(body);
        task.setId(generateId());
        tasks.add(task);

        send(exchange, 201, toJSON(task));
    }

    private void handleUpdate(HttpExchange exchange, Task task) throws IOException {
        String body = getBody(exchange);
        Task source = toTask(body);

        task.setTitle(source.getTitle());
        send(exchange, 200, toJSON(task));
    }

    private void handleDelete(HttpExchange exchange, Task task) throws IOException {
        tasks.remove(task);

        send(exchange, 200, "");
    }

    private String getBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));
    }

    private void send(HttpExchange exchange, int statusCode, String content) throws IOException {
        exchange.sendResponseHeaders(statusCode, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.close();
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String toJSON(Object object) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, object);

        return outputStream.toString();
    }

    private Task findTask(Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private Long generateId() {
        newId += 1;
        return newId;
    }
}
