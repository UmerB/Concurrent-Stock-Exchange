import javax.sound.midi.SysexMessage;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Client implements Runnable {
    HashMap<Company, Float> shares = new HashMap<Company, Float>();
    private float balance;
    private final Lock balanceMutex = new ReentrantLock(true);
    private StockExchange stockExchange;

    /**
     * Client constructor
     *
     *
     */
    Client() {
    }

    public void Client() {
        this.balance = 0f;
    }

    public void setStockExchange(StockExchange stockExchange){
        this.stockExchange = stockExchange;
    }

    public StockExchange getStockExchange(){
        return stockExchange;
    }

    /**
     * Getter to return stocks
     *
     * @return shares
     */
    public HashMap<Company, Float> getStocks() {
        return shares;
    }

    /**
     * Sets the number of stocks for the client
     *
     * @param company
     * @param numberOfShares
     * @return true if the company exists, else returns false
     */
    public boolean setStocks(Company company, float numberOfShares) {
        try {
            shares.putIfAbsent(company, 0f);
            shares.replace(company, numberOfShares);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Allows client to buy shares.
     * Identified buy as a concurrent process so applied a mutex in order to stop race condition + deadlock.
     *
     * @param company
     * @param numberOfShares
     * @return modifyStocks
     */
    public boolean buy(Company company, float numberOfShares) {
        return ((isTransactionPossible(company, numberOfShares) && modifyStocks(company, numberOfShares)));
    }

    /**
     * The same as the buy method but the client sells instead.
     * Mutex applied here as well as sell has the possibility of causing race condition.
     *
     * @param company
     * @param numberOfShares
     * @return modifyStocks
     */

    public boolean sell(Company company, float numberOfShares) {
        return ((isTransactionPossible(company, numberOfShares) && modifyStocks(company, -numberOfShares)));
    }

    /**
     * Client can only buy if the price of the shares is below the client's limit.
     *
     * @param company
     * @param numberOfShares
     * @param limit
     * @return buy if the cost of the shares is below client's limit else return false.
     */
    public boolean buyLow(Company company, float numberOfShares, float limit) throws InterruptedException {
        isTransactionPossible(company, balance);
        if (checkCompany(company)) {
            while (company.getPrice() > limit) {
                wait();
            }
        }
        return buy(company, numberOfShares);
    }


    /**
     * Client can only sell if the price of the shares is above the client's limit.
     *
     * @param company
     * @param numberOfShares
     * @param limit
     * @return buy if the cost of the shares is above client's limit else return false.
     */
    public boolean sellHigh(Company company, float numberOfShares, float limit) throws InterruptedException {
        if (checkCompany(company)) {
            while (company.getPrice() < limit) {
                wait();
            }
        }
        return sell(company, numberOfShares);
    }

    /**
     * Deposits money into client's account.
     *
     * @param amount
     * @return true
     */
    public boolean deposit(float amount) {
        modifyBalance(amount);
        return true;
    }

    /**
     * Withdraw money from client's account
     *
     * @param amount
     * @return true if client has sufficient funds else return false.
     */
    public boolean withdraw(float amount) {
        try {
            if (amount > balance) {
                throw new Exception("Insufficient funds");
            }
            modifyBalance(amount);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * If company doesn't exist in shares it will be added.
     * Sets the number of shares of the client after the transaction.
     *
     * @param company
     * @param shareDifference
     * @return true if company has sufficient stocks to sell.
     */
    private boolean modifyStocks(Company company, float shareDifference) {
        if (company != null && company.getAvailableShares() - shareDifference < 0) {
            return false;
        }
        float shareCount;


        shares.putIfAbsent(company, 0f);

        company.getMutex().lock();
        shareCount = shares.get(company) + shareDifference;

        setStocks(company, shareCount);
        company.setAvailableShares(company.getAvailableShares() - shareDifference);


        modifyBalance(-shareDifference * company.getPrice());

        company.getMutex().unlock();
        return true;

    }

    private boolean checkCompany(Company company) {
        return shares.containsKey(company);
    }

    /**
     * Checks if transaction is possible
     *
     * @param company
     * @param numberOfShares
     * @return true if client has sufficient funds for purchase of shares else return false.
     */
    private boolean isTransactionPossible(Company company, float numberOfShares) {
        try {
            if (company.getPrice() * numberOfShares > balance) {
                throw new Exception("Insufficient funds");
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gets balance of client
     *
     * @return balance
     */
    public float getBalance() {
        return this.balance;
    }

    /**
     * Modify balance of client
     *
     * @param balanceDifference
     */
    private boolean modifyBalance(float balanceDifference) {
        balanceMutex.lock();
        float updatedBalance = balance + balanceDifference;
        if (updatedBalance < 0) {
            balanceMutex.unlock();
            return false;
        }
        balance = updatedBalance;
        balanceMutex.unlock();
        return true;
    }

    @Override
    public void run() {
        try {
            deposit(100);
            Thread.sleep(100);
            Company testCompany = new Company();
            sellHigh(testCompany, 20, 2);
            buyLow(testCompany, 5, 1);
            System.out.println(testCompany.getAvailableShares());
        } catch (InterruptedException e) {
            System.out.println("Interrupted thread");
        }

    }

}
