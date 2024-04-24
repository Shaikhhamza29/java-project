import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Date;

class Expenses extends JFrame {

    String url, sqlstat;
    Statement smt;
    PreparedStatement ps;
    ResultSet rset;
    Connection conn;
    String line = "**************************************";

    JDBC obj = new JDBC();

    public DefaultListModel<String> expenseListModel;
    public JList<String> expenseList;

    public Expenses() {
        JFrame f = new JFrame();
        // f.setVisible(true);
        f.setSize(700, 500);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(EXIT_ON_CLOSE);
        // f.setLayout(new FlowLayout());
        JPanel buttonJPanel = new JPanel();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        // panel.setLayout();

        expenseListModel = new DefaultListModel<>();
        expenseList = new JList<>(expenseListModel);
        JScrollPane scrollPane = new JScrollPane(expenseList);
        expenseList.setBounds(0, 0, 700, 400);

        JButton addButton = new JButton("Add Expense");
        JButton displayButton = new JButton("Display Expenses");
        JButton delete = new JButton("Delete Expenses");

        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteExpenses();
            }
        });

        displayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayexpenses();
            }
        });

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addexpenses();
            }
        });

        panel.add(scrollPane);
        panel.add(buttonJPanel);

        buttonJPanel.add(addButton);
        buttonJPanel.add(displayButton);
        buttonJPanel.add(delete);
        f.getContentPane().add(panel);
        f.setVisible(true);
    }

    private void addexpenses() {
        expenseListModel.clear();
        final JDialog addExpenseDialog = new JDialog(this, "Add Expense", true);
        addExpenseDialog.setSize(300, 200);
        addExpenseDialog.setLayout(new GridLayout(4, 2));

        JLabel descriptionLabel = new JLabel("Description:");
        final JTextField descriptionField = new JTextField();
        addExpenseDialog.add(descriptionLabel);
        addExpenseDialog.add(descriptionField);

        JLabel amountLabel = new JLabel("Amount:");
        final JTextField amountField = new JTextField();
        addExpenseDialog.add(amountLabel);
        addExpenseDialog.add(amountField);
        // addExpensesDialog.setRelativeLayoutTo(null);

        JButton addButton = new JButton("Add");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String description = descriptionField.getText();
                String amount = amountField.getText();
                
        Date date = new Date();
        long l = date.getTime();
        java.sql.Date sdate = new java.sql.Date(l);
        try {
            url = "jdbc:mysql://localhost/firstDB";
            conn = DriverManager.getConnection(url, "root", "root");
            sqlstat = "insert into expenses values(?,?,?)";
            ps = conn.prepareStatement(sqlstat);
            // ps = smt.execute(sqlstat);
            // System.out.println(b);
            ps.setString(1, sdate.toString());
            ps.setString(2, description);
            ps.setString(3, amount);
            ps.executeUpdate();

            smt.close();
            conn.close();

        }

        catch (Exception Ec) {
            System.out.println(Ec);
        }

                if (!description.isEmpty() && !amount.isEmpty()) {
                    String expenseEntry = date + "  " + description + "  $ " + amount;

                    expenseListModel.addElement(expenseEntry);
                    expenseListModel.addElement(line);
                    addExpenseDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(addExpenseDialog, "Please fill in all fields.");
                }
            }
        });

        addExpenseDialog.add(addButton);
        addExpenseDialog.setVisible(true);
    }

    private void displayexpenses() {
        expenseListModel.clear();
        final JDialog addExpenseDialog = new JDialog(this, "DIsplay expenses", true);
        addExpenseDialog.setSize(300, 200);
        addExpenseDialog.setLayout(new GridLayout(4, 2));

        JLabel startdate = new JLabel("START-DATE:");
        final JTextField starTextField = new JTextField();
        addExpenseDialog.add(startdate);
        addExpenseDialog.add(starTextField);

        JLabel end_date = new JLabel("END-DATE:");
        final JTextField end_date_Field = new JTextField();
        addExpenseDialog.add(end_date);
        addExpenseDialog.add(end_date_Field);

        JButton addButton = new JButton("ADD");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String start_date = starTextField.getText();
                String end_date = end_date_Field.getText();

                if (!start_date.isEmpty() && !end_date.isEmpty()) {
                    try {
                        int total = 0;
                        url = "jdbc:mysql://localhost:3306/firstDB";
                        conn = DriverManager.getConnection(url, "root", "root");

                        sqlstat = "Select *from expenses where date between ? and ?";

                        ps = conn.prepareStatement(sqlstat);
                        // ps = smt.execute(sqlstat);
                        // System.out.println(b);
                        ps.setString(1, start_date);
                        ps.setString(2, end_date);

                        rset = ps.executeQuery();
                        while (rset.next()) {
                            java.sql.Date date;
                            int smarks;

                            date = rset.getDate(1);
                            String strname = rset.getString(2);
                            smarks = rset.getInt(3);
                            // System.out.println(date + "\t" + strname + "\t" + smarks);
                            expenseListModel.addElement(date + "    " + strname + "    " + smarks);
                            total = total + smarks;
                            addExpenseDialog.dispose();

                        }
                        expenseListModel.addElement(line);
                        expenseListModel.addElement("TOTAL                 " + Integer.toString(total));

                        addExpenseDialog.dispose();
                        System.out.println("\nDATA RETRIVE SUCCESSFULLY \n");
                        smt.close();
                        conn.close();
                    } catch (Exception Ec) {
                        System.out.println();
                    }

                } else if (!start_date.isEmpty()) {

                    try {
                        int total = 0;
                        url = "jdbc:mysql://localhost:3306/firstDB";
                        conn = DriverManager.getConnection(url, "root", "root");

                        sqlstat = "Select *from expenses where date between ? and now()";

                        ps = conn.prepareStatement(sqlstat);
                        // ps = smt.execute(sqlstat);
                        // System.out.println(b);
                        ps.setString(1, start_date);
                        // ps.setString(2, end_date);

                        rset = ps.executeQuery();
                        while (rset.next()) {
                            java.sql.Date date;
                            int smarks;

                            date = rset.getDate(1);
                            String strname = rset.getString(2);
                            smarks = rset.getInt(3);
                            // System.out.println(date + "\t" + strname + "\t" + smarks);
                            total = total + smarks;
                            expenseListModel.addElement(date + "    " + strname + "    " + smarks);
                            addExpenseDialog.dispose();

                        }
                        expenseListModel.addElement(line);
                        expenseListModel.addElement("TOTAL                 " + Integer.toString(total));

                        addExpenseDialog.dispose();

                        System.out.println("\nDATA RETRIVE SUCCESSFULLY \n");
                        smt.close();
                        conn.close();
                    } catch (Exception Ec) {
                        System.out.println();
                    }

                } else {
                    JOptionPane.showMessageDialog(addExpenseDialog, "Please fill in all fields.");
                }
            }
        });

        addExpenseDialog.add(addButton);
        addExpenseDialog.setVisible(true);
    }

    public void deleteExpenses() {
        expenseListModel.clear();
        final JDialog addExpenseDialog = new JDialog(this, "Delete Expenses", true);
        addExpenseDialog.setSize(300, 200);
        addExpenseDialog.setLayout(new GridLayout(4, 2));

        JLabel descriptionLabel = new JLabel("Description:");
        final JTextField descriptionField = new JTextField();
        addExpenseDialog.add(descriptionLabel);
        addExpenseDialog.add(descriptionField);

        JButton delButton = new JButton("Delete");

        delButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = descriptionField.getText();

                try {
                    url = "jdbc:mysql://localhost/firstDB";
                    conn = DriverManager.getConnection(url, "root", "root");
                    sqlstat = "delete from expenses where p_name=?";
                    ps = conn.prepareStatement(sqlstat);
                    // ps = smt.execute(sqlstat);
                    // System.out.println(b);
                    ps.setString(1, name);

                    System.out.println(name);
                    ps.executeUpdate();
                    System.out.println("DELETED");
                    addExpenseDialog.dispose();

                    smt.close();
                    conn.close();

                }

                catch (Exception Ec) {
                    System.out.println(Ec);
                }

            }
        });

        addExpenseDialog.add(delButton);

        addExpenseDialog.setVisible(true);

    }
}