
import org.example.model.Book;
import org.example.manager.BookManager;
import org.example.manager.Manager;
import org.example.model.enums.MemberLevel;
import org.example.model.Member;

import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static boolean running = true;
    private static boolean memberIsLoggedIn = false;
    private static final Manager manager=new Manager();
    private static Member member;
    private static final BookManager bookManager=new BookManager();
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("Welcome to the Library");

        while (running){
         printMenu();
         int choice =innerInteger("enter your choice");
         switch (choice){
             case 1:
                 memberJoin();
                 break;
             case 2:
                 memberLogin();
                 break;
             case 3:
                 borrow();
                 break;
             case 4:
                 returnBook();
                 break;
             case 5:
                 showAllBooks();
                 break;
             case 6:
                 showMyBooks();
                 break;
             case 7:
                 showMyLevel();
                 break;
             case 8:
                 showMyMembershipRemainDays();
                 break;
             case 9:
                 renewMyMembership();
                 break;
             case 10:
                 exit();
         }


    }}
    private static void printMenu(){
        System.out.println("\n========== LIBRARY ==========");
        System.out.println("1. SignUp");
        System.out.println("2. Login");
        System.out.println("3. borrowBook");
        System.out.println("4. returnBook");
        System.out.println("5. showAllBooks");
        System.out.println("6. showMyBooks");
        System.out.println("7. showMyLevel");
        System.out.println("8. showMyMembershipRemainDays");
        System.out.println("9. renewMyMembership");
        System.out.println("10. exit");
        System.out.println("====================================");

    }

    private  static void renewMyMembership(){
        if(checkLogin()){
        member.getMemberManager().renewMember();}
    }
    private static void showMyMembershipRemainDays(){
        if(checkLogin()){
        System.out.println(" Membership Remain Days:"+member.getMemberManager().getRemainDays());
    }}
    private static void showMyLevel(){
        if(checkLogin()){
        System.out.println("your Level:"+ member.getLevel().toString());
    }}
    private static void showMyBooks(){
        if(checkLogin()){
        manager.getBorrowManager(member).returnBooks().forEach(System.out::println);
    }}
    private static void showAllBooks(){
        if(checkLogin()){
        bookManager.getBooks().forEach(System.out::println);
    }}
    private static void borrow(){
        if(checkLogin()){
        System.out.println("Please enter book ");
        Optional<Book> b= bookManager.getBook(scanner.nextInt());
        if(b.isPresent()){
        manager.borrowBook(b.get(),member);}
        else System.out.println("Book Not Found");
    }}
    private static void returnBook(){
        if(checkLogin()){
        System.out.println("Please enter book ");
        Optional<Book> b= bookManager.getBook(scanner.nextInt());
        if(b.isPresent()){
            manager.returnBook(b.get(),member);
        }
        else System.out.println("Book Not Found");
    }}
    private static void memberLogin(){
        scanner.nextLine();
        System.out.println("Please enter your username:");
        String username = scanner.nextLine();
        System.out.println("Please enter your password:");
        String password = scanner.nextLine();
        member= (Member) manager.theMemberLoggedIn(username,password);
        if(member!=null){
            System.out.println("You have successfully logged in");
            memberIsLoggedIn=true;
        }
        else{
            System.out.println("The member is not exist");
        }
    }

    private static void memberJoin(){
        scanner.nextLine();
        System.out.println("Please enter your username(username must contain at least three character):");
        String username = scanner.nextLine();
        System.out.println("Please enter your password(password must contain at least eight character/one special character/one number/one upperCaseCharacter):");
        String password = scanner.nextLine();
        System.out.println("Please choose a MemberLevel:");
        System.out.println("""
                 1*GOLD(fee:500,bookLimit:7,returnLimit:30,memberShipDays:400),
                   2* SILVER(fee:300,bookLimit:5,returnLimit:25,memberShipDays:300),
                    3*BRONZE(fee:100,bookLimit:3,returnLimit:20,memberShipDays:200);\
                """);
        int l= Integer.parseInt(scanner.nextLine());
        MemberLevel level=l==1?MemberLevel.GOLD:MemberLevel.SILVER;
        if(l==3)level=MemberLevel.BRONZE;
        manager.addMember(username,password,level);
    }
    private static int innerInteger(String message){
        System.out.println(message);
        try {
            return scanner.nextInt();
        }
        catch (InputMismatchException e){
            System.out.println("Please enter a valid choice");
            return -1;
        }
    }
    private static void exit(){
        running = false;
        System.out.println("Goodbye!");
    }
    private static boolean checkLogin(){
        if(memberIsLoggedIn&&member!=null){
            return true;
        }
        else{
            System.out.println("you should login first");
            return false;
    }}
    }
