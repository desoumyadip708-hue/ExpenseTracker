import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class ExpenseManager {

    private ArrayList<Expense> expenses = new ArrayList<>();
    private final String FILE_NAME = "expenses.txt";
    private final String REPORT_FILE = "monthly_report.txt";

    public ExpenseManager() {
        loadExpenses();
    }

    void loadExpenses() {
        try {
            File file = new File(FILE_NAME);

            if (!file.exists()) {
                return;
            }

            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",", 4);

                if (parts.length >= 4) {
                    String name = parts[0];
                    double amount = Double.parseDouble(parts[1]);
                    String category = parts[2];
                    String date = parts[3];

                    expenses.add(new Expense(name, amount, category, date));
                }
            }

            fileScanner.close();
        } catch (Exception e) {
            System.out.println("Error Loading Expenses");
        }
    }

    void saveExpenses() {
        try {
            PrintWriter writer = new PrintWriter(FILE_NAME);

            for (Expense e : expenses) {
                writer.println(
                        e.getName() + ","
                                + e.getAmount() + ","
                                + e.getCategory() + ","
                                + e.getDate());
            }

            writer.close();
        } catch (Exception e) {
            System.out.println("Error Saving Expenses");
        }
    }

    void addExpense(Scanner sc) {
        try {
            sc.nextLine();

            System.out.print("Enter Expense Name: ");
            String name = sc.nextLine().trim();

            if (name.isEmpty()) {
                throw new InvalidExpenseException("Name cannot be empty.");
            }

            System.out.print("Enter Amount: ");
            if (!sc.hasNextDouble()) {
                System.out.println("Invalid amount. Please enter a number.");
                sc.nextLine();
                return;
            }

            double amount = sc.nextDouble();

            if (amount <= 0) {
                throw new InvalidExpenseException("Amount must be greater than 0.");
            }

            sc.nextLine();

            System.out.print("Enter Category: ");
            String category = sc.nextLine().trim();

            if (category.isEmpty()) {
                throw new InvalidExpenseException("Category cannot be empty.");
            }

            String date = LocalDate.now().toString();

            Expense expense = new Expense(name, amount, category, date);

            expenses.add(expense);
            saveExpenses();

            System.out.println("Expense Added Successfully!");
        } catch (InvalidExpenseException e) {
            System.out.println(e.getMessage());
        }
    }

    void viewExpenses() {
        if (expenses.size() == 0) {
            System.out.println("No Expenses Found");
            return;
        }

        System.out.println("\nExpenses:");

        for (int i = 0; i < expenses.size(); i++) {
            Expense e = expenses.get(i);

            System.out.println(
                    (i + 1) + ". "
                            + e.getName()
                            + " | Rs."
                            + e.getAmount()
                            + " | "
                            + e.getCategory()
                            + " | "
                            + e.getDate());
        }
    }

    void showTotal() {
        double total = 0;

        for (Expense e : expenses) {
            total += e.getAmount();
        }

        System.out.println("Total Expense = Rs." + total);
    }

    void deleteExpense(Scanner sc) {
        if (expenses.size() == 0) {
            System.out.println("No Expenses Found");
            return;
        }

        viewExpenses();

        System.out.print("Enter expense number to delete: ");
        int index = sc.nextInt();

        if (index >= 1 && index <= expenses.size()) {
            expenses.remove(index - 1);
            saveExpenses();
            System.out.println("Expense Deleted Successfully!");
        } else {
            System.out.println("Invalid Expense Number");
        }
    }

    void searchExpense(Scanner sc) {
        if (expenses.size() == 0) {
            System.out.println("No Expenses Found");
            return;
        }

        sc.nextLine();

        System.out.print("Enter keyword: ");
        String keyword = sc.nextLine();

        boolean found = false;

        for (Expense e : expenses) {
            if (e.getName().toLowerCase().contains(keyword.toLowerCase())
                    || e.getCategory().toLowerCase().contains(keyword.toLowerCase())
                    || e.getDate().toLowerCase().contains(keyword.toLowerCase())) {

                System.out.println(
                        e.getName()
                                + " | Rs."
                                + e.getAmount()
                                + " | "
                                + e.getCategory()
                                + " | "
                                + e.getDate());

                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching expenses found.");
        }
    }

    void categorySummary() {
        double foodTotal = 0;
        double travelTotal = 0;
        double educationTotal = 0;
        double shoppingTotal = 0;

        for (Expense e : expenses) {
            String category = e.getCategory().toLowerCase();

            if (category.equals("food")) {
                foodTotal += e.getAmount();
            } else if (category.equals("travel")) {
                travelTotal += e.getAmount();
            } else if (category.equals("education")) {
                educationTotal += e.getAmount();
            } else if (category.equals("shopping")) {
                shoppingTotal += e.getAmount();
            }
        }

        System.out.println("\nCategory Summary");
        System.out.println("Food = Rs." + foodTotal);
        System.out.println("Travel = Rs." + travelTotal);
        System.out.println("Education = Rs." + educationTotal);
        System.out.println("Shopping = Rs." + shoppingTotal);
    }

    void updateExpense(Scanner sc) {
        try {
            if (expenses.size() == 0) {
                System.out.println("No Expenses Found");
                return;
            }

            viewExpenses();

            System.out.print("Enter expense number to update: ");
            int index = sc.nextInt();

            if (index < 1 || index > expenses.size()) {
                System.out.println("Invalid Expense Number");
                return;
            }

            sc.nextLine();

            Expense e = expenses.get(index - 1);

            System.out.print("Enter New Name: ");
            String newName = sc.nextLine().trim();

            if (newName.isEmpty()) {
                throw new InvalidExpenseException("Name cannot be empty.");
            }

            System.out.print("Enter New Amount: ");
            if (!sc.hasNextDouble()) {
                System.out.println("Invalid amount. Please enter a number.");
                sc.nextLine();
                return;
            }

            double newAmount = sc.nextDouble();

            if (newAmount <= 0) {
                throw new InvalidExpenseException("Amount must be greater than 0.");
            }

            sc.nextLine();

            System.out.print("Enter New Category: ");
            String newCategory = sc.nextLine().trim();

            if (newCategory.isEmpty()) {
                throw new InvalidExpenseException("Category cannot be empty.");
            }

            String newDate = LocalDate.now().toString();

            e.setName(newName);
            e.setAmount(newAmount);
            e.setCategory(newCategory);
            e.setDate(newDate);

            saveExpenses();

            System.out.println("Expense Updated Successfully!");
        } catch (InvalidExpenseException e) {
            System.out.println(e.getMessage());
        }
    }

    void sortByAmount() {
        if (expenses.size() == 0) {
            System.out.println("No Expenses Found");
            return;
        }

        Collections.sort(expenses, new Comparator<Expense>() {
            @Override
            public int compare(Expense e1, Expense e2) {
                if (e1.getAmount() > e2.getAmount()) {
                    return 1;
                } else if (e1.getAmount() < e2.getAmount()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        System.out.println("Expenses Sorted Successfully!");
        viewExpenses();
    }

    void monthlyReport() {
        Map<String, Double> monthlyTotals = buildMonthlyTotals();

        if (monthlyTotals.isEmpty()) {
            System.out.println("No valid monthly data found.");
            return;
        }

        System.out.println("\nMonthly Report");
        for (Map.Entry<String, Double> entry : monthlyTotals.entrySet()) {
            System.out.println(entry.getKey() + " = Rs." + entry.getValue());
        }
    }

    void exportMonthlyReport() {
        Map<String, Double> monthlyTotals = buildMonthlyTotals();

        if (monthlyTotals.isEmpty()) {
            System.out.println("No valid monthly data found.");
            return;
        }

        try {
            PrintWriter writer = new PrintWriter(REPORT_FILE);

            writer.println("MONTHLY REPORT");
            writer.println();

            for (Map.Entry<String, Double> entry : monthlyTotals.entrySet()) {
                writer.println(entry.getKey() + " = Rs." + entry.getValue());
            }

            writer.close();

            System.out.println("Monthly report exported to " + REPORT_FILE);
        } catch (Exception e) {
            System.out.println("Error Exporting Monthly Report");
        }
    }

    private Map<String, Double> buildMonthlyTotals() {
        Map<String, Double> monthlyTotals = new TreeMap<>();

        for (Expense e : expenses) {
            String date = e.getDate();

            if (date != null && date.length() >= 7) {
                String month = date.substring(0, 7);

                double currentTotal = monthlyTotals.getOrDefault(month, 0.0);
                monthlyTotals.put(month, currentTotal + e.getAmount());
            }
        }

        return monthlyTotals;
    }
}