package edu.scu.coen317.common.util;

import edu.scu.coen317.common.Configuration;
import edu.scu.coen317.common.model.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

public class NodeGlobalView {
    private static final Logger LOG = LoggerFactory.getLogger(NodeGlobalView.class);

    public static void addNode(Node node) throws IOException {
        ConcurrentSkipListSet<Node> nodes = readAll();
        nodes.add(node);
        syncFile(nodes);
    }

    public static void removeNode() {
        // currently unsupported
        throw new UnsupportedOperationException();
    }

    public static ConcurrentSkipListSet<Node> readAll() throws IOException {
        ConcurrentSkipListSet<Node> nodes = new ConcurrentSkipListSet<>();
        Path path = Paths.get(Configuration.NODE_LIST);
        if (!Files.exists(path)) {
            return nodes;
        }

        List<String> lines = Files.readAllLines(path);
        for (String line : lines) {
            Node node = new Node(line);
            nodes.add(node);
        }
        return nodes;
    }

    private static void syncFile(ConcurrentSkipListSet<Node> nodes) throws IOException {
        File file = new File(Configuration.NODE_LIST);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        FileWriter fw = new FileWriter(file);
        PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
        for (Node node : nodes) {
            pw.println(node.toString());
        }
        pw.close();
    }
}
