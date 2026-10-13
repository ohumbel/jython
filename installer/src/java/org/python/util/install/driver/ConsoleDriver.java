package org.python.util.install.driver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * A class driving another class, while the other class is performing console I/O.
 * 
 * <pre>
 *   (2)  [Driver]   receives question  [Tunnel]   sends question   [Console]  (1)
 *   (3)  [Driver]   sends answer       [Tunnel]   receives answer  [Console]  (4)
 * </pre>
 */
public class ConsoleDriver extends Thread {

    private Tunnel tunnel;
    private List<String> answers;

    public ConsoleDriver(Tunnel tunnel, List<String> answers) {
        this.tunnel = tunnel;
        this.answers = answers;
    }

    /**
     * Send answers in the correct sequence, as soon as the question is asked.
     */
    public void run() {
        for (String answer : answers) {
            try {
                readLine();
                sendAnswer(answer);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendAnswer(String answer) throws IOException, InterruptedException {
        Thread.sleep(100); // wait to be sure the question is really issued on the other end of the tunnel
        System.out.println(" -> driving: '" + answer + "'");
        answer += Tunnel.NEW_LINE;
        tunnel.getAnswerSenderStream().write(answer.getBytes());
        tunnel.getAnswerSenderStream().flush();
    }

    private void readLine() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(tunnel.getQuestionReceiverStream()));
        reader.readLine();
    }

}
