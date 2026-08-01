package com.rz.langchain.demo.server.tools;

import com.rz.langchain.demo.server.Helper;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SystemTools {
    @Tool("Run shell command")
    public String bash(@P("Shell command to run") String cmd,
                       @P("Working directory (optional)") String dir) throws Exception {
        System.out.println("\n" + Helper.GREEN + "⏺ Bash" + Helper.RESET + "(" + Helper.DIM + Helper.preview(cmd, 50) + Helper.RESET + ")");
        var pb = new ProcessBuilder("sh", "-c", cmd).redirectErrorStream(true);
        if (dir != null && !dir.isBlank()) pb.directory(new File(dir));
        var proc = pb.start();
        var out = new ArrayList<String>();
        try (var r = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
            String line;
            while ((line = r.readLine()) != null) {
                System.out.println("  " + Helper.DIM + "│ " + line + Helper.RESET);
                out.add(line);
            }
        }
        if (!proc.waitFor(30, TimeUnit.SECONDS)) {
            proc.destroyForcibly();
            out.add("(timed out after 30s)");
        }
        return out.isEmpty() ? "(empty)" : String.join("\n", out);
    }
}
