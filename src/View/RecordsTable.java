package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RecordsTable extends JFrame {
    public RecordsTable() {
        super("Model.Records");

        setPreferredSize(new Dimension(400, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel content = new JPanel(new BorderLayout());


        JButton exit = new JButton("BACK");
        exit.setPreferredSize(new Dimension(70,30));
        exit.setBackground(Color.WHITE);

        content.setBackground(new Color(54, 136, 191));
        content.add(exit, BorderLayout.PAGE_END);

        setContentPane(content);
        setSize(400, 500);
        setLocationRelativeTo(null);
        pack();

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
    }


}
