package com.example.toollibs.Activity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class RuntimeExec {
    private static volatile RuntimeExec instance;
    private Runtime runtime = Runtime.getRuntime();

    private RuntimeExec() {
    }

    public static RuntimeExec getInstance() {
        if (instance == null) {
            Class var0 = RuntimeExec.class;
            synchronized(RuntimeExec.class) {
                if (instance == null) {
                    instance = new RuntimeExec();
                }
            }
        }

        return instance;
    }

    public String executeCommand(String... command) {
        StringBuilder builder = new StringBuilder();
        InputStream inputStream = this.executeCommandGetInputStream(command);
        if (inputStream == null) {
            return "";
        } else {
            Scanner scanner = new Scanner(inputStream);

            while(scanner.hasNext()) {
                builder.append(scanner.nextLine());
                builder.append("\n");
            }

            scanner.close();

            try {
                inputStream.close();
            } catch (IOException var6) {
                var6.printStackTrace();
            }

            return builder.toString();
        }
    }

    public static void takeScreenShot(String file) {
        Process process = null;
        DataOutputStream os = null;

        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("screencap -p " + file);
            os.flush();
            process.waitFor();
            return;
        } catch (Exception var14) {
            var14.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }

                process.destroy();
            } catch (Exception var13) {
            }

        }

    }

    private InputStream executeCommandGetInputStream(String... command) {
        try {
            Process process = command.length == 1 ? this.runtime.exec(command[0]) : this.runtime.exec(command);
            (new RuntimeExec.StreamGobbler(process.getErrorStream())).start();
            return process.getInputStream();
        } catch (IOException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    class StreamGobbler extends Thread {
        InputStream mInputStream;

        public StreamGobbler(InputStream is) {
            this.mInputStream = is;
        }

        public void run() {
            try {
                InputStreamReader isr = new InputStreamReader(this.mInputStream);
                BufferedReader br = new BufferedReader(isr);

                while(true) {
                    if (br.readLine() != null) {
                        continue;
                    }
                }
            } catch (IOException var3) {
                var3.printStackTrace();
            }

        }
    }
}
