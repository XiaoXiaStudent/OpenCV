package com.opencv;

import org.junit.jupiter.api.Test;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.opencv.highgui.HighGui.imshow;
import static org.opencv.highgui.HighGui.waitKey;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;
import static org.opencv.imgproc.Imgproc.COLOR_RGB2GRAY;
import static org.opencv.imgproc.Imgproc.cvtColor;

@SpringBootTest
class OpenCvJavaApplicationTests {

        @Test
        void contextLoads() throws Exception {

                // 解决awt报错问题
                System.setProperty("java.awt.headless", "false");
                System.out.println(System.getProperty("java.library.path"));
                // 加载动态库
                URL url = ClassLoader.getSystemResource("opencv_java480.dll");
                System.load(url.getPath());
                // 读取图像
                Mat image = imread("C:\\Users\\86159\\Desktop\\openCV\\1.jpg");
                if (image.empty()) {
                        throw new Exception("image is empty");
                }

                // 先缩放原图像
                Imgproc.resize(image, image, new Size(), 0.6, 0.6, Imgproc.INTER_LINEAR);
                //imshow("Original Image", image);

                // 创建输出单通道图像，并转换为灰度图
                Mat grayImage = new Mat();
                cvtColor(image, grayImage, Imgproc.COLOR_RGB2GRAY);

                // 然后缩放灰度图像
                Imgproc.resize(grayImage, grayImage, new Size(), 0.6, 0.6, Imgproc.INTER_LINEAR);

                // 应用高斯模糊去除噪声
                Imgproc.GaussianBlur(grayImage, grayImage, new Size(5, 5), 0);

                // 应用Canny边缘检测
                Mat edges = new Mat();
                Imgproc.Canny(grayImage, edges, 75, 200);

                // 找轮廓
                List<MatOfPoint> contours = new ArrayList<>();
                Mat hierarchy = new Mat();
                Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

                // 在原始图像上绘制轮廓
                for (int i = 0; i < contours.size(); i++) {
                        Imgproc.drawContours(grayImage, contours, i, new Scalar(0, 255, 0), 2);
                }

                imshow("Processed Image", grayImage);
                imwrite("C:\\Users\\86159\\Desktop\\openCV\\2.png", grayImage);
                waitKey();




        }

        static {
                // 解决awt报错问题
                System.setProperty("java.awt.headless", "false");

                // 加载动态库
                URL url = ClassLoader.getSystemResource("opencv_java480.dll");
                System.load(url.getPath());

        }

        @Test
        void heard(){



                // 读取图像
                Mat image = Imgcodecs.imread("C:\\Users\\86159\\Desktop\\openCV\\1.jpg");
                if (image.empty()) {
                        System.out.println("Image not found!");
                        return;
                }


                // 加载面部检测的级联分类器
                CascadeClassifier faceDetector = new CascadeClassifier("D:\\opencv\\sources\\data\\lbpcascades\\lbpcascade_frontalface_improved.xml");
                // 如果使用其他模型，请确保更改上面的路径为您的haarcascade文件路径

                // 检测面部
                MatOfRect faceDetections = new MatOfRect();
                faceDetector.detectMultiScale(image, faceDetections);

                // 循环在每个检测到的面部周围绘制正方形
                for (Rect rect : faceDetections.toArray()) {
                        Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0),2);
                }

                // 先缩放原图像
                Imgproc.resize(image, image, new Size(), 0.6, 0.6, Imgproc.INTER_LINEAR);

                // 显示图像
                imshow("Detected Faces", image);

                // 保存图像
                Imgcodecs.imwrite("C:\\Users\\86159\\Desktop\\openCV\\detected_faces.jpg", image);

                // 等待任意键以关闭窗口
                waitKey();

        }




}
