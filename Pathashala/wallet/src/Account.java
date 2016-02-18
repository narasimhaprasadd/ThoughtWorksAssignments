public class Account {
    private double balance;
    private Notifier notifier;
    private String email;

    Account(double balance, Notifier notifier, String email) {
        this.balance = balance;
        this.notifier = notifier;
        this.email = email;
    }

    public void putMoney(double moneyToBeAdded) throws NegativeMoneyInputException {
        if (moneyToBeAdded <= 0.0)
            throw new NegativeMoneyInputException("Unpported Input");
        balance = balance + moneyToBeAdded;
    }


    public double getBalance() {
        return balance;
    }

    public double takeMoney(double moneyToBeWithdrawn) throws NegativeMoneyInputException {
        if (moneyToBeWithdrawn <= 0.0)
            throw new NegativeMoneyInputException("Unsupportd Input");
        this.balance -= moneyToBeWithdrawn;
        if(balance<0)
        {
            notifier.notifyViaEmail(this.email,this.balance);

        }
        return moneyToBeWithdrawn;
    }
}
