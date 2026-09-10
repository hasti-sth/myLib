
import org.example.dao.BookDAO;
import org.example.dao.BorrowDAO;
import org.example.manager.BorrowManager;
import org.example.manager.MemberManager;
import org.example.model.Book;
import org.example.manager.BookManager;
import org.example.model.enums.MemberLevel;
import org.example.model.Member;
import org.example.util.PasswordUtil;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static boolean running = true;
    private static boolean memberIsLoggedIn = false;
    private static Member member;
    private static final BookDAO bd=new BookDAO();
    private static final BorrowDAO borrowDAO=new BorrowDAO();
    private static final MemberManager memberManager=new MemberManager();
    private static final BorrowManager borrowManager =new BorrowManager(borrowDAO);
    private static final BookManager bookManager=new BookManager(bd);
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
                 returnAllBook();
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
                 returnSelectedBook();
                 break;
             case 11:
                 exit();
                 break;
         }


    }}
    private static void printMenu(){
        System.out.println("\n========== LIBRARY ==========");
        System.out.println("1. SignUp");
        System.out.println("2. Login");
        System.out.println("3. borrowBook");
        System.out.println("4. returnAllBooks");
        System.out.println("5. showAllBooks");
        System.out.println("6. showMyBooks");
        System.out.println("7. showMyLevel");
        System.out.println("8. showMyMembershipRemainDays");
        System.out.println("9. renewMyMembership");
        System.out.println("10. returnSelectedBook");
        System.out.println("11. exit");
        System.out.println("====================================");

    }

    private  static void renewMyMembership(){
        if(checkLogin()){
        memberManager.renewMember(member);}
    }
    private static void showMyMembershipRemainDays(){
        if(checkLogin()){
        System.out.println(" Membership Remain Days:"+ MemberManager.getRemainDays(member));
    }}
    private static void showMyLevel(){
        if(checkLogin()){
        System.out.println("your Level:"+ member.getLevel().toString());
    }}
    private static void showMyBooks(){
        if(checkLogin()){
        borrowManager.returnMemberBooks(member).forEach(System.out::println);
    }}
    private static void showAllBooks(){
        if(checkLogin()){
        bookManager.printAllBooks();
    }}
    private static void borrow(){
        if(checkLogin()){
            System.out.println("Please enter book title");
            bookManager.getBookByTitle(scanner.nextLine()).forEach(System.out::println);
        System.out.println("Please enter book id");
        Book b= bookManager.getBookByID(scanner.nextInt());
        if(b!=null){
        borrowManager.borrowBook(b,member);}
        else System.out.println("Book Not Found");
    }}
    private static void returnAllBook(){
        if(checkLogin()){
        List<Book> books=borrowManager.returnMemberBooks(member);
        if(books!=null){
            books.forEach( b->borrowManager.returnBook(b,member));
        }
        else System.out.println("You have no books to return");
    }}
    private static void returnSelectedBook(){
        System.out.println("enter book id");
        showMyBooks();
        Book b=bookManager.getBookByID(scanner.nextInt());
        if(b!=null) borrowManager.returnBook(b,member);
        else System.out.println("Book Not Found");
    }
    private static void memberLogin(){
        scanner.nextLine();
        System.out.println("Please enter your username:");
        String username = scanner.nextLine();
        System.out.println("Please enter your password:");
        String password = PasswordUtil.hashPassword(scanner.nextLine());
        member=  memberManager.theMemberLoggedIn(username,password);
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
        String password = PasswordUtil.hashPassword( scanner.nextLine());
        System.out.println("Please choose a MemberLevel:");
        System.out.println("""
                 1*GOLD(fee:500,bookLimit:7,returnLimit:30,memberShipDays:400),
                   2* SILVER(fee:300,bookLimit:5,returnLimit:25,memberShipDays:300),
                    3*BRONZE(fee:100,bookLimit:3,returnLimit:20,memberShipDays:200);\
                """);
        int l= Integer.parseInt(scanner.nextLine());
        MemberLevel level=l==1?MemberLevel.GOLD:MemberLevel.SILVER;
        if(l==3)level=MemberLevel.BRONZE;
        memberManager.addMember(username,password,level);
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
