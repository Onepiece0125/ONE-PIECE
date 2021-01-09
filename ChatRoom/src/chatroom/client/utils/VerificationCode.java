package chatroom.client.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 随机验证码类：
 *
 * @description: 生成随机验证码和图片
 * @author: Vcatory
 * @date: 2020-12-20 11:27
 */
public class VerificationCode {

    private StringBuilder code;     // 验证码
    private BufferedImage image;    // 图片

    public VerificationCode() {
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
        String codes = "0123456789" + "ABCDEFGHIJKLNMOPQRSTUVWXYZ" + "abcdefghijklnopqrstuvwxyz";
        // 生成随机验证码
        code = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            char rand = codes.charAt(random.nextInt(codes.length() - 1));
            code.append(rand);
        }
        g.drawString(code.toString(), 15, 24);
        // 关闭窗体
        g.dispose();
    }

    public BufferedImage getImage() {
        return image;
    }

    public String getCode() {
        return code.toString();
    }

}
