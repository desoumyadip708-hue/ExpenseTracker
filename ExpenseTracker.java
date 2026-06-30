import java.util.Scanner;

public class ExpenseTracker {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ExpenseManager manager = new ExpenseManager();

        while (true) {
            System.out.println("\n===== EXPENSE TRACKER =====");
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. Show Total");
            System.out.println("4. Delete Expense");
            System.out.println("5. Search Expense");
            System.out.println("6. Category Summary");
            System.out.println("7. Update Expense");
            System.out.println("8. Sort By Amount");
            System.out.println("9. Monthly Report");
            System.out.println("10. Export Monthly Report");
            System.out.println("11. Exit");

            System.out.print("Enter Choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    manager.addExpense(sc);
                    break;
                case 2:
                    manager.viewExpenses();
                    break;
                case 3:
                    manager.showTotal();
                    break;
                case 4:
                    manager.deleteExpense(sc);
                    break;
                case 5:
                    manager.searchExpense(sc);
                    break;
                case 6:
                    manager.categorySummary();
                    break;
                case 7:
                    manager.updateExpense(sc);
                    break;
                case 8:
                    manager.sortByAmount();
                    break;
                case 9:
                    manager.monthlyReport();
                    break;
                case 10:
                    manager.exportMonthlyReport();
                    break;
                case 11:
                    System.out.println("Thank You!");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid Choice");
            }
        }
    }
}