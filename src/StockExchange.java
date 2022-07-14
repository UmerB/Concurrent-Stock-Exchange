import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class StockExchange {
    private HashMap<Company, Float> companies = new HashMap<Company, Float>();
    private ArrayList<Client> clients = new ArrayList<>();
    Company company;

    /**
     * Register a company with its number of shares
     *
     * @param company
     * @param numberOfShares
     * @return
     */
    public boolean registerCompany(Company company, float numberOfShares) {
        if (numberOfShares <= 0) {
            return false;
        } else {
            companies.put(company, numberOfShares);
            return true;
        }
    }

    /**
     * Derigsters a company
     *
     * @param company
     * @return
     */
    public boolean deregisterCompany(Company company) {
        try {
            if (!companies.containsValue(company)) {
                throw new Exception("Company doesn't exist");
            }
            companies.remove(company);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Adds a client to the stock exchange
     *
     * @param client
     * @return
     */
    public boolean addClient(Client client) {
        if (clients.contains(client)) {
            System.out.println("Client already exists");
            return false;
        } else {
            clients.add(client);
            client.setStockExchange(this);
            return true;
        }
    }


    /**
     * Removes a client from the stock exchange
     *
     * @param client
     * @return
     */
    public boolean removeClient(Client client) {
        if (client != null && clients.contains(client)) {
            clients.remove(client);
            return true;
        } else {
            System.out.println("Client doesnt exist");
            return false;
        }
    }


        /**
         * Returns all clients in the stock exchange
         *
         * @return
         */
        public ArrayList<Client> getClients () {
            return clients;
        }

        /**
         * Returns all companies in the stock exchange
         *
         * @return
         */
        public HashMap<Company, Float> getCompanies () {
            return companies;
        }

        /**
         * Sets a price for shares for a company
         *
         * @param company
         * @param price
         */
        public void setPrice (Company company,float price){
            company.setPrice(price);
        }

        /**
         * Changes the price of shares for a company.
         *
         * @param company
         * @param priceDifference
         */
        public void changePriceBy (Company company,float priceDifference){
            setPrice(company,company.getPrice() + priceDifference);
        }
    }

