import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

public class AccountTest {

    private Account account;
    Notifier emailHandler;

    @Rule
    public ExpectedException expectedException=ExpectedException.none();


    @Before
    public void setup() {
        account = new Account(0, null, "customer@cust.com", "auditor@audit.com");
        emailHandler = mock(Notifier.class);
    }

    @Test
    public void OwnerCanPut$10ToZeroBalanceWallet() throws NegativeMoneyInputException {
        account.putMoney(10);
        assertEquals(10.0, account.getBalance());
    }

    @Test
    public void OwnerCannotPut$minus_10ToZeroBalanceWallet() throws NegativeMoneyInputException {
        thrown.expect(NegativeMoneyInputException.class);
        account.putMoney(-10);
    }

    @Test
    public void OwnerCanPut$10To$TenBalanceWallet() throws NegativeMoneyInputException {
        Account account = new Account(10, emailHandler, "customer@cust.com", "auditor@audit.com");
        account.putMoney(10);
        assertEquals(20.0, account.getBalance());
    }

    @Test
    public void OwnerCanWithdraw$10FromAccountWith$0() throws NegativeMoneyInputException, NotificationFailedException {
        Notifier mockedNotifier = Mockito.mock(Notifier.class);
        when(mockedNotifier.notifyViaEmail("customer@cust.com", -10.00)).thenReturn(true);
        when(mockedNotifier.notifyViaEmail("auditor@audit.com", -10.00)).thenReturn(true);
        account = new Account(0, mockedNotifier, "customer@cust.com", "auditor@audit.com");
        assertEquals(10.0, account.takeMoney(10));
        verify(mockedNotifier).notifyViaEmail("customer@cust.com", -10.00);
        assertEquals(-10.0, account.getBalance());

        verify(mockedNotifier).notifyViaEmail("auditor@audit.com", -10.00);
        assertEquals(-10.0, account.getBalance());
    }


    @Test
    public void OwnerCanWithdraw$100FromWalletWith$50() throws NegativeMoneyInputException, NotificationFailedException {
        Notifier mockedNotifier = Mockito.mock(Notifier.class);
        Account account = new Account(50, mockedNotifier, "customer@cust.com", "auditor@audit.com");
        when(mockedNotifier.notifyViaEmail("customer@cust.com", -50.00)).thenReturn(true);
        when(mockedNotifier.notifyViaEmail("auditor@audit.com", -50.00)).thenReturn(true);
        assertEquals(100.0, account.takeMoney(100));
        assertEquals(-50.0, account.getBalance());
        verify(mockedNotifier).notifyViaEmail("customer@cust.com", -50.00);
        verify(mockedNotifier).notifyViaEmail("auditor@audit.com", -50.00);


    }

    @Test
    public void OwnerCanWithdraw$50FromAccountWith$100() throws NegativeMoneyInputException, NotificationFailedException {
        Account account = new Account(100, emailHandler, "customer@cust.com", "auditor@audit.com");
        assertEquals(50.0, account.takeMoney(50));
        assertEquals(50.0, account.getBalance());
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void OwnerCannotTake$0FromZeroBalanceAccount() throws NegativeMoneyInputException, NotificationFailedException {

        thrown.expect(NegativeMoneyInputException.class);

        account.takeMoney(0);
        assertEquals(0.0, account.getBalance());


    }

    @Test
    public void OwnerCannotTake$minus_10FromZeroBalanceAccount() throws NegativeMoneyInputException, NotificationFailedException {
        thrown.expect(NegativeMoneyInputException.class);

        account.takeMoney(-10);
    }

    @Test
    public void OwnerCanWithdraw$50and$20FromAccountWith$100() throws NegativeMoneyInputException, NotificationFailedException {
        Account account = new Account(100, emailHandler, "customer@cust.com", "auditor@audit.com");
        assertEquals(50.0, account.takeMoney(50));
        assertEquals(50.0, account.getBalance());

        assertEquals(20.0, account.takeMoney(20));
        assertEquals(30.0, account.getBalance());


    }
    @Test
    public void OwnerCanOverWithdrawButOwnerDidntGetNotified() throws NegativeMoneyInputException, NotificationFailedException {
        expectedException.expect(NotificationFailedException.class);
        expectedException.expectMessage("Owner Did not get notified");
        Notifier mockedNotifier = Mockito.mock(Notifier.class);
        Account account = new Account(50, mockedNotifier, "customer@cust.com", "auditor@audit.com");
        when(mockedNotifier.notifyViaEmail("customer@cust.com", -50.00)).thenReturn(false);
        when(mockedNotifier.notifyViaEmail("auditor@audit.com", -50.00)).thenReturn(true);
        assertEquals(100.0, account.takeMoney(100));
        assertEquals(-50.0, account.getBalance());
        verify(mockedNotifier).notifyViaEmail("customer@cust.com", -50.00);


        verify(mockedNotifier).notifyViaEmail("auditor@audit.com", -50.00);


    }

    @Test
    public void OwnerCanOverWithdrawButAuditorDidntGetNotified() throws NegativeMoneyInputException, NotificationFailedException {
        expectedException.expect(NotificationFailedException.class);
        expectedException.expectMessage("Auditor Did not get notified");

        Notifier mockedNotifier = Mockito.mock(Notifier.class);
        Account account = new Account(50, mockedNotifier, "customer@cust.com", "auditor@audit.com");
        when(mockedNotifier.notifyViaEmail("customer@cust.com", -50.00)).thenReturn(true);
        when(mockedNotifier.notifyViaEmail("auditor@audit.com", -50.00)).thenReturn(false);
        assertEquals(100.0, account.takeMoney(100));
        assertEquals(-50.0, account.getBalance());
        verify(mockedNotifier).notifyViaEmail("customer@cust.com", -50.00);
        verify(mockedNotifier).notifyViaEmail("auditor@audit.com", -50.00);


    }


    @Test
    public void OwnerCanOverWithdrawMultipleTimesGetNotifiedMultipleTimes() throws NegativeMoneyInputException, NotificationFailedException {

        Notifier mockedNotifier = Mockito.mock(Notifier.class);
        Account account = new Account(50, mockedNotifier, "customer@cust.com", "auditor@audit.com");
        when(mockedNotifier.notifyViaEmail("customer@cust.com", -50.00)).thenReturn(true).thenReturn(false);
        when(mockedNotifier.notifyViaEmail("auditor@audit.com", -50.00)).thenReturn(true);

        when(mockedNotifier.notifyViaEmail("customer@cust.com", -150.00)).thenReturn(true);
        when(mockedNotifier.notifyViaEmail("auditor@audit.com", -150.00)).thenReturn(true);

        assertEquals(100.0, account.takeMoney(100));
        assertEquals(100.0, account.takeMoney(100));
        assertEquals(-150.0, account.getBalance());
        verify(mockedNotifier).notifyViaEmail("customer@cust.com", -50.00);
        verify(mockedNotifier).notifyViaEmail("auditor@audit.com", -50.00);
        verify(mockedNotifier).notifyViaEmail("customer@cust.com", -150.00);
        verify(mockedNotifier).notifyViaEmail("auditor@audit.com", -150.00);



    }


}


