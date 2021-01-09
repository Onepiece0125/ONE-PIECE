package chatroom.test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

/**
 * 测试类：
 *
 * @description: 测试随机验证码
 * @author: Vcatory
 * @date: 2020-12-20 9:32
 */

public final class Demo_02 {

    private StringBuilder code;
    private BufferedImage image;

    public Demo_02() {
        generateCodeImage();
    }

    private void generateCodeImage() {
        // 指定图片的默认大小
        int defWidth = 80;
        int defHeight = 30;
        // 生成一张图片
        image = new BufferedImage(defWidth, defHeight, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // 填充背景颜色
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, defWidth, defHeight);
        // 画出边界
        g.setColor(Color.black);
        g.drawRect(0, 0, defWidth - 1, defHeight - 1);
        // 设置字体
        g.setFont(new Font("Times New Roman", Font.ITALIC, 24));
        // 创建随机函数对象
        Random random = new Random();
        // 通过画出椭圆，制造混淆
        for (int i = 0; i < 100; i++) {
            g.drawOval(random.nextInt(defWidth), random.nextInt(defHeight), 0, 0);
        }
        // 随机验证码范围
        String codes = new StringBuilder().append("0123456789").append("ABCDEFGHIJKLNMOPQRSTUVWXYZ")
                .append("abcdefghijklnopqrstuvwxyz").toString();
        // 生成随机验证码
        code = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            char rand = codes.charAt(random.nextInt(codes.length() - 1));
            code.append(rand);
        }
        g.drawString(code.toString(),15,24);
        // 关闭窗体
        g.dispose();
    }

    public BufferedImage getImage() {
        return image;
    }

    public String getCode() {
        return code.toString();
    }

    public static void main(String[] args) throws Exception {
        File imgFile = new File("C:\\Users\\Vcatory\\Desktop\\TestFiles\\ChatRoom\\codeImage3.jpeg");
        Demo_02 cig = new Demo_02();
        ImageIO.write(cig.getImage(), "JPEG", imgFile);
        System.out.println(cig.getCode());
    }
}
