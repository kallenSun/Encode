import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;

import org.jasypt.properties.PropertyValueEncryptionUtils;
import org.jasypt.util.text.BasicTextEncryptor;

import com.sun.deploy.util.StringUtils;

public class CodeUI extends JFrame {


    private static String salt = "lybgeek";

    private static BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();

    private static JPanel saltPanel;

    private static JPanel encoderPanel;

    private static JTextArea mainArea;

    private static Container contain;

    static {
        basicTextEncryptor.setPassword(salt);
    }

    public CodeUI() {
        super("编码");
        contain = getContentPane();
        contain.setLayout(new BorderLayout(10,20));

        saltPanel = new JPanel();
        encoderPanel = new JPanel();
        saltPanel.setLayout(new FlowLayout());
        encoderPanel.setLayout(new FlowLayout());
        mainArea = new JTextArea(10,30);

        // 盐值模块
        addSaltPanel();

        addEncodePanel();

        contain.add(saltPanel, BorderLayout.NORTH);
        contain.add(encoderPanel, BorderLayout.CENTER);
        contain.add(mainArea, BorderLayout.SOUTH);

        setSize(500, 300);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
    }

    public static void main(String[] args) {
        CodeUI codeUi = new CodeUI();
    }

    /**
     * 明文加密
     */
    public static String encode(String plaintext) {
        return basicTextEncryptor.encrypt(plaintext);
    }

    /**
     * 解密
     */
    public static String decode(String ciphertext) {
        ciphertext = "ENC(" + ciphertext + ")";
        try {
            if (PropertyValueEncryptionUtils.isEncryptedValue(ciphertext)) {
                return PropertyValueEncryptionUtils.decrypt(ciphertext, basicTextEncryptor);
            }
        } catch (Exception e) {
            System.out.println("解密失败");
            e.printStackTrace();
        }
        return "";
    }

    public void addSaltPanel() {
        saltPanel = new JPanel();

        JLabel saltLabel = new JLabel("盐值：", SwingConstants.LEFT);
        saltLabel.setSize(40,10);

        JTextField saltTextField = new JTextField(40);
        saltTextField.setText("lybgeek");
        JButton saltButton = new JButton("确定");

        saltButton.addActionListener((ActionEvent ae) ->{
            salt = saltTextField.getText();
            basicTextEncryptor.setPassword(salt);
            System.out.println("已经确定盐值:" + " "+ salt );
        });

        saltPanel.add(saltLabel);
        saltPanel.add(saltTextField);
        saltPanel.add(saltButton);
    }

    public void addEncodePanel() {
        JLabel encodeLabel = new JLabel("加密/解密文本：", SwingConstants.LEFT);
        encodeLabel.setSize(50,10);
        JTextField encodeTextField = new JTextField(40);
        JButton encodeButton = new JButton("确定");

        // 模式选择器
        ButtonGroup group = new ButtonGroup();
        JRadioButton encodeMode = new JRadioButton("加密", true);
        JRadioButton decodeMode = new JRadioButton("解密", false);
        group.add(encodeMode);
        group.add(decodeMode);

        encoderPanel.add(encodeMode);
        encoderPanel.add(decodeMode);
        encoderPanel.add(encodeLabel);
        encoderPanel.add(encodeTextField);
        encoderPanel.add(encodeButton);

        encodeButton.addActionListener((ActionEvent ae)-> {
            String str = null;
            if (encodeMode.isSelected()) {
                String text = encodeTextField.getText();
                str = encode(text);
                System.out.println("加密结果:" + " " + str );
            } else {
                String text = encodeTextField.getText();
                str = decode(text);
                if (str == null || str.length() == 0) {
                    str = "解密失败";
                } else {
                    System.out.println("解密结果:" + " " + str );
                }
            }
            mainArea.setText(str);
        });
    }


}
