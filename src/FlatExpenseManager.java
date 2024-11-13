import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Person {
    private String name;
    private double balance;

    public Person(String name) {
        this.name = name;
        this.balance = 0.0;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void updateBalance(double amount) {
        this.balance += amount;
    }

    @Override
    public String toString() {
        return name + " has balance: " + balance;
    }
}

class Expense {
    private double amount;
    private Person payer;
    private List<Person> participants;

    public Expense(double amount, Person payer, List<Person> participants) {
        this.amount = amount;
        this.payer = payer;
        this.participants = participants;
        splitExpense();
    }

    private void splitExpense() {
        double splitAmount = amount / participants.size();

        // Update payer's balance
        payer.updateBalance(amount);

        // Subtract split amount from each participant, including payer if involved
        for (Person person : participants) {
            person.updateBalance(-splitAmount);
        }
    }
}

class Group {
    private String groupName;
    private List<Person> members;

    public Group(String groupName) {
        this.groupName = groupName;
        this.members = new ArrayList<>();
    }

    public void addMember(Person person) {
        members.add(person);
    }

    public List<Person> getMembers() {
        return members;
    }

    public void addExpense(double amount, Person payer, List<Person> participants) {
        new Expense(amount, payer, participants);
    }

    public void showBalances() {
        for (Person person : members) {
            System.out.println(person);
        }
    }

    public Person findMember(String name) {
        for (Person person : members) {
            if (person.getName().equalsIgnoreCase(name)) {
                return person;
            }
        }
        return null;
    }
}

public class FlatExpenseManager {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Group group = null;

        while (true) {
            System.out.println("\n---- Flat Expense Manager ----");
            System.out.println("1. Create Group");
            System.out.println("2. Add Member");
            System.out.println("3. Add Expense");
            System.out.println("4. View Balances");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    group = createGroup();
                    break;
                case 2:
                    if (group != null) {
                        addMember(group);
                    } else {
                        System.out.println("Please create a group first.");
                    }
                    break;
                case 3:
                    if (group != null) {
                        addExpense(group);
                    } else {
                        System.out.println("Please create a group first.");
                    }
                    break;
                case 4:
                    if (group != null) {
                        System.out.println("\n--- Group Balances ---");
                        group.showBalances();
                    } else {
                        System.out.println("Please create a group first.");
                    }
                    break;
                case 5:
                    System.out.println("Exiting... Thank you!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static Group createGroup() {
        System.out.print("Enter group name: ");
        String groupName = scanner.nextLine();
        System.out.println("Group '" + groupName + "' created successfully.");
        return new Group(groupName);
    }

    private static void addMember(Group group) {
        System.out.print("Enter member name: ");
        String name = scanner.nextLine();
        Person person = new Person(name);
        group.addMember(person);
        System.out.println("Member '" + name + "' added to group.");
    }

    private static void addExpense(Group group) {
        System.out.print("Enter payer's name: ");
        String payerName = scanner.nextLine();
        Person payer = group.findMember(payerName);

        if (payer == null) {
            System.out.println("Payer not found in group. Please add them as a member first.");
            return;
        }

        System.out.print("Enter amount paid: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        List<Person> participants = new ArrayList<>();
        System.out.println("Enter participants' names (type 'done' when finished): ");
        while (true) {
            String participantName = scanner.nextLine();
            if (participantName.equalsIgnoreCase("done")) break;
            Person participant = group.findMember(participantName);
            if (participant != null) {
                participants.add(participant);
            } else {
                System.out.println("Participant not found in group.");
            }
        }

        if (!participants.contains(payer)) {
            participants.add(payer); // add payer to participants if not included
        }

        group.addExpense(amount, payer, participants);
        System.out.println("Expense added successfully!");
    }
}
