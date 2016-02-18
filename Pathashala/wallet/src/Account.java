public class Account {
    private double balance;
    private Notifier notifier;
    private String email;
    private String auditorEmail;
    Account(double balance, Notifier notifier, String customerEmail, String auditorEmail) {
        this.balance = balance;
        this.notifier = notifier;
        this.email = customerEmail;
        this.auditorEmail=auditorEmail;
    }

    public void putMoney(double moneyToBeAdded) throws NegativeMoneyInputException {
        if (moneyToBeAdded <= 0.0)
            throw new NegativeMoneyInputException("Unpported Input");
        balance = balance + moneyToBeAdded;
    }


    public double getBalance() {
        return balance;
    }

    public double takeMoney(double moneyToBeWithdrawn) throws NegativeMoneyInputException ,NotificationFailedException{
        if (moneyToBeWithdrawn <= 0.0)
            throw new NegativeMoneyInputException("Unsupportd Input");
        this.balance -= moneyToBeWithdrawn;
        if(balance<0)
        {
            if(!notifier.notifyViaEmail(this.email, this.balance))
                throw new NotificationFailedException("Owner Did not get notified");

            if(!notifier.notifyViaEmail(this.auditorEmail, this.balance))
                throw new NotificationFailedException("Auditor Did not get notified");


        }
        return moneyToBeWithdrawn;
    }
}
