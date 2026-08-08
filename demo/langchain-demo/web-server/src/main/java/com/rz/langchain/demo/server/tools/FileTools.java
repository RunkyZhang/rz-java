package com.rz.langchain.demo.server.tools;

import com.rz.langchain.demo.server.Helper;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.file.Files.*;
import static java.nio.file.Files.readAllLines;

@Service
public class FileTools {
    @Tool("Read file with line numbers")
    public String read(@P("Path to the file") String path,
                       @P("Start line (optional)") Integer offset,
                       @P("Number of lines to read (optional)") Integer limit) throws IOException {
        System.out.println("\n" + Helper.GREEN + "⏺ Read" + Helper.RESET + "(" + Helper.DIM + path + Helper.RESET + ")");
        var lines = readAllLines(Path.of(path));
        int off = offset != null ? offset : 0;
        int lim = limit != null ? limit : lines.size();
        var sb = new StringBuilder();
        for (int i = off; i < Math.min(off + lim, lines.size()); i++)
            sb.append("%4d| %s%n".formatted(i + 1, lines.get(i)));
        var result = sb.toString();
        System.out.println("  " + Helper.DIM + "⎿  " + Helper.preview(result, 60) + Helper.RESET);
        return result;
    }

    @Tool("Write content to file")
    public String write(@P("Path to the file") String path,
                        @P("Content to write") String content) throws IOException {
        System.out.println("\n" + Helper.GREEN + "⏺ Write" + Helper.RESET + "(" + Helper.DIM + path + Helper.RESET + ")");
        writeString(Path.of(path), content);
        System.out.println("  " + Helper.DIM + "⎿  ok" + Helper.RESET);
        return "ok";
    }

    @Tool("Replace old with new in file (old must be unique unless all=true)")
    public String edit(@P("Path to the file") String path,
                       @P("Original string to replace") String old,
                       @P("New string") String repl,
                       @P("Replace all occurrences") Boolean all) throws IOException {
        System.out.println("\n" + Helper.GREEN + "⏺ Edit" + Helper.RESET + "(" + Helper.DIM + path + Helper.RESET + ")");
        var filePath = Path.of(path);
        var text = readString(filePath);
        if (!text.contains(old)) return "error: old_string not found";
        long count = (text.length() - text.replace(old, "").length()) / old.length();
        boolean replaceAll = all != null && all;
        if (!replaceAll && count > 1) return "error: old_string appears " + count + " times, must be unique (use all=true)";
        writeString(filePath, replaceAll ? text.replace(old, repl) : text.replaceFirst(Pattern.quote(old), Matcher.quoteReplacement(repl)));
        System.out.println("  " + Helper.DIM + "⎿  ok" + Helper.RESET);
        return "ok";
    }

    @Tool("Find files by pattern, sorted by mtime")
    public String glob(@P("Glob pattern (e.g. **/*.java)") String pat,
                       @P("Base path (optional)") String path) throws IOException {
        System.out.println("\n" + Helper.GREEN + "⏺ Glob" + Helper.RESET + "(" + Helper.DIM + pat + Helper.RESET + ")");
        var base = Path.of(path != null ? path : ".");
        var matcher = FileSystems.getDefault().getPathMatcher("glob:" + base + "/" + pat);
        if (!exists(base)) return "none";
        try (var walk = walk(base)) {
            var files = walk.filter(Files::isRegularFile).filter(matcher::matches)
                    .sorted((a, b) -> {
                        try { return getLastModifiedTime(b).compareTo(getLastModifiedTime(a)); }
                        catch (IOException e) { return 0; }
                    })
                    .map(Path::toString).toList();
            var result = files.isEmpty() ? "none" : String.join("\n", files);
            System.out.println("  " + Helper.DIM + "⎿  " + Helper.preview(result, 60) + Helper.RESET);
            return result;
        }
    }

    @Tool("Search files for regex pattern")
    public String grep(@P("Regex pattern") String pat,
                       @P("Base path (optional)") String path) throws IOException {
        System.out.println("\n" + Helper.GREEN + "⏺ Grep" + Helper.RESET + "(" + Helper.DIM + pat + Helper.RESET + ")");
        var pattern = Pattern.compile(pat);
        var base = Path.of(path != null ? path : ".");
        var hits = new ArrayList<String>();
        try (var walk = walk(base)) {
            walk.filter(Files::isRegularFile).takeWhile(f -> hits.size() < 50).forEach(file -> {
                try {
                    var lines = readAllLines(file);
                    for (int i = 0; i < lines.size() && hits.size() < 50; i++)
                        if (pattern.matcher(lines.get(i)).find())
                            hits.add(file + ":" + (i + 1) + ":" + lines.get(i));
                } catch (Exception e) { /* skip */ }
            });
        }
        var result = hits.isEmpty() ? "none" : String.join("\n", hits);
        System.out.println("  " + Helper.DIM + "⎿  " + Helper.preview(result, 60) + Helper.RESET);
        return result;
    }
}
