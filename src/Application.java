import java.util.ArrayList;
import java.util.HashMap;

public class Application {


    public static void main(String[] args) throws InterruptedException {

        StockExchange stockExchange = new StockExchange();


        Company A = new Company();
        Company B = new Company();
        Company C = new Company();
        Company D = new Company();

        Client client1 = new Client();
        Client client2 = new Client();
        Client client3 = new Client();
        Client client4 = new Client();


        stockExchange.registerCompany(A, 300);
        stockExchange.registerCompany(B, 600);
        stockExchange.registerCompany(C, 900);
        stockExchange.registerCompany(D, 1200);


        stockExchange.addClient(client1);
        stockExchange.addClient(client2);
        stockExchange.addClient(client3);
        stockExchange.addClient(client4);

        Thread T0 = new Thread(client1);
        Thread T1 = new Thread(client2);
        Thread T2 = new Thread(client3);
        Thread T3 = new Thread(client4);

        T0.start();
        T1.start();
        T2.start();
        T3.start();

        T0.join();
        T1.join();
        T2.join();
        T3.join();




    }
}
