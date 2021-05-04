package bank.account.transfer;
//banka.bank_account
//VARCHAR(25) -> String

import bank.account.transfer.layout.BankWindow;
import javax.swing.SwingUtilities;



public class BankApplication {

    
    public static void main(String[] args) {
        BankWindow bankWindow = new BankWindow();
        SwingUtilities.invokeLater(bankWindow::showWindow);
    }
}
