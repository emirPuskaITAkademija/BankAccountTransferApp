package bank.account.transfer.layout;

import bank.account.transfer.entity.BankAccount;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BankWindow extends JFrame {

    private JLabel fromAccountLabel = new JLabel("From : ");
    private JLabel toAccountLabel = new JLabel("To : ");
    private JLabel amountLabel = new JLabel("Amount : ");
    private JComboBox<BankAccount> fromAccountComboBox = new JComboBox<>();
    private JComboBox<BankAccount> toAccountComboBox = new JComboBox<>();
    private JTextField amountTextField = new JTextField(10);
    private JButton executeButton = new JButton("Execute");

    public BankWindow() {
        super("Bank transfer prozor");
        setSize(400, 250);
        setLayout(new GridLayout(4, 1));
        JPanel fromPanel = new JPanel();
        BankAccount.loadAll().forEach(fromAccountComboBox::addItem);
        fromPanel.add(fromAccountLabel);
        fromPanel.add(fromAccountComboBox);
        JPanel toPanel = new JPanel();
        BankAccount.loadAll().forEach(toAccountComboBox::addItem);
        toPanel.add(toAccountLabel);
        toPanel.add(toAccountComboBox);
        JPanel amountPanel = new JPanel();
        amountPanel.add(amountLabel);
        amountPanel.add(amountTextField);
        JPanel executePanel = new JPanel();
        executePanel.add(executeButton);
        executeButton.addActionListener(this::executeTransfer);
        add(fromPanel);
        add(toPanel);
        add(amountPanel);
        add(executePanel);
    }

    private void executeTransfer(ActionEvent e) {
        BankAccount fromAccount = (BankAccount) fromAccountComboBox.getSelectedItem();
        System.out.println("FROM: " + fromAccount);
        BankAccount toAccount = (BankAccount) toAccountComboBox.getSelectedItem();
        BigDecimal amount = new BigDecimal(amountTextField.getText());
        //Ažuriram bazu odnosno tabelu u bazi
        BankAccount.transfer(fromAccount, toAccount, amount);

        //Ažuriram UI odnosno prozor
        fromAccountComboBox.removeAllItems();
        BankAccount.loadAll().forEach(fromAccountComboBox::addItem);

        toAccountComboBox.removeAllItems();
        BankAccount.loadAll().forEach(toAccountComboBox::addItem);
        System.out.println("FROM: " + fromAccount);
        fromAccountComboBox.setSelectedItem(fromAccount);
        toAccountComboBox.setSelectedItem(toAccount);
        amountTextField.setText("");
    }

    public void showWindow() {
        pack();
        setVisible(true);
    }

}
