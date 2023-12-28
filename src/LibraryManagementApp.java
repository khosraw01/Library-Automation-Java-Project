import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.ArrayList;

public class LibraryManagementApp extends JFrame {
    static String booksFilePath = "books.txt";
    static String usersFilePAth = "users.txt";
    static final String transactionsFilePath = "Transactions.txt";
    private JButton loginButton;
    private JButton signUpButton;
    public int timelyUserId;//to keep user's ID when logged in.
    public NormalUser thisUser;

    public LibraryManagementApp() {
        setTitle("Library");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700,600);
        setLocationRelativeTo(null);
        setResizable(false);//Turns off the minimize button.
        setVisible(true);

        ImageIcon icon = new ImageIcon("icon.jpg");
        setIconImage(icon.getImage());//Set an icon to the frame.

        loginButton = new JButton("Login");
        loginButton.setFocusPainted(false);//Remove the button focus.
        signUpButton = new JButton("Sign Up");
        signUpButton.setFocusPainted(false);

        Font defaultFont = new Font("Rockwell",Font.PLAIN, 50);

        loginButton.setFont(defaultFont);
        signUpButton.setFont(defaultFont);

        //Adding actionListener to buttons.
        loginButton.addActionListener(e -> openLoginPage());
        signUpButton.addActionListener(e -> openSignUpPage());
        //Adding background image to the panel.
        JPanel mainPanel = new JPanel(){
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("millet.jpg");
                // Scale the image to fit the panel.
                Image scaledImage = backgroundImage.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                scaledIcon.paintIcon(this, g, 0, 0);
            }
        };

        mainPanel.setLayout(new FlowLayout());
        mainPanel.add(loginButton);
        mainPanel.add(signUpButton);
        add(mainPanel);
    }
    //When login button is clicked the tasks that are in login page.
    private void openLoginPage() {
        JFrame loginFrame = new JFrame("Login Page");
        loginFrame.setSize(600,400);
        loginFrame.setLocationRelativeTo(null);

        JTextField userIdField = new JTextField();
        Font textFieldFont = new Font("Arial", Font.PLAIN, 25);
        userIdField.setFont(textFieldFont);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(textFieldFont);

        JButton loginSubmitButton = new JButton("Submit");
        loginSubmitButton.setFocusPainted(false);//Remove the button focus.

        loginSubmitButton.setFont(textFieldFont);

        Font labelFont = new Font("Rockwell", Font.PLAIN, 25);//Set font for the labels.

        JPanel loginPanel = new JPanel(new GridLayout(3,2));

        JLabel userIdLabel = new JLabel("User ID:");
        userIdLabel.setFont(labelFont);
        loginPanel.add(userIdLabel);
        loginPanel.add(userIdField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(labelFont);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);

        loginPanel.add(new JLabel());//Empty label for spacing
        loginPanel.add(loginSubmitButton);
        loginFrame.add(loginPanel);

        loginSubmitButton.addActionListener(e -> {
            try {
                int userId = Integer.parseInt(userIdField.getText());
                char[] passwordsChars = passwordField.getPassword();
                String password = new String(passwordsChars);

                if (LibraryManagementSystem.authenticateUserbyPassword(userId, password)) {;
                    timelyUserId = userId;
                    thisUser = LibraryManagementSystem.findUserById(userId);
                    JOptionPane.showMessageDialog(null, "Login successful!");
                    loginFrame.dispose();
                    openMainApplicationPage();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid userId or password");
                }
                userIdField.setText("");//Clear the userId input.
                passwordField.setText("");//Clear the password input.
            } catch (NumberFormatException ex) {
                // Handle the case where the user entered a non-numeric value for userId
                JOptionPane.showMessageDialog(null, "Invalid userId. Please enter a numeric value.");
                userIdField.setText(""); // Clear the invalid input
                passwordField.setText("");
            }
        });
        loginFrame.getRootPane().setDefaultButton(loginSubmitButton);//Set the default(Enter) button to loginSubmitButton
        loginFrame.setVisible(true);
    }

    private void openSignUpPage() {
        JFrame signUpFrame = new JFrame("Sign Up Page");
        signUpFrame.setSize(600,400);
        signUpFrame.setLocationRelativeTo(null);

        Font textFieldFont = new Font("Arial", Font.PLAIN, 25);
        JTextField nameField = new JTextField();
        nameField.setFont(textFieldFont);
        nameField.setToolTipText("Enter your name.");//When hover over it, shows this.
        JTextField emailField = new JTextField();
        emailField.setFont(textFieldFont);
        emailField.setToolTipText("Should include '@' and ends with '.com' ");
        JTextField ageField = new JTextField();
        ageField.setFont(textFieldFont);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(textFieldFont);

        JButton signUpSubmitButton = new JButton("Submit");
        signUpSubmitButton.setFocusPainted(false);//Remove the button focus.
        signUpSubmitButton.setFont(textFieldFont);

        JPanel signUpPanel = new JPanel(new GridLayout(5,2));
        Font labelFont = new Font("Rockwell", Font.PLAIN, 25);//Set font for the labels.
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(labelFont);
        signUpPanel.add(nameLabel);
        signUpPanel.add(nameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(labelFont);
        signUpPanel.add(emailLabel);
        signUpPanel.add(emailField);

        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setFont(labelFont);
        signUpPanel.add(ageLabel);
        signUpPanel.add(ageField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(labelFont);
        signUpPanel.add(passLabel);
        signUpPanel.add(passwordField);

        signUpPanel.add(new JLabel());//Empty label for spacing.
        signUpPanel.add(signUpSubmitButton);
        signUpFrame.add(signUpPanel);

        signUpSubmitButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                String email = null;
                if (emailField.getText().contains("@") && emailField.getText().endsWith(".com")) {//Controlling the email.
                    email = emailField.getText();
                } else {
                    JOptionPane.showMessageDialog(null, "Wrong mail.");
                    emailField.setText("");
                }
                int age = Integer.parseInt(ageField.getText());
                String password1 = new String(passwordField.getPassword());//Casting password to String to check if it is empty or not.
                if(emailField.getText().isEmpty() || nameField.getText().isEmpty() || password1.isEmpty()) throw new CustomException();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars);
                int userId = LibraryManagementSystem.generateUserId();
                NormalUser newUser = new NormalUser(name, email, age, password);
                newUser.setUserID(userId);
                LibraryManagementSystem.addUser(newUser);
                JOptionPane.showMessageDialog(null, "User registrated successfully");
                signUpFrame.dispose();//Closing the signup frame when registration is completed.
                //Clearing the fields to register another user.
                nameField.setText("");
                emailField.setText("");
                ageField.setText("");
                passwordField.setText("");
                JOptionPane.showMessageDialog(null, "Your User Id: " + userId);//Showing user Id to user.
            }catch (CustomException exception){
                JOptionPane.showMessageDialog(null, exception.getMessage() + ": Name, email and password fields cannot be empty!");
            }catch (NumberFormatException exception){
                JOptionPane.showMessageDialog(null, "String value is not accepted in the age field!");
                ageField.setText("");//Clearing the field after the error.
            }
        });
        signUpFrame.getRootPane().setDefaultButton(signUpSubmitButton);//Set the default(Enter) button to signUpSubmitButton.
        signUpFrame.setVisible(true);
    }
    private void openMainApplicationPage(){//Main page after logging in.
        JFrame mainFrame = new JFrame("Main page");
        mainFrame.setSize(700, 600);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        mainFrame.setResizable(false);
        //Adding background image to the panel.
        JPanel panel = new JPanel(){
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("R.jpeg");
                // Scale the image to fit the panel.
                Image scaledImage = backgroundImage.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                scaledIcon.paintIcon(this, g, 0, 0);
            }
        };
        mainFrame.add(panel);
        Font buttonFont = new Font("Rockwell", Font.BOLD, 20);//Set font for the buttons.
        //Making me button that shows the information of the user.
        JButton userButton = new JButton("Me");
        userButton.setFocusPainted(false);//Remove the focus button.
        userButton.setPreferredSize(new Dimension(150, 80));//Set size of the button manually.
        userButton.setFont(buttonFont);
        panel.add(userButton);

        userButton.addActionListener(e -> {//Actions me button do when clicked.
            for (NormalUser user: LibraryManagementSystem.allusers){
                if (timelyUserId == user.getUserID()){
                    JFrame userFrame = new JFrame("User Info");
                    userFrame.setResizable(false);
                    userFrame.setSize(700, 600);
                    JPanel userPAnel = new JPanel(new GridLayout(5,2)){//Change user frame's background.
                        protected void paintComponent(Graphics g){
                            super.paintComponent(g);
                            ImageIcon backgroundImage = new ImageIcon("user.png");
                            // Scale the image to fit the panel.
                            Image scaledImage = backgroundImage.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                            ImageIcon scaledIcon = new ImageIcon(scaledImage);
                            scaledIcon.paintIcon(this, g, 0, 0);
                        }
                    };
                    userFrame.add(userPAnel);//Adding panel to the frame.
                    Font labelFont = new Font("Rockwell", Font.PLAIN, 25);//Set font for the labels.
                    Color labelColor = Color.white;//Set font color.
                    //Adding labels for showing user's information.
                    JLabel userIdLabel = new JLabel("User ID:");
                    userIdLabel.setFont(labelFont);
                    userIdLabel.setForeground(labelColor);
                    userPAnel.add(userIdLabel);

                    JLabel idLabel = new JLabel(String.valueOf(user.getUserID()));
                    idLabel.setFont(labelFont);
                    idLabel.setForeground(labelColor);
                    userPAnel.add(idLabel);

                    JLabel nameLabel = new JLabel("Name:");
                    nameLabel.setFont(labelFont);
                    nameLabel.setForeground(labelColor);
                    userPAnel.add(nameLabel);

                    JLabel nameLabel1 = new JLabel(user.getName());
                    nameLabel1.setFont(labelFont);
                    nameLabel1.setForeground(labelColor);
                    userPAnel.add(nameLabel1);

                    JLabel emailLabel = new JLabel("Email:");
                    emailLabel.setFont(labelFont);
                    emailLabel.setForeground(labelColor);
                    userPAnel.add(emailLabel);

                    JLabel emailLabel1 = new JLabel(user.getEmail());
                    emailLabel1.setFont(labelFont);
                    emailLabel1.setForeground(labelColor);
                    userPAnel.add(emailLabel1);

                    JLabel ageLabel = new JLabel("Age:");
                    ageLabel.setFont(labelFont);
                    ageLabel.setForeground(labelColor);
                    userPAnel.add(ageLabel);

                    JLabel ageLabel1 = new JLabel(String.valueOf(user.getAge()));
                    ageLabel1.setFont(labelFont);
                    ageLabel1.setForeground(labelColor);
                    userPAnel.add(ageLabel1);

                    JLabel passLabel = new JLabel("Password:");
                    passLabel.setFont(labelFont);
                    passLabel.setForeground(labelColor);
                    userPAnel.add(passLabel);

                    JLabel passLabel1 = new JLabel(user.getPassword());
                    passLabel1.setFont(labelFont);
                    passLabel1.setForeground(labelColor);
                    userPAnel.add(passLabel1);

                    userFrame.setVisible(true);
                    userFrame.setLocationRelativeTo(null);
                }
            }
        });
        //Making button for seeing the books.
        JButton booksButton = new JButton("All Books");
        booksButton.setFont(buttonFont);
        booksButton.setFocusPainted(false);
        booksButton.setPreferredSize(new Dimension(150, 80));
        panel.add(booksButton);

        booksButton.addActionListener(e -> {
            JFrame bookFrame = new JFrame("Books");
            bookFrame.setResizable(false);
            bookFrame.setSize(700, 600);

            JPanel bookPanel = new JPanel(new BorderLayout());
            bookPanel.setBackground(Color.CYAN);
            bookFrame.add(bookPanel);

            JTextArea booksTextArea = new JTextArea();
            booksTextArea.setEditable(false);
            ArrayList<Books> booksList = LibraryManagementSystem.readBooksFromFile(booksFilePath);
            StringBuilder booksText = new StringBuilder();
            for (Books book : booksList){
                booksText.append(book.toString()).append("\n");
            }
            booksTextArea.setText(booksText.toString());
            bookPanel.add(new JScrollPane(booksTextArea),BorderLayout.CENTER);//for scrolling books

            bookFrame.setLocationRelativeTo(null);
            bookFrame.setVisible(true);
        });

        //Button for borrowing books.
        JButton borrowAbook = new JButton("Borrow a Book");
        borrowAbook.setFont(buttonFont);
        borrowAbook.setPreferredSize(new Dimension(150,80));
        panel.add(borrowAbook);

        borrowAbook.addActionListener(e -> {//Actions that the button do when clicking borrow a book button.
            JFrame borrowFrame = new JFrame();
            borrowFrame.setResizable(false);
            borrowFrame.setSize(700,600);
            borrowFrame.setLocationRelativeTo(null);
            borrowFrame.setVisible(true);

            JPanel borrowPanel = new JPanel();
            borrowFrame.add(borrowPanel);

            JLabel bookIdLabel = new JLabel("Enter Book ID");
            JTextField bookIdTextField = new JTextField(10);
            JButton borrowButton = new JButton("Borrow book");

            borrowPanel.add(bookIdLabel);
            borrowPanel.add(bookIdTextField);
            borrowPanel.add(borrowButton);

            borrowButton.addActionListener(e1 -> {
                String bookId =  bookIdTextField.getText();
                Books bookToBorrow = LibraryManagementSystem.findBookById(bookId);
                boolean isAvailable = bookToBorrow.isAvailable();
                if (bookToBorrow != null && isAvailable){
                    LocalDate thisDate = LocalDate.now();
                    LibraryManagementSystem.borrowBook(thisUser,bookToBorrow,thisDate);
                    JOptionPane.showMessageDialog(null,"Book borrowed successfully");
                    LibraryManagementSystem.updateBookAvailabilityInFile(bookId,false);//After borrowing the book makes it unavailable.

                }
                else {
                    JOptionPane.showMessageDialog(null,"Book not found");
                }
            });
            borrowFrame.getRootPane().setDefaultButton(borrowButton);//Set the default(Enter) button to borrowButton.
            borrowFrame.setLocationRelativeTo(null);//Set location to center.
            borrowFrame.setVisible(true);
        });

        //When closing the frame it writes the transactions that took place to the Transaction.txt file.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                LibraryManagementSystem.writeTransactionsToFile(LibraryManagementSystem.alltransactions,transactionsFilePath);
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        Admin admin = new Admin(1313, "Kazybek", "kazy@gmail.com", 21, "123", "admin");

        Books book1 = new Books("0001","The tale of two cities","Charles Dickens","historical fiction",false,1859);
        LibraryManagementSystem.addBook(book1);//Add book to book Array List.
        Books book2 = new Books("0002","The little prince","Antoine de SAint Exupery","Fantasy",true,1943);
        LibraryManagementSystem.addBook(book2);
        Books book3 = new Books("0003"," Hobbit","J.R TolkienThe","Fantasy",true,1937);
        LibraryManagementSystem.addBook(book3);
        Books book4 = new Books("0004","The Alchemist","Paulo Coelho","Fantasy",true,1988);
        LibraryManagementSystem.addBook(book4);

        LibraryManagementSystem.alltransactions = LibraryManagementSystem.readTransactionsFromFile(transactionsFilePath);

        LibraryManagementSystem.writeBooksToFile(LibraryManagementSystem.allbooks,booksFilePath);
        LibraryManagementSystem.allbooks = LibraryManagementSystem.readBooksFromFile(booksFilePath);

        LibraryManagementSystem.writeUsersToFile(LibraryManagementSystem.allusers,usersFilePAth);
        LibraryManagementSystem.allusers = LibraryManagementSystem.readUsersFromFile(usersFilePAth);

        EventQueue.invokeLater(() -> {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    try {
                        UIManager.setLookAndFeel(info.getClassName());
                    } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
            }
            new LibraryManagementApp();
        });
    }
}

