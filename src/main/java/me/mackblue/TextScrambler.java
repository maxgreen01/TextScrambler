package me.mackblue;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import java.awt.event.*;
import java.util.*;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class TextScrambler {

    private String input;
    private String[] words;

    public static void main(String[] args) {
        createAndShowGUI();
    }

    public static void createAndShowGUI() {
        JFrame frame = new JFrame("Text Scrambler");
        frame.setPreferredSize(new Dimension(850, 750));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(450, 400));

        TextScrambler scrambler = new TextScrambler();
        scrambler.populateContentPane(frame.getContentPane());

        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    public void populateContentPane(Container contentPane) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));

        JLabel titleLabel = new JLabel("Text Scrambler");
        titleLabel.setAlignmentX(Box.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 25));
        JLabel subTitleLabel = new JLabel("All the letters in each word will be scrambled except the word's first and last letters!");
        subTitleLabel.setAlignmentX(Box.CENTER_ALIGNMENT);
        subTitleLabel.setFont(new Font(subTitleLabel.getFont().getName(), Font.BOLD, 15));
        JLabel subTitleLabel2 = new JLabel("Note: special characters (including punctuation) will not be scrambled, and words separated by a hyphen will be scrambled separately.");
        subTitleLabel2.setAlignmentX(Box.CENTER_ALIGNMENT);
        subTitleLabel2.setFont(new Font(subTitleLabel.getFont().getName(), Font.BOLD, 12));

        JTextArea inputArea = new JTextArea("Type your original text here");
        inputArea.setForeground(Color.GRAY);
        inputArea.setFont(inputArea.getFont().deriveFont(15f));
        inputArea.setLineWrap(true);
        inputArea.setMinimumSize(new Dimension(100, 100));
        inputArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (inputArea.getForeground().equals(Color.GRAY)) {
                    inputArea.setText("");
                    inputArea.setForeground(Color.BLACK);
                }
            }
        });
        inputArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (inputArea.getForeground().equals(Color.GRAY)) {
                    inputArea.setText("");
                    inputArea.setForeground(Color.BLACK);
                }
            }
        });

        TitledBorder inputTitle = new TitledBorder("Unscrambled Text");
        inputTitle.setTitleFont(inputTitle.getTitleFont().deriveFont(20f));
        inputTitle.setBorder(BorderFactory.createLineBorder(((LineBorder) inputTitle.getBorder()).getLineColor(), 3, true));
        CompoundBorder inputBorder = BorderFactory.createCompoundBorder(inputTitle, BorderFactory.createEmptyBorder(5, 5, 10, 5));

        Box inputBox = Box.createHorizontalBox();
        inputBox.setBorder(inputBorder);

        JScrollPane inputScrollPane = new JScrollPane(inputArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        inputBox.add(Box.createRigidArea(new Dimension(10, 0)));
        inputBox.add(inputScrollPane);
        inputBox.add(Box.createRigidArea(new Dimension(10, 0)));

        JTextArea outputArea = new JTextArea("Scrambled text will appear here");
        outputArea.setForeground(Color.GRAY);
        outputArea.setFont(outputArea.getFont().deriveFont(15f));
        outputArea.setEditable(false);
        outputArea.setMinimumSize(new Dimension(100, 100));

        TitledBorder outputTitle = new TitledBorder("Scrambled Text");
        outputTitle.setTitleFont(outputTitle.getTitleFont().deriveFont(20f));
        outputTitle.setBorder(BorderFactory.createLineBorder(((LineBorder) outputTitle.getBorder()).getLineColor(), 3, true));
        CompoundBorder outputBorder = BorderFactory.createCompoundBorder(outputTitle, BorderFactory.createEmptyBorder(5, 5, 10, 5));

        Box outputBox = Box.createHorizontalBox();
        outputBox.setBorder(outputBorder);

        JScrollPane outputScrollPane = new JScrollPane(outputArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        outputBox.add(Box.createRigidArea(new Dimension(10, 0)));
        outputBox.add(outputScrollPane);
        outputBox.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton btn = new JButton("Convert to scrambled text");
        btn.setAlignmentX(Box.CENTER_ALIGNMENT);
        btn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
        btn.setPreferredSize(new Dimension(0, 50));
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                input = inputArea.getText();

                if (!inputArea.getForeground().equals(Color.GRAY) && !input.equals("")) {
                    words = input.split("(?<=[\\n\\t -])");
                    //System.out.println(Arrays.toString(words));
                    String output = "";
                    for (String word : words) {
                        output += scrambleWord(word);
                    }

                    outputArea.setForeground(Color.BLACK);
                    outputArea.setText(output);
                }
            }
        });

        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(subTitleLabel);
        panel.add(subTitleLabel2);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(inputBox);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btn);
        panel.add(outputBox);

        Box endBox = Box.createVerticalBox();
        JLabel credit = new JLabel("Created by Mackblue     ");
        credit.setAlignmentX(Box.RIGHT_ALIGNMENT);

        endBox.add(credit);
        endBox.add(Box.createRigidArea(new Dimension(0, 10)));

        contentPane.add(panel, BorderLayout.CENTER);
        contentPane.add(endBox, BorderLayout.PAGE_END);
    }

    public static String scrambleWord(String word) {
        List<Character> chars = Arrays.asList(ArrayUtils.toObject(word.toCharArray()));
        List<Character> scrambledChars = new LinkedList<>(chars);
        Map<Integer, Character> savedChars = new TreeMap<>();

        int totalAN = 0;
        int lastAN = 0;
        for (int i = chars.size() - 1; i >= 0; i--) {
            Character c = chars.get(i);
            if (Character.isLetterOrDigit(c)) {
                if (totalAN == 0) { //last AN letter
                    //System.out.println("last AN: " + i + "-" + c);
                    savedChars.put(i, c);
                }

                totalAN++;
                lastAN = i;
            } else {
                //System.out.println("AN save: " + i + "-" + c);
                savedChars.put(i, c);
            }
        }

        if (!savedChars.containsKey(lastAN)) { //first AN letter
            //System.out.println("first AN: " + lastAN + "-" + chars.get(lastAN));
            savedChars.put(lastAN, chars.get(lastAN));
        }

        Set<Integer> set = new TreeSet<>(Collections.reverseOrder());
        set.addAll(savedChars.keySet());
        for (int index : set) {
            scrambledChars.remove(index);
        }

        //System.out.println("before scramble:  " + scrambledChars);
        scrambledChars = scrambleChars(scrambledChars);
        //System.out.println("\nafter scramble:  " + scrambledChars);

        /*ArrayList<Map.Entry<Integer, Character>> list = new ArrayList<>(savedChars.entrySet());
        Collections.reverse(list);*/
        for (Map.Entry<Integer, Character> entry : /*list*/savedChars.entrySet()) {
            //System.out.println(entry.getKey() + "  :  " + entry.getValue());
            scrambledChars.add(entry.getKey(), entry.getValue());
        }

        return StringUtils.join(scrambledChars, null);
    }

    public static List<Character> scrambleChars(List<Character> chars) {
        List<Character> scrambledChars = new LinkedList<>(chars);

        if (chars.size() < 2 || chars.stream().allMatch(chars.get(0)::equals)) {
            return chars;
        }

        if (chars.size() == 2) {
            Collections.swap(scrambledChars, 0, 1);
        } else {
            Collections.shuffle(scrambledChars);
        }

        //System.out.println("end of scramble:  " + scrambledChars + "          " + scrambledChars.equals(chars));
        if (scrambledChars.equals(chars)) {
            return scrambleChars(chars);
        }
        return scrambledChars;
    }
}
