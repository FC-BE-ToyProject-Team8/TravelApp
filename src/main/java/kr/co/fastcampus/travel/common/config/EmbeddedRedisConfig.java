package kr.co.fastcampus.travel.common.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import redis.embedded.RedisServer;

@Configuration
public class EmbeddedRedisConfig {

    @Value("${redis.port}")
    private int redisPort;

    private RedisServer redisServer;

    @PostConstruct
    public void startRedis() throws IOException {
        int port = isRedisRunning() ? findAvailablePort() : redisPort;
        if (isArmArchitecture()) {
            redisServer = new RedisServer(Objects.requireNonNull(getRedisServerExecutable()), port);
        } else {
            redisServer = RedisServer.builder()
                    .port(port)
                    .build();
        }
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        redisServer.stop();
    }

    private boolean isRedisRunning() throws IOException {
        return isRunning(executeGrepProcessCommand(redisPort));
    }

    public int findAvailablePort() throws IOException {
        for (int port = 10000; port <= 65535; port++) {
            Process process = executeGrepProcessCommand(port);
            if (!isRunning(process)) {
                return port;
            }
        }

        throw new IllegalArgumentException("Not Found Available port: 10000 ~ 65535");
    }

    private Process executeGrepProcessCommand(int port) throws IOException {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            String command = String.format("netstat -nao | find \"LISTEN\" | find \"%d\"", port);
            String[] shell = {"cmd.exe", "/y", "/c", command};
            return Runtime.getRuntime().exec(shell);
        }
        String command = String.format("netstat -nat | grep LISTEN|grep %d", port);
        String[] shell = {"/bin/sh", "-c", command};
        return Runtime.getRuntime().exec(shell);
    }

    private boolean isRunning(Process process) {
        String line;
        StringBuilder pidInfo = new StringBuilder();

        try (BufferedReader input = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            while ((line = input.readLine()) != null) {
                pidInfo.append(line);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return StringUtils.hasText(pidInfo.toString());
    }

    private File getRedisServerExecutable() {
        try {
            return new File("src/main/resources/binary/redis/redis-server-7.0.3-mac-arm64");
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private boolean isArmArchitecture() {
        return System.getProperty("os.arch").contains("aarch64");
    }
}
