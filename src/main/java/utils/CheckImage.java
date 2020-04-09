package utils;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;

public class CheckImage {
//    OutputStream os
    public static String createImage(OutputStream os) throws IOException {
        String res="";
        int width=100;
        int height=50;
        BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics = image.createGraphics();//画笔对象
        graphics.setColor(Color.PINK);
        graphics.fillRect(0,0,width-1,height-1);
        graphics.setColor(Color.BLUE);
        graphics.drawRect(0,0,width-1,height-1);
        String str="1234567890";
        for(int i=0;i<26;i++){
            str+=(char)('a'+i);
            str+=(char)('A'+i);
        }
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(str.length());
            res+=str.charAt(index) + "";
            graphics.drawString(str.charAt(index) + "", width/5 * (i + 1), height/2);
        }
        //干扰线
        graphics.setColor(Color.green);
        for (int i = 0; i < 10; i++) {
            int x1=random.nextInt(width);
            int x2=random.nextInt(width);
            int y1=random.nextInt(height);
            int y2=random.nextInt(height);
            graphics.drawLine(x1,y1,x2,y2);
        }

//        ImageIO.write(image,"jpg",os);
        return res;
    }
}
