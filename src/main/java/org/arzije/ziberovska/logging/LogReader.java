package org.arzije.ziberovska.logging;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class LogReader implements Runnable {
    private final String filePath;
    private final Consumer<String> callback;
    private long lastFileSize = 0;

    public LogReader(String filePath, Consumer<String> callback) {
        this.filePath = filePath;
        this.callback = callback;
    }

    @Override
    public void run() {
        while (true) {
            try (FileInputStream fis = new FileInputStream(filePath)) {
                if (fis.available() > lastFileSize) {
                    fis.skip(lastFileSize);
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            callback.accept(line);
                        }
                        lastFileSize = fis.available();
                    }
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
