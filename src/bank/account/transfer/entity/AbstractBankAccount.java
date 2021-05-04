package bank.account.transfer.entity;

import bank.account.transfer.entity.util.HibernateUtil;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public abstract class AbstractBankAccount {

    public BankAccount getThis() {
        return (BankAccount) this;
    }

    public void save() {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(this);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void update() {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.update(this);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void get() {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.get(BankAccount.class, getThis().getAccountNumber());
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void delete() {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.delete(this);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public static List<BankAccount> loadAll() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            //select *from bank_account
            Query query = session.createQuery("from BankAccount");
            session.getTransaction().commit();
            List<BankAccount> bankAccounts = query.list();
            return bankAccounts;
        } catch (HibernateException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Za proslijeđeni broj računa vraća stanje računa
     * <p>
     * @param accountNumber
     * @return amount
     */
    public static BigDecimal showBankAccountState(String accountNumber) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Object obj = session.get(BankAccount.class, accountNumber);
            if (obj == null) {
                return null;
            }
            BankAccount bankAccount = (BankAccount) obj;
            session.getTransaction().commit();
            return bankAccount.getAmount();
        } catch (HibernateException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Za proslijeđene računa ova funkcija izvršava transakciju prenosa
     * sredstava u definiranom iznosu
     * <p>
     * @param fromAccount
     * @param toAccount
     * @param amount
     * @return status
     */
    public static boolean transfer(BankAccount fromAccount, BankAccount toAccount, BigDecimal amount) {
        if (fromAccount == null || fromAccount.getAccountNumber() == null || fromAccount.getAccountNumber().isEmpty()) {
            return false;
        }
        if (toAccount == null || toAccount.getAccountNumber() == null || toAccount.getAccountNumber().isEmpty()) {
            return false;
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        if(fromAccount.getAccountNumber().equals(toAccount.getAccountNumber())){
            return false;
        }
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            fromAccount.setAmount(fromAccount.getAmount().subtract(amount));
            toAccount.setAmount(toAccount.getAmount().add(amount));
            session.update(fromAccount);
            session.update(toAccount);
            transaction.commit();
            return true;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println(e.getMessage());
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
