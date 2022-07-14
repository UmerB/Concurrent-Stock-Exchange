import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Company {
    private String name;
    private float totalNumberOfShares;
    private float availableNumberOfShares;
    private float price;
    private StockExchange stockExchange;
    private final Lock mutex = new ReentrantLock(true);


    /**
     * Creates constructor
     *
     */
    Company() {

    }

    /**
     * Return object
     *
     * @return
     */
    public Company Company() {
        return this;
    }

    /**
     * Gets mutex object on company
     * @return
     */
    public Lock getMutex() {
        return mutex;
    }

    /**
     * Gets name of company
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets company name
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets total number of shares a company has
     *
     * @return shares
     */
    public float getTotalShares() {
        return totalNumberOfShares;
    }

    /**
     * Sets total number of shares a company has
     *
     * @param totalNumberOfShares
     */
    public void setTotalShares(float totalNumberOfShares) {
        this.totalNumberOfShares = totalNumberOfShares;
    }

    /**
     * Gets available number of shares a company has
     *
     * @return availableShares
     */
    public float getAvailableShares() {
        return availableNumberOfShares;
    }

    /**
     * Sets amount of available shares a company has
     *
     * @param availableNumberOfShares
     */
    public synchronized void setAvailableShares(float availableNumberOfShares) {
        this.availableNumberOfShares = availableNumberOfShares;
        notifyAll();
    }

    /**
     * Get price of company share
     *
     * @return price of share
     */
    public float getPrice() {
        return price;
    }

    /**
     * Sets price of company share
     *
     * @param price
     */
    public synchronized void setPrice(float price) {
        this.price = price;
        notifyAll();
    }
}

