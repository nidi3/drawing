package org.mimacom.fun.drawing;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: stni
 * Date: 16.12.11
 * Time: 12:58
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    private JFrame frame;
    private JTextArea[] group = new JTextArea[4];
    private JTextArea[] priority = new JTextArea[4];
    private Random rnd = new Random();

    public Main() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000);
        frame.setTitle("Drawing");
        frame.setLayout(new BorderLayout());

        JButton start = new JButton("Start");
        frame.add(start, BorderLayout.SOUTH);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                    @Override
                    protected Void doInBackground() throws Exception {
                        String[] prios = new String[4];
                        for (int i = 0; i < 4; i++) {
                            prios[i] = priority[i].getText();
                        }
                        for (int i = 0; i < 4; i++) {
                            group[i].setText("");
                        }
                        int g = 0;
                        for (int prio = 0; prio < 4; prio++) {
                            mark(priority, prio);
                            priority[prio].setText(" " + priority[prio].getText().trim().replace("\n", "\n "));
                            int len = lines(priority[prio].getText().trim());
                            for (int i = 0; i < len; i++) {
                                mark(group, g);
                                int count = rnd.nextInt(20) + 15;
                                int j = 0;
                                for (; j < count; j++) {
                                    priority[prio].setText(mark(priority[prio].getText(), j % (len - i)));
                                    sleep(100);
                                }
                                sleep(1000);
                                String selName = extractLine(priority[prio], (j - 1) % (len - i));
                                group[g].setText(group[g].getText() + ((group[g].getText().length() > 0) ? "\n" : "") + selName);
                                g++;
                                if (g == 4) {
                                    g = 0;
                                }
                                sleep(1000);
                            }
                        }
                        for (int i = 0; i < 4; i++) {
                            priority[i].setText(prios[i]);
                        }
                        mark(priority, -1);
                        mark(group, -1);
                        return null;
                    }
                };
                worker.execute();
            }
        });

        GridLayout layout = new GridLayout(2, 4);
        layout.setHgap(20);
        JPanel center = new JPanel(layout);
        frame.add(center, BorderLayout.CENTER);
        Font font = new JLabel().getFont().deriveFont(24f);
        for (int i = 0; i < 4; i++) {
            JPanel panel = new JPanel();
            center.add(panel);
            panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
            JLabel label = new JLabel("Group " + i);
            label.setFont(font);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(label);
            group[i] = new JTextArea();
            group[i].setFont(font);
            group[i].setEditable(false);
            panel.add(group[i]);
        }

        for (int i = 0; i < 4; i++) {
            JPanel panel = new JPanel();
            center.add(panel);
            panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
            JLabel label = new JLabel("Priority " + i);
            label.setFont(font);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(label);
            priority[i] = new JTextArea();
            priority[i].setFont(font);
            panel.add(priority[i]);
        }

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((screen.width - frame.getWidth()) / 2, (screen.height - frame.getHeight()) / 2);
        frame.setVisible(true);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e1) {
        }
    }

    private int lines(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        int res = 1;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '\n') {
                res++;
            }
        }
        return res;
    }

    private void mark(JTextArea[] areas, int n) {
        for (int i = 0; i < 4; i++) {
            if (i == n) {
                areas[i].setBorder(new LineBorder(Color.RED, 3));
            } else {
                areas[i].setBorder(null);
            }
        }
    }

    private String mark(String text, int line) {
        String s = "\n" + text + "\n";
        int cl = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '\n') {
                if (cl == line) {
                    i++;
                    s = s.substring(0, i) + "*" + s.substring(i + 1);
                }
                cl++;
            } else if (s.charAt(i) == '*') {
                s = s.substring(0, i) + " " + s.substring(i + 1);
            }
        }
        return s.substring(1, s.length() - 1);
    }

    private String extractLine(JTextArea text, int line) {
        int cl = 0;
        int lastPos = -1, currPos = -1;
        String s = text.getText() + "\n";
        for (int i = 0; i < s.length() && cl <= line; i++) {
            if (s.charAt(i) == '\n') {
                cl++;
                lastPos = currPos;
                currPos = i;
            }
        }
        String res = s.substring(lastPos + 1, currPos);
        String newText = s.substring(0, lastPos + 1);
        if (currPos + 1 < s.length() - 1) {
            newText += s.substring(currPos + 1, s.length() - 1);
        }
        text.setText(newText);
        return res;
    }
}
