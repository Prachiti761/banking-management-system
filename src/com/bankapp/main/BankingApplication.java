package com.bankapp.main;

import com.bankapp.exception.BankException;
import com.bankapp.model.Account;
import com.bankapp.model.Customer;
import com.bankapp.model.Transation;
import com.bankapp.model.Transation;
import com.bankapp.service.BankingService;
import com.bankapp.service.BankingServiceImpl;

import java.util.Iterator;
import java.util.List; 
import java.util.Map;
import java.util.Scanner;

public class BankingApplication {

    private static final BankingService service = new BankingServiceImpl();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        try {
            service.ensureDefaultAdmin();
        } catch (BankException e) {
            System.out.println("Startup failed: " + e.getMessage());
            System.out.println("Check db.properties and that sql/schema.sql has been executed.");
            return;
        }

        service.printWelcomeMessage();

        boolean running = true;

        while (running) {
            try {
                if (service.getCurrentUser() == null)
                    running = mainMenu();
                else if ("ADMIN".equals(service.getCurrentUser().getRole()))
                    adminMenu();
                else
                    customerMenu();

            } catch (BankException e) {
                System.out.println("\n[Error] " + e.getMessage());
            }
        }

        System.out.println("Thank you for using ABC Bank.");
    }

    // ---------------- MENUS ----------------

    private static boolean mainMenu() {

        header("ABC BANKING SYSTEM");

        System.out.println("1. Customer Registration");
        System.out.println("2. Customer Login");
        System.out.println("3. Admin Login");
        System.out.println("4. Exit");

        System.out.print("Enter choice: ");
        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {

            case 1:
                register();
                break;

            case 2:
                login(false);
                break;

            case 3:
                login(true);
                break;

            case 4:
                return false;

            default:
                System.out.println("Invalid choice.");
        }

        return true;
    }

    private static void customerMenu() {

        header("CUSTOMER MENU - " + service.getCurrentUser().getName());

        System.out.println("1. Create Bank Account");
        System.out.println("2. Deposit Money");
        System.out.println("3. Withdraw Money");
        System.out.println("4. Transfer Money");
        System.out.println("5. Check Balance");
        System.out.println("6. Transaction History");
        System.out.println("7. View Account Details");
        System.out.println("8. Logout");

        System.out.print("Enter choice: ");
        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {

            case 1:
                createAccount();
                break;

            case 2:
                System.out.print("Account Number : ");
                long depositAccount = sc.nextLong();

                System.out.print("Amount         : ");
                double depositAmount = sc.nextDouble();
                sc.nextLine();

                service.deposit(depositAccount, depositAmount);

                System.out.println("Deposit successful.");
                break;

            case 3:
                System.out.print("Account Number : ");
                long withdrawAccount = sc.nextLong();

                System.out.print("Amount         : ");
                double withdrawAmount = sc.nextDouble();
                sc.nextLine();

                service.withdraw(withdrawAccount, withdrawAmount);

                System.out.println("Withdrawal successful.");
                break;

            case 4:
                System.out.print("From Account : ");
                long fromAccount = sc.nextLong();

                System.out.print("To Account   : ");
                long toAccount = sc.nextLong();

                System.out.print("Amount       : ");
                double transferAmount = sc.nextDouble();
                sc.nextLine();

                service.transfer(fromAccount, toAccount, transferAmount);

                System.out.println("Transfer successful.");
                break;

            case 5:
                System.out.print("Account Number : ");
                long balanceAccount = sc.nextLong();
                sc.nextLine();

                System.out.printf(
                        "Current Balance: %.2f%n",
                        service.checkBalance(balanceAccount)
                );
                break;

            case 6:
                System.out.print("Account Number : ");
                long transactionAccount = sc.nextLong();
                sc.nextLine();

                showTransactions(
                        service.getTransactions(transactionAccount)
                );
                break;

            case 7:
                viewAccounts();
                break;

            case 8:
                service.logout();
                System.out.println("Logged out.");
                break;

            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void adminMenu() {

        header("ADMIN MENU - " + service.getCurrentUser().getName());

        System.out.println("1. View All Customers");
        System.out.println("2. Search Customer");
        System.out.println("3. View All Accounts");
        System.out.println("4. Activate / Deactivate Account");
        System.out.println("5. View All Transactions");
        System.out.println("6. Logout");

        System.out.print("Enter choice: ");
        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {

            case 1:
                printCustomers(service.getAllCustomers());
                break;

            case 2:
                System.out.print("Name/Email/Mobile/ID: ");
                String search = sc.nextLine();

                printCustomers(service.searchCustomers(search));
                break;

            case 3:
                viewAllAccounts();
                break;

            case 4:
                System.out.print("Account Number : ");
                long acc = sc.nextLong();
                sc.nextLine();

                System.out.print(
                        "Activate? (y = activate, n = deactivate): "
                );
                String s = sc.nextLine();

                service.setAccountStatus(
                        acc,
                        s.equalsIgnoreCase("y")
                );

                System.out.println("Account status updated.");
                break;

            case 5:
                showTransactions(service.getAllTransactions());
                break;

            case 6:
                service.logout();
                System.out.println("Logged out.");
                break;

            default:
                System.out.println("Invalid choice.");
        }
    }

    // ---------------- ACTIONS ----------------

    private static void register() {

        header("CUSTOMER REGISTRATION");

        System.out.print("Name     : ");
        String name = sc.nextLine();

        System.out.print("Email    : ");
        String email = sc.nextLine();

        System.out.print("Mobile   : ");
        String mobile = sc.nextLine();

        System.out.print("Address  : ");
        String address = sc.nextLine();

        System.out.print("Password : ");
        String password = sc.nextLine();

        Customer c = service.register(
                name,
                email,
                mobile,
                address,
                password
        );

        System.out.println(
                "Registered successfully. Customer ID: "
                        + c.getCustomerId()
                        + ". Please login."
        );
    }

    private static void login(boolean admin) {

        header(admin ? "ADMIN LOGIN" : "CUSTOMER LOGIN");

        System.out.print("Email    : ");
        String email = sc.nextLine();

        System.out.print("Password : ");
        String password = sc.nextLine();

        Customer c = service.login(
                email,
                password,
                admin
        );

        System.out.println("Welcome, " + c.getName() + "!");
    }

    private static void createAccount() {

        System.out.println("1. Savings Account");
        System.out.println("2. Current Account");

        System.out.print("Choose type: ");
        int t = sc.nextInt();

        if (t != 1 && t != 2) {
            sc.nextLine();
            System.out.println("Invalid account type.");
            return;
        }

        System.out.print("Opening deposit (0 for none): ");
        double opening = sc.nextDouble();
        sc.nextLine();

        long no = service.createAccount(
                t == 1 ? "SAVINGS" : "CURRENT",
                opening
        );

        System.out.println(
                "Account created. Account Number: " + no
        );
    }

    private static void viewAccounts() {

        List<Account> accounts = service.getMyAccounts();

        if (accounts.isEmpty()) {
            System.out.println("You have no accounts yet.");
            return;
        }

        for (Account a : accounts) {
            System.out.println("--------------------------------");
            a.DisplayAccount();
        }
    }

    private static void viewAllAccounts() {

        Map<Integer, List<Account>> map =
                service.getAllAccountsByCustomer();

        if (map.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }

        map.forEach((customerId, list) -> {

            System.out.println("Customer #" + customerId);

            list.forEach(a ->
                    System.out.printf(
                            "   %d | %-8s | %12.2f | %s%n",
                            a.getAccountNumber(),
                            a.getAccountType(),
                            a.getBalance(),
                            a.getStatus()
                    )
            );
        });
    }

    private static void printCustomers(List<Customer> list) {

        if (list.isEmpty()) {
            System.out.println("No customers found.");
            return;
        }

        System.out.printf(
                "%-5s %-20s %-28s %-12s%n",
                "ID",
                "Name",
                "Email",
                "Mobile"
        );

        Iterator<Customer> it = list.iterator();

        while (it.hasNext()) {

            Customer c = it.next();

            System.out.printf(
                    "%-5d %-20s %-28s %-12s%n",
                    c.getCustomerId(),
                    c.getName(),
                    c.getEmail(),
                    c.getMobile()
            );
        }
    }

    private static void showTransactions(List<Transation> list) {

        if (list.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        System.out.printf(
                "%-4s %-8s %-14s %-12s %-12s %s%n",
                "#",
                "Acct",
                "Type",
                "Amount",
                "Date",
                "Description"
        );

        for (int i = 0; i < list.size(); i++) {

            Transation t = list.get(i);

            System.out.printf(
                    "%-4d %-8d %-14s %-12.2f %-12s %s%n",
                    i + 1,
                    t.getAccountNumber(),
                    t.getType(),
                    t.getAmount(),
                    String.valueOf(t.getDate()).substring(0, 10),
                    t.getDesription()
            );
        }

        double credited = list.stream()
                .filter(t ->
                        t.getType().equals("DEPOSIT")
                                || t.getType().equals("TRANSFER_IN")
                )
                .mapToDouble(Transation::getAmount)
                .sum();

        System.out.printf(
                "Total credited in listing: %.2f%n",
                credited
        );
    }

    private static void header(String title) {

        System.out.println("\n======================================");
        System.out.println("  " + title);
        System.out.println("======================================");
    }
}
