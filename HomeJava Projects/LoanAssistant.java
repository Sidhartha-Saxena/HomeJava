import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.Calendar;

public class LoanAssistant extends JFrame {

    private static final long serialVersionUID = 1L;

    JLabel balanceLabel = new JLabel();
    JTextField balanceTextField = new JTextField();
    JLabel interestLabel = new JLabel();
    JTextField interestTextField = new JTextField();
    JLabel monthsLabel = new JLabel();
    JTextField monthsTextField = new JTextField();
    JLabel paymentLabel = new JLabel();
    JTextField paymentTextField = new JTextField();
    JButton computeButton = new JButton();
    JButton newLoanButton = new JButton();
    JButton balanceButton = new JButton();
    JButton monthsButton = new JButton();
    JButton paymentButton = new JButton();
    JLabel analysisLabel = new JLabel();
    JTextArea analysisTextArea = new JTextArea();
    JButton exitButton = new JButton();
    Font myFont = new Font("Arial", Font.PLAIN, 16);
    Color lightYellow = new Color(255, 255, 128);
    int computePayment;

    public LoanAssistant() {

        this.setLayout(new GridBagLayout());
        this.setTitle("Loan Assistant");
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds((int) (0.4 * (screenSize.width - getWidth())), (int) (0.4 * (screenSize.height - getHeight())),
                getWidth(), getHeight());
        ImageIcon icon=new ImageIcon("HomeJava Projects/loanIcon.png");
        this.setIconImage(icon.getImage());
        GridBagConstraints gridConstraints;

        balanceLabel.setText("Loan Balance");
        balanceLabel.setFont(myFont);
        balanceLabel.setForeground(Color.BLACK);
        // change text color of label in validating balance method
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 0;
        gridConstraints.gridy = 0;
        gridConstraints.anchor = GridBagConstraints.WEST;
        gridConstraints.insets = new Insets(10, 10, 0, 0);
        this.add(balanceLabel, gridConstraints);

        balanceTextField.setPreferredSize(new Dimension(100, 25));
        balanceTextField.setHorizontalAlignment(SwingConstants.RIGHT);
        balanceTextField.setFont(myFont);
        balanceTextField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        balanceTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                balanceTextFieldActionPerformed(e);
            }
        });
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 0;
        gridConstraints.insets = new Insets(10, 10, 0, 10);
        this.add(balanceTextField, gridConstraints);

        interestLabel.setText("Interest Rate");
        interestLabel.setFont(myFont);
        interestLabel.setForeground(Color.BLACK);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 0;
        gridConstraints.gridy = 1;
        gridConstraints.anchor = GridBagConstraints.WEST;
        gridConstraints.insets = new Insets(10, 10, 0, 0);
        this.add(interestLabel, gridConstraints);

        interestTextField.setPreferredSize(new Dimension(100, 25));
        interestTextField.setHorizontalAlignment(SwingConstants.RIGHT);
        interestTextField.setFont(myFont);
        interestTextField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        interestTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                interestTextFieldActionPerformed(e);
            }
        });
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 1;
        gridConstraints.insets = new Insets(10, 10, 0, 10);
        this.add(interestTextField, gridConstraints);

        monthsLabel.setText("Number of Payments");
        monthsLabel.setFont(myFont);
        monthsLabel.setForeground(Color.BLACK);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 0;
        gridConstraints.gridy = 2;
        gridConstraints.anchor = GridBagConstraints.WEST;
        gridConstraints.insets = new Insets(10, 10, 0, 0);
        this.add(monthsLabel, gridConstraints);

        monthsTextField.setPreferredSize(new Dimension(100, 25));
        monthsTextField.setHorizontalAlignment(SwingConstants.RIGHT);
        monthsTextField.setFont(myFont);
        monthsTextField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        monthsTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                monthsTextFieldActionPerformed(e);
            }
        });
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 2;
        gridConstraints.insets = new Insets(10, 10, 0, 10);
        getContentPane().add(monthsTextField, gridConstraints);

        paymentLabel.setText("Monthly Payment");
        paymentLabel.setFont(myFont);
        paymentLabel.setForeground(Color.BLACK);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 0;
        gridConstraints.gridy = 3;
        gridConstraints.anchor = GridBagConstraints.WEST;
        gridConstraints.insets = new Insets(10, 10, 0, 0);
        this.add(paymentLabel, gridConstraints);

        paymentTextField.setPreferredSize(new Dimension(100, 25));
        paymentTextField.setHorizontalAlignment(SwingConstants.RIGHT);
        paymentTextField.setFont(myFont);
        paymentTextField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        paymentTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                paymentTextFieldActionPerformed(e);
            }
        });
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 3;
        gridConstraints.insets = new Insets(10, 10, 0, 10);
        getContentPane().add(paymentTextField, gridConstraints);

        computeButton.setText("Compute Monthly Payment");
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 0;
        gridConstraints.gridy = 4;
        gridConstraints.gridwidth = 2;
        gridConstraints.insets = new Insets(10, 0, 0, 0);
        computeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                computeButtonActionPerformed(e);
            }
        });
        this.add(computeButton, gridConstraints);

        newLoanButton.setText("New Loan Analysis");
        newLoanButton.setEnabled(false);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 0;
        gridConstraints.gridy = 5;
        gridConstraints.gridwidth = 2;
        gridConstraints.insets = new Insets(10, 0, 10, 0);
        newLoanButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                newLoanButtonActionPerformed(e);
            }
        });
        this.add(newLoanButton, gridConstraints);

        balanceButton.setText("X");
        balanceButton.setFocusable(false);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 0;
        gridConstraints.insets = new Insets(10, 0, 0, 0);
        balanceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                balanceButtonActionPerformed(e);
            }
        });
        this.add(balanceButton, gridConstraints);

        monthsButton.setText("X");
        monthsButton.setFocusable(false);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 2;
        gridConstraints.insets = new Insets(10, 0, 0, 0);
        monthsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                monthsButtonActionPerformed(e);
            }
        });
        this.add(monthsButton, gridConstraints);

        paymentButton.setText("X");
        paymentButton.setFocusable(false);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 3;
        gridConstraints.insets = new Insets(10, 0, 0, 0);
        paymentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                paymentButtonActionPerformed(e);
            }
        });
        this.add(paymentButton, gridConstraints);

        analysisLabel.setText("Loan Analysis:");
        analysisLabel.setFont(myFont);
        analysisLabel.setForeground(Color.black);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 3;
        gridConstraints.gridy = 0;
        gridConstraints.anchor = GridBagConstraints.WEST;
        gridConstraints.insets = new Insets(0, 10, 0, 0);
        this.add(analysisLabel, gridConstraints);

        analysisTextArea.setPreferredSize(new Dimension(250, 150));
        analysisTextArea.setEditable(false);
        analysisTextArea.setFont(new Font("Courier New", Font.PLAIN, 14));
        analysisTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        analysisTextArea.setBackground(Color.WHITE);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 3;
        gridConstraints.gridy = 1;
        gridConstraints.gridheight = 4;
        gridConstraints.anchor = GridBagConstraints.WEST;
        gridConstraints.insets = new Insets(0, 10, 0, 10);
        this.add(analysisTextArea, gridConstraints);

        exitButton.setText("Exit");
        exitButton.setFocusable(false);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 3;
        gridConstraints.gridy = 5;
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exitButtonActionPerformed(e);
            }
        });
        this.add(exitButton, gridConstraints);

        this.pack();
        this.setVisible(true);
        paymentButton.doClick();
    }

    private void paymentTextFieldActionPerformed(ActionEvent e) {
        paymentTextField.transferFocus();

    }

    private void balanceTextFieldActionPerformed(ActionEvent e) {
        balanceTextField.transferFocus();

    }

    private void interestTextFieldActionPerformed(ActionEvent e) {
        interestTextField.transferFocus();

    }

    private void monthsTextFieldActionPerformed(ActionEvent e) {
        monthsTextField.transferFocus();

    }

    private void exitButtonActionPerformed(ActionEvent e) {
        System.exit(0);

    }

    public boolean validateDecimalNumber(JTextField tf) {
        boolean valid, hasDecimal;
        String s = tf.getText().trim();
        valid = true;
        hasDecimal = false;
        if (s.length() == 0) {
            valid = false;
        } else {
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c >= '0' && c <= '9') {
                    continue;
                } else if (c == '.' && !hasDecimal) {
                } else {
                    valid = false;
                }
            }
        }
        tf.setText(s);
        if (!valid) {
            tf.requestFocus();
        }
        return valid;
    }

    private void balanceButtonActionPerformed(ActionEvent e) {
        computePayment =2;
        paymentButton.setVisible(true);
        monthsButton.setVisible(false);
        balanceButton.setVisible(false);
        balanceTextField.setText("");
        balanceTextField.setEditable(false);
        balanceTextField.setBackground(lightYellow);
        balanceTextField.setFocusable(false);
        monthsTextField.setEditable(true);
        monthsTextField.setBackground(Color.WHITE);
        monthsTextField.setFocusable(true);
        computeButton.setText("Compute Loan Balance");
        interestTextField.requestFocus();
        balanceLabel.setForeground(Color.BLACK);
        monthsLabel.setForeground(Color.BLACK);
        paymentLabel.setForeground(Color.BLACK);
    }

    private void monthsButtonActionPerformed(ActionEvent e) {
        computePayment = 0;
        paymentButton.setVisible(false);
        monthsButton.setVisible(false);
        balanceButton.setVisible(true);
        monthsTextField.setText("");
        monthsTextField.setEditable(false);
        monthsTextField.setBackground(lightYellow);
        monthsTextField.setFocusable(false);
        paymentTextField.setEditable(true);
        paymentTextField.setBackground(Color.WHITE);
        paymentTextField.setFocusable(true);
        computeButton.setText("Compute Number of Payments");
        balanceTextField.requestFocus();
        balanceLabel.setForeground(Color.BLACK);
        monthsLabel.setForeground(Color.BLACK);
        paymentLabel.setForeground(Color.BLACK);
    }

    private void paymentButtonActionPerformed(ActionEvent e) {
        computePayment = 1;
        monthsButton.setVisible(true);
        balanceButton.setVisible(false);
        paymentButton.setVisible(false);
        paymentTextField.setText("");
        paymentTextField.setBackground(lightYellow);
        paymentTextField.setEditable(false);
        paymentTextField.setFocusable(false);
        monthsTextField.setEditable(true);
        monthsTextField.setBackground(Color.WHITE);
        monthsTextField.setFocusable(true);
        balanceTextField.setEditable(true);
        balanceTextField.setBackground(Color.WHITE);
        balanceTextField.setFocusable(true);
        computeButton.setText("Compute Monthly Payment");
        balanceTextField.requestFocus();
        balanceLabel.setForeground(Color.BLACK);
        monthsLabel.setForeground(Color.BLACK);
        paymentLabel.setForeground(Color.BLACK);
    }

    private void computeButtonActionPerformed(ActionEvent e) {
        double balance, payment, interest, monthlyInterest, multiplier;
        int months;
        double loanBalance, finalPayment;
        if (validateDecimalNumber(interestTextField)) {
            interestLabel.setForeground(Color.BLACK);
            interest = Double.valueOf(interestTextField.getText()).doubleValue();
        } else {
            JOptionPane.showConfirmDialog(null, "Invalid or empty Interest Rate.\nPlease correct.","InterestInput Error", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
            interestTextField.setText("");
            interestLabel.setForeground(Color.RED);
            return;
        }
        monthlyInterest = interest / 1200;

        if (computePayment==1) {

            if (validateDecimalNumber(balanceTextField)&& Double.valueOf(balanceTextField.getText()).doubleValue()!=0) {
                balance = Double.valueOf(balanceTextField.getText()).doubleValue();
                balanceLabel.setForeground(Color.BLACK);
            } else {
                JOptionPane.showConfirmDialog(null, "Invalid or empty Loan Balance entry.\nPlease correct.","Balance Input Error", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
                balanceTextField.setText("");
                balanceLabel.setForeground(Color.RED);
                return;
            }
            if (validateDecimalNumber(monthsTextField)&& Double.valueOf(monthsTextField.getText()).doubleValue()!=0) {
                months = Integer.valueOf(monthsTextField.getText()).intValue();
                monthsLabel.setForeground(Color.BLACK);
            } else {
                JOptionPane.showConfirmDialog(null, "Invalid or empty Number of Payments entry.\nPlease correct.","Number of Payments Input Error", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
                monthsTextField.setText("");
                monthsLabel.setForeground(Color.RED);
                return;
            }

            if (interest == 0) {
                payment = balance / months;
            } else {
                multiplier = Math.pow(1 + monthlyInterest, months);
                payment = balance * monthlyInterest * multiplier / (multiplier - 1);
            }

            paymentTextField.setText(new DecimalFormat("0.00").format(payment));
        }

        else if(computePayment==0){
            if (validateDecimalNumber(balanceTextField)&& Double.valueOf(balanceTextField.getText()).doubleValue()!=0) {
                balance = Double.valueOf(balanceTextField.getText()).doubleValue();
                balanceLabel.setForeground(Color.BLACK);
            } else {
                JOptionPane.showConfirmDialog(null, "Invalid or empty Loan Balance entry.\nPlease correct.","Balance Input Error", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
                balanceTextField.setText("");
                balanceLabel.setForeground(Color.RED);
                return;
            }
            if (validateDecimalNumber(paymentTextField)) {
                payment = Double.valueOf(paymentTextField.getText()).doubleValue();
                paymentLabel.setForeground(Color.BLACK);
                if (payment <= balance * monthlyInterest + 1.0) {
                    if (JOptionPane.showConfirmDialog(null,"Minimum payment must be $"+ new DecimalFormat("0.00").format((int) (balance * monthlyInterest + 1.0)) + "\n"+ "Do you want to use the minimum payment?","Input Error", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                        paymentTextField
                                .setText(new DecimalFormat("0.00").format((int) (balance * monthlyInterest + 1.0)));
                        payment = Double.valueOf(paymentTextField.getText()).doubleValue();
                    } else {
                        paymentTextField.requestFocus();
                        return;
                    }
                }
            } else {
                JOptionPane.showConfirmDialog(null, "Invalid or empty Monthly Payment entry.\nPlease correct.","Monthly Payment Input Error", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
                paymentTextField.setText("");
                paymentLabel.setForeground(Color.RED);
                return;
            }
            if (interest == 0) {
                months = (int) (balance / payment);
            } else {
                months = (int) ((Math.log(payment) - Math.log(payment - balance * monthlyInterest))
                        / Math.log(1 + monthlyInterest));
            }

            monthsTextField.setText(String.valueOf(months));
        }
        else{
            if (validateDecimalNumber(monthsTextField)&& Double.valueOf(monthsTextField.getText()).doubleValue()!=0) {
                months = Integer.valueOf(monthsTextField.getText()).intValue();
                monthsLabel.setForeground(Color.BLACK);
            } else {
                JOptionPane.showConfirmDialog(null, "Invalid or empty Number of Payments entry.\nPlease correct.","Number of Payments Input Error", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
                monthsTextField.setText("");
                monthsLabel.setForeground(Color.RED);
                return;
            }
            if (validateDecimalNumber(paymentTextField)&& Double.valueOf(paymentTextField.getText()).doubleValue()!=0) {
                payment = Double.valueOf(paymentTextField.getText()).doubleValue();
                paymentLabel.setForeground(Color.BLACK);
            } else {
                JOptionPane.showConfirmDialog(null, "Invalid or empty Monthly Payment entry.\nPlease correct.","Monthly Payment Input Error", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
                paymentTextField.setText("");
                paymentLabel.setForeground(Color.RED);
                return;
            }
            if (interest == 0) {
                balance =payment* months;
            } else{
                multiplier = Math.pow(1 + monthlyInterest, months);
                balance = (payment *(multiplier - 1))/(monthlyInterest  * multiplier );
            }
            balanceTextField.setText(new DecimalFormat("0.00").format(balance));
        }

        payment = Double.valueOf(paymentTextField.getText()).doubleValue();
        analysisTextArea.setText("Loan Balance: $" + new DecimalFormat("0.00").format(balance));
        analysisTextArea.append("\n" + "Interest Rate: " + new DecimalFormat("0.00").format(interest) + "%");

        loanBalance = balance;
        for (int paymentNumber = 1; paymentNumber <= months - 1; paymentNumber++) {
            loanBalance += loanBalance * monthlyInterest - payment;
        }
        finalPayment = loanBalance;
        if (finalPayment > payment) {
            loanBalance += loanBalance * monthlyInterest - payment;
            finalPayment = loanBalance;
            months++;
            monthsTextField.setText(String.valueOf(months));
        }
        analysisTextArea.append(
                "\n\n" + String.valueOf(months - 1) + " Payment of: $" + new DecimalFormat("0.00").format(payment));
        analysisTextArea.append("\n" + "Final Payment of: $" + new DecimalFormat("0.00").format(finalPayment));
        analysisTextArea.append(
                "\n" + "Total Payments: $" + new DecimalFormat("0.00").format((months - 1) * payment + finalPayment));
        analysisTextArea.append("\n" + "Interest Paid $"
                + new DecimalFormat("0.00").format((months - 1) * payment + finalPayment - balance));
        DateFormat df=new SimpleDateFormat("yyyy/MMM");
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.MONTH, months);
        analysisTextArea.append("\n" + "Loan Paid off by:"+ df.format(cal.getTime()));
        computeButton.setEnabled(false);
        newLoanButton.setEnabled(true);
        newLoanButton.requestFocus();
    }

    private void newLoanButtonActionPerformed(ActionEvent e) {
        newLoanButton.setEnabled(false);
        if (computePayment==1) {
            paymentTextField.setText("");
        } else if(computePayment==0){
            monthsTextField.setText("");
        }
        else{
            balanceTextField.setText("");
        }
        analysisTextArea.setText("");
        computeButton.setEnabled(true);
        balanceTextField.requestFocus();

    }

    public static void main(String[] args) {
        new LoanAssistant();
    }

}
