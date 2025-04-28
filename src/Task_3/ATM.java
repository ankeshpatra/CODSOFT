package Task_3;

public class ATM {
    private BankAccount account;

    public ATM(BankAccount account) {
        this.account = account;
    }

    public boolean deposit(double amount) {
        return account.deposit(amount);
    }

    public boolean withdraw(double amount) {
        return account.withdraw(amount);
    }

    public double checkBalance() {
        return account.getBalance();
    }
}
