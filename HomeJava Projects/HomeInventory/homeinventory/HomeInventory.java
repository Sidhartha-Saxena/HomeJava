package homeinventory;

// import javax.print.PrintException;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;
import com.toedter.calendar.*;
import java.beans.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;
import java.text.*;
import java.awt.print.*;

class PhotoPanel extends JPanel {
    public void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        super.paintComponent(g2D);
        g2D.setPaint(Color.BLACK);
        g2D.draw(new Rectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1));

        Image photoImage = new ImageIcon(HomeInventory.photoTextArea.getText()).getImage();
        int w = getWidth();
        int h = getHeight();
        double rWidth = (double) getWidth() / (double) photoImage.getWidth(null);
        double rHeight = (double) getHeight() / (double) photoImage.getHeight(null);
        if (rWidth > rHeight) {
            // leave height at display height, change width by amount height is changed
            w = (int) (photoImage.getWidth(null) * rHeight);
        } else {
            // leave width at display width, change height by amount width is changed
            h = (int) (photoImage.getHeight(null) * rWidth);
        }
        // center in panel
        g2D.drawImage(photoImage, (int) (0.5 * (getWidth() - w)), (int) (0.5 * (getHeight() - h)),
                w, h, null);

        g2D.dispose();
    }
}

class InventoryDocument implements Printable{
    public int print(Graphics g,PageFormat pf,int pageIndex) {
        Graphics2D g2D=(Graphics2D)g;
        if((pageIndex+1)>HomeInventory.lastPage){
            return NO_SUCH_PAGE;
        }
        int i,iEnd;
        g2D.setFont(new Font("Arial", Font.BOLD, 14));
        g2D.drawString("Home Inventory Items - Page " + String.valueOf(pageIndex + 1),(int) pf.getImageableX(), (int) (pf.getImageableY() + 25));
        int dy=(int) g2D.getFont().getStringBounds("S",g2D.getFontRenderContext()).getHeight();
        int y = (int) (pf.getImageableY() + 4 * dy);
        iEnd = HomeInventory.entriesPerPage * (pageIndex + 1);
        if (iEnd > HomeInventory.numberEntries){
            iEnd = HomeInventory.numberEntries;
        }
        for (i = 0 + HomeInventory.entriesPerPage * pageIndex; i < iEnd; i++)
        {
        // dividing line
        Line2D.Double dividingLine = new Line2D.Double(pf.getImageableX(), y,
        pf.getImageableX() + pf.getImageableWidth(), y);
        g2D.draw(dividingLine);
        y += dy;
        g2D.setFont(new Font("Arial", Font.BOLD, 12));
        g2D.drawString(HomeInventory.myInventory[i].description, (int) pf.getImageableX(), y);
        y += dy;
        g2D.setFont(new Font("Arial", Font.PLAIN, 12));
        g2D.drawString("Location: " + HomeInventory.myInventory[i].location, (int)
        (pf.getImageableX() + 25), y);
        y += dy;
        if (HomeInventory.myInventory[i].marked)
            g2D.drawString("Item is marked with identifying information.", (int)(pf.getImageableX() + 25), y);
        else
            g2D.drawString("Item is NOT marked with identifying information.", (int)(pf.getImageableX() + 25), y);
        y += dy;
        g2D.drawString("Serial Number: " +
        HomeInventory.myInventory[i].serialNumber, (int) (pf.getImageableX() + 25), y);
        y += dy;
        g2D.drawString("Price: $" + HomeInventory.myInventory[i].purchasePrice + ",Purchased on: " + HomeInventory.myInventory[i].purchaseDate, (int) (pf.getImageableX() +
        25), y);
        y += dy;
        g2D.drawString("Purchased at: " +
        HomeInventory.myInventory[i].purchaseLocation, (int) (pf.getImageableX() + 25), y);
        y += dy;
        g2D.drawString("Note: " + HomeInventory.myInventory[i].note, (int)
        (pf.getImageableX() + 25), y);
        y += dy;
        try {
            Image inventoryImage = new
            ImageIcon(HomeInventory.myInventory[i].photoFile).getImage ();
            double ratio = (double) (inventoryImage.getWidth(null)) / (double)
            inventoryImage.getHeight(null);
            g2D.drawImage(inventoryImage, (int) (pf.getImageableX() + 25), y, (int) (100 *
            ratio), 100, null);
        } catch (Exception e) {
        }
        y += 2 * dy + 100;
    }
        return PAGE_EXISTS;
    
    }
}

public class HomeInventory extends JFrame {

    JToolBar inventoryToolBar = new JToolBar();
    // image size 24px for buttons
    JButton newButton = new JButton(new ImageIcon("HomeJava Projects/HomeInventory/new-file.png"));
    JButton deleteButton = new JButton(new ImageIcon("HomeJava Projects/HomeInventory/delete-file.png"));
    JButton saveButton = new JButton(new ImageIcon("HomeJava Projects/HomeInventory/save-file.png"));
    JButton previousButton = new JButton(new ImageIcon("HomeJava Projects/HomeInventory/previous-file.png"));
    JButton nextButton = new JButton(new ImageIcon("HomeJava Projects/HomeInventory/next-file.png"));
    JButton printButton = new JButton(new ImageIcon("HomeJava Projects/HomeInventory/printer-file.png"));
    JButton exitButton = new JButton(new ImageIcon("HomeJava Projects/HomeInventory/logout-file.png"));
    JButton cancelButton=new JButton(new ImageIcon("HomeJava Projects/HomeInventory/cancel.png"));
    JLabel itemLabel = new JLabel("Inventory Item");
    JTextField itemTextField = new JTextField();
    JLabel locationLabel = new JLabel("Location");
    JComboBox<String> locationComboBox = new JComboBox<>();
    JCheckBox markedCheckBox = new JCheckBox("Marked?");
    JLabel serialLabel = new JLabel("Serial Number");
    JTextField serialTextField = new JTextField();
    JLabel priceLabel = new JLabel("Purchase Price");
    JTextField priceTextField = new JTextField();
    JLabel dateLabel = new JLabel("Date Purchased");
    JDateChooser dateDateChooser = new JDateChooser();
    JLabel storeLabel = new JLabel("Store/Website");
    JTextField storeTextField = new JTextField();
    JLabel noteLabel = new JLabel("Note");
    JTextField noteTextField = new JTextField();
    JLabel photoLabel = new JLabel("Photo");
    static JTextArea photoTextArea = new JTextArea();
    JButton photoButton = new JButton("...");
    JPanel searchPanel = new JPanel();
    JButton[] searchButton = new JButton[26];
    PhotoPanel photoPanel = new PhotoPanel();

    private static final int maximumEntries = 300;
    static int numberEntries;
    static InvetoryItem[] myInventory = new InvetoryItem[maximumEntries];
    int currentEntry;
    static final int entriesPerPage = 2;
    static int lastPage;

    public HomeInventory() {
        this.setLayout(new GridBagLayout());
        this.setTitle("Home Inventory Manager");
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                exitForm(evt);
            }
        });
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds((int) (0.2 * (screenSize.width - getWidth())), (int) (0.2 * (screenSize.height - getHeight())),
                getWidth(), getHeight());
        GridBagConstraints gridConstraints;

        inventoryToolBar.setFloatable(false);
        // check what happens if float able is true
        inventoryToolBar.setBackground(new Color(0x00ffff));
        inventoryToolBar.setOrientation(SwingConstants.VERTICAL);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 0;
        gridConstraints.gridy = 0;
        gridConstraints.gridheight = 8;
        gridConstraints.fill = GridBagConstraints.VERTICAL;
        this.add(inventoryToolBar, gridConstraints);

        inventoryToolBar.addSeparator();

        Dimension bSize = new Dimension(80, 50);
        newButton.setText("New");
        sizeButton(newButton, bSize);
        newButton.setToolTipText("Add New Item");
        newButton.setHorizontalTextPosition(SwingConstants.CENTER);
        newButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        newButton.setFocusable(false);
        newButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newButtonActionPerformed(e);
            }
        });
        inventoryToolBar.add(newButton);

        deleteButton.setText("Delete");
        sizeButton(deleteButton, bSize);
        deleteButton.setToolTipText("Delete Current Item");
        deleteButton.setHorizontalTextPosition(SwingConstants.CENTER);
        deleteButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        deleteButton.setFocusable(false);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteButtonActionPerformed(e);
            }
        });
        inventoryToolBar.add(deleteButton);

        cancelButton.setText("Cancel");
        sizeButton(cancelButton, bSize);
        cancelButton.setToolTipText("Cancel New Item");
        cancelButton.setHorizontalTextPosition(SwingConstants.CENTER);
        cancelButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        cancelButton.setFocusable(false);
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelButtonActionPerformed(e);
            }
        });
        inventoryToolBar.add(cancelButton);

        saveButton.setText("Save");
        sizeButton(saveButton, bSize);
        saveButton.setToolTipText("Save Current Item");
        saveButton.setHorizontalTextPosition(SwingConstants.CENTER);
        saveButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        saveButton.setFocusable(false);
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveButtonActionPerformed(e);
            }
        });
        inventoryToolBar.add(saveButton);

        inventoryToolBar.addSeparator();

        previousButton.setText("Previous");
        sizeButton(previousButton, bSize);
        previousButton.setToolTipText("Display Previous Item");
        previousButton.setHorizontalTextPosition(SwingConstants.CENTER);
        previousButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        previousButton.setFocusable(false);
        previousButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                previousButtonActionPerformed(e);
            }
        });
        inventoryToolBar.add(previousButton);

        nextButton.setText("Next");
        sizeButton(nextButton, bSize);
        nextButton.setToolTipText("Display Next Item");
        nextButton.setHorizontalTextPosition(SwingConstants.CENTER);
        nextButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        nextButton.setFocusable(false);
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextButtonActionPerformed(e);
            }
        });
        inventoryToolBar.add(nextButton);

        inventoryToolBar.addSeparator();

        printButton.setText("Print");
        sizeButton(printButton, bSize);
        printButton.setToolTipText("Print Inventory List");
        printButton.setHorizontalTextPosition(SwingConstants.CENTER);
        printButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        printButton.setFocusable(false);
        printButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                printButtonActionPerformed(e);
            }
        });
        inventoryToolBar.add(printButton);

        exitButton.setText("Exit");
        sizeButton(exitButton, bSize);
        exitButton.setToolTipText("Exit Program");
        exitButton.setHorizontalTextPosition(SwingConstants.CENTER);
        exitButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        exitButton.setFocusable(false);
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exitButtonActionPerformed(e);
            }
        });
        inventoryToolBar.add(exitButton);

        itemLabel.setForeground(Color.BLACK);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 0;
        gridConstraints.insets = new Insets(10, 10, 0, 10);
        gridConstraints.anchor = GridBagConstraints.EAST;
        this.add(itemLabel, gridConstraints);

        itemTextField.setPreferredSize(new Dimension(400, 25));
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 0;
        gridConstraints.gridwidth = 5;
        gridConstraints.insets = new Insets(10, 0, 0, 10);
        gridConstraints.anchor = GridBagConstraints.WEST;
        itemTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                itemTextFieldActionPerformed(e);
            }

        });
        this.add(itemTextField, gridConstraints);

        locationLabel.setForeground(Color.BLACK);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 1;
        gridConstraints.insets = new Insets(10, 10, 0, 10);
        gridConstraints.anchor = GridBagConstraints.EAST;
        this.add(locationLabel, gridConstraints);

        locationComboBox.setPreferredSize(new Dimension(270, 25));
        locationComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
        locationComboBox.setEditable(true);
        locationComboBox.setBackground(Color.WHITE);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 1;
        gridConstraints.gridwidth = 3;
        gridConstraints.insets = new Insets(10, 0, 0, 10);
        gridConstraints.anchor = GridBagConstraints.WEST;
        locationComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                locationComboBoxActionPerformed(e);
            }
        });
        this.add(locationComboBox, gridConstraints);

        markedCheckBox.setForeground(Color.BLACK);
        markedCheckBox.setToolTipText(" Select if Item has some identifying information like social security number, driver's license number, etc");
        gridConstraints = new GridBagConstraints(); 
        gridConstraints.gridx = 5;
        gridConstraints.gridy = 1;
        gridConstraints.insets = new Insets(10, 10, 0, 0);
        gridConstraints.anchor = GridBagConstraints.WEST;
        markedCheckBox.setFocusable(false);
        this.add(markedCheckBox, gridConstraints);

        serialLabel.setForeground(Color.BLACK);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 2;
        gridConstraints.insets = new Insets(10, 10, 0, 10);
        gridConstraints.anchor = GridBagConstraints.EAST;
        this.add(serialLabel, gridConstraints);

        serialTextField.setPreferredSize(new Dimension(270, 25));
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 2;
        gridConstraints.gridwidth = 3;
        gridConstraints.insets = new Insets(10, 0, 0, 10);
        gridConstraints.anchor = GridBagConstraints.WEST;
        serialTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                serialTextFieldActionPerformed(e);
            }
        });
        this.add(serialTextField, gridConstraints);

        priceLabel.setForeground(Color.BLACK);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 3;
        gridConstraints.insets = new Insets(10, 10, 0, 10);
        gridConstraints.anchor = GridBagConstraints.EAST;
        this.add(priceLabel, gridConstraints);

        priceTextField.setPreferredSize(new Dimension(160, 25));
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 3;
        gridConstraints.gridwidth = 2;
        gridConstraints.insets = new Insets(10, 0, 0, 10);
        gridConstraints.anchor = GridBagConstraints.WEST;
        priceTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                priceTextFieldActionPerformed(e);
            }
        });
        this.add(priceTextField, gridConstraints);

        dateLabel.setForeground(Color.BLACK);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 4;
        gridConstraints.gridy = 3;
        gridConstraints.insets = new Insets(10, 10, 0, 0);
        gridConstraints.anchor = GridBagConstraints.EAST;
        this.add(dateLabel, gridConstraints);

        dateDateChooser.setPreferredSize(new Dimension(120, 25));
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 5;
        gridConstraints.gridy = 3;
        gridConstraints.gridwidth = 2;
        gridConstraints.insets = new Insets(10, 0, 0, 10);
        gridConstraints.anchor = GridBagConstraints.WEST;
        dateDateChooser.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                dateDateChooserPropertyChange(e);
            }
        });
        this.add(dateDateChooser, gridConstraints);

        storeLabel.setForeground(Color.BLACK);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 4;
        gridConstraints.insets = new Insets(10, 10, 0, 10);
        gridConstraints.anchor = GridBagConstraints.EAST;
        this.add(storeLabel, gridConstraints);

        storeTextField.setPreferredSize(new Dimension(400, 25));
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 4;
        gridConstraints.gridwidth = 5;
        gridConstraints.insets = new Insets(10, 0, 0, 10);
        gridConstraints.anchor = GridBagConstraints.WEST;
        storeTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                storeTextFieldActionPerformed(e);
            }
        });
        this.add(storeTextField, gridConstraints);

        noteLabel.setForeground(Color.BLACK);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 5;
        gridConstraints.insets = new Insets(10, 10, 0, 10);
        gridConstraints.anchor = GridBagConstraints.EAST;
        this.add(noteLabel, gridConstraints);

        noteTextField.setPreferredSize(new Dimension(400, 25));
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 5;
        gridConstraints.gridwidth = 5;
        gridConstraints.insets = new Insets(10, 0, 0, 10);
        gridConstraints.anchor = GridBagConstraints.WEST;
        noteTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                noteTextFieldActionPerformed(e);
            }
        });
        this.add(noteTextField, gridConstraints);

        photoLabel.setText("Photo");
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 6;
        gridConstraints.insets = new Insets(10, 10, 0, 10);
        gridConstraints.anchor = GridBagConstraints.EAST;
        this.add(photoLabel, gridConstraints);

        photoTextArea.setPreferredSize(new Dimension(350, 35));
        photoTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
        photoTextArea.setEditable(false);
        photoTextArea.setBackground(new Color(0xFFFFA8));
        photoTextArea.setFocusable(false);
        photoTextArea.setLineWrap(true);
        photoTextArea.setWrapStyleWord(true);
        photoTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 6;
        gridConstraints.gridwidth = 4;
        gridConstraints.insets = new Insets(10, 0, 0, 10);
        gridConstraints.anchor = GridBagConstraints.WEST;
        this.add(photoTextArea, gridConstraints);

        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 6;
        gridConstraints.gridy = 6;
        gridConstraints.insets = new Insets(10, 0, 0, 10);
        gridConstraints.anchor = GridBagConstraints.EAST;
        this.add(photoButton, gridConstraints);
        photoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                photoButtonActionPerformed(e);
            }
        });

        searchPanel.setPreferredSize(new Dimension(240, 160));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Item Search"));
        searchPanel.setLayout(new GridBagLayout());
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 7;
        gridConstraints.gridwidth = 3;
        gridConstraints.insets = new Insets(10, 0, 10, 0);
        gridConstraints.anchor = GridBagConstraints.CENTER;
        this.add(searchPanel, gridConstraints);

        int x = 0, y = 0;

        for (int i = 0; i < 26; i++) {

            searchButton[i] = new JButton(String.valueOf((char) (65 + i)));
            searchButton[i].setFont(new Font("Arial", Font.BOLD, 12));
            searchButton[i].setMargin(new Insets(-10, -10, -10, -10));
            sizeButton(searchButton[i], new Dimension(37, 27));
            searchButton[i].setBackground(Color.YELLOW);
            searchButton[i].setFocusable(false);
            gridConstraints = new GridBagConstraints();
            gridConstraints.gridx = x;
            gridConstraints.gridy = y;
            searchButton[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    searchButtonActionPerformed(e);
                }
            });
            searchPanel.add(searchButton[i], gridConstraints);

            x++;
            if (x % 6 == 0) {
                x = 0;
                y++;
            }
        }

        photoPanel.setPreferredSize(new Dimension(240, 160));
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 4;
        gridConstraints.gridy = 7;
        gridConstraints.gridwidth = 3;
        gridConstraints.insets = new Insets(10, 0, 10, 10);
        gridConstraints.anchor = GridBagConstraints.CENTER;
        this.add(photoPanel, gridConstraints);

        int n;
        try {

            BufferedReader inputFile = new BufferedReader(
                    new FileReader("HomeJava Projects/HomeInventory/inventory.txt"));
            numberEntries = Integer.valueOf(inputFile.readLine()).intValue();

            if (numberEntries != 0) {
                for (int i = 0; i < numberEntries; i++) {
                    myInventory[i] = new InvetoryItem();
                    myInventory[i].description = inputFile.readLine();
                    myInventory[i].location = inputFile.readLine();
                    myInventory[i].serialNumber = inputFile.readLine();
                    myInventory[i].marked = Boolean.valueOf(inputFile.readLine()).booleanValue();
                    myInventory[i].purchasePrice = inputFile.readLine();
                    myInventory[i].purchaseDate = inputFile.readLine();
                    myInventory[i].purchaseLocation = inputFile.readLine();
                    myInventory[i].note = inputFile.readLine();
                    myInventory[i].photoFile = inputFile.readLine();
                }
            }
            n = Integer.valueOf(inputFile.readLine()).intValue();
            if (n != 0) {
                for (int i = 0; i < n; i++) {
                    locationComboBox.addItem(inputFile.readLine());
                }
            }
            inputFile.close();
            currentEntry = 1;
            showEntry(currentEntry);

        } catch (Exception e) {
            numberEntries = 0;
            currentEntry = 0;
        }
        if (numberEntries == 0) {
            newButton.setEnabled(false);
            deleteButton.setEnabled(false);
            nextButton.setEnabled(false);
            previousButton.setEnabled(false);
            printButton.setEnabled(false);
            cancelButton.setEnabled(false);
        }

        this.pack();
    }

    private void itemTextFieldActionPerformed(ActionEvent e) {
        locationComboBox.requestFocus();
    }

    private void locationComboBoxActionPerformed(ActionEvent e) {

        if (locationComboBox.getItemCount() != 0) {
            for (int i = 0; i < locationComboBox.getItemCount(); i++) {
                if (locationComboBox.getSelectedItem().toString().equals(locationComboBox.getItemAt(i))) {
                    serialTextField.requestFocus();
                    return;
                }
            }
        }
        locationComboBox.addItem(locationComboBox.getSelectedItem().toString());
        serialTextField.requestFocus();
    }

    private void serialTextFieldActionPerformed(ActionEvent e) {
        priceTextField.requestFocus();
    }

    private void priceTextFieldActionPerformed(ActionEvent e) {
        dateDateChooser.requestFocus();
    }

    private void dateDateChooserPropertyChange(PropertyChangeEvent e) {
        storeTextField.requestFocus();
    }

    private void noteTextFieldActionPerformed(ActionEvent e) {
        photoButton.requestFocus();
    }

    private void storeTextFieldActionPerformed(ActionEvent e) {
        noteTextField.requestFocus();
    }

    private void searchButtonActionPerformed(ActionEvent e) {
        int i;
        if (numberEntries == 0) {
            return;
        }
        String letterClicked = e.getActionCommand();
        i = 0;
        do {
            if (myInventory[i].description.substring(0, 1).equals(letterClicked)) {
                currentEntry = i + 1;
                showEntry(currentEntry);
                return;
            }
            i++;
        } while (i < numberEntries);
        JOptionPane.showConfirmDialog(null, "No " + letterClicked + " inventory items.", "None Found",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
    }

    private void photoButtonActionPerformed(ActionEvent e) {
        JFileChooser openChooser = new JFileChooser();
        openChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        openChooser.setDialogTitle("Open Photo file");
        openChooser.setCurrentDirectory((new File("HomeJava Projects/HomeInventory/inventoryPhotos")));
        openChooser.addChoosableFileFilter(new FileNameExtensionFilter("Photo Files", ".jpg"));
        if (openChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            showPhoto(openChooser.getSelectedFile().toString());
    }

    private void sizeButton(JButton b, Dimension d) {
        b.setPreferredSize(d);
        b.setMinimumSize(d);
        b.setMaximumSize(d);
    }

    private void newButtonActionPerformed(ActionEvent e) {
        checkSave();
        blankValues();
        cancelButton.setEnabled(true);
    }

    private void deleteButtonActionPerformed(ActionEvent e) {
        if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this item?", "Delete Inventory Item",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) {
            return;
        }
        deleteEntry(currentEntry);
        if (numberEntries == 0) {
            currentEntry = 0;
            blankValues();
        } else {
            currentEntry--;
            if (currentEntry == 0) {
                currentEntry = 1;
            }
            showEntry(currentEntry);
        }
    }

    private void cancelButtonActionPerformed(ActionEvent e){
        if (JOptionPane.showConfirmDialog(null, "Are you sure you don't want to save this item?", "Cancel Inventory Item",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) {
            return;
        }
        blankValues();
        showEntry(currentEntry);
        if (numberEntries < maximumEntries)
            newButton.setEnabled(true);
        else
            newButton.setEnabled(false);
        deleteButton.setEnabled(true);
        printButton.setEnabled(true);
    }

    private void saveButtonActionPerformed(ActionEvent e) {

        itemTextField.setText(itemTextField.getText().trim());

        if (itemTextField.getText().equals("")) {
            JOptionPane.showConfirmDialog(null, "Must have item description.", "Error",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            itemTextField.requestFocus();
            return;
        }
        if (newButton.isEnabled()) {
            deleteEntry(currentEntry);
        }
        String s = itemTextField.getText();
        itemTextField.setText(s.substring(0, 1).toUpperCase() + s.substring(1));
        numberEntries++;
        currentEntry = 1;
        if (numberEntries != 0) {
            do {
                if (itemTextField.getText().compareTo(myInventory[currentEntry - 1].description) < 0) {
                    break;
                }
                currentEntry++;
            } while (currentEntry < numberEntries);
        }
        if (currentEntry != numberEntries) {
            for (int i = numberEntries; i >= currentEntry + 1; i--) {
                myInventory[i - 1] = myInventory[i - 2];
                myInventory[i - 2] = new InvetoryItem();
            }
        }
        myInventory[currentEntry - 1] = new InvetoryItem();
        myInventory[currentEntry - 1].description = itemTextField.getText();
        myInventory[currentEntry - 1].location = locationComboBox.getSelectedItem().toString();
        myInventory[currentEntry - 1].marked = markedCheckBox.isSelected();
        myInventory[currentEntry - 1].serialNumber = serialTextField.getText();
        myInventory[currentEntry - 1].purchasePrice = priceTextField.getText();
        myInventory[currentEntry - 1].purchaseDate = dateToString(dateDateChooser.getDate());
        myInventory[currentEntry - 1].purchaseLocation = storeTextField.getText();
        myInventory[currentEntry - 1].photoFile = photoTextArea.getText();
        myInventory[currentEntry - 1].note = noteTextField.getText();
        showEntry(currentEntry);
        if (numberEntries < maximumEntries)
            newButton.setEnabled(true);
        else
            newButton.setEnabled(false);
        deleteButton.setEnabled(true);
        printButton.setEnabled(true);

    }

    private void previousButtonActionPerformed(ActionEvent e) {
        checkSave();
        currentEntry--;
        showEntry(currentEntry);
    }

    private void nextButtonActionPerformed(ActionEvent e) {
        checkSave();
        currentEntry++;
        showEntry(currentEntry);
    }

    private void printButtonActionPerformed(ActionEvent e) {
        lastPage = (int) (1 + (numberEntries - 1) / entriesPerPage);
        PrinterJob inventoryPrinterJob = PrinterJob.getPrinterJob();
        inventoryPrinterJob.setPrintable(new InventoryDocument());
        if (inventoryPrinterJob.printDialog()){
        try {
            inventoryPrinterJob.print();
        } catch (PrinterException ex){
            JOptionPane.showConfirmDialog(null, ex.getMessage(), "Print Error",JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        }
        }
    }

    private void exitButtonActionPerformed(ActionEvent e) {

        exitForm(null);
    }

    private void checkSave() {
        boolean edited = false;
        if (!myInventory[currentEntry - 1].description.equals(itemTextField.getText())) {
            edited = true;
        } else if (!myInventory[currentEntry - 1].location.equals(locationComboBox.getSelectedItem().toString())) {
            edited = true;
        } else if (myInventory[currentEntry - 1].marked != markedCheckBox.isSelected()) {
            edited = true;
        } else if (!myInventory[currentEntry - 1].serialNumber.equals(serialTextField.getText())) {
            edited = true;
        } else if (!myInventory[currentEntry - 1].purchasePrice.equals(priceTextField.getText())) {
            edited = true;
        } else if (!myInventory[currentEntry -
                1].purchaseDate.equals(dateToString(dateDateChooser.getDate()))) {
            edited = true;
        } else if (!myInventory[currentEntry -
                1].purchaseLocation.equals(storeTextField.getText())) {
            edited = true;
        } else if (!myInventory[currentEntry - 1].note.equals(noteTextField.getText())) {
            edited = true;
        } else if (!myInventory[currentEntry - 1].photoFile.equals(photoTextArea.getText())) {
            edited = true;
        }
        if (edited) {
            if (JOptionPane.showConfirmDialog(null, "You have edited this item. Do you want to save the changes?",
                    "Save Item", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                saveButton.doClick();
            }
        }
    }

    private void exitForm(WindowEvent evt) {

        if (JOptionPane.showConfirmDialog(null, "Any unsaved changes will be lost.\nAre you sure you want to exit?",
                "Exit Program", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) {
            return;
        }

        try {
            PrintWriter outputFile = new PrintWriter(
                    new BufferedWriter(new FileWriter("HomeJava Projects/HomeInventory/inventory.txt")));
            outputFile.println(numberEntries);
            if (numberEntries != 0) {
                for (int i = 0; i < numberEntries; i++) {
                    outputFile.println(myInventory[i].description);
                    outputFile.println(myInventory[i].location);
                    outputFile.println(myInventory[i].serialNumber);
                    outputFile.println(myInventory[i].marked);
                    outputFile.println(myInventory[i].purchasePrice);
                    outputFile.println(myInventory[i].purchaseDate);
                    outputFile.println(myInventory[i].purchaseLocation);
                    outputFile.println(myInventory[i].note);
                    outputFile.println(myInventory[i].photoFile);
                }
            }
            outputFile.println(locationComboBox.getItemCount());
            if (locationComboBox.getItemCount() != 0) {
                for (int i = 0; i < locationComboBox.getItemCount(); i++) {
                    outputFile.println(locationComboBox.getItemAt(i));
                }
            }
            outputFile.close();
        } catch (Exception e) {

        }
        System.exit(0);
    }

    private void blankValues() {
        newButton.setEnabled(false);
        deleteButton.setEnabled(false);
        saveButton.setEnabled(true);
        previousButton.setEnabled(false);
        nextButton.setEnabled(false);
        printButton.setEnabled(false);
        itemTextField.setText("");
        locationComboBox.setSelectedItem("");
        markedCheckBox.setSelected(false);
        serialTextField.setText("");
        priceTextField.setText("");
        dateDateChooser.setDate(new Date());
        storeTextField.setText("");
        noteTextField.setText("");
        photoTextArea.setText("");
        photoPanel.repaint();
        itemTextField.requestFocus();
    }

    private void showEntry(int j) {
        itemTextField.setText(myInventory[j - 1].description);
        locationComboBox.setSelectedItem(myInventory[j - 1].location);
        markedCheckBox.setSelected(myInventory[j - 1].marked);
        serialTextField.setText(myInventory[j - 1].serialNumber);
        priceTextField.setText(myInventory[j - 1].purchasePrice);
        dateDateChooser.setDate(stringToDate(myInventory[j - 1].purchaseDate));
        storeTextField.setText(myInventory[j - 1].purchaseLocation);
        noteTextField.setText(myInventory[j - 1].note);
        showPhoto(myInventory[j - 1].photoFile);

        nextButton.setEnabled(true);
        cancelButton.setEnabled(false);
        previousButton.setEnabled(true);
        if (j == 1) {
            previousButton.setEnabled(false);
        }
        if (j == numberEntries) {
            nextButton.setEnabled(false);
        }

        itemTextField.requestFocus();
    }

    private void deleteEntry(int j) {
        if (j != numberEntries) {
            for (int i = j; i < numberEntries; i++) {
                myInventory[i - 1] = new InvetoryItem();
                myInventory[i - 1] = myInventory[i];
            }
        }
        numberEntries--;
    }

    private Date stringToDate(String s) {
        SimpleDateFormat format=new SimpleDateFormat("MM/dd/yyyy");
        Date date;
        try {
            date = format.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
            date = Calendar.getInstance().getTime();
        }
        
        return date;    
    }

    private String dateToString(Date dd) {
        DateFormat df=new SimpleDateFormat("MM/dd/yyyy");
        String s=df.format(dd);
        return s;
    }

    private void showPhoto(String photoFile) {
        if (!photoFile.equals("")) {
            try {
                photoTextArea.setText(photoFile);
            } catch (Exception ex) {
                photoTextArea.setText("");
            }
        } else {
            photoTextArea.setText("");
        }
        photoPanel.repaint();
    }

    public static void main(String[] args) {
        new HomeInventory();
    }
}
